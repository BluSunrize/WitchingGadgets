package witchinggadgets.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderRoseVine implements ISimpleBlockRenderingHandler
{

	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		boolean invertXZ = true;
		boolean mirror = false;
		GL11.glPushMatrix();
		GL11.glRotated(-65, 0, 1, 0);
		GL11.glScaled(1.5, 1.5, 1.5);
		GL11.glTranslated(0,-0.0,-0.6);
		
//		WGGraphicUtilities.drawSubBlockInInventory(0.1250, 0.0000, 0.1250, 0.1875, 0.1250, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.1250, 0.0625, 0.0625, 0.1875, 0.2500, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.0625, 0.2500, 0.0000, 0.1250, 0.4375, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.1250, 0.4375, 0.0000, 0.1875, 0.5625, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.0625, 0.5625, 0.0000, 0.1250, 0.7500, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.1250, 0.7500, 0.0000, 0.1875, 0.8125, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.1875, 0.8125, 0.0000, 0.2500, 0.9375, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.1250, 0.9375, 0.0000, 0.1875, 1.0000, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.2500, 0.9375, 0.0000, 0.3750, 1.0000, 0.0625,invertXZ,mirror,block,renderer);
//
//		WGGraphicUtilities.drawSubBlockInInventory(0.3125, 0.0000, 0.0000, 0.3750, 0.1250, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.3750, 0.1250, 0.0625, 0.4375, 0.3750, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.3750, 0.2500, 0.0625, 0.4375, 0.4375, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.2500, 0.3125, 0.0000, 0.3750, 0.5000, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.4375, 0.4375, 0.0625, 0.5000, 0.5000, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.5000, 0.5000, 0.0625, 0.5625, 0.7500, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.3125, 0.5000, 0.0625, 0.3750, 0.6250, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.2500, 0.6250, 0.0000, 0.3125, 0.8125, 0.0625,invertXZ,mirror,block,renderer);
//
//		WGGraphicUtilities.drawSubBlockInInventory(0.7500, 0.0000, 0.0000, 0.8125, 0.2500, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.6875, 0.2500, 0.0625, 0.7500, 0.4375, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.6875, 0.4375, 0.0625, 0.7500, 0.6250, 0.1250,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.6250, 0.6250, 0.0000, 0.6875, 0.7500, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.5625, 0.7500, 0.0000, 0.6250, 0.8125, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.6875, 0.7500, 0.0000, 0.7500, 0.8750, 0.0625,invertXZ,mirror,block,renderer);
//		WGGraphicUtilities.drawSubBlockInInventory(0.7500, 0.8125, 0.0000, 0.8125, 1.0000, 0.0625,invertXZ,mirror,block,renderer);
		
		ClientUtilities.drawSubBlockInInventory(0.3750, 0.0000, 0.3750, 0.5000, 0.1875, 0.5000,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.4375, 0.1875, 0.5000, 0.5625, 0.3125, 0.5625,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.5000, 0.1875, 0.5625, 0.6250, 0.3125, 0.6250,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.5625, 0.1250, 0.6250, 0.6250, 0.2500, 0.6875,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.6250, 0.0625, 0.6875, 0.6875, 0.1875, 0.7500,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.6875, 0.0000, 0.7500, 0.7500, 0.1250, 0.8125,invertXZ,mirror,block,renderer);

		ClientUtilities.drawSubBlockInInventory(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.7500, 0.1250, 0.6875, 0.8125, 0.1875, 0.7500,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.8125, 0.1250, 0.6875, 0.8750, 0.2500, 0.7500,invertXZ,mirror,block,renderer);

		ClientUtilities.drawSubBlockInInventory(0.2500, 0.0000, 0.6250, 0.3125, 0.1250, 0.6875,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.2500, 0.1250, 0.6875, 0.3125, 0.1875, 0.7500,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.1875, 0.1250, 0.6875, 0.2500, 0.2500, 0.7500,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.1250, 0.1250, 0.7500, 0.1875, 0.1875, 0.8125,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.0625, 0.0000, 0.7500, 0.1250, 0.1250, 0.8125,invertXZ,mirror,block,renderer);

		ClientUtilities.drawSubBlockInInventory(0.1250, 0.0000, 0.1250, 0.2500, 0.1250, 0.2500,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.1875, 0.1250, 0.1875, 0.3125, 0.1875, 0.3125,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.2500, 0.1875, 0.1875, 0.3750, 0.2500, 0.3125,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.3750, 0.2500, 0.1875, 0.6250, 0.3125, 0.3125,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.6250, 0.1875, 0.2500, 0.7500, 0.2500, 0.3750,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.7500, 0.0625, 0.2500, 0.8125, 0.1875, 0.3750,invertXZ,mirror,block,renderer);

		ClientUtilities.drawSubBlockInInventory(0.0625, 0.0000, 0.4375, 0.1875, 0.1875, 0.5625,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.1250, 0.1875, 0.5000, 0.2500, 0.2500, 0.6250,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.1875, 0.2500, 0.5625, 0.3750, 0.3125, 0.6875,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.3125, 0.1875, 0.6250, 0.5000, 0.2500, 0.7500,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.4375, 0.0625, 0.6875, 0.5625, 0.1875, 0.8125,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.5000, 0.0000, 0.7500, 0.6250, 0.0625, 0.8750,invertXZ,mirror,block,renderer);

		ClientUtilities.drawSubBlockInInventory(0.6250, 0.0000, 0.0625, 0.7500, 0.1250, 0.1250,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.6250, 0.1250, 0.1250, 0.7500, 0.2500, 0.1875,invertXZ,mirror,block,renderer);
		ClientUtilities.drawSubBlockInInventory(0.6250, 0.2500, 0.1250, 0.6875, 0.3125, 0.1875,invertXZ,mirror,block,renderer);
		
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		int growthState = world.getBlockMetadata(x, y, z);
		//Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("witchinggadgets:textures/blocks/white"));

		if(world.isSideSolid(x, y-1, z, ForgeDirection.UP, false))
			this.drawFloorVines(x, y, z, block, growthState, renderer);
		if(world.isSideSolid(x, y, z-1, ForgeDirection.SOUTH, false))
			this.drawWallVines(x, y, z, block, growthState, renderer,false,false);
		if(world.isSideSolid(x, y, z+1, ForgeDirection.NORTH, false))
			this.drawWallVines(x, y, z, block, growthState, renderer,false,true);
		if(world.isSideSolid(x-1, y, z, ForgeDirection.WEST, false))
			this.drawWallVines(x, y, z, block, growthState, renderer,true,false);
		if(world.isSideSolid(x+1, y, z, ForgeDirection.EAST, false))
			this.drawWallVines(x, y, z, block, growthState, renderer,true,true);

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}

	private void drawWallVines(int x, int y, int z, Block block, int growthState, RenderBlocks renderer, boolean invertXZ, boolean mirror)
	{
		if(growthState == 0)
		{
			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.0000, 0.1875, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.0000, 0.2500, 0.1875, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.3125, 0.0000, 0.0000, 0.3750, 0.1875, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.1875, 0.0000, 0.4375, 0.3125, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
		}
		else if(growthState == 1)
		{
			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.0000, 0.1875, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.0000, 0.2500, 0.1875, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.3125, 0.0000, 0.0000, 0.3750, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.1250, 0.0625, 0.4375, 0.3750, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.3125, 0.0000, 0.5000, 0.4375, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.0000, 0.8125, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.1250, 0.0000, 0.7500, 0.2500, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
		}
		else if(growthState == 2)
		{
			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.0625, 0.1875, 0.1250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.0625, 0.2500, 0.2500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.2500, 0.0000, 0.2500, 0.3125, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.3125, 0.0000, 0.0000, 0.3750, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.1250, 0.0625, 0.4375, 0.3750, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.3125, 0.0000, 0.5000, 0.4375, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.0000, 0.8125, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.1250, 0.0625, 0.7500, 0.3750, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.3750, 0.0000, 0.7500, 0.5000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
		}
		else if(growthState == 3)
		{
			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.0625, 0.1875, 0.1250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1250, 0.0625, 0.1875, 0.2500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.2500, 0.0000, 0.1250, 0.4375, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.3125, 0.0000, 0.0000, 0.3750, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.1250, 0.0625, 0.4375, 0.3750, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.3125, 0.0000, 0.3750, 0.5000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.0000, 0.8125, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.1250, 0.0625, 0.7500, 0.3750, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.3750, 0.0000, 0.7500, 0.5000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.5000, 0.0000, 0.6875, 0.6250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
		}
		else if(growthState == 4)
		{
			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.0625, 0.1875, 0.1250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1250, 0.0625, 0.1875, 0.2500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.2500, 0.0000, 0.1250, 0.4375, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.4375, 0.0000, 0.1875, 0.5625, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.3125, 0.0000, 0.0000, 0.3750, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.0625, 0.0625, 0.4375, 0.4375, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.3125, 0.0000, 0.3750, 0.5000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.4375, 0.0625, 0.5000, 0.5000, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.5000, 0.0625, 0.5625, 0.7500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.0000, 0.8125, 0.2500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.2500, 0.1250, 0.7500, 0.4375, 0.1875,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.4375, 0.0625, 0.7500, 0.6250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.6250, 0.0000, 0.6875, 0.7500, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.7500, 0.0000, 0.6250, 0.8125, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
		}
		else if(growthState == 5)
		{
			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.1250, 0.1875, 0.1250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.0625, 0.0625, 0.1875, 0.2500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.2500, 0.0000, 0.1250, 0.4375, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.4375, 0.0000, 0.1875, 0.5625, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.5625, 0.0000, 0.1250, 0.7500, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.7500, 0.0000, 0.1875, 0.8125, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.8125, 0.0000, 0.2500, 0.9375, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.9375, 0.0000, 0.1875, 1.0000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.9375, 0.0000, 0.3750, 1.0000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.3125, 0.0000, 0.0000, 0.3750, 0.1250, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.1250, 0.0625, 0.4375, 0.3750, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.2500, 0.0625, 0.4375, 0.4375, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.3125, 0.0000, 0.3750, 0.5000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.4375, 0.0625, 0.5000, 0.5000, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.5000, 0.0625, 0.5625, 0.7500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3125, 0.5000, 0.0625, 0.3750, 0.6250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.6250, 0.0000, 0.3125, 0.8125, 0.0625,invertXZ,mirror,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.0000, 0.8125, 0.2500, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.2500, 0.0625, 0.7500, 0.4375, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.4375, 0.0625, 0.7500, 0.6250, 0.1250,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.6250, 0.0000, 0.6875, 0.7500, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.7500, 0.0000, 0.6250, 0.8125, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.7500, 0.0000, 0.7500, 0.8750, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.8125, 0.0000, 0.8125, 1.0000, 0.0625,invertXZ,mirror,x,y,z,block,renderer);
		}
	}

	private void drawFloorVines(int x, int y, int z, Block block, int growthState, RenderBlocks renderer)
	{
		if(growthState == 0)
		{
			ClientUtilities.drawSubBlock(0.3750, 0.0000, 0.3750, 0.5000, 0.1875, 0.5000,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.1875, 0.3750, 0.4375, 0.3125, 0.4375,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.3125, 0.4375, 0.5000, 0.3750, 0.5000,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
		}
		else if(growthState == 1)
		{
			ClientUtilities.drawSubBlock(0.3750, 0.0000, 0.3705, 0.5000, 0.1875, 0.5000,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.1875, 0.5000, 0.5625, 0.3125, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.1875, 0.5625, 0.6250, 0.3125, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.1875, 0.6250, 0.6250, 0.3125, 0.6875,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.1250, 0.6875, 0.8125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);

		}
		else if(growthState == 2)
		{
			ClientUtilities.drawSubBlock(0.3750, 0.0000, 0.3750, 0.5000, 0.1875, 0.5000,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.1875, 0.5000, 0.5625, 0.3125, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.1875, 0.5625, 0.6250, 0.3125, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.1250, 0.6250, 0.6250, 0.2500, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.0625, 0.6875, 0.6875, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.0000, 0.7500, 0.7500, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.1250, 0.6875, 0.8125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.8125, 0.1250, 0.6875, 0.8750, 0.2500, 0.7500,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.2500, 0.0000, 0.6250, 0.3125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1250, 0.6875, 0.3125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.6875, 0.2500, 0.2500, 0.7500,false,false,x,y,z,block,renderer);
		}
		else if(growthState == 3)
		{
			ClientUtilities.drawSubBlock(0.3750, 0.0000, 0.3750, 0.5000, 0.1875, 0.5000,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.1875, 0.5000, 0.5625, 0.3125, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.1875, 0.5625, 0.6250, 0.3125, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.1250, 0.6250, 0.6250, 0.2500, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.0625, 0.6875, 0.6875, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.0000, 0.7500, 0.7500, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.1250, 0.6875, 0.8125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.8125, 0.1250, 0.6875, 0.8750, 0.2500, 0.7500,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.2500, 0.0000, 0.6250, 0.3125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1250, 0.6875, 0.3125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.6875, 0.2500, 0.2500, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1250, 0.7500, 0.1875, 0.1875, 0.8125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.0000, 0.7500, 0.1250, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.1250, 0.2500, 0.1250, 0.2500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.1875, 0.3125, 0.1875, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1875, 0.1875, 0.3750, 0.2500, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.2500, 0.1875, 0.6250, 0.3125, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.1875, 0.2500, 0.7500, 0.2500, 0.3750,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.0625, 0.2500, 0.8125, 0.1875, 0.3750,false,false,x,y,z,block,renderer);

		}
		else if(growthState == 4)
		{
			ClientUtilities.drawSubBlock(0.3750, 0.0000, 0.3750, 0.5000, 0.1875, 0.5000,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.1875, 0.5000, 0.5625, 0.3125, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.1875, 0.5625, 0.6250, 0.3125, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.1250, 0.6250, 0.6250, 0.2500, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.0625, 0.6875, 0.6875, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.0000, 0.7500, 0.7500, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.1250, 0.6875, 0.8125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.8125, 0.1250, 0.6875, 0.8750, 0.2500, 0.7500,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.2500, 0.0000, 0.6250, 0.3125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1250, 0.6875, 0.3125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.6875, 0.2500, 0.2500, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1250, 0.7500, 0.1875, 0.1875, 0.8125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.0000, 0.7500, 0.1250, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.1250, 0.2500, 0.1250, 0.2500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.1875, 0.3125, 0.1875, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1875, 0.1875, 0.3750, 0.2500, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.2500, 0.1875, 0.6250, 0.3125, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.1875, 0.2500, 0.7500, 0.2500, 0.3750,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.0625, 0.2500, 0.8125, 0.1875, 0.3750,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.0625, 0.0000, 0.4375, 0.1875, 0.1875, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1875, 0.5000, 0.2500, 0.2500, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.2500, 0.5625, 0.3750, 0.3125, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3125, 0.1875, 0.6250, 0.5000, 0.2500, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.0625, 0.6875, 0.5625, 0.1875, 0.8125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.0000, 0.7500, 0.6250, 0.0625, 0.8750,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.6250, 0.0000, 0.0625, 0.7500, 0.1250, 0.1250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.1250, 0.0625, 0.7500, 0.2500, 0.1250,false,false,x,y,z,block,renderer);
		}
		else if(growthState == 5)
		{
			ClientUtilities.drawSubBlock(0.3750, 0.0000, 0.3750, 0.5000, 0.1875, 0.5000,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.1875, 0.5000, 0.5625, 0.3125, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.1875, 0.5625, 0.6250, 0.3125, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5625, 0.1250, 0.6250, 0.6250, 0.2500, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.0625, 0.6875, 0.6875, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6875, 0.0000, 0.7500, 0.7500, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.7500, 0.0000, 0.6250, 0.8125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.1250, 0.6875, 0.8125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.8125, 0.1250, 0.6875, 0.8750, 0.2500, 0.7500,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.2500, 0.0000, 0.6250, 0.3125, 0.1250, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1250, 0.6875, 0.3125, 0.1875, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.6875, 0.2500, 0.2500, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1250, 0.7500, 0.1875, 0.1875, 0.8125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.0625, 0.0000, 0.7500, 0.1250, 0.1250, 0.8125,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.1250, 0.0000, 0.1250, 0.2500, 0.1250, 0.2500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.1250, 0.1875, 0.3125, 0.1875, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.2500, 0.1875, 0.1875, 0.3750, 0.2500, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3750, 0.2500, 0.1875, 0.6250, 0.3125, 0.3125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.1875, 0.2500, 0.7500, 0.2500, 0.3750,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.7500, 0.0625, 0.2500, 0.8125, 0.1875, 0.3750,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.0625, 0.0000, 0.4375, 0.1875, 0.1875, 0.5625,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1250, 0.1875, 0.5000, 0.2500, 0.2500, 0.6250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.1875, 0.2500, 0.5625, 0.3750, 0.3125, 0.6875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.3125, 0.1875, 0.6250, 0.5000, 0.2500, 0.7500,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.4375, 0.0625, 0.6875, 0.5625, 0.1875, 0.8125,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.5000, 0.0000, 0.7500, 0.6250, 0.0625, 0.8750,false,false,x,y,z,block,renderer);

			ClientUtilities.drawSubBlock(0.6250, 0.0000, 0.0625, 0.7500, 0.1250, 0.1250,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.1250, 0.1250, 0.7500, 0.2500, 0.1875,false,false,x,y,z,block,renderer);
			ClientUtilities.drawSubBlock(0.6250, 0.2500, 0.1250, 0.6875, 0.3125, 0.1875,false,false,x,y,z,block,renderer);
		}
	}

}