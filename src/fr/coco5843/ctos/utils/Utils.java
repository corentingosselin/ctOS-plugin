package fr.coco5843.ctos.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {
	
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
	public static final Vector calculateVelocity(Player p, Entity e) {
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

}
