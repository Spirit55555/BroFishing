package dk.spirit55555.brofishing;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BroFishing extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
	}
	
	@EventHandler
	public void playerFishEvent(PlayerFishEvent event) {
		if (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) && event.getCaught() instanceof Player) {
			Player player = event.getPlayer();
			Player playerCaught = (Player)event.getCaught();
			
			if (player.hasPermission("brofishing.canfishbros")) {
				if (!playerCaught.hasPermission("brofishing.nonfishable"))
					teleport(playerCaught, player);
				else if (getConfig().getBoolean("nonfishable-error"))
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("nonfishable-errormessage")));
			}
		}
	}

	private void teleport(Player from, Player to) {
		Vector vect1 = from.getLocation().toVector();
		Vector vect2 = to.getLocation().toVector();
		Vector vector = vect2.subtract(vect1).normalize();
		double distance = from.getLocation().distance(to.getLocation());
		for (double i = 1.0D; i < distance; i += 1.0D) {
			vect1.add(vector);
			double x = vect1.getX();
			double y = vect1.getY();
			double z = vect1.getZ();
			from.teleport(new Location(from.getWorld(), x, y + 0.25D, z, from.getLocation().getYaw(), from.getLocation().getPitch()));

			waitTicks(25L);
		}
  	}

	private void waitTicks(long n) {
		long t0 = System.currentTimeMillis();
		long t1;
		do
			t1 = System.currentTimeMillis();
		while (t1 - t0 < n);
	}
}