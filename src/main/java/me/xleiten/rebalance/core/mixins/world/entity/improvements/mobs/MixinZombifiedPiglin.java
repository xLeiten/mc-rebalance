package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.RandomHelper;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.ZOMBIFIED_PIGLIN_SETTINGS;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.chance;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class MixinZombifiedPiglin extends MixinZombieEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = ZOMBIFIED_PIGLIN_SETTINGS.option("move-speed-mult", Range.create(0.25, 0.55));
    @Unique private static final Option<DoubleRange> BABY_MOVE_SPEED_MULT = ZOMBIFIED_PIGLIN_SETTINGS.option("baby-move-speed-mult", Range.create(0.05, 0.25));
    @Unique private static final Option<Double> USE_AXE_CHANCE = ZOMBIFIED_PIGLIN_SETTINGS.option("use-axe-chance", 5d);

    protected MixinZombifiedPiglin(EntityType<? extends ZombieEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        this.cringeMod$setCanAdaptToLight(false);
        this.cringeMod$setBurnInDayLight(false);
        this.cringeMod$setCanAlertOthers(false);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost").value(RandomHelper.range(random, (isBaby() ? BABY_MOVE_SPEED_MULT : MOVE_SPEED_MULT).getValue())));
    }

    @Override
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        if (chance(random, USE_AXE_CHANCE.getValue()))
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
