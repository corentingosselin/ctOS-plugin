package fr.coco5843.ctos;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CTOSAPI {
	
	private static final Plugin ctOS = Bukkit.getPluginManager().getPlugin("ctOS");
	private static final List<Player> forceFields = new ArrayList<Player>();

	/**
	 * Creates a force field on the player.
	 * 
	 * @param p The specified player.
	 * 
	 * @note EffectLib Utilization : effect.cancel() not working
	 */
	
	public static final void createForcefield(final Player p) {
		/*
		 * final ShieldEntityEffect effect = new
		 * ShieldEntityEffect(effectManager, p); effect.sphere = false;
		 * effect.particle = ParticleEffect.PORTAL; effect.radius = 2;
		 * effect.iterations = Integer.MAX_VALUE;
		 */
		forceFields.add(p);
	}
	
	/**
	 * Checks if the specified player has a force field.
	 * 
	 * @param player The player.
	 * 
	 * @return <b>true</b> If the specified player has a force field.
	 * <b>false</b> Otherwise.
	 */
	
	public static final boolean isInForceField(final Player player) {
		return forceFields.contains(player);
	}
	
	/**
	 * Removes a player's force field.
	 * 
	 * @param player The player.
	 */
	
	public static final void removePlayerFromForceField(final Player player) {
		forceFields.remove(player);
		// effect.cancel();
	}
	
	/**
	 * Gets the players who have a force field.
	 * 
	 * @return Those players.
	 */
	
	public static final List<Player> getPlayersInForceField() {
		return forceFields;
	}
	
	/**
	 * Gets the ctOS's system instance.
	 * 
	 * @return The plugin.
	 */
	
	public static final CTOSSystem getPlugin() {
		return (CTOSSystem)ctOS;
	}

}
