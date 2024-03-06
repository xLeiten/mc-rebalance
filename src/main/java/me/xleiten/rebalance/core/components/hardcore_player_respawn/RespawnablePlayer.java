package me.xleiten.rebalance.core.components.hardcore_player_respawn;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.text_display.DynamicTextDisplay;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.stages.AwaitingStage;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.stages.RitualContext;
import me.xleiten.rebalance.api.game.world.staged_process.StagedProcess;
import me.xleiten.rebalance.util.Messenger;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.world.GameMode;

public final class RespawnablePlayer
{
    private static final Option<Integer> MAX_TICKS = Settings.HARDCORE_PLAYER_RESPAWN.option("max-existence-ticks", 20 * 60 * 40);

    private int existenceTime = MAX_TICKS.getValue();
    private final DynamicTextDisplay existenceTimer;
    private final StagedProcess<RitualContext> ritual;
    private final RitualContext context;

    RespawnablePlayer(ServerPlayerEntity player) {
        this.context = new RitualContext(player);
        this.ritual = new StagedProcess<>(new AwaitingStage(context), ((result, context) -> remove()));
        this.existenceTimer = new DynamicTextDisplay(context.world, context.position.add(0, 0.5, 0), () -> Text.of(StringHelper.formatTicks(existenceTime, 20)));
        existenceTimer.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
        existenceTimer.spawn();
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, existenceTime, 1, true, false, false));
        //PlayerUtils.copyInventory(context.deadBody, player.getInventory());
        //player.getInventory().clear();
    }

    public void tick() {
        if (existenceTime > 0) {
            existenceTime--;
            ritual.tick();
        } else {
            if (!ritual.isCompleted()) {
                Messenger.sendMessage("Хихихиха момент", context.player);
                ritual.forceStop();
            }
        }
    }

    public void forceRevive() {
        context.player.changeGameMode(GameMode.SURVIVAL);
        Messenger.sendMessage("Ты воскресил себя", context.player);
        ritual.forceStop();
    }

    public void remove() {
        context.world.spawnParticles(ParticleTypes.CLOUD, context.deadBody.getX(), context.deadBody.getY(), context.deadBody.getZ(), context.random.nextBetween(5, 10), 0, 0, 0, 0);
        //PlayerUtils.copyInventory(context.player, context.deadBody.getInventory());
        existenceTimer.discard();
        context.player.clearStatusEffects();
        context.deadBody.remove();
        existenceTime = 0;
    }

    public StagedProcess<RitualContext> getRitualProcess() {
        return ritual;
    }

    public boolean isRevived() {
        return ritual.isCompleted();
    }

    public ServerPlayerEntity getPlayer() {
        return context.player;
    }

}