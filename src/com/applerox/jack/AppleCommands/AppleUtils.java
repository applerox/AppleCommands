package com.applerox.jack.AppleCommands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AppleUtils {
	public AppleCommands plugin;
	public static Logger logger = Logger.getLogger("Minecraft");
	
	public AppleUtils(AppleCommands plugin) {
		this.plugin = plugin;
	}
	
	public String messageHeader(String s) {
		String message = (ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "AppleCommands" + ChatColor.GRAY + "] " + s);
		String wrappedMessage = wrapToChatBox(message);
		return wrappedMessage;
	}
	
	public String colorize(String original) {
		/*ChatColor[] colors = new ChatColor[16];
		*colors[0]  = ChatColor.BLACK;
		*colors[1]  = ChatColor.DARK_BLUE;
		*colors[2]  = ChatColor.DARK_GREEN;
		*colors[3]  = ChatColor.DARK_AQUA;
		*colors[4]  = ChatColor.DARK_RED;
		*colors[5]  = ChatColor.DARK_PURPLE;
		*colors[6]  = ChatColor.GOLD;
		*colors[7]  = ChatColor.GRAY;
		*colors[8]  = ChatColor.DARK_GRAY;
		*colors[9]  = ChatColor.BLUE;
		*colors[10] = ChatColor.GREEN;
		*colors[11] = ChatColor.AQUA;
		*colors[12] = ChatColor.RED;
		*colors[13] = ChatColor.LIGHT_PURPLE;
		*colors[14] = ChatColor.YELLOW;
		*colors[15] = ChatColor.WHITE;
		*/
		String colored = original.replaceAll("/&0/", "§0");
		colored = colored.replaceAll        ("/&1/", "§1");
		colored = colored.replaceAll        ("/&2/", "§2");
		colored = colored.replaceAll        ("/&3/", "§3");
		colored = colored.replaceAll        ("/&4/", "§4");
		colored = colored.replaceAll        ("/&5/", "§5");
		colored = colored.replaceAll        ("/&6/", "§6");
		colored = colored.replaceAll        ("/&7/", "§7");
		colored = colored.replaceAll        ("/&8/", "§8");
		colored = colored.replaceAll        ("/&9/", "§9");
		colored = colored.replaceAll        ("/&a/", "§a");
		colored = colored.replaceAll        ("/&b/", "§b");
		colored = colored.replaceAll        ("/&c/", "§c");
		colored = colored.replaceAll        ("/&d/", "§d");
		colored = colored.replaceAll        ("/&e/", "§e");
		colored = colored.replaceAll        ("/&f/", "§f");
		
		return colored;
	}
	
	
	//Surprisingly easy for what it does.
	public String wrapToChatBox(String original) {
		String newString = original.replaceAll("(.{75})", "$1\n");
		return newString;
	}
	
	public void notifyAllOps(String message) {
		Player[] ops = new Player[0];
		
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.isOp()) {
				ops[ops.length + 1] = player;
			}
		}
		
		for (Player op : ops) {
			String newMessage;
			newMessage = wrapToChatBox(message);
			newMessage = colorize(newMessage);
			newMessage = messageHeader(newMessage);
			
			op.sendMessage(newMessage);
			
		}
		
	}
	
}
