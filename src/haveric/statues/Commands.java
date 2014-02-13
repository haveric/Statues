package haveric.statues;

import haveric.statues.Statue.Direction;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private static Statues plugin;

    // Commands
    private static String cmdMain    = "statue";
    private static String cmdMainAlt = "statues";
    private static String cmdHelp = "help";
    private static String cmdHere = "here";
    private static String cmdRestore = "restore";
    private static String cmdDestroy = "destroy";


    public Commands(Statues st) {
        plugin = st;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ChatColor msgColor = ChatColor.DARK_AQUA;
        ChatColor highlightColor = ChatColor.YELLOW;
        ChatColor curColor = ChatColor.GOLD;
        ChatColor defColor = ChatColor.WHITE;

        String title = msgColor + "[" + ChatColor.GRAY + plugin.getDescription().getName() + msgColor + "] ";


        if (Perms.canBuild((Player) sender)) {
            if (commandLabel.equalsIgnoreCase(cmdMain) || commandLabel.equalsIgnoreCase(cmdMainAlt)) {

                if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase(cmdHelp))) {
                    sender.sendMessage(title + "github.com/haveric/statues - v" + plugin.getDescription().getVersion());
                    sender.sendMessage(msgColor + "With wool right click a diamond block to construct the statue");
                    sender.sendMessage("/" + cmdMain + " [name] (" + curColor + PlayerToBuild.getPlayer((Player) sender) + defColor + ") - " + msgColor + "Change the next statue built to [name]");
                } else if (args.length >= 1 && args[0].equalsIgnoreCase(cmdHere)) {

                    if (sender instanceof Player) {
                        Player player = (Player) sender;


                        String playerName = PlayerToBuild.getPlayer(player);
                        boolean currencyEnabled = true;
                        boolean canBuild = Perms.canBuild(player);
                        if (canBuild) {
                            Economy econ = plugin.getEcon();
                            if (econ == null || Perms.hasIgnoreCost(player)) {
                                currencyEnabled = false;
                            } else if (!econ.has(player.getName(), Config.getCost())) {
                                player.sendMessage(ChatColor.RED + "Not enough money to create a statue. Need " + Config.getCost());
                                canBuild = false;
                            }

                            if (canBuild) {
                                Block block = player.getTargetBlock(null, 100);
                                World world = block.getWorld();

                                Statue s = new Statue();

                                Direction direction = Statue.getDirection(player);


                                int[][][] pixelData = s.loadTexture(player, playerName);

                                if (pixelData != null){
                                    if (Config.isChatty()) {
                                        player.sendMessage("Creating pixel mapping matrix for woolBit color space");
                                        player.sendMessage("Shearing sheep...");
                                    }

                                    s.createStatue(world, direction, block.getX(), block.getY(), block.getZ(), pixelData);

                                    if (currencyEnabled) {
                                        econ.withdrawPlayer(player.getName(), Config.getCost());
                                    }

                                    if (Config.isChatty()) {
                                        player.sendMessage("Boom! Look at that statue of " + playerName + "!");
                                    }
                                }
                            }
                        }
                    }

                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase(cmdRestore)) {

                    } else if (args[0].equalsIgnoreCase(cmdDestroy)) {

                    } else {
                        sender.sendMessage(msgColor + "With wool, right click a diamond block to construct " + highlightColor + args[0] + "'s" + msgColor + " statue");
                        PlayerToBuild.setPlayer((Player) sender, args[0]);
                    }
                }
            }
        } else {
            sender.sendMessage(title + " You do not have access to this command.");
        }
        return false;
    }

    public static String getMain() {
        return cmdMain;
    }
}
