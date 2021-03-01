package fr.jayrex.pioche;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import fr.jayrex.pioche.handlers.BlockBreakHandler;
import fr.jayrex.pioche.handlers.EnchantItemHandler;
import fr.jayrex.pioche.handlers.InventoryClickHandler;
import fr.jayrex.pioche.handlers.PlayerInteractHandler;
import fr.jayrex.pioche.lib.Reference;

public final class Main extends JavaPlugin {
	PlayerInteractHandler handlerPlayerInteract;
	BlockBreakHandler handlerBlockBreak;
	EnchantItemHandler handlerEnchantItem;
	InventoryClickHandler handlerInventoryClick;

	@Override
	public void onEnable(){
		handlerPlayerInteract = new PlayerInteractHandler();
		handlerBlockBreak = new BlockBreakHandler();
		handlerEnchantItem = new EnchantItemHandler();
		handlerInventoryClick = new InventoryClickHandler();

		handlerPlayerInteract.Init(this);
		handlerBlockBreak.Init(this);
		handlerEnchantItem.Init(this);
		handlerInventoryClick.Init(this);

		this.saveDefaultConfig();

		getLogger().info("Plugin was enabled.");

		processConfig();
		getLogger().info("Finished processing config file.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin was disabled.");
	}


	public void processConfig() {
		try {
			for (Object x : (ArrayList<?>) getConfig().getList("Minable")) {
				LinkedHashMap<String, ArrayList> l = (LinkedHashMap<String, ArrayList>)x;

				for (String blockType: l.keySet()) {
					if (blockType == null || blockType.isEmpty())
						continue;

					if (Material.getMaterial(blockType) == null || Reference.MINABLE.containsKey(Material.getMaterial(blockType)))
						continue;

					Reference.MINABLE.put(Material.getMaterial(blockType), new ArrayList<Material>());
					ArrayList<Material> temp = Reference.MINABLE.get(Material.getMaterial(blockType));

					for (String hammerType: (ArrayList<String>)l.get(blockType)) {
						if (hammerType == null || hammerType.isEmpty())
							continue;

						if (hammerType.equals("any"))
							temp = null;

						if (hammerType != null && (Material.getMaterial(hammerType) == null ||
								(temp != null && temp.contains(Material.getMaterial(hammerType)))))
							continue;

						if (temp != null)
							temp.add(Material.getMaterial(hammerType));
					}

					Reference.MINABLE.put(Material.getMaterial(blockType), temp);
				}
			}
		}
		catch (NullPointerException e) {
			getLogger().info("NullPointerException lorsque vous essayez de lire la liste Minable à partir du fichier de configuration, vérifiez si elle est correctement définie!");
		}
	}

	public PlayerInteractHandler getPlayerInteractHandler() {
		return handlerPlayerInteract;
	}

	public BlockBreakHandler getBlockBreakHandler() {
		return handlerBlockBreak;
	}

	public EnchantItemHandler getEnchantItemHandler() {
		return handlerEnchantItem;
	}

	public InventoryClickHandler getInventoryClickHandler() {
		return handlerInventoryClick;
	}
}
