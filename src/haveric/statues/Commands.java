package haveric.statues;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{

	Statues plugin;
	
	Permission perm;
	// Commands
	public static String cmdBuild = "build";
	
	public static String cmdMain    = "statue";
	public static String cmdMainAlt = "statues";
	
	public static String cmdHelp = "help";

	public Commands(Statues st) {
		plugin = st;
	}



	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		ChatColor msgColor = ChatColor.DARK_AQUA;
		ChatColor highlightColor = ChatColor.YELLOW;
		
		String title = msgColor + "[" + ChatColor.GRAY + "Statues" + msgColor + "] ";

		
		if(sender.isOp() || (perm != null && perm.has((Player)sender, Perms.getBuild()))){
			if (commandLabel.equalsIgnoreCase(cmdMain) || commandLabel.equalsIgnoreCase(cmdMainAlt)){
				
				if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase(cmdHelp))){
					sender.sendMessage(title+"github.com/haveric/statues - v" + plugin.getDescription().getVersion());
					sender.sendMessage(msgColor + "With wool right click a diamond block to construct the statue");
					sender.sendMessage("/statue build - " + msgColor + "Sets statue name to your name.");
					sender.sendMessage("/statue build [name] - " + msgColor +"Sets the statue name to [name]");
				} else if (args.length == 1){
					if (args[0].equalsIgnoreCase(cmdBuild)){
						sender.sendMessage(msgColor+"With wool, right click a diamond block to construct the statue of your player.");
						PlayerToBuild.removePlayer((Player)sender);
					}
				} else if (args.length == 2){
					if (args[0].equalsIgnoreCase(cmdBuild)){
						sender.sendMessage(msgColor+"With wool, right click a diamond block to construct "+highlightColor+args[1]+"'s"+msgColor+" statue");
						PlayerToBuild.setPlayer((Player)sender, args[1]);
					}
				}
			}
		} else {
			sender.sendMessage(title+" You do not have access to this command.");
		}
		return false;
	}
	
	public static String getBuild() {
		return cmdBuild;
	}

	public static void setBuild(String cmd) {
		cmdBuild = cmd;
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
