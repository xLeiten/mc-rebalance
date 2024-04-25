package me.xleiten.rebalance.core.mixins.world.entity.player.offline_skins;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import me.xleiten.rebalance.api.game.world.entity.SkinData;
import me.xleiten.rebalance.api.game.world.entity.SkinHolder;
import me.xleiten.rebalance.core.mixins.world.chunk.ChunkStorageAccessor;
import me.xleiten.rebalance.core.mixins.world.chunk.EntityTrackerAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements SkinHolder
{
    @Unique private SkinData skinData;

    @Shadow public ServerPlayNetworkHandler networkHandler;

    @Shadow @Nullable public abstract Entity moveToWorld(ServerWorld destination);

    @Shadow public abstract void sendAbilitiesUpdate();

    @Shadow public abstract CommonPlayerSpawnInfo createCommonPlayerSpawnInfo(ServerWorld world);

    @Shadow public abstract void travel(Vec3d movementInput);

    protected MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile)
    {
        super(world, pos, yaw, gameProfile);
    }

    @Override
    public @NotNull TrackedData<Byte> cringeMod$getModelPartsTracker() {
        return PLAYER_MODEL_PARTS;
    }

    @Override
    public void cringeMod$setSkinData(SkinData skinData, boolean reload) {
        var data = getGameProfile().getProperties();
        data.removeAll(SkinData.TEXTURES);
        data.put(SkinData.TEXTURES, skinData.convert());
        if (reload)
            cringeMod$updateSkin();
    }

    @Override
    public Optional<SkinData> cringeMod$getSkinData() {
        if (skinData != null) return Optional.of(skinData);
        try {
            var property = getGameProfile().getProperties().get("textures").iterator().next();
            return Optional.of(SkinData.copyFrom(property));
        } catch (Exception ignored) { }
        return Optional.empty();
    }

    @Override
    public void cringeMod$updateSkin() {
        var self = (ServerPlayerEntity) (Object) this;
        var world = self.getServerWorld();

        PlayerManager playerManager = world.getServer().getPlayerManager();
        playerManager.sendToAll(new PlayerRemoveS2CPacket(new ArrayList<>(Collections.singleton(getUuid()))));
        playerManager.sendToAll(PlayerListS2CPacket.entryFromPlayer(Collections.singleton((self))));

        EntityTrackerAccessor tracker = (EntityTrackerAccessor) ((ChunkStorageAccessor) world.getChunkManager().threadedAnvilChunkStorage).getEntityTrackers().get(getId());
        tracker.getListeners().forEach(tracking -> tracker.getEntry().startTracking(tracking.getPlayer()));

        this.networkHandler.sendPacket(new PlayerRespawnS2CPacket(createCommonPlayerSpawnInfo(world), PlayerRespawnS2CPacket.KEEP_ALL));
        this.networkHandler.sendPacket(new PlayerPositionLookS2CPacket(getX(), getY(), getZ(), getYaw(), getPitch(), Collections.emptySet(), 0));
        this.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(this.getInventory().selectedSlot));

        this.networkHandler.sendPacket(new DifficultyS2CPacket(world.getDifficulty(), world.getLevelProperties().isDifficultyLocked()));
        this.networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(this.experienceProgress, this.totalExperience, this.experienceLevel));
        playerManager.sendWorldInfo(self, world);
        playerManager.sendCommandTree(self);

        this.networkHandler.sendPacket(new HealthUpdateS2CPacket(this.getHealth(), this.getHungerManager().getFoodLevel(), this.hungerManager.getSaturationLevel()));

        for (StatusEffectInstance statusEffect : this.getStatusEffects()) {
            this.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(getId(), statusEffect));
        }

        var equipmentList = new ArrayList<Pair<EquipmentSlot, ItemStack>>();
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = getEquippedStack(equipmentSlot);
            if (!itemStack.isEmpty()) {
                equipmentList.add(Pair.of(equipmentSlot, itemStack.copy()));
            }
        }

        if (!equipmentList.isEmpty()) {
            this.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(getId(), equipmentList));
        }

        if (!getPassengerList().isEmpty()) {
            this.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(self));
        }

        var vehicle = getVehicle();
        if (vehicle != null) {
            this.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(vehicle));
        }

        this.sendAbilitiesUpdate();
        playerManager.sendPlayerStatus(self);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound tag, CallbackInfo ci) {
        if (skinData != null) {
            tag.put("the_pain:skin", skinData.toNbt());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound tag, CallbackInfo ci) {
        NbtCompound skinDataNbt = tag.getCompound("the_pain:skin");
        var texture = skinDataNbt.contains("value") ? skinDataNbt.getString("value") : null;
        var signature = skinDataNbt.contains("signature") ? skinDataNbt.getString("signature") : null;

        if (texture != null) {
            this.skinData = new SkinData(texture, signature);
            this.cringeMod$setSkinData(this.skinData, true);
        }
    }
}
