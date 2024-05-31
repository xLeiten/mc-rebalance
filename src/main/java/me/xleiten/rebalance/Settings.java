package me.xleiten.rebalance;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.config.Section;
import me.xleiten.rebalance.api.math.DoubleRange;
import me.xleiten.rebalance.api.math.FloatRange;
import me.xleiten.rebalance.api.math.IntRange;
import me.xleiten.rebalance.api.math.Range;
import me.xleiten.rebalance.core.game.world.item.ArmorConfig;

import static me.xleiten.rebalance.Rebalance.MAIN_STORAGE;

public final class Settings 
{
    // World
    public static final Section WORLD = MAIN_STORAGE.section("World");
    
    // Entities
    public static final Section ENTITIES = WORLD.section("Entities");
    public static final Section MOBS = ENTITIES.section("Mobs");

    public static final Section MOB_SPIDER = MOBS.section("Spider");
    public static final Option<IntRange> MOB_SPIDER__COBWEB_COOLDOWN = MOB_SPIDER.option("cobweb-cooldown", Range.create(15, 85));
    public static final Option<DoubleRange> MOB_SPIDER__COBWEB_DISTANCE = MOB_SPIDER.option("cobweb-distance", Range.create(0.3, 9.5));
    public static final Option<DoubleRange> MOB_SPIDER__MOVE_SPEED_BOOST = MOB_SPIDER.option("move-speed-mult", Range.create(0.3, 0.45));
    public static final Option<Double> MOB_SPIDER__COBWEB_CHANCE = MOB_SPIDER.option("cobweb-chance", 75d);
    
    public static final Section MOB_CREEPER = MOBS.section("Creeper");
    public static final Option<IntRange> MOB_CREEPER__EXPLOSION_RADIUS = MOB_CREEPER.option("explosion-radius", Range.create(3, 7));
    public static final Option<Double> MOB_CREEPER__CHARGED_CHANCE = MOB_CREEPER.option("spawn-charged-chance", 3d);
    public static final Option<Boolean> MOB_CREEPER__EXPLODE_WITH_FIRE = MOB_CREEPER.option("explode-with-fire", true);
    public static final Option<DoubleRange> MOB_CREEPER__MOVE_SPEED_MULT = MOB_CREEPER.option("move-speed-mult", Range.create(0.2, 0.4));
    public static final Option<Boolean> MOB_CREEPER__MOVE_WHILE_FUSE = MOB_CREEPER.option("move-while-fuse", true);
    
    public static final Section MOB_ZOMBIE = MOBS.section("Zombie");
    public static final Option<DoubleRange> MOB_ZOMBIE__MOVE_SPEED_MULT = MOB_ZOMBIE.option("move-speed-mult", Range.create(0.35, 0.65));
    public static final Option<DoubleRange> MOB_ZOMBIE__BABY_MOVE_SPEED_MULT = MOB_ZOMBIE.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    public static final Option<DoubleRange> MOB_ZOMBIE__FOLLOW_RANGE_MULT = MOB_ZOMBIE.option("follow-range-mult", Range.create(2.0, 3.0));
    public static final Option<DoubleRange> MOB_ZOMBIE__ZOMBIE_REINFORCEMENT = MOB_ZOMBIE.option("reinforcement-attribute-value", Range.create(0.1, 0.6));
    public static final Option<Double> MOB_ZOMBIE__SPAWN_REINFORCEMENT_CHANCE = MOB_ZOMBIE.option("can-spawn-reinforcement-chance", 40d);
    public static final Option<Integer> MOB_ZOMBIE__REINFORCEMENT_COOLDOWN = MOB_ZOMBIE.option("reinforcement-cooldown", 100);
    public static final Option<IntRange> MOB_ZOMBIE__ALERT_OTHERS_COOLDOWN = MOB_ZOMBIE.option("alert-others-cooldown", Range.create(80, 160));
    public static final Option<Double> MOB_ZOMBIE__ALERT_OTHERS_CHANCE = MOB_ZOMBIE.option("alert-others-chance", 50d);
    public static final Option<Double> MOB_ZOMBIE__SEARCH_TARGET_ON_SPAWN_CHANCE = MOB_ZOMBIE.option("search-target-on-spawn-chance", 70d);
    public static final Option<Integer> MOB_ZOMBIE__SPAWN_ADAPTED_TO_LIGHT_CHANCE = MOB_ZOMBIE.option("spawn-with-light-adaptation-chance", 3);
    public static final Option<Double> MOB_ZOMBIE__LIGHT_ADAPTATION_CHANCE = MOB_ZOMBIE.option("light-adaptation-chance", 3d);
    public static final Option<Double> MOB_ZOMBIE__BREAK_DOORS_CHANCE = MOB_ZOMBIE.option("break-doors-chance", 35d);
    public static final Option<Double> MOB_ZOMBIE__SPAWN_WITH_TOOL_CHANCE = MOB_ZOMBIE.option("spawn-with-tool-chance", 15d);
    public static final Option<Boolean> MOB_ZOMBIE__SHOULD_BURN_IN_LIGHT = MOB_ZOMBIE.option("should-burn-in-light", true);
    public static final Option<Boolean> MOB_ZOMBIE__SHOULD_ADAPT_TO_LIGHT = MOB_ZOMBIE.option("should-adapt-to-light", true);
    public static final Option<Boolean> MOB_ZOMBIE__SHOULD_ALERT_OTHERS = MOB_ZOMBIE.option("should-alert-others", true);
    
