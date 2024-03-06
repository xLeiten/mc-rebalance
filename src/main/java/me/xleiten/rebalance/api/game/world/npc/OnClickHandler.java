package me.xleiten.rebalance.api.game.world.npc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public interface OnClickHandler
{
    void onClick(PlayerEntity other, Hand hand);
}
