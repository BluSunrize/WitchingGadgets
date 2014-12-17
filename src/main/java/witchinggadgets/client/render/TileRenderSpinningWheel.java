package witchinggadgets.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;

public class TileRenderSpinningWheel extends TileEntitySpecialRenderer
{
	
	ModelSpinningWheel model = new ModelSpinningWheel();

	public void renderTileEntityAt(TileEntitySpinningWheel tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		GL11.glTranslatef((float)x, (float)y, (float)z);
		
		switch(tile.facing)
		{
		case 2:
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, 0.9F, 0.1F);
			break;
		case 3:
			GL11.glRotatef(270, 0, 1, 0);
			GL11.glTranslatef(0.5F, 0.9F, -0.9F);
			break;
		case 4:
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslatef(-0.5F, 0.9F, -0.9F);
			break;
		case 5:
			GL11.glTranslatef(0.5F, 0.9F, 0.1F);
			break;
		}
		
		ClientUtilities.bindTexture("witchinggadgets:textures/models/SpinningWheel.png");
		model.render(tile.animation);

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		renderTileEntityAt((TileEntitySpinningWheel)tileentity, d0, d1, d2, f);
	}
}