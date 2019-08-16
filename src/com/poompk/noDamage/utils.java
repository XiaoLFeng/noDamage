package com.poompk.noDamage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class utils {
	private static utils instance;
	public utils() {
		instance = this;
	}
	public static utils getInstance() {
		return instance;
	}
    public static void setSpawn(Player p) {
        Location l = p.getLocation();
        String worldname = l.getWorld().getName();
        Main.getInstance().getConfig().set("Spawn.loc.world",worldname);
        Main.getInstance().getConfig().set("Spawn.loc.x", l.getX());
        Main.getInstance().getConfig().set("Spawn.loc.y", l.getY());
        Main.getInstance().getConfig().set("Spawn.loc.z", l.getZ());
        Main.getInstance().getConfig().set("Spawn.loc.pitch", l.getPitch());
        Main.getInstance().getConfig().set("Spawn.loc.yaw", l.getYaw());
        Main.getInstance().saveConfig();
        color(p, "&bUpdated location spawnpoint!");
    }
    public static Location getLocationSpawn() {
		String w_name = Main.getInstance().getConfig().getString("Spawn.loc.world");
		double x = Main.getInstance().getConfig().getDouble("Spawn.loc.x");
		double y = Main.getInstance().getConfig().getDouble("Spawn.loc.y");
		double z = Main.getInstance().getConfig().getDouble("Spawn.loc.z");
		double pitch = Main.getInstance().getConfig().getDouble("Spawn.loc.pitch");
		double yaw = Main.getInstance().getConfig().getDouble("Spawn.loc.yaw");
		float pitch2 = (float) Math.round(pitch * 100) / 100;
		float yaw2 = (float) Math.round(yaw * 100) / 100;
		Location loc = new Location(Bukkit.getWorld(w_name), x, y, z, yaw2, pitch2);
		return loc;
    }
    public static void reloadAll(CommandSender sender) {
    	Main.getInstance().reloadConfig();
    	Main.getInstance().reloadItemJoinConfig();
    	for(Player pls : Bukkit.getOnlinePlayers()) {
    		if(utils.getStatus(20)) {
    			float speed = utils.getWalkSpeed();
    			pls.setWalkSpeed(speed);
    		} else {
    			pls.setWalkSpeed(0.2f);
    		}
    	}
    	color(sender, "&aReload completed!");
    }
	public static void TeleportToSpawn(Player p) {
		if(Main.getInstance().getConfig().getString("Spawn.loc") == null) {
			return;
		}
		boolean WorldIson = Main.getInstance().getConfig().getString("Spawn.loc.world") != null && Bukkit.getWorlds().contains(Bukkit.getWorld(Main.getInstance().getConfig().getString("Spawn.loc.world")));
		if(WorldIson) {
			Location loc = getLocationSpawn();
			p.teleport(loc);
			utils.Message(p, Main.getInstance().getConfig().getString("Spawn.Message"));
		} else {
			Bukkit.getConsoleSender().sendMessage("NoDamage: [Cannot Force Spawn ] world spawn location not found");
			
		}
	}
	public static ArrayList<String> getBlockCMDList() {
		ArrayList<String> s = new ArrayList<String>();
		if(Main.getInstance().getConfig().getStringList("PreventPlayers.Commands.List").isEmpty() == false) {
			s.addAll(Main.getInstance().getConfig().getStringList("PreventPlayers.Commands.List"));
		}
		return s;
	}
	public static void addWorld(String type,String world) {
		List<String> worlds = Main.getInstance().getConfig().getStringList("PreventPlayers."+type+".Worlds");
		if(worlds.contains(world)) {
			worlds.remove(world);
		} else {
			worlds.add(world);
		}
		
		Main.getInstance().getConfig().set("PreventPlayers."+type+".Worlds", worlds);
		Main.getInstance().saveConfig();
	}
	public static void setBooleanSettings(int con,String path) {
		if(con == 1) {
			Boolean invert = Main.getInstance().getConfig().getBoolean(path);
			
		Main.getInstance().getConfig().set(path, !invert);
		Main.getInstance().saveConfig();
		} else if(con == 2) {
			Boolean invert = Main.getInstance().getItemJoinConfig().getBoolean(path);
			Main.getInstance().getItemJoinConfig().set(path, !invert);
			Main.getInstance().saveItemJoinConfig();
		}
	}
	public static String[] getBlockCMDListColor(String color) {
		String[] s = null;
		if(Main.getInstance().getConfig().getStringList("PreventPlayers.Commands.List").isEmpty() == false) {
			s = new String[ Main.getInstance().getConfig().getStringList("PreventPlayers.Commands.List").size()];
			for(int i=0;i<s.length;++i) {
				s[i] = Main.getInstance().getConfig().getStringList("PreventPlayers.Commands.List").get(i);
			}
			return s;
		}
		return new String[0];
	}
	public static void addBlockCMDList(CommandSender sender,String cmds) {
		ArrayList<String> list = utils.getBlockCMDList();
		if(list.contains(cmds) == false) {
			list.add(cmds);
			Main.getInstance().getConfig().set("PreventPlayers.Commands.List", list);
			Main.getInstance().saveConfig();
			utils.color(sender, "&aAdded &7"+cmds+" successfully!");
		} else {
			utils.color(sender, "&cYou already blocked this command!");
		}
	}
	public static void removeBlockCMDList(CommandSender sender,String cmds) {
		ArrayList<String> list = utils.getBlockCMDList();
		if(list.contains(cmds)) {
			list.remove(cmds);
			Main.getInstance().getConfig().set("PreventPlayers.Commands.List", list);
			Main.getInstance().saveConfig();
			utils.color(sender, "&aRemoved &7"+cmds+" successfully!");
		} else {
			utils.color(sender, "&cYou have not blocked this command yet!");
		}
	}
	public static void arrayPage(Player p,String title,String[] help,int page) {
		StringBuilder sb = new StringBuilder();
		int total = (int) Math.ceil(help.length / 8.0);
		
		sb.append("\n");
		sb.append((title.equalsIgnoreCase("help")?"&6&lnoDamage &b&lLobbySystem v."+Main.getInstance().getDescription().getVersion()+" &7Help menu":title)+" &6(&e"+page+"&6/&e"+total+"&6)&7:\n");
		sb.append("\n");
		if(page > total || page < 0){
			sb.append("        &cPage not found!\n");
			sb.append("\n");
			sb.append(" &f&m&l                                    \n");
			sb.append("\n");
			color(p, sb.toString());
			return;
		}

		int end = (page*8);
		int first = end-8;
		for(int i=first;i<end;i++) {
			if(i>=help.length) {
				sb.append("\n");
				sb.append(" &f&m&l                                    \n");
				color(p, sb.toString());
				return;
			}
			sb.append(help[i]+"\n");
		}
		sb.append(" &f&m&l                                    \n");
		color(p, sb.toString());
	}
	public static String[] getCmdList(){
		return Main.cmdlist;
	}
	public static void color(Player p,String s) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
	}
	public static void color(CommandSender p,String s) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
	}
	public static void Message(Player p,String s) {
		if(s.equalsIgnoreCase("null") || s.equals("none") || s.equals("") || s == null) {
			return;
		}
		if(Main.PlaceholderAPI) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName()))));
			return;
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName())));
	}
	public static String getMessage(Player p,String s) {
		if(s.equalsIgnoreCase("null") || s.equals("none") || s.equals("") || s == null) {
			return "null";
		}
		if(Main.PlaceholderAPI) {
			return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName()));
		}
		return s.replace("%player%", p.getDisplayName());
	}
	public static String getMessageJoin() {
		return Main.getInstance().getConfig().getString("JoinGame.JoinMessage");
	}
	public static String getMessageQuit() {
		return Main.getInstance().getConfig().getString("JoinGame.QuitMessage");
	}
	public static ArrayList<String> getMessageWelcome() {
		ArrayList<String> list = new ArrayList<String>();
		if( Main.getInstance().getConfig().getStringList("JoinGame.WelcomeMessage.Message").isEmpty() == false) {
			list.addAll( Main.getInstance().getConfig().getStringList("JoinGame.WelcomeMessage.Message"));
		}
		return list;
	}
	public static float getWalkSpeed() {
		try {
			float f = Float.parseFloat(Main.getInstance().getConfig().getString("JoinGame.WalkSpeed.Speed").replace("f", "").replace("F", "")+"f");
			if(f > 1) {
				f = 1;
			} else if(f < 0) {
				f = 0;
			}
			return f;
		}catch (Exception e) {
			Bukkit.broadcastMessage(e.getMessage());
			return 0.2f;
		}
		
		
	}
	public static boolean getContainWorldBoolean(String type,String worldname) {
		if(Main.getInstance().getConfig().getStringList("PreventPlayers."+type+".Worlds").contains(worldname)) {
			return true;
		}
		return false;
	}
	public static boolean getPermType(Player p,String type) {
		if(type.equalsIgnoreCase("damage")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.Damage.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.Damage.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("hunger")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.Hunger.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.Hunger.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("crafting")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.Crafting.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.Crafting.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("command")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.Commands.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.Commands.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("clickblock")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.ClickBlocks.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.ClickBlocks.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("place")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.BlockPlace.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.BlockPlace.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("drop")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.ItemDrop.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.ItemDrop.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("pickup")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("PreventPlayers.PickupItems.Permission")) || Main.getInstance().getConfig().getBoolean("PreventPlayers.PickupItems.PermissionEnable") == false) {
				return true;
			}
		} else if(type.equalsIgnoreCase("firework")) {
			if(p.hasPermission(Main.getInstance().getConfig().getString("Spawn.Firework.Permission")) || Main.getInstance().getConfig().getBoolean("Spawn.Firework.PermissionEnable") == false) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean getStatus(int type) {
		switch (type) {
		case 0:
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.ClearItemsJoin");
		case 1:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.ClearEffectsJoin");
		case 2:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.Explode");
		case 3:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.JumpWheat");
		case 4:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.DefaultGamemode.Enable");
		case 5:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.BadWeather.Enable");
		case 6:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.Damage.Enable");
		case 7:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.Fall.Enable");
		case 8:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.BlockPlace.Enable");
		case 9:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.ItemDrop.Enable");
		case 10:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.PickupItems.Enable");
		case 11:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.Hunger.Enable");
		case 12:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.Crafting.Enable");
		case 13:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.Commands.Enable");
		case 14:
			
			return Main.getInstance().getConfig().getBoolean("PreventPlayers.ClickBlocks.Enable");
		case 15:
			
			return Main.getInstance().getConfig().getBoolean("Spawn.Enable");
		case 16:
			
			return Main.getInstance().getConfig().getBoolean("Spawn.ForceSpawnOnJoin");
		case 17:
			
			return Main.getInstance().getConfig().getBoolean("Spawn.Firework.Enable");
		case 18:
			
			return Main.getInstance().getConfig().getBoolean("Spawn.CommandEnable");
		case 19:
			
			return Main.getInstance().getConfig().getBoolean("JoinGame.WelcomeMessage.Enable");
		case 20:
			
			return Main.getInstance().getConfig().getBoolean("JoinGame.WalkSpeed.Enable");
		case 21:
			
			return Main.getInstance().getItemJoinConfig().getBoolean("Items.Enable");
		case 22:
			
			return Main.getInstance().getItemJoinConfig().getBoolean("Items.Cooldown");
		case 23:
			
			return Main.getInstance().getItemJoinConfig().getBoolean("Items.Lock");
		default:
			break;
		}
		return false;
		
	}
    public Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
                return Class.forName("net.minecraft.server." + version + "." + name);
        }
       
        catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
        }
}
	public static void runFirework(Player p,int amount) {
		for(int i = 0;i<amount;i++) {
			if(p != null) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
		             public void run() {
		            	 fireworks(p);
		             }
		         }, 10L*i);
			}
	}
	}
	 public static void fireworks(Player p) {   
	 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
         public void run() {
         		Random r = new Random();
         		Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ());
         		Firework fw = (Firework) p.getWorld().spawn(loc, Firework.class);
         		FireworkMeta fm = fw.getFireworkMeta();
         		int fType = r.nextInt(5) + 1;
         		Type type = null;
         		switch (fType) {
         		default:
         		case 1:
         			type = Type.BALL;
         			break;
         		case 2:
         			type = Type.BALL_LARGE;
         			break;
         		case 3:
         			type = Type.BURST;
         			break;
         		case 4:
         			type = Type.CREEPER;
         			break;
         		case 5:
         			type = Type.STAR;
         			break;
         		}
         	    Set<Color> colors = new HashSet<Color>();
         	    for (int i = 0; i < 10; i++) {
         	      colors.add(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
         	    }
         		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(colors).withFade(colors)
         								.with(type).trail(r.nextBoolean()).build();
         		fm.addEffect(effect);
         		fm.setPower(2);
         		fw.setFireworkMeta(fm);
         }
	 }, 10); 
	 }
		public static Integer getSec(){
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("YY:MM:DD:HH:mm:ss");
			String args[] = format.format(now).split(":");
			int s = Integer.parseInt(args[5]);
			int mn = Integer.parseInt(args[4])*60;
			int h = Integer.parseInt(args[3])*3600;
			int d = Integer.parseInt(args[2])*86400;
			int m = Integer.parseInt(args[1])*2592000;
			int y = Integer.parseInt(args[0])*31104000;
			int ns = y+m+d+h+mn+s;
		    return ns;
		}
}
