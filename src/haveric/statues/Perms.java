package haveric.statues;

public class Perms {
	
	// TODO: make these defaults and load from file
	private static String build = "statues.build";
	private static String ignoreCost = "statues.ignoreCost";
	private static String destroyLand = "statues.destroyLand";
	private static String adjust = "statues.adjust";	
	
	public static void setBuild(String newPerm){
		build = newPerm;
		// TODO: set other stuff
	}
	
	public static String getBuild(){
		return build;
	}
	
	public static void setIgnoreCost(String newPerm){
		ignoreCost = newPerm;
		// TODO: set other stuff
	}
	
	public static String getIgnoreCost(){
		return ignoreCost;
	}
	
	public static void setDestroyLand(String newPerm){
		destroyLand = newPerm;
		// TODO: more stuff
	}
	
	public static String getDestroyLand(){
		return destroyLand;
	}
	
	public static void setAdjust(String newPerm){
		adjust = newPerm;
		// TODO: more stuff
	}
	
	public static String getAdjust(){
		return adjust;
	}
}
