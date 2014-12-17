package witchinggadgets.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;

public class ItemRenderWallMirror implements IItemRenderer
{
	public ItemRenderWallMirror()
	{
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		float scale;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glRotatef(90,0,1,0);

		switch(type)
		{
		case ENTITY:
			scale = 1f;
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(0f, 0f, -0.5f);
			break;
		case EQUIPPED:
			scale = 0.8f;
			GL11.glRotatef(5f, 0f, -1f, 0f);
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(-0.8f, -0.25f, 0.2f);
			break;
		case INVENTORY:
			scale = 0.9f;
			GL11.glRotatef(45f, 0f, 1f, 0f);
			GL11.glRotatef(180, 0f, 1f, 0f);
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(0f, -1f, -0.5f);
			break;
		case EQUIPPED_FIRST_PERSON:
			scale = 2f;
			GL11.glScalef(scale, scale, scale);
			GL11.glRotatef(60,0,1,0);
			GL11.glRotatef(30,1,0,0);
			GL11.glTranslatef(-2f, -1f, 0.5f);
			break;

		default:
			break;
		}

		Tessellator tes = Tessellator.instance;

		double glassUmin = 0.0;
		double glassUmax = 1.0/32.0;
		double glassVmin = 0.0;
		double glassVmax = 0.5;

		ClientUtilities.bindTexture("witchinggadgets:textures/models/glass.png");
		tes.startDrawingQuads();
		tes.addVertexWithUV(0.03125, 0, 0, glassUmax, glassVmax);
		tes.addVertexWithUV(0.03125, 2, 0, glassUmax, glassVmin);
		tes.addVertexWithUV(0.03125, 2, 1, glassUmin, glassVmin);
		tes.addVertexWithUV(0.03125, 0, 1, glassUmin, glassVmax);
		tes.draw();
		
		tes.startDrawingQuads();
		tes.addVertexWithUV(0.03125, 0, 0, glassUmin, glassVmax);
		tes.addVertexWithUV(0.03125, 0, 1, glassUmax, glassVmax);
		tes.addVertexWithUV(0.03125, 2, 1, glassUmax, glassVmin);
		tes.addVertexWithUV(0.03125, 2, 0, glassUmin, glassVmin);
		tes.draw();

		//this.bindTexture(new ResourceLocation(Block.blockGold.getItemIconName()));
		ClientUtilities.bindTexture("witchinggadgets:textures/blocks/white.png");

		int[][][] shape = TileRenderWallMirror.shape;

		for(int i=0;i<shape.length;i++)
		{
			for(int j=0;j<shape[i].length;j++)
			{
				if(shape[i][j][0] != -1)
				{
					double r = shape[i][j][0] / 256.0;
					double g = shape[i][j][1] / 256.0;
					double b = shape[i][j][2] / 256.0;
					GL11.glColor3d(r, g, b);
					ClientUtilities.renderPixelBlock(tes, 0, i, j, 0.0625, 0, 0, 1, 1);
					ClientUtilities.renderPixelBlock(tes, 0, i, 15-j, 0.0625, 0, 0, 1, 1);

					ClientUtilities.renderPixelBlock(tes, 0, 15-i+16, j, 0.0625, 0, 0, 1, 1);
					ClientUtilities.renderPixelBlock(tes, 0, 15-i+16, 15-j, 0.0625, 0, 0, 1, 1);
					GL11.glColor3d(1, 1, 1);
				}
			}
		}


		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}