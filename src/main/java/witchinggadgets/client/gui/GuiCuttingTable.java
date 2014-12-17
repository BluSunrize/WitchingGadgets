package witchinggadgets.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.gui.ContainerCuttingTable;
import witchinggadgets.common.util.network.PacketTileUpdate;

public class GuiCuttingTable extends GuiContainer
{
	private TileEntityCuttingTable tile;
	private EntityPlayer player;

	public GuiCuttingTable (InventoryPlayer inventoryPlayer, TileEntityCuttingTable tileEntity)
	{
		super(new ContainerCuttingTable(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
		this.player = inventoryPlayer.player;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mX, int mY)
	{
		if(this.tile.getOutput()!=null)
			this.drawVirtualItem(tile.getOutput(), 118, 24, false, mX, mY, true);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int mX, int mY)
	{
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);   
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/cuttingTable.png");
		//Inventory
		this.drawTexturedModalRect(guiLeft,guiTop, 0, 0, xSize, ySize);

		ClientUtilities.bindTexture("witchinggadgets:textures/gui/research/gemcuts_transparent.png");
		int xOff = this.tile.targetGemCut%3 * 80;
		int yOff = this.tile.targetGemCut/3 * 80;
		Tessellator tes = Tessellator.instance;
		GL11.glEnable(3042);
		tes.startDrawingQuads();
		tes.addVertexWithUV(guiLeft+73,guiTop+17, 0, (xOff)/255f,(yOff)/255f);
		tes.addVertexWithUV(guiLeft+73,guiTop+47, 0, (xOff)/255f,(yOff+80)/255f);
		tes.addVertexWithUV(guiLeft+103,guiTop+47, 0, (xOff+80)/255f,(yOff+80)/255f);
		tes.addVertexWithUV(guiLeft+103,guiTop+17, 0, (xOff+80)/255f,(yOff)/255f);
		tes.draw();

		UtilsFX.drawTag(guiLeft+118,guiTop+41, this.tile.getInfusingAspect(), 0.0F, 0, this.zLevel);

		if(this.tile.getOutput()!=null)
			this.drawVirtualItem(tile.getOutput(), guiLeft+118,guiTop+24, false, mX, mY, false);
	}

	@Override
	protected void mouseClicked(int mX, int mY, int button)
	{
		super.mouseClicked(mX, mY, button);
		mX-= (width - xSize)/2;
		mY-= (height - ySize)/2;
		if(mY>12 && mY<24)
		{
			int old = this.tile.targetGemCut;
			if(mX>106 && mX<117)
				this.tile.targetGemCut = (byte) (old==0?4:0);
//				this.tile.targetGemCut++;
			if(mX>59 && mX<70)
				this.tile.targetGemCut = (byte) (old==0?4:0);
//				this.tile.targetGemCut--;
//			if(this.tile.targetGemCut < 0)
//				this.tile.targetGemCut = (byte) (ItemInfusedGem.GemCut.values().length-1);
//			else if(this.tile.targetGemCut >= ItemInfusedGem.GemCut.values().length)
//				this.tile.targetGemCut=0;
			
			if(this.tile.targetGemCut != old)
				WitchingGadgets.packetPipeline.sendToServer(new PacketTileUpdate(this.tile));
		}
	}

	public void drawVirtualItem(ItemStack stack, int x, int y, boolean isDull, int mouseX, int mouseY, boolean foreground)
	{
		itemRender.renderWithColor = false;
		if(!foreground)
		{
			GL11.glPushMatrix();
			if(isDull)
				itemRender.renderWithColor = true;
			GL11.glEnable(2896);
			GL11.glEnable(2884);
			GL11.glEnable(3042);
			GL11.glColor4f(1,1,1, 0.2F);
			itemRender.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);
			itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);
			GL11.glDisable(3042);
			GL11.glDisable(2896);
			GL11.glPopMatrix();
		}
		else
		{
			GL11.glPushMatrix();
			int xx = mouseX - this.guiLeft - x;
			int yy = mouseY - this.guiTop - y;
			if ((xx >= 0) && (yy >= 0) && (xx < 16) && (yy < 16))
			{
				this.renderToolTip(stack, mouseX-guiLeft, mouseY-guiTop);
				List tooltip = stack.getTooltip(this.player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
				RenderHelper.enableGUIStandardItemLighting();
				//				WGGraphicUtilities.drawTooltipHoveringText(this, tooltip, mouseX-guiLeft, mouseY-guiTop, fontRendererObj, 0, 0);
				//				UtilsFX.drawCustomTooltip(this, itemRender, this.fontRendererObj, tooltip, mouseX-guiLeft, mouseY-guiTop, 11);
			}
			GL11.glPopMatrix();
			//			itemRender.renderWithCoolor = true;
		}
	}
}