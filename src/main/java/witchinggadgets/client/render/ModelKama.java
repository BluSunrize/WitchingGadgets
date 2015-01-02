package witchinggadgets.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class ModelKama extends ModelBiped
{
	int colour;
	static ResourceLocation texBelt = new ResourceLocation("witchinggadgets:textures/models/magicalBaubles.png");
	public ModelKama(int colour)
	{
		this.colour = colour;
		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedBody.showModel=false;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		super.render(entity, par2, par3, par4, par5, par6, par7);
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);

		Tessellator tessellator = Tessellator.instance;

		GL11.glTranslatef(0, 1.45f, 0);
		GL11.glScalef(1, -1, 1);
		GL11.glColor3f((colour>>16&255)/255.0f, (colour>>8&255)/255.0f, (colour&255)/255.0f);

		double dL = .250;
		double dR = .250;
		double h = .6875;
		double w = .01;
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(1,0,0);
		tessellator.addVertexWithUV( .43750, .125,-.125, .25, 1);
		tessellator.addVertexWithUV( .25000+w, h,-.125, .25, 0);
		tessellator.addVertexWithUV( .25000+w, h, .125+w, 0, 0);
		tessellator.addVertexWithUV( .43750, .125, dL, 0, 1);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0,0,1);
		tessellator.addVertexWithUV( .03125, .125, dL, .484375, 1);
		tessellator.addVertexWithUV( .03125, h, .125+w, .484375, 0);
		tessellator.addVertexWithUV( .25000+w, h, .125+w, .25, 0);
		tessellator.addVertexWithUV( .43750, .125, dL, .25, 1);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0,0,1);
		tessellator.addVertexWithUV(-.03125, .125, dL, .515625, 1);
		tessellator.addVertexWithUV(-.03125, h, .125+w, .515625, 0);
		tessellator.addVertexWithUV( .03125, h, .125+w, .484375, 0);
		tessellator.addVertexWithUV( .03125, .125, dR, .484375, 1);
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0,0,1);
		tessellator.addVertexWithUV(-.43750, .125, dR, .75, 1);
		tessellator.addVertexWithUV(-.25000-w, h, .125+w, .75, 0);
		tessellator.addVertexWithUV(-.03125, h, .125+w, .515625, 0);
		tessellator.addVertexWithUV(-.03125, .125, dR, .515625, 1);
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1,0,0);
		tessellator.addVertexWithUV(-.43750, .125, dR, 1, 1);
		tessellator.addVertexWithUV(-.25000-w, h, .125+w, 1, 0);
		tessellator.addVertexWithUV(-.25000-w, h,-.125, .75, 0);
		tessellator.addVertexWithUV(-.43750, .125,-.125, .75, 1);
		tessellator.draw();
		
		GL11.glColor3f(1,1,1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texBelt);
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1,0,0);
		tessellator.addVertexWithUV(-.25-w, h   , .125+w, .0, 1);
		tessellator.addVertexWithUV(-.25-w, .875, .125+w, .0, .875);
		tessellator.addVertexWithUV(-.25-w, .875,-.125-w, .0625, .875);
		tessellator.addVertexWithUV(-.25-w, h   ,-.125-w, .0625, 1);
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0,0,-1);
		tessellator.addVertexWithUV(-.25-w, h   ,-.125-w, .0625, 1);
		tessellator.addVertexWithUV(-.25-w, .875,-.125-w, .0625, .875);
		tessellator.addVertexWithUV( .25+w, .875,-.125-w, .1875, .875);
		tessellator.addVertexWithUV( .25+w, h   ,-.125-w, .1875, 1);
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(1,0,0);
		tessellator.addVertexWithUV(.25+w, h  ,-.125-w, .1875, 1);
		tessellator.addVertexWithUV(.25+w,.875,-.125-w, .1875, .875);
		tessellator.addVertexWithUV(.25+w,.875, .125+w, .25, .875);
		tessellator.addVertexWithUV(.25+w, h  , .125+w, .25, 1);
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0,0,-1);
		tessellator.addVertexWithUV( .25+w, h   , .125+w, .25, 1);
		tessellator.addVertexWithUV( .25+w, .875, .125+w, .25, .875);
		tessellator.addVertexWithUV(-.25-w, .875, .125+w, .375, .875);
		tessellator.addVertexWithUV(-.25-w, h   , .125+w, .375, 1);
		tessellator.draw();
		
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}
}
