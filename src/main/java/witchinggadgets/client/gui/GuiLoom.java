package witchinggadgets.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityLoom;
import witchinggadgets.common.gui.ContainerLoom;
import witchinggadgets.common.util.recipe.WeavingRecipe;

public class GuiLoom extends GuiContainer
{
	private TileEntityLoom tile;
	private InventoryPlayer invP;

	public GuiLoom (InventoryPlayer inventoryPlayer,
			TileEntityLoom tileEntity) {
		super(new ContainerLoom(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
		this.xSize=184;
		this.ySize=233;
		this.invP = inventoryPlayer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int mX, int mY)
	{
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);   
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/loom.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		//Inventory
		this.drawTexturedModalRect(x, y+144, 0, 144, 184, 90);

		//Input
		this.drawTexturedModalRect(x, y, 0, 0, 38, 144);
		this.drawTexturedModalRect(x, y, 0, 0, 149, 31);

		//Output
		this.drawTexturedModalRect(x+111, y+100, 111, 100, 40, 40);

		//Thread
		int var7 = tile.getProgressScaled(73);
		this.drawTexturedModalRect(x+38, y+47, 38, 47, var7, 76);
		var7 = tile.getProgressScaled(68);
		this.drawTexturedModalRect(x+57, y+31, 57, 31, 75, var7);


		WeavingRecipe w = this.tile.getRecipe();
		if(w!= null)
		{
			if(this.tile.getStackInSlot(8)==null)
				drawVirtualItem(w.getOutput(), x+123, y+112, true, mX, mY);
			AspectList aspects = this.tile.storedAspects;
			AspectList neededApects = w.getAspects();
			int aspectCounter = 0;
			for(Aspect a:neededApects.getAspectsSorted())
			{
				int reqAmount = neededApects.getAmount(a);
				int aOffsetX = 154+((aspectCounter%6)*18);
				int aOffsetY = 32;
				if(aspectCounter>5)aOffsetY+=18;
				if(aspects.getAmount(a)>0 || aspects.getAmount(a)<reqAmount)
				{
					float alpha = 0.5F + (MathHelper.sin((this.invP.player.ticksExisted + 0 * 10) / 2.0F) * 0.2F - 0.2F);
					if(aspects.getAmount(a)>=reqAmount)alpha = 1;
					UtilsFX.drawTag(x+aOffsetX, y+aOffsetY, a, aspects.getAmount(a), 0, this.zLevel, 771, alpha);
					aspectCounter++;
				}
			}
		}
	}

	public void drawVirtualItem(ItemStack stack, int x, int y, boolean isDull, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		itemRender.renderWithColor = false;
		GL11.glEnable(2896);
		GL11.glEnable(2884);
		GL11.glEnable(3042);

		GL11.glColor4f(1,1,1, 0.2F);
		itemRender.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);

		itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);

		GL11.glColor4f(1,1,1,1);
		int xx = mouseX - x;
		int yy = mouseY - y;
		if ((xx >= 0) && (yy >= 0) && (xx < 16) && (yy < 16)) {
			List tooltip = stack.getTooltip(this.invP.player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
            UtilsFX.drawCustomTooltip(this, itemRender, this.fontRendererObj, tooltip, mouseX, mouseY, 11);
		}
		itemRender.renderWithColor = true;
		GL11.glDisable(3042);
		GL11.glDisable(2896);
		GL11.glPopMatrix();
	}
}
