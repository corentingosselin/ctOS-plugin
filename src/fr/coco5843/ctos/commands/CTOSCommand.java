package fr.coco5843.ctos.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.google.common.base.Joiner;

import fr.coco5843.ctos.CTOSAPI;
import fr.coco5843.ctos.CTOSSystem;

public class CTOSCommand implements CommandExecutor {

	public String forcefieldCheck;

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if(cmd.getName().equalsIgnoreCase("ctOS")) {
			final Player p = (Player)sender;
			if(p.hasPermission("ctos.main")) {
				if(args.length == 0) {
					final PluginDescriptionFile desc = CTOSAPI.getPlugin().getDescription();
					p.sendMessage("§9╔══════════════════════════════════════════════════════════╗");
					p.sendMessage("§9║ §cVersion §fctOS§7: §3" + desc.getVersion());
					p.sendMessage("§9║ §cAuthor: §3" + Joiner.on(", ").join(desc.getAuthors()));
					p.sendMessage("§9║ §bctOS Systems:");
					p.sendMessage("§9║ §7Antispam: " + CTOSSystem.ON);
					p.sendMessage("§9║ §7forcefield: " + (CTOSAPI.isInForceField(p) ? CTOSSystem.ON : CTOSSystem.OFF));
					p.sendMessage("§9╚═══════════════════════════════════════════════════════════");
				}

			}
			else {
				p.sendMessage(CTOSSystem.config.messageNoPermission);
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					if(p.hasPermission("ctos.help")) {
						p.sendMessage("§9╔══════════════════════════════════════════════════════════╗");
						p.sendMessage("§9║ §c/ctos");
						p.sendMessage("§9║ §c/ctos help");
						p.sendMessage("§9║ §c/ctos forcefield");
						//p.sendMessage("§9║ §c/ctos reload");
						p.sendMessage("§9╚═══════════════════════════════════════════════════════════");
					}
					else {
						p.sendMessage(CTOSSystem.config.messageNoPermission);
					}
				}
				// Not working :(
				/*else if(args[0].equalsIgnoreCase("reload")) {
					if(p.hasPermission("ctos.reload")) {
						reloadConfig();
						p.sendMessage(PREFIX + " §cest maintenant rechargé !");
						return true;
					}
					else {
						p.sendMessage(config.messageNoPermission);
					}
				}*/
				else if(args[0].equalsIgnoreCase("forcefield")) {
					if(p.hasPermission("ctos.forcefield")) {
						if(!CTOSAPI.isInForceField(p)) {
							CTOSAPI.createForcefield(p);
							p.sendMessage(CTOSSystem.PREFIX + " §2Forcefield activé");
						}
						else {
							CTOSAPI.removePlayerFromForceField(p);
							p.sendMessage(CTOSSystem.PREFIX + " §cForcefield désactivé");
						}
					}
					else {
						p.sendMessage(CTOSSystem.config.messageNoPermission);
					}
				}
			}
			else {
				return false;
			}
		}
		return true;
	}

}
