package me.xleiten.rebalance.api.game.world.text_display;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class StaticTextDisplay extends TextDisplay
{
    public StaticTextDisplay(ServerWorld world, Vec3d pos, @NotNull Text text) {
        super(world, pos);
        setText(text);
    }
}
