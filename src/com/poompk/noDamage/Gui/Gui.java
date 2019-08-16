package com.poompk.noDamage.Gui;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.poompk.noDamage.utils;

public class Gui implements Listener{
	@EventHandler
	public void onClickNoDamagesettings(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Inventory inv1 = Bukkit.createInventory(null, 36, "NoDamage Settings");
		if (inv.getName().equals(inv1.getName())) {
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onClickWorlds(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Inventory inv1 = Bukkit.createInventory(null, 54, "Worlds Settings");
		if (inv.getName().contains(inv1.getName())) {
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onClickAdvanced(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Inventory inv1 = Bukkit.createInventory(null, 54, "Advanced Settings");
		if (inv.getName().contains(inv1.getName())) {
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onClickActionWorlds(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Inventory inv1 = Bukkit.createInventory(null, 54, "Action world:");
		if (inv.getName().contains(inv1.getName())) {
			e.setCancelled(true);
			return;
		}
	}
	public static void openMainMenu(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "NoDamage Settings");
		Item(395, 1, 0, inv, 10, "&aWorlds Settings", "&7Settings world%n%n&eClick to open worlds Settings!");
		Item(151, 1, 0, inv, 13, "&aAdvanced Settings", "&7Toggle Enable/Disable%n%n&eClick to open Advanced Settings!");
		Item(262, 1, 0, inv, 31, "&aClose", "&7Close gui%n%n&eClick to close!");
		Item(152, 1, 0, inv, 16, "&aReload", "&7Reload config%n%n&eClick to reload!");
		p.openInventory(inv);
	}
	public static void openActionWorlds(Player p,String world) {
		Inventory inv = Bukkit.createInventory(null, 54, "Action world: "+world);
		getStatus(utils.getContainWorldBoolean("BadWeather", world), "No BadWeather", inv, 10);
		getStatus(utils.getContainWorldBoolean("Damage", world), "No Damage", inv, 11);
		getStatus(utils.getContainWorldBoolean("Hunger", world), "No Hunger", inv, 12);
		getStatus(utils.getContainWorldBoolean("Fall", world), "No Fall", inv, 13);
		getStatus(utils.getContainWorldBoolean("Crafting", world), "No Crafting", inv, 14);
		getStatus(utils.getContainWorldBoolean("BlockPlace", world), "No BlockPlace", inv, 15);
		getStatus(utils.getContainWorldBoolean("ItemDrop", world), "No ItemDrop", inv, 16);
		getStatus(utils.getContainWorldBoolean("PickupItems", world), "No PickupItems", inv, 19);
		getStatus(utils.getContainWorldBoolean("ClickBlocks", world), "No ClickBlocks", inv, 20);

		Item(262, 1, 0, inv, 49, "&aGo back", "&eMain Menu!");
		p.openInventory(inv);
	}
	public static void openAdvancedSettings(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Advanced Settings");
		
		getStatus(utils.getStatus(0), "ClearItemsJoin", inv, 10);
		getStatus(utils.getStatus(1), "ClearEffectsJoin", inv, 11);
		getStatus(utils.getStatus(2), "Explode", inv, 12);
		getStatus(utils.getStatus(4), "DefaultGamemode", inv, 13);
		getStatus(utils.getStatus(3), "No JumpWheat", inv, 14);
		getStatus(utils.getStatus(5), "No BadWeather", inv, 15);
		getStatus(utils.getStatus(6), "No Damage", inv, 16);
		getStatus(utils.getStatus(7), "No Fall", inv, 19);
		getStatus(utils.getStatus(8), "No BlockPlace", inv, 20);
		getStatus(utils.getStatus(9), "No ItemDrop", inv, 21);
		getStatus(utils.getStatus(10), "No PickupItems", inv, 22);
		getStatus(utils.getStatus(11), "No Hunger", inv, 23);
		getStatus(utils.getStatus(12), "No Crafting", inv, 24);
		getStatus(utils.getStatus(13), "No Commands", inv, 25);
		getStatus(utils.getStatus(14), "No ClickBlocks", inv, 28);
		getStatus(utils.getStatus(15), "Spawn", inv, 29);
		getStatus(utils.getStatus(16), "ForceSpawnOnJoin", inv, 30);
		getStatus(utils.getStatus(17), "FireworkOnJoin", inv, 31);
		getStatus(utils.getStatus(18), "SpawnCommand", inv, 32);
		getStatus(utils.getStatus(19), "WelcomeMessage", inv, 33);
		getStatus(utils.getStatus(20), "WalkSpeed", inv, 34);
		getStatus(utils.getStatus(21), "ItemsOnJoin", inv, 37);
		getStatus(utils.getStatus(22), "ClickItemCooldown", inv, 38);
		getStatus(utils.getStatus(23), "No MoveItem", inv, 39);
		Item(262, 1, 0, inv, 49, "&aGo back", "&eMain Menu!");
		p.openInventory(inv);
	}
	public static void openWorldSettings(Player p,int page) {
		int total = (int) Math.ceil((Bukkit.getWorlds().size()) / 28.0);
		Inventory inv = Bukkit.createInventory(null, 54, "Worlds Settings ("+page+"/"+total+")");
		ArrayList<String> worlds = new ArrayList<>();
		for(World w : Bukkit.getWorlds()) {
			worlds.add(w.getName());
			
			
		}
			getArrayList(p, worlds, page, inv);
			Item(262, 1, 0, inv, 49, "&aGo back", "&eMain Menu!");
		p.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryMainMenu(InventoryClickEvent e) {
		try {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (inv.getName().contains("NoDamage Settings")) {
			if (c == null || c.getType() == Material.AIR) {
				e.setCancelled(true);
				return;
			}
			if(isItems(e.getCurrentItem().getTypeId(), 262, e.getSlot(), 31, e.getCurrentItem().hasItemMeta())) {
				p.closeInventory();
			}
			
			if(isItems(e.getCurrentItem().getTypeId(), 395, e.getSlot(), 10, e.getCurrentItem().hasItemMeta())) {
				openWorldSettings(p, 1);
			}
			if(isItems(e.getCurrentItem().getTypeId(), 151, e.getSlot(), 13, e.getCurrentItem().hasItemMeta())) {
				openAdvancedSettings(p);
			}
			if(isItems(e.getCurrentItem().getTypeId(), 152, e.getSlot(), 16, e.getCurrentItem().hasItemMeta())) {
				utils.reloadAll(p);
				p.closeInventory();
			}
		}
		}catch (Exception e1) {

		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryWorldsSettings(InventoryClickEvent e) {
		try {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (inv.getName().contains("Worlds Settings")) {
			if (c == null || c.getType() == Material.AIR) {
				e.setCancelled(true);
				return;
			}
			String textpage = ChatColor.stripColor(inv.getName()).split("Worlds Settings ")[1].replace("(", "").replace(")", "");
			
			int previous = Integer.parseInt(textpage.split("/")[0])-1;
			int next = Integer.parseInt(textpage.split("/")[0])+1;
			if(isItems(e.getCurrentItem().getTypeId(), 262, e.getSlot(), 49, e.getCurrentItem().hasItemMeta())) {
				openMainMenu(p);
			}
			if(isItems(e.getCurrentItem().getTypeId(), 262, e.getSlot(), 50, e.getCurrentItem().hasItemMeta())) {
				openWorldSettings(p, next);
			}
			if(isItems(e.getCurrentItem().getTypeId(), 262, e.getSlot(), 48, e.getCurrentItem().hasItemMeta())) {
				openWorldSettings(p, previous);
			}
			if(e.getSlot() >= 10 && e.getSlot() <= 16) {
				int slot = e.getSlot();
				if(isItems(e.getCurrentItem().getTypeId(), 381, e.getSlot(), slot, e.getCurrentItem().hasItemMeta())) {
					openActionWorlds(p, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				}
			}
			if(e.getSlot() >= 19 && e.getSlot() <= 25) {
				int slot = e.getSlot();
				if(isItems(e.getCurrentItem().getTypeId(), 381, e.getSlot(), slot, e.getCurrentItem().hasItemMeta())) {
					openActionWorlds(p, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				}
			}
			if(e.getSlot() >= 28 && e.getSlot() <= 34) {
				int slot = e.getSlot();
				if(isItems(e.getCurrentItem().getTypeId(), 381, e.getSlot(), slot, e.getCurrentItem().hasItemMeta())) {
					openActionWorlds(p, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				}
			}
			if(e.getSlot() >= 37 && e.getSlot() <= 43) {
				int slot = e.getSlot();
				if(isItems(e.getCurrentItem().getTypeId(), 381, e.getSlot(), slot, e.getCurrentItem().hasItemMeta())) {
					openActionWorlds(p, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				}
			}

		}
		}catch (Exception e1) {
			
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryActionWorldsSettings(InventoryClickEvent e) {
		try {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (inv.getName().contains("Action world:")) {
			if (c == null || c.getType() == Material.AIR) {
				e.setCancelled(true);
				return;
			}
			String world = ChatColor.stripColor(inv.getName().split("Action world: ")[1]);
			if(isItems(e.getCurrentItem().getTypeId(), 262, e.getSlot(), 49, e.getCurrentItem().hasItemMeta())) {
				openWorldSettings(p, 1);
				return;
			}
			for(int i=10;i<=16;i++) {
				int slot = i;
				if(isItems(e.getCurrentItem().getTypeId(), 351, e.getSlot(), getSlot(slot), e.getCurrentItem().hasItemMeta())) {
					switch (getSlot(slot)) {
					case 10:
						utils.addWorld("BadWeather", world);
						break;
					case 11:
						utils.addWorld("Damage", world);
						break;
					case 12:
						utils.addWorld("Hunger", world);
						break;
					case 13:
						utils.addWorld("Fall", world);
						break;
					case 14:
						utils.addWorld("Crafting", world);
						break;
					case 15:
						utils.addWorld("BlockPlace", world);
						break;
					case 16:
						utils.addWorld("ItemDrop", world);
						break;
					default:
						break;
					}
				}
			}
			for(int i=19;i<=20;i++) {
				int slot = i;
				if(isItems(e.getCurrentItem().getTypeId(), 351, e.getSlot(), getSlot(slot), e.getCurrentItem().hasItemMeta())) {
					switch (getSlot(slot)) {
					case 19:
						utils.addWorld("PickupItems", world);
						break;
					case 20:
						utils.addWorld("ClickBlocks", world);
						break;
					default:
						break;
					}
				}
			}
			openActionWorlds(p, world);
			soundclick(p);
		}
		}catch (Exception e1) {
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryAdvancedSettingss(InventoryClickEvent e) {
		try {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (inv.getName().contains("Advanced Settings")) {
			if (c == null || c.getType() == Material.AIR) {
				e.setCancelled(true);
				return;
			}
			if(isItems(e.getCurrentItem().getTypeId(), 262, e.getSlot(), 49, e.getCurrentItem().hasItemMeta())) {
				openMainMenu(p);
				return;
			}
			for(int i=10;i<=16;i++) {
				int slot = i;

				if(isItems(e.getCurrentItem().getTypeId(), 351, e.getSlot(), getSlot(slot), e.getCurrentItem().hasItemMeta())) {
					switch (getSlot(slot)) {
					case 10:
						utils.setBooleanSettings(1, "PreventPlayers.ClearItemsJoin");
						break;
					case 11:
						utils.setBooleanSettings(1, "PreventPlayers.ClearEffectsJoin");
						break;
					case 12:
						utils.setBooleanSettings(1, "PreventPlayers.Explode");
						break;
					case 13:
						utils.setBooleanSettings(1, "PreventPlayers.DefaultGamemode.Enable");
						break;
					case 14:
						utils.setBooleanSettings(1, "PreventPlayers.JumpWheat");
						break;
					case 15:
						utils.setBooleanSettings(1, "PreventPlayers.BadWeather.Enable");
						break;
					case 16:
						utils.setBooleanSettings(1, "PreventPlayers.Damage.Enable");
						break;
					default:
						break;
					}
				}
			}
				for(int i=19;i<=25;i++) {
					int slot = i;

					if(isItems(e.getCurrentItem().getTypeId(), 351, e.getSlot(), getSlot(slot), e.getCurrentItem().hasItemMeta())) {
						switch (getSlot(slot)) {
					case 19:
						utils.setBooleanSettings(1, "PreventPlayers.Fall.Enable");
						break;
					case 20:
						utils.setBooleanSettings(1, "PreventPlayers.BlockPlace.Enable");
						break;
					case 21:
						utils.setBooleanSettings(1, "PreventPlayers.ItemDrop.Enable");
						break;
					case 22:
						utils.setBooleanSettings(1, "PreventPlayers.PickupItems.Enable");
						break;
					case 23:
						utils.setBooleanSettings(1, "PreventPlayers.Hunger.Enable");
						break;
					case 24:
						utils.setBooleanSettings(1, "PreventPlayers.Crafting.Enable");
						break;
					case 25:
						utils.setBooleanSettings(1, "PreventPlayers.Commands.Enable");
						break;
					default:
						break;
						}
					}
				}
				for(int i=28;i<=34;i++) {
					int slot = i;

					if(isItems(e.getCurrentItem().getTypeId(), 351, e.getSlot(), getSlot(slot), e.getCurrentItem().hasItemMeta())) {
						switch (getSlot(slot)) {
						case 28:
							utils.setBooleanSettings(1, "PreventPlayers.ClickBlocks.Enable");
							break;
						case 29:
							utils.setBooleanSettings(1, "Spawn.Enable");
							break;
						case 30:
							utils.setBooleanSettings(1, "Spawn.ForceSpawnOnJoin");
							break;
						case 31:
							utils.setBooleanSettings(1, "Spawn.Firework.Enable");
							break;
						case 32:
							utils.setBooleanSettings(1, "Spawn.CommandEnable");
							break;
						case 33:
							utils.setBooleanSettings(1, "JoinGame.WelcomeMessage.Enable");
							break;
						case 34:
							utils.setBooleanSettings(1, "JoinGame.WalkSpeed.Enable");
							break;
						default:
							break;
						}
					}
				}
				for(int i=37;i<=39;i++) {
					int slot = i;
					if(isItems(e.getCurrentItem().getTypeId(), 351, e.getSlot(), getSlot(slot), e.getCurrentItem().hasItemMeta())) {
						switch (getSlot(slot)) {
						case 37:
							utils.setBooleanSettings(2, "Items.Enable");
							break;
						case 38:
							utils.setBooleanSettings(2, "Items.Cooldown");
							break;
						case 39:
							utils.setBooleanSettings(2, "Items.Lock");
							break;
						default:
							break;
						}
					}
				}
					openAdvancedSettings(p);
					soundclick(p);
				}
		}catch (Exception e1) {
		}
	}
	public static void soundclick(Player p) {
		try {
		p.playSound(p.getLocation(), Sound.valueOf("NOTE_PLING"), 0.5f, 1.2f);
		}catch (IllegalArgumentException  e) {
			p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 0.5f, 1.2f);
		}
	}
	public static int getSlot(int slot) {
		if(slot == 17 || slot == 26 || slot == 35 || slot == 44) {
			slot += 2;
		}
		return slot;
	}
	public static boolean isItems(int id1,int id2,int slot1,int slot2,boolean isHasmeta) {
		if(id1 == id2 && slot1 == slot2 && isHasmeta) {
			return true;
		}
		return false;
	}
	public static void getArrayList(Player p,ArrayList<String> store,int page,Inventory inv) {
		
		int total = (int) Math.ceil(store.size() / 28.0);

		if(page > 0 && page < total) {
			Item(262, 1, 0, inv, 50, "&aNext page", "&ePage "+(page+1));
		}
		if(page > 1 && page > 0) {
			Item(262, 1, 0, inv, 48, "&aPrevious page", "&ePage "+(page-1));
		}
		if(page > total || page <= 0){
				Item(160, 1, 14, inv, 31, "&c404", "&cNot found!");
			return;
		}
		
		int end = (page*28);
		int first = end-28;
		int slot = 10;
	
		for(int i=first;i<end;i++) {
			if(i>=store.size()) {
				return;
			}
			if(slot == 17 || slot == 26 || slot == 35 || slot == 44) {
				slot += 2;
			}

			try {
				int slots = slot;
				String w_name = store.get(i);
				String badweaher = "&7No Bad weather: "+utils.getContainWorldBoolean("BadWeather", w_name);
				String Damage = "&7No Damage: "+utils.getContainWorldBoolean("Damage", w_name);
				String Hunger = "&7No Hunger: "+utils.getContainWorldBoolean("Hunger", w_name);
				String Fall = "&7No Fall: "+utils.getContainWorldBoolean("Fall", w_name);
				String Crafting = "&7No Crafting: "+utils.getContainWorldBoolean("Crafting", w_name);
				String BlockPlace = "&7No BlockPlace: "+utils.getContainWorldBoolean("BlockPlace", w_name);
				String ItemDrop = "&7No ItemDrop: "+utils.getContainWorldBoolean("ItemDrop", w_name);
				String PickupItems = "&7No PickupItems: "+utils.getContainWorldBoolean("PickupItems", w_name);
				String ClickBlocks = "&7No ClickBlocks: "+utils.getContainWorldBoolean("ClickBlocks", w_name);
				String click_to_settings = "&eClick to settings";
				String build =
						"&7World name: "+w_name+"%n"
						+badweaher.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+Damage.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+Hunger.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+Fall.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+Crafting.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+BlockPlace.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+ItemDrop.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+PickupItems.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+ClickBlocks.replace("false", "&cOff").replace("true", "&aOn")+"%n"
						+"%n"
						+click_to_settings;
				Item(381, 1, 0, inv, slots, "&a"+w_name, build);
		    } catch (Exception e) {

			}
			
			slot++;
		}
	}
	public static void getStatus(Boolean status,String type,Inventory inv,int Slot) {
		if(status) {
			Item(351, 1, 10, inv, Slot, "&a"+type, "&eClick to Disable!");
		} else {
			Item(351, 1, 8, inv, Slot, "&c"+type, "&eClick to Enable!");
		}
	}
	public static void Item(int mat, int amount, int shrt, Inventory inv, int Slot, String name, String lore) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(mat, amount, (short) shrt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		String[] lored = ChatColor.translateAlternateColorCodes('&', lore).split("%n");
		meta.setLore(Arrays.asList(lored));
		item.setItemMeta(meta);
		inv.setItem(Slot, item);
	}

	public static void ItemSkull(int amount, String playername, Inventory inv, int Slot, String name,
			String lore) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount, (short)SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta)skull.getItemMeta();
		meta.setOwner(playername);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		String[] lored = ChatColor.translateAlternateColorCodes('&', lore).split("%n");
		meta.setLore(Arrays.asList(lored));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skull.setItemMeta(meta);
		inv.setItem(Slot, skull);
	}
}