    public static final Section MOB_ZOMBIE_VILLAGER = MOBS.section("Zombie villager");
    public static final Option<DoubleRange> MOB_ZOMBIE_VILLAGER__MOVE_SPEED_MULT = MOB_ZOMBIE_VILLAGER.option("move-speed-mult", Range.create(0.35, 0.55));
    public static final Option<DoubleRange> MOB_ZOMBIE_VILLAGER__BABY_MOVE_SPEED_MULT = MOB_ZOMBIE_VILLAGER.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    public static final Option<DoubleRange> MOB_ZOMBIE_VILLAGER__FOLLOW_RANGE_MULT = MOB_ZOMBIE_VILLAGER.option("follow-range-mult", Range.create(2.0, 3.0));
    public static final Option<Double> MOB_ZOMBIE_VILLAGER__BREAK_DOORS_CHANCE = MOB_ZOMBIE_VILLAGER.option("break-doors-chance", 55d);
    public static final Option<Boolean> MOB_ZOMBIE_VILLAGER__SHOULD_ADAPT_TO_LIGHT = MOB_ZOMBIE_VILLAGER.option("should-adapt-to-light", true);
    public static final Option<Boolean> MOB_ZOMBIE_VILLAGER__SHOULD_BURN_IN_LIGHT = MOB_ZOMBIE_VILLAGER.option("should-burn-in-light", true);
    public static final Option<Boolean> MOB_ZOMBIE_VILLAGER__SHOULD_ALERT_OTHERS = MOB_ZOMBIE_VILLAGER.option("should-alert-others", true);
    public static final Option<Integer> MOB_ZOMBIE_VILLAGER__SPAWN_ADAPTED_TO_LIGHT_CHANCE = MOB_ZOMBIE_VILLAGER.option("spawn-with-light-adaptation-chance", 3);
    public static final Option<IntRange> MOB_ZOMBIE_VILLAGER__ALERT_OTHERS_COOLDOWN = MOB_ZOMBIE_VILLAGER.option("alert-others-cooldown", Range.create(80, 160));
    
