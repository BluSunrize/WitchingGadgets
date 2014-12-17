package witchinggadgets.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

import org.lwjgl.opengl.GL11;

import witchinggadgets.common.WGConfig;

public class ModelCloak extends ModelBiped
{
	private double[] circPos = new double[32]; // Circle Position

	public boolean doAnimation = true;
	int colour;
	
	public ModelCloak(int colour)
	{
		this.colour = colour;
		circPos[0] = 0.5;
		circPos[1] = 0.49039;
		circPos[2] = 0.46194;
		circPos[3] = 0.41573;
		circPos[4] = 0.35355;
		circPos[5] = 0.27779;
		circPos[6] = 0.19134;
		circPos[7] = 0.09755;
		circPos[8] = 0.0;
		circPos[9] = -0.09755;
		circPos[10] = -0.19134;
		circPos[11] = -0.27779;
		circPos[12] = -0.35355;
		circPos[13] = -0.41573;
		circPos[14] = -0.46194;
		circPos[15] = -0.49039;
		circPos[16] = -0.5;
		circPos[17] = -0.49039;
		circPos[18] = -0.46194;
		circPos[19] = -0.41573;
		circPos[20] = -0.35355;
		circPos[21] = -0.27779;
		circPos[22] = -0.19134;
		circPos[23] = -0.09755;
		circPos[24] = 0.0;
		circPos[25] = 0.09755;
		circPos[26] = 0.19134;
		circPos[27] = 0.27779;
		circPos[28] = 0.35355;
		circPos[29] = 0.41573;
		circPos[30] = 0.46194;
		circPos[31] = 0.49039;
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedBody.showModel = false;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;
		super.render(entity, par2, par3, par4, par5, par6, par7);
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		boolean drawHood = false;

		Tessellator tessellator = Tessellator.instance;

		if(doAnimation && WGConfig.cloakAnimationMode == 1)
		{
			if(bipedLeftLeg.rotateAngleX * (180F / (float)Math.PI) > 0)
				GL11.glRotatef(bipedLeftLeg.rotateAngleX * (180F / (float)Math.PI) * 0.15f, 1F, 0.0F, 0.0F);
			if(bipedRightLeg.rotateAngleX * (180F / (float)Math.PI) > 0)
				GL11.glRotatef(bipedRightLeg.rotateAngleX * (180F / (float)Math.PI) * 0.15f, 1F, 0.0F, 0.0F);
		}

		GL11.glTranslatef(0, 1.45f, 0);
		GL11.glScalef(1, -1, 1);
		GL11.glColor3f((colour>>16&255)/255.0f, (colour>>8&255)/255.0f, (colour&255)/255.0f);

		double d0_1 = circPos[0]*1;
		double d1_1 = circPos[1]*1;
		double d2_1 = circPos[24]*1;
		double d3_1 = circPos[25]*1;

		if(doAnimation && entity instanceof EntityLivingBase)
			if(((EntityLivingBase)entity).getActivePotionEffect(Potion.invisibility)!=null)
				GL11.glColor4d(1, 1, 1, 0.1001);
		
		
		for(int i=0;i<15;i++)
		{
			int it0 = i;
			int it1 = it0+1;
			if(it1 > 31)it1-=31;
			int it2 = i+24;
			if(it2 > 31)it2-=31;
			int it3 = it2+1;
			if(it3 > 31)it3-=31;


			for(int j=0; j < 8;j++)
			{
				int jt0 = j;
				int jt1 = jt0+1;
				double h0 = (circPos[jt0]*circPos[jt0])*7;
				double h1 = (circPos[jt1]*circPos[jt1])*7;
				double dividerA[] = {0.3,0.725,0.75,0.8,0.825,0.9,1.0,1.1};
				//double dividerA[] = {1.1,1.0,0.9,0.825,0.8,0.75,0.725,0.3};
				double divider = dividerA[j];

				double d0 = circPos[it0]*1.5*divider;
				double d1 = circPos[it1]*1.5*divider;
				double d2 = circPos[it2]*1.5*divider;
				double d3 = circPos[it3]*1.5*divider;


				double f3 = i*0.0625;//icon.getMinU();
				double f4 = (i+1)*0.0625;//icon.getMaxU();
				double f5 = j * 0.125;//1 - (j+1)*0.125;//icon.getMinV();
				double f6 = (j+1)*0.125;//1 - j*0.125;//icon.getMaxV();

				if(j==2)h0*=0.975;
				if(j==1)
				{
					h1*=0.975;
					h0*=0.9;
				}
				if(j==0)
				{
					d0 *=0.0;
					d0_1 *=0.0;
					d1 *=0.0;
					d1_1 *=0.0;
					d2 *=0.0;
					d2_1 *=0.0;
					d3 *=0.0;
					d3_1 *=0.0;
					h1*=0.9;
					h0*=0.9;
				}

				if(doAnimation && WGConfig.cloakAnimationMode == 2)
				{
					double offsettingAngle = Math.max(bipedLeftLeg.rotateAngleX * (180F / (float)Math.PI), bipedRightLeg.rotateAngleX * (180F / (float)Math.PI));

					if(offsettingAngle > 1)
					{
						double stretch = 0.3 *  (offsettingAngle / 90.0);
						stretch += 1.0;
						d2*=stretch;
						d3*=stretch;
					}
				}

				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.addVertexWithUV(d0_1, h0, d2_1, f3, f5);
				tessellator.addVertexWithUV(d0  , h1, d2  , f3, f6);
				tessellator.addVertexWithUV(d1  , h1, d3  , f4, f6);
				tessellator.addVertexWithUV(d1_1, h0, d3_1, f4, f5);
				tessellator.draw();

				d0_1 = d0;
				d1_1 = d1;
				d2_1 = d2;
				d3_1 = d3;
			}
		}


		if(drawHood)
		{
			GL11.glTranslated(0, 1.4, 0);

			if(bipedHead.rotateAngleZ * (180F / (float)Math.PI) != 0)
				GL11.glRotatef(bipedHead.rotateAngleZ * (180F / (float)Math.PI) , 0F, 1F, 0F);
			if(bipedHead.rotateAngleY * (180F / (float)Math.PI) != 0)
				GL11.glRotatef(bipedHead.rotateAngleY * (180F / (float)Math.PI) , 0F, 1F, 0F);
			if(bipedHeadwear.rotateAngleX * (180F / (float)Math.PI) != 0)
				GL11.glRotatef(bipedHeadwear.rotateAngleX * (180F / (float)Math.PI) * -1F, 1F, 0F, 0F);

			GL11.glTranslated(0, 0, 0.3);

			GL11.glScaled(1, 1, 1.6);

			for(int i=0;i<15;i++)
			{
				int it0 = i;
				int it1 = it0+1;
				if(it1 > 31)it1-=31;
				int it2 = i+24;
				if(it2 > 31)it2-=31;
				int it3 = it2+1;
				if(it3 > 31)it3-=31;

				int it0_1 = i+1;
				int it1_1 = it0_1+1;
				if(it1_1 > 31)it1_1-=31;
				int it2_1 = i+24;
				if(it2_1 > 31)it2_1-=31;
				int it3_1 = it2_1+1;
				if(it3_1 > 31)it3_1-=31;

				for(int j=0; j < 8;j++)
				{

					int jt0 = j;
					int jt1 = jt0+1;
					double h0 = (circPos[jt0]*circPos[jt0])*2.75;
					double h1 = (circPos[jt1]*circPos[jt1])*2.75;
					double dividerA[] = {0,0.65,0.675,0.7,0.725,0.775,0.825,0.9};
					double divider = dividerA[j];

					double d0 = circPos[it0]*0.9*divider;
					double d1 = circPos[it1]*0.9*divider;
					double d2 = circPos[it2]*0.9*divider;
					double d3 = circPos[it3]*0.9*divider;

					double f3 = i*0.0625;//icon.getMinU();
					double f4 = (i+1)*0.0625;//icon.getMaxU();
					double f5 = 0.5;//1 - (j+1)*0.125;//icon.getMinV();
					double f6 = 1;//1 - j*0.125;//icon.getMaxV();


					if(j==2)h0*=0.975;
					if(j==1)
					{
						h1*=0.975;
						h0*=0.9;
					}
					if(j==0)
					{
						d0 *=0.25;
						d0_1 *=0.25;
						d1 *=0.25;
						d1_1 *=0.25;
						d2 *=0.25;
						d2_1 *=0.25;
						d3 *=0.25;
						d3_1 *=0.25;
						h1*=0.9;
						h0*=0.9;
					}
					if(j==2||j==3||j==4)
					{
						d2 *= 1.25;
						d3 *= 1.25;
					}

					GL11.glColor4d(1, 1, 1, 1);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					tessellator.addVertexWithUV(d0  , h1, d2  , f3, f6);
					tessellator.addVertexWithUV(d0_1, h0, d2_1, f3, f6);
					tessellator.addVertexWithUV(d1_1, h0, d3_1, f4, f5);
					tessellator.addVertexWithUV(d1  , h1, d3  , f4, f5);
					tessellator.draw();

					d0_1 = d0;
					d1_1 = d1;
					d2_1 = d2;
					d3_1 = d3;
				}
			}
		}
		
		GL11.glColor3f(1,1,1);
		GL11.glDisable(3042);
		//GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
