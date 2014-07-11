package ctOS;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import Particles.ParticleEffects;

public class SystemctOS extends JavaPlugin implements Listener {

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

	private ConsoleCommandSender console;

	// Messages description ctOS center
	public String prefix = "§8[§f§lct§b§lOS§8]";
	public String ctOSAntispamMessage = "§bVous ne pouvez parler avant §c%time% §bsecondes.(Grade vip: bypass)";
	public String ctOSForcefieldEffect = "FIREWORKS_SPARK";
	public String ctOSForcefieldHit = "§4 Impossible d'approcher un membre du staff";
	public String NoPermission = "§4Vous ne pouvez pas utiliser cette commande";
	public String on = "§8[§2Ω§8]";
	public String off = "§8[§4Ω§8]";
	public String forcefieldCheck;

	// Utils ctOS Development
	public Integer forcefieldID = 0;
	public HashMap<String, Long> chat = new HashMap<String, Long>();
	public ArrayList<Player> forcefield = new ArrayList<Player>();
	public ArrayList<Entity> victim = new ArrayList<Entity>();

	// Variables development
	public Integer time = Integer.valueOf(4);
	public Double range = Double.valueOf(5);;
	public Boolean ActiveForcefieldEntity = true;

	public void onEnable() {
		// Load Events
		Bukkit.getPluginManager().registerEvents(this, this);
		// Load message console plugin
		console = Bukkit.getServer().getConsoleSender();
		console.sendMessage(prefix + " §acontrol system enable");
		// Load configuration File
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if (!file.exists()) {
			time = Integer.valueOf(getConfig().getInt("ctOS-AntiSpam.Time"));
			range = Double.valueOf(getConfig().getInt("ctOS-Forcefield.Range"));
			ctOSAntispamMessage = getConfig()
					.getString("ctOS-AntiSpam.Message");
			ctOSForcefieldEffect = getConfig().getString(
					"ctOS-Forcefield.Effect");
			ctOSForcefieldHit = getConfig().getString(
					"ctOS-Forcefield.HitMessage");
			NoPermission = getConfig().getString("ctOS-NoPermission");
			ActiveForcefieldEntity = getConfig().getBoolean(
					"ctOS-ActiveForcefieldEntity");
			loadConfig();
		}
	}

	public void onDisable() {
		// Load message console plugin
		console = Bukkit.getServer().getConsoleSender();
		console.sendMessage(prefix + " §ccontrol system disable");
		// save configuration
		saveConfig();
	}

	/**
	 * loadConfig - Chargement des configurations du ctOS Center Ajoutez vos
	 * propres configurations
	 * 
	 * loadConfig() / this.loadConfig()
	 * 
	 * @return null
	 * @param null
	 * @exception null
	 * 
	 * @see null
	 * @author coco_gigpn
	 * @date 11/07/2014
	 * @note null
	 * 
	 */

