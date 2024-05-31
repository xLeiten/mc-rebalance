package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity implements Mob
{
    @Shadow public abstract @Nullable LivingEntity getTarget();

    @Shadow public abstract LookControl getLookControl();

    @Shadow @Final protected GoalSelector goalSelector;

    @Shadow protected void initEquipment(Random random, LocalDifficulty localDifficulty) { }

    @Shadow public abstract void setTarget(@Nullable LivingEntity target);

    @Shadow public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Shadow public abstract ActionResult interact(PlayerEntity player, Hand hand);

    @Shadow public abstract boolean startRiding(Entity entity, boolean force);

    @Shadow public abstract void setEquipmentDropChance(EquipmentSlot slot, float chance);

    protected MixinMobEntity(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {

    }
}
