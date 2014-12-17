package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityMagicalTileLock;

public class TileRenderMagicalTileLock extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityMagicalTileLock tile = (TileEntityMagicalTileLock) tileentity;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		ClientUtilities.bindTexture("witchinggadgets:textures/blocks/white.png");
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(.125f, 0f, .1875f, .5f);
		float scale = .5f/3f;
		for(int i=0;i<9;i++)
			if(tile.tiles[i]==1)
			{
				ClientUtilities.renderPixelBlock(Tessellator.instance, 1.5+(i%3), 0, 1.5+(i/3), scale, 0,0,1,1);
				ClientUtilities.renderPixelBlock(Tessellator.instance, 1.5+(i%3), 5, 1.5+(i/3), scale, 0,0,1,1);
				ClientUtilities.renderPixelBlock(Tessellator.instance, 0, 3.5-(i%3), 1.5+(i/3), scale, 0,0,1,1);
				ClientUtilities.renderPixelBlock(Tessellator.instance, 5, 3.5-(i%3), 3.5-(i/3), scale, 0,0,1,1);
				ClientUtilities.renderPixelBlock(Tessellator.instance, 3.5-(i/3), 3.5-(i%3), 0, scale, 0,0,1,1);
				ClientUtilities.renderPixelBlock(Tessellator.instance, 1.5+(i/3), 3.5-(i%3), 5, scale, 0,0,1,1);
				}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}