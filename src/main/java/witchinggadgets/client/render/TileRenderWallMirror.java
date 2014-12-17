package witchinggadgets.client.render;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityWallMirror;

public class TileRenderWallMirror extends TileEntitySpecialRenderer
{
	static int[] n = {-1,-1,-1};
	static int[] g01 = {243,184,56};
	static int[] g02 = { 99, 69, 6};
	static int[] g03 = {220,155,14};
	static int[] g04 = {156,109, 9};
	static int[] g05 = {132, 92, 8};
	static int[] g06 = {241,170,14};
	static int[] g07 = {179,126,11};
	static int[] g08 = {241,176,36};
	static int[] g09 = {135, 94, 7};
	static int[] g10 = {203,143,13};
	static int[] g11 = {190,134,12};
	static int[] g12 = {242,181,48};
	static int[] w ={208,224,248};
	public static int[][][] shape = {
		/* 1*/{  n,  n,  n,  n,  n,g11,g11,g10},
		/* 2*/{  n,g04,g09,  n,g07,g01,g10,  n},
		/* 3*/{g10,  w,  w,g05,g07,  n,  n,  n},
		/* 4*/{g06,  w,g01,g03,g01,  n,  n,  n},
		/* 5*/{  n,g04,g04,g11,  n,  n,  n,  n},
		/* 6*/{  n,  n,g05,g11,  n,  n,  n,  n},
		/* 7*/{  n,  n,g11,g04,  n,  n,  n,  n},
		/* 8*/{  n,  n,g04,g11,  n,  n,  n,  n},
		/* 9*/{  n,  n,g03,g04,  n,  n,  n,  n},
		/*10*/{  n,  n,g03,g01,  n,  n,  n,  n},
		/*11*/{  n,  n,g04,g03,  n,  n,  n,  n},
		/*12*/{  n,g03,g04,  n,  n,  n,  n,  n},
		/*13*/{  n,g03,g10,  n,  n,  n,  n,  n},
		/*14*/{  n,g01,g10,  n,  n,  n,  n,  n},
		/*15*/{  n,g08,g12,  n,  n,  n,  n,  n},
		/*16*/{g08,g08,g12,  n,  n,  n,  n,  n}
	};

	public void renderTileEntityAt(TileEntityWallMirror tile, double x, double y, double z, float f)
	{
		if(tile.isDummy)return;
		double animation = tile.animation;
		double activationAnimation = tile.activationAnimation;
		boolean active = tile.isActive;
		boolean activating = tile.temp_isActivating;
		boolean deactivating = tile.temp_isDeActivating;

		GL11.glPushMatrix();

		Tessellator tes = Tessellator.instance;

		//GL11.glEnable(3042);
		//GL11.glBlendFunc(770, 771);

		GL11.glTranslatef((float)x, (float)y, (float)z);

		switch(tile.facing)
		{
		case 2:
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-1, 0, 0);
			break;
		case 3:
			GL11.glRotatef(270, 0, 1, 0);
			GL11.glTranslatef(0, 0, -1);
			break;
		case 4:
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslatef(-1f, 0, -1);
			break;
		}

		double glassUmin = 0.0;
		double glassUmax = 1.0/32.0;
		double glassVmin = 0.0;
		double glassVmax = 0.5;
		double darknessVmin= 0.5;
		double darknessVmax= 1.0;
		if(active && !deactivating)
		{
			glassUmin = animation*(1.0/32.0);
			glassUmax = (animation+1)*(1.0/32.0);
		}
		if(activating || deactivating)
		{
			glassUmin = activationAnimation*(1.0/32.0);
			glassUmax = (activationAnimation+1)*(1.0/32.0);
		}

