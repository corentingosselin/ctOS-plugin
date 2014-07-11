package fr.coco5843.ctos;

import java.io.File;
import java.util.Arrays;

import org.bukkit.ChatColor;

import fr.coco5843.ctos.particles.ParticleEffects;
import fr.coco5843.ctos.utils.Skyoconfig;

public class PluginConfig extends Skyoconfig {
	
	@ConfigOptions(name = "forcefield.effect")
	public ParticleEffects forceFieldEffect = ParticleEffects.FIREWORKS_SPARK;
	@ConfigOptions(name = "forcefield.hit-message")
	public String forceFieldHitMessage = ChatColor.DARK_RED + " Impossible d'approcher un membre du staff";
	@ConfigOptions(name = "forcefield.range")
	public int forceFieldRange = 5;
	@ConfigOptions(name = "forcefield.entities")
	public boolean forceFieldEntities = true;
	@ConfigOptions(name = "forcefield.refresh-time")
	public long forceFieldRefreshTime = 20L;
	
	@ConfigOptions(name = "antispam.spam-message")
	public String antispamMessage = ChatColor.AQUA + "Vous ne pouvez parler avant " + ChatColor.RED + "%time% " + ChatColor.AQUA + "secondes.(Grade vip: bypass)";
	@ConfigOptions(name = "antispam.time")
	public int antispamTime = 4;
	
	@ConfigOptions(name = "options.message-nopermission")
	public String messageNoPermission = ChatColor.DARK_RED + "Vous ne pouvez pas utiliser cette commande";
	
	public PluginConfig(final File file) {
		super(file, Arrays.asList("ctOS Config"));
	}

}
