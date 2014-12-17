package witchinggadgets.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class TileRenderMPVisRelay extends TileEntitySpecialRenderer
{
	float[][] faces= new float[][]{{1,0},{.5f,.25f},{.5f,.75f},{1,1}};

	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		Tessellator tes = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)x, (float)y, (float)z);
		double distx = tile.xCoord-RenderManager.instance.viewerPosX;
		double disty = tile.yCoord-RenderManager.instance.viewerPosY;
		double distz = tile.zCoord-RenderManager.instance.viewerPosZ;
		double distxz =  Math.sqrt( distx*distx + distz*distz );
		double dist = Math.sqrt( distxz*distxz + disty*disty );
		float line = (float) (dist>=32?1: 6-dist/32*6);
		GL11.glLineWidth(line);

		int step = 400;
		float third = 1/3f;
		float sixth = third*2;
		GL11.glTranslatef(.5f,.5f,.5f);		for(int i=0;i<faces.length;i++)		{			float curAction	= Minecraft.getMinecraft().thePlayer.ticksExisted%step/(step/5f) +i;			//				curAction=i;			if(curAction>4)				curAction-=4;			faces[i][0] = curAction<1?1-curAction*sixth: curAction<2?third: curAction<3?third+(curAction-2)*sixth: 1f;			faces[i][1] = curAction<3?third*curAction: 1-(curAction-3);
			float r = faces[i][0]/2;			float h = faces[i][1];			tes.startDrawing(2);			tes.addVertex(-r, h,-r);			tes.addVertex( r, h,-r);			tes.addVertex( r, h, r);			tes.addVertex(-r, h, r);			tes.draw();
			int n = i-1;
			if(n<0)				n=faces.length-1;
			tes.startDrawing(2);			tes.addVertex(-faces[i][0]/2, faces[i][1], faces[i][0]/2);			tes.addVertex( faces[i][0]/2, faces[i][1], faces[i][0]/2);			tes.addVertex( faces[n][0]/2, faces[n][1], faces[n][0]/2);			tes.addVertex(-faces[n][0]/2, faces[n][1], faces[n][0]/2);			tes.draw();			tes.startDrawing(2);			tes.addVertex(-faces[i][0]/2, faces[i][1],-faces[i][0]/2);			tes.addVertex( faces[i][0]/2, faces[i][1],-faces[i][0]/2);			tes.addVertex( faces[n][0]/2, faces[n][1],-faces[n][0]/2);			tes.addVertex(-faces[n][0]/2, faces[n][1],-faces[n][0]/2);			tes.draw();
		}

//		GL11.glEnable(GL11.GL_CULL_FACE);

		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}

}