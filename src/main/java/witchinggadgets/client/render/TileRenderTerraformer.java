package witchinggadgets.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import witchinggadgets.client.ClientProxy;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformer;

public class TileRenderTerraformer extends TileEntitySpecialRenderer
{
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		//GL11.glDisable(GL11.GL_LIGHTING);
		//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)x+.5f, (float)y, (float)z+.5f);

		if(ClientProxy.terraformerModel != null)
		{
			ClientUtilities.bindTexture("witchinggadgets:textures/models/terraformer.png");			ClientProxy.terraformerModel.renderPart("main_01");
			if(tile!=null && tile.getWorldObj()!=null)
			{
				Aspect a = ((TileEntityTerraformer)tile).getSuctionType(null);
				if(a!=null)
					GL11.glColor3f((a.getColor()>>16&255)/255f, (a.getColor()>>8&255)/255f, (a.getColor()&255)/255f);
			}
			ClientProxy.terraformerModel.renderPart("crystals_02");
			GL11.glColor3f(1, 1, 1);
		}

		GL11.glPopMatrix();
	}

}