package me.xleiten.rebalance.util;

import net.minecraft.util.math.MathHelper;

public class EntityUtils
{
    public static float healthToDamageRatio(float damage, float health) {
        return MathHelper.clamp(damage, 0, health) / health;
    }
}
