package witchinggadgets.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;

public class ItemRenderSpinningWheel  implements IItemRenderer
{
	ModelSpinningWheel model;

	public ItemRenderSpinningWheel()
	{
		model = new ModelSpinningWheel();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return item.getItemDamage() == 0;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return item.getItemDamage() == 0;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		if(item.getItemDamage() != 8)
			return;
		float scale;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glRotatef(90,0,1,0);

		switch(type)
		{
		case ENTITY:
			scale = 1f;
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(0f, 0f, -0.9f);
			break;
		case EQUIPPED:
			scale = 1f;
			GL11.glRotatef(5f, 0f, -1f, 0f);
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(-0.5f, 0.9f, 0.9f);
			break;
		case INVENTORY:
			scale = 1f;
			GL11.glRotatef(45f, 0f, 1f, 0f);
			GL11.glRotatef(180, 0f, 1f, 0f);
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(-0.4f, -0f, -0.4f);
			break;
		case EQUIPPED_FIRST_PERSON:
			scale = 0.5f;
			GL11.glScalef(scale, scale, scale);
			GL11.glRotatef(90,0,1,0);
			GL11.glTranslatef(1.5f, 2.9f, -1.65f);
			break;

		default:
			break;
		}

		ClientUtilities.bindTexture("witchinggadgets:textures/models/SpinningWheel.png");
		model.render(0);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}