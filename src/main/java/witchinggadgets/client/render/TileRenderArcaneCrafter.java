package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import thaumcraft.common.items.wands.ItemWandCasting;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityArcaneCrafter;

public class TileRenderArcaneCrafter extends TileEntitySpecialRenderer
{

	public void renderTileEntityAt(TileEntityArcaneCrafter tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		Tessellator tes = Tessellator.instance;
		//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)x, (float)y, (float)z);

		ClientUtilities.bindTexture("thaumcraft:textures/models/worktable.png");
		//FOOT
		renderPixelBlock(tes, 0, 0.25, 0, 1, 0.25, 1, 0.25,0,0.375,0.25);
		renderPixelBlock(tes, 0, 0, 0, 1, 0, 1,       0.25,0,0.375,0.25);
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/planks_greatwood.png");
		renderPixelBlock(tes, 0, 0.0001, 0, 1, 0.2499, 1,    0,0,1,0.25);
		ClientUtilities.bindTexture("thaumcraft:textures/models/worktable.png");
		
		//LEGS
		renderPixelBlock(tes, 0.0625, 0.25, 0.0625, 0.3125, 0.5, 0.3125 ,0.625,0.0625,0.65625,0.0);
		renderPixelBlock(tes, 0.6875, 0.25, 0.0625, 0.9375, 0.5, 0.3125 ,0.625,0.0625,0.65625,0.0);
		renderPixelBlock(tes, 0.0625, 0.25, 0.6875, 0.3125, 0.5, 0.9375 ,0.625,0.0625,0.65625,0.0);
		renderPixelBlock(tes, 0.6875, 0.25, 0.6875, 0.9375, 0.5, 0.9375 ,0.625,0.0625,0.65625,0.0);
		
		//GRID
		GL11.glDisable(GL11.GL_LIGHTING);
		renderPixelBlock(tes, 0.0, 0.9376, 0.0, 1, 0.9376, 1,0.125,0,0.25,0.25);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		//UPPER COLUMNS
		renderPixelBlock(tes, 0, 0.5, 0,     0, 0.9375, 0.25,  0,       0.375, 0.03125, 0.265625);
		renderPixelBlock(tes, 0, 0.5, 0.25,  0, 0.6875, 0.75,  0.03125, 0.375, 0.09375, 0.328125);
		renderPixelBlock(tes, 0, 0.5, 0.75,  0, 0.9375, 1,     0.09375, 0.375, 0.125,   0.265625);

		renderPixelBlock(tes, 0,    0.5, 1,  0.25, 0.9375, 1,  0.125,   0.375, 0.15625, 0.265625);
		renderPixelBlock(tes, 0.25, 0.5, 1,  0.75, 0.6875, 1,  0.15625, 0.375, 0.21875, 0.328125);
		renderPixelBlock(tes, 0.75, 0.5, 1,  1,    0.9375, 1,  0.21875, 0.375, 0.25,    0.265625);

		renderPixelBlock(tes, 1, 0.5, 0.75,  1, 0.9375, 1,     0.28125, 0.375, 0.25,    0.265625);
		renderPixelBlock(tes, 1, 0.5, 0.25,  1, 0.6875, 0.75,  0.34375, 0.375, 0.28125, 0.328125);
		renderPixelBlock(tes, 1, 0.5, 0,     1, 0.9375, 0.25,  0.375,   0.375, 0.34375, 0.265625);

		renderPixelBlock(tes, 0.75, 0.5, 0,  1,    0.9375, 0,  0.40625, 0.375, 0.375,   0.265625);
		renderPixelBlock(tes, 0.25, 0.5, 0,  0.75, 0.6875, 0,  0.46875, 0.375, 0.40625, 0.328125);
		renderPixelBlock(tes, 0,    0.5, 0,  0.25, 0.9375, 0,  0.5,     0.375, 0.46875, 0.265625);
		
		//UPPER FILLERS
		renderPixelBlock(tes, 0, 0.5, 0, 1, 0.5, 1 ,0.25,0,0.375,0.25);
		renderPixelBlock(tes, 0, 0.6875, 0, 1, 0.6875, 1 ,0.25,0,0.375,0.25);
		
		renderPixelBlock(tes, 0.25, 0.5, 0,  0.25, 0.9375, 1,  0,       0.375, 0.125,   0.265625);
		renderPixelBlock(tes, 0.75, 0.5, 0,  0.75, 0.9375, 1,  0,       0.375, 0.125,   0.265625);
		
		renderPixelBlock(tes, 0, 0.5, 0.25,  1, 0.9375, 0.25,  0,       0.375, 0.125,   0.265625);
		renderPixelBlock(tes, 0, 0.5, 0.75,  1, 0.9375, 0.75,  0,       0.375, 0.125,   0.265625);

		renderPixelBlock(tes, 0, 0.9375, 0, 1, 0.9375, 1 ,0.25,0,0.375,0.25);
		
		//CORE
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/applate2.png");
		renderPixelBlock(tes, 0.125, 0.5635, 0.125, 0.875, 0.9375, 0.875,0.125,0.3125,0.875,0.6875);
		
		//TOP
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/thaumiumblock.png");
		renderPixelBlock(tes, 0, 0.9375, 0, 0.1875, 1, 0.0625,0,0,0.1875,0.0625);
		renderPixelBlock(tes, 0, 0.9375, 0.0625, 0.0625, 1, 0.1875,0,0.0625,0.0625,0.1875);
		renderPixelBlock(tes, 0.0625, 0.9375, 0.0625, 0.125, 1, 0.125,0.0625,0.0625,0.125,0.125);
		
		renderPixelBlock(tes, 0.8125, 0.9375, 0.9375, 1, 1, 1,  0.8125,0.9375,1,1);
		renderPixelBlock(tes, 0.9375, 0.9375, 0.8125, 1, 1, 0.9375,   0.9375, 0.8125, 1, 0.9375);
		renderPixelBlock(tes, 0.875, 0.9375, 0.875, 0.9375, 1, 0.9375,0.875,0.875,0.9375,0.9375);

		renderPixelBlock(tes, 0, 0.9375, 0.9375, 0.1875, 1, 1,  0,0.9375,0.1875,1);
		renderPixelBlock(tes, 0, 0.9375, 0.8125, 0.0625, 1, 0.9375,   0, 0.8125, 0.0625, 0.9375);
		renderPixelBlock(tes, 0.0625, 0.9375, 0.875, 0.125, 1, 0.9375, 0.0625,0.875,0.125,0.9375);
		
		renderPixelBlock(tes, 0.8125, 0.9375, 0, 1, 1, 0.0625,      0.8125,0,1,0.0625);
		renderPixelBlock(tes, 0.9375, 0.9375, 0.0625, 1, 1, 0.1875, 0.9375,0.0625,1,0.1875);
		renderPixelBlock(tes, 0.875, 0.9375, 0.0625, 1, 1, 0.125,   0.875,0.0625,0.9375,0.125);
		
		//WAND
		if ((tile.getWorldObj() != null) && (tile.getStackInSlot(9) != null) && ((tile.getStackInSlot(9).getItem() instanceof ItemWandCasting)))
	    {
	      GL11.glPushMatrix();
	      GL11.glTranslatef(0.75F, 1F, 0.25F);
	      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
	      GL11.glRotatef(33.0F, 0.0F, 0.0F, 1.0F);
	      
	      ItemStack is = tile.getStackInSlot(9).copy();
	      is.stackSize = 1;
	      EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0D, 0.0D, 0.0D, is);
	      entityitem.hoverStart = 0.0F;
	      net.minecraft.client.renderer.entity.RenderItem.renderInFrame = true;
	      RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
	      net.minecraft.client.renderer.entity.RenderItem.renderInFrame = false;
	      
	      GL11.glPopMatrix();
	    }
		
		GL11.glPopMatrix();
	}

	public static void renderPixelBlock(Tessellator tes, double x,double y,double z,double pixelLengthX,double pixelLengthY,double pixelLengthZ,double uMin,double vMin,double uMax,double vMax)
	{
		double dXMin = x;
		double dXMax = pixelLengthX;
		double dYMin = y;
		double dYMax = pixelLengthY;
		double dZMin = z;
		double dZMax = pixelLengthZ;
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
		renderTileEntityAt((TileEntityArcaneCrafter)tileentity, d0, d1, d2, f);
	}

}