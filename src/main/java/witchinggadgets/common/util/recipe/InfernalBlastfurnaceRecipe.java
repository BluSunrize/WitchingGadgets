package witchinggadgets.common.util.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import witchinggadgets.common.util.Utilities;

public class InfernalBlastfurnaceRecipe
{
	private final ItemStack input;
	private final ItemStack output;
	private final int time;
	private final boolean isOreDict;
	private final boolean isSpecial;
	private ItemStack bonus;

	static List<InfernalBlastfurnaceRecipe> recipes = new ArrayList();

	public InfernalBlastfurnaceRecipe(ItemStack output, ItemStack input, int time, boolean isOreDict, boolean isSpecial)
	{
		this.input=input;
		this.output=	output;
		this.time=time;
		this.isOreDict=isOreDict;
		this.isSpecial = isSpecial;
		bonus = null;
	}

	public boolean isSpecial()
	{
		return this.isSpecial;
	}

	public void addBonus(ItemStack bonus)
	{
		this.bonus = bonus;
	}
	public ItemStack getBonus()
	{
		return this.bonus;
	}

	public ItemStack getInput()
	{
		return this.input;
	}
	public ItemStack getOutput()
	{
		return this.output;
	}
	public int getSmeltingTime()
	{
		return this.time;
	}
	public boolean matches(ItemStack stack)
	{
		boolean baseMatch = (!this.isOreDict)? OreDictionary.itemMatches(input, stack, true) : Utilities.stacksMatchWithOreDic(input, stack);
		return baseMatch && stack.stackSize>=input.stackSize;
	}

	public static InfernalBlastfurnaceRecipe getRecipe(ItemStack stack)
	{
		for(InfernalBlastfurnaceRecipe ir: recipes)
			if(ir.matches(stack))
				return ir;
		return null;
	}

	public static InfernalBlastfurnaceRecipe addRecipe(ItemStack output, String input, int inputSize, int time, boolean isSpecial)
	{
		if(!OreDictionary.getOres(input).isEmpty())
		{
			ItemStack inputStack = OreDictionary.getOres(input).get(0);
			return addRecipe(output, inputStack, time, true, isSpecial);
		}
		return null;
	}
	public static InfernalBlastfurnaceRecipe addRecipe(ItemStack output, ItemStack input, int time, boolean oreDic, boolean isSpecial)
	{
		InfernalBlastfurnaceRecipe recipe = new InfernalBlastfurnaceRecipe(output,input, time, oreDic, isSpecial);
		return addRecipe(recipe);
	}
	public static InfernalBlastfurnaceRecipe addRecipe(InfernalBlastfurnaceRecipe recipe)
	{
		recipes.add(recipe);
		return recipe;
	}
	public static void removeRecipe(InfernalBlastfurnaceRecipe recipe)
	{
		recipes.remove(recipe);
	}
	public static void removeRecipe(ItemStack stack)
	{
		Iterator<InfernalBlastfurnaceRecipe> it = recipes.iterator();
		while(it.hasNext())
		{
			InfernalBlastfurnaceRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.output, stack, true))
				it.remove();
		}
	}

	public static void tryAddSpecialOreMelting(String ore, String ingot, boolean isSpecial)
	{
		if(OreDictionary.getOres("ore"+ore).isEmpty()||OreDictionary.getOres("ingot"+ingot).isEmpty())
			return;
		InfernalBlastfurnaceRecipe.addRecipe(Utilities.copyStackWithSize(OreDictionary.getOres("ingot"+ingot).get(0), 1), "ore"+ore,1, 1200, isSpecial);
	}

	public static void tryAddIngotImprovement(String base, String result, boolean isSpecial)
	{
		if(OreDictionary.getOres("ingot"+base).isEmpty()||OreDictionary.getOres("ingot"+result).isEmpty())
			return;
		InfernalBlastfurnaceRecipe.addRecipe(Utilities.copyStackWithSize(OreDictionary.getOres("ingot"+result).get(0), 1), "ingot"+base,1, 1200, isSpecial);
		if(!OreDictionary.getOres("block"+base).isEmpty())
		{
			if(!OreDictionary.getOres("block"+result).isEmpty())
				InfernalBlastfurnaceRecipe.addRecipe(Utilities.copyStackWithSize(OreDictionary.getOres("block"+result).get(0), 1), "block"+base,1, 1200, isSpecial);
			else
				InfernalBlastfurnaceRecipe.addRecipe(Utilities.copyStackWithSize(OreDictionary.getOres("ingot"+result).get(0), 9), "block"+base,1, 1200, isSpecial);
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof InfernalBlastfurnaceRecipe))
			return false;
		InfernalBlastfurnaceRecipe r = (InfernalBlastfurnaceRecipe) o;

		return ItemStack.areItemStacksEqual(r.input, this.input)
				&& ItemStack.areItemStacksEqual(r.output, this.output)
				&& r.isOreDict==this.isOreDict;
	}
}