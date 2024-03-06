package me.xleiten.rebalance.api.game.world.text_display.armor_stand;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DynamicDisplay extends Display
{
    private Supplier<Text> textSupplier;

    public DynamicDisplay(@NotNull ServerWorld world, @NotNull Vec3d pos, @NotNull Supplier<Text> textSupplier, boolean interactive) {
        super(world, pos, interactive);
        this.textSupplier = textSupplier;
    }

    public DynamicDisplay(@NotNull ServerWorld world, @NotNull Vec3d pos, @NotNull Supplier<Text> textSupplier) {
        super(world, pos);
        this.textSupplier = textSupplier;
    }

    public void setTextSupplier(@NotNull Supplier<Text> textSupplier) {
        this.textSupplier = textSupplier;
    }

    @Override
    public void tick() {
        setCustomName(textSupplier.get());
    }
}