	public void loadConfig() {
		getConfig().addDefault("ctOS-AntiSpam.Time", Integer.valueOf(4));
		getConfig().addDefault("ctOS-Forcefield.Range", Integer.valueOf(5));
		getConfig()
				.addDefault("ctOS-AntiSpam.Message",
						"&bVous ne pouvez parler avant &c%time% &bsecondes.(Grade vip: bypass)");
		getConfig().addDefault("ctOS-Forcefield.HitMessage",
				"&4 Impossible d'approcher un membre du staff");
		getConfig().addDefault("ctOS-NoPermission",
				"&4Vous ne pouvez pas utiliser cette commande");
		getConfig().addDefault("ctOS-ActiveForcefieldEntity", true);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	/**
	 * loadConfig - Chargement des configurations du ctOS Center Ajoutez vos
	 * propres configurations
	 * 
	 * loadConfig() / this.loadConfig()
	 * 
	 * @return false
	 * @param null
	 * @exception null
	 * 
	 * @see null
	 * @author coco_gigpn
	 * @date 11/07/2014
	 * @note commandes liste: /ctOS , help , forcefield : (reload not working)
	 * 
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("ctOS")) {
			Player p = (Player) sender;
			if (p.hasPermission("ctos.main")) {

				if (args.length == 0) {
					// Method check forcefield On / Off
					if (forcefield.contains(p)) {
						forcefieldCheck = on;
					} else {
						forcefieldCheck = off;
					}

					p.sendMessage("§9╔══════════════════════════════════════════════════════════╗");
					p.sendMessage("§9║ §cVersion §fctOS§7: §3"
							+ getDescription().getVersion());
					p.sendMessage("§9║ §cAuthor: §3"
							+ getDescription().getAuthors());
					p.sendMessage("§9║ §bAll ctOS System:");
					p.sendMessage("§9║ §7Antispam: " + on);
					p.sendMessage("§9║ §7forcefield: " + forcefieldCheck);
					p.sendMessage("§9╚══════════════════════════════════════════════════════════╝");
				}

			} else {
				p.sendMessage(NoPermission.replace("&", "§"));
			}

			if (args.length == 1) {

				if (args[0].equalsIgnoreCase("help")) {
					if (p.hasPermission("ctos.help")) {
						p.sendMessage("§9╔══════════════════════════════════════════════════════════╗");
						p.sendMessage("§9║ §c/ctos");
						p.sendMessage("§9║ §c/ctos help");
						p.sendMessage("§9║ §c/ctos forcefield");
						p.sendMessage("§9║ §c/ctos reload");
						p.sendMessage("§9╚══════════════════════════════════════════════════════════╝");
					} else {
						p.sendMessage(NoPermission.replace("&", "§"));
					}
				}

				// Not working :(
				else if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("ctos.help")) {
						reloadConfig();
						p.sendMessage(prefix + " §cest maintenant rechargé !");
						return true;
					} else {
						p.sendMessage(NoPermission.replace("&", "§"));
					}
				} else if (args[0].equalsIgnoreCase("forcefield")) {

					if (p.hasPermission("ctos.forcefield")) {

						if (!forcefield.contains(p)) {
							forcefield.add(p);
							forcefield(p);
							p.sendMessage(prefix + " §2Forcefield activé");
						} else {
							forcefield.remove(p);
							p.sendMessage(prefix + " §cForcefield désactivé");
							forcefield(p);
						}
					} else {
						p.sendMessage(NoPermission.replace("&", "§"));
					}
				}
			}

		}
		return true;
	}

	/**
	 * forcefield - déclenchement du forcefield joueur system ctOS Forcefield
	 * player / entity
	 * 
	 * forcefield(final Player) , forcefield(Player) , this.forcefield(final
	 * Player / Player);
	 * 
	 * @return false
	 * @param null
	 * @exception null
	 * 
	 * @see ParticleEffects
	 * @author coco_gigpn
	 * @date 11/07/2014
	 * @note EffectLib Utilization : effect.cancel() not working
	 * 
	 */
	public void forcefield(final Player p) {

		/**
		 * final ShieldEntityEffect effect = new
		 * ShieldEntityEffect(effectManager, p); effect.sphere = false;
		 * effect.particle = ParticleEffect.PORTAL; effect.radius = 2;
		 * effect.iterations = Integer.MAX_VALUE;
		 */

		forcefieldID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
				new Runnable() {
			/*
			 * Initialize scheduler
			 */
					public void run() {
		
						if (forcefield.contains(p)) {
							/*
							 * Switch On / Off
							 */
							
							for (Entity entity : p.getNearbyEntities(range,
									range, range)) {
								/*
								 * Check rayon entity
								 */

								if (ActiveForcefieldEntity) {
									/*
									 * Check if the forcefield is enable for entity
									 */
									if (entity instanceof Animals || entity instanceof Monster) {
										
										entity.setVelocity(calculateVelocity(p,entity));

										/*
										 * Application velocity
										 */
									}

								}

								if (entity instanceof Player) {

									if (!entity.equals(p)) {

										if (!((Player) entity).hasPermission("forcefield.bypass")) {

											if (!victim.contains(entity)) {
												/*
												 * Anti flood message / Sound function for forcefield method
												 * Add Particles ?
												 */
												victim.add(entity);
												message(entity);
												/*
												 * Apply message check after
												 */
												
												((Player) entity).sendMessage(prefix
														+ ctOSForcefieldHit
																.replace("&",
																		"§"));
												((Player) entity).playSound(
														entity.getLocation(),
														Sound.ENDERMAN_TELEPORT,
														1, 1);
											}

											if (entity.isInsideVehicle()) {
												entity.getVehicle().setVelocity(calculateVelocity(p,entity));
												/*
												 * Check if player is InsideVehicle() and application new velocity
												 * Add Particles for the player inside the vehicle ?
												 */
											} else {

												entity.setVelocity(calculateVelocity(
														p, entity));

												ParticleEffects.FIREWORKS_SPARK
														.display(
																entity.getLocation()
																		.add(0,
																				1.5f,
																				0),
																0.1f, 0.1f,
																0.1f, 0.1f, 10);
												/*
												 * Normal function to set velocity
												 */

											}

										}
									}
								}
							}
						} else {
							Bukkit.getScheduler().cancelTask(forcefieldID);
							// effect.cancel();
							/*
							 * Switch method forcefield to off
							 * effect.cancel(); not working
							 */

						}
					}
				}, 1L, 1L); 
		/*
		 * Check forcefield any ticks
		 * Make lags ?
		 */

	}
	
