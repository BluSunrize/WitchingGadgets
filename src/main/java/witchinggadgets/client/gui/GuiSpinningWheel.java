package witchinggadgets.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;
import witchinggadgets.common.gui.ContainerSpinningWheel;

public class GuiSpinningWheel extends GuiContainer
{
	private TileEntitySpinningWheel tile;

	public GuiSpinningWheel (InventoryPlayer inventoryPlayer,
			TileEntitySpinningWheel tileEntity) {
		super(new ContainerSpinningWheel(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
		this.xSize=184;
		this.ySize=233;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);   
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/spinningwheel.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		//Inventory
		this.drawTexturedModalRect(x, y+144, 0, 144, 184, 90);
		
		//Input
		this.drawTexturedModalRect(x, y, 0, 0, 38, 144);
		
		//Output
		this.drawTexturedModalRect(x+126, y, 126, 0, 48, 144);
		
		//Thread
		int var7 = tile.getProgressScaled(88);
		this.drawTexturedModalRect(x+38, y, 38, 0, var7, 144);
	}
}
