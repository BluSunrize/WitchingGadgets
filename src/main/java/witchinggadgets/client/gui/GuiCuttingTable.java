package witchinggadgets.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.gui.ContainerCuttingTable;
import witchinggadgets.common.items.ItemInfusedGem;
import witchinggadgets.common.util.network.message.MessageTileUpdate;

public class GuiCuttingTable extends GuiContainer
{
	private TileEntityCuttingTable tile;

	public GuiCuttingTable (InventoryPlayer inventoryPlayer, TileEntityCuttingTable tileEntity)
	{
		super(new ContainerCuttingTable(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
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
		int xOff = this.tile.targetGemCut==0?0:80;
		int yOff = this.tile.targetGemCut==0?0:80;
		Tessellator tes = Tessellator.instance;
		GL11.glEnable(3042);
		tes.startDrawingQuads();
		tes.addVertexWithUV(guiLeft+73,guiTop+17, 0, (xOff)/255f,(yOff)/255f);
		tes.addVertexWithUV(guiLeft+73,guiTop+47, 0, (xOff)/255f,(yOff+80)/255f);
		tes.addVertexWithUV(guiLeft+103,guiTop+47, 0, (xOff+80)/255f,(yOff+80)/255f);
		tes.addVertexWithUV(guiLeft+103,guiTop+17, 0, (xOff+80)/255f,(yOff)/255f);
		tes.draw();

		UtilsFX.drawTag(guiLeft+118,guiTop+41, this.tile.getInfusingAspect(), 0.0F, 0, this.zLevel);
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
//				this.tile.targetGemCut = (byte) (old==0?1:0);
				this.tile.targetGemCut++;
			if(mX>59 && mX<70)
//				this.tile.targetGemCut = (byte) (old==0?1:0);
				this.tile.targetGemCut--;
			if(this.tile.targetGemCut < 0)
				this.tile.targetGemCut = (byte) (ItemInfusedGem.GemCut.values().length-1);
			else if(this.tile.targetGemCut >= ItemInfusedGem.GemCut.values().length)
				this.tile.targetGemCut=0;
			
			if(this.tile.targetGemCut != old)
				WitchingGadgets.packetHandler.sendToServer(new MessageTileUpdate(this.tile));
				//WGPacketPipeline.INSTANCE.sendToServer(new PacketTileUpdate(this.tile));
		}
	}
}