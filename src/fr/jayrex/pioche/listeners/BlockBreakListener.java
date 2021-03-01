package fr.jayrex.pioche.listeners;

import java.util.Map;

import fr.jayrex.lands.LandManager;
import fr.jayrex.lands.LandsPlugin;
import fr.jayrex.lands.enums.Action;
import fr.jayrex.lands.objects.Land;
import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.lib.PowerUtils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {
	public Main plugin;
	public boolean useDurabilityPerBlock;
	private LandManager landmanager = LandsPlugin.getInstance().getLandManager();

	public BlockBreakListener(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		useDurabilityPerBlock = plugin.getConfig().getBoolean("useDurabilityPerBlock");
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void checkToolAndBreakBlocks(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();
		Material handItemType = handItem.getType();

		if (player != null && (player instanceof Player)) {
			if (player.isSneaking())
				return;

			if (!PowerUtils.checkUsePermission(player, handItemType))
				return;

			if (!PowerUtils.isPowerTool(handItem))
				return;
			
			Block block = event.getBlock();
			String playerName = player.getName();
			
			Chunk chunk = block.getChunk();
			Land land = landmanager.getLandAt(chunk);

			if(land != null && !land.isBypassing(event.getPlayer(), Action.BLOCK_BREAK)) return;

			PlayerInteractListener pil = plugin.getPlayerInteractHandler().getListener();
			BlockFace blockFace = pil.getBlockFaceByPlayerName(playerName);

			Map<Enchantment, Integer> enchants = handItem.getEnchantments();
			Enchantment enchant = null;
			int enchantLevel = 0;
			if (enchants.get(Enchantment.SILK_TOUCH) != null) {
				enchant = Enchantment.SILK_TOUCH;
				enchantLevel = enchants.get(Enchantment.SILK_TOUCH);
			}
			else if (enchants.get(Enchantment.LOOT_BONUS_BLOCKS) != null) {
				enchant = Enchantment.LOOT_BONUS_BLOCKS;
				enchantLevel = enchants.get(Enchantment.LOOT_BONUS_BLOCKS);
			}

			short curDur = handItem.getDurability();
			short maxDur = handItem.getType().getMaxDurability();

			for (Block e: PowerUtils.getSurroundingBlocks(blockFace, block)) {
				Material blockMat = e.getType();
				Location blockLoc = e.getLocation();
				Chunk chunk1 = blockLoc.getChunk();
				Land land1 = landmanager.getLandAt(chunk1);
				
				if(land1 != null && !land1.isBypassing(event.getPlayer(), Action.BLOCK_BREAK)) return;
				
				boolean useHammer = false;

				if (useHammer = PowerUtils.validateHammer(handItem.getType(), blockMat));

				if (useHammer) {

					if (!PowerUtils.canBreak(plugin, player, e))
						continue;

					if (enchant == null ||
							((!PowerUtils.canSilkTouchMine(blockMat) || !PowerUtils.canFortuneMine(blockMat)) && useHammer))
						e.breakNaturally(handItem);
					else {
						ItemStack drop = PowerUtils.processEnchantsAndReturnItemStack(enchant, enchantLevel, e);

						if (drop != null) {
							e.getWorld().dropItemNaturally(blockLoc, drop);
							e.setType(Material.AIR);
						}
						else
							e.breakNaturally(handItem);
					}

					if (useDurabilityPerBlock || !player.hasPermission("jayrex.highdurability")) {
						if (curDur++ < maxDur)
							handItem.setDurability(curDur);
						else
							break;
					}
				}
			}
		}
	}
}
