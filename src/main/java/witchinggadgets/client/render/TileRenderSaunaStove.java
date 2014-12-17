package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntitySaunaStove;

public class TileRenderSaunaStove extends TileEntitySpecialRenderer
{
	final static boolean DEBUG = false;
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntitySaunaStove tile = (TileEntitySaunaStove) tileentity;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		if(DEBUG)
		{
			ClientUtilities.bindTexture("witchinggadgets:textures/blocks/white.png");
			GL11.glLineWidth(5);
			GL11.glEnable(GL11.GL_BLEND);
			for(AxisAlignedBB aabb : tile.boundingBoxes)
			{
				double minX = aabb.minX-tile.xCoord;
				double minY = aabb.minY-tile.yCoord;
				double minZ = aabb.minZ-tile.zCoord;
				double maxX = aabb.maxX-tile.xCoord;
				double maxY = aabb.maxY-tile.yCoord;
				double maxZ = aabb.maxZ-tile.zCoord;
				Tessellator tes = Tessellator.instance;
				tes.startDrawing(2);
				tes.setColorOpaque_I(0xff0000);
				tes.addVertex(minX, minY, minZ);
				tes.addVertex(minX, minY, maxZ);
				tes.addVertex(maxX, minY, maxZ);
				tes.addVertex(maxX, minY, minZ);
				tes.draw();
				tes.startDrawing(2);
				tes.setColorOpaque_I(0xff0000);
				tes.addVertex(minX, minY, minZ);
				tes.addVertex(minX, maxY, minZ);
				tes.addVertex(maxX, maxY, minZ);
				tes.addVertex(maxX, minY, minZ);
				tes.draw();
				tes.startDrawing(2);
				tes.setColorOpaque_I(0xff0000);
				tes.addVertex(minX, minY, maxZ);
				tes.addVertex(minX, maxY, maxZ);
				tes.addVertex(maxX, maxY, maxZ);
				tes.addVertex(maxX, minY, maxZ);
				tes.draw();
				tes.startDrawing(2);
				tes.setColorOpaque_I(0xff0000);
				tes.addVertex(minX, maxY, minZ);
				tes.addVertex(minX, maxY, maxZ);
				tes.addVertex(maxX, maxY, maxZ);
				tes.addVertex(maxX, maxY, minZ);
				tes.draw();
			}
		}

		GL11.glPopMatrix();
	}
}