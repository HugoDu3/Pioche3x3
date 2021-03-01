package fr.jayrex.pioche.crafting;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftItemHammer {
	public JavaPlugin plugin;
	public static String loreString = "§3|| §bMine du §93x3§3 ||";

	ItemStack Pioche = new ItemStack(Material.DIAMOND_PICKAXE, 1);

	public CraftItemHammer(JavaPlugin plugin) {
		this.plugin = plugin;

	}

	public void modifyItemMeta() {
		ItemMeta PiocheMeta = Pioche.getItemMeta();

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(loreString);

		PiocheMeta.setDisplayName("§9§lMarteau du Dieu");

		PiocheMeta.setLore(lore);

		Pioche.setItemMeta(PiocheMeta);
	}

}
