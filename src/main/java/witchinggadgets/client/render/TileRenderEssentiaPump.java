package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import thaumcraft.common.tiles.TileBellows;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityEssentiaPump;

public class TileRenderEssentiaPump extends TileEntitySpecialRenderer
{
	static TileBellows bellow = new TileBellows();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		Tessellator tes = Tessellator.instance;
		ClientUtilities.bindTexture("thaumcraft:textures/models/alembic.png");
		TileEntityEssentiaPump tile = (TileEntityEssentiaPump)tileentity;

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

		tes.startDrawing(4);
		tes.setNormal(0, 0,1);
		tes.addVertexWithUV(.375,.75, .25, 59/64f, 42/64f);
		tes.addVertexWithUV(.375,.25, .25, 59/64f, 22/64f);
		tes.addVertexWithUV(.25,.5, .25, 64/64f, 32/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0, 0,1);
		tes.addVertexWithUV(.375,.75, .25, 59/64f, 22/64f);
		tes.addVertexWithUV(.625,.75, .25, 45/64f, 22/64f);
		tes.addVertexWithUV(.625,.25, .25, 45/64f, 42/64f);
		tes.addVertexWithUV(.375,.25, .25, 59/64f, 42/64f);
		tes.draw();
		tes.startDrawing(4);
		tes.setNormal(0, 0,1);
		tes.addVertexWithUV(.625,.75, .25, 45/64f, 42/64f);
		tes.addVertexWithUV(.75,.5, .25, 40/64f, 32/64f);
		tes.addVertexWithUV(.625,.25, .25, 45/64f, 22/64f);
		tes.draw();

		tes.startDrawing(4);
		tes.setNormal(0, 0,1);
		tes.addVertexWithUV(0,.5, .75, 64/64f, 32/64f);
		tes.addVertexWithUV(.25, 0, .75, 59/64f, 22/64f);
		tes.addVertexWithUV(.25, 1, .75, 59/64f, 42/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0, 0,1);
		tes.addVertexWithUV(.75, 1, .75, 45/64f, 22/64f);
		tes.addVertexWithUV(.25, 1, .75, 59/64f, 22/64f);
		tes.addVertexWithUV(.25, 0, .75, 59/64f, 42/64f);
		tes.addVertexWithUV(.75, 0, .75, 45/64f, 42/64f);
		tes.draw();
		tes.startDrawing(4);
		tes.setNormal(0, 0,1);
		tes.addVertexWithUV(.75, 0, .75, 45/64f, 22/64f);
		tes.addVertexWithUV(1,.5, .75, 40/64f, 32/64f);
		tes.addVertexWithUV(.75, 1, .75, 45/64f, 42/64f);
		tes.draw();
		////////////////
		tes.startDrawingQuads();
		tes.setNormal(0, 1, 0);
		tes.addVertexWithUV(.25, 1, .75, 18/64f, 46/64f);
		tes.addVertexWithUV(.75, 1, .75, 18/64f, 24/64f);
		tes.addVertexWithUV(.625, .75, .25, 30/64f, 30/64f);
		tes.addVertexWithUV(.375, .75, .25, 30/64f, 40/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0,-1, 0);
		tes.addVertexWithUV(.75, 0, .75, 18/64f, 24/64f);
		tes.addVertexWithUV(.25, 0, .75, 18/64f, 46/64f);
		tes.addVertexWithUV(.375, .25, .25, 30/64f, 40/64f);
		tes.addVertexWithUV(.625, .25, .25, 30/64f, 30/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-1, 1, 0);
		tes.addVertexWithUV(0, .5, .75, 18/64f, 46/64f);
		tes.addVertexWithUV(.25, 1, .75, 18/64f, 24/64f);
		tes.addVertexWithUV(.375, .75, .25, 30/64f, 30/64f);
		tes.addVertexWithUV(.25, .5, .25, 30/64f, 40/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-1,-1, 0);
		tes.addVertexWithUV(.25, 0, .75, 18/64f, 24/64f);
		tes.addVertexWithUV(0, .5, .75, 18/64f, 46/64f);
		tes.addVertexWithUV(.25, .5, .25, 30/64f, 40/64f);
		tes.addVertexWithUV(.375, .25, .25, 30/64f, 30/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(1, 1, 0);
		tes.addVertexWithUV(.75, 1, .75, 18/64f, 46/64f);
		tes.addVertexWithUV(1, .5, .75, 18/64f, 24/64f);
		tes.addVertexWithUV(.75, .5, .25, 30/64f, 30/64f);
		tes.addVertexWithUV(.625, .75, .25, 30/64f, 40/64f);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(1,-1, 0);
		tes.addVertexWithUV(1, .5, .75, 18/64f, 24/64f);
		tes.addVertexWithUV(.75, 0, .75, 18/64f, 46/64f);
		tes.addVertexWithUV(.625, .25, .25, 30/64f, 40/64f);
		tes.addVertexWithUV(.75, .5, .25, 30/64f, 30/64f);
		tes.draw();

		ClientUtilities.bindTexture("thaumcraft:textures/blocks/alchemyblock.png");
		tes.startDrawingQuads();
		tes.setNormal(1, 1, 0);
		tes.addVertexWithUV(.5, .75, .25, 0, 1);
		tes.addVertexWithUV(.75, .5, .25, 1, 1);
		tes.addVertexWithUV(.75, .5, 0, 1, 0);
		tes.addVertexWithUV(.5, .75, 0, 0, 0);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-1,-1, 0);
		tes.addVertexWithUV(.5, .25, .25, 1, 1);
		tes.addVertexWithUV(.25, .5, .25, 0, 1);
		tes.addVertexWithUV(.25, .5, 0, 0, 0);
		tes.addVertexWithUV(.5, .25, 0, 1, 0);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0, 0,-1);
		tes.addVertexWithUV(.5, .75, 0, 1, 0);
		tes.addVertexWithUV(.75, .5, 0, 0, 0);
		tes.addVertexWithUV(.5, .25, 0, 0, 1);
		tes.addVertexWithUV(.25, .5, 0, 1, 1);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-1, 1, 0);
		tes.addVertexWithUV(.25, .5, 0, 0, 1);
		tes.addVertexWithUV(.25, .5, .25, 1, 1);
		tes.addVertexWithUV(.5, .75, .25, 1, 0);
		tes.addVertexWithUV(.5, .75, 0, 0, 0);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(1,-1, 0);
		tes.addVertexWithUV(.5, .25, .25, 0, 1);
		tes.addVertexWithUV(.5, .25, 0, 1, 1);
		tes.addVertexWithUV(.75, .5, 0, 1, 0);
		tes.addVertexWithUV(.75, .5, .25, 0, 0);
		tes.draw();

		try{
			GL11.glScaled(.375, .375, .375);
			GL11.glTranslated(.625,-.0625,-.125);
			GL11.glRotatef(45, 0, 0, 1);
			
			GL11.glRotatef(-90, 0, 1, 0);

			GL11.glTranslatef(0, 0,-1.0625f);
			TileEntityRendererDispatcher.instance.renderTileEntityAt(bellow, 0.0D, 0.0D, 0.0D, 0.0F);
			GL11.glTranslatef(0, 0, 1.0625f);
			
			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glTranslatef(0, .9375f, .875f);
			TileEntityRendererDispatcher.instance.renderTileEntityAt(bellow, 0.0D, 0.0D, 0.0D, 0.0F);
			GL11.glTranslatef(0,-.9375f,-.875f);

			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glTranslatef(0,-1, 1.8125f);
			TileEntityRendererDispatcher.instance.renderTileEntityAt(bellow, 0.0D, 0.0D, 0.0D, 0.0F);
			GL11.glTranslatef(0, 1,-1.8125f);
			
			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glTranslatef(0,-1.9375f,-.125f);
			TileEntityRendererDispatcher.instance.renderTileEntityAt(bellow, 0.0D, 0.0D, 0.0D, 0.0F);
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		//		ModelBellows bellow = new ModelBellows();

		//	    GL11.glScalef(0.5F, inflation/2, 0.5F);
		//		GL11.glScalef(.5f,.5f,.5f);
		//	    bellow.Bag.render(0.0625F);
		//		GL11.glScalef(2,2,2);
		////	    GL11.glScalef(2F, 1/(inflation/2), 2F);
		//	    
		////	    GL11.glTranslated(0, inflation/2, 0);
		//		bellow.TopPlank.render(0.0625F);
		////	    GL11.glTranslated(0,-inflation/2, 0);
		//
		//		bellow.MiddlePlank.render(0.0625F);
		//		
		////	    GL11.glTranslated(0,-inflation/2, 0);
		//		bellow.BottomPlank.render(0.0625F);
		////	    GL11.glTranslated(0, inflation/2, 0);

		GL11.glPopMatrix();
	}

}