package witchinggadgets.client.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import witchinggadgets.common.WGContent;

public class ItemRenderMagicalBaubles implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type)
	{
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			return true;
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper)
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
	{
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		EntityLivingBase par1EntityLivingBase;
		try{
			par1EntityLivingBase = (EntityLivingBase) data[1];
		}catch(Exception e)
		{
			e.printStackTrace();
			GL11.glPopMatrix();
			return;
		}
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		for (int l = 0; l < WGContent.ItemMagicalBaubles.getRenderPasses(stack.getItemDamage()); ++l)
		{
			IIcon iicon = par1EntityLivingBase.getItemIcon(stack, l);

			if (iicon == null)
			{
				GL11.glPopMatrix();
				return;
			}

			textureManager.bindTexture(textureManager.getResourceLocation(stack.getItemSpriteNumber()));
			TextureUtil.func_152777_a(false, false, 1f);
			Tessellator tessellator = Tessellator.instance;
			float f = iicon.getMinU();
			float f1 = iicon.getMaxU();
			float f2 = iicon.getMinV();
			float f3 = iicon.getMaxV();

			int colour = WGContent.ItemMagicalBaubles.getColorFromItemStack(stack, l);
			Color col = new Color(colour);
			if(l>0)
				GL11.glColor3d(col.getRed()/255.0, col.getGreen()/255.0, col.getBlue()/255.0);
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		textureManager.bindTexture(textureManager.getResourceLocation(stack.getItemSpriteNumber()));
		TextureUtil.func_147945_b();
	}
}