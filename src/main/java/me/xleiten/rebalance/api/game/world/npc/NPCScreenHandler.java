package me.xleiten.rebalance.api.game.world.npc;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.NotNull;

public abstract class NPCScreenHandler extends ScreenHandler
{
    protected final HumanEntity owner;

    protected NPCScreenHandler(@NotNull HumanEntity owner, @NotNull ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
        this.owner = owner;
    }

    public abstract void loadInventory();

    public abstract void saveInventory();

    public final HumanEntity getOwner() {
        return owner;
    }
}
