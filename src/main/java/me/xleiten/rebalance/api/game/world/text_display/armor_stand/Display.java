package me.xleiten.rebalance.api.game.world.text_display.armor_stand;

import me.xleiten.rebalance.api.game.world.entity.ArmorStand;
import me.xleiten.rebalance.api.game.world.npc.OnClickHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public abstract class Display extends ArmorStandEntity {
    private OnClickHandler onLeftClick;
    private OnClickHandler onRightClick;

    public Display(@NotNull ServerWorld world, @NotNull Vec3d pos, boolean interactive) {
        super(EntityType.ARMOR_STAND, world);

        this.setInvisible(true);
        this.setCustomNameVisible(true);
        this.setNoGravity(true);
        this.setSilent(true);
        this.setInvulnerable(true);
        if (!interactive) {
            ((ArmorStand) this).cringeMod$setIsSmall(true);
            ((ArmorStand) this).cringeMod$setIsMarker(true);
            this.setPosition(pos);
            this.setNoDrag(true);
        } else
            this.setPosition(isSmall() ? pos.subtract(0, 1.4375, 0) : pos.subtract(0, 2.425, 0));
    }

    public Display(@NotNull ServerWorld world, @NotNull Vec3d pos) {
        this(world, pos, false);
    }

    public Display setOnRightClick(@NotNull OnClickHandler handler) {
        this.onRightClick = handler;
        return this;
    }

    public Display setOnLeftClick(@NotNull OnClickHandler handler) {
        this.onLeftClick = handler;
        return this;
    }

    public void spawn() {
        getWorld().spawnEntity(this);
    }

    @Override
    public void tick() {

    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (onRightClick != null)
            onRightClick.onClick(player, hand);

        return ActionResult.PASS;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.PLAYER_ATTACK)) {
            if (onLeftClick != null) {
                var entity = source.getAttacker();
                if (entity instanceof ServerPlayerEntity player) {
                    onLeftClick.onClick(player, player.getActiveHand());
                }
            }
        }
        return false;
    }
}
