package me.xleiten.rebalance.api.config;

import com.google.gson.*;
import me.xleiten.rebalance.api.config.types.*;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DynamicStorageLoader
{
    private final DynamicStorage config;
    private final Path file;
    private final Path dir;

    public final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Section.class, new ConfigAdapter())
            .setPrettyPrinting()
            .create();

    final ConcurrentHashMap<String, IType<?, ?>> typeAdapters = new ConcurrentHashMap<>(
            Set.of(new BoolType(), new StringType(), new IntType(), new LongType(), new ShortType(), new DoubleType(), new FloatType())
                    .stream()
                    .collect(Collectors.toMap(IType::getTypeKey, Function.identity()))
    );

    DynamicStorageLoader(@NotNull DynamicStorage config, @Nullable Path dir) {
        this.config = config;
        this.dir = dir;
        var name = config.name + ".json";
        this.file = dir == null ? Path.of(name) : dir.resolve(name);
    }

    void addType(@NotNull IType<?, ?> type) {
        var t = typeAdapters.get(type.getTypeKey());
        if (t != null) {
            if (!t.getTypeKey().equals(type.getTypeKey()))
                throw new IllegalArgumentException("Type with this key is already exist and has different type");
        } else
            typeAdapters.put(type.getTypeKey(), type);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private IType<Object, JsonElement> getTypeAdapterFor(@NotNull Object object) {
        for (Map.Entry<String, IType<?, ?>> entry : typeAdapters.entrySet()) {
            if (entry.getValue().getTypeClass() == object.getClass())
                return (IType<Object, JsonElement>) entry.getValue();
        }
        return null;
    }

    public void load(Consumer<ActionResult> callback) {
        ActionResult result;
        try {
            if (Files.isReadable(file)) {
                Section data = gson.fromJson(Files.newBufferedReader(file), Section.class);
                if (data != null) {
                    config.root.copyFrom(data);
                }
            }
            result = ActionResult.SUCCESS;
        } catch (Exception exception) {
            config.logger.error("config load error.", exception);
            result = ActionResult.FAIL;
        }
        if (callback != null)
            callback.accept(result);
    }

    public void load() {
        load(null);
    }

    public void save(Consumer<ActionResult> callback) {
        ActionResult result;
        try {
            if (dir != null && !Files.exists(dir)) Files.createDirectories(dir);
            var writer = Files.newBufferedWriter(file);
            writer.write(gson.toJson(config.root));
            writer.flush();
            writer.close();
            result = ActionResult.SUCCESS;
        } catch (Exception exception) {
            config.logger.error("config save error.", exception);
            result = ActionResult.FAIL;
        }
        if (callback != null)
            callback.accept(result);
    }

    public void save() {
        save(null);
    }

    private class ConfigAdapter implements JsonSerializer<Section>, JsonDeserializer<Section>
    {
        @SuppressWarnings("unchecked")
        @Override
        public Section deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var section = new Section(config);
            if (json.isJsonObject()) {
                for (Map.Entry<String, JsonElement> entry: json.getAsJsonObject().entrySet()) {
                    var key = entry.getKey();
                    var value = entry.getValue();
                    if (!key.isEmpty()) {
                        if (!key.contains(":")) {
                            if (value.isJsonObject())
                                section.entries.put(key, context.deserialize(value, Section.class));
                            else
                                config.logger.warn("Unknown type of config entry '" + key + "'. Skipping...");
                        } else {
                            var optionInfo = key.split(":", 2);
                            var type = (IType<Object, JsonElement>) typeAdapters.get(optionInfo[0]);
                            if (type != null) {
                                if (optionInfo.length >= 2) section.entries.put(optionInfo[1], new Option<>(optionInfo[1], type.deserialize(value, context), config));
                                else config.logger.warn("An option with an empty key is detected. It will be skipped.");
                            } else
                                config.logger.warn("Unknown type of config entry '" + key + "'. Skipping...");
                        }
                    } else
                        config.logger.warn("An option with an empty key is detected. Skipping...");
                }
            }
            return section;
        }

        @Override
        public JsonElement serialize(Section src, Type typeOfSrc, JsonSerializationContext context) {
            var object = new JsonObject();
            for (Map.Entry<String, DynamicStorageEntry> entry: src.entries.entrySet()) {
                var key = entry.getKey();
                var value = entry.getValue();
                if (value instanceof Option<?> opt) {
                    var type = getTypeAdapterFor(opt.getValue());
                    if (type != null) {
                        /*if (opt.isValidated())*/
                            object.add(type.getTypeKey() + ":" + key, type.serialize(opt.getValue(), context));
                        /*else
                            config.logger.warn("An option with key '" + key + "' is not validated. Skipping...");*/
                    } else
                        config.logger.warn("Unable to find a type with the key '" + key + "' for the option. Skipping...");
                } else {
                    if (value instanceof Section section) {
                        object.add(key, context.serialize(section));
                    } else
                        config.logger.warn("Unknown type of config entry '" + key + "'. Class - " + value.getClass().getSimpleName());
                }
            }
            return object;
        }
    }
}
