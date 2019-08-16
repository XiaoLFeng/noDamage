package com.poompk.noDamage;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;

public class PreventPlayer implements Listener {

	
	@EventHandler
	public void PlayerJoinSpawn(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(utils.getStatus(15)) {
			if(utils.getStatus(16)) {
				try {
				utils.TeleportToSpawn(p);
				}catch (Exception e1) {
					Bukkit.getLogger().info("noDamage: Not found spawnpoint location /ndmg setspawn!");
				}
			}
			if(utils.getStatus(17)) {
				if(utils.getPermType(p, "firework")) {
				utils.runFirework(p, Main.getInstance().getConfig().getInt("Spawn.Firework.Amount"));
				}
			}
		}
	}
	////////////////////////
	// Prevent Creature  //
	//////////////////////
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        SpawnReason reason = e.getSpawnReason();
        if(Main.getInstance().getConfig().getBoolean("PreventPlayers.CreatureSpawn.all")) {
    			e.setCancelled(true);
    		return;
    	}
    	if(Main.getInstance().getConfig().getBoolean("PreventPlayers.CreatureSpawn.Snowman")) {
    		if(reason == SpawnReason.BUILD_SNOWMAN) {
    			e.setCancelled(true);
    		}
    	}
    	if(Main.getInstance().getConfig().getBoolean("PreventPlayers.CreatureSpawn.Irongolem")) {
    		if(reason == SpawnReason.BUILD_IRONGOLEM) {
    			e.setCancelled(true);
    		}
    	}
    	if(Main.getInstance().getConfig().getBoolean("PreventPlayers.CreatureSpawn.Wither")) {
    		if(reason == SpawnReason.BUILD_WITHER) {
    			e.setCancelled(true);
    		}
    	}
    }
