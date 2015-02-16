package witchinggadgets.common.util.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;

public class SpinningRecipe 
{
	static List<SpinningRecipe> recipeList = new ArrayList<SpinningRecipe>();

	String researchTag;
	ItemStack output;
	Object[] input;

	public SpinningRecipe(String r_tag, ItemStack r_output, Object... r_recipe)
	{
		this.researchTag = r_tag;
		this.output = r_output;
		this.input = new Object[r_recipe.length];

		for(int i=0;i<r_recipe.length;i++)
		{
			Object in = r_recipe[i];

			if (in instanceof ItemStack)
			{
				input[i] = ((ItemStack)in).copy();
			}
			else if (in instanceof Item)
			{
				input[i] = new ItemStack((Item)in);
			}
			else if (in instanceof Block)
			{
				input[i] = new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE);
			}
			else if (in instanceof String)
			{
				input[i] = OreDictionary.getOres((String)in);
			}
			else
			{
				String ret = "Invalid SpinningWheel recipe: "+r_tag+", Output: "+r_output.getDisplayName();
				throw new RuntimeException(ret);
			}
		}
	}

	public boolean inputsMatch(EntityPlayer player, ItemStack[] inp)
	{
		if (researchTag != null && player != null && researchTag.length()>0 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), researchTag)) {
			return false;
		}
		if(inp == null || this.input == null)return false;
		if(inp.length != this.input.length)
		{
			return false;
		}

		List<Object> tempList = new ArrayList<Object>();
		for(int ix=0; ix<this.input.length; ix++)
		{
			tempList.add(input[ix]);
		}

		//		for(Object temp: this.input)
		//		{
		//			//if(temp == null)System.out.println("Impossible!");
		//			//if(temp instanceof ItemStack)System.out.println(((ItemStack)temp).getDisplayName());
		//			//if(temp instanceof ArrayList)System.out.println("OreDictStuff");
		//		}

		boolean inRecipe = false;

		for(int ix=0;ix<inp.length;ix++)
		{
			ItemStack stack = inp[ix];
			Iterator i = tempList.iterator();
			while(i.hasNext())
			{
				boolean match = false;

				Object next = i.next();
				//if(next == null)System.out.println("HOW CAN THAT BE NULL?!");
				if (next instanceof ItemStack)
				{
					match = itemsMatch((ItemStack)next, stack);
				}
				else if (next instanceof ArrayList)
				{
					ArrayList oreDict = (ArrayList)next;
					for (int io=0; io<oreDict.size(); io++)
					{
						ItemStack oreDictStack = (ItemStack)oreDict.get(io);
						match = match || itemsMatch(oreDictStack, stack);
					}
				}



				if(match)
				{
					inRecipe = true;
					tempList.remove(next);
					break;
				}
			}
			if(!inRecipe)
				return false;

		}

		return tempList.isEmpty();
	}

	private boolean itemsMatch(ItemStack i1, ItemStack i2)
	{
		boolean id = i1.getItem().equals(i2.getItem());
		boolean meta = i1.getItemDamage() == i2.getItemDamage();
		return id&&meta;
	}

	public ItemStack getOutput()
	{
		return this.output;
	}



	public static void addRecipe(SpinningRecipe recipe)
	{
		recipeList.add(recipe);
	}
	public static SpinningRecipe getSpinningRecipe(ItemStack[] input)
	{
		Iterator<SpinningRecipe> i = recipeList.iterator();
		int factualLength = 0;
		for(ItemStack temp : input)
		{
			if(temp!=null)factualLength++;
		}
		ItemStack[] inputCopy = new ItemStack[factualLength];
		int ix=0;
		for(ItemStack temp : input)
		{
			if(temp!=null)
			{
				inputCopy[ix]=temp;
				ix++;
			}
		}

		while(i.hasNext())
		{
			SpinningRecipe s = i.next();
			if(s.inputsMatch(null, inputCopy))return s;
		}
		return null;
	}
	public static void removeRecipe(SpinningRecipe recipe)
	{
		recipeList.remove(recipe);
	}
	public static void removeRecipe(ItemStack stack)
	{
		Iterator<SpinningRecipe> i = recipeList.iterator();
		while(i.hasNext())
		{
			SpinningRecipe s = i.next();
			if(ItemStack.areItemStacksEqual(s.output, stack))
				i.remove();
		}		
	}
}
