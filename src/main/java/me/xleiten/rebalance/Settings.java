package me.xleiten.rebalance;

import me.xleiten.rebalance.api.config.Section;

import static me.xleiten.rebalance.Rebalance.MAIN_STORAGE;

public final class Settings
{
    // Server
    public static final Section SERVER_SETTINGS = MAIN_STORAGE.section("Server");

    // World
    public static final Section WORLD_SETTINGS = MAIN_STORAGE.section("World");

    // Entities
    public static final Section ENTITY_SETTINGS = WORLD_SETTINGS.section("Entities");

    // Mobs
    public static final Section MOB_SETTINGS = ENTITY_SETTINGS.section("Mobs");
    public static final Section SPIDER_SETTINGS = MOB_SETTINGS.section("Spider");
    public static final Section CREEPER_SETTINGS = MOB_SETTINGS.section("Creeper");
    public static final Section ZOMBIE_SETTINGS = MOB_SETTINGS.section("Zombie");
    public static final Section ZOMBIE_VILLAGER_SETTINGS = MOB_SETTINGS.section("Zombie villager");
    public static final Section HUSK_SETTINGS = MOB_SETTINGS.section("Husk");
    public static final Section DROWNED_SETTINGS = MOB_SETTINGS.section("Drowned");
    public static final Section END_CRYSTAL_SETTINGS = ENTITY_SETTINGS.section("End crystal");
    public static final Section PLAYER_SETTINGS = ENTITY_SETTINGS.section("Player");
    public static final Section ZOMBIFIED_PIGLIN_SETTINGS = MOB_SETTINGS.section("Zombified piglin");
    public static final Section WITCH_SETTINGS = MOB_SETTINGS.section("Witch");
    public static final Section SKELETON_SETTINGS = MOB_SETTINGS.section("Skeleton");
    public static final Section WITHER_SKELETON_SETTINGS = MOB_SETTINGS.section("Wither skeleton");
    public static final Section WARDEN_SETTINGS = MOB_SETTINGS.section("Warden");
    public static final Section IRON_GOLEM_SETTINGS = MOB_SETTINGS.section("Iron golem");

    // Damage
    public static final Section DAMAGE_MULTIPLIERS = WORLD_SETTINGS.section("Damage source multipliers");

    // AI
    public static final Section AI_SETTINGS = ENTITY_SETTINGS.section("AI");
    public static final Section HOSTILE_ENTITY_BLOCK_BREAK_GOAL = AI_SETTINGS.section("Mob block breaking");
    public static final Section HOSTILE_ENTITY_BLOCK_PLACE_GOAL = AI_SETTINGS.section("Mob block placing");

    // Items
    public static final Section ITEMS = WORLD_SETTINGS.section("Items");
    public static final Section ARMOR_BUFFS = ITEMS.section("Armor");

    // Mechanics
    public static final Section SLOWDOWN_BY_HEALTH = MAIN_STORAGE.section("Slowdown by health");
    public static final Section PASSIVE_HEALTH_REGENERATION = MAIN_STORAGE.section("Passive health regeneration");
    public static final Section SHIELD_REWORK = MAIN_STORAGE.section("Shield rework");
    public static final Section HEALTH_DISPLAY = MAIN_STORAGE.section("Health display");
    public static final Section CUSTOM_SLEEPING = MAIN_STORAGE.section("Custom sleeping");
}
