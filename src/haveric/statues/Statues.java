package haveric.statues;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Statues extends JavaPlugin{
	public static final Logger log = Logger.getLogger("Minecraft");
	private final StPlayerInteract playerInteract = new StPlayerInteract(this);
	private Commands commands = new Commands(this);

	// Vault
	private Economy econ;
	private Permission perm;
	

	@Override
	public void onEnable() {		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerInteract, Event.Priority.Normal, this);
		
		Config.init(this);
		
		setupVault();
        
        Config.setup();
        
        getCommand(Commands.getMain()).setExecutor(commands);
        getCommand(Commands.getMainAlt()).setExecutor(commands);
        
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
    
	
    public Permission getPerm(){
    	return perm;
    }
    
    public Economy getEcon(){
    	return econ;
    }
}
