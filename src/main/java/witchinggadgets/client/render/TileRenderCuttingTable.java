package witchinggadgets.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;

public class TileRenderCuttingTable extends TileEntitySpecialRenderer
{
	static ModelCuttingTable model = new ModelCuttingTable();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityCuttingTable tile = (TileEntityCuttingTable) tileentity;
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

		ClientUtilities.bindTexture("witchinggadgets:textures/models/cuttingTable.png");
		model.render(null,0,0,0,0,0, .0625f);
		GL11.glBlendFunc(770, 771);
		
		for(byte i=0;i<3;i++)
			if(tile.getStackInSlot(1+i)!=null && tile.getStackInSlot(1+i).hasTagCompound())
			{
				AspectList aspects = new AspectList();
				aspects.readFromNBT(tile.getStackInSlot(1+i).getTagCompound());
				if(!aspects.aspects.isEmpty())
					if(tile.getStackInSlot(1+i).getItem().equals(ConfigItems.itemEssence))
						model.renderFlask(i, aspects.getAspects()[0].getColor());
					else if(tile.getStackInSlot(1+i).getItem().equals(ConfigItems.itemWispEssence))
						model.renderEssence(i, aspects.getAspects()[0].getColor());
			}
		ClientUtilities.bindTexture("textures/atlas/items.png");
		GL11.glTranslatef(-.2f, .875f, -.25f);
		GL11.glScaled(.375f,.75f,.375f);
		GL11.glRotatef(90, 1,0,0);
		if(tile.getStackInSlot(0)!=null)
			for(int pass=0;pass<tile.getStackInSlot(0).getItem().getRenderPasses(tile.getStackInSlot(0).getItemDamage());pass++)
			{
				IIcon iicon = tile.getStackInSlot(0).getItem().getIcon(tile.getStackInSlot(0), pass);
				ItemRenderer.renderItemIn2D(Tessellator.instance,  iicon.getMaxU(), iicon.getMinV(), iicon.getMinU(), iicon.getMaxV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
			}

		GL11.glPopMatrix();
	}

	static class ModelCuttingTable extends ModelBase
	{
		List<ModelRenderer> parts  = new ArrayList();
		List<ModelRenderer[]> flasks  = new ArrayList();
		List<ModelRenderer[]> bowls  = new ArrayList();
		public ModelCuttingTable()
		{
			parts.clear();
			ModelRenderer temp;
			//BOTTOM
			temp = new ModelRenderer(this, 0, 0);
			temp.addBox(0.0F, 0.0F, 0.0F, 16, 2, 16);
			temp.setRotationPoint(-8.0F, 0.0F, -8.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//TOP
			temp = new ModelRenderer(this, 0, 0);
			temp.addBox(0.0F, 0.0F, 0.0F, 16, 4, 16);
			temp.setRotationPoint(-8.0F, 10.0F, -8.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//CARPET
			temp = new ModelRenderer(this, -32, 0);
			temp.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16);
			temp.setRotationPoint(-8.0F, 14.05F, -8.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//LEG1
			temp = new ModelRenderer(this, 0, 20);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 8, 3);
			temp.setRotationPoint(-7.0F, 2.0F, -7.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//LEG2
			temp = new ModelRenderer(this, 0, 20);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 8, 3);
			temp.setRotationPoint(4.0F, 2.0F, -7.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//LEG3
			temp = new ModelRenderer(this, 0, 20);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 8, 3);
			temp.setRotationPoint(-7.0F, 2.0F, 4.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//LEG4
			temp = new ModelRenderer(this, 0, 20);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 8, 3);
			temp.setRotationPoint(4.0F, 2.0F, 4.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			//QUARTZ TOOL
			temp = new ModelRenderer(this, 52, 0);
			temp.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
			temp.setRotationPoint(4.8F, 13.9F, 2.2F);
			temp.setTextureSize(64, 32);
			temp.rotateAngleY = 0.4f;
			temp.mirror = true;
			parts.add(temp);
			temp = new ModelRenderer(this, 52, 0);
			temp.addBox(0.0F, 0.0F, 0.0F, 1, 1, 5);
			temp.setRotationPoint(7F, 14.0F, -2.5F);
			temp.setTextureSize(64, 32);
			temp.rotateAngleY = -0.4f;
			temp.mirror = true;
			parts.add(temp);
			//FLINT TOOL
			temp = new ModelRenderer(this, 52, 6);
			temp.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
			temp.setRotationPoint(-6F, 13.9F, -.8F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp.rotateAngleY=-.1f;
			parts.add(temp);
			temp = new ModelRenderer(this, 52, 0);
			temp.addBox(0.0F, 0.0F, 0.0F, 1, 1, 5);
			temp.setRotationPoint(-6.5F, 14.0F, -5.5F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp.rotateAngleY=.1f;
			parts.add(temp);
			//IRON TOOL
			temp = new ModelRenderer(this, 52, 1);
			temp.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4);
			temp.setRotationPoint(4.5F, 14.0F, -5.5F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			parts.add(temp);
			temp = new ModelRenderer(this, 58, 6);
			temp.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
			temp.setRotationPoint(4.5F, 13.9F, -2.8F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp.rotateAngleY=-.1f;
			parts.add(temp);





			ModelRenderer temp2;
			ModelRenderer temp3;
			ModelRenderer temp4;
			//FLASK 0
			temp = new ModelRenderer(this, 16, 26);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 3, 3);
			temp.setRotationPoint(-7.5F, 14F, 4F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp2 = new ModelRenderer(this, 16, 23);
			temp2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
			temp2.setRotationPoint(-7F, 17F, 4.5F);
			temp2.setTextureSize(64, 32);
			temp2.mirror = true;
			temp3 = new ModelRenderer(this, 24, 24);
			temp3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
			temp3.setRotationPoint(-6.5F, 17.5F, 5F);
			temp3.setTextureSize(64, 32);
			temp3.mirror = true;
			temp4 = new ModelRenderer(this, 28, 27);
			temp4.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2);
			temp4.setRotationPoint(-7F, 14.5F, 4.5F);
			temp4.setTextureSize(64, 32);
			temp4.mirror = true;
			flasks.add(new ModelRenderer[]{temp,temp2,temp3,temp4});
			//FLASK 1
			temp = new ModelRenderer(this, 16, 26);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 3, 3);
			temp.setRotationPoint(-3F, 14F, 4.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp2 = new ModelRenderer(this, 16, 23);
			temp2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
			temp2.setRotationPoint(-2.5F, 17F, 4.5F);
			temp2.setTextureSize(64, 32);
			temp2.mirror = true;
			temp3 = new ModelRenderer(this, 24, 24);
			temp3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
			temp3.setRotationPoint(-2F, 17.5F, 5F);
			temp3.setTextureSize(64, 32);
			temp3.mirror = true;
			temp4 = new ModelRenderer(this, 28, 27);
			temp4.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2);
			temp4.setRotationPoint(-2.5F, 14.5F, 4.5F);
			temp4.setTextureSize(64, 32);
			temp4.mirror = true;
			flasks.add(new ModelRenderer[]{temp,temp2,temp3,temp4});
			//FLASK 2
			temp = new ModelRenderer(this, 16, 26);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 3, 3);
			temp.setRotationPoint(1.5F, 14F, 4.0F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp2 = new ModelRenderer(this, 16, 23);
			temp2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
			temp2.setRotationPoint(2F, 17F, 4.5F);
			temp2.setTextureSize(64, 32);
			temp2.mirror = true;
			temp3 = new ModelRenderer(this, 24, 24);
			temp3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
			temp3.setRotationPoint(2.5F, 17.5F, 5F);
			temp3.setTextureSize(64, 32);
			temp3.mirror = true;
			temp4 = new ModelRenderer(this, 28, 27);
			temp4.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2);
			temp4.setRotationPoint(2F, 14.5F, 4.5F);
			temp4.setTextureSize(64, 32);
			temp4.mirror = true;
			flasks.add(new ModelRenderer[]{temp,temp2,temp3,temp4});

			ModelRenderer temp5;
			ModelRenderer temp6;
			//ESSENCE 0
			temp = new ModelRenderer(this, 36, 24);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
			temp.setRotationPoint(-7.5F, 14F, 4F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp2 = new ModelRenderer(this, 36, 20);
			temp2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
			temp2.setRotationPoint(-8F, 15F, 4F);
			temp2.setTextureSize(64, 32);
			temp2.mirror = true;
			temp3 = new ModelRenderer(this, 36, 20);
			temp3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
			temp3.setRotationPoint(-5F, 15F, 4F);
			temp3.setTextureSize(64, 32);
			temp3.mirror = true;
			temp4 = new ModelRenderer(this, 44, 20);
			temp4.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1);
			temp4.setRotationPoint(-7.5F, 15F, 6.5F);
			temp4.setTextureSize(64, 32);
			temp4.mirror = true;
			temp5 = new ModelRenderer(this, 44, 20);
			temp5.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1);
			temp5.setRotationPoint(-7.5F, 15F, 3.5F);
			temp5.setTextureSize(64, 32);
			temp5.mirror = true;
			temp6 = new ModelRenderer(this, 48, 22);
			temp6.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
			temp6.setRotationPoint(-7F, 14.75F, 4.5F);
			temp6.setTextureSize(64, 32);
			temp6.mirror = true;
			bowls.add(new ModelRenderer[]{temp,temp2,temp3,temp4,temp5,temp6});
			//ESSENCE 1
			temp = new ModelRenderer(this, 36, 24);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
			temp.setRotationPoint(-3F, 14F, 4F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp2 = new ModelRenderer(this, 36, 20);
			temp2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
			temp2.setRotationPoint(-.5F, 15F, 4F);
			temp2.setTextureSize(64, 32);
			temp2.mirror = true;
			temp3 = new ModelRenderer(this, 36, 20);
			temp3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
			temp3.setRotationPoint(-3.5F, 15F, 4F);
			temp3.setTextureSize(64, 32);
			temp3.mirror = true;
			temp4 = new ModelRenderer(this, 44, 20);
			temp4.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1);
			temp4.setRotationPoint(-3F, 15F, 6.5F);
			temp4.setTextureSize(64, 32);
			temp4.mirror = true;
			temp5 = new ModelRenderer(this, 44, 20);
			temp5.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1);
			temp5.setRotationPoint(-3F, 15F, 3.5F);
			temp5.setTextureSize(64, 32);
			temp5.mirror = true;
			temp6 = new ModelRenderer(this, 48, 22);
			temp6.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
			temp6.setRotationPoint(-2.5F, 14.75F, 4.5F);
			temp6.setTextureSize(64, 32);
			temp6.mirror = true;
			bowls.add(new ModelRenderer[]{temp,temp2,temp3,temp4,temp5,temp6});
			//ESSENCE 2
			temp = new ModelRenderer(this, 36, 24);
			temp.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
			temp.setRotationPoint(1.5F, 14F, 4F);
			temp.setTextureSize(64, 32);
			temp.mirror = true;
			temp2 = new ModelRenderer(this, 36, 20);
			temp2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
			temp2.setRotationPoint(1F, 15F, 4F);
			temp2.setTextureSize(64, 32);
			temp2.mirror = true;
			temp3 = new ModelRenderer(this, 36, 20);
			temp3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
			temp3.setRotationPoint(4F, 15F, 4F);
			temp3.setTextureSize(64, 32);
			temp3.mirror = true;
			temp4 = new ModelRenderer(this, 44, 20);
			temp4.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1);
			temp4.setRotationPoint(1.5F, 15F, 6.5F);
			temp4.setTextureSize(64, 32);
			temp4.mirror = true;
			temp5 = new ModelRenderer(this, 44, 20);
			temp5.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1);
			temp5.setRotationPoint(1.5F, 15F, 3.5F);
			temp5.setTextureSize(64, 32);
			temp5.mirror = true;
			temp6 = new ModelRenderer(this, 48, 22);
			temp6.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
			temp6.setRotationPoint(2F, 14.75F, 4.5F);
			temp6.setTextureSize(64, 32);
			temp6.mirror = true;
			bowls.add(new ModelRenderer[]{temp,temp2,temp3,temp4,temp5,temp6});
		}

		@Override
		public void render(Entity ent, float par2, float par3, float par4, float par5, float par6, float par7)
		{
			for(ModelRenderer mr : parts)
				mr.render(par7);
		}

		public void renderFlask(byte fl, int colour)
		{
			//			System.out.println("rendering flask");
			GL11.glEnable(3042);
			flasks.get(fl)[2].render(.0625f);
			GL11.glColor3f((colour >> 16 & 255)/255.0F, (colour >> 8 & 255)/255.0F, (colour & 255) / 255.0F);
			flasks.get(fl)[3].render(.0625f);
			GL11.glColor4f(1,1,1,1);
			flasks.get(fl)[0].render(.0625f);
			flasks.get(fl)[1].render(.0625f);
			GL11.glDisable(3042);
		}
		public void renderEssence(byte fl, int colour)
		{
			bowls.get(fl)[0].render(.0625f);
			bowls.get(fl)[1].render(.0625f);
			bowls.get(fl)[2].render(.0625f);
			bowls.get(fl)[3].render(.0625f);
			bowls.get(fl)[4].render(.0625f);
			GL11.glColor3f((colour >> 16 & 255)/255.0F, (colour >> 8 & 255)/255.0F, (colour & 255) / 255.0F);
			bowls.get(fl)[5].render(.0625f);
			GL11.glColor4f(1,1,1,1);
		}


	}
}