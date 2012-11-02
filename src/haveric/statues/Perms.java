package haveric.statues;

import org.bukkit.entity.Player;

public class Perms {

    private static String permBuild = "statues.build";
    private static String permIgnoreCost = "statues.ignorecost";
    private static String permDestroyLand = "statues.destroyland";
    private static String permAdjust = "statues.adjust";

    public static boolean canBuild(Player player) {
    	return player.hasPermission(permBuild);
    }
    
    public static boolean hasIgnoreCost(Player player) {
    	return player.hasPermission(permIgnoreCost);
    }
    
    public static boolean canDestroyLand(Player player) {
    	return player.hasPermission(permDestroyLand);
    }
    
    public static boolean canAdjust(Player player) {
    	return player.hasPermission(permAdjust);
    }
}
