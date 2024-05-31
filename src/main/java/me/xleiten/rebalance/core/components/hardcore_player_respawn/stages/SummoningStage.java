package me.xleiten.rebalance.core.components.hardcore_player_respawn.stages;

import me.xleiten.rebalance.api.game.world.staged_process.Stage;
import me.xleiten.rebalance.api.game.world.staged_process.TickResult;
import me.xleiten.rebalance.api.game.world.text_display.DynamicTextDisplay;
import me.xleiten.rebalance.util.Messenger;
import me.xleiten.rebalance.util.ParticleHelper;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

public final class SummoningStage extends Stage<RitualContext>
{
    private final ServerPlayerEntity participant;
    private final DynamicTextDisplay timer;
    private final float xpDrainRatio;
    private float xpTempBank = 0;
    private int ticks = context.component.ritualTime.value();

    public SummoningStage(@NotNull RitualContext context, @NotNull ServerPlayerEntity participant)
    {
        super(context);
        context.world.playSound(context.deadBody, context.position.x, context.position.y, context.position.z, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS);
        Messenger.sendMessage("Ритуал воскрешения начат", context.player, participant);
        participant.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, ticks, 4, true, false, false));
        participant.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, ticks, 1, true, false, false));
        this.participant = participant;
        this.xpDrainRatio = context.component.xpNeeded.value() / ticks;
        this.timer = new DynamicTextDisplay(context.world, context.position.add(0, 0.8, 0), () -> Text.literal(StringHelper.formatTicks(ticks, 20)).formatted(Formatting.GOLD));
        timer.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
        timer.spawn();
    }

    @Override
    public @NotNull TickResult<Stage<RitualContext>> tick() {
        if (ticks > 0) {
            ticks--;
            if (canRitualContinue()) {
                pullPlayerToBody();
                drainParticipantXp();
                if (context.random.nextFloat() < 0.3) spawnParticles();
                if (ticks % 80L == 0L && ticks > 0) {
                    context.world.playSound(context.deadBody, context.position.x, context.position.y, context.position.z, SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.PLAYERS);
                }
                return TickResult.stage(this);
            } else {
                interruptRitual();
                return TickResult.complete(false);
            }
        } else {
            completeRitual();
            return TickResult.complete(true);
        }
    }

    @Override
    public void onComplete() {
        //participant.closeHandledScreen();
        this.timer.discard();
    }

    private boolean canRitualContinue() {
        return !context.player.isDisconnected() && participant.canTakeDamage() && !participant.isDisconnected() && participant.squaredDistanceTo(context.deadBody) <= context.component.summoningRadiusSquared;
    }

    private void completeRitual() {
        Messenger.sendMessage("Ритуал завершен", context.player, participant);
        context.world.playSound(context.deadBody, context.position.x, context.position.y, context.position.z, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS);
        context.player.setPosition(context.position.x, context.position.y, context.position.z);
        context.player.changeGameMode(GameMode.SURVIVAL);
        context.player.setHealth(context.random.nextFloat() * (context.player.getMaxHealth() - 8) + 8);
    }

    private void interruptRitual() {
        context.world.playSound(context.deadBody, context.position.x, context.position.y, context.position.z, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS);
        Messenger.sendMessage("Ритуал прерван", context.player, participant);
        context.player.clearStatusEffects();
        participant.clearStatusEffects();
        participant.kill();
    }

    private void pullPlayerToBody() {
        var current = context.player.getEyePos();
        var distance = context.player.squaredDistanceTo(context.position);
        context.player.setVelocity(new Vec3d(context.position.x - current.x, context.position.y - current.y, context.position.z - current.z).normalize().multiply(distance > 400 ? 1 : (distance / 400)));
        context.player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(context.player));
    }

    private void drainParticipantXp() {
        if ((xpTempBank += xpDrainRatio) > 1) {
            if (participant.experienceLevel > 0 || participant.totalExperience > 0) {
                participant.addExperience(-(int) xpTempBank);
            } else {
                participant.damage(context.world.getDamageSources().generic(), 1.5f);
            }
            xpTempBank = xpTempBank - (float) Math.floor(xpTempBank);
        }
    }

    private void spawnParticles() {
        var blockPos = context.deadBody.getPos();
        var random = context.random;

        var pos = createParticlePos(blockPos);
        for (int i = 0; i < random.nextBetween(1, 6); i++) {
            var vel = new Vec3d(pos.x - context.position.x, pos.y - context.position.y, pos.z - context.position.z).normalize().multiply(0.25);
            context.world.spawnParticles(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 0, vel.x, vel.y, vel.z, 0.5);
            pos = createParticlePos(blockPos);
        }

        var r = context.component.summoningRadius.value();
        for (int i = 0; i < 360; i += 2) {
            var t = ParticleHelper.RADIAN * i;
            context.world.spawnParticles(ParticleTypes.FLAME, blockPos.x + Math.sin(t) * r, pos.y, blockPos.z + Math.cos(t) * r, 1, 0, 0, 0, 0);
        }
    }

    private Vec3d createParticlePos(Vec3d center) {
        var random = context.random;
        var offsetX = (random.nextInt(2) == 0 ? -2 : 2) * random.nextFloat();
        var offsetY = (random.nextInt(2) == 0 ? -2 : 2) * random.nextFloat();
        var offsetZ = (random.nextInt(2) == 0 ? -2 : 2) * random.nextFloat();
        return new Vec3d(center.x + offsetX, center.y + offsetY, center.z + offsetZ);
    }

    public ServerPlayerEntity getParticipant() {
        return participant;
    }
}
