package org.liamondeid.skipthenight.listeners;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.liamondeid.skipthenight.Main;
import org.liamondeid.skipthenight.functions.BedLocker;
import org.liamondeid.skipthenight.functions.PlayerCounter;
import org.liamondeid.skipthenight.functions.SleepFunction;
import org.liamondeid.skipthenight.messaging.MessageSender;

public class SleepListener implements Listener {

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if(event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) return;

        if (BedLocker.isLocked(event.getPlayer())) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(),() -> {
                MessageSender.sendToPlayer(event.getPlayer(), "messages.punished-player-tries-to-sleep" , ChatMessageType.ACTION_BAR);
            }, 0L);
            event.setUseBed(Event.Result.DENY);
            return;
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            PlayerCounter playerCounter = new PlayerCounter(event.getPlayer().getWorld());

            if(playerCounter.getSleeping() == 1) {
                SleepFunction.addInitiator(event.getPlayer().getWorld(), event.getPlayer().getName());
                MessageSender.sendToPlayersInWorld(event.getPlayer().getWorld(), "messages.first-player-lay-to-sleep", ChatMessageType.CHAT);
            }
            MessageSender.sendToPlayersInWorld(event.getPlayer().getWorld(),"messages.other-players-lay-to-sleep", ChatMessageType.ACTION_BAR);

            if (SleepFunction.canSleep(playerCounter)) SleepFunction.turningOn(event.getPlayer().getWorld());
        }, 0L);
        BedLocker.addToAttempts(event.getPlayer());
    }

    @EventHandler
    public void onPlayerWakeUp(PlayerBedLeaveEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (event.getPlayer().getWorld().getTime() >= 23976) return;
            MessageSender.sendToPlayersInWorld(event.getPlayer().getWorld(),"messages.other-players-lay-to-sleep", ChatMessageType.ACTION_BAR);
        }, 0L);
    }
}
