package witchinggadgets.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityArcaneCrafter;
import witchinggadgets.common.gui.ContainerArcaneCrafter;

public class GuiArcaneCrafter extends GuiContainer
{

	private TileEntityArcaneCrafter tile;
	private InventoryPlayer invP;

	public GuiArcaneCrafter (InventoryPlayer inventoryPlayer,
			TileEntityArcaneCrafter tileEntity) {
		super(new ContainerArcaneCrafter(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
		this.invP = inventoryPlayer;
		this.xSize=192;
		this.ySize=208;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);   
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/arcanecrafter.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		//Inventory
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

//		ItemStack craftResult = tile.getCraftingResult();
//		if(craftResult != null)
//		{
//			drawVirtualItem(craftResult, x+123, y+30, false, mouseX, mouseY);
//		}
//		else
//			return;

//		System.out.println(craftResult);
//		System.out.println(ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tile, this.tile.owner));
//		boolean isArcane = InventoryHelper.areItemStacksEqualStrict(craftResult , ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tile, this.tile.owner));
//		if(isArcane)
//		{
//			AspectList aspectsRequired = ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this.tile, this.tile.owner);
//			AspectList aspectsInWand = new AspectList();
//			if( tile.getStackInSlot(9)!=null && tile.getStackInSlot(9).getItem() instanceof ItemWandCasting)
//				aspectsInWand = ((ItemWandCasting)tile.getStackInSlot(9).getItem()).getAllVis(tile.getStackInSlot(9));
//			int aspectCounter = 0;
//			for(Aspect a:Aspect.getPrimalAspects())
//			{
//				int reqAmount = aspectsRequired.getAmount(a);
//				int containedAmount = aspectsInWand.getAmount(a);
//				int aOffsetX = 76+aspectCounter*18;
//				int aOffsetY = 57;
//				float alpha = 0.5F + (MathHelper.sin((this.invP.player.ticksExisted + 0 * 10) / 2.0F) * 0.2F - 0.2F);
//				if(containedAmount>=reqAmount)alpha = 1;
//				if(reqAmount<=0)
//					alpha = 0.1f;
//				UtilsFX.drawTag(x+aOffsetX, y+aOffsetY, a, reqAmount, 0, this.zLevel, 771, alpha);
//				aspectCounter++;
//			}
//		}
	}


	public void drawVirtualItem(ItemStack stack, int x, int y, boolean isDull, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		if(isDull)
		{
			GuiContainer.itemRender.renderWithColor = false;
		}
		GL11.glEnable(2896);
		GL11.glEnable(2884);
		GL11.glEnable(3042);

		GuiContainer.itemRender.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);

		GuiContainer.itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);

		int xx = mouseX - x;
		int yy = mouseY - y;
		if ((xx >= 0) && (yy >= 0) && (xx < 16) && (yy < 16)) {
			List tooltip = stack.getTooltip(this.invP.player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
            UtilsFX.drawCustomTooltip(this, itemRender, this.fontRendererObj, tooltip, mouseX, mouseY, 11);
//		    			drawItemStackTooltip(stack, mouseX, mouseY);
		}
		if (isDull) {
			GuiContainer.itemRender.renderWithColor = true;
		}
		GL11.glDisable(3042);
		GL11.glDisable(2896);
		GL11.glPopMatrix();
	}
}
