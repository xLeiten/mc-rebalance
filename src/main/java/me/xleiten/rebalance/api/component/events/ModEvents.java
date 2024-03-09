package me.xleiten.rebalance.api.component.events;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ModEvents
{
    private static final Set<EventType<?>> EVENTS = new HashSet<>();

    public static final EventType<ModInitializeEvent> MOD = new EventType<>(ModInitializeEvent.class);

    public static ImmutableSet<EventType<?>> getRegisteredTypes() {
        return ImmutableSet.copyOf(EVENTS);
    }

    public static class EventType<T extends ModEvent>
    {
        public final Class<T> eventClass;

        EventType(@NotNull Class<T> type) {
            this.eventClass = type;
            EVENTS.add(this);
        }
    }
}
