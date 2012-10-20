package com.applerox.jack.AppleCommands.commandFiles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.applerox.jack.AppleCommands.AppleCommands;

public class CommandLevel implements CommandExecutor {
	
	AppleCommands plugin;
	
	public CommandLevel(AppleCommands plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command com, String l, String[] options) {
		if (com.getName().equalsIgnoreCase("level") && plugin.hasPerm(sender, "applecommands.level")) {
			
		}
		return false;
	}

}
