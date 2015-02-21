package witchinggadgets.common.util.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SpinningRecipe 
{
	public static List<SpinningRecipe> recipeList = new ArrayList<SpinningRecipe>();

	ItemStack output;
	Object[] input;

	public SpinningRecipe(ItemStack r_output, Object... r_recipe)
	{
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
				String ret = "Invalid SpinningWheel recipe for: "+r_output.getDisplayName()+" input should be ItemStack, Item, Block or String";
				throw new RuntimeException(ret);
			}
		}
	}

	public boolean inputsMatch(ItemStack[] query)
	{
		if(query == null || this.input == null)return false;
		if(query.length != this.input.length)
			return false;
		
		List<Object> inputList = new ArrayList(Arrays.asList(input));
		boolean match = false;

		for(ItemStack stack : query)
		{
			Iterator iterator = inputList.iterator();
			while(iterator.hasNext())
			{
				Object next = iterator.next();
				if (next instanceof ItemStack)
				{
					if(OreDictionary.itemMatches((ItemStack)next, stack, false))
					{
						match = true;
						iterator.remove();
						break;
					}
				}
				else if (next instanceof ArrayList)
				{
					ArrayList<ItemStack> oreDict = (ArrayList<ItemStack>)next;
					for(ItemStack oreStack : oreDict)
					{
						if(OreDictionary.itemMatches(oreStack, stack, false))
						{
							match = true;
							iterator.remove();
							break;
						}
					}
				}
			}
			if(!match)
				return false;
		}

		return inputList.isEmpty();
	}

	public ItemStack getOutput()
	{
		return this.output;
	}
	public Object[] getInput()
	{
		return this.input;
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
			if(s.inputsMatch(inputCopy))
				return s;
		}
		return null;
	}
	public static SpinningRecipe getSpinningRecipe(ItemStack output)
	{
		Iterator<SpinningRecipe> i = recipeList.iterator();
		while(i.hasNext())
		{
			SpinningRecipe s = i.next();
			if(OreDictionary.itemMatches(s.getOutput(),output, false))
				return s;
		}
		return null;
	}
	public static void removeRecipe(SpinningRecipe recipe)
	{
		recipeList.remove(recipe);
	}
	public static List<SpinningRecipe> removeRecipes(ItemStack stack)
	{
		List<SpinningRecipe> list = new ArrayList();
		Iterator<SpinningRecipe> i = recipeList.iterator();
		while(i.hasNext())
		{
			SpinningRecipe s = i.next();
			if(OreDictionary.itemMatches(s.output, stack, false))
			{
				list.add(s);
				i.remove();
			}
		}	
		return list;
	}
}
