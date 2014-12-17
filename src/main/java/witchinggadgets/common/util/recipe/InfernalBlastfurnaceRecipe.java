package witchinggadgets.common.util.recipe;

import java.util.ArrayList;
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

	public InfernalBlastfurnaceRecipe(ItemStack input, ItemStack output, int time, boolean isOreDict, boolean isSpecial)
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
		if(!this.isOreDict)
			return OreDictionary.itemMatches(input, stack, true);
		return Utilities.stacksMatchWithOreDic(input, stack);
	}

	public static InfernalBlastfurnaceRecipe getRecipe(ItemStack stack)
	{
		for(InfernalBlastfurnaceRecipe ir: recipes)
			if(ir.matches(stack))
				return ir;
		return null;
	}

	public static InfernalBlastfurnaceRecipe addRecipe(String input, int inputSize, ItemStack output, int time, boolean isSpecial)
	{
		if(!OreDictionary.getOres(input).isEmpty())
		{
			ItemStack inputStack = OreDictionary.getOres(input).get(0);
			return addRecipe(inputStack, output, time, true, isSpecial);
		}
		return null;
	}
	public static InfernalBlastfurnaceRecipe addRecipe(ItemStack input, ItemStack output, int time, boolean oreDic, boolean isSpecial)
	{
		InfernalBlastfurnaceRecipe recipe = new InfernalBlastfurnaceRecipe(input,output, time, oreDic, isSpecial);
		recipes.add(recipe);
		return recipe;
	}

	public static void tryAddSpecialOreMelting(String ore, String ingot, boolean isSpecial)
	{
		if(OreDictionary.getOres("ore"+ore).isEmpty()||OreDictionary.getOres("ingot"+ingot).isEmpty())
			return;
		InfernalBlastfurnaceRecipe.addRecipe("ore"+ore,1, Utilities.copyStackWithSize(OreDictionary.getOres("ingot"+ingot).get(0), 1), 1200, isSpecial);
	}

	public static void tryAddIngotImprovement(String base, String result, boolean isSpecial)
	{
		if(OreDictionary.getOres("ingot"+base).isEmpty()||OreDictionary.getOres("ingot"+result).isEmpty())
			return;
		InfernalBlastfurnaceRecipe.addRecipe("ingot"+base,1, Utilities.copyStackWithSize(OreDictionary.getOres("ingot"+result).get(0), 1), 1200, isSpecial);
		if(!OreDictionary.getOres("block"+base).isEmpty())
		{
			if(!OreDictionary.getOres("block"+result).isEmpty())
				InfernalBlastfurnaceRecipe.addRecipe("block"+base,1, Utilities.copyStackWithSize(OreDictionary.getOres("block"+result).get(0), 1), 1200, isSpecial);
			else
				InfernalBlastfurnaceRecipe.addRecipe("block"+base,1, Utilities.copyStackWithSize(OreDictionary.getOres("ingot"+result).get(0), 9), 1200, isSpecial);
		}
	}
}