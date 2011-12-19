package haveric.statues;

public class Perms {
	
	private static String build = "statues.build";
	private static String ignoreCost 	= "statues.ignoreCost";
	
	
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
}
