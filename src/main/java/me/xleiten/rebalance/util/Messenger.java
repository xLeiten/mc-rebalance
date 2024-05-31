package me.xleiten.rebalance.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class Messenger
{
    public static void sendMessage(String message, PlayerEntity player) {
        player.sendMessage(Text.of(message));
    }

    public static void sendMessage(String message, PlayerEntity...players) {
        var text = Text.of(message);
        for (PlayerEntity player: players)
            player.sendMessage(text);
    }

    public static void sendMessage(String message, CommandContext<ServerCommandSource> context) {
        context.getSource().sendMessage(Text.of(message));
    }
}
