package me.xleiten.rebalance.api.game.world.text_display;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public abstract class TextDisplay extends DisplayEntity.TextDisplayEntity
{
    public TextDisplay(ServerWorld world, Vec3d pos) {
        super(EntityType.TEXT_DISPLAY, world);
        setPosition(pos);
    }

    public void spawn() {
        getWorld().spawnEntity(this);
    }

    public void setUseShadow(boolean useShadow) {
        this.dataTracker.set(TEXT_DISPLAY_FLAGS, setBitField(getDisplayFlags(), SHADOW_FLAG, useShadow));
    }

    public void setCanSeeThrough(boolean canSee) {
        this.dataTracker.set(TEXT_DISPLAY_FLAGS, setBitField(getDisplayFlags(), SEE_THROUGH_FLAG, canSee));
    }

    public void setUseBackground(boolean background) {
        this.dataTracker.set(TEXT_DISPLAY_FLAGS, setBitField(getDisplayFlags(), DEFAULT_BACKGROUND_FLAG, background));
    }

    public void setBackgroundColor(@NotNull String hexColor) {
        this.dataTracker.set(BACKGROUND, Integer.parseInt(hexColor.replaceFirst("#", ""), 16));
    }

    public void setOpacity(float opacity) {
        setTextOpacity((byte) (MathHelper.clamp(opacity, 0, 1) * 127f));
    }

    public void setTextAligment(@NotNull TextAlignment textAligment) {
        byte result = setBitFields(getDisplayFlags(), false, LEFT_ALIGNMENT_FLAG, RIGHT_ALIGNMENT_FLAG);
        switch (textAligment) {
            case CENTER -> this.dataTracker.set(TEXT_DISPLAY_FLAGS, result);
            case LEFT -> this.dataTracker.set(TEXT_DISPLAY_FLAGS, setBitField(result, LEFT_ALIGNMENT_FLAG, true));
            case RIGHT -> this.dataTracker.set(TEXT_DISPLAY_FLAGS, setBitField(result, RIGHT_ALIGNMENT_FLAG, true));
        }
    }

    public static byte setBitField(byte bits, int bitField, boolean state) {
        return (byte) (state ? (bits | bitField) : (bits & ~bitField));
    }

    public static byte setBitFields(byte bits, boolean state, int...bitFields) {
        var result = bits;
        for (int i: bitFields) result = setBitField(result, i, state);
        return result;
    }

}
