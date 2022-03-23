package org.liamondeid.skipthenight;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.liamondeid.skipthenight.listeners.SleepListener;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        if (getConfig().getInt("numbers.percent-sleeping-need") > 100) getLogger().warning("\"numbers.percent-sleeping-need\" in config can't be more 100");

        Bukkit.getPluginManager().registerEvents(new SleepListener(), this);

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }
}
