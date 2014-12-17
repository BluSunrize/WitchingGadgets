package witchinggadgets.client.render;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformFocus;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderMetalDevice implements ISimpleBlockRenderingHandler
{

	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		GL11.glPushMatrix();
		try{
			if(metadata==0)
			{
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderer.setRenderBounds(.4375,0,0, .5625,1,1);
				ClientUtilities.drawStandardBlock(block, metadata, renderer);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			}
		}catch(Exception e)
		{
			//			e.printStackTrace();
			Tessellator.instance.draw();
		}
		try{
			if(metadata==1)
			{
				GL11.glTranslatef(-.5f, -.5F, -.5f);
				TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntityTerraformer(), 0.0D, 0.0D, 0.0D, 0.0F);
			}
			else if(metadata>=2&&metadata<=6)
			{
				GL11.glTranslatef(-.5f, -.5F, -.5f);
				TileEntityTerraformFocus tetf = new TileEntityTerraformFocus();
				tetf.blockType = block;
				tetf.blockMetadata = metadata;
				TileEntityRendererDispatcher.instance.renderTileEntityAt(tetf, 0.0D, 0.0D, 0.0D, 0.0F);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		GL11.glEnable(32826);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0)
		{
			try{
				return renderPane(world, block, x, y, z, renderer);
			}catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
//		else if(meta!=1)
//		{
//			renderer.setRenderBounds(0,0,0, 1,1,1);
//			return renderer.renderStandardBlock(block, x, y, z);
//		}
		return false;
		//		return true;
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

	public static boolean renderPane(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer)
	{
		int l = renderer.blockAccess.getHeight();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
		int i1 = block.colorMultiplier(renderer.blockAccess, x, y, z);
		float f = (float)(i1 >> 16 & 255) / 255.0F;
		float f1 = (float)(i1 >> 8 & 255) / 255.0F;
		float f2 = (float)(i1 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable)
		{
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		IIcon iicon;
		IIcon iicon1;

		if (renderer.hasOverrideBlockTexture())
		{
			iicon = renderer.overrideBlockTexture;
			iicon1 = renderer.overrideBlockTexture;
		}
		else
		{
			int j1 = renderer.blockAccess.getBlockMetadata(x, y, z);
			iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, j1);
			iicon1 = block.getIcon(world, x, y, z, 0);
		}

		double d21 = (double)iicon.getMinU();
		double d0 = (double)iicon.getInterpolatedU(8.0D);
		double d1 = (double)iicon.getMaxU();
		double d2 = (double)iicon.getMinV();
		double d3 = (double)iicon.getMaxV();
		double d4 = (double)iicon1.getInterpolatedU(7.0D);
		double d5 = (double)iicon1.getInterpolatedU(9.0D);
		double d6 = (double)iicon1.getMinV();
		double d7 = (double)iicon1.getInterpolatedV(8.0D);
		double d8 = (double)iicon1.getMaxV();
		double d9 = (double)x;
		double d10 = (double)x + 0.5D;
		double d11 = (double)(x + 1);
		double d12 = (double)z;
		double d13 = (double)z + 0.5D;
		double d14 = (double)(z + 1);
		double d15 = (double)x + 0.5D - 0.0625D;
		double d16 = (double)x + 0.5D + 0.0625D;
		double d17 = (double)z + 0.5D - 0.0625D;
		double d18 = (double)z + 0.5D + 0.0625D;
		boolean flag  = canPaneConnectTo(block, renderer.blockAccess, x, y, z - 1, NORTH);
		boolean flag1 = canPaneConnectTo(block, renderer.blockAccess, x, y, z + 1, SOUTH);
		boolean flag2 = canPaneConnectTo(block, renderer.blockAccess, x - 1, y, z, WEST );
		boolean flag3 = canPaneConnectTo(block, renderer.blockAccess, x + 1, y, z, EAST );
		boolean flag4 = block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1);
		boolean flag5 = block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0);

		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1))
		{
			if (flag2 && !flag3)
			{
				tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d21, d2);
				tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d21, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d21, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d21, d3);
				tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d0, d3);
				tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d0, d2);

				if (!flag1 && !flag)
				{
					tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d4, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d4, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d5, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d5, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d4, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d4, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d5, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d5, d6);
				}

				if (flag4 || y < l - 1 && renderer.blockAccess.isAirBlock(x - 1, y + 1, z))
				{
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
				}

				if (flag5 || y > 1 && renderer.blockAccess.isAirBlock(x - 1, y - 1, z))
				{
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
				}
			}
			else if (!flag2 && flag3)
			{
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
				tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d1, d3);
				tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d1, d2);
				tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d0, d2);
				tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d0, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d1, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d1, d2);

				if (!flag1 && !flag)
				{
					tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d4, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d4, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d5, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d5, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d4, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d4, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d5, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d5, d6);
				}

				if (flag4 || y < l - 1 && renderer.blockAccess.isAirBlock(x + 1, y + 1, z))
				{
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d6);
				}

				if (flag5 || y > 1 && renderer.blockAccess.isAirBlock(x + 1, y - 1, z))
				{
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d6);
				}
			}
		}
		else
		{
			tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d21, d2);
			tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d21, d3);
			tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d1, d3);
			tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d1, d2);
			tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d21, d2);
			tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d21, d3);
			tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d1, d3);
			tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d1, d2);

			if (flag4)
			{
				tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d8);
				tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d8);
			}
			else
			{
				if (y < l - 1 && renderer.blockAccess.isAirBlock(x - 1, y + 1, z))
				{
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
				}

				if (y < l - 1 && renderer.blockAccess.isAirBlock(x + 1, y + 1, z))
				{
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d6);
				}
			}

			if (flag5)
			{
				tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d8);
				tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d8);
			}
			else
			{
				if (y > 1 && renderer.blockAccess.isAirBlock(x - 1, y - 1, z))
				{
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
				}

				if (y > 1 && renderer.blockAccess.isAirBlock(x + 1, y - 1, z))
				{
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d6);
				}
			}
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1))
		{
			if (flag && !flag1)
			{
				tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d21, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d21, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d21, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d21, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d0, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d0, d2);

				if (!flag3 && !flag2)
				{
					tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d4, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d4, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d5, d6);
					tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d4, d6);
					tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d4, d8);
					tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d5, d8);
					tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d5, d6);
				}

				if (flag4 || y < l - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z - 1))
				{
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d6);
				}

				if (flag5 || y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z - 1))
				{
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d6);
				}
			}
			else if (!flag && flag1)
			{
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d1, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d1, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d0, d2);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d0, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d1, d3);
				tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d1, d2);

				if (!flag3 && !flag2)
				{
					tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d4, d6);
					tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d4, d8);
					tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d5, d8);
					tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d5, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d4, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d4, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d5, d6);
				}

				if (flag4 || y < l - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z + 1))
				{
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d7);
				}

				if (flag5 || y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z + 1))
				{
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d7);
				}
			}
		}
		else
		{
			tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d21, d2);
			tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d21, d3);
			tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d1, d3);
			tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d1, d2);
			tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d21, d2);
			tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d21, d3);
			tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d1, d3);
			tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d1, d2);

			if (flag4)
			{
				tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d8);
				tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d5, d6);
				tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d4, d6);
				tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d8);
				tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d5, d8);
				tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d6);
				tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d6);
				tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d4, d8);
			}
			else
			{
				if (y < l - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z - 1))
				{
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d6);
				}

				if (y < l - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z + 1))
				{
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d7);
				}
			}

			if (flag5)
			{
				tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d8);
				tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d5, d6);
				tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d4, d6);
				tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d8);
				tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d5, d8);
				tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d6);
				tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d6);
				tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d4, d8);
			}
			else
			{
				if (y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z - 1))
				{
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d6);
				}

				if (y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z + 1))
				{
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d7);
				}
			}
		}
		return true;
	}
	static boolean canPaneConnectToBlock(Block block, Block target)
	{
		return target.func_149730_j() || target == block || target == Blocks.glass || target == Blocks.stained_glass || target == Blocks.stained_glass_pane || target instanceof BlockPane;
	}
	static boolean canPaneConnectTo(Block block, IBlockAccess world, int x, int y, int z, ForgeDirection dir)
	{
		return canPaneConnectToBlock(block, world.getBlock(x, y, z)) || world.isSideSolid(x, y, z, dir.getOpposite(), false);
	}

}