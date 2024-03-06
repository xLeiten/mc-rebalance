/*
package me.xleiten.the_pain.core.components.hardcore_player_respawn.stages;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import me.xleiten.the_pain.api.game.world.npc.HumanEntity;
import me.xleiten.the_pain.util.math.IntRange;
import me.xleiten.the_pain.util.math.Range;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DeadBodyInventoryInterface extends GenericContainerScreenHandler
{
    public static final IntRange INVENTORY_SLOTS = Range.create(0, 35);
    public static final IntRange ARMOR_SLOTS = Range.create(36, 40);
    public static final IntRange UNUSED_SLOTS = Range.create(41, 44);

    public final HumanEntity owner;
    public final PlayerInventory inventory;

    public DeadBodyInventoryInterface(@NotNull HumanEntity owner, @NotNull PlayerInventory playerInventory, int syncId) {
        super(ScreenHandlerType.GENERIC_9X5, syncId, playerInventory, new SimpleInventory(45), 5);
        this.owner = owner;
        this.inventory = owner.getInventory();
        loadInventory();
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        player.sendMessage(Text.of("slotIndex: " + slotIndex + ", action: " + actionType.name()));
        if (slotIndex >= 36 && slotIndex < 45) {
            if (slotIndex < 40) {
                var stack = getCursorStack();
                if (!stack.isEmpty() && !(Equipment.fromStack(stack) instanceof ArmorItem)) return;
            } else {
                if (slotIndex != 40) {
                    return;
                }
            }
        }
        super.onSlotClick(slotIndex, button, actionType, player);
        syncInventory();
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        syncInventory();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < 45) {
                if (!this.insertItem(itemStack2, 45, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.getInventory().canPlayerUse(player);
    }

    public void loadInventory() {
        var inventory = owner.getInventory().main;
        for (int i = 0; i < 36; i++) {
            this.setStackInSlot(i, 0, inventory.get(i));
        }
        setStackInSlot(36, 0, owner.getEquippedStack(EquipmentSlot.HEAD));
        setStackInSlot(37, 0, owner.getEquippedStack(EquipmentSlot.CHEST));
        setStackInSlot(38, 0, owner.getEquippedStack(EquipmentSlot.LEGS));
        setStackInSlot(39, 0, owner.getEquippedStack(EquipmentSlot.FEET));

        setStackInSlot(40, 0, owner.getEquippedStack(EquipmentSlot.OFFHAND));
        for (int i = UNUSED_SLOTS.min; i <= UNUSED_SLOTS.max; i++) {
            this.setStackInSlot(i, 0, new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE));
        }
        sendContentUpdates();
    }

    public void syncInventory() {
        ChestBlockEntity
        var inventory = owner.getInventory();
        for (int i = 0; i < 36; i++) {
            inventory.main.set(i, getSlot(i).getStack());
        }
        inventory.armor.set(3, getSlot(36).getStack());
        inventory.armor.set(2, getSlot(37).getStack());
        inventory.armor.set(1, getSlot(38).getStack());
        inventory.armor.set(0, getSlot(39).getStack());

        inventory.offHand.set(0, getSlot(40).getStack());
        sendContentUpdates();
    }

    public void sendUpdate() {
        owner.getInventory().markDirty();
        sendEquipmentUpdate();
    }

    private void sendEquipmentUpdate() {
        List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = owner.getEquippedStack(equipmentSlot);
            if (!itemStack.isEmpty()) {
                list.add(Pair.of(equipmentSlot, itemStack.copy()));
            }
        }
        var server = owner.getServer();
        if (server != null)
            server.getPlayerManager().sendToAll(new EntityEquipmentUpdateS2CPacket(owner.getId(), list));
    }
}
*/
