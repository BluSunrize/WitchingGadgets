package witchinggadgets.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityLoom;

public class TileRenderLoom extends TileEntitySpecialRenderer
{

	public void renderTileEntityAt(TileEntityLoom tile, double x, double y, double z, float f)
	{
		if(tile.isDummy)return;

		GL11.glPushMatrix();

		Tessellator tes = Tessellator.instance;

		//GL11.glEnable(3042);
		//GL11.glBlendFunc(770, 771);

		GL11.glTranslatef((float)x, (float)y, (float)z);

		switch(tile.facing)
		{
		case 2:
			GL11.glTranslatef(-1, 0, -1);
			break;
		case 3:
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslatef(-2, 0, -2);
			break;
		case 4:
			GL11.glRotatef(270, 0, 1, 0);
			GL11.glTranslatef(-1, 0, -2);
			break;
		case 5:
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-2, 0, -1);
			break;
		}

		ClientUtilities.bindTexture("thaumcraft:textures/blocks/greatwoodside.png");
		renderPixelBlock(tes, 0.5, 0, 1, 0.25, 0.625, 0.25,0,0,1,1);
		renderPixelBlock(tes, 6.5, 0, 1, 0.25, 0.625, 0.25,0,0,1,1);
		renderPixelBlock(tes, 6.5, 1, 1, 0.25, 0.625, 0.25,0,0,1,1);
		renderPixelBlock(tes, 6.5, 2, 1, 0.25, 0.625, 0.25,0,0,1,1);

		renderPixelBlock(tes, 0.5, 0, 10, 0.25, 0.625, 0.25,0,0,1,1);
		renderPixelBlock(tes, 6.5, 0, 10, 0.25, 0.625, 0.25,0,0,1,1);
		renderPixelBlock(tes, 6.5, 1, 10, 0.25, 0.625, 0.25,0,0,1,1);
		renderPixelBlock(tes, 6.5, 2, 10, 0.25, 0.625, 0.25,0,0,1,1);

		GL11.glRotated(90, 0, 0, 1);
		//BottomLeft
		renderPixelBlock(tes, 1, -3.125, 2.5, 0.25, 0.625, 0.125,0,0,1,1);
		renderPixelBlock(tes, 1, -2.125, 2.5, 0.25, 0.625, 0.125,0,0,1,1);
		renderPixelBlock(tes, 1, -1.125, 2.5, 0.25, 0.625, 0.125,0,0,1,1);
		//TopLeft
		renderPixelBlock(tes, 6, -3, 2.5, 0.25, 0.625, 0.125,0,0,1,1);
		renderPixelBlock(tes, 6, -2, 2.5, 0.25, 0.625, 0.125,0,0,1,1);
		//BottomRight
		renderPixelBlock(tes, 1, -3.125, 20.5, 0.25, 0.625, 0.125,0,0,1,1);
		renderPixelBlock(tes, 1, -2.125, 20.5, 0.25, 0.625, 0.125,0,0,1,1);
		renderPixelBlock(tes, 1, -1.125, 20.5, 0.25, 0.625, 0.125,0,0,1,1);
		//TopRight
		renderPixelBlock(tes, 6, -3, 20.5, 0.25, 0.625, 0.125,0,0,1,1);
		renderPixelBlock(tes, 6, -2, 20.5, 0.25, 0.625, 0.125,0,0,1,1);
		GL11.glRotated(90, 0, 0,-1);
		//Top Bar
		GL11.glRotated(90, 1, 0, 0);
		renderPixelBlock(tes, 5.5, 0.2, -13.5, 0.125, 1.25, 0.125,0,0,1,1);
		renderPixelBlock(tes, 5.5, 1.2, -13.5, 0.125, 1.25, 0.125,0,0,1,1);
		//Bottom Bar
		renderPixelBlock(tes, 1.5, 0.2, -4.5, 0.125, 1.25, 0.125,0,0,1,1);
		renderPixelBlock(tes, 1.5, 1.2, -4.5, 0.125, 1.25, 0.125,0,0,1,1);
		GL11.glRotated(90,-1, 0, 0);
		//Axle
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/wardedstone.png");
		renderPixelBlock(tes, 13.5, 5, 0.1, 0.125, 0.125, 2.5,0,0,1,1);
		//Roll
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/planks_greatwood.png");
		renderPixelBlock(tes, 4.125, 0.875, 1.5, 0.375, 0.5, 0.375,0,0,1,1);
		renderPixelBlock(tes, 2.925, 1.3125, 1.5, 0.5, 0.375, 0.375,0,0,1,1);
		renderPixelBlock(tes, 4.125, 0.875, 2.5, 0.375, 0.5, 0.375,0,0,1,1);
		renderPixelBlock(tes, 2.925, 1.3125, 2.5, 0.5, 0.375, 0.375,0,0,1,1);
		renderPixelBlock(tes, 4.125, 0.875, 3.5, 0.375, 0.5, 0.375,0,0,1,1);
		renderPixelBlock(tes, 2.925, 1.3125, 3.5, 0.5, 0.375, 0.375,0,0,1,1);
		renderPixelBlock(tes, 4.125, 0.875, 4.5, 0.375, 0.5, 0.375,0,0,1,1);
		renderPixelBlock(tes, 2.925, 1.3125, 4.5, 0.5, 0.375, 0.375,0,0,1,1);
		renderPixelBlock(tes, 4.125, 0.875, 5.5, 0.375, 0.5, 0.375,0,0,1,1);
		renderPixelBlock(tes, 2.925, 1.3125, 5.5, 0.5, 0.375, 0.375,0,0,1,1);


		float progress = (float)tile.progress;
		double tick = (double)tile.processingTick/(double)tile.processingMax;
		if(tile.shufflePos)tick = 1-tick;

		ClientUtilities.bindTexture("thaumcraft:textures/blocks/woodplain.png");

		double sAngle = 15;
		double tx = 0.5 + (0.75 * progress);
		double ty = (Math.tan(sAngle) * tx)/4;
		double tz = 1.75 * tick;

		GL11.glTranslated( tx,-ty, tz);
		GL11.glRotated(sAngle,0,0,1);

		renderPixelBlock(tes, 1, 8, 2, 0.125, 0.0625, 0.25,0,0,1,1);


		GL11.glRotated(sAngle,0,0,-1);
		GL11.glTranslated(-tx, ty,-tz);



		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		//RenderHelper.enableStandardItemLighting();

		//Threads
//		IIcon icon = new ItemStack(Block.get,0).getIconIndex();
//
//		if(icon == null)
//		{
//			GL11.glPopMatrix();
//			return;
//		}
		ClientUtilities.bindTexture("witchinggadgets:textures/blocks/loomStrings.png");
		float f0 = 0;//icon.getMinU();
		float f1 = 1;//icon.getMaxU();
		float f2 = 0;//icon.getMinV();
		float f3 = 1;//icon.getMaxV();
		GL11.glScaled(0.75, 0.75, 0.75);
		GL11.glTranslated(1, -0.125, 0.5);
		GL11.glRotated(15,0,0,1);

		double i2 = 2.5 / 3.0;
		
		tes.startDrawingQuads();
		tes.addVertexWithUV(-0.5, 1, 0.25+i2, f1, f3);
		tes.addVertexWithUV(1.5, 1, 0.25+i2, f1, f2);
		tes.addVertexWithUV(1.5, 1, 0.25, f0, f2);
		tes.addVertexWithUV(-0.5, 1, 0.25, f0, f3);
		tes.draw();
		tes.startDrawingQuads();
		tes.addVertexWithUV(-0.5, 1, 0.25+i2*2, f1, f3);
		tes.addVertexWithUV(1.5, 1, 0.25+i2*2, f1, f2);
		tes.addVertexWithUV(1.5, 1, 0.25+i2, f0, f2);
		tes.addVertexWithUV(-0.5, 1, 0.25+i2, f0, f3);
		tes.draw();
		tes.startDrawingQuads();
		tes.addVertexWithUV(-0.5, 1, 2.75, f1, f3);
		tes.addVertexWithUV(1.5, 1, 2.75, f1, f2);
		tes.addVertexWithUV(1.5, 1, 0.25+i2*2, f0, f2);
		tes.addVertexWithUV(-0.5, 1, 0.25+i2*2, f0, f3);
		tes.draw();

		GL11.glRotated(15,0,0,-1);
		GL11.glTranslated(-1, 0.125, -0.5);
		GL11.glScaled(1/0.75, 1/0.75, 1/0.75);


		//Item
		if(tile.getRecipe()!=null)
		{
			ItemStack output= tile.getRecipe().getOutput();
			if(output != null)			
			{
				//System.out.println("heyo");
				
				IIcon icon = output.getIconIndex();
				if(icon == null)
				{
					GL11.glPopMatrix();
					return;
				}
				ClientUtilities.bindTexture(Minecraft.getMinecraft().getTextureManager().getResourceLocation(output.getItemSpriteNumber()).getResourceDomain()+":"+Minecraft.getMinecraft().getTextureManager().getResourceLocation(output.getItemSpriteNumber()).getResourcePath());
				f0 = icon.getMinU();
				f1 = icon.getMaxU();
				float distance = icon.getMaxV() - icon.getMinV();
				f2 = icon.getMaxV() - (distance*progress);
				f3 = icon.getMaxV();
				GL11.glScaled(0.75, 0.75, 0.75);
				GL11.glTranslated(0.9, -0.0625, 0.5);
				GL11.glRotated(15,0,0,1);
				
				int color = output.getItem().getColorFromItemStack(output, 0);
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                GL11.glColor4f(r, g, b, 1.0F);
				
				tes.startDrawingQuads();
				tes.addVertexWithUV(0, 1, 2, f1, f3);
				tes.addVertexWithUV(progress, 1, 2, f1, f2);
				tes.addVertexWithUV(progress, 1, 1, f0, f2);
				tes.addVertexWithUV(0, 1, 1, f0, f3);
				tes.draw();
				

			}
		}
		//drawPlayer(RenderManager.instance.livingPlayer, 1, tile.blockMetadata);

		//GL11.glDisable(3042);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		GL11.glPopMatrix();
	}

	public static void renderPixelBlock(Tessellator tes, double x,double y,double z,double pixelLengthX,double pixelLengthY,double pixelLengthZ,double uMin,double vMin,double uMax,double vMax)
	{
		double dXMin = x*pixelLengthX;
		double dXMax = (x+1)*pixelLengthX;
		double dYMin = y*pixelLengthY;
		double dYMax = (y+1)*pixelLengthY;
		double dZMin = z*pixelLengthZ;
		double dZMax = (z+1)*pixelLengthZ;
		//Side YNeg
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMax, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMin, vMax);
		tes.draw();
		//Side YPos
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMax, vMin);
		tes.draw();
		//Side ZNeg
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMax, vMin);
		tes.draw();
		//Side ZPos
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMin);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMin, vMax);
		tes.draw();
		//Side XNeg
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMax, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMax);
		tes.draw();
		//Side XPos
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMin);
		tes.draw();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		renderTileEntityAt((TileEntityLoom)tileentity, d0, d1, d2, f);
	}

}