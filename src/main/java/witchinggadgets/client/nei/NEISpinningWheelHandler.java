package witchinggadgets.client.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import scala.actors.threadpool.Arrays;
import witchinggadgets.common.util.recipe.SpinningRecipe;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEISpinningWheelHandler extends TemplateRecipeHandler
{
	public class CachedSpinningWheelRecipe extends CachedRecipe
	{
		PositionedStack[] inputs;
		PositionedStack output;
		public CachedSpinningWheelRecipe(SpinningRecipe recipe)
		{
			inputs = new PositionedStack[recipe.getInput().length];
			for(int i=0; i<inputs.length; i++)
				inputs[i] = new PositionedStack(recipe.getInput()[i], 21, 9+18*i, true);
			output = new PositionedStack(recipe.getOutput(), 126, 46, false);
		}
		@Override
		public List<PositionedStack> getIngredients()
		{
			return Arrays.asList(inputs);
		}
		@Override
		public PositionedStack getResult()
		{
			return output;
		}
	}

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(81,46, 32,16), "wgSpinningWheel"));
	}
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId == getOverlayIdentifier())
		{
			for(SpinningRecipe recipe : SpinningRecipe.recipeList)
				if (recipe != null && recipe.getOutput() != null)
					arecipes.add(new CachedSpinningWheelRecipe(recipe));
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	@Override
	public String getRecipeName()
	{
		return StatCollector.translateToLocal("tile.WG_WoodenDevice.spinningWheel.name");
	}
	@Override
	public String getGuiTexture()
	{
		return "witchinggadgets:textures/gui/nei/spinningwheel.png";
	}
	@Override
	public String getOverlayIdentifier()
	{
		return "wgSpinningWheel";
	}
	@Override
	public int recipiesPerPage()
	{
		return 1;
	}
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		SpinningRecipe recipe = SpinningRecipe.getSpinningRecipe(result);
		if(recipe != null)
			this.arecipes.add(new CachedSpinningWheelRecipe(recipe));
	}
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for(SpinningRecipe recipe : SpinningRecipe.recipeList)
			if(recipe != null && recipe.getOutput() != null)
				for(Object ss : recipe.getInput())
				{
					if(ss instanceof List && ((List)ss).contains(ingredient))
					{
						this.arecipes.add(new CachedSpinningWheelRecipe(recipe));
						break;
					}
					else if(ss instanceof ItemStack && OreDictionary.itemMatches((ItemStack) ss, ingredient, true))
					{
						this.arecipes.add(new CachedSpinningWheelRecipe(recipe));
						break;
					}
				}
	}
	@Override
	public void drawBackground(int recipe)
	{
		GL11.glColor4f(1, 1, 1, 1);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(0, 0, 5, 11, 166, 106);
	}

}