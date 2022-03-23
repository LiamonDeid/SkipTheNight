package org.liamondeid.skipthenight.messaging;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.liamondeid.skipthenight.Main;
import org.liamondeid.skipthenight.functions.PlayerCounter;
import org.liamondeid.skipthenight.functions.SleepFunction;

public class MessageSender {

    public static void sendToPlayer(@NotNull Player player, String path, @NotNull ChatMessageType messageType) {
        String message = Main.getInstance().getConfig().getString(path, "empty-msg");

        PlayerCounter playerCounter = new PlayerCounter(player.getWorld());

        String playerName = SleepFunction.getInitiator(player.getWorld());
        String countOfRequiredPlayers = String.valueOf(SleepFunction.getCountOfRequiredPlayers(playerCounter));
        String alreadySleeping = playerCounter.getSleeping() >= SleepFunction.getCountOfRequiredPlayers(playerCounter) ?
                "&a" + playerCounter.getSleeping() + "&f" : "&c" + playerCounter.getSleeping() + "&f";


        playerCounter = null;

        message = message.
                replaceAll("%playerName%", playerName).
                replaceAll("%alreadySleeping%", alreadySleeping).
                replaceAll("%countOfRequiredPlayers%", countOfRequiredPlayers);
        switch (messageType) {
            case ACTION_BAR:
                player.sendActionBar(ChatColor.translateAlternateColorCodes('&', message));
                break;
            case CHAT:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                break;
        }
    }

    public static void sendToPlayersInWorld(@NotNull World world, String path, ChatMessageType messageType) {
        for (Player player: world.getPlayers()) {
            sendToPlayer(player, path, messageType);
        }
    }

}
