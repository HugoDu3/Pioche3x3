package fr.jayrex.pioche.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.crafting.CraftItemHammer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PowerUtils {
	public static double CHANCE_FORTUNE_I = 0.33;
	public static double CHANCE_FORTUNE_II = 0.25;
	public static double CHANCE_FORTUNE_III = 0.20;

	public PowerUtils() {}

	public static boolean isPowerTool(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return false;

		List<String> lore = item.getItemMeta().getLore();

		if (lore == null)
			return false;

		if (lore.contains("§3|| §bMine du §93x3§3 ||")) {
			return (Reference.PICKAXES.contains(item.getType()) && (lore.contains(CraftItemHammer.loreString)));
		}else {
			return false;
		}
	}

	public static int getAmountPerFortune(int level, int amount) {
		Random rand = new Random();

		if (level == 1 && rand.nextDouble() <= CHANCE_FORTUNE_I)
			return amount * 2;
		else if (level == 2) {
			if (rand.nextDouble() <= CHANCE_FORTUNE_II)
				return amount * 3;
			if (rand.nextDouble() <= CHANCE_FORTUNE_II)
				return amount * 2;
		}
		else if (level == 3) {
			if (rand.nextDouble() <= CHANCE_FORTUNE_III)
				return amount * 4;
			if (rand.nextDouble() <= CHANCE_FORTUNE_III)
				return amount * 3;
			if (rand.nextDouble() <= CHANCE_FORTUNE_III)
				return amount * 2;
		}

		return amount;
	}

	public static double getFlintDropChance(int level) {
		double chance = 0.10;

		if (level == 1)
			chance = 0.14;
		else if (level == 2)
			chance = 0.25;
		else if (level == 3)
			chance = 1.0;

		return chance;
	}

	public static boolean canSilkTouchMine(Material blockType) {
		return Reference.MINABLE_SILKTOUCH.contains(blockType);
	}

	public static boolean canFortuneMine(Material blockType) {
		return Reference.MINABLE_FORTUNE.get(blockType) != null;
	}
	
	public static boolean isMineable(Material blockType) {
		return Reference.MINABLE.containsKey(blockType);
	}

	public static ItemStack processEnchantsAndReturnItemStack(Enchantment enchant, int enchantLevel, Block block) {
		Material blockType = block.getType();
		ItemStack drop = null;

		if (enchant == Enchantment.SILK_TOUCH)
			drop = new ItemStack(blockType, 1);
		else if (enchant == Enchantment.LOOT_BONUS_BLOCKS) {
			int amount = 0;
			Random rand = new Random();

			if (Reference.MINABLE_FORTUNE.get(blockType) != null) {
				switch (blockType) {
					case GLOWSTONE:
						amount = Math.min((rand.nextInt(5) + 2) + enchantLevel, 4);

						break;
					case REDSTONE_ORE:
					case LEGACY_GLOWING_REDSTONE_ORE:
						amount = Math.min((rand.nextInt(2) + 4) + enchantLevel, 8);

						break;
					case COAL_ORE:
					case DIAMOND_ORE:
					case EMERALD_ORE:
					case NETHER_QUARTZ_ORE:
						amount = getAmountPerFortune(enchantLevel, 1);
						break;
					case LAPIS_ORE:
						amount = Math.min(getAmountPerFortune(enchantLevel, (rand.nextInt(5) + 4)), 32);
						break;
					default:
						break;
				}

				if (amount > 0) {
					if (blockType == Material.LAPIS_ORE)
						drop = new ItemStack(Reference.MINABLE_FORTUNE.get(blockType), amount, (short)4);
					else
						drop = new ItemStack(Reference.MINABLE_FORTUNE.get(blockType), amount);
				}
			}
		}

		return drop;
	}

	public static ArrayList<Block> getSurroundingBlocks(BlockFace blockFace, Block targetBlock) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		World world = targetBlock.getWorld();

		int x, y, z;
		x = targetBlock.getX();
		y = targetBlock.getY();
		z = targetBlock.getZ();

		switch(blockFace) {
			case UP:
			case DOWN:
				blocks.add(world.getBlockAt(x+1, y, z));
				blocks.add(world.getBlockAt(x-1, y, z));
				blocks.add(world.getBlockAt(x, y, z+1));
				blocks.add(world.getBlockAt(x, y, z-1));
				blocks.add(world.getBlockAt(x+1, y, z+1));
				blocks.add(world.getBlockAt(x-1, y, z-1));
				blocks.add(world.getBlockAt(x+1, y, z-1));
				blocks.add(world.getBlockAt(x-1, y, z+1));
				break;
			case EAST:
			case WEST:
				blocks.add(world.getBlockAt(x, y, z+1));
				blocks.add(world.getBlockAt(x, y, z-1));
				blocks.add(world.getBlockAt(x, y+1, z));
				blocks.add(world.getBlockAt(x, y-1, z));
				blocks.add(world.getBlockAt(x, y+1, z+1));
				blocks.add(world.getBlockAt(x, y-1, z-1));
				blocks.add(world.getBlockAt(x, y-1, z+1));
				blocks.add(world.getBlockAt(x, y+1, z-1));
				break;
			case NORTH:
			case SOUTH:
				blocks.add(world.getBlockAt(x+1, y, z));
				blocks.add(world.getBlockAt(x-1, y, z));
				blocks.add(world.getBlockAt(x, y+1, z));
				blocks.add(world.getBlockAt(x, y-1, z));
				blocks.add(world.getBlockAt(x+1, y+1, z));
				blocks.add(world.getBlockAt(x-1, y-1, z));
				blocks.add(world.getBlockAt(x+1, y-1, z));
				blocks.add(world.getBlockAt(x-1, y+1, z));
				break;
			default:
				break;
		}

		blocks.removeAll(Collections.singleton(null));
		return blocks;
	}

	public static boolean checkUsePermission(Player player, Material itemType) {
		boolean canUse = false;

		switch (player.getItemInHand().getType()) {
			case NETHERITE_PICKAXE:
				if (player.hasPermission("jayrex.use.pickaxe"))
					canUse = true;

				break;
			default:
				break;
		}

		return canUse;
	}

	public static boolean checkEnchantPermission(Player player, Material itemType) {
		boolean canEnchant = false;

		switch (itemType) {
			case NETHERITE_PICKAXE:
				if (player.hasPermission("jayrex.enchant.pickaxe"))
					canEnchant = true;

				break;
			default:
				break;
		}

		return canEnchant;
	}

	public static boolean canBreak(Main plugin, Player player, Block block) {


		return true;
	}

	public static boolean validateHammer(Material hammerType, Material blockType) {
		return (isMineable(blockType) && Reference.PICKAXES.contains(hammerType) &&
				(Reference.MINABLE.get(blockType) == null || Reference.MINABLE.get(blockType).contains(hammerType)));
	}

}
