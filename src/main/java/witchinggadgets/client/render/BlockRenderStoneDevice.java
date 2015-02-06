package witchinggadgets.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityBlastfurnace;
import witchinggadgets.common.blocks.tiles.TileEntityEtherealWall;
import witchinggadgets.common.blocks.tiles.TileEntityMagicalTileLock;
import witchinggadgets.common.blocks.tiles.TileEntitySarcophagus;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderStoneDevice implements ISimpleBlockRenderingHandler
{
	public static int renderPass = 0;
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		//		System.out.println(block);
		GL11.glPushMatrix();
		try{
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

			ClientUtilities.drawStandardBlock(block, metadata, renderer);

			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}catch(Exception e)
		{
			//			e.printStackTrace();
			Tessellator.instance.draw();
		}
		GL11.glEnable(32826);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityEtherealWall)
		{
			TileEntityEtherealWall tile = (TileEntityEtherealWall) world.getTileEntity(x, y, z);
			Block blockToRender = tile.camoID!=null?tile.camoID:block;
			if(!blockToRender.canRenderInPass(renderPass))
				return false;
		}
		else if(renderPass!=0)
			return false;

		if(world.getTileEntity(x, y, z) instanceof TileEntityEtherealWall)
		{
			TileEntityEtherealWall tile = (TileEntityEtherealWall) world.getTileEntity(x, y, z);

			Block blockToRender = tile.camoID!=null?tile.camoID:block;
			int metaToRender = tile.camoMeta;
			int	renderType = blockToRender!=null?blockToRender.getRenderType():0;

			if(tile.camoID==null)
				return renderer.renderStandardBlock(block, x, y, z);
			if (renderType == -1)
				return false;
			blockToRender.setBlockBoundsBasedOnState(renderer.blockAccess, x, y, z);
			renderer.setRenderBoundsFromBlock(blockToRender);

			int l = block.colorMultiplier(world,x,y,z);
			float[] rgb = {(l>>16&255)/255f,(l>>8&255)/255f,(l&255)/255f};

			if (EntityRenderer.anaglyphEnable)
			{
				float f3 = (rgb[0] * 30.0F + rgb[1] * 59.0F + rgb[2] * 11.0F) / 100.0F;
				float f4 = (rgb[0] * 30.0F + rgb[1] * 70.0F) / 100.0F;
				float f5 = (rgb[0] * 30.0F + rgb[2] * 70.0F) / 100.0F;
				rgb[0] = f3;
				rgb[1] = f4;
				rgb[2] = f5;
			}

			boolean flag=false;
			switch (renderType)
			{
			case 0:
				renderer.renderStandardBlockWithAmbientOcclusion(block, x, y, z, rgb[0], rgb[1], rgb[2]);
				return true;
			case 31: //LOG
				int i1 = metaToRender & 12;

				if (i1 == 4)
				{
					renderer.uvRotateEast = 1;
					renderer.uvRotateWest = 1;
					renderer.uvRotateTop = 1;
					renderer.uvRotateBottom = 1;
				}
				else if (i1 == 8)
				{
					renderer.uvRotateSouth = 1;
					renderer.uvRotateNorth = 1;
				}

				flag = renderer.renderStandardBlock(blockToRender, x, y, z);
				renderer.uvRotateSouth = 0;
				renderer.uvRotateEast = 0;
				renderer.uvRotateWest = 0;
				renderer.uvRotateNorth = 0;
				renderer.uvRotateTop = 0;
				renderer.uvRotateBottom = 0;
				return flag;
			case 39: //QUARTZ
				if (metaToRender == 3)
				{
					renderer.uvRotateEast = 1;
					renderer.uvRotateWest = 1;
					renderer.uvRotateTop = 1;
					renderer.uvRotateBottom = 1;
				}
				else if (metaToRender == 4)
				{
					renderer.uvRotateSouth = 1;
					renderer.uvRotateNorth = 1;
				}

				flag = renderer.renderStandardBlock(blockToRender, x, y, z);
				renderer.uvRotateSouth = 0;
				renderer.uvRotateEast = 0;
				renderer.uvRotateWest = 0;
				renderer.uvRotateNorth = 0;
				renderer.uvRotateTop = 0;
				renderer.uvRotateBottom = 0;
				return flag;
			case 10: return renderer.renderBlockStairs((BlockStairs)blockToRender, x, y, z);
			case 32: return renderer.renderBlockWall((BlockWall)blockToRender, x, y, z);

			case 18: return renderer.renderBlockPane((BlockPane)blockToRender, x, y, z);
			case 21: return renderer.renderBlockFenceGate((BlockFenceGate)blockToRender, x, y, z);

			default: return false;
			}
		}
		else if(world.getTileEntity(x, y, z) instanceof TileEntityMagicalTileLock)
		{
			renderer.setRenderBounds(0,0,0, .25,1,.25);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0,0,.75, .25,1,1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.75,0,0, 1,1,.25);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.75,0,.75, 1,1,1);
			renderer.renderStandardBlock(block, x, y, z);


			renderer.setRenderBounds(.25,0,0, .75,.25,.25);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0,0,.25, .25,.25,.75);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.25,0,.75, .75,.25,1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.75,0,.25, 1,.25,.75);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setRenderBounds(.25,.75,0, .75,1,.25);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0,.75,.25, .25,1,.75);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.25,.75,.75, .75,1,1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(.75,.75,.25, 1,1,.75);
			renderer.renderStandardBlock(block, x, y, z);


			renderer.setRenderBounds(.0625,.0625,.0625, .9375,.9375,.9375);
			return renderer.renderStandardBlock(block, x, y, z);

			//			renderer.setRenderBounds(.0625,.25,.0625,.9375,.75,.9375);
			//			return renderer.renderStandardBlock(block, x, y, z);

		}
		else if(world.getTileEntity(x, y, z) instanceof TileEntitySarcophagus)
		{
			TileEntitySarcophagus tile = (TileEntitySarcophagus)world.getTileEntity(x, y, z);
			switch(tile.facing)
			{
			case 2:
			case 3:
				renderer.setRenderBounds(tile.dummyLeft?.0625:0, 0,.0625,tile.dummyRight?.9375:1,.125,.9375);
				renderer.renderStandardBlock(block, x, y, z);
				if(tile.dummyLeft)
				{
					renderer.setRenderBounds( 0, 0,.0625,.0625,.75,.9375);
					renderer.renderStandardBlock(block, x, y, z);
				}
				else if(tile.dummyRight)
				{
					renderer.setRenderBounds(.9375, 0,.0625, 1,.75,.9375);
					renderer.renderStandardBlock(block, x, y, z);
				}
				renderer.setRenderBounds( 0, 0, 0, 1,.75,.0625);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 0, 0,.9375, 1,.75, 1);
				renderer.renderStandardBlock(block, x, y, z);
				if(!tile.open)
					renderer.setRenderBounds(tile.dummyLeft?.0625:0,.75,.0625, tile.dummyRight?.9375:1,1,.9375);
				else
				{
					if(tile.facing==2)
						renderer.setRenderBounds(tile.dummyLeft?.0625:0,0,1, tile.dummyRight?.9375:1,.875,1.25);
					else
						renderer.setRenderBounds(tile.dummyLeft?.0625:0,0,-.25, tile.dummyRight?.9375:1,.875,0);
				}
				break;
			case 4:
			case 5:
				renderer.setRenderBounds(.0625, 0,tile.dummyLeft?.0625:0,.9375,.125,tile.dummyRight?.9375:1);
				renderer.renderStandardBlock(block, x, y, z);
				if(tile.dummyLeft)
				{
					renderer.setRenderBounds(.0625, 0, 0,.9375,.75,.0625);
					renderer.renderStandardBlock(block, x, y, z);
				}
				else if(tile.dummyRight)
				{
					renderer.setRenderBounds(.0625, 0,.9375,.9375,.75, 1);
					renderer.renderStandardBlock(block, x, y, z);
				}
				renderer.setRenderBounds( 0, 0, 0,.0625,.75, 1);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(.9375, 0, 0, 1,.75, 1);
				renderer.renderStandardBlock(block, x, y, z);
				if(!tile.open)
					renderer.setRenderBounds(.0625,.75,tile.dummyLeft?.0625:0, .9375,1,tile.dummyRight?.9375:1);
				else
				{
					if(tile.facing==4)
						renderer.setRenderBounds(1,0,tile.dummyLeft?.0625:0, 1.25,.875,tile.dummyRight?.9375:1);
					else
						renderer.setRenderBounds(-.25,0,tile.dummyLeft?.0625:0, 0,.875,tile.dummyRight?.9375:1);
				}
				break;
			}
			return renderer.renderStandardBlock(block, x, y, z);
		}
		else 
			if(world.getBlockMetadata(x, y, z) == 8)
			{
				//			if(true)
				//			return false;
				byte pos = ((TileEntityBlastfurnace)world.getTileEntity(x, y, z)).position;
				//ForgeDirection facing = ((TileEntityBlastfurnace)world.getTileEntity(x, y, z)).facing;
				//System.out.println(pos);
				if(pos==22)
					renderer.setRenderBounds(0,0,0, 1,.875,1);
				else if(pos>=18)
				{
					pos-=18;
					renderer.setRenderBounds(0,0,0, 1,.5,1);
					renderer.renderStandardBlock(block, x, y, z);
					renderer.setRenderBounds(pos%3==0?.5:0,.5,pos<3?.5:0, (pos+1)%3==0?.5:1,1,pos>5?.5:1);
				}
				else
					renderer.setRenderBounds(0,0,0, 1,1,1);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else
			{
				block.setBlockBoundsBasedOnState(world, x, y, z);
				renderer.setRenderBoundsFromBlock(block);
				return renderer.renderStandardBlock(block, x, y, z);
			}
		return false;
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