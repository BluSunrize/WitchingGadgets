package witchinggadgets.client.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.items.ItemCrystalCapsule;

public class ItemRenderCapsule implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return type.equals(IItemRenderer.ItemRenderType.ENTITY);
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glEnable(3008);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);

		if(type.equals(ItemRenderType.ENTITY))
		{
			GL11.glScaled(.75f,.75f,.75f);
			GL11.glTranslated(-.5,-.2, 0);
		}

		IIcon iicon;
		Tessellator tes = Tessellator.instance;

		if( ((ItemCrystalCapsule)WGContent.ItemCapsule).getFluidStored(item)!=null )
		{
			Fluid fluid = ((ItemCrystalCapsule)WGContent.ItemCapsule).getFluidStored(item);
			iicon = fluid.getIcon(new FluidStack(fluid,1000));
			int colour = fluid.getColor(new FluidStack(fluid,1000)); 

			if(fluid.getBlock()!=null)
			{
				iicon = fluid.getBlock().getBlockTextureFromSide(0);
				colour = fluid.getBlock().colorMultiplier(Minecraft.getMinecraft().theWorld, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
			}
			if(iicon==null)
			{
				GL11.glPopMatrix();
				return;
			}

			GL11.glScaled(1,1,.75f);
			GL11.glTranslatef(0,0,-.01f);
			ClientUtilities.renderIconWithMask(iicon, ((ItemCrystalCapsule)WGContent.ItemCapsule).fluidMask, new Color(colour), type);
			GL11.glTranslatef(0,0,.01f);
			GL11.glScaled(1,1,1/.75f);

			//			ItemRenderer.renderItemIn2D(p_78439_0_, p_78439_1_, p_78439_2_, p_78439_3_, p_78439_4_, p_78439_5_, p_78439_6_, p_78439_7_);derItemIn2D(tes,  iicon.getMaxU(), iicon.getMinV(), iicon.getMinU(), iicon.getMaxV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
		}

		iicon = WGContent.ItemCapsule.getIcon(item, 0);
		textureManager.bindTexture(textureManager.getResourceLocation(item.getItemSpriteNumber()));
		if(type.equals(ItemRenderType.INVENTORY))
		{
			GL11.glScaled(16,16,16);
			GL11.glTranslatef(0,0,1);
		}
		ItemRenderer.renderItemIn2D(tes,  iicon.getMaxU(), iicon.getMinV(), iicon.getMinU(), iicon.getMaxV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
		iicon = WGContent.ItemCapsule.getIcon(item, 1);
		if(iicon==null)
		{
			GL11.glPopMatrix();
			return;
		}

		GL11.glEnable(3042);
		ItemRenderer.renderItemIn2D(tes,  iicon.getMaxU(), iicon.getMinV(), iicon.getMinU(), iicon.getMaxV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
		if(type.equals(ItemRenderType.INVENTORY))
			GL11.glScaled(1/16,1/16,1/16);

		GL11.glDisable(3042);
		textureManager.bindTexture(textureManager.getResourceLocation(item.getItemSpriteNumber()));
		TextureUtil.func_147945_b();
		GL11.glPopMatrix();
	}

}
