package witchinggadgets.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;

public class TileRenderSpinningWheel extends TileEntitySpecialRenderer
{

	static ModelSpinningWheel model = new ModelSpinningWheel();

	public void renderTileEntityAt(TileEntitySpinningWheel tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		GL11.glTranslatef((float)x, (float)y, (float)z);

		switch(tile.facing)
		{
		case 2:
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, 0F, 0F);
			break;
		case 3:
			GL11.glRotatef(270, 0, 1, 0);
			GL11.glTranslatef(0.5F, 0F,-1F);
			break;
		case 4:
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslatef(-0.5F, 0F,-1F);
			break;
		case 5:
			GL11.glTranslatef(0.5F, 0F, 0F);
			break;
		}

		ClientUtilities.bindTexture("witchinggadgets:textures/models/spinningwheel.png");
		model.render(null,0,0,0,0,0, .0625f);

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		renderTileEntityAt((TileEntitySpinningWheel)tileentity, d0, d1, d2, f);
	}

	static class ModelSpinningWheel extends ModelBase
	{
		List<ModelRenderer> parts  = new ArrayList();
		public ModelSpinningWheel()
		{
			parts.clear();
			ModelRenderer temp;
			
			//LEG 1
			temp = new ModelRenderer(this, 0, 0);
			temp.addBox(0F, 0.0F, 0.0F, 2, 8, 2);
			temp.setRotationPoint(1F, 0.0F, 14F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			//LEG 2
			temp = new ModelRenderer(this, 0, 0);
			temp.addBox(0F, 0.0F, 0.0F, 2, 8, 2);
			temp.setRotationPoint(-3F, 0.0F, 14F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			//LEG 3
			temp = new ModelRenderer(this, 0, 0);
			temp.addBox(0F, 0.0F, 0.0F, 2, 6, 2);
			temp.setRotationPoint(-1F, 0.0F, 1F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			
			//BASE
			temp = new ModelRenderer(this, 0, 0);
			temp.addBox(0F, 0.0F, 0.0F, 6, 2, 16);
			temp.setRotationPoint(-3F, 5.0F, 0.25F);
			temp.rotateAngleX=(float) Math.toRadians(-10f);
			temp.setTextureSize(64,32);
			parts.add(temp);
			
			//WHEEL
			temp = new ModelRenderer(this, 8, 0);
			temp.addBox(0F, 0.0F, 0.0F, 1, 3, 1);
			temp.setRotationPoint(-.5F, 12.0F, 0F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			temp = new ModelRenderer(this, 8, 0);
			temp.addBox(0F, 0.0F, 0.0F, 1, 3, 1);
			temp.setRotationPoint(-.5F, 12.0F, 6.35F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			temp = new ModelRenderer(this, 8, 4);
			temp.addBox(0F, 0.0F, 0.0F, 1, 1, 3);
			temp.setRotationPoint(-.5F, 9.9F, 2.2F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			temp = new ModelRenderer(this, 8, 4);
			temp.addBox(0F, 0.0F, 0.0F, 1, 1, 3);
			temp.setRotationPoint(-.5F, 16.1F, 2.2F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			//WHEEL DIAGONAL
			temp = new ModelRenderer(this, 8, 4);
			temp.addBox(0F, 0.0F,-1.5F, 1, 1, 3);
			temp.setRotationPoint(-.5F, 15.3F, 5.6F);
			temp.setTextureSize(64,32);
			temp.rotateAngleX=(float) Math.toRadians(45f);
			parts.add(temp);
			temp = new ModelRenderer(this, 8, 4);
			temp.addBox(0F, 0.0F,-1.5F, 1, 1, 3);
			temp.setRotationPoint(-.5F, 11F, 6.3F);
			temp.setTextureSize(64,32);
			temp.rotateAngleX=(float) Math.toRadians(-45f);
			parts.add(temp);
			temp = new ModelRenderer(this, 8, 4);
			temp.addBox(0F, 0.0F,-1.5F, 1, 1, 3);
			temp.setRotationPoint(-.5F, 15.3F, 1.8F);
			temp.setTextureSize(64,32);
			temp.rotateAngleX=(float) Math.toRadians(-45f);
			parts.add(temp);
			temp = new ModelRenderer(this, 8, 4);
			temp.addBox(0F, 0.0F,-1.5F, 1, 1, 3);
			temp.setRotationPoint(-.5F, 11F, 1.1F);
			temp.setTextureSize(64,32);
			temp.rotateAngleX=(float) Math.toRadians(45f);
			parts.add(temp);
			//CROSS
			temp = new ModelRenderer(this, 28, 0);
			temp.addBox(0F, 13.0F, 3.7F, 1, 6, 1);
			temp.setRotationPoint(-.5F,-2.5F,-.5F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			temp = new ModelRenderer(this, 28, 8);
			temp.addBox(0F, 13.6F, 3F, 1, 1, 6);
			temp.setRotationPoint(-.5F,-.5F,-2.5F);
			temp.setTextureSize(64,32);
			parts.add(temp);
			//WHEEL STAND
			temp = new ModelRenderer(this, 36, 0);
			temp.addBox(5F, 10F, 3.7F, 1, 7, 2);
			temp.setRotationPoint(-.5F, -4.0F, -1F);
			temp.setTextureSize(64,32);
			temp.rotateAngleZ = (float)Math.toRadians(15f);
			parts.add(temp);

			temp = new ModelRenderer(this, 28, 0);
			temp.addBox(0F, 9F, 13F, 1, 4, 1);
			temp.setRotationPoint(-.5F, 0.0F, -.5F);
			temp.setTextureSize(64,32);
			parts.add(temp);

			temp = new ModelRenderer(this, 0, 18);
			temp.addBox(0F, 11F, 13F, 2, 3, 2);
			temp.setRotationPoint(-1F, 0F, -1F);
			temp.setTextureSize(64,32);
			parts.add(temp);
		}

		@Override
		public void render(Entity ent, float par2, float par3, float par4, float par5, float par6, float par7)
		{
			for(ModelRenderer mr : parts)
				mr.render(par7);
		}
	}
}