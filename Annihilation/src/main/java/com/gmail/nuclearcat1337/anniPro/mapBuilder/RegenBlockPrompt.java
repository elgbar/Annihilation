package com.gmail.nuclearcat1337.anniPro.mapBuilder;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniMap.RegeneratingBlock;

public class RegenBlockPrompt extends ValidatingPrompt
{
	private final Block block;
	private int questionlvl;
	private boolean initial;
	private RegeneratingBlock b;
	private final ChatColor purple = ChatColor.LIGHT_PURPLE;
	private final ChatColor gold = ChatColor.GOLD;
	private final ChatColor red = ChatColor.RED;
	private final ChatColor green = ChatColor.GREEN;

	// fields for building of the actual regenerating block
	private Material mat;
	private Integer dataVal = -1;
	private boolean regenerate = true;
	private boolean cobbleReplace = true;
	private boolean naturalBreak = false;
	private int time = 0;
	private TimeUnit unit = null;
	private int xp = 0;
	private Material product = null;
	private String amount = null;
	private Integer productData = -1;
	private String effect = null;

	@SuppressWarnings("deprecation")
	public RegenBlockPrompt(Block block)
	{
		this.block = block;
		this.mat = block.getType();
		questionlvl = 0;
		initial = true;
		b = Game.getGameMap().getRegeneratingBlocks().getRegeneratingBlock(block.getType(), (int) block.getData());
		if (b == null)
			b = Game.getGameMap().getRegeneratingBlocks().getRegeneratingBlock(block.getType(), -1);

	}

	@SuppressWarnings("deprecation")
	@Override
	public String getPromptText(ConversationContext context)
	{
		String finalMessage = "";
		if (initial)
		{
			initial = false;
			context.getForWhom().sendRawMessage(purple + "Welcome to the " + gold + "Regenerating Block Helper!");
		}

		switch (questionlvl)
		{
			// case 0 is the intro case
			case 0:
			default:
			{
				context.getForWhom().sendRawMessage(purple + "You have selected a block of type " + gold + block.getType().name() + purple + ".");
				context.getForWhom().sendRawMessage(purple + "At any time you may go back one question by typing " + green + "Back" + purple + ".");
				context.getForWhom().sendRawMessage(purple + "You may also exit at anytime by typing " + red + "Quit" + purple + ".");
				finalMessage = purple + "If you with to continue, type " + green + "Ok" + purple + " otherwise type " + red + "Quit" + purple + ".";
				break;
			}
			// cases 1-3 are the removing a regen block
			case 1:
			{
				if (b != null)
				{
					if (this.b.MaterialData == (int) block.getData())
					{
						context.getForWhom().sendRawMessage(purple + "A regenerating block of this type with this data value already exists.");
						context.getForWhom().sendRawMessage(purple + "If you would like to remove it, type " + green + "Remove" + purple + ".");
						finalMessage = purple + "If you would like to override it, type " + green + "Override" + purple + ", otherwise type " + red
								+ "Quit" + purple + ".";
					} else if (b.MaterialData == -1)
					{
						context.getForWhom()
								.sendRawMessage(purple + "A regenerating block has already been specified for all data values of this type.");
						finalMessage = purple + "If you would like to remove it, type " + green + "Remove" + purple + " otherwise type " + red
								+ "Quit" + purple + ".";
					}
				} else
				{
					questionlvl = 4;
					return this.getPromptText(context);
				}
				break;
			}
			case 2:
			{
				break;
			}
			case 3:
			{
				break;
			}
			case 4:
			{
				context.getForWhom()
						.sendRawMessage(purple + "Would you like these settings to apply to all blocks of this type or just this data value?");
				finalMessage = purple + "Type either " + green + "This" + purple + " or " + green + "All" + purple + ".";
				break;
			}
			case 5:
			{
				context.getForWhom().sendRawMessage(purple + "Would you like this block to be just unbreakable, or would you like it to regenerate?");
				finalMessage = purple + "Type either " + green + "Unbreakable" + purple + " or " + green + "Regenerate" + purple + ".";
				break;
			}
			case 6:
			{
				context.getForWhom().sendRawMessage(
						purple + "Would you like this block to break naturally or would you like the items to be added to the players's inventory?");
				finalMessage = purple + "Type either " + green + "Natural" + purple + " or " + green + "UnNatural" + purple + ".";
				break;
			}
			case 7:
			{
				context.getForWhom().sendRawMessage(purple + "How long would you like this block to remain broken before it is regenerated?");
				finalMessage = purple + "Enter a value in the format: " + red + "[" + green + "Number" + red + "] [" + green + "Unit" + red + "]"
						+ purple + " (omit the brackets)";
				break;
			}
			case 8:
			{
				context.getForWhom().sendRawMessage(purple + "When it is broken, would you like this block to be replaced by cobblestone or air?");
				finalMessage = purple + "Type either " + green + "Cobblestone" + purple + " or " + green + "Air" + purple + ".";
				break;
			}
			case 9:
			{
				context.getForWhom().sendRawMessage(purple + "How much XP do you want to give?");
				finalMessage = purple + "Enter a " + green + "Number" + purple + " greater than -1.";
				break;
			}
			case 10:
			{
				context.getForWhom().sendRawMessage(purple + "If you would like to use a special effect, enter it.");
				finalMessage = purple + "Type either " + green + "None" + purple + " or " + green + "The name of an Effect" + purple + ".";
				break;
			}
			case 11:
			{
				context.getForWhom().sendRawMessage(purple + "What type of product would you like to give when this block is broken?");
				context.getForWhom().sendRawMessage(purple + "Enter a material value and/or a data value in the format:" + red + "[" + green
						+ "Material" + red + "] [" + green + "DataValue" + red + "]" + purple + "(omit brackets)");
				finalMessage = purple + "Material enum reference: " + ChatColor.RESET
						+ "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html";
				break;
			}
			case 12:
			{
				context.getForWhom().sendRawMessage(purple + "How much product would you like to give?");
				finalMessage = purple + "Enter a " + green + "Number" + purple + ".";
				break;
			}
		}

		return finalMessage;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input)
	{
		String lowerCaseInput = input.toLowerCase().trim();
		if (lowerCaseInput.startsWith("/"))
			lowerCaseInput = lowerCaseInput.substring(1);
		

		if ("quit".equals(lowerCaseInput) || "exit".equals(lowerCaseInput) || "stop".equals(lowerCaseInput))
		{
			context.getForWhom().sendRawMessage(ChatColor.GOLD + "Regenerating Block Helper" + ChatColor.LIGHT_PURPLE + " has been closed.");
			return Prompt.END_OF_CONVERSATION;
		} else if ("back".equals(lowerCaseInput) || "previous".equals(lowerCaseInput))
		{
			if (this.questionlvl == 4)
				this.questionlvl = 0;
			else if (this.questionlvl == 0)
				this.questionlvl = 0;
			else
				this.questionlvl--;
			return this;
		}

		switch (questionlvl)
		{
			case 0:
			{
				if ("ok".equals(lowerCaseInput))
				{
					questionlvl = 1;
				}
				break;
			}
			case 1:
			{
				if (this.b.MaterialData == (int) block.getData())
				{
					if ("remove".equals(lowerCaseInput))
					{
						if (Game.getGameMap().getRegeneratingBlocks().removeRegeneratingBlock(b.Type, b.MaterialData))
							context.getForWhom().sendRawMessage(purple + "The regenerating block has been removed.");
						else
							context.getForWhom().sendRawMessage(purple + "There was an error while removing the regenerating block.");
						return this.endBlockHelper(context);
					} else if ("override".equals(lowerCaseInput))
					{
						this.questionlvl = 4;
					}
				} else if (b.MaterialData == -1)
				{
					if ("remove".equals(lowerCaseInput))
					{
						if (Game.getGameMap().getRegeneratingBlocks().removeRegeneratingBlock(b.Type, b.MaterialData))
							context.getForWhom().sendRawMessage(purple + "The regenerating block has been removed.");
						else
							context.getForWhom().sendRawMessage(purple + "There was an error while removing the regenerating block.");
						return this.endBlockHelper(context);
					}
				}
				break;
			}
			case 2:
			{
				break;
			}
			case 3:
			{
				break;
			}
			case 4:
			{
				if ("this".equals(lowerCaseInput))
				{
					this.dataVal = (int) block.getData();
					questionlvl = 5;
				} else if ("all".equals(lowerCaseInput))
				{
					this.dataVal = -1;
					questionlvl = 5;
				}
				break;
			}
			case 5:
			{
				if ("unbreakable".equals(lowerCaseInput))
				{
					this.regenerate = false;
					return saveBlockAndQuit(context);
				} else if ("regenerate".equals(lowerCaseInput))
				{
					this.regenerate = true;
					this.questionlvl = 6;
				}
				break;
			}
			case 6:
			{
				if ("natural".equals(lowerCaseInput))
				{
					this.naturalBreak = true;
					this.questionlvl = 7;
				} else if ("unnatural".equals(lowerCaseInput))
				{
					this.naturalBreak = false;
					this.questionlvl = 7;
				}
				break;
			}
			case 7:
			{
				String[] args = lowerCaseInput.split(" ");
				if (args.length == 2)
				{
					try
					{
						int number = Integer.parseInt(args[0]);
						TimeUnit u = MapBuilder.getUnit(args[1]);
						if (u != null)
						{
							this.time = number;
							this.unit = u;

							if (this.naturalBreak)
								return this.saveBlockAndQuit(context);
							else
								this.questionlvl = 8;
						}
					} catch (Exception e)
					{

					}
				}
				break;
			}
			case 8:
			{
				if ("cobblestone".equals(lowerCaseInput))
				{
					this.cobbleReplace = true;
					this.questionlvl = 9;
				} else if ("air".equals(lowerCaseInput))
				{
					this.cobbleReplace = false;
					this.questionlvl = 9;
				}
				break;
			}
			case 9:
			{
				try
				{
					int num = Integer.parseInt(lowerCaseInput);
					this.xp = num;
					this.questionlvl = 10;
				} catch (Exception e)
				{

				}
				break;
			}
			case 10:
			{
				if ("none".equals(lowerCaseInput))
				{
					this.effect = null;
					this.questionlvl = 11;
				} else if ("gravel".equals(lowerCaseInput))
				{
					this.effect = "Gravel";
					return this.saveBlockAndQuit(context);
				}
				break;
			}
			case 11:
			{
				String[] args = lowerCaseInput.split(" ");
				try
				{
					if (args.length == 1)
						this.productData = -1;
					else
						this.productData = Integer.parseInt(args[1]);
					args[0] = args[0].toUpperCase().replace(" ", "_");
					Material m = Material.getMaterial(args[0]);
					if (m != null)
					{
						this.product = m;
						this.questionlvl = 12;
					}
				} catch (Exception e)
				{

				}
				break;
			}
			case 12:
			{
				try
				{
					if (lowerCaseInput.contains("random"))
					{
						String x, y;
						x = lowerCaseInput.split(",")[0];
						y = lowerCaseInput.split(",")[1];
						x = x.substring(7);
						y = y.substring(0, y.length() - 1);
						try
						{
							Integer.parseInt(x);
							Integer.parseInt(y);
							this.amount = lowerCaseInput.toUpperCase();
							return this.saveBlockAndQuit(context);
						} catch (NumberFormatException e)
						{

						}
					} else
					{
						Integer r = Integer.parseInt(lowerCaseInput);
						this.amount = r.toString();
						return this.saveBlockAndQuit(context);
					}
				} catch (Exception e)
				{

				}
				break;
			}
			default:
				break;
		}

		return this;
	}

	private Prompt saveBlockAndQuit(ConversationContext context)
	{
		Game.getGameMap().getRegeneratingBlocks().addRegeneratingBlock(new RegeneratingBlock(this.mat, this.dataVal, this.regenerate,
				this.cobbleReplace, this.naturalBreak, this.time, this.unit, this.xp, this.product, this.amount, this.productData, this.effect));
		return endBlockHelper(context);
	}

	private Prompt endBlockHelper(ConversationContext context)
	{
		context.getForWhom().sendRawMessage(purple + "These regenerating block settings have been saved.");
		context.getForWhom().sendRawMessage(ChatColor.GOLD + "Regenerating Block Helper" + ChatColor.LIGHT_PURPLE + " has been closed.");
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input)
	{
		return true;
	}
}