    public static final Section MOB_HUSK = MOBS.section("Husk");
    public static final Option<DoubleRange> MOB_HUSK__MOVE_SPEED_MULT = MOB_HUSK.option("move-speed-mult", Range.create(0.35, 0.65));
    public static final Option<DoubleRange> MOB_HUSK__BABY_MOVE_SPEED_MULT = MOB_HUSK.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    public static final Option<DoubleRange> MOB_HUSK__FOLLOW_RANGE_MULT = MOB_HUSK.option("follow-range-mult", Range.create(2.0, 3.0));
    public static final Option<DoubleRange> MOB_HUSK__ZOMBIE_REINFORCEMENT = MOB_HUSK.option("reinforcement-attribute-value", Range.create(0.1, 0.6));
    public static final Option<Double> MOB_HUSK__SPAWN_REINFORCEMENT_CHANCE = MOB_HUSK.option("can-spawn-reinforcement-chance", 35d);
    public static final Option<Double> MOB_HUSK__BREAK_DOORS_CHANCE = MOB_HUSK.option("break-doors-chance", 30d);
    public static final Option<Boolean> MOB_HUSK__SHOULD_ADAPT_TO_LIGHT = MOB_HUSK.option("should-adapt-to-light", false);
    public static final Option<Boolean> MOB_HUSK__SHOULD_BURN_IN_LIGHT = MOB_HUSK.option("should-burn-in-light", false);
    public static final Option<Boolean> MOB_HUSK__SHOULD_ALERT_OTHERS = MOB_HUSK.option("should-alert-others", true);
    public static final Option<IntRange> MOB_HUSK__ALERT_OTHERS_COOLDOWN = MOB_HUSK.option("alert-others-cooldown", Range.create(80, 160));
    
    public static final Section MOB_DROWNED = MOBS.section("Drowned");
    public static final Option<DoubleRange> MOB_DROWNED__MOVE_SPEED_MULT = MOB_DROWNED.option("move-speed-mult", Range.create(0.15, 0.45));
    public static final Option<DoubleRange> MOB_DROWNED__BABY_MOVE_SPEED_MULT = MOB_DROWNED.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    public static final Option<DoubleRange> MOB_DROWNED__FOLLOW_RANGE_MULT = MOB_DROWNED.option("follow-range-mult", Range.create(2.0, 3.0));
    public static final Option<Double> MOB_DROWNED__BREAK_DOORS_CHANCE = MOB_DROWNED.option("break-doors-chance", 20d);
    public static final Option<Boolean> MOB_DROWNED__SHOULD_ADAPT_TO_LIGHT = MOB_DROWNED.option("should-adapt-to-light", false);
    public static final Option<Boolean> MOB_DROWNED__SHOULD_BURN_IN_LIGHT = MOB_DROWNED.option("should-burn-in-light", true);
    public static final Option<Boolean> MOB_DROWNED__SHOULD_ALERT_OTHERS = MOB_DROWNED.option("should-alert-others", true);
    public static final Option<IntRange> MOB_DROWNED__ALERT_OTHERS_COOLDOWN = MOB_DROWNED.option("alert-others-cooldown", Range.create(80, 160));

    public static final Section ENTITY_ENDCRYSTAL = ENTITIES.section("End crystal");
    public static final Option<Boolean> ENTITY_ENDCRYSTAL__EXPLODE_WITH_FIRE = ENTITY_ENDCRYSTAL.option("explode-with-fire", true);

    public static final Section ENTITY_PLAYER = ENTITIES.section("Player");
    public static final Option<Boolean> ENTITY_PLAYER__INVULNERABLE_ON_SPAWN = ENTITY_PLAYER.option("invulnerable-on-spawn", false);
    
    public static final Section MOB_ZOMBIFIED_PIGLIN = MOBS.section("Zombified piglin");
    public static final Option<DoubleRange> MOB_ZOMBIFIED_PIGLIN__MOVE_SPEED_MULT = MOB_ZOMBIFIED_PIGLIN.option("move-speed-mult", Range.create(0.25, 0.55));
    public static final Option<DoubleRange> MOB_ZOMBIFIED_PIGLIN__BABY_MOVE_SPEED_MULT = MOB_ZOMBIFIED_PIGLIN.option("baby-move-speed-mult", Range.create(0.05, 0.25));
    public static final Option<Double> MOB_ZOMBIFIED_PIGLIN__USE_AXE_CHANCE = MOB_ZOMBIFIED_PIGLIN.option("use-axe-chance", 5d);
    
    public static final Section MOB_WITCH = MOBS.section("Witch");
    public static final Option<DoubleRange> MOB_WITCH__MOVE_SPEED_MULT = MOB_WITCH.option("move-speed-mult", Range.create(0.1, 0.2));
    
    public static final Section MOB_SKELETON = MOBS.section("Skeleton");
    public static final Option<DoubleRange> MOB_SKELETON__MOVE_SPEED_MULT = MOB_SKELETON.option("move-speed-mult", Range.create(0.15, 0.35));
    public static final Option<Double> MOB_SKELETON__USE_SWORD_CHANCE = MOB_SKELETON.option("use-sword-chance", 3d);
    public static final Option<Double> MOB_SKELETON__DIAMOND_SWORD_CHANCE = MOB_SKELETON.option("diamond-sword-chance", 0.01d);
    
    public static final Section MOB_WITHER_SKELETON = MOBS.section("Wither skeleton");
    public static final Option<DoubleRange> MOB_WITHER_SKELETON__MOVE_SPEED_MULT = MOB_WITHER_SKELETON.option("move-speed-mult", Range.create(0.25, 0.45));
    public static final Option<Double> MOB_WITHER_SKELETON__NETHERITE_EQUIPMENT_CHANCE = MOB_WITHER_SKELETON.option("netherite-equipment-chance", 0.01);
    public static final Option<Double> MOB_WITHER_SKELETON__SWORD_SHARPNESS_CHANCE = MOB_WITHER_SKELETON.option("sword-sharpness-chance", 10d);
    public static final Option<Double> MOB_WITHER_SKELETON__SWORD_FIREASPECT_CHANCE = MOB_WITHER_SKELETON.option("sword-fire-aspect-chance", 10d);
    public static final Option<IntRange> MOB_WITHER_SKELETON__SWORD_SHARPNESS_LEVEL = MOB_WITHER_SKELETON.option("sword-sharpness-level", Range.create(1, 4));
    public static final Option<IntRange> MOB_WITHER_SKELETON__SWORD_FIREASPECT_LEVEL = MOB_WITHER_SKELETON.option("sword-fireaspect-level", Range.create(1, 2));
    
    public static final Section MOB_WARDEN = MOBS.section("Warden");
    public static final Option<DoubleRange> MOB_WARDEN__MOVE_SPEED_MULT = MOB_WARDEN.option("move-speed-mult", Range.create(0.15, 0.25));
    public static final Option<FloatRange> MOB_WARDEN__SONIC_BOOM_DAMAGE_MULT = MOB_WARDEN.option("sonic-boom-damage-mult", Range.create(1.5f, 2));

    public static final Section MOB_IRON_GOLEM = MOBS.section("Iron golem");
    public static final Option<Float> MOB_IRON_GOLEM__MAX_HEALTH = MOB_IRON_GOLEM.option("max-health", 250f);
    public static final Option<DoubleRange> MOB_IRON_GOLEM__MOVE_SPEED_BOOST = MOB_IRON_GOLEM.option("move-speed-boost", Range.create(0.1, 0.2));
    public static final Option<DoubleRange> MOB_IRON_GOLEM__FOLLOW_RANGE_BOOST = MOB_IRON_GOLEM.option("follow-range", Range.create(24., 32));

    public static final Section MOB_GHAST = MOBS.section("Ghast");
    public static final Option<Float> MOB_GHAST__MAX_HEALTH = MOB_GHAST.option("max-health", 20f);
    public static final Option<Integer> MOB_GHAST__FIREBALL_POWER = MOB_GHAST.option("fireball-power", 3);

    // AI
    public static final Section AI = ENTITIES.section("AI");
    
    public static final Section AI_MOB_BLOCK_BREAK = AI.section("Mob block breaking");
    public static final Option<IntRange> AI_MOB_BLOCK_BREAK__SEARCH_COOLDOWN = AI_MOB_BLOCK_BREAK.option("search-new-block-cooldown", Range.create(6, 12));
    public static final Option<Integer> AI_MOB_BLOCK_BREAK__MAX_BREAK_TICKS = AI_MOB_BLOCK_BREAK.option("max-block-break-ticks", 100);
    
    public static final Section AI_MOB_BLOCK_PLACE = AI.section("Mob block placing");
    public static final Option<IntRange> AI_MOB_BLOCK_PLACE__PLACE_COOLDOWN = AI_MOB_BLOCK_PLACE.option("block-place-cooldown", Range.create(20, 40));
    public static final Option<Integer> AI_MOB_BLOCK_PLACE__TICKS_AFTER_JUMP_TO_PLACE_BLOCK = AI_MOB_BLOCK_PLACE.option("ticks-after-jump-to-place-block", 3);
    public static final Option<Boolean> AI_MOB_BLOCK_PLACE__INFINITE_BLOCKS = AI_MOB_BLOCK_PLACE.option("infinite-blocks", false);

    public static final Option<DoubleRange> AI__HOSTILE_FOLLOW_RANGE = AI.option("hostile-follow-range", Range.create(32.0, 52.0));
    public static final Option<Boolean> AI__LOOK_AT_TARGET_EVERY_TICK = AI.option("look-at-target-every-tick", true);
    
    // Items
    public static final Section ITEMS = WORLD.section("Items");

    public static final Section ITEM_ARMOR = ITEMS.section("Armor");
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_LEATHER = Settings.ITEM_ARMOR.option("leather", new ArmorConfig(1, 2, 3, 1, 3, 0f, 0f, 15));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_CHAIN = Settings.ITEM_ARMOR.option("chain", new ArmorConfig(1, 4, 5, 2, 4, 1f, 0f, 12));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_IRON = Settings.ITEM_ARMOR.option("iron", new ArmorConfig(2, 5, 6, 2, 5, 1.5f, 0.02f, 9));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_GOLD = Settings.ITEM_ARMOR.option("gold", new ArmorConfig(1, 3, 5, 2, 7, 0f, 0f, 25));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_DIAMOND = Settings.ITEM_ARMOR.option("diamond", new ArmorConfig(3, 6, 8, 3, 11, 3f, 0.04f, 10));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_TURTLE = Settings.ITEM_ARMOR.option("turtle", new ArmorConfig(2, 5, 6, 2, 5, 0f, 0f, 9));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_NETHERITE = Settings.ITEM_ARMOR.option("netherite", new ArmorConfig(3, 6, 8, 3, 11, 4.5f, 0.12f, 15));
    public static final Option<ArmorConfig> ITEM_ARMOR__MATERIAL_ARMADILLO = Settings.ITEM_ARMOR.option("armadillo", new ArmorConfig(3, 6, 8, 3, 11, 0f, 0f, 10));
    public static final Option<Float> ITEM_ARMOR__NETHERITE_FIRE_RESISTANCE = ITEM_ARMOR.option("resistance.fire.netherite", 0.04f);
    public static final Option<Float> ITEM_ARMOR__NETHERITE_LAVA_RESISTANCE = ITEM_ARMOR.option("resistance.lava.netherite", 0.03f);
    public static final Option<Float> ITEM_ARMOR__DIAMOND_FIRE_RESISTANCE = ITEM_ARMOR.option("resistance.fire.diamond", 0.025f);
    public static final Option<Float> ITEM_ARMOR__DIAMOND_LAVA_RESISTANCE = ITEM_ARMOR.option("resistance.lava.diamond", 0.015f);
    
    public static final Section ITEM_SHIELD = ITEMS.section("Shield");
    public static final Option<Float> ITEM_SHIELD__EXPLOSION_ABSORPTION = ITEM_SHIELD.option("explosion-absorption-percent", 0.90f);
    public static final Option<Float> ITEM_SHIELD__BLAST_RESISTANCE_PER_LEVEL = ITEM_SHIELD.option("blast-resistance-absorption-percent-per-level", 0.15f);
    public static final Option<IntRange> ITEM_SHIELD__ON_EXPLOSION_COOLDOWN = ITEM_SHIELD.option("on-explosion-cooldown-by-damage", Range.create(60, 100));
    
    // Damage
    public static final Section DAMAGE_SOURCE = WORLD.section("Damage source multipliers");
    public static final Option<Float> DAMAGE_SOURCE__LAVA_MULT = DAMAGE_SOURCE.option("lava-damage-mult", 2.5f);
    public static final Option<Float> DAMAGE_SOURCE__FIRE_MULT = DAMAGE_SOURCE.option("fire-damage-mult", 1.75f);
    public static final Option<Float> DAMAGE_SOURCE__IN_WALL_MULT = DAMAGE_SOURCE.option("in-wall-damage-mult", 2.25f);
    public static final Option<Float> DAMAGE_SOURCE__DROWN_MULT = DAMAGE_SOURCE.option("drown-damage-mult", 2.0f);
    
    // Mechanics
    public static final Section SLOWDOWN_BY_HEALTH = MAIN_STORAGE.section("Slowdown by health");
    public static final Option<Float> SLOWDOWN_BY_HEALTH__AFFECTION_HEALTH_LIMIT = Settings.SLOWDOWN_BY_HEALTH.option("mobs.affection-health-limit", 0.6f);
    public static final Option<Float> SLOWDOWN_BY_HEALTH__EFFECT_MULTIPLIER = Settings.SLOWDOWN_BY_HEALTH.option("mobs.effect-multiplier", 0.8f);
    public static final Option<String> SLOWDOWN_BY_HEALTH__MOVE_SPEED_MODIFIER_UUID = Settings.SLOWDOWN_BY_HEALTH.option("modifier-uuid", "e7c316a0-7f28-4897-a096-57e572e66ce3");
    public static final Option<Float> SLOWDOWN_BY_HEALTH__PLAYER__EFFECT_MULTIPLIER = Settings.SLOWDOWN_BY_HEALTH.option("player.effect-multiplier", 0.6f);
    public static final Option<Float> SLOWDOWN_BY_HEALTH__PLAYER__AFFECTION_HEALTH_LIMIT = Settings.SLOWDOWN_BY_HEALTH.option("player.affection-health-limit", 0.65f);
    
    public static final Section PASSIVE_HEALTH_REGENERATION = MAIN_STORAGE.section("Passive health regeneration");
    public static final Option<IntRange> PASSIVE_HEALTH_REGENERATION__COOLDOWN = Settings.PASSIVE_HEALTH_REGENERATION.option("healing-cooldown", Range.create(15, 35));
    public static final Option<Integer> PASSIVE_HEALTH_REGENERATION__START_DELAY = Settings.PASSIVE_HEALTH_REGENERATION.option("after-damage-delay", 100);
    public static final Option<Float> PASSIVE_HEALTH_REGENERATION__HEALTH_PERCENT = Settings.PASSIVE_HEALTH_REGENERATION.option("healing-percent-of-max-health", 0.05f);
    
    public static final Section HEALTH_DISPLAY = MAIN_STORAGE.section("Health display");
    public static final Option<Integer> HEALTH_DISPLAY__DISPLAY_TICKS = Settings.HEALTH_DISPLAY.option("display-ticks", 120);
    
    public static final Section CUSTOM_SLEEPING = MAIN_STORAGE.section("Custom sleeping");
    public static final Option<Integer> CUSTOM_SLEEPING__MAX_NIGHT_SKIP_MULTIPLIER = Settings.CUSTOM_SLEEPING.option("night-skip-tick-multiplier", 20);
    public static final Option<Integer> CUSTOM_SLEEPING__TICKS_DELTA = Settings.CUSTOM_SLEEPING.option("formatted-time-ticks-delta", 7000);
    
    public static final Section FIREBALL_THROWING = MAIN_STORAGE.section("Fireball Throwing");
    public static final Option<Integer> FIREBALL_THROWING__POWER = me.xleiten.rebalance.Settings.FIREBALL_THROWING.option("power", 1);
    public static final Option<Integer> FIREBALL_THROWING__COOLDOWN = me.xleiten.rebalance.Settings.FIREBALL_THROWING.option("cooldown", 5);
}