		ClientUtilities.bindTexture("witchinggadgets:textures/models/glass.png");
		//		tes.startDrawingQuads();
		//		tes.addVertexWithUV(0.03, 0, 0, glassUmax, darknessVmax);
		//		tes.addVertexWithUV(0.03, 2, 0, glassUmax, darknessVmin);
		//		tes.addVertexWithUV(0.03, 2, 1, glassUmin, darknessVmin);
		//		tes.addVertexWithUV(0.03, 0, 1, glassUmin, darknessVmax);
		//		tes.draw();
		if(activating || deactivating || !active)
		{
			tes.startDrawingQuads();
			tes.addVertexWithUV(0.0005, 0, 0, glassUmax, glassVmax);
			tes.addVertexWithUV(0.0005, 2, 0, glassUmax, glassVmin);
			tes.addVertexWithUV(0.0005, 2, 1, glassUmin, glassVmin);
			tes.addVertexWithUV(0.0005, 0, 1, glassUmin, glassVmax);
			tes.draw();
		}

		//this.bindTexture(new ResourceLocation(Block.blockGold.getItemIconName()));
		ClientUtilities.bindTexture("witchinggadgets:textures/blocks/white.png");
		for(int i=0;i<shape.length;i++)
		{
			for(int j=0;j<shape[i].length;j++)
			{
				if(shape[i][j][0] != -1)
				{
					double r = shape[i][j][0] / 256.0;
					double g = shape[i][j][1] / 256.0;
					double b = shape[i][j][2] / 256.0;
					GL11.glColor3d(r, g, b);
					ClientUtilities.renderPixelBlock(tes, 0, i, j, 0.0625, 0, 0, 1, 1);
					ClientUtilities.renderPixelBlock(tes, 0, i, 15-j, 0.0625, 0, 0, 1, 1);

					ClientUtilities.renderPixelBlock(tes, 0, 15-i+16, j, 0.0625, 0, 0, 1, 1);
					ClientUtilities.renderPixelBlock(tes, 0, 15-i+16, 15-j, 0.0625, 0, 0, 1, 1);
					GL11.glColor3d(1, 1, 1);
				}
			}
		}

		List<EntityPlayer> players = tile.getMirroredPlayers();
		for(EntityPlayer pp : players)
		{

			double distance = Math.sqrt((tile.xCoord-pp.posX)*(tile.xCoord-pp.posX)+(tile.zCoord-pp.posZ)*(tile.zCoord-pp.posZ));
			//			System.out.println(distance);
			float distanceScaling = .8125f * (float)(9-distance)/9f;
			float hOffset =(float)( tile.facing==2||tile.facing==3?(pp.posX-(tile.xCoord+.5)):(pp.posZ-(tile.zCoord+.5)));
			hOffset /= 4;
			float vOffset = (float) (pp.posY-pp.yOffset -tile.yCoord);

			drawPlayer(pp, distanceScaling, tile.facing, hOffset,vOffset);
		}

		GL11.glPopMatrix();
	}



	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		renderTileEntityAt((TileEntityWallMirror)tileentity, d0, d1, d2, f);
	}

	private void drawPlayer(EntityLivingBase player, float scale, int facing, float hOffset, float vOffset)
	{
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.01875f, 0.875f, 0.5f);
		int cuttingAngle = facing==3||facing==4?(hOffset>0?1:-1): (hOffset>0?-1:1);
		GL11.glRotatef(cuttingAngle*2.875f, 0,1,0);
		GL11.glScalef((-0.0048f), 1, 1);
		GL11.glScalef(1,scale,scale);
		GL11.glTranslatef(0.5f,0,0);
		//+vOffset
		switch(facing)
		{
		case 2:
			GL11.glRotatef(90, 0,-1, 0);
			break;
		case 3:
			GL11.glRotatef(270, 0,-1, 0);
			break;
		case 4:
			GL11.glRotatef(180, 0,-1, 0);
			break;
		}
		if(!player.equals(RenderManager.instance.livingPlayer))
			GL11.glTranslatef(0.0F,-.75f, 0.0F);
		GL11.glTranslatef(0.0F, player.yOffset/2, 0.0F);
		GL11.glTranslatef(facing==2||facing==3?hOffset:0, vOffset, facing==2||facing==3?0:hOffset);

		ItemStack cc = player.getHeldItem();
		player.setCurrentItemOrArmor(0, null);
		

//		GL11.glDepthMask(false);
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glDepthFunc(0);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);

//		GL11.glDepthMask(true);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		player.setCurrentItemOrArmor(0, cc);

		GL11.glPopMatrix();
		//		RenderHelper.disableStandardItemLighting();
		//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		//		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		//		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

}
