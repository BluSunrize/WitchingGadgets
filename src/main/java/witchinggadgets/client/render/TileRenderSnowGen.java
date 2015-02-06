package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntitySnowGen;

public class TileRenderSnowGen extends TileEntitySpecialRenderer
{
	public void renderTileEntityAt(TileEntitySnowGen tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		Tessellator tes = Tessellator.instance;
		GL11.glTranslatef((float)x, (float)y, (float)z);

		switch(tile.facing)
		{
		case NORTH:
			break;
		case SOUTH:
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslatef(-1, 0, -1);
			break;
		case EAST:
			GL11.glRotatef(270, 0, 1, 0);
			GL11.glTranslatef(0, 0, -1);
			break;
		case WEST:
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-1, 0, 0);
			break;
		default:
			break;
		}

		ClientUtilities.bindTexture("thaumcraft:textures/models/Bore.png");
		if(tile.facing.equals(ForgeDirection.UP))
		{
			TileRenderCobbleGen.renderPixelBlock(tes, 0.375, 1, 0.375, 0.625, 1.125, 0.625, 0.859375,0.65625,0.8984375,0.71875);
			TileRenderCobbleGen.renderPixelBlock(tes, 0.375, 1.125, 0.375, 0.625, 1.125, 0.625, 0.828125,0.875,0.8671875,0.953125);
		}
		else if(tile.facing.equals(ForgeDirection.DOWN))
		{
			TileRenderCobbleGen.renderPixelBlock(tes, 0.375,-0.125, 0.375, 0.625, 0, 0.625, 0.859375,0.65625,0.8984375,0.71875);
			TileRenderCobbleGen.renderPixelBlock(tes, 0.375,-0.125, 0.375, 0.625,-0.125, 0.625, 0.828125,0.875,0.8671875,0.953125);
		}
		else
		{
			TileRenderCobbleGen.renderPixelBlock(tes, 0.4075, 0.4075, 0, 0.5925, 0.5925, 0.0624, 0.859375,0.65625,0.8984375,0.71875);
			TileRenderCobbleGen.renderPixelBlock(tes, 0.375, 0.375,-0.125, 0.625, 0.625, 0, 0.859375,0.65625,0.8984375,0.71875);
			TileRenderCobbleGen.renderPixelBlock(tes, 0.375, 0.375,-0.125, 0.625, 0.625,-0.125, 0.828125,0.875,0.8671875,0.953125);
		}

		
		// TODO INDICATE ACTIVE STATUS
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		ClientUtilities.bindTexture("textures/blocks/ice.png");
		TileRenderCobbleGen.renderPixelBlock(tes, 0.1875, 0.25, 0.1875, 0.375, 0.5, 0.375, 0,0,1,1);
		TileRenderCobbleGen.renderPixelBlock(tes, 0.375, 0.25, 0.1875, 0.625, 0.5625, 0.4375, 0,0,1,1);
		TileRenderCobbleGen.renderPixelBlock(tes, 0.15625, 0.25, 0.5, 0.4375, 0.5625, 0.8125, 0,0,1,1);
		TileRenderCobbleGen.renderPixelBlock(tes, 0.5, 0.25, 0.5625, 0.75, 0.375, 0.875, 0,0,1,1);
		TileRenderCobbleGen.renderPixelBlock(tes, 0.65625, 0.25, 0.3125, 0.8125, 0.75, 0.5, 0,0,1,1);
		GL11.glDisable(3042);
		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		renderTileEntityAt((TileEntitySnowGen)tileentity, d0, d1, d2, f);
	}

}