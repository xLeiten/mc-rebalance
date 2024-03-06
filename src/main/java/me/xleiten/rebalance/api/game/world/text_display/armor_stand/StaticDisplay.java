package me.xleiten.rebalance.api.game.world.text_display.armor_stand;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class StaticDisplay extends Display
{
    public StaticDisplay(@NotNull ServerWorld world, @NotNull Vec3d pos, @NotNull Text text, boolean interactive) {
        super(world, pos, interactive);
        setCustomName(text);
    }

    public StaticDisplay(@NotNull ServerWorld world, @NotNull Vec3d pos, @NotNull Text text) {
        this(world, pos, text, false);
    }

    public StaticDisplay setText(@NotNull Text text) {
        setCustomName(text);
        return this;
    }
}
