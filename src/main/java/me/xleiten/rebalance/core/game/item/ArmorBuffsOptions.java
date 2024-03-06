package me.xleiten.rebalance.core.game.item;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;

public final class ArmorBuffsOptions
{
    public static final Option<Float> CHAINMAIL_TOUGHNESS = Settings.ARMOR_BUFFS.option("chainmail-toughness", 1f);
    public static final Option<Float> IRON_TOUGHNESS = Settings.ARMOR_BUFFS.option("iron-toughness", 1.5f);
    public static final Option<Float> IRON_KNOCKBACK_RESISTANCE = Settings.ARMOR_BUFFS.option("iron-knockback-resistance", 0.02f);
    public static final Option<Float> DIAMOND_TOUGHNESS = Settings.ARMOR_BUFFS.option("diamond-toughness", 3f);
    public static final Option<Float> DIAMOND_KNOCKBACK_RESISTANCE = Settings.ARMOR_BUFFS.option("diamond-knockback-resistance", 0.04f);
    public static final Option<Float> NETHERITE_TOUGHNESS = Settings.ARMOR_BUFFS.option("netherite-toughness", 4.5f);
}
