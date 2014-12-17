package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityCobbleGen;

public class TileRenderCobbleGen extends TileEntitySpecialRenderer
{
	public void renderTileEntityAt(TileEntityCobbleGen tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		Tessellator tes = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		int tick = tile.tick;
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

		ClientUtilities.bindTexture("thaumcraft:textures/blocks/woodplain.png");
		renderPixelBlock(tes, 0, 0, 0, 1, 0.1875, 1,0,0,1,0.1875);
		renderPixelBlock(tes, 0.0625, 0.1875, 0.0625, 0.9375, 0.3125, 0.9375,0,0,1,0.125);
		renderPixelBlock(tes, 0, 0.8125, 0, 1, 0.9375, 1,0,0,1,0.1875);

		ClientUtilities.bindTexture("thaumcraft:textures/blocks/arcane_stone.png");
		renderPixelBlock(tes, 0, 1, 0, 1, 1, 1,0,0,1,1);
		renderPixelBlock(tes, 0, 0.9375, 0, 1, 0.999, 1,0,0,0.125,0.125);
		renderPixelBlock(tes, 0.0625, 0.3125, 0.0625, 0.9375, 0.3125, 0.9375,0.0625,0.0625,0.9375,0.9375);
		renderPixelBlock(tes, 0, 0.8125, 0, 1, 0.8125, 1,0,0,1,1);
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/pedestal_top.png");
		renderPixelBlock(tes, 0, 0.1875, 0, 0.125, 0.8125, 0.125,0,0,0.125,1);
		renderPixelBlock(tes, 0, 0.1875, 0.875, 0.125, 0.8125, 1,0,0,0.125,1);
		renderPixelBlock(tes, 0.875, 0.1875, 0, 1, 0.8125, 0.125,0,0,0.125,1);
		renderPixelBlock(tes, 0.875, 0.1875, 0.875, 1, 0.8125, 1,0,0,0.125,1);

		if(!tile.facing.equals(ForgeDirection.UP) && !tile.facing.equals(ForgeDirection.DOWN))
			ClientUtilities.bindTexture("thaumcraft:textures/blocks/liftertop.png");
		else
			ClientUtilities.bindTexture("thaumcraft:textures/blocks/lifterside.png");
		renderPixelBlock(tes, 0.125, 0.125, 0.0624, 0.875, 0.875, 0.0625, 0.125,0.125,0.875,0.875);
		ClientUtilities.bindTexture("thaumcraft:textures/blocks/lifterside.png");
		renderPixelBlock(tes, 0.125, 0.125, 0.9375, 0.875, 0.875, 0.9376, 0.125,0.125,0.875,0.875);
		renderPixelBlock(tes, 0.9375, 0.125, 0.125, 0.9376, 0.625, 0.875, 0.125,0.125,0.875,0.625);
		renderPixelBlock(tes, 0.0624, 0.125, 0.125, 0.0625, 0.625, 0.875, 0.125,0.125,0.875,0.625);

		ClientUtilities.bindTexture("thaumcraft:textures/models/Bore.png");
		if(tile.facing.equals(ForgeDirection.UP))
		{
			renderPixelBlock(tes, 0.375, 1, 0.375, 0.625, 1.125, 0.625, 0.859375,0.65625,0.8984375,0.71875);
			renderPixelBlock(tes, 0.375, 1.125, 0.375, 0.625, 1.125, 0.625, 0.828125,0.875,0.8671875,0.953125);
		}
		else if(tile.facing.equals(ForgeDirection.DOWN))
		{
			renderPixelBlock(tes, 0.375,-0.125, 0.375, 0.625, 0, 0.625, 0.859375,0.65625,0.8984375,0.71875);
			renderPixelBlock(tes, 0.375,-0.125, 0.375, 0.625,-0.125, 0.625, 0.828125,0.875,0.8671875,0.953125);
		}
		else
		{
			renderPixelBlock(tes, 0.4075, 0.4075, 0, 0.5925, 0.5925, 0.0624, 0.859375,0.65625,0.8984375,0.71875);
			renderPixelBlock(tes, 0.375, 0.375,-0.125, 0.625, 0.625, 0, 0.859375,0.65625,0.8984375,0.71875);
			renderPixelBlock(tes, 0.375, 0.375,-0.125, 0.625, 0.625,-0.125, 0.828125,0.875,0.8671875,0.953125);
		}

		if(tile.getWorldObj()==null || tile.getWorldObj().getBlockPowerInput(tile.xCoord, tile.yCoord, tile.zCoord)<=0 && !tile.getWorldObj().isBlockIndirectlyGettingPowered(tile.xCoord, tile.yCoord, tile.zCoord))
		{
			double slowTick = tick/4;
			double loopTick = slowTick*1.65;
			double inc = 1.0/512.0;
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			ClientUtilities.bindTexture("textures/blocks/lava_flow.png");
			renderPixelBlock(tes, 0.1875, 0.3125, 0.375, 0.3125, 0.8125, 0.625, .375,(loopTick*inc),.625,((loopTick+8)*inc));

			ClientUtilities.bindTexture("textures/blocks/water_flow.png");
			renderPixelBlock(tes, 0.6875, 0.3125, 0.375, 0.8125, 0.8125, 0.625, .375,(loopTick*inc),.625,((loopTick+8)*inc));
			GL11.glDisable(3042);

			ClientUtilities.bindTexture("textures/blocks/cobblestone.png");
			if(tick>32)
				renderPixelBlock(tes, 0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875, 0,0,1,1);
		}

		GL11.glEnable(GL11.GL_LIGHTING);

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
		renderTileEntityAt((TileEntityCobbleGen)tileentity, d0, d1, d2, f);
	}

}