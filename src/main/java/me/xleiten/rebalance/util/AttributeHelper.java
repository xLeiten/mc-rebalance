package me.xleiten.rebalance.util;

import me.xleiten.rebalance.api.game.world.entity.mob.attribute.AttributeModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class AttributeHelper
{
    public static boolean hasModifier(EntityAttributeInstance instance, @NotNull String name) {
        if (instance != null) {
            for (EntityAttributeModifier mod: instance.getModifiers())
                if (Objects.equals(((AttributeModifier) mod).cringeMod$getName(), name))
                    return true;
        }
        return false;
    }

    public static boolean hasModifier(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull String name) {
        return hasModifier(entity.getAttributeInstance(attribute), name);
    }

    public static boolean hasModifier(EntityAttributeInstance instance, @NotNull UUID id) {
        if (instance != null) {
            return instance.getModifier(id) != null;
        }
        return false;
    }


    public static boolean hasModifier(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull UUID id) {
        return hasModifier(entity.getAttributeInstance(attribute), id);
    }

    @Nullable
    public static EntityAttributeModifier getModifier(EntityAttributeInstance instance, @NotNull String name) {
        if (instance != null) {
            for (EntityAttributeModifier mod: instance.getModifiers())
                if (Objects.equals(((AttributeModifier) mod).cringeMod$getName(), name))
                    return mod;
        }
        return null;
    }

    @Nullable
    public static EntityAttributeModifier getModifier(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull String name) {
        return getModifier(entity.getAttributeInstance(attribute), name);
    }

    @Nullable
    public static EntityAttributeModifier getModifier(EntityAttributeInstance instance, @NotNull UUID id) {
        if (instance != null) {
            return instance.getModifier(id);
        }
        return null;
    }

    @Nullable
    public static EntityAttributeModifier getModifier(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull UUID id) {
        return getModifier(entity.getAttributeInstance(attribute), id);
    }

    public static List<String> getModifiersNames(EntityAttributeInstance instance) {
        return instance == null ?
                List.of() : instance.getModifiers().stream().map(mod -> ((AttributeModifier) mod).cringeMod$getName()).toList();
    }

    public static List<String> getModifiersNames(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute) {
        return getModifiersNames(entity.getAttributeInstance(attribute));
    }

    public static boolean removeModifier(EntityAttributeInstance instance, @NotNull String name) {
        if (instance != null) {
            var modifier = getModifier(instance, name);
            if (modifier != null) {
                instance.removeModifier(modifier.getId());
                return true;
            }
        }
        return false;
    }

    public static boolean removeModifier(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull String name) {
        return removeModifier(entity.getAttributeInstance(attribute), name);
    }

    public static double getValue(EntityAttributeInstance instance, double defaultValue) {
        return instance == null ? defaultValue : instance.getValue();
    }

    public static double getValue(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, double defaultValue) {
        return getValue(entity.getAttributeInstance(attribute), defaultValue);
    }

    public static double getBaseValue(EntityAttributeInstance instance, double defaultValue) {
        return instance == null ? defaultValue : instance.getBaseValue();
    }

    public static double getBaseValue(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, double defaultValue) {
        return getBaseValue(entity.getAttributeInstance(attribute), defaultValue);
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, double value) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null)
            instance.setBaseValue(value);
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, double value, @NotNull Predicate<EntityAttributeInstance> predicate) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null && predicate.test(instance))
            instance.setBaseValue(value);
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull Function<Double, Double> ofBase) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null)
            instance.setBaseValue(ofBase.apply(instance.getBaseValue()));
    }

    public static void setBaseValue(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull Function<Double, Double> ofBase, @NotNull Predicate<EntityAttributeInstance> predicate) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null && predicate.test(instance))
            instance.setBaseValue(ofBase.apply(instance.getBaseValue()));
    }

    public static void addModifier(EntityAttributeInstance instance, @NotNull Modifier modifier) {
        if (instance != null) {
            var oldModifier = getModifier(instance, modifier.name);
            if (oldModifier != null) {
                if (modifier.overwrite)
                    instance.removeModifier(oldModifier.getId());
            }
            var newModifier = modifier.convert();
            if (modifier.persistent)
                instance.addPersistentModifier(newModifier);
            else
                instance.addTemporaryModifier(newModifier);
        }
    }

    public static void addModifier(@NotNull LivingEntity entity, EntityAttribute attribute, Modifier modifier) {
        addModifier(entity.getAttributeInstance(attribute), modifier);
    }

    public static boolean changeModifier(EntityAttributeInstance instance, @NotNull UUID id, @NotNull Function<Double, Double> value) {
        if (instance != null) {
            var modifier = instance.getModifier(id);
            if (modifier != null) {
                ((AttributeModifier) modifier).cringeMod$setValue(value.apply(modifier.getValue()));
                instance.onUpdate();
                return true;
            }
        }
        return false;
    }

    public static boolean changeModifier(EntityAttributeInstance instance, @NotNull UUID id, double value) {
        if (instance != null) {
            var modifier = instance.getModifier(id);
            if (modifier != null) {
                ((AttributeModifier) modifier).cringeMod$setValue(value);
                instance.onUpdate();
                return true;
            }
        }
        return false;
    }

    public static boolean changeModifier(@NotNull LivingEntity entity, @NotNull EntityAttribute attribute, @NotNull UUID id, @NotNull Function<Double, Double> value) {
        return changeModifier(entity.getAttributeInstance(attribute), id, value);
    }

    public static Modifier modifier(@NotNull String name, double value) { return new Modifier(name, value); }
    public static Modifier modifier(@NotNull String name) { return new Modifier(name); }

    public static class Modifier
    {
        public final String name;

        private boolean persistent = true;
        private boolean overwrite = false;
        private UUID id;

        private EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.MULTIPLY_BASE;
        private double value = 0;

        private Modifier(String name, double value)
        {
            this.name = name;
            this.value = value;
        }

        private Modifier(String name)
        {
            this.name = name;
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
