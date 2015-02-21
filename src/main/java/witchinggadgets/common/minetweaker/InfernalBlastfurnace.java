package witchinggadgets.common.minetweaker;

import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import witchinggadgets.common.util.Utilities.OreDictStack;
import witchinggadgets.common.util.recipe.InfernalBlastfurnaceRecipe;

@ZenClass("mods.witchinggadgets.InfernalBlastfurnace")
public class InfernalBlastfurnace
{
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input, int time, IItemStack bonus, boolean isSpecial)
	{
		Object oInput = WGMinetweaker.toObject(input);
		if(oInput==null)
			return;
		Object inputStack = (oInput instanceof String)? new OreDictStack((String)oInput, input.getAmount()) : (ItemStack)oInput;

		InfernalBlastfurnaceRecipe r = new InfernalBlastfurnaceRecipe(WGMinetweaker.toStack(output), inputStack, time, isSpecial);
		if(bonus!=null)
			r.addBonus(WGMinetweaker.toStack(bonus));
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction
	{
		private final InfernalBlastfurnaceRecipe recipe;
		public Add(InfernalBlastfurnaceRecipe recipe)
		{
			this.recipe = recipe;
		}
		@Override
		public void apply()
		{
			InfernalBlastfurnaceRecipe.addRecipe(recipe);
		}
		@Override
		public boolean canUndo()
		{
			return true;
		}
		@Override
		public void undo()
		{
			InfernalBlastfurnaceRecipe.removeRecipe(recipe);
		}
		@Override
		public String describe()
		{
			return "Adding Infernal Blastfurnace Recipe for " + recipe.getOutput().getDisplayName();
		}
		@Override
		public String describeUndo()
		{
			return "Removing Infernal Blastfurnace Recipe for " + recipe.getOutput().getDisplayName();
		}
		@Override
		public Object getOverrideKey()
		{
			return null;
		}
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		MineTweakerAPI.apply(new Remove(WGMinetweaker.toStack(output)));
	}
	private static class Remove implements IUndoableAction
	{
		private final ItemStack output;
		List<InfernalBlastfurnaceRecipe> removedRecipes;
		public Remove(ItemStack output)
		{
			this.output = output;
		}
		@Override
		public void apply()
		{
			removedRecipes = InfernalBlastfurnaceRecipe.removeRecipes(output);
		}
		@Override
		public void undo()
		{
			if(removedRecipes!=null)
				for(InfernalBlastfurnaceRecipe recipe : removedRecipes)
					if(recipe!=null)
						InfernalBlastfurnaceRecipe.addRecipe(recipe);
		}
		@Override
		public String describe()
		{
			return "Removing Infernal Blastfurnace Recipe for " + output.getDisplayName();
		}
		@Override
		public String describeUndo()
		{
			return "Re-Adding Infernal Blastfurnace Recipe for " + output.getDisplayName();
		}
		@Override
		public Object getOverrideKey()
		{
			return null;
		}
		@Override
		public boolean canUndo()
		{
			return true;
		}
	}
}
