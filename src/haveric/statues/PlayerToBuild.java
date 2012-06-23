package haveric.statues;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerToBuild {

    private static HashMap<Player, String> players = new HashMap<Player, String>();

    public static String getPlayer(Player p) {
        String toBuild;
        if (players.containsKey(p)) {
            toBuild = players.get(p);
        } else {
            toBuild = p.getName();
        }
        return toBuild;
    }

    public static void setPlayer(Player key, String val) {
        players.put(key, val);
    }

    public static void removePlayer(Player p) {
        if (players.containsKey(p)) {
            players.remove(p);
        }
    }
}
