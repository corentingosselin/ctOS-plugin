package fr.coco5843.ctos.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import fr.coco5843.ctos.CTOSAPI;
import fr.coco5843.ctos.CTOSSystem;
import fr.coco5843.ctos.utils.Utils;

public class ForceField implements Runnable {
	
	private static final List<Entity> victims = new ArrayList<Entity>();

	@Override
	public void run() {
		for(final Player player : CTOSAPI.getPlayersInForceField()) {
			for(Entity entity : player.getNearbyEntities(CTOSSystem.config.forceFieldRange, CTOSSystem.config.forceFieldRange, CTOSSystem.config.forceFieldRange)) { // Check rayon entity
				if(CTOSSystem.config.forceFieldEntities) { // Check if the forcefield is enabled for the specified entity
					if(entity instanceof Animals || entity instanceof Monster) {
						entity.setVelocity(Utils.calculateVelocity(player, entity));
					}
				}
				if(entity.getType() == EntityType.PLAYER) {
					if(!entity.equals(player)) {
						if(!((Player)entity).hasPermission("forcefield.bypass")) {
							if(!victims.contains(entity)) { // Anti flood message / Sound function used by the forcefield's method
								//TODO: Add Particles ?
								victims.add(entity);
								message(entity); // Apply message check after
								((Player)entity).sendMessage(CTOSSystem.PREFIX + CTOSSystem.config.forceFieldHitMessage.replace("&", "§"));
								((Player)entity).playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							}
							if(entity.isInsideVehicle()) {
								entity.getVehicle().setVelocity(Utils.calculateVelocity(player, entity)); // Check if player is inside a vehicle and apply the new velocity
								 // TODO: Add Particles for the player inside the vehicle ?
							}
							else { // "Normal" function to set velocity
								entity.setVelocity(Utils.calculateVelocity(player, entity));
								CTOSSystem.config.forceFieldEffect.display(entity.getLocation().add(0, 1.5f, 0), 0.1f, 0.1f, 0.1f, 0.1f, 10);
							}
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * message -  Nouvelle autorisation tous les 80 ticks soit toute les 3 secondes 
	 * Player p / Entity e
	 * 
	 * Message(Player p / Entity e)
	 * 
	 * 
	 * @return new vector
	 * @param null
	 * @exception null
	 * 
	 * @see null
	 * @author coco_gigpn
	 * @date 11/07/2014
	 * @note null
	 * 
	 */
	private final void message(final Entity e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(CTOSAPI.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				victims.remove(e);
			}
			
		}, 80L);
	}

}
