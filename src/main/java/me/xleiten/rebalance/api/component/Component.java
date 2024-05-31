package me.xleiten.rebalance.api.component;

import com.google.common.collect.ImmutableMap;
import me.xleiten.rebalance.api.component.events.ModEvent;
import me.xleiten.rebalance.api.component.events.ModEvents;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.config.Section;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Component<T extends Mod>
{
    private final ImmutableMap<ModEvents.EventType<?>, List<Consumer<ModEvent>>> eventHandlers = ImmutableMap.copyOf(ModEvents.getRegisteredTypes().stream().collect(Collectors.toMap(Function.identity(), (it) -> new ArrayList<>())));

    public final Logger logger;
    public final Section settings;
    private final Option<Boolean> isEnabled;

    public final String name;
    public final T mod;

    public Component(@NotNull String name, @NotNull T mod) {
        this.name = name;
        this.mod = mod;
        this.settings = mod.settings.section(name);
        this.isEnabled = settings.option("enabled", true);
        this.logger = mod.getLogger();
        mod.register(this);
    }

    public final boolean isEnabled() {
        return isEnabled.value();
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <E extends ModEvent> void on(@NotNull ModEvents.EventType<E> type, @NotNull Consumer<E> action) {
        eventHandlers.get(type).add((Consumer<ModEvent>) action);
    }

    @SuppressWarnings("ConstantConditions")
    public <E extends ModEvent> void call(@NotNull ModEvents.EventType<E> type, @NotNull E event) {
        for (Consumer<ModEvent> handler: eventHandlers.get(type))
            handler.accept(event);
    }

}
