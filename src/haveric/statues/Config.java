package haveric.statues;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    private static Statues plugin;

    // Config variables
    private static FileConfiguration config;
    private static File configFile;

    private static String cfgChatty = "Chatty Messages";
    private static String cfgCost = "Cost to create";

    // Defaults
    public static final double COST_DEFAULT = 10000.0;
    public static final boolean CHATTY_DEFAULT = false;

    public static void init(Statues st) {
        plugin = st;
        configFile = new File(plugin.getDataFolder() + "/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void setup() {
        double cost = config.getDouble(cfgCost, COST_DEFAULT);
        if (cost < 0) {
            cost = 0;
            // TODO: warning message
        }
        config.set(cfgCost, cost);

        boolean chatty = config.getBoolean(cfgChatty, CHATTY_DEFAULT);
        config.set(cfgChatty, chatty);

        saveConfig();
    }

    public static void setCost(double newCost) {
        if (newCost < 0) {
            newCost = 0.0;
            // TODO: warn user?
        }
        config.set(cfgCost, newCost);
        saveConfig();
    }

    public static double getCost() {
        return config.getDouble(cfgCost);
    }

    public static boolean isChatty() {
        return config.getBoolean(cfgChatty);
    }

    public static void setChatty(boolean newChatty) {
        config.set(cfgChatty, newChatty);
        saveConfig();
    }

    private static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
