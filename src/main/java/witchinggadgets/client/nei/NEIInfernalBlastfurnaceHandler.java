package witchinggadgets.client.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.Utilities.OreDictStack;
import witchinggadgets.common.util.recipe.InfernalBlastfurnaceRecipe;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIInfernalBlastfurnaceHandler extends TemplateRecipeHandler
{
	public class CachedInfernalBlastfurnaceRecipe extends CachedRecipe
	{
		PositionedStack input;
		PositionedStack output;
		PositionedStack bonus;
		public CachedInfernalBlastfurnaceRecipe(InfernalBlastfurnaceRecipe recipe)
		{
			Object oInput = (recipe.getInput() instanceof OreDictStack)?OreDictionary.getOres(((OreDictStack)recipe.getInput()).key):recipe.getInput();
			if(oInput instanceof ArrayList)
			{
				oInput = ((ArrayList) oInput).clone();
				for(ItemStack is : (ArrayList<ItemStack>)oInput)
					is.stackSize = ((OreDictStack)recipe.getInput()).amount;
			}
			input = new PositionedStack(oInput, 24, 24, true);
			output = new PositionedStack(recipe.getOutput(), 126, 14, false);
			if(recipe.getBonus()!=null)
				bonus = new PositionedStack(recipe.getBonus(), 126, 39, false);
		}
		@Override
		public PositionedStack getIngredient()
		{
			return input;
		}
		@Override
		public PositionedStack getResult()
		{
			return output;
		}
		@Override
		public PositionedStack getOtherStack()
		{
			return bonus;
		}
	}

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(59, 8, 48,48), "wgInfernalBlastfurnace"));
	}
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId == getOverlayIdentifier())
		{
			for(InfernalBlastfurnaceRecipe recipe : InfernalBlastfurnaceRecipe.recipes)
				if (recipe != null && recipe.getOutput() != null)
					arecipes.add(new CachedInfernalBlastfurnaceRecipe(recipe));
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	@Override
	public String getRecipeName()
	{
		return StatCollector.translateToLocal("tile.WG_StoneDevice.blastFurnace.name");
	}
	@Override
	public String getGuiTexture()
	{
		return "witchinggadgets:textures/gui/nei/blastfurnace.png";
	}
	@Override
	public String getOverlayIdentifier()
	{
		return "wgInfernalBlastfurnace";
	}
	@Override
	public int recipiesPerPage()
	{
		return 1;
	}
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		InfernalBlastfurnaceRecipe recipe = InfernalBlastfurnaceRecipe.getRecipeForOutput(result);
		if(recipe != null)
			this.arecipes.add(new CachedInfernalBlastfurnaceRecipe(recipe));
	}
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for(InfernalBlastfurnaceRecipe recipe: InfernalBlastfurnaceRecipe.recipes)
			if(recipe != null && ( (recipe.getInput() instanceof OreDictStack)? Utilities.compareToOreName(ingredient, ((OreDictStack)recipe.getInput()).key) : OreDictionary.itemMatches(ingredient, (ItemStack)recipe.getInput(), true)) )
				this.arecipes.add(new CachedInfernalBlastfurnaceRecipe(recipe));
	}
	@Override
	public void drawBackground(int recipe)
	{
		GL11.glColor4f(1, 1, 1, 1);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(0, 0, 5, 11, 166, 106);
	}

}