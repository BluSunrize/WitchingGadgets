package witchinggadgets.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityMirrorPortal;

public class TileRenderMirrorPortal extends TileEntitySpecialRenderer
{
	RenderManager renderManager = RenderManager.instance;


	public void renderTileEntityAt(TileEntityMirrorPortal tileentity, double par2, double par4, double par6, float par8)
	{
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		ClientUtilities.bindTexture("witchinggadgets:textures/models/portal.png");
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		Tessellator tessellator = Tessellator.instance;

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		int animation = tileentity.animation;
		double partial = 1.0 / 32.0;

		double f3 = (animation-1)*partial;
		double f4 = animation*partial;
		double f5 = 0.0f;
		double f6 = 1.0f;
		double f7 = 1.0F;
		double f8 = 0.5F;
		double f9 = 0.25F;

		float scaleFloor = 1.25f;
		GL11.glTranslatef(0.5f, 0.1f, 0.75f);
		GL11.glRotatef(90.0F,-1.0F, 0.0F, 0.0F);
		GL11.glScalef(scaleFloor,scaleFloor,scaleFloor);
		draw(tessellator,f3,f4,f5,f6,f7,f8,f9);
		GL11.glScalef(1/scaleFloor,1/scaleFloor,1/scaleFloor);
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);


		GL11.glTranslatef(0.0f, 0.4f, -0.25f);
		GL11.glScalef(1.5f, 3f, 1.5f);
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		//		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
		tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
		tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
		tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
		tessellator.draw();
		GL11.glScalef(1/1.5f,1/3f,1/1.5f);


		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, -1.0F, 0.0F);

		//		System.out.println(name);
		FontRenderer fontrenderer = this.renderManager.getFontRenderer();
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GL11.glPushMatrix();
		GL11.glTranslatef(0,2.6f,0);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int j = Math.max(fontrenderer.getStringWidth(tileentity.name),fontrenderer.getStringWidth(tileentity.linkedDimensionName)) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex(-j-1, 0-((tileentity.name!=null&&!tileentity.name.isEmpty())?8:0), 0.0D);
		tessellator.addVertex(-j-1, 9, 0.0D);
		tessellator.addVertex( j+1, 9, 0.0D);
		tessellator.addVertex( j+1, 0-((tileentity.name!=null&&!tileentity.name.isEmpty())?8:0), 0.0D);
		tessellator.draw();

		String ss = tileentity.linkedDimensionName;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontrenderer.drawString(ss, -fontrenderer.getStringWidth(ss)/2, 1, 553648127);
		if(tileentity.name!=null && !tileentity.name.isEmpty())
			fontrenderer.drawString(tileentity.name, -fontrenderer.getStringWidth(tileentity.name)/2,-8, 553648127);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(ss, -fontrenderer.getStringWidth(ss) / 2, 1, -1);
		if(tileentity.name!=null && !tileentity.name.isEmpty())
			fontrenderer.drawString(tileentity.name, -fontrenderer.getStringWidth(tileentity.name)/2,-7, -1);


		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();


		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	private void draw(Tessellator tessellator, double f3, double f4, double f5, double f6, double f7, double f8, double f9)
	{
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
		tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
		tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
		tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
		tessellator.draw();	

	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,	double d2, float f) {
		renderTileEntityAt((TileEntityMirrorPortal)tileentity, d0, d1, d2, f);
	}

}
