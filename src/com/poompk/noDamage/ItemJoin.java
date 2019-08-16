package com.poompk.noDamage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
public class ItemJoin implements Listener{
	public static HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();

	////////////////
	// Add ITEMS///
	//////////////
	@EventHandler
	public void JoingameWithItem(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Inventory inv = p.getInventory();
		if (Main.getInstance().getItemJoinConfig().getBoolean("Items.Enable") == false) {
			return;
		}
		for (int i = 0; i <= 9; i++) {
			if (Main.getInstance().getItemJoinConfig().get("Items.slots." + i) != null) {
				String args[] = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".item").split(":");
				if (args[0].equals("397")) {
					List<String> list = Main.getInstance().getItemJoinConfig().getStringList(("Items.slots." + i + ".lore"));
					List<String> lore = new ArrayList<String>();
					String name = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".display")
							.replaceAll("%player%", p.getName());
					for (String string : list) {
						lore.add(string.replace("&", "§").replace("%player%", p.getName()));
						ItemSkull(p,Integer.parseInt(args[2]), args[3].replaceAll("%player%", p.getName()), inv, i, name,
								lore);
					}
				} else if (!args[0].equals("397")) {
					List<String> list = Main.getInstance().getItemJoinConfig().getStringList(("Items.slots." + i + ".lore"));
					List<String> lore = new ArrayList<String>();
					String name = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".display")
							.replaceAll("%player%", p.getName());
					for (String string : list) {
						lore.add(string.replace("&", "§").replace("%player%", p.getName()));
						Item(p,Integer.parseInt(args[0]), Integer.parseInt(args[2]), Integer.parseInt(args[1]), inv, i,
								name, lore);
					}
				}
			}
		}

	}

	//////////////////
	// Click Command//
	/////////////////
	@SuppressWarnings("deprecation")
	@EventHandler
	public void Playerclickhotbar(PlayerInteractEvent e) {
		if (Main.getInstance().getItemJoinConfig().getBoolean("Items.Enable") == false) {
			return;
		}
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.AIR) {
			return;
		}
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			for (int i = 0; i <= 9; i++) {
				if (p.getInventory().getHeldItemSlot() == i) {
					if (getNotNull(i) == false) {
						return;
					}
					if (p.getItemInHand().getItemMeta().hasDisplayName() == false) {
						return;
					}
					if(canClick(p.getUniqueId()) == false) {
						utils.color(p, Main.getInstance().getItemJoinConfig().getString("Items.CooldownMessage").replace("%cooldown%", getCooldownTime(p.getUniqueId())+""));
						return;
					} else {
						cooldown.put(p.getUniqueId(), (long) (utils.getSec()+Main.getInstance().getItemJoinConfig().getInt("Items.Cooldown")));
					}
					String name = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".display");
					if (p.getItemInHand().getTypeId() == getID(i) && p.getItemInHand().getItemMeta().getDisplayName()
							.equals(ChatColor.translateAlternateColorCodes('&', name))) {
						if (Main.getInstance().getItemJoinConfig().get("Items.slots." + i + ".onclick") != null) {
							String args[] = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".onclick")
									.split(":");
							if (args[0].equalsIgnoreCase("none")) {
								return;
							} else if (args[0].equalsIgnoreCase("player")) {
								String command = args[1].replaceAll("%player%", p.getName());
								Bukkit.dispatchCommand(p, command);
							} else if (args[0].equalsIgnoreCase("console")) {
								String command = args[1].replaceAll("%player%", p.getName());
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
							}
						}
					}
				}
			}
		}

	}

	@EventHandler
	public void nomoveinv(InventoryClickEvent e) {
		if (Main.getInstance().getItemJoinConfig().getBoolean("Items.Lock")) {
			if(e.getWhoClicked() instanceof Player) {
				if(e.getWhoClicked().hasPermission(Main.getInstance().getItemJoinConfig().getString("Items.PermissionMoveItem"))) {
					return;
				}
			}
			e.setCancelled(true);
		}
	}

	public boolean getNotNull(int i) {
		boolean check = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i) != null;
		return check;
	}

	public int getID(int i) {
		String args[] = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".item").split(":");
		return Integer.parseInt(args[0]);
	}

	public boolean Same(Player p, int i) {
		String args[] = Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".item").split(":");
		boolean name = p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes(
				'&', Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".display")));
		boolean amount = p.getItemInHand().getAmount() == Integer.parseInt(args[2]);
		boolean check = name == true && amount == true;
		return check;

	}
	public boolean SameSame(Player p, int i) {
		boolean name = p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes(
				'&', Main.getInstance().getItemJoinConfig().getString("Items.slots." + i + ".display")));
		boolean check = name;
		return check;

	}
	public static Long getCooldown(UUID uuid) {
		if(cooldown.get(uuid) == null) {
			cooldown.put(uuid, (long) 0);
			return (long) 0;
		} else {
			return cooldown.get(uuid);
		}
	}
	public static int getCooldownTime(UUID uuid) {
		int i = (int) (getCooldown(uuid)-utils.getSec());
		return i;
	}
	public static boolean canClick(UUID uuid) {
		if(utils.getSec() > getCooldown(uuid)) {
			return true;
		}
		return false;
	}
	public static void Item(Player p,int mat, int amount, int shrt, Inventory inv, int Slot, String name, List<String> lore) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(mat, amount, (short) shrt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		ArrayList<String> list = new ArrayList<>();
		if(Main.PlaceholderAPI) {
			for(String s : lore) {
				list.add(ChatColor.translateAlternateColorCodes('&',  me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName()))));
			}
			meta.setLore(list);
		} else {
			meta.setLore(lore);
		}
		
		item.setItemMeta(meta);
		inv.setItem(Slot, item);
	}

	public static void ItemSkull(Player p,int amount, String playername, Inventory inv, int Slot, String name,
			List<String> lore) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(playername);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		ArrayList<String> list = new ArrayList<>();
		if(Main.PlaceholderAPI) {
			for(String s : lore) {
				list.add(ChatColor.translateAlternateColorCodes('&',  me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName()))));
			}
			meta.setLore(list);
		} else {
			meta.setLore(lore);
		}
		skull.setItemMeta(meta);
		inv.setItem(Slot, skull);
	}



}
