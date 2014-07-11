package fr.coco5843.ctos.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.coco5843.ctos.CTOSAPI;
import fr.coco5843.ctos.CTOSSystem;

public class GlobalEvents implements Listener {

	// Used for the development of ctOS
	private static final HashMap<String, Long> chat = new HashMap<String, Long>();

	/**
	 * <b>Antispam</b> -  Anti spam system of ctOS
	 * 
	 * @note Changement de méthode prévu , pour afficher le nombre de seconde restante (timer)
	 */

	@EventHandler(priority = EventPriority.NORMAL)
	public void AntiSpam(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("antispam.bypass")) {
			final String playerName = p.getName();
			final long atime = System.currentTimeMillis();
			Long now = chat.get(playerName);
			if(now != null) {
				if(now > atime) {
					p.sendMessage(CTOSSystem.config.antispamMessage.replace("%time%", String.valueOf(CTOSSystem.config.antispamTime)));
					e.setCancelled(true);
				}
				else {
					chat.remove(playerName);
					chat.put(playerName, Long.valueOf(atime + Long.valueOf(CTOSSystem.config.antispamTime * 1000)));
				}
			}
			else {
				chat.put(playerName, Long.valueOf(atime + Long.valueOf(CTOSSystem.config.antispamTime * 1000)));
			}
		}
		else {
			e.setCancelled(false);
		}
	}

	/**
	 * Used to avoid memory leaks.
	 */

	@EventHandler
	public void OnLeave(PlayerQuitEvent e) {
		CTOSAPI.removePlayerFromForceField(e.getPlayer());
	}

}
