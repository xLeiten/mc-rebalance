package me.xleiten.rebalance.api.game.world.entity;

import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.NbtCompound;

public record SkinData(String texture, String signature)
{
    public static final String TEXTURES = "textures";

    public Property convert() {
        return new Property(TEXTURES, texture, signature);
    }

    public NbtCompound toNbt() {
        var compound = new NbtCompound();
        compound.putString("texture", texture);
        compound.putString("signature", signature);
        return compound;
    }

    public static SkinData copyFrom(Property property) {
        return new SkinData(property.value(), property.signature());
    }
}
