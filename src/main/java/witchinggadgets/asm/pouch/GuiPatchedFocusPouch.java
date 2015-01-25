package witchinggadgets.asm.pouch;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.lib.UtilsFX;

public class GuiPatchedFocusPouch extends GuiContainer
{
	public GuiPatchedFocusPouch(InventoryPlayer inventoryPlayer, World world, int x, int y, int z)
	{
		super(new ContainerPatchedFocusPouch(inventoryPlayer, world, x, y, z));
		//		this.blockSlot = par1InventoryPlayer.currentItem;
		this.xSize = 175;
		this.ySize = 232;
	}

	@Override
	protected boolean checkHotbarKeys(int par1)
	{
		return false;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		UtilsFX.bindTexture("textures/gui/gui_focuspouch.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		GL11.glEnable(3042);
		drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
		GL11.glDisable(3042);
	}
}
