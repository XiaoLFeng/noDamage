package com.poompk.noDamage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.poompk.noDamage.Gui.Gui;

public class Main extends JavaPlugin{
	private static Main instance;
	public static String[] cmdlist = new String[8];
	public static boolean PlaceholderAPI = false;
	private FileConfiguration ItemJoinConfig = null;
	private File ItemJoinFile = null;
	public Main() {
		instance = this;
	}
	public static Main getInstance() {
		return instance;
	}
	public void cc(ConsoleCommandSender a,String s) {
		a.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
	}
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveConfig();
		saveItemJoinDefaultConfig();
		saveItemJoinConfig();
	}
	public void onEnable() {
		Server server = Bukkit.getServer();
		PluginManager pm = Bukkit.getPluginManager();
		PluginDescriptionFile pdf = getDescription();
		ConsoleCommandSender a = server.getConsoleSender();
		StringBuilder sb = new StringBuilder();
		sb.append("&enoDamage "+pdf.getVersion()+" by PoomPK\n");
		sb.append("&fChecking\n");
		
		if(pm.isPluginEnabled("PlaceholderAPI")) {
			PlaceholderAPI = true;
			sb.append("  &aPlaceholderAPI\n");
		} else{
			sb.append("  &cPlaceholderAPI\n");
		}
		//CheckVersionSystem(a);
		sb.append(CheckVersionSystem());
		sb.append("&bnoDamage Enabled!\n");
		pm.registerEvents(new PreventPlayer(), this);
		pm.registerEvents(new allevent(), this);
		pm.registerEvents(new ItemJoin(), this);
		pm.registerEvents(new Gui(), this);
		getCommand("nodamage").setExecutor(new Commands());
		cmdlist[0]=(" &6/nodamage help <page>");
		cmdlist[1]=(" &6/nodamage setspawn");
		cmdlist[2]=(" &6/nodamage spawn");
		cmdlist[3]=(" &6/nodamage blockcmd");
		cmdlist[4]=(" &6/nodamage gui");
		cmdlist[5]=(" &6/nodamage version");
		cmdlist[6]=(" &6/nodamage author");
		cmdlist[7]=(" &6/nodamage reload");
		loadConfig();
		cc(a, sb.toString());
	}
	public void reloadItemJoinConfig() {
		if (ItemJoinFile == null) {
			ItemJoinFile = new File(getDataFolder(), "ItemJoin.yml");
		}
		ItemJoinConfig = YamlConfiguration.loadConfiguration(ItemJoinFile);
		try {
			Reader defConfigStream = new InputStreamReader(getResource("ItemJoin.yml"), "UTF-8");
			if (defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				ItemJoinConfig.setDefaults(defConfig);
			}
		} catch (Exception e) {
			
		}
	}

	public FileConfiguration getItemJoinConfig() {
		if (ItemJoinConfig == null) {
			reloadItemJoinConfig();
		}
		return ItemJoinConfig;
	}

	public void saveItemJoinConfig() {
		if (ItemJoinConfig == null || ItemJoinFile == null) {
			return;
		}
		try {
			getItemJoinConfig().save(ItemJoinFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + ItemJoinFile, ex);
		}
	}

	public void saveItemJoinDefaultConfig() {
		if (ItemJoinFile == null) {
			ItemJoinFile = new File(getDataFolder(), "ItemJoin.yml");
		}
		if (!ItemJoinFile.exists()) {
			this.saveResource("ItemJoin.yml", false);
		}
	}
	public static String CheckVersionSystem() {
		try {
			URL url = new URL("http://pastebin.com/raw/KMeU3xaa");
			BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
			double version_this = Double.parseDouble(Main.getInstance().getDescription().getVersion());
			double version_new = Double.parseDouble(bf.readLine());
			if(version_this < version_new) {
				StringBuilder sb = new StringBuilder();
				sb.append("&c  Version outdated You must update noDamage to verion "+version_new+"!\n");
				sb.append("&e  https://www.spigotmc.org/resources/42876/\n");
				return sb.toString();
				//sender.sendMessage(ChatColor.translateAlternateColorCodes('&',sb.toString()));
			} else {
				return "&f  Version: &bLatest version\n";
			}
		} catch (Exception e) {
			return "&c  Checking version failed!\n";
		}
	}
}
