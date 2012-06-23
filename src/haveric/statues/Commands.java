package haveric.statues;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
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

    public Commands(Statues st) {
        plugin = st;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Permission perm = plugin.getPerm();
        ChatColor msgColor = ChatColor.DARK_AQUA;
        ChatColor highlightColor = ChatColor.YELLOW;
        ChatColor curColor = ChatColor.GOLD;
        ChatColor defColor = ChatColor.WHITE;

        String title = msgColor + "[" + ChatColor.GRAY + plugin.getDescription().getName() + msgColor + "] ";


        if (sender.isOp() || (perm != null && perm.has((Player) sender, Perms.getBuild()))) {
            if (commandLabel.equalsIgnoreCase(cmdMain) || commandLabel.equalsIgnoreCase(cmdMainAlt)) {

                if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase(cmdHelp))) {
                    sender.sendMessage(title + "github.com/haveric/statues - v" + plugin.getDescription().getVersion());
                    sender.sendMessage(msgColor + "With wool right click a diamond block to construct the statue");
                    sender.sendMessage("/" + cmdMain + " [name] (" + curColor + PlayerToBuild.getPlayer((Player) sender) + defColor + ") - " + msgColor + "Change the next statue built to [name]");
                } else if (args.length == 1) {
                    sender.sendMessage(msgColor + "With wool, right click a diamond block to construct " + highlightColor + args[0] + "'s" + msgColor + " statue");
                    PlayerToBuild.setPlayer((Player) sender, args[0]);
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

    public static void setMain(String cmd) {
        cmdMain = cmd;
    }

    public static String getMainAlt() {
        return cmdMainAlt;
    }

    public static void setMainAlt(String cmd) {
        cmdMainAlt = cmd;
    }

    public static String getHelp() {
        return cmdHelp;
    }

    public static void setHelp(String cmd) {
        cmdHelp = cmd;
    }
}
