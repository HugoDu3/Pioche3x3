package fr.jayrex.pioche.listeners;

import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.lib.PowerUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClickListener implements Listener {
	Main plugin;

	public InventoryClickListener(Main plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(InventoryClickEvent e){
	// check if the event has been cancelled by another plugin
	if(!e.isCancelled()){
	HumanEntity ent = e.getWhoClicked();
	 
	// not really necessary
	if(ent instanceof Player){
	Player player = (Player)ent;
	Inventory inv = e.getInventory();
	 
	// see if the event is about an anvil
	if(inv instanceof AnvilInventory){
	InventoryView view = e.getView();
	int rawSlot = e.getRawSlot();
	 
	// compare the raw slot with the inventory view to make sure we are talking about the upper inventory
	if(rawSlot == view.convertSlot(rawSlot)){
	/*
	slot 0 = left item slot
	slot 1 = right item slot
	slot 2 = result item slot
	 
	see if the player clicked in the result item slot of the anvil inventory
	*/
	if(rawSlot == 2){
	/*
	get the current item in the result slot
	I think inv.getItem(rawSlot) would be possible too
	*/
	ItemStack item = e.getCurrentItem();
	 
	// check if there is an item in the result slot
	if(item != null){
	ItemMeta meta = item.getItemMeta();
	 
	// it is possible that the item does not have meta data
	if(meta != null){
	// see whether the item is beeing renamed
	if(meta.hasDisplayName()){
		if(meta.getDisplayName().equals("§9§lMarteau du Dieu")) {
	           String displayName = meta.getDisplayName();
		              }
	                }
	              }
	            }
	          }
	        }
	      }
	    }
	  }
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void canEnchant(InventoryClickEvent event) {
		if (!(event.getInventory() instanceof AnvilInventory))
			return;

		if (event.getSlotType() != SlotType.RESULT)
			return;

		ItemStack item = event.getInventory().getItem(0);
		ItemStack item2 = event.getInventory().getItem(1);

		if (!PowerUtils.isPowerTool(item))
			return;
		
		if (PowerUtils.isPowerTool(item))	{
	          if (event.getInventory().getType() == InventoryType.ANVIL) {
	            if(event.getSlotType() == InventoryType.SlotType.RESULT) {
	                event.setCancelled(true);
	            }
	         }
	       }

		if (item2 == null)
			return;
		

		if (item2.getType() != Material.ENCHANTED_BOOK) {
			switch(item2.getType()) {
			case IRON_INGOT:
			case GOLD_INGOT:
			case DIAMOND:
				return;
			default:
				break;
			}

			if (PowerUtils.isPowerTool(item2)) {
				if (item.getEnchantments().isEmpty())
					return;
			}
			else {
				event.setCancelled(true);
				return;
			}
		}

		if (!PowerUtils.checkEnchantPermission((Player) event.getWhoClicked(), item.getType()))
			event.setCancelled(true);
	}
}
