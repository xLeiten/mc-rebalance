package me.xleiten.rebalance.api.component;

import me.xleiten.rebalance.api.component.events.*;
import me.xleiten.rebalance.api.config.DynamicStorage;
import me.xleiten.rebalance.api.config.Section;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Optional;

public abstract class Mod
{
    private final HashMap<String, Component<?>> components = new HashMap<>();
    protected final Logger logger = getLogger();
    protected final DynamicStorage storage = getStorage();

    protected final Section settings = storage.section("components");

    protected final void initialize() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> storage.save(false)));
        ServerWorldEvents.LOAD.register((server, world) -> call(ModEvents.WORLD, new ServerWorldEvent(world, server, ServerWorldEvent.WorldState.LOAD)));
        ServerWorldEvents.UNLOAD.register((server, world) -> call(ModEvents.WORLD, new ServerWorldEvent(world, server, ServerWorldEvent.WorldState.UNLOAD)));
        call(ModEvents.MOD, new ModInitializeEvent(this));
        onInitialize();
    }

    protected abstract void onInitialize();

    @NotNull public abstract DynamicStorage getStorage();

    @NotNull public abstract Logger getLogger();

    protected final <S extends Component<?>> void register(S component) {
        components.putIfAbsent(component.name, component);
    }

    public final <T extends ModEvent> void call(@NotNull ModEvents.EventType<T> type, @NotNull T event) {
        components.forEach(((s, component) -> component.call(type, event)));
    }

    public final Optional<Component<?>> getComponent(@NotNull String name) {
        return Optional.ofNullable(components.get(name));
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component<?>> Optional<T> getComponent(@NotNull Class<T> clazz) {
        for (Component<?> component: components.values())
            if (component.getClass() == clazz)
                return Optional.of((T) component);

        return Optional.empty();
    }
}
