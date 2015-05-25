package witchinggadgets.client.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.client.ClientProxy;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;

public class ItemRenderPrimordialGauntlet implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type)
	{
		//		if(type==ItemRenderType.INVENTORY)
		//			return false;
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
	{
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tessellator = Tessellator.instance;
		int ticksExisted = mc.thePlayer.ticksExisted;
		String[] fingers = {"Thumb","Index","Middle","Ring","Pinky"};
		int[][] fingerOverlayColour = {
				{ 93, 25, 79},
				{103, 39, 90},
				{113, 52,100},
				{123, 67,111},
				{132, 80,122},
				{141, 94,132},
				{152,108,142},
				{161,122,153},
				{171,135,164},
				{161,122,153},
				{152,108,142},
				{141, 94,132},
				{132, 80,122},
				{123, 67,111},
				{113, 52,100},
				{103, 39, 90},
		};

		try{


			GL11.glPushMatrix();

			double scale;
			AbstractClientPlayer equippingPlayer=null;
			switch(type)
			{
			case INVENTORY:
				GL11.glRotated(170, 1, 0, 0);
				GL11.glRotated(10, 0, 1, 0);
				GL11.glRotated(-30, 0, 0, 1);
				GL11.glScaled(3,3.5,3);
				GL11.glTranslated(0.5,-0.6,-0.3);
				break;
			case ENTITY:
				GL11.glScaled(1.5,1.75,1.5);
				GL11.glTranslated(0,-0.4,0);
				break;
			case EQUIPPED:
				equippingPlayer = (AbstractClientPlayer) data[1];

				scale = 2.68;

				ClientUtilities.bindTexture(equippingPlayer.getLocationSkin().getResourceDomain()+":"+equippingPlayer.getLocationSkin().getResourcePath());
				GL11.glRotated(-45, 0, 1, 0);
				GL11.glRotated(160, 0, 0, 1);
				//				GL11.glRotated(-2, 1, 0, 0);
				//double swayAngle = 4.0 + ( (ticksExisted%40.0 > 20 ? 1 : -1) * ( (ticksExisted%20.0 / 20)  * 2.0) );
				GL11.glScaled(scale,scale,scale);
				GL11.glTranslated(-0.434,-0.887, -0.0014);
				RenderHelper.enableStandardItemLighting();
				//				ClientProxy.gauntletModel.renderOnly("Arm_00");

				break;
			case EQUIPPED_FIRST_PERSON:
				equippingPlayer = (AbstractClientPlayer) data[1];

				scale = 8.0/4.0;
				if(equippingPlayer.isUsingItem())
				{
					GL11.glRotated(20, 0, 0, 1);
					GL11.glTranslated(.5,-.5,0);
				}

				ClientUtilities.bindTexture(equippingPlayer.getLocationSkin().getResourceDomain()+":"+equippingPlayer.getLocationSkin().getResourcePath());

				GL11.glRotated(280, 0, 0, 1);
				GL11.glRotated(-60, 1, 0, 0);
				GL11.glRotated(-75, 0, 1, 0);
				GL11.glScaled(scale,scale,-scale);
				GL11.glTranslated(-0.0,-0.85,-0.51);
				RenderHelper.enableStandardItemLighting();
				ClientProxy.gauntletModel.renderOnly("Arm_00");
				GL11.glScaled(-1,1,1);

				break;
			default:
				break;
			}

			float overlayScale = 1.00001f;
			GL11.glPushMatrix();
			ClientUtilities.bindTexture("witchinggadgets:textures/models/primordialBracelet.png");
			GL11.glColor3d(0.8,0.8,0.8);
			ClientProxy.gauntletModel.renderPart("Guard_Arm_04");
			ClientProxy.gauntletModel.renderPart("Guard_Hand_05");
			GL11.glColor3d(1,1,1);
			for(int f=0;f<5;f++)
			{
				float fingerAngle = 0;
				float xx = f<2?.09375f: f==2?.03125f: f==3?-.03125f: -.09375f;
				float zz = f==0?.0833167f :-.0913815f;
				float yy = .625f;
				GL11.glTranslated(xx,yy,zz);
				if(equippingPlayer!=null && equippingPlayer.isUsingItem())
				{
					fingerAngle =-80;
					GL11.glRotatef(f==0?30: f==1?-30: f==3?20: f==4?40: 0, 0, 1, 0);
					GL11.glRotatef(fingerAngle, f==0?-1:1, 0, 0);
				}

				ClientProxy.gauntletModel.renderPart("Finger_"+fingers[f]+"_f"+f);
				int colour = ticksExisted%32 / 2;
				GL11.glColor3d(fingerOverlayColour[colour][0]/255.0, fingerOverlayColour[colour][1]/255.0, fingerOverlayColour[colour][2]/255.0);
				GL11.glScalef(overlayScale,overlayScale,overlayScale);
				ClientProxy.gauntletModel.renderPart("Finger_"+fingers[f]+"_Overlay_f"+f+"_1");
				GL11.glScalef(1/overlayScale,1/overlayScale,1/overlayScale);
				GL11.glColor3d(1,1,1);

				if(equippingPlayer!=null && equippingPlayer.isUsingItem())
				{
					GL11.glRotatef(-fingerAngle, f==0?-1:1, 0, 0);
					GL11.glRotatef(f==0?30: f==1?-30: f==3?20: f==4?40: 0, 0,-1, 0);
				}

				GL11.glTranslated(-xx,-yy,-zz);
				//				GL11.glTranslated(xx,yy,zz);
				//				GL11.glTranslated(0,-.5*Math.abs(fingerOffset),-fingerOffset);
			}

			float runeColour = ticksExisted%32 + 1;
			if(runeColour>15)
				runeColour = 16 - (runeColour-16);
			GL11.glScalef(overlayScale,overlayScale,overlayScale);
			GL11.glColor4f(runeColour*0.2f,runeColour*0.2f,runeColour*0.2f,1);
			ClientUtilities.bindTexture("witchinggadgets:textures/gui/bracelet.png");
			ClientProxy.gauntletModel.renderPart("Runes_06");
			GL11.glColor4f(1,1,1,1);
			GL11.glScalef(1/overlayScale,1/overlayScale,1/overlayScale);

			ClientUtilities.bindTexture("witchinggadgets:textures/models/white.png");
			ItemStack[] gems = ItemPrimordialGlove.getSetGems(stack);//{ItemInfusedGem.createGem(Aspect.FIRE,GemCut.OVAL,true),ItemInfusedGem.createGem(Aspect.WATER,GemCut.OVAL,true),ItemInfusedGem.createGem(Aspect.EARTH,GemCut.OVAL,true),ItemInfusedGem.createGem(Aspect.AIR,GemCut.OVAL,true),ItemInfusedGem.createGem(Aspect.ORDER,GemCut.OVAL,true)};//ItemPrimordialGlove.getSetGems(stack);
			if(gems!=null)
				for(int g=0;g<gems.length;g++)
					if(gems[g]!= null)
					{
						Color col = new Color(gems[g].getItem().getColorFromItemStack(gems[g], 0));
						if(gems[g].getItemDamage()==0)
							GL11.glColor3d(col.getRed()/255.0, col.getGreen()/255.0, col.getBlue()/255.0);
						else
							GL11.glColor3d(col.getRed()/511.0, col.getGreen()/511.0, col.getBlue()/511.0);
						ClientProxy.gauntletModel.renderPart("Gem0"+g+"_oval0"+g);
						GL11.glColor3d(1,1,1);
					}

			GL11.glPopMatrix();
			if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
				GL11.glTranslated(0.1,0,0);
			GL11.glTranslated(0,0.675,0);

			GL11.glDisable(GL11.GL_LIGHTING);
			UtilsFX.bindTexture("textures/misc/nodes.png");
			if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				int sel = stack.hasTagCompound()?stack.getTagCompound().getInteger("selected"):-1;
				if(sel>=0&&sel<gems.length&&gems[sel]!=null)
				{
					GL11.glPushMatrix();
					GL11.glRotated(-90, 1, 0, 0);
					GL11.glRotated(-90, 0, 1, 0);
					GL11.glTranslated(-.1875,.147,0.0);
					GL11.glEnable(3042);

					GL11.glTranslated(sel<2?.09375: sel==3?.0125: .035,0, sel==0||sel==2?.05625:sel==1||sel==4?.14: .1);
					GL11.glRotated(45, 1, 0, 0);
					int perm = (int) ((System.currentTimeMillis()/64)%32);
					double uMin = perm * .03125;
					double uMax = (perm+1) * .03125;
					double vMin = gems[sel].getItemDamage()==0?.0 : .09375;
					double vMax = gems[sel].getItemDamage()==0?.03125 : .125;

					tessellator.startDrawingQuads();
					tessellator.setColorOpaque_I(gems[sel].getItem().getColorFromItemStack(gems[sel], 0));
					tessellator.addVertexWithUV(-.025f, .025f, 0, uMin, vMax);
					tessellator.addVertexWithUV( .025f, .025f, 0, uMax, vMax);
					tessellator.addVertexWithUV( .025f,-.025f, 0, uMax, vMin);
					tessellator.addVertexWithUV(-.025f,-.025f, 0, uMin, vMin);
					tessellator.draw();
					
					vMin = gems[sel].getItemDamage()==0?.125 : .09375;
					vMax = gems[sel].getItemDamage()==0?.15625 : .125;
					tessellator.startDrawingQuads();
					tessellator.setColorOpaque_I(gems[sel].getItem().getColorFromItemStack(gems[sel], 0));
					tessellator.addVertexWithUV(-.025f, .025f, 0, uMin, vMax);
					tessellator.addVertexWithUV( .025f, .025f, 0, uMax, vMax);
					tessellator.addVertexWithUV( .025f,-.025f, 0, uMax, vMin);
					tessellator.addVertexWithUV(-.025f,-.025f, 0, uMin, vMin);
					tessellator.draw();

					GL11.glPopMatrix();
				}
				GL11.glRotated(-90, 0, 1, 0);
				GL11.glRotated(-40, 1, 0, 0);
				GL11.glTranslated(0,-.01,0.05);
			}
			else if(type == ItemRenderType.EQUIPPED)
			{
				double pRot = ((AbstractClientPlayer) data[1]).renderYawOffset % 360;
				GL11.glRotated(90+pRot-RenderManager.instance.playerViewY%360.0, 0, 1, 0);
				GL11.glRotated(RenderManager.instance.playerViewX%360.0, 1, 0, 0);
			}
			else if(type == ItemRenderType.ENTITY)
			{
				EntityItem entItem = ((EntityItem)data[1]);
				double entRot = ((entItem.age + UtilsFX.getTimer(mc).renderPartialTicks) / 20.0F + entItem.hoverStart) * (180F / (float)Math.PI);
				GL11.glRotated(-entRot + RenderManager.instance.playerViewY%360.0, 0, 1, 0);
				GL11.glRotated(RenderManager.instance.playerViewX%360.0, 1, 0, 0);
			}

			AspectList nodeAspects = new AspectList();
			if(type != ItemRenderType.INVENTORY && stack.hasTagCompound() && stack.getTagCompound().hasKey("storedNode"))
			{

				NBTTagCompound nodeTag = stack.getTagCompound().getCompoundTag("storedNode");
				int nodeType = nodeTag.getInteger("type");
				NodeModifier nodeModifier = NodeModifier.values()[nodeTag.getInteger("modifier")];
				nodeAspects.readFromNBT(nodeTag);

				float alpha = 1f;
				float size = 1f;

				if (nodeModifier != null) {
					switch (nodeModifier.ordinal())
					{
					case 1: 
						alpha *= 1.5F; break;
					case 2: 
						alpha *= 0.66F; break;
					case 3: 
						alpha *= (MathHelper.sin(ticksExisted / 3.0F) * 0.25F + 0.33F);
					}
				}

				float nodeScale = 0.0F;
				int count = 0;
				float bscale = 0.25F;
				float average = 0.0F;
				for (Aspect aspect : nodeAspects.getAspects())
				{
					if(aspect==null)
						break;
					if (aspect.getBlend() == 771) {
						alpha = (float)(alpha * 1.5D);
					}
					average += nodeAspects.getAmount(aspect);
					GL11.glPushMatrix();
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, aspect.getBlend());
					nodeScale = MathHelper.sin(ticksExisted / (14.0F - count)) * bscale + bscale * 2.0F;
					nodeScale = 0.2F + nodeScale * (nodeAspects.getAmount(aspect) / 50.0F);
					nodeScale *= size;

					float mod = 2f * (((System.currentTimeMillis()+count*512)%4096)/4096f);
					if(mod>1)
						mod = 2-mod;
					float radius = .075f + .05f *mod*(nodeAspects.getAmount(aspect)/average);
					int perm = (int) ((System.currentTimeMillis()/64)%32)+count*4;


					double uMin = perm * .03125;
					double uMax = (perm+1) * .03125;

					tessellator.startDrawingQuads();
					tessellator.setColorRGBA_I(aspect.getColor(), (int)(alpha * 255.0F));
					tessellator.addVertexWithUV(-radius, radius, -count*.001, uMin, .03125);
					tessellator.addVertexWithUV( radius, radius, -count*.001, uMax, .03125);
					tessellator.addVertexWithUV( radius,-radius, -count*.001, uMax, 0);
					tessellator.addVertexWithUV(-radius,-radius, -count*.001, uMin, 0);
					tessellator.draw();

					GL11.glDisable(3042);
					GL11.glPopMatrix();
					count++;
					if (aspect.getBlend() == 771) {
						alpha = (float)(alpha / 1.5D);
					}
				}
				average /= nodeAspects.size();
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				nodeScale = 0.1F + average / 150.0F;
				nodeScale *= size;

				if(nodeType!=0)
					GL11.glBlendFunc(770, nodeType==3||nodeType==4?771 : 1);

				int perm = (int) ((System.currentTimeMillis()/64)%32);
				int overl = nodeType==2?6: nodeType==3?2: nodeType==4?5: nodeType==5?4: nodeType==6?3: 1;
				double uMin = perm * .03125;
				double uMax = (perm+1) * .03125;

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(-0.0825, 0.0825, -count*.001, uMin, (overl+1)*.03125);
				tessellator.addVertexWithUV( 0.0825, 0.0825, -count*.001, uMax, (overl+1)*.03125);
				tessellator.addVertexWithUV( 0.0825,-0.0825, -count*.001, uMax, (overl+0)*.03125);
				tessellator.addVertexWithUV(-0.0825,-0.0825, -count*.001, uMin, (overl+0)*.03125);
				tessellator.draw();

				GL11.glDisable(3042);
				GL11.glPopMatrix();
			}

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}