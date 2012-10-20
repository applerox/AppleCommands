/*
 * @author Jack Stratton
 * <jack@applerox.com>
 * 
 * LICENSE:
 * 
 * This plugin is under a "Do what you want, let me know" policy.
 * You're free to fork and submit pull requests, but if you post a copy of this on the Bukkit site, that's not allowed.
 * 
 * @TODO: Learn more about Bukkit coding.
 * 
 * 
 */

package com.applerox.jack.AppleCommands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.applerox.jack.AppleCommands.commandFiles.*;

public class AppleCommands extends JavaPlugin {
	
	// Variables
	public static AppleCommands ac;
	public Logger logger = Logger.getLogger("Minecraft"); // Loggers = derpy
	public YMLManager yml;
	public static File confFolder;
	
	public static Permission perms;
	public static Chat chat;
	
	public static String version = ac.getDescription().getVersion();
	
	public static List<String> blacklistedCommands = new ArrayList<String>();
	public List<String> motd = new ArrayList<String>();
	public List<String> whitelist = new ArrayList<String>();
	public static Map<String, Map<String, Object>> commands, defaults;
	public final Map<String, FileConfiguration> confs = new HashMap<String, FileConfiguration>();
	//public String[] logLevels = new String[4];
	
	//logLevels[0] = "debug";
	//logLevels[1] = "info";
	//logLevels[2] = "warning";
	//logLevels[3] = "severe";
	
	// Default config items @TODO
	
	// Vault Setup
	private static boolean setupChat() {
    	if (ac.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = ac.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
    // Bukkit onEnable, onDisable
	@Override
	public void onEnable() {
		if (!setupChat()) {
			logger.severe("[AppleCommands] Vault not found. Disabling plugin.");
			disableAppleCommands();
			return;
		}
		setupPermissions();
		setupChat();
		
		ac = this;
		confFolder = getDataFolder();
		
		commands = getDescription().getCommands();
		version = getDescription().getVersion();
		
		// Ugh...
		/*registerCommand(new CommandLevel(this), "level", this);
		*registerCommand(new CommandTalk(this), "talk", this);
		*registerCommand(new CommandIsBanned(this), "isbanned", this);
		*registerCommand(new CommandArmor(this), "armor", this);
		*registerCommand(new CommandIP(this), "ip", this);
		*registerCommand(new CommandSetGroup(this), "setgroup", this);
		*registerCommand(new CommandForceTeleport(this), "forceteleport", this);
		*registerCommand(new CommandForceTeleportHere(this), "forceteleporthere", this);
		*registerCommand(new CommandSmite(this), "smite", this);
		*registerCommand(new CommandGiveItem(this), "give", this);
		*registerCommand(new CommandSendMessage(this), "sendmessage", this);
		*registerCommand(new CommandReplyMessage(this), "reply", this);
		*registerCommand(new CommandInvClear(this), "invclear", this);
		*registerCommand(new CommandSetWeather(this), "setweather", this);
		*/
		
		
		logger.log(Level.INFO, "[AppleCommands] Plugin Enabled!");
	}
	
	@Override
	public void onDisable() {
		logger.log(Level.INFO, "[AppleCommands] Plugin Disabled!");
		disableAppleCommands();
	}
	
	// Error? Disable AppleCommands
	public void disableAppleCommands() {
		getServer().getPluginManager().disablePlugin(this);
	}
	
	// Permission checks
	public boolean isAllowed(final Player p, final String perm) {
		return !(p != null) || (AppleCommands.perms.has(p.getWorld(), p.getName(), "applecommands.all")) || AppleCommands.perms.playerHas(p.getWorld(),  p.getName(), perm);
	}
	
	public boolean isAllowed(final OfflinePlayer p, final String perm) {
		String world = getServer().getWorlds().get(0).getName();
		return !(p instanceof Player) && !(p != null) || (AppleCommands.perms.has(world, p.getName(), "applecommands.all")) || AppleCommands.perms.has(world, p.getName(), perm);
	}
	public boolean isAllowed(final CommandSender p, final String perm) {
		return !(p instanceof Player) && !(p instanceof OfflinePlayer) || (AppleCommands.perms.has(p, "applecommands.all")) || AppleCommands.perms.has(p, perm);
	}
	public static boolean hasPerm(final CommandSender p, final String perm) {
		return !(p instanceof Player) && !(p instanceof OfflinePlayer) || (AppleCommands.perms.has(p, "applecommands.all")) || AppleCommands.perms.has(p, perm);
	}
	
	// String builder (PHP implode())
	public static String finalArg(String[] strings, int pos) {
		StrBuilder sb = new StrBuilder();
		for (int i=pos;i < strings.length; i++) {
			sb.append(strings[i]);
			sb.append(" ");
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	// Command Registration
	private void registerCommand(CommandExecutor ce, String com, JavaPlugin plugin) {
		if (AppleCommands.blacklistedCommands.contains(com)) return;
		plugin.getCommand(com).setExecutor(ce);
	}
	
	// Create default configs
	private void createDefaultItem(File file, String def) {
		if (!file.exists()) {
			try {
				if (file.createNewFile()) {
					try {
						FileWriter filestream = new FileWriter(file.getAbsolutePath());
						BufferedWriter out = new BufferedWriter(filestream);
						out.write(def);
						out.close();
					}
					catch (Exception e) {
						logger.severe("[AppleCommands] Couldn't write to a config!");
						e.printStackTrace();
					}
					logger.info("[AppleCommands] Created Config File!");
				}
			}
			catch (Exception e) {
				logger.severe("[AppleCommands] Failed to create config file!");
				e.printStackTrace();
			}
		}
	}
	
	public static boolean resetConfigFile(File configFile, boolean deleteOld) {
		if (deleteOld && configFile.exists()) {
			configFile.delete();
			try {
				//createDefaultItems(configFile, defaults);
				return true;
			}
			catch (Exception e) {
				ac.logger.severe("[AppleCommands] Failed to reset configuration! Let AppleCommands devs know this:");
				e.printStackTrace();
			}
		}
		else if (configFile.exists() && !deleteOld) {
			try {
				FileReader instream   = new FileReader(configFile);
				FileWriter outstream  = new FileWriter(configFile);
				BufferedReader reader = new BufferedReader(instream);
				BufferedWriter writer = new BufferedWriter(outstream);
				
				//for (line : reader.getLines()) {
				//	li
				//}
				
				instream.close();
				outstream.close();
			} catch (Exception e) {
				ac.logger.severe("[AppleCommands] Failed to reset configuration! Let AppleCommands devs know this!");
				e.printStackTrace();
			}
		}
		else if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			}
			catch (Exception e) {
				ac.logger.severe("[AppleCommands] Failed to create configuration file! Let AppleCommands devs know this!");
			}
			
		}
		
		return true;
	}
	
	// Load config
	public void loadConfig() {
		// @TODO: This bit
	}
}