/*	////////////////////////
	// Prevent Arror Stuck//
	//////////////////////
	@EventHandler
	public void AttackingByArrow(EntityDamageByEntityEvent e) {
		try {
			if (Main.getInstance().getConfig().getBoolean("PreventPlayers.Arrow") == true) {
				if (e.getEntity() instanceof Player) {
					if (e.getDamager().getType().equals(EntityType.ARROW)) {
						Player p = (Player) e.getEntity();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
							public void run() {
								ClearArrow(p);
							}
						}, 0L);
					}
				}
			} else {
				return;
			}
		} catch (Exception e1) {
		}
	}

	@EventHandler
	public void ChangeWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		ClearArrow(p);
	}

	public void ClearArrow(Player p) {
		((CraftPlayer) p).getHandle().getDataWatcher().watch(9, Byte.valueOf((byte) 0));
	}*/
	///////////////////////
	// Prevent BadWeather//
	/////////////////////
	@EventHandler
	public void GoodDay(WeatherChangeEvent e) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.BadWeather.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.BadWeather.Worlds");
		if (!(e.toWeatherState())) {
			return;
		}
		if (isOn) {
			if (isWorlds.contains(e.getWorld().getName())) {
				e.setCancelled(true);
				e.getWorld().setThunderDuration(0);
				e.getWorld().setThundering(false);
			}
		}
	}

	///////////////////
	// Prevent Damage//
	/////////////////
	@EventHandler
	public void Damage0(EntityDamageEvent e) {
		
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.Damage.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.Damage.Worlds");
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(utils.getPermType(p, "damage") == false) {
				return;
			}
			if (isOn) {
				if (isWorlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
					p.setFireTicks(0);
					if (p.getHealth() < 19.0D) {
						p.setHealth(20);
					}
				}
			}
		}
	}
	///////////////////////////
	// Prevent noFall       //
	//////////////////////////
	@EventHandler
	public void noFall(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.Fall.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.Fall.Worlds");
		int loc_y = Main.getInstance().getConfig().getInt("PreventPlayers.Fall.locY");
		if (isOn) {
			if (isWorlds.contains(p.getWorld().getName())) {
				if(p.getLocation().getY() <= loc_y) {
					utils.TeleportToSpawn(p);
				}
			}
		}
	}
	//////////////////////
	// Prevent Item Drop//
	////////////////////
	@EventHandler
	public void ItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(utils.getPermType(p, "drop")) {
			return;
		}
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.ItemDrop.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.ItemDrop.Worlds");
		if (isOn) {
			if (isWorlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
			}
		}
	}

	//////////////////////
	// Prevent PickupItem//
	/////////////////////
	@EventHandler
	public void nopickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if(utils.getPermType(p, "pickup")) {
			return;
		}
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.PickupItems.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.PickupItems.Worlds");
		if (isOn) {
			if (isWorlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
			}
		}
	}

	//////////////////////
	// Prevent BlockPlace//
	/////////////////////
	@EventHandler
	public void nopickup(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(utils.getPermType(p, "place")) {
			return;
		}
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.BlockPlace.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.BlockPlace.Worlds");
		if (isOn) {
			if (isWorlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
			}
		}
	}
	//////////////////////
	// Prevent Explode//
	/////////////////////
	@EventHandler
	public void PreventExplode(EntityExplodeEvent e) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.Explode");
		if (isOn) {
			e.setCancelled(true);
		}
	}
	/////////////////////////
	// Prevent ClickOnBlock//
	////////////////////////
	@EventHandler
	public void ClickBlocks(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(utils.getPermType(p, "clickblock")) {
			return;
		}
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.ClickBlocks.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.ClickBlocks.Worlds");
		List<String> id = Main.getInstance().getConfig().getStringList("PreventPlayers.ClickBlocks.Blocks");
		if(id.isEmpty()) {
			return;
		}
		if (isOn) {
			if(isWorlds.contains(p.getWorld().getName())) {
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Material mat = e.getClickedBlock().getType();
					if(id.contains(mat.toString())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
	///////////////////
	// Prevent Hunger//
	/////////////////
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.Hunger.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.Hunger.Worlds");
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(utils.getPermType(p, "hunger") == false) {
				return;
			}
			if (isOn) {
				if (isWorlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
					if (p.getFoodLevel() < 19.0D) {
						p.setFoodLevel(20);
					}
				}
			}

		}
	}

	//////////////////////////
	// Prevent Gamemode//
	////////////////////////
	@EventHandler
	public void GamemodeOther(PlayerJoinEvent e) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.DefaultGamemode.Enable");
		Player p = e.getPlayer();
		if (isOn) {
			GameMode gm = GameMode.valueOf(
					Main.getInstance().getConfig().getString("PreventPlayers.DefaultGamemode.Mode").toUpperCase());
			p.setGameMode(gm);
		}
	}

	////////////////////////
	// Prevent Clear Items//
	///////////////////////
	public void ClearItems(Player p) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.ClearItemsJoin");
		if (isOn) {
			p.getInventory().clear();
		}
	}
	@EventHandler
	public void onplayerclearoinEvent(PlayerJoinEvent e) {
		ClearItems(e.getPlayer());
	}
	//////////////////////////
	// Prevent Clear Effect//
	////////////////////////
	@EventHandler
	public void ClearEffect(PlayerJoinEvent e) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.ClearEffectsJoin");
		Player p = e.getPlayer();
		if (isOn) {
			for (PotionEffect ef : p.getActivePotionEffects()) {
				p.removePotionEffect(ef.getType());
			}
		}
	}
	///////////////////////////
	// Prevent JumpWheat//
	/////////////////////////
	@EventHandler
	public void JumpWheat(PlayerInteractEvent e) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.JumpWheat");
		if(isOn == false) {
			return;
		}
	    if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL)
	        e.setCancelled(true);
	}
	////////////////////////////
	////Prevent Command////////
	//////////////////////////
	 @EventHandler
	  public void onCommand(PlayerCommandPreprocessEvent e)
	  {
	    Player p = e.getPlayer();
	    if(utils.getPermType(p, "command")) {
			return;
		}
	    if(Main.getInstance().getConfig().getBoolean("PreventPlayers.Commands.Enable")){
	    List<String> cmds = Main.getInstance().getConfig().getStringList("PreventPlayers.Commands.List");
	    for (String command : cmds) {
	        if (e.getMessage().equalsIgnoreCase(command)) {
	          e.setCancelled(true);
	          utils.Message(p, Main.getInstance().getConfig().getString("PreventPlayers.Commands.Message"));
	        }
	      }
	   }
	  }
	///////////////////////////
	// Prevent CraftingTable//
	/////////////////////////
	@EventHandler
	public void craftingItem(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(p instanceof Player) {
		boolean isOn = Main.getInstance().getConfig().getBoolean("PreventPlayers.Crafting.Enable");
		List<String> isWorlds = Main.getInstance().getConfig().getStringList("PreventPlayers.Crafting.Worlds");
		if (isOn) {
			if (isWorlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
			}
		}
		}
	}
}

