package witchinggadgets.client.render;

import java.util.List;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.renderers.models.ModelArcaneWorkbench;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityLabelLibrary;

public class TileRenderLabelLibrary extends TileEntitySpecialRenderer
{
	static ModelArcaneWorkbench model = new ModelArcaneWorkbench();
	static
	{
		ModelRenderer bookOut = new ModelRenderer(model, 72,8);
		bookOut.addBox(0.0F, 0.0F, 0.0F, 3, 8, 6);
		bookOut.setRotationPoint(-7.0F, 16.0F, 0.0F);
		model.boxList.add(bookOut);

		ModelRenderer bookIn = new ModelRenderer(model, 72,22);
		bookIn.addBox(0.0F, 0.0F, 0.0F, 3, 7, 5);
		bookIn.setRotationPoint(-7.0F, 16.0F, 1.0F);
		model.boxList.add(bookIn);

		ModelRenderer bookOut2 = new ModelRenderer(model, 90,8);
		bookOut2.addBox(0.0F, 0.0F, 0.0F, 6, 3, 8);
		bookOut2.setRotationPoint(1.0F, 16.0001F, -7.0F);
		model.boxList.add(bookOut2);

		ModelRenderer bookIn2 = new ModelRenderer(model, 90,19);
		bookIn2.addBox(0.0F, 0.0F, 0.0F, 5, 3, 7);
		bookIn2.setRotationPoint(2.0F, 16.0001F, -6.5F);
		model.boxList.add(bookIn2);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityLabelLibrary tile = (TileEntityLabelLibrary) tileentity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x+.5f, (float)y, (float)z+.5f);
		switch(tile.facing)
		{
		case 2:
			break;
		case 3:
			GL11.glRotatef(180, 0,1,0);
			break;
		case 4:
			GL11.glRotatef(90, 0,1,0);
			break;
		case 5:
			GL11.glRotatef(270, 0,1,0);
			break;
		}

		ClientUtilities.bindTexture("witchinggadgets:textures/models/labelLib.png");
		GL11.glDisable(GL11.GL_CULL_FACE);
		for(ModelRenderer m : (List<ModelRenderer>)model.boxList)
		{
			m.render(.0625f);
		}

		if(tile.getWorldObj() != null)
		{
			GL11.glBlendFunc(770, 771);
			ClientUtilities.bindTexture("textures/atlas/items.png");
			GL11.glTranslatef(-.375f, 1f, -.4375f);
			GL11.glScaled(.375f,.75f,.375f);
			GL11.glRotatef(90, 1,0,0);
			render2DItem(tile.getStackInSlot(1));

			if(tile.getStackInSlot(0)!=null)
			{
				GL11.glTranslatef(.875f, 1.4f, 0);
				GL11.glScaled(1,1,.5f);
				int stacksize = Math.max(1, tile.getStackInSlot(0).stackSize/6);
				for(int i=0; i<stacksize; i++)
				{
					GL11.glTranslatef(.5f,.5f,0);
					GL11.glRotatef(10+i*i, 0,0,1);
					GL11.glTranslatef(-.5f,-.5f,0);

					GL11.glTranslatef(0,0, -.05f);
					render2DItem(tile.getStackInSlot(0));
				}
			}
		}
		GL11.glPopMatrix();
	}

	void render2DItem(ItemStack stack)
	{
		if(stack!=null)
			for(int pass=0;pass<stack.getItem().getRenderPasses(stack.getItemDamage());pass++)
			{
				int c = stack.getItem().getColorFromItemStack(stack, pass);
				GL11.glColor3f((c>>16&255)/255f,(c>>8&255)/255f,(c&255)/255f);
				IIcon iicon = stack.getItem().getIcon(stack, pass);
				ItemRenderer.renderItemIn2D(Tessellator.instance,  iicon.getMaxU(), iicon.getMinV(), iicon.getMinU(), iicon.getMaxV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
				GL11.glColor3f(1,1,1);
			}
	}

}