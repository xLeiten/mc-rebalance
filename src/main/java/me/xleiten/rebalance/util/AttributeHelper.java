package me.xleiten.rebalance.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public final class AttributeHelper
{
    @Nullable
    public static EntityAttributeModifier getModifier(EntityAttributeInstance instance, @NotNull String name) {
        return instance == null ? null : instance.getModifiers().stream().filter(it -> Objects.equals(it.name(), name)).findFirst().orElse(null);
    }

    public static double getValue(EntityAttributeInstance instance, double defaultValue) {
        return instance == null ? defaultValue : instance.getValue();
    }

    public static double getValue(@NotNull LivingEntity entity, @NotNull RegistryEntry<EntityAttribute> attribute, double defaultValue) {
        return getValue(entity.getAttributeInstance(attribute), defaultValue);
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull RegistryEntry<EntityAttribute> attribute, double value) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null)
            instance.setBaseValue(value);
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull RegistryEntry<EntityAttribute> attribute, double value, @NotNull Predicate<EntityAttributeInstance> predicate) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null && predicate.test(instance))
            instance.setBaseValue(value);
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull RegistryEntry<EntityAttribute> attribute, @NotNull Function<Double, Double> ofBase) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null)
            instance.setBaseValue(ofBase.apply(instance.getBaseValue()));
    }

    public static void addModifier(EntityAttributeInstance instance, @NotNull Modifier modifier) {
        if (instance != null) {
            var mod = modifier.id == null ? getModifier(instance, modifier.name) : instance.getModifier(modifier.id);
            if (mod != null) {
                if (modifier.overwrite)
                    instance.removeModifier(mod.uuid());
                else
                    return;
            }
            mod = modifier.convert();
            if (modifier.persistent)
                instance.addPersistentModifier(mod);
            else
                instance.addTemporaryModifier(mod);
        }
    }

    public static void addModifier(@NotNull LivingEntity entity, RegistryEntry<EntityAttribute> attribute, Modifier modifier) {
        addModifier(entity.getAttributeInstance(attribute), modifier);
    }

    public static Modifier modifier(@NotNull String name, double value) { return new Modifier(name, value); }
    public static Modifier modifier(@NotNull String name) { return new Modifier(name, 0); }

    public static class Modifier
    {
        public final String name;

        private boolean persistent = true;
        private boolean overwrite = false;
        private UUID id;

        private EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE;
        private double value;

        private Modifier(String name, double value)
        {
            this.name = name;
            this.value = value;
        }

        public Modifier id(@NotNull UUID id) {
            this.id = id;
            return this;
        }

        public Modifier operation(@NotNull EntityAttributeModifier.Operation operation) {
            this.operation = operation;
            return this;
        }

        public Modifier persistent(boolean persistent) {
            this.persistent = persistent;
            return this;
        }

        public Modifier overwrite(boolean overwrite) {
            this.overwrite = overwrite;
            return this;
        }

        public Modifier value(double value) {
            this.value = value;
            return this;
        }

        public EntityAttributeModifier convert() {
            return id != null ? new EntityAttributeModifier(id, name, value, operation) : new EntityAttributeModifier(name, value, operation);
        }
    }
}
