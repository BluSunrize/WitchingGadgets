package witchinggadgets.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import witchinggadgets.api.ITerraformFocus;
import witchinggadgets.client.ClientProxy;
import witchinggadgets.client.ClientUtilities;

public class TileRenderTerraformFocus extends TileEntitySpecialRenderer
{
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();

		GL11.glTranslatef((float)x+.5f, (float)y-1f, (float)z+.5f);

		if(ClientProxy.terraformerModel != null)
		{
			ClientUtilities.bindTexture("witchinggadgets:textures/models/terraformer.png");			ClientProxy.terraformerModel.renderPart("focus_03");
			if(tile!=null && tile.getWorldObj()!=null && tile.getBlockType() instanceof ITerraformFocus)
			{
				Aspect a = ((ITerraformFocus)tile.getBlockType()).requiredAspect(tile.getWorldObj(),tile.xCoord,tile.yCoord,tile.zCoord);
				if(a!=null)
					GL11.glColor3f((a.getColor()>>16&255)/255f, (a.getColor()>>8&255)/255f, (a.getColor()&255)/255f);
			}
			else if(tile!=null && tile.blockMetadata>-1 && tile.blockType instanceof ITerraformFocus)
			{
				Aspect a =  ((ITerraformFocus)tile.blockType).requiredAspect(tile.blockMetadata);
				GL11.glDisable(GL11.GL_LIGHTING);
				if(a!=null)
					GL11.glColor3f((a.getColor()>>16&255)/255f, (a.getColor()>>8&255)/255f, (a.getColor()&255)/255f);
			}

			ClientProxy.terraformerModel.renderPart("focus_crystal_04");
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor3f(1, 1, 1);

			if(tile!=null && tile.getWorldObj()!=null && tile.getBlockType() instanceof ITerraformFocus)
			{
				ClientUtilities.bindTexture("textures/atlas/blocks.png");
				ItemStack stack = ((ITerraformFocus)tile.getBlockType()).getDisplayedBlock(tile.getWorldObj(),tile.xCoord,tile.yCoord,tile.zCoord);
				Block b = Block.getBlockFromItem(stack.getItem());
				if(b!=null)
				{
					float rot = RenderManager.instance.livingPlayer.ticksExisted%40 / 40f;
					GL11.glTranslatef(0,1.3125f,0);
					GL11.glScalef(.25f, .25f, .25f);
					GL11.glRotatef(rot*360, 0, 1, 0);
					GL11.glRotatef(rot*360, 0, 0, 1);
					RenderBlocks.getInstance().renderBlockAsItem(b, stack.getItemDamage(), .75f);
				}
			}
		}
		GL11.glPopMatrix();
	}

}