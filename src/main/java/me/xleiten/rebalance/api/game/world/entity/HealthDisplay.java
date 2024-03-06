package me.xleiten.rebalance.api.game.world.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public final class HealthDisplay
{
    public final LivingEntity holder;

    private boolean hasNameVisible = false;
    private Text savedName = null;

    private boolean isShowing = false;
    private int maxDisplayTime = 100;
    private int ticks = 0;

    public HealthDisplay(@NotNull LivingEntity holder)
    {
        this.holder = holder;
    }

    public void tick() {
        if (isShowing) {
            if (holder.canTakeDamage() && ticks++ < maxDisplayTime)
                refresh();
            else
                hide();
        }
    }

    public void refresh() {
        var maxHealth = holder.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
        var current = holder.getHealth();

        var percent = MathHelper.clamp((int) Math.ceil((current / maxHealth) * 10), 0, 10);
        Formatting color = switch (percent) {
            case 1, 2, 3 -> Formatting.RED;
            case 4, 5, 6 -> Formatting.YELLOW;
            default -> Formatting.GREEN;
        };

        var health = Text.literal("")
                .append(Text.literal("⬛".repeat(percent)).formatted(color))
                .append(Text.literal("⬛".repeat(10 - percent)).formatted(Formatting.GRAY));

        holder.setCustomName(health);
    }

    public void updateCustomName(Text newCustomName) {
        this.savedName = newCustomName;
    }

    public void show(@Range(from = 0, to = Integer.MAX_VALUE) int time) {
        if (holder.canTakeDamage()) {
            if (!isShowing) {
                maxDisplayTime = time;
                this.hasNameVisible = holder.isCustomNameVisible();
                this.savedName = holder.getCustomName();
                refresh();
                holder.setCustomNameVisible(true);
                isShowing = true;
            } else
                ticks = 0;
        } else
            hide();
    }

    public void hide() {
        if (isShowing) {
            holder.setCustomNameVisible(hasNameVisible);
            holder.setCustomName(savedName);
            isShowing = false;
            ticks = 0;
        }
    }

    public boolean hasVisibleCustomName() {
        return hasNameVisible;
    }

    public Text getSavedCustomName() {
        return savedName;
    }

    public boolean isActive() {
        return isShowing;
    }
}
