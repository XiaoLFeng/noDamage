package com.poompk.noDamage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class allevent implements Listener{

	@EventHandler
	public void onPlayerJoinGame(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		try {
		String ms = utils.getMessage(p, utils.getMessageJoin());
		if(ms.equals("null")) {
			e.setJoinMessage(null);
		} else {
			e.setJoinMessage(ms);
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            public void run() {
		if(utils.getStatus(19)) {
			for(String s : utils.getMessageWelcome()) {
				if(p != null) {
				utils.Message(p, s);
				}
			}
			}
            }
            }, 10L*1);
		if(utils.getStatus(20)) {
			float speed = utils.getWalkSpeed();
			p.setWalkSpeed(speed);
		} else {
			p.setWalkSpeed(0.2f);
		}
		}catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	@EventHandler
	public void onPlayerQuitGame(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String mq = utils.getMessage(p, utils.getMessageQuit());
		if(mq.equals("null")) {
			e.setQuitMessage(null);
		} else {
		    e.setQuitMessage(mq);	
		}
	}
	 @EventHandler
	  public void onSpawn(PlayerCommandPreprocessEvent e)
	  {
	    if(Main.getInstance().getConfig().getBoolean("Spawn.Enable") != false && Main.getInstance().getConfig().getBoolean("Spawn.CommandEnable") != false){

			Player p = e.getPlayer();
	        if (e.getMessage().equalsIgnoreCase("/" + Main.getInstance().getConfig().getString("Spawn.CustomCommand"))) {
	        	e.setCancelled(true);
	      	    utils.TeleportToSpawn(p);
	        	
	        }
	   }
	 }
}
