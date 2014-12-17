package witchinggadgets.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGContent;


/**
 * My own collection of functions I need to access more than once when rendering stuff
 * 
 * @author BluSunrize
 */
public class ClientUtilities
{
	public static int colour_CloakBlue = 2041173;
	public static int colour_CloakNil = 14737632;
	public static int colour_CloakRaven = 3487288;
	public static int colour_CloakStorage = Aspect.VOID.getColor();
	public static int colour_BagDefault = 9073853;

	public static String[] nodeTypeChatColour = {"7","2","8","5","4","f"};
	public static String[] nodeModifierChatColour = {"f","7","8"};

	private static Minecraft mc()
	{
		return Minecraft.getMinecraft();
	}

	public static void renderPixelBlock(Tessellator tes, double x,double y,double z,double pixelLength,double uMin,double vMin,double uMax,double vMax)
	{
		double dXMin = x*pixelLength;
		double dXMax = (x+1)*pixelLength;
		double dYMin = y*pixelLength;
		double dYMax = (y+1)*pixelLength;
		double dZMin = z*pixelLength;
		double dZMax = (z+1)*pixelLength;
		//Side YNeg
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMax, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMin, vMax);
		tes.draw();
		//Side YPos
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMax, vMin);
		tes.draw();
		//Side ZNeg
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMax, vMin);
		tes.draw();
		//Side ZPos
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMin);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMin, vMax);
		tes.draw();
		//Side XNeg
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMax, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMax);
		tes.draw();
		//Side XPos
		tes.startDrawingQuads();
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMin);
		tes.draw();
	}

	public static void renderPixelBlockOnlyVertex(Tessellator tes, double x,double y,double z,double pixelLength,double uMin,double vMin,double uMax,double vMax)
	{
		double dXMin = x*pixelLength;
		double dXMax = (x+1)*pixelLength;
		double dYMin = y*pixelLength;
		double dYMax = (y+1)*pixelLength;
		double dZMin = z*pixelLength;
		double dZMax = (z+1)*pixelLength;
		//Side YNeg
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMax, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMin, vMax);
		//Side YPos
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMax, vMin);
		//Side ZNeg
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMax, vMin);
		//Side ZPos
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMin);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMin, vMax);
		//Side XNeg
		tes.addVertexWithUV(dXMin, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMin, dYMin, dZMax, uMax, vMin);
		tes.addVertexWithUV(dXMin, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMin, dYMax, dZMin, uMin, vMax);
		//Side XPos
		tes.addVertexWithUV(dXMax, dYMin, dZMin, uMin, vMin);
		tes.addVertexWithUV(dXMax, dYMax, dZMin, uMin, vMax);
		tes.addVertexWithUV(dXMax, dYMax, dZMax, uMax, vMax);
		tes.addVertexWithUV(dXMax, dYMin, dZMax, uMax, vMin);
	}

	public static void drawSubBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, boolean inverseXZ, boolean mirror, int x, int y, int z, Block block, RenderBlocks renderer)
	{
		if(mirror)
		{
			minX = 1-minX;
			minZ = 1-minZ;
			maxX = 1-maxX;
			maxZ = 1-maxZ;
		}

		if(inverseXZ)
			renderer.setRenderBounds(minZ,minY,minX,maxZ,maxY,maxX);
		else
			renderer.setRenderBounds(minX,minY,minZ,maxX,maxY,maxZ);

		renderer.renderStandardBlock(block, x, y, z);
	}

	public static void drawSubBlockInInventory(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, boolean inverseXZ, boolean mirror, Block block, RenderBlocks renderer)
	{
		if(mirror)
		{
			minX = 1-minX;
			minZ = 1-minZ;
			maxX = 1-maxX;
			maxZ = 1-maxZ;
		}

		if(inverseXZ)
			renderer.setRenderBounds(minZ,minY,minX,maxZ,maxY,maxX);
		else
			renderer.setRenderBounds(minX,minY,minZ,maxX,maxY,maxZ);

		drawStandardBlock(block, 0, renderer);
	}

	public static void drawStandardBlock(Block block, int meta, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;
		//GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
		tessellator.draw();
		//GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void renderIconWithMask(IIcon icon, IIcon mask, Color colour, IItemRenderer.ItemRenderType paramItemRenderType)
	{

		if ((mask == null) || (icon == null))
		{
			return;
		}
		//		System.out.println(colour);
		if(colour!=null)
			GL11.glColor4f(colour.getRed()/255f, colour.getGreen()/255f, colour.getBlue()/255f, colour.getAlpha()/255f);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);

		GL11.glDisable(2884);
		Tessellator localTessellator = Tessellator.instance;
		localTessellator.startDrawingQuads();
		localTessellator.setNormal(0.0F, 0.0F, 1.0F);
		if (paramItemRenderType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {

			Tessellator.instance.addVertexWithUV(0.0D, 16.0D, 10.0D, mask.getMinU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 16.0D, 10.0D, mask.getMaxU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 0.0D, 10.0D, mask.getMaxU(), mask.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, 10.0D, mask.getMinU(), mask.getMinV());

			//		    preRenderIconInv(mask, 10.0D);
		} else {

			Tessellator.instance.addVertexWithUV(0.0D, 1.0D, 0.001D, mask.getMinU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 1.0D, 0.001D, mask.getMaxU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 0.0D, 0.001D, mask.getMaxU(), mask.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, 0.001D, mask.getMinU(), mask.getMinV());
			//			preRenderIconWorld(mask, 0.001D);
		}
		localTessellator.draw();

		localTessellator.startDrawingQuads();
		localTessellator.setNormal(0.0F, 0.0F, -1.0F);
		if (paramItemRenderType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {


			Tessellator.instance.addVertexWithUV(0.0D, 16.0D, -0.0635D, mask.getMinU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 16.0D, -0.0635D, mask.getMaxU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 0.0D, -0.0635D, mask.getMaxU(), mask.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, -0.0635D, mask.getMinU(), mask.getMinV());
			//			preRenderIconInv(mask, -0.0635D);
		} else {

			Tessellator.instance.addVertexWithUV(0.0D, 1.0D, -0.0635D, mask.getMinU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 1.0D, -0.0635D, mask.getMaxU(), mask.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 0.0D, -0.0635D, mask.getMaxU(), mask.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, -0.0635D, mask.getMinU(), mask.getMinV());

			//			preRenderIconWorld(mask, -0.0635D);
		}
		localTessellator.draw();

		//		RenderHelper.setBlockTextureSheet();
		bindTexture("textures/atlas/blocks.png");


		GL11.glDepthFunc(514);
		GL11.glDepthMask(false);

		localTessellator.startDrawingQuads();
		localTessellator.setNormal(0.0F, 0.0F, 1.0F);
		if (paramItemRenderType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {


			Tessellator.instance.addVertexWithUV(0.0D, 16.0D, 10.0D, icon.getMinU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 16.0D, 10.0D, icon.getMaxU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 0.0D, 10.0D, icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, 10.0D, icon.getMinU(), icon.getMinV());

			//			preRenderIconInv(icon, 10.0D);
		} else {

			Tessellator.instance.addVertexWithUV(0.0D, 1.0D, 0.001D, icon.getMinU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 1.0D, 0.001D, icon.getMaxU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 0.0D, 0.001D, icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, 0.001D, icon.getMinU(), icon.getMinV());
			//			preRenderIconWorld(icon, 0.001D);
		}
		localTessellator.draw();

		localTessellator.startDrawingQuads();
		localTessellator.setNormal(0.0F, 0.0F, -1.0F);
		if (paramItemRenderType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {


			Tessellator.instance.addVertexWithUV(0.0D, 16.0D, -0.0635D, icon.getMinU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 16.0D, -0.0635D, icon.getMaxU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(16.0D, 0.0D, -0.0635D, icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, -0.0635D, icon.getMinU(), icon.getMinV());

			//			preRenderIconInv(icon, -0.0635D);
		} else {



			Tessellator.instance.addVertexWithUV(0.0D, 1.0D, -0.0635D, icon.getMinU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 1.0D, -0.0635D, icon.getMaxU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(1.0D, 0.0D, -0.0635D, icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(0.0D, 0.0D, -0.0635D, icon.getMinU(), icon.getMinV());
			//		  preRenderIconWorld(icon, -0.0635D);
		}
		localTessellator.draw();

		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(515);
		GL11.glEnable(2884);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void drawTooltipHoveringText(GuiScreen gui, List<String> list, int x, int y, FontRenderer font, int minimalWidth, int minimalHeight)
	{
		if ((list == null) || (list.isEmpty())) {
			return;
		}
		GL11.glDisable(32826);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		int k = minimalWidth;
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			String s = (String)iterator.next();
			int l = font.getStringWidth(s);
			if (l > k) {
				k = l;
			}
		}
		int i1 = x + 12;
		int j1 = y - 12;
		int k1 = 8;
		if (list.size() > 1) {
			k1 += 2 + (list.size() - 1) * 10;
		}
		if (i1 + k > gui.width) {
			i1 -= i1+k - gui.width;
		}
		if (j1 + k1 + 6 > gui.height) {
			j1 = gui.height - k1 - 6;
		}

		//gui.zLevel = 300.0F;
		//GuiContainer.itemRenderer.zLevel = 300.0F;
		int l1 = -267386864;
		drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
		drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
		drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
		drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
		drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
		int i2 = 1347420415;
		int j2 = (i2 & 0xFEFEFE) >> 1 | i2 & 0xFF000000;
		drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
		drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
		drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
		drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

		drawHoveringText(list, i1, j1, font);
		//		for (int k2 = 0; k2 < list.size(); k2++)
		//		{
		//			String s1 = (String)list.get(k2);
		//			font.drawStringWithShadow(s1, i1, j1, -1);
		//			if (k2 == 0) {
		//				j1 += 2;
		//			}
		//			j1 += 10;
		//		}
		//gui.zLevel = 0.0F;
		//GuiContainer.itemRenderer.zLevel = 0.0F;
		//		GL11.glEnable(2896);
		GL11.glEnable(2929);
		GL11.glEnable(32826);
	}

	public static void drawHoveringText(List<String> list, int x, int y, FontRenderer font)
	{
		for (int iS = 0; iS < list.size(); iS++)
		{
			String s1 = (String)list.get(iS);
			font.drawStringWithShadow(s1, x, y, -1);
			if (iS == 0) {
				y += 2;
			}
			y += 10;
		}
	}
	public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = (float)(par5 >> 24 & 255) / 255.0F;
		float f1 = (float)(par5 >> 16 & 255) / 255.0F;
		float f2 = (float)(par5 >> 8 & 255) / 255.0F;
		float f3 = (float)(par5 & 255) / 255.0F;
		float f4 = (float)(par6 >> 24 & 255) / 255.0F;
		float f5 = (float)(par6 >> 16 & 255) / 255.0F;
		float f6 = (float)(par6 >> 8 & 255) / 255.0F;
		float f7 = (float)(par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setColorRGBA_F(f1, f2, f3, f);
		Tessellator.instance.addVertex((double)par3, (double)par2, (double)500);
		Tessellator.instance.addVertex((double)par1, (double)par2, (double)500);
		Tessellator.instance.setColorRGBA_F(f5, f6, f7, f4);
		Tessellator.instance.addVertex((double)par1, (double)par4, (double)500);
		Tessellator.instance.addVertex((double)par3, (double)par4, (double)500);
		Tessellator.instance.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static double[] getPlayerHandPos(EntityPlayer player, boolean targetItem)
	{
		boolean flag = player.getEntityId()==Minecraft.getMinecraft().thePlayer.getEntityId() && Minecraft.getMinecraft().gameSettings.thirdPersonView!=0;
		double x = player.posX;
		double y = player.posY-(targetItem?.75:.875);
		double z = player.posZ;
		float radius = targetItem?.5f:.375f;
		double rotation  = ((player.renderYawOffset%360)+(player.getHeldItem()!=null?-25:0)+(targetItem?-30:0)) /180 *Math.PI;
		if(!flag)
		{
			y += .25;
			rotation  = ((player.rotationYaw%360)+(player.getHeldItem()!=null?-25:0)) /180 *Math.PI;
		}
		double xOff = Math.cos(rotation);
		double zOff = Math.sin(rotation);
		return new double[]{x-xOff*radius,y,z-zOff*radius};
	}


	static HashMap<String,IModelCustom> modelMap = new HashMap<String,IModelCustom>();
	static HashMap<String,ResourceLocation> textureMap = new HashMap<String,ResourceLocation>();

	public static void bindTexture(String path)
	{
		mc().getTextureManager().bindTexture(getResource(path));
	}
	public static ResourceLocation getResource(String path)
	{
		ResourceLocation rl = textureMap.containsKey(path) ? textureMap.get(path) : new ResourceLocation(path);
		if(!textureMap.containsKey(path))
			textureMap.put(path, rl);
		return rl;
	}

	public static IModelCustom bindModel(String domain, String path)
	{
		String mapPath = domain+":"+path;
		if(modelMap.containsKey(mapPath))
			return modelMap.get(mapPath);

		try
		{
			IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(domain, path));
			modelMap.put(mapPath, model);
			return model;
		}
		catch(Exception e)
		{
			WitchingGadgets.logger.log(Level.ERROR, "Error on attempt to load model: "+domain+","+path);
			e.printStackTrace();
			return null;
		}
	}

	public static void addBoxToBlockrender(double xMin,double yMin,double zMin, double xMax,double yMax,double zMax, IIcon icon, int... coords)
	{
		addBoxToBlockrender(null,xMin,yMin,zMin, xMax,yMax,zMax, icon, coords);
	}
	public static void addBoxToBlockrender(Vec3 offset, double xMin,double yMin,double zMin, double xMax,double yMax,double zMax, IIcon icon, int... coords)
	{
		Tessellator tes = Tessellator.instance;

		double w = icon.getMaxU()-icon.getMinU();
		double h = icon.getMaxV()-icon.getMinV();
		double tx = offset!=null?offset.xCoord:0;
		double ty = offset!=null?offset.yCoord:0;
		double tz = offset!=null?offset.zCoord:0;
		int x = coords[0];
		int y = coords[1];
		int z = coords[2];
		// Y
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMin, z+tz+zMax, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-zMax*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMin, z+tz+zMin, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-zMin*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMin, z+tz+zMin, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-zMin*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMin, z+tz+zMax, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-zMax*16));

		tes.addVertexWithUV(x+tx+xMax, y+ty+yMax, z+tz+zMax, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-zMax*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMax, z+tz+zMin, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-zMin*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMax, z+tz+zMin, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-zMin*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMax, z+tz+zMax, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-zMax*16));

		// Z
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMax, z+tz+zMin, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-yMax*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMax, z+tz+zMin, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-yMax*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMin, z+tz+zMin, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-yMin*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMin, z+tz+zMin, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-yMin*16));

		tes.addVertexWithUV(x+tx+xMin, y+ty+yMax, z+tz+zMax, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-yMax*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMin, z+tz+zMax, icon.getMinU()+w*Math.abs(xMin), icon.getInterpolatedV(16-yMin*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMin, z+tz+zMax, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-yMin*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMax, z+tz+zMax, icon.getMinU()+w*Math.abs(xMax), icon.getInterpolatedV(16-yMax*16));

		// X
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMax, z+tz+zMax, icon.getMinU()+w*Math.abs(zMax), icon.getInterpolatedV(16-yMax*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMax, z+tz+zMin, icon.getMinU()+w*Math.abs(zMin), icon.getInterpolatedV(16-yMax*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMin, z+tz+zMin, icon.getMinU()+w*Math.abs(zMin), icon.getInterpolatedV(16-yMin*16));
		tes.addVertexWithUV(x+tx+xMin, y+ty+yMin, z+tz+zMax, icon.getMinU()+w*Math.abs(zMax), icon.getInterpolatedV(16-yMin*16));

		tes.addVertexWithUV(x+tx+xMax, y+ty+yMin, z+tz+zMax, icon.getInterpolatedU(16-zMax*16), icon.getInterpolatedV(16-yMin*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMin, z+tz+zMin, icon.getInterpolatedU(16-zMin*16), icon.getInterpolatedV(16-yMin*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMax, z+tz+zMin, icon.getInterpolatedU(16-zMin*16), icon.getInterpolatedV(16-yMax*16));
		tes.addVertexWithUV(x+tx+xMax, y+ty+yMax, z+tz+zMax, icon.getInterpolatedU(16-zMax*16), icon.getInterpolatedV(16-yMax*16));
	}
	
	//	public static Framebuffer stackBuffer;
	//	/** Thanks TTFTCUTS! :P */
	//	public static BufferedImage getItemStackImage(ItemStack stack)
	//	{
	//		if (stackBuffer == null)
	//			stackBuffer = new Framebuffer(16,16,true);
	//		Minecraft mc = Minecraft.getMinecraft();
	//		// bind the buffer and such
	//		GL11.glPushMatrix();
	//		GL11.glLoadIdentity();
	//		stackBuffer.bindFramebuffer(true);
	//		GL11.glClearColor(0, 0, 0, 0);
	//		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	//		GL11.glMatrixMode(GL11.GL_PROJECTION);
	//		GL11.glLoadIdentity();
	//		GL11.glOrtho(0.0D, 16, 16, 0.0D, 1000.0D, 3000.0D);
	//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	//		GL11.glLoadIdentity();
	//		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	//		//GL11.glTranslatef(0, 0, -5);
	//		GL11.glColor3f(1, 0, 0);
	//		// draw image
	//		RenderItem render = new RenderItem();
	//		GL11.glPushMatrix();
	//		GL11.glEnable(GL11.GL_BLEND);
	//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	//		RenderHelper.enableGUIStandardItemLighting();
	//		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	//		GL11.glEnable(GL11.GL_DEPTH_TEST);
	//		render.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, 0, 0);
	//		RenderHelper.disableStandardItemLighting();
	//		GL11.glDisable(GL11.GL_BLEND);
	//		GL11.glPopMatrix();
	//		// set back to normal
	//		stackBuffer.unbindFramebuffer();
	//		GL11.glPopMatrix();
	//		// dump to image
	//		int width = 16;
	//		int height = 16;
	//		int length = width * height;
	//		IntBuffer data = BufferUtils.createIntBuffer(length);
	//		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	//		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
	//		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
	//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, stackBuffer.framebufferTexture);
	//		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, data);
	//		int[] dataarray = new int[length];
	//		data.get(dataarray);
	//		TextureUtil.func_147953_a(dataarray, 16, 16);
	//		int l = stackBuffer.framebufferTextureHeight - stackBuffer.framebufferHeight;
	//		image.setRGB(0, 0, 16, 16, dataarray, 0, 16);
	//		for (int i1 = l; i1 < stackBuffer.framebufferTextureHeight; ++i1)
	//			for (int j1 = 0; j1 < stackBuffer.framebufferWidth; ++j1)
	//				image.setRGB(j1, i1 - l, dataarray[i1 * stackBuffer.framebufferTextureWidth + j1]);
	//		// send out the image
	//		return image;
	//	}

	public static BufferedImage getImageForResource(ResourceLocation resource) throws IOException
	{
		ITextureObject texob = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
		//return ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
		InputStream layer = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
		return ImageIO.read(layer);
	}

	public static List<Integer> getItemColours(ItemStack stack)
	{
		List<Integer> colourSet = new ArrayList();
		Item item = stack.getItem();

		ResourceLocation resource;
		BufferedImage buffered;
		//= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
		try{

			for(int pass=0; pass<item.getRenderPasses(stack.getItemDamage()); pass++)
			{
				IIcon icon = item.getIcon(stack, pass);

				if(icon instanceof TextureAtlasSprite)
				{
					TextureAtlasSprite tas = (TextureAtlasSprite) icon;
					String iconName = tas.getIconName();
					iconName = iconName.substring(0, Math.max(0,iconName.indexOf(":")+1)) + (item.getSpriteNumber()==0?"textures/blocks/":"textures/items/") + iconName.substring(Math.max(0,iconName.indexOf(":")+1)) + ".png";
					resource = getResource(iconName);
					buffered = getImageForResource(resource);
					int passColour = item.getColorFromItemStack(stack, pass);
					//			System.out.println("Is atlas");
					int[] data = new int[tas.getIconWidth()*tas.getIconHeight()];
					buffered.getRGB(0,0, tas.getIconWidth(),tas.getIconHeight(), data, 0,tas.getIconWidth());
					//buffered.getRGB(tas.getOriginX(),tas.getOriginY(), tas.getIconWidth(),tas.getIconHeight(), data, 0,tas.getIconWidth());
					for(int rgb : data)
						if(rgb!=0)
						{
							//						System.out.println("Before: "+Integer.toHexString(rgb));
							int coloured = blendColoursToInt(rgb,passColour)&0xffffff;
							//						System.out.println("After: "+Integer.toHexString(coloured));
							if(coloured>0 /*&& !colourSet.contains(coloured)*/)
							{
								//System.out.println("herro?");
								colourSet.add(coloured);
							}
						}
					//					int maxFrames = ((TextureAtlasSprite)icon).hasAnimationMetadata()?((TextureAtlasSprite)icon).getFrameCount():1;
					//					for(int frame=0;frame<maxFrames;frame++)
					//					{
					//
					//
					//						//				System.out.println("Frame "+frame);
					//						//					int[][] frameColours = ((TextureAtlasSprite)icon).getFrameTextureData(frame);
					//						//
					//						//					//System.out.println(frameColours);
					//						//					for(int[] lvl : frameColours)
					//						//						for(int px : lvl)
					//						//						{
					//						//							//						System.out.print("Pixel: ");
					//						//							int full = blendColoursToInt(px,passColour);
					//						//							if(full>0 && !colourSet.contains(full))
					//						//							{
					//						//								//							System.out.print("Legit");
					//						//								colourSet.add(full);
					//						//							}
					//						//							//						System.out.println();
					//						//						}
					//					}
				}

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}


		return colourSet;
	}
	public static int getAverageItemColour(ItemStack stack)
	{
		List<Integer> col = ClientUtilities.getItemColours(stack);
		if(col!=null && col.size()>0)
		{
			int rgb = 0xffffff;
			for(int rgb2 : col)
				rgb = ClientUtilities.blendColoursToInt(rgb, rgb2);
			return rgb&0xffffff;
		}
		return 0xffffff;
	}
	

	public static List<Integer> getImageColours(BufferedImage image)
	{
		List<Integer> colourSet = new ArrayList();
		int[] data = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0,0, image.getWidth(),image.getHeight(), data, 0, image.getWidth());
		for(int i=0; i<data.length; i++)
			colourSet.add(data[i]);
		Collections.sort(colourSet, new ColourBrightnessComparator());
		return colourSet;
	}

	public static int blendColoursToInt(Object o0, Object o1)
	{
		return blendColours(o0,o1).getRGB();
	}
	public static Color blendColours(Object o0, Object o1)
	{
		assert(o0 instanceof Color || o0 instanceof Integer);
		assert(o1 instanceof Color || o1 instanceof Integer);
		Color c0 = o0 instanceof Color?(Color)o0:new Color((Integer)o0);
		Color c1 = o1 instanceof Color?(Color)o1:new Color((Integer)o1);

		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;

		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());
		return new Color((int) r, (int) g, (int) b, (int) a);
	}
	
	public static int getVibrantColourToInt(Object o0)
	{
		return getVibrantColour(o0).getRGB();
	}
	public static Color getVibrantColour(Object o0)
	{
		assert(o0 instanceof Color || o0 instanceof Integer);
		Color c = o0 instanceof Color?(Color)o0:new Color((Integer)o0);

		int modifierR = c.getRed()>c.getGreen()&&c.getRed()>c.getBlue()?0xff: c.getRed()<c.getGreen()&&c.getRed()<c.getBlue()?0x88: 0xbb; 
		int modifierG = c.getGreen()>c.getRed()&&c.getGreen()>c.getBlue()?0xff: c.getGreen()<c.getRed()&&c.getGreen()<c.getBlue()?0x88: 0xbb;
		int modifierB = c.getBlue()>c.getGreen()&&c.getBlue()>c.getRed()?0xff: c.getBlue()<c.getGreen()&&c.getBlue()<c.getRed()?0x88: 0xbb;
		int modifier = (modifierR<<16)+(modifierG<<8)+(modifierB);
		Color cV = ClientUtilities.blendColours(c, modifier);
		return cV;
	}


	public static int getPerceptualBrightness(int col)
	{
		double r = ((col>>16) &0xFF) / 255.0;
		double g = ((col>> 8) &0xFF) / 255.0;
		double b = ((col    ) &0xFF) / 255.0;
		double brightness = Math.sqrt(0.241 * r*r + 0.691 * g*g + 0.068 * b*b);
		return (int)(brightness*255);
	}

	public static class ColourBrightnessComparator implements Comparator<Integer>
	{
		@Override
		public int compare(Integer i0, Integer i1)
		{
			Integer b1 = getPerceptualBrightness(i0);
			Integer b2 = getPerceptualBrightness(i1);
			return b1.compareTo(b2);
		}

	}
}