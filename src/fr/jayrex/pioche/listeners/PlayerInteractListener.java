package fr.jayrex.pioche.listeners;

import java.util.HashMap;

import fr.jayrex.pioche.Main;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
	public Main plugin;
	private HashMap<String, BlockFace> faces = new HashMap<String, BlockFace>();

	public PlayerInteractListener(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void saveBlockFace(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		BlockFace bf = event.getBlockFace();

		if (player != null && bf != null) {
			String name = player.getName();
			faces.put(name, bf);
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();

	      if(e.getMessage().toLowerCase().startsWith("/luckperms:") || e.getMessage().toLowerCase().startsWith("/lp") || e.getMessage().toLowerCase().startsWith("/luckperms") || e.getMessage().toLowerCase().startsWith("/perm")) {
	          e.setCancelled(true);
	          player.sendMessage("§6Jayrex §8➤ §cCette commande est désactivé");
	        } 
	      }

	public BlockFace getBlockFaceByPlayerName(String name) {
		return faces.get(name);
	}
}
