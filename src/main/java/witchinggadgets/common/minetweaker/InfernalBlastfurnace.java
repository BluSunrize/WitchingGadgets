package witchinggadgets.common.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.OneWayAction;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import witchinggadgets.common.util.Utilities.OreDictStack;
import witchinggadgets.common.util.recipe.InfernalBlastfurnaceRecipe;

@ZenClass("mods.witchinggadgets.InfernalBlastfurnace")
public class InfernalBlastfurnace
{
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input, int time, @Optional IItemStack bonus, @Optional boolean isSpecial)
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
	private static class Remove extends OneWayAction
	{
		private final ItemStack output;
		public Remove(ItemStack output)
		{
			this.output = output;
		}
		@Override
		public void apply()
		{
			InfernalBlastfurnaceRecipe.removeRecipe(output);
		}
		@Override
		public String describe()
		{
			return "Removing Infernal Blastfurnace Recipe for " + output.getDisplayName();
		}
		@Override
		public Object getOverrideKey()
		{
			return null;
		}
	}
}
