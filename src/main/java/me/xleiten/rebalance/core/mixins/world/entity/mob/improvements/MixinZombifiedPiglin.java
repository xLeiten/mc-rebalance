package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.util.AttributeHelper.addModifier;
import static me.xleiten.rebalance.util.AttributeHelper.modifier;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class MixinZombifiedPiglin extends MixinZombieEntity
{
    protected MixinZombifiedPiglin(EntityType<? extends ZombieEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initCustomVars(EntityType<? extends ZombieEntity> entityType, World world, CallbackInfo ci) {
        this.cringeMod$setCanAdaptToLight(false);
        this.cringeMod$setBurnInDayLight(false);
        this.cringeMod$setCanAlertOthers(false);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        addModifier(getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED), modifier("moveSpeedBoost").value(RandomHelper.range(random, (isBaby() ? MOB_ZOMBIFIED_PIGLIN__BABY_MOVE_SPEED_MULT : MOB_ZOMBIFIED_PIGLIN__MOVE_SPEED_MULT).value())));
    }

    @Override
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        if (chance(random, MOB_ZOMBIFIED_PIGLIN__USE_AXE_CHANCE.value()))
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
        else
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    @Override
    protected void alertOthers() { }

    @Override
    protected void searchForTarget() { }

    @Override
    protected void onCustomGoalsSetup(CallbackInfo ci) { }

    @Override
    protected void resetAlertCooldown() { }
}
