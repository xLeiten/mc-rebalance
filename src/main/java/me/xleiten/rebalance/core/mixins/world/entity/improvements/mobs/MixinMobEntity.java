package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

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

    @Shadow @Nullable public abstract EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, net.minecraft.entity.SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt);

    @Shadow public abstract boolean spawnsTooManyForEachTry(int count);

    @Shadow protected abstract void onPlayerSpawnedChild(PlayerEntity player, MobEntity child);

    @Unique
    private SpawnReason spawnReason = SpawnReason.GENERIC;

    protected MixinMobEntity(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onCustomNbtDataWrite(NbtCompound nbt) {
        nbt.putString("SpawnReason", spawnReason.name());
    }

    @Override
    public void cringeMod$onCustomNbtDataRead(NbtCompound nbt) {
        spawnReason = SpawnReason.from(nbt.getString("SpawnReason"));
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        this.spawnReason = reason;
    }
}