	/**
	 * calculateVelocity - Calcule de la vélocité de l'entité en fonction de sa direction par rapport au Player
	 * Player p / Entity e
	 * 
	 * CalculateVelocity(Player p , Entity e)
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
	public Vector calculateVelocity(Player p, Entity e) {
		Location ploc = p.getLocation();
		Location eloc = e.getLocation();

		double px = ploc.getX();
		double py = ploc.getY();
		double pz = ploc.getZ();
		double ex = eloc.getX();
		double ey = eloc.getY();
		double ez = eloc.getZ();

		double x = 0.0D;
		double y = 0.0D;
		double z = 0.0D;
		if (px < ex) {
			x = 1.0D;
		} else if (px > ex) {
			x = -1.0D;
		}
		if (py < ey) {
			y = 0.5D;
		} else if (py > ey) {
			y = -0.5D;
		}
		if (pz < ez) {
			z = 1.0D;
		} else if (pz > ez) {
			z = -1.0D;
		}
		return new Vector(x, y, z);
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
	public void message(final Entity e) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {

				victim.remove(e);

			}
		}, 80L);
	}

	
	/**
	 * Antispam -  Event Anti spam system ctOS
	 * 
	 * 
	 * @return null
	 * @param null
	 * @exception null
	 * 
	 * @see null
	 * @author coco_gigpn
	 * @date 11/07/2014
	 * @note Changement de méthode prévu , pour afficher le nombre de seconde restante (timer)
	 * 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void AntiSpam(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if ((!p.hasPermission("antispam.bypass"))) {
			Long atime = Long.valueOf(System.currentTimeMillis());
			if (chat.containsKey(p.getName())) {
				Long now = (Long) chat.get(p.getName());
				if (now.longValue() > atime.longValue()) {
					p.sendMessage(ctOSAntispamMessage.replace("&", "§")
							.replace("%time%", time.toString()));
					e.setCancelled(true);
				} else {
					chat.remove(p.getName());
					chat.put(
							p.getName(),
							Long.valueOf(atime.longValue() + time.intValue()
									* 1000));
				}
			} else {
				chat.put(p.getName(), Long.valueOf(atime.longValue()
						+ time.intValue() * 1000));
			}
		} else {
			e.setCancelled(false);
		}
	}

	/**
	 * OnLeave-  Debug for forcefield deconnection
	 * 
	 * 
	 * @return null
	 * @param null
	 * @exception null
	 * 
	 * @see null
	 * @author coco_gigpn
	 * @date 11/07/2014
	 * @note Ceci empêche la fonction de continuer à run lors d'une éventuelle déconnection
	 * 
	 */
	@EventHandler
	public void OnLeave(PlayerQuitEvent e) {
		forcefield.remove(e.getPlayer());
	}

}
