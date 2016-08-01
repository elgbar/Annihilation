package com.hcs.Listners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;

public class BlockBreakListner implements Listener {
	
    public BlockBreakListner(Plugin p)  {
        Bukkit.getPluginManager().registerEvents(this,p);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
    	if (!event.isCancelled()){
    		Player p = event.getPlayer();
    		if (p.getGameMode() != GameVars.getDefaultGamemode()) {
    			return;
    		}
    		Material bb = event.getBlock().getType(); //bb = broken block
        	switch(bb) {
        		case SNOW:
        		case GLOWSTONE:
        		case TORCH:
        		case CARPET:
        		case SPONGE:
        		case MELON_STEM:
        		case PUMPKIN_STEM:
				case ICE:
        		case WATER_LILY:
          		case SKULL:
          		case SLIME_BLOCK:
          		case FLOWER_POT:
          		case SEA_LANTERN:
          			
        		/* Flowers & plants */
        		case DOUBLE_PLANT:
        		case LONG_GRASS:
        		case YELLOW_FLOWER:
        		case RED_ROSE:
        			
        		/* edible */
        		case CROPS:
        		case COCOA:
        		case NETHER_WARTS:
        		case CARROT:
        		case POTATO:
        		case RED_MUSHROOM:
        		case BROWN_MUSHROOM:
        		case SUGAR_CANE_BLOCK:
        		case MELON_BLOCK:
        		case HAY_BLOCK:
        			
        		/* Glass */
        		case GLASS:
        		case STAINED_GLASS:
        		case THIN_GLASS:
        		case STAINED_GLASS_PANE:
        			
        		/* Pistion */
        		case PISTON_BASE:
        		case PISTON_EXTENSION:
        		case PISTON_MOVING_PIECE:
        		case PISTON_STICKY_BASE:
        		case BREWING_STAND:
        			
        		/* Button */	
				case STONE_BUTTON:
				case WOOD_BUTTON:
					
				/* Redstone */	
				case REDSTONE_COMPARATOR_OFF:
				case REDSTONE_COMPARATOR_ON:
				case REDSTONE_LAMP_OFF:
				case DIODE_BLOCK_OFF: //repeater off
				case DIODE_BLOCK_ON: //repeater on
				case REDSTONE_LAMP_ON:
				case REDSTONE_TORCH_OFF:
				case REDSTONE_TORCH_ON:
				case REDSTONE_WIRE:
        		case TRIPWIRE_HOOK:
        		case TRIPWIRE:
        		case LEVER:
        		case DAYLIGHT_DETECTOR:
        		case DAYLIGHT_DETECTOR_INVERTED:
        			
        		/* These cannot in the original adventure mode (adv mode 1.7 and earlier) */
        		case FIRE:
        		case TNT:
        		case CACTUS:
        		case BED_BLOCK:
        		case CAKE_BLOCK:
        			return;
        		default:
					/* falls through */
    		}
    		
        	String toolMat = p.getInventory().getItemInHand().getType().name();
    		if (toolMat != null){
    			if (toolMat.contains("_PICKAXE")) {
    				switch(bb){
						/* Ore */
						case COAL_ORE:
						case DIAMOND_ORE:
						case EMERALD_ORE:
						case GOLD_ORE:
						case IRON_ORE:
						case LAPIS_ORE:
						case QUARTZ_ORE:
						case REDSTONE_ORE:
						case GLOWING_REDSTONE_ORE:
    					/* Ore blocks */
    					case COAL_BLOCK:
    					case DIAMOND_BLOCK:
    					case EMERALD_BLOCK:
    					case REDSTONE_BLOCK:
    					case GOLD_BLOCK:
    					case IRON_BLOCK:
    					case LAPIS_BLOCK:
    					/* Pressure plates */
    					case STONE_PLATE:
    					case GOLD_PLATE:
    					case IRON_PLATE:
    					case STONE_SLAB2:
    					case DOUBLE_STONE_SLAB2:
    					/* Rails */
       					case RAILS:
    					case POWERED_RAIL:
    					case ACTIVATOR_RAIL:
    					case DETECTOR_RAIL:
    					/* Stone */
    					case STONE:
    					case COBBLESTONE:
    					case COBBLE_WALL:
    					case COBBLESTONE_STAIRS:
    					case MOSSY_COBBLESTONE:
    					case SANDSTONE:
    					case SANDSTONE_STAIRS:
    					case RED_SANDSTONE:
    					case RED_SANDSTONE_STAIRS:
    					case SMOOTH_BRICK:
    					case SMOOTH_STAIRS:
    					case STEP:
    					case DOUBLE_STEP:
    					/* Nether */
    					case NETHER_BRICK:
    					case NETHER_BRICK_STAIRS:
    					case NETHER_FENCE:
    					case NETHERRACK:
    					/*Quartz*/
    					case QUARTZ_BLOCK:
    					case QUARTZ_STAIRS:
    					/* Furnace */
    					case FURNACE:
    					case BURNING_FURNACE:
    					/* clay & brick */
    					case STAINED_CLAY:
    					case HARD_CLAY:
    					case BRICK:
    					case BRICK_STAIRS:
    					/* Usables */
						case IRON_DOOR_BLOCK:
						case HOPPER:
						case ENDER_CHEST:
						case DISPENSER:
						case DROPPER:
						case ANVIL:
						/* Other */
						case OBSIDIAN:
						case MOB_SPAWNER:
						case ENDER_STONE:
						case IRON_FENCE:
						case PACKED_ICE:
						case IRON_TRAPDOOR:
						case CAULDRON:
						case BEACON:
						case ENCHANTMENT_TABLE:
						case PRISMARINE:
    						return;
    					default:
    						/* falls through */
    				}
    			} else if (toolMat.contains("_AXE")) { //need the _ so it wont be confused with pickaxe
    				switch(bb){
    					/* Doors */
    					case WOODEN_DOOR:
    					case ACACIA_DOOR:
    					case BIRCH_DOOR:
    					case JUNGLE_DOOR:
    					case SPRUCE_DOOR:
    					case DARK_OAK_DOOR:
    					/* Gate */
    					case FENCE_GATE:
    					case ACACIA_FENCE_GATE:
    					case BIRCH_FENCE_GATE:
    					case DARK_OAK_FENCE_GATE:
    					case JUNGLE_FENCE_GATE:
    					case SPRUCE_FENCE_GATE:
    					/* stairs */
    					case WOOD_STAIRS:
    					case SPRUCE_WOOD_STAIRS:
    					case ACACIA_STAIRS:
    					case BIRCH_WOOD_STAIRS:
    					case DARK_OAK_STAIRS:
    					case JUNGLE_WOOD_STAIRS:
    					/*Fence*/
    					case FENCE:
    					case ACACIA_FENCE:
    					case BIRCH_FENCE:
    					case DARK_OAK_FENCE:
    					case SPRUCE_FENCE:
    					case JUNGLE_FENCE:
    					/* other */
    					case BOOKSHELF:
    					case WORKBENCH:
    					case CHEST:  						
    					case WOOD:
    					case NOTE_BLOCK:
    					case HUGE_MUSHROOM_1:
    					case HUGE_MUSHROOM_2:
    					case JUKEBOX:
    					case SIGN_POST:
    					case WALL_SIGN:
    					case LADDER:
    					case LOG:
    					case LOG_2:
    					case WOOD_STEP:
    					case WOOD_DOUBLE_STEP:
    					case WOOD_PLATE:
    					case TRAP_DOOR:
    					case TRAPPED_CHEST:
						case JACK_O_LANTERN:
						case PUMPKIN:
						case STANDING_BANNER:
						case WALL_BANNER:
							
    						return;
						default:
							/* falls through */
    				}
    			} else if (toolMat.contains("_SPADE")) {
    				switch(bb){
						case GRASS:
						case DIRT:
						case GRAVEL:
						case SAND:
						case SOIL:
						case SNOW_BLOCK:
	  					case CLAY:
	  					case SOUL_SAND:
	  					case MYCEL:
							return;
						default:
							/* falls through */
    				}
    			} else if (toolMat.contains("SHEARS")) {
    				switch(bb){
						case LEAVES:
						case LEAVES_2:
						case WOOL:
							return;
						default:
							/* falls through */
    				}
    			} else if (toolMat.contains("_SWORD")) {
    				switch(bb){
						case LEAVES:
						case LEAVES_2:
						case WEB:
							return;
						default:
							/* falls through */
    				}
    			}
    		}
    		
    		//If it does not furfull the requirements above then cancel the event
    		event.setCancelled(true);
    	}
    }
}
