package witchinggadgets.common.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.OneWayAction;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import witchinggadgets.common.util.recipe.SpinningRecipe;

@ZenClass("mods.witchinggadgets.SpinningWheel")
public class SpinningWheel
{

	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[] input, String researchTag)
	{
		Object[] oInput = WGMinetweaker.toObjects(input);
		if(oInput==null)
			return;
		SpinningRecipe r = new SpinningRecipe(researchTag, WGMinetweaker.toStack(output), oInput);
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction
	{
		private final SpinningRecipe recipe;
		public Add(SpinningRecipe recipe)
		{
			this.recipe = recipe;
		}
		@Override
		public void apply()
		{
			SpinningRecipe.addRecipe(recipe);
		}
		@Override
		public boolean canUndo()
		{
			return true;
		}
		@Override
		public void undo()
		{
			SpinningRecipe.removeRecipe(recipe);
		}
		@Override
		public String describe()
		{
			return "Adding Spinning Recipe for " + recipe.getOutput().getDisplayName();
		}
		@Override
		public String describeUndo()
		{
			return "Removing Spinning Recipe for " + recipe.getOutput().getDisplayName();
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
			SpinningRecipe.removeRecipe(output);
		}
		@Override
		public String describe()
		{
			return "Removing Spinning Recipe for " + output.getDisplayName();
		}
		@Override
		public Object getOverrideKey()
		{
			return null;
		}
	}
}
