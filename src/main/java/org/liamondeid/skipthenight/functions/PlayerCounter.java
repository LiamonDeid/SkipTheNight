package org.liamondeid.skipthenight.functions;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerCounter {

    private final World world;

    public PlayerCounter(World world) {
        this.world = world;
    }

    public int getAll() {
        int count = 0;

        for (Player player: world.getPlayers()) {
            count++;
        }

        return count;
    }

    public int getSleeping() {
        int count = 0;

        for (Player player: world.getPlayers()) {
            if (player.isSleeping()) count++;
        }

        return count;
    }
}
