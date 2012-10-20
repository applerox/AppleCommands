package com.applerox.jack.AppleCommands;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class YMLManager {

	//Configuration Vars + Functions
	private FileConfiguration conf;
	private File confLoc;
	private File confFolder;
	
	public YMLManager(String filename) {
		confFolder = AppleCommands.confFolder;
		synchronized (AppleCommands.ac) {
			
		}
	}
}
