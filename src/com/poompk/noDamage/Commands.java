package com.poompk.noDamage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.poompk.noDamage.Gui.Gui;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String acmdLabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("nodamage")) {
				if(p.hasPermission("nodamage.admin") == false) {
					utils.color(p, Main.getInstance().getConfig().getString("donthaveperm"));
					return false;
				}
				if(args.length == 0) {
					utils.arrayPage(p, "help",utils.getCmdList(), 1);
					return false;
				}
				if(args[0].equalsIgnoreCase("help")) {
					if(args.length == 1) {
						utils.arrayPage(p, "help", utils.getCmdList(), 1);
						return false;
					}
					if(args[1] != null) {
						try {
						int page = Integer.parseInt(args[1]);
						if(page == 0) {
							page = 1;
						}
						utils.arrayPage(p, "help", utils.getCmdList(), page);
						} catch (Exception e) {
							utils.color(p, "&c/nodamage &7<page>");
						}
					}
				} else if(args[0].equalsIgnoreCase("setspawn")) {
					utils.setSpawn(p);
				} else if(args[0].equalsIgnoreCase("spawn")) {
					utils.TeleportToSpawn(p);
				} else if(args[0].equalsIgnoreCase("blockcmd")) {
					if(args.length == 1) {
						utils.color(p, "");
						utils.color(p, "&a/nodamage blockcmd add &7</command>");
						utils.color(p, "&a/nodamage blockcmd remove &7</command>");
						utils.color(p, "&a/nodamage blockcmd list &7<page>");
						utils.color(p, "");
						return false;
					}
					if(args[1].equalsIgnoreCase("add")) {
						if(args.length == 2) {
							utils.color(p, "&c/nodamage blockcmd add &7</command>");
							return false;
						}
						String s = "";
						for(int i=2;i<args.length;i++) {
							s += args[i]+" ";
						}
						String cmds = s.substring(0, s.length()-1);
						utils.addBlockCMDList(p,cmds);
						
					} else if(args[1].equalsIgnoreCase("remove")) {
						if(args.length == 2) {
							utils.color(p, "&c/nodamage blockcmd remove &7</command>");
							return false;
						}
						String s = "";
						for(int i=2;i<args.length;i++) {
							s += args[i]+" ";
						}
						String cmds = s.substring(0, s.length()-1);
						utils.removeBlockCMDList(sender, cmds);
					} else if(args[1].equalsIgnoreCase("list")) {
						if(args.length == 2) {
							utils.arrayPage(p, "&c&lBlocked-Commands ", utils.getBlockCMDListColor("&b"), 1);
							return false;
						}
						try {
						int page = Integer.parseInt(args[2]);
						if(page == 0) {
							page = 1;
						}
						utils.arrayPage(p, "&c&lBlocked-Commands ", utils.getBlockCMDListColor("&b"), page);
						} catch (Exception e) {
							utils.color(p, "&c/nodamage blockcmd list &7<page>");
						}
						
					}
				} else if(args[0].equalsIgnoreCase("reload")) {
					utils.reloadAll(p);
				} else if(args[0].equalsIgnoreCase("gui")) {
					Gui.openMainMenu(p);
				} else if(args[0].equalsIgnoreCase("version")) {
					utils.color(p, Main.CheckVersionSystem());
				} else if(args[0].equalsIgnoreCase("author")) {
					StringBuilder sb = new StringBuilder();
					sb.append("&6noDamage "+Main.getInstance().getDescription().getVersion()+" &bby PoomPK");
					sb.append("  &f&lResources: &ehttps://www.spigotmc.org/resources/authors/poompk.59678/");
					utils.color(p, sb.toString());
				}
			}
		} else if(sender instanceof ConsoleCommandSender){
			if(cmd.getName().equalsIgnoreCase("nodamage")) {
				if(args.length == 0) {
					sender.sendMessage("/nodamage reload");
					return false;
				}
				if(args[0].equalsIgnoreCase("reload")) {
					utils.reloadAll(sender);
					sender.sendMessage("noDamage reloaded!");
				}
			}
			
		}
		return false;
		
	}
}
