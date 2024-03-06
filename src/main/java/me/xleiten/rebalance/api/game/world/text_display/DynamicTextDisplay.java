package me.xleiten.rebalance.api.game.world.text_display;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DynamicTextDisplay extends TextDisplay
{
    private Supplier<Text> textSupplier;

    public DynamicTextDisplay(ServerWorld world, Vec3d pos, @NotNull Supplier<Text> textSupplier) {
        super(world, pos);
        setDynamicText(textSupplier);
    }

    public void setDynamicText(@NotNull Supplier<Text> textSupplier) {
        this.textSupplier = textSupplier;
    }

    @Override
    public void tick() {
        setText(textSupplier.get());
        super.tick();
    }
}
