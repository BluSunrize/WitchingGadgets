package witchinggadgets.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import witchinggadgets.client.ClientProxy;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.items.ItemInfusedGem;
import witchinggadgets.common.util.Utilities;

public class ItemRenderInfusedGem implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper)
	{
		if(type.equals(ItemRenderType.ENTITY))
			return true;
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
	{
		if(!(stack.getItem() instanceof ItemInfusedGem))
			return;
		GL11.glPushMatrix();
		GL11.glRotatef(90, 0,1,0);
		GL11.glRotatef(22.5f,0,0,1);
		switch(type)
		{
		case ENTITY:
			GL11.glScalef(2,2,2);
			break;
		case EQUIPPED:
			GL11.glScalef(1.5f,1.5f,1.5f);
			GL11.glTranslated(0.3125f, 0.0f, 0);
			GL11.glRotatef(15f, 0, 0, 1);
			GL11.glRotatef(5f, 1, 0, 0);
			break;
		case EQUIPPED_FIRST_PERSON:
			GL11.glScalef(1f,1f,-1f);
			GL11.glTranslated(0.75f, .0625f, 0);
			break;
		case FIRST_PERSON_MAP:
			break;
		case INVENTORY:
			GL11.glScalef(40f,40f,40f);
			GL11.glRotatef(180,0,0,1);
			GL11.glTranslatef(-.125f,-.32f, .2f);
			break;
		default:
			break;
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(3042);

		ItemInfusedGem.GemCut cut = ItemInfusedGem.getCut(stack);

		ClientUtilities.bindTexture("witchinggadgets:textures/models/white.png");
		Aspect a = ItemInfusedGem.getAspect(stack);
		if(a!=null)
		{
			//			GL11.glBlendFunc(770, a.getBlend());
			float r = (a.getColor() >> 16 & 0xff)/255f;
			float g = (a.getColor() >> 8 & 0xff)/255f;
			float b = (a.getColor() & 0xff)/255f;
			GL11.glColor4f(r, g, b, .9375f);
		}
		if(cut!=null)
			ClientProxy.gemModel.renderPart(Utilities.getTitleCase(cut.name())+"Cut_0"+cut.ordinal());
		if(cut!=null && type!=ItemRenderType.INVENTORY)
		{
			GL11.glBlendFunc(770, 1);
			GL11.glColor4f(1,1,1, 1f);
			float scale = .875f;
			GL11.glScaled(scale,scale,scale);
			GL11.glTranslatef(0, .015625f,0);
			ClientProxy.gemModel.renderPart(Utilities.getTitleCase(cut.name())+"Cut_0"+cut.ordinal());
			GL11.glTranslatef(0,-.015625f,0);
			GL11.glScaled(1/scale,1/scale,1/scale);
		}
		if(cut!=null && stack.hasEffect(0))
		{
			GL11.glPushMatrix();
			//			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			ClientUtilities.bindTexture("textures/misc/enchanted_item_glint.png");
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(768, 1, 1, 0);
			float f7 = 0.76F;
			GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			float f8 = 0.125F;
			GL11.glScalef(f8, f8, f8);
			float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
			GL11.glTranslatef(f9, 0.0F, 0.0F);
			GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);

			ClientProxy.gemModel.renderPart(Utilities.getTitleCase(cut.name())+"Cut_0"+cut.ordinal());

			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(f8, f8, f8);
			f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
			GL11.glTranslatef(-f9, 0.0F, 0.0F);
			GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);

			ClientProxy.gemModel.renderPart(Utilities.getTitleCase(cut.name())+"Cut_0"+cut.ordinal());

			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glPopMatrix();
			GL11.glColor3f(1, 1, 1);

		}
		GL11.glColor4f(1,1,1,1);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}