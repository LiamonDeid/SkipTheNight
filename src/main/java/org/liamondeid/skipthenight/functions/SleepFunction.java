package org.liamondeid.skipthenight.functions;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.liamondeid.skipthenight.Main;
import org.liamondeid.skipthenight.messaging.MessageSender;

import java.util.ArrayList;
import java.util.HashMap;

public class SleepFunction {

    private static final HashMap<World, String> initiators = new HashMap<>();

    private static final int durationOfEffects = Main.getInstance().getConfig().getInt("numbers.duration-of-effects");

    private static final FileConfiguration config = Main.getInstance().getConfig();

    public static boolean canSleep(@NotNull PlayerCounter playerCounter) {
        float percentOfSleeping =  playerCounter.getSleeping() / (float) playerCounter.getAll() * 100;

        return percentOfSleeping >= config.getInt("numbers.percent-sleeping-need");
    }

    public static int getCountOfRequiredPlayers(@NotNull PlayerCounter playerCounter) {
        return (int) Math.ceil(config.getInt("numbers.percent-sleeping-need") * playerCounter.getAll() / 100.0f);
    }

    public static void turningOn(World world) {
        Runnable task = () -> {
            world.setTime(23975L);
            MessageSender.sendToPlayersInWorld(world, "messages.after-night-skip", ChatMessageType.CHAT);

            ArrayList<Player> sleepingPlayers = PlayerUtil.getSleeping(world);

            for (Player player: sleepingPlayers) {
                player.wakeup(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, durationOfEffects  * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, durationOfEffects * 20, 0));
            }
        };
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), task, 20L);
    }

    public static String getInitiator(World world) {
        return initiators.get(world);
    }

    public static void addInitiator(World world, String name) {
        if (getInitiator(world) != null) initiators.replace(world, name);
        else initiators.put(world, name);
    }
}
