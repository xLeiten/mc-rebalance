package me.xleiten.rebalance.api.game.world.item;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

public final class ItemNbtHelper {
    public enum NbtKey
    {
        LORE("Lore"),
        DISPLAY("display"),
        NAME("Name");

        private final String name;

        NbtKey(String name) {
            this.name = name;
        }

        public String nbt() {
            return name;
        }
    }

    public static ItemStack setLore(@NotNull ItemStack stack, @NotNull List<Text> lore) {
        var displayNbt = stack.getOrCreateSubNbt("display");
        var loreNbt = new NbtList();
        lore.forEach(text -> loreNbt.add(NbtString.of(Text.Serialization.toJsonString(text))));
        displayNbt.put(NbtKey.LORE.nbt(), loreNbt);
        return stack;
    }

    public static LoreEditor editLore(@NotNull ItemStack stack) {

        return null;
    }

    public static class LoreEditor
    {
        private final ItemStack stack;
        private NbtList loreNbt = null;

        public LoreEditor(ItemStack stack)
        {
            this.stack = stack;
            if (stack.hasNbt()) {
                var displayNbt = stack.getOrCreateSubNbt(NbtKey.DISPLAY.nbt());
                if (displayNbt.contains(NbtKey.LORE.nbt()))
                    this.loreNbt = displayNbt.getList(NbtKey.LORE.nbt(), NbtElement.STRING_TYPE);
            }

            if (this.loreNbt == null)
                this.loreNbt = new NbtList();
        }

        public LoreEditor appendLine(@NotNull Text text) {
            loreNbt.add(fromText(text));
            return this;
        }

        public LoreEditor appendLine(@NotNull String text) {
            loreNbt.add(fromText(Text.literal(text).setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GRAY))));
            return this;
        }

        public LoreEditor removeLine(@Range(from = 1, to = Integer.MAX_VALUE) int number) {
            if (number > loreNbt.size()) return this;
            loreNbt.remove(number - 1);
            return this;
        }
    }

    public static NbtString fromText(@NotNull Text text) {
        return NbtString.of(Text.Serialization.toJsonString(text));
    }
}
