package witchinggadgets.common.util.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.Utilities.OreDictStack;

public class InfernalBlastfurnaceRecipe
{
	private final Object input;
	private final ItemStack output;
	private final int time;
	private final boolean isSpecial;
	private ItemStack bonus;

	public static List<InfernalBlastfurnaceRecipe> recipes = new ArrayList();

	public InfernalBlastfurnaceRecipe(ItemStack output, Object input, int time, boolean isSpecial)
	{
		if(input instanceof ItemStack)
			this.input= (ItemStack)input;
		else if(input instanceof OreDictStack)
			this.input= (OreDictStack)input;
		else if(input instanceof String)
			this.input=new OreDictStack((String) input, 1);
		else
			throw new RuntimeException("Infernal Blast Furance Recipes MUST be initialized with ItemStack, OreDictStack or String");

		this.output=output;
		this.time=time;
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

	public Object getInput()
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
		if(input instanceof OreDictStack)
			return ((OreDictStack)input).matches(stack);
		else if(input instanceof ItemStack)
			return OreDictionary.itemMatches((ItemStack) input, stack, false) && (stack.stackSize>=((ItemStack)input).stackSize) ;
		return false;
	}

	public static InfernalBlastfurnaceRecipe getRecipeForInput(ItemStack stack)
	{
		for(InfernalBlastfurnaceRecipe ir: recipes)
			if(ir.matches(stack))
				return ir;
		return null;
	}
	public static InfernalBlastfurnaceRecipe getRecipeForOutput(ItemStack stack)
	{
		for(InfernalBlastfurnaceRecipe ir: recipes)
			if(OreDictionary.itemMatches(ir.getOutput(), stack, true))
				return ir;
			else if(OreDictionary.itemMatches(ir.getBonus(), stack, true))
				return ir;
		return null;
	}

	public static InfernalBlastfurnaceRecipe addRecipe(ItemStack output, String input, int inputSize, int time, boolean isSpecial)
	{
		if(!OreDictionary.getOres(input).isEmpty())
		{
			ItemStack inputStack = OreDictionary.getOres(input).get(0);
			return addRecipe(output, inputStack, time, isSpecial);
		}
		return null;
	}
	public static InfernalBlastfurnaceRecipe addRecipe(ItemStack output, Object input, int time, boolean isSpecial)
	{
		InfernalBlastfurnaceRecipe recipe = new InfernalBlastfurnaceRecipe(output,input, time, isSpecial);
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
	public static List<InfernalBlastfurnaceRecipe> removeRecipes(ItemStack stack)
	{
		List<InfernalBlastfurnaceRecipe> list = new ArrayList();
		Iterator<InfernalBlastfurnaceRecipe> it = recipes.iterator();
		while(it.hasNext())
		{
			InfernalBlastfurnaceRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.output, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
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

		boolean b_out = ItemStack.areItemStacksEqual(r.output, this.output);
		boolean b_in_IS = (this.input instanceof ItemStack && r.input instanceof ItemStack) && OreDictionary.itemMatches((ItemStack)this.input, (ItemStack)r.input, true);
		boolean b_in_OD = (this.input instanceof OreDictStack && r.input instanceof OreDictStack) && ((OreDictStack)this.input).key.equals( ((OreDictStack)r.input).key );
		
		return b_out && (b_in_IS||b_in_OD);
	}
}