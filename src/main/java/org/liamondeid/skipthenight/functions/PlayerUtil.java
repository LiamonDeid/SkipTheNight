package org.liamondeid.skipthenight.functions;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Utility for player grouping.
 */
public class PlayerUtil {

    /**
     * Returns list of players which sleep in specific world
     *
     * @param world a specific world
     * @return list of players
     */
    public static ArrayList<Player> getSleeping(@NotNull World world) {
        ArrayList<Player> players = new ArrayList<>();

        for (Player player: world.getPlayers()) {
            if (player.isSleeping()) players.add(player);
        }

        return players;
    }
}
