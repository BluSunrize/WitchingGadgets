package witchinggadgets.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import thaumcraft.common.blocks.BlockMetalDevice;
import thaumcraft.common.config.ConfigBlocks;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityCobbleGen;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderWoodenDevice implements ISimpleBlockRenderingHandler
{

	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		GL11.glPushMatrix();
		try{
			if(metadata == 0)
			{
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.25F, 0.5F, -0.25F);
				TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntitySpinningWheel(), 0.0D, 0.0D, 0.0D, 0.0F);
			}
			if(metadata == 1)
			{
				//				GL11.glScaled(.5625,.5625,.5625);
				//				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				//				GL11.glTranslatef(-3.125F, 0.75F, 0.5F);
				//				TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntityLoom(), 0.0D, 0.0D, 0.0D, 0.0F);
			}
			if(metadata == 2)
			{
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntityCobbleGen(), 0.0D, 0.0D, 0.0D, 0.0F);
			}
			if(metadata == 3)
				TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntityCuttingTable(), 0.0D, 0.0D, 0.0D, 0.0F);
			if(metadata == 4)
			{
				renderer.setRenderBounds(0, 0, 0, 1, .75f, 1);
				ClientUtilities.drawStandardBlock(block, metadata, renderer);
				renderer.setRenderBounds(0, .75, 0, 1, 1, .1875);
				ClientUtilities.drawStandardBlock(block, metadata, renderer);
				renderer.setRenderBounds(0, .75, .8125, 1, 1, 1);
				ClientUtilities.drawStandardBlock(block, metadata, renderer);
				renderer.setRenderBounds(0, .75, .1875, .1875, 1, .8125);
				ClientUtilities.drawStandardBlock(block, metadata, renderer);
				renderer.setRenderBounds(.8125, .75, .1875, 1, 1, .8125);
				ClientUtilities.drawStandardBlock(block, metadata, renderer);

			}
		}catch(Exception e)
		{
			//Tessellator.instance.draw();
			e.printStackTrace();
			//			System.out.println("Yay for critical errors >_>");
		}
		GL11.glEnable(32826);
		GL11.glPopMatrix();
	}

	public static IIcon coal;
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if(world.getBlockMetadata(x, y, z)==1)
		{
			renderer.setOverrideBlockTexture(ConfigBlocks.blockWoodenDevice.getIcon(0,0));
			renderer.setRenderBounds(0, 0, 0, 1, .1875, 1);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setOverrideBlockTexture(ConfigBlocks.blockCosmeticSolid.getIcon(0,7));
			renderer.setRenderBounds(0, .1875, 0, 1, .25, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .25, 0, 1, .3125, .1875);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .25, .8125, 1, .3125, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .25, .1875, .1875, .3125, .8125);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.8125, .25, .1875, 1, .3125, .8125);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setOverrideBlockTexture(Blocks.water.getIcon(0, 0));
			renderer.setRenderBounds(.1875, .25, .1875, .8125, .3125, .8125);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setOverrideBlockTexture(ConfigBlocks.blockCosmeticSolid.getIcon(2,9));
			renderer.setRenderBounds(0, .3125, 0, .1875, 1, .1875);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .3125, .8125, .1875, 1, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.8125, .3125, .8125, 1, 1, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.8125, .3125, 0, 1, 1, .1875);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setOverrideBlockTexture( ((BlockMetalDevice)ConfigBlocks.blockMetalDevice).icon[8] );
			renderer.setRenderBounds(.125, .25, 0, .125, .875, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.875, .25, 0, .875, .875, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .25, .125, 1, .875, .125);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .25, .875, 1, .875, .875);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setRenderBounds(0, .9375, 0, 1, .9375, 1);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.clearOverrideBlockTexture();
		}
		if(world.getBlockMetadata(x, y, z)==4)
		{
			renderer.setRenderBounds(0, 0, 0, 1, .75f, 1);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setRenderBounds(0, .75, 0, 1, 1, .1875);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .75, .8125, 1, 1, 1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0, .75, .1875, .1875, 1, .8125);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.8125, .75, .1875, 1, 1, .8125);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setOverrideBlockTexture(coal);
			renderPartialCoal(renderer,block,x,y,z, 3 ,5 , 3 ,6 , .125f);
			renderPartialCoal(renderer,block,x,y,z, 5 ,7 , 3 ,6 , .1875f);
			renderPartialCoal(renderer,block,x,y,z, 7 ,9 , 3 ,6 , .15625f);
			renderPartialCoal(renderer,block,x,y,z, 9 ,12, 3 ,7 , .125f);
			renderPartialCoal(renderer,block,x,y,z, 12,13, 3 ,6 , .09375f);
			renderPartialCoal(renderer,block,x,y,z, 3 ,4 , 6 ,7 , .15625f);
			renderPartialCoal(renderer,block,x,y,z, 4 ,6 , 6 ,7 , .1875f);
			renderPartialCoal(renderer,block,x,y,z, 6 ,7 , 6 ,7 , .15625f);
			renderPartialCoal(renderer,block,x,y,z, 7 ,9 , 6 ,7 , .125f);
			renderPartialCoal(renderer,block,x,y,z, 12,13, 6 ,9 , .15625f);
			renderPartialCoal(renderer,block,x,y,z, 3 ,4 , 7 ,9 , .15625f);
			renderPartialCoal(renderer,block,x,y,z, 4 ,6 , 7 ,9 , .125f);
			renderPartialCoal(renderer,block,x,y,z, 6 ,9 , 7 ,8 , .09375f);
			renderPartialCoal(renderer,block,x,y,z, 9 ,10, 7 ,9 , .1875f);
			renderPartialCoal(renderer,block,x,y,z, 10,12, 7 ,8 , .125f);
			renderPartialCoal(renderer,block,x,y,z, 6 ,9 , 8 ,10, .15625f);
			renderPartialCoal(renderer,block,x,y,z, 10,12, 8 ,9 , .15625f);
			renderPartialCoal(renderer,block,x,y,z, 3 ,6 , 9 ,11, .09375f);
			renderPartialCoal(renderer,block,x,y,z, 9 ,13, 9 ,10, .09375f);
			renderPartialCoal(renderer,block,x,y,z, 6 ,8 , 10,11, .125f);
			renderPartialCoal(renderer,block,x,y,z, 8 ,10, 10,11, .1875f);
			renderPartialCoal(renderer,block,x,y,z, 10,13, 10,11, .125f);
			renderPartialCoal(renderer,block,x,y,z, 3 ,4 , 11,13, .1875f);
			renderPartialCoal(renderer,block,x,y,z, 4 ,6 , 11,12, .15625f);
			renderPartialCoal(renderer,block,x,y,z, 6 ,9 , 11,13, .125f);
			renderPartialCoal(renderer,block,x,y,z, 9 ,11, 11,13, .15625f);
			renderPartialCoal(renderer,block,x,y,z, 11,13, 11,13, .09375f);
			renderPartialCoal(renderer,block,x,y,z, 4 ,6 , 12,13, .09375f);
			renderer.clearOverrideBlockTexture();

			return true;
		}
		return false;
	}

	void renderPartialCoal(RenderBlocks renderer, Block b, int x,int y,int z, int xMin, int xMax, int zMin, int zMax, float yF)
	{
		renderer.setRenderBounds(xMin/16f,.75,zMin/16f, xMax/16f,.75+yF,zMax/16f);
		renderer.renderStandardBlock(b, x, y, z);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}

}