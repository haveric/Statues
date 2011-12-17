package haveric.statues;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Statues extends JavaPlugin{
	public static final Logger log = Logger.getLogger("Minecraft");
	private final StPlayerInteract playerInteract = new StPlayerInteract(this);

	// Vault
	private Economy econ;
	private Permission perm;

	// Perms
	public String permBuild = "statues.build";
	public String permIC 	= "statues.ignorecost";
	public String permICAlt = "statues.ignoreCost";
	
	// Commands
	public String cmdBuild = "build";
	
	public String cmdStatue    = "statue";
	public String cmdStatueAlt = "statues";
	
	public String cmdHelp = "help";
	
	
	public String playerToBuildName = null;

	@Override
	public void onEnable() {		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerInteract, Event.Priority.Normal, this);
		
        setupVault();
        
        log.info(String.format("[%s] v%s Started",getDescription().getName(), getDescription().getVersion()));
	}
	
	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled",getDescription().getName()));
	}
	
    public void setupVault() {       
        RegisteredServiceProvider<Permission> permProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permProvider != null) {
            perm = permProvider.getProvider();
        }

        RegisteredServiceProvider<Economy> econProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (econProvider != null) {
            econ = econProvider.getProvider();
        }
    }
    
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		String title = ChatColor.DARK_AQUA + "[" + ChatColor.GRAY + "Statues" + ChatColor.DARK_AQUA + "] ";
		String msgColor = ChatColor.DARK_AQUA;
		String highlightColor = ChatColor.DARK_YELLOW;
		
		if(sender.isOp() || (perm != null && perm.has((Player)sender, permBuild))){
			if (commandLabel.equalsIgnoreCase(cmdStatue) || commandLabel.equalsIgnoreCase(cmdStatueAlt)){
				
				if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase(cmdHelp))){
					sender.sendMessage(title+"github.com/haveric/statues - v1.0");
					sender.sendMessage(msgColor+"With wool right click a diamond block to construct the statue");
					sender.sendMessage(msgColor+"/statue build - Sets statue name to your name.");
					sender.sendMessage(msgColor+"/statue build [name] - Sets the statue name to [name]");
				} else if (args.length == 1){
					if (args[0].equalsIgnoreCase(cmdBuild)){
						sender.sendMessage(msgColor+" With wool right click a diamond block to construct the statue of your player.");
						playerToBuildName = null;
					}
				} else if (args.length == 2){
					if (args[0].equalsIgnoreCase(cmdBuild)){
						sender.sendMessage(msgColor+"With wool right click a diamond block to construct "+hightlightColor+args[1]+"'s"+msgColor+" statue");
						playerToBuildName = args[1];
					}
				}
			}
		} else {
			sender.sendMessage(title+" You do not have access to this command.");
		}
		return false;
	}
	
    public Permission getPerm(){
    	return perm;
    }
    
    public Economy getEcon(){
    	return econ;
    }
}
