package witchinggadgets.client.render;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.renderers.models.gear.ModelRobe;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntitySarcophagus;

public class TileRenderSarcophagus extends TileEntitySpecialRenderer
{
	static ModelSkeleton modelSkel = new ModelSkeleton();
	static ModelRobe modelRobe = new ModelRobe(.0625f);
	static{
		modelRobe.isChild = false;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntitySarcophagus tile = (TileEntitySarcophagus) tileentity;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotated(90, 1, 0, 0); 
		GL11.glRotated((tile.facing==2||tile.facing==3)?90:180, 0, 0, 1);
		
		GL11.glTranslated((tile.facing==2||tile.facing==3)?.5:-.5,0,-.25);
		this.bindTexture(new ResourceLocation("textures/entity/skeleton/skeleton.png"));
		if(!tile.dummyLeft && tile.dummyRight && tile.open)
		{
			GL11.glPushMatrix();
			modelSkel.bipedHead.render(.0625f);
			modelSkel.bipedBody.render(.0625f);
			modelSkel.bipedLeftLeg.render(.0625f);
			modelSkel.bipedRightLeg.render(.0625f);
			GL11.glTranslated(.03125,0,0);
			modelSkel.bipedLeftArm.render(.0625f);
			GL11.glTranslated(-.0625,0,0);
			modelSkel.bipedRightArm.render(.0625f);
			GL11.glPopMatrix();

			GL11.glTranslated(0,0,-.015625);
			GL11.glPushMatrix();
			GL11.glScalef(2,2,2);
			ClientUtilities.bindTexture("thaumcraft:textures/models/void_robe_armor_overlay.png");
			modelRobe.bipedHead.render(.03125f);
			GL11.glScalef(.75f,.75f,.75f);
			GL11.glTranslatef(0, .125f, 0);
			modelRobe.bipedBody.render(.03125f);
			modelRobe.bipedLeftLeg.render(.03125f);
			modelRobe.bipedRightLeg.render(.03125f);
			GL11.glTranslatef(0,-.125f, 0);
			modelRobe.bipedLeftArm.render(.03125f);
			modelRobe.bipedRightArm.render(.03125f);
			GL11.glPopMatrix();
			ClientUtilities.bindTexture("thaumcraft:textures/models/void_robe_armor.png");
			GL11.glScalef(2,2,2);
			modelRobe.bipedHead.render(.03125f);
			GL11.glScalef(.75f,.75f,.75f);
			GL11.glTranslatef(0, .125f, 0);
			modelRobe.bipedBody.render(.03125f);
			modelRobe.bipedLeftLeg.render(.03125f);
			modelRobe.bipedRightLeg.render(.03125f);
			GL11.glTranslatef(0,-.125f, 0);
			modelRobe.bipedLeftArm.render(.03125f);
			modelRobe.bipedRightArm.render(.03125f);
		}
		GL11.glPopMatrix();
	}
}