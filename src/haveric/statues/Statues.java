package haveric.statues;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Statues extends JavaPlugin {
    static Logger log;
    private final StPlayerInteract playerInteract = new StPlayerInteract(this);
    private Commands commands = new Commands(this);


    // Vault
    private Economy econ;

    @Override
    public void onEnable() {
        log = getLogger();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerInteract, this);

        Config.init(this);
        ColorConfig.init(this);

        setupVault();

        Config.setup();
        ColorConfig.setup();

        getCommand(Commands.getMain()).setExecutor(commands);
    }

    @Override
    public void onDisable() {

    }

    public void setupVault() {
        RegisteredServiceProvider<Economy> econProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (econProvider != null) {
            econ = econProvider.getProvider();
        }
    }

    public Economy getEcon() {
        return econ;
    }
}
