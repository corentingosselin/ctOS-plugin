package fr.coco5843.ctos;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import fr.coco5843.ctos.commands.CTOSCommand;
import fr.coco5843.ctos.events.GlobalEvents;
import fr.coco5843.ctos.particles.ParticleEffects;
import fr.coco5843.ctos.tasks.ForceField;

public class CTOSSystem extends JavaPlugin {

	/**
	 * - System ctOS center
	 * 
	 * 
	 * @version1.0
	 * 
	 * @see ParticleEffects
	 * @author coco_gigpn
	 * @copyright (C) moi 2014 :D
	 * @date 11/07/2014
	 * @notes Aucunes
	 * 
	 * @revision référence date 10/07/2014 author coco_gigpn
	 */
	
	public static PluginConfig config;

	// Constants of ctOS
	public static final String PREFIX = "§8[§f§lct§b§lOS§8]";
	public static final String ON = "§8[§2Ω§8]";
	public static final String OFF = "§8[§4Ω§8]";

	@Override
	public void onEnable() {
		try {
			// Registers Events
			Bukkit.getPluginManager().registerEvents(new GlobalEvents(), this);
			// Load message in the console
			Bukkit.getConsoleSender().sendMessage(PREFIX + " §aEnabling control system...");
			// Loads configuration file
			config = new PluginConfig(new File(this.getDataFolder(), "config.yml"));
			config.load();
			// Registers the ctOS Command
			final PluginCommand command = this.getCommand("ctos");
			command.setUsage(ChatColor.RED + command.getUsage());
			command.setExecutor(new CTOSCommand());
			// Registers the force field's task
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ForceField(), 0L, config.forceFieldRefreshTime);
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		try {
			// Unload message in the console
			Bukkit.getConsoleSender().sendMessage(PREFIX + " §cDisabling control system...");
			// Save the configuration file
			config.save();
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
	}

}
