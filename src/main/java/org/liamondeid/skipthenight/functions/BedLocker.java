package org.liamondeid.skipthenight.functions;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.liamondeid.skipthenight.Main;
import org.liamondeid.skipthenight.messaging.MessageSender;

import java.util.HashMap;

public class BedLocker {

    private static final HashMap<Player, Integer> attemptsOfPlayers = new HashMap<>();

    private static final HashMap<Player, Integer> taskIds = new HashMap<>();

    private static final HashMap<Player, Boolean> lockStatus = new HashMap<>();

    private static final FileConfiguration config = Main.getInstance().getConfig();

    public static void addToAttempts(Player player) {
        if (taskIds.containsKey(player)) {
            Bukkit.getScheduler().cancelTask(taskIds.get(player));
            taskIds.remove(player);
        }
        if (!attemptsOfPlayers.containsKey(player)) attemptsOfPlayers.put(player, 0);

        attemptsOfPlayers.replace(player, attemptsOfPlayers.get(player) + 1);

        int value = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            attemptsOfPlayers.replace(player, 0);
        }, config.getInt("numbers.time-for-clearing-attempts-counter") * 20L).getTaskId();
        taskIds.put(player, value);

        if (attemptsOfPlayers.get(player) == 3) {
            Bukkit.getScheduler().cancelTask(taskIds.get(player));
            taskIds.remove(player);

            MessageSender.sendToPlayer(player, "messages.player-receive-a-locking", ChatMessageType.CHAT);

            if (lockStatus.containsKey(player)) lockStatus.replace(player, true);
            lockStatus.put(player, true);

            value = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                attemptsOfPlayers.replace(player, 0);
                lockStatus.replace(player, false);
                MessageSender.sendToPlayer(player, "messages.player-released-from-locking", ChatMessageType.CHAT);
            }, config.getInt("numbers.lock-time") * 20L).getTaskId();
            taskIds.put(player, value);
        }
    }

    public static boolean isLocked(Player player) {
        if(lockStatus.containsKey(player)) return lockStatus.get(player);
        return false;
    }

}
