package witchinggadgets.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class ModelRobeSkirted extends ModelBiped
{
	List<ModelRenderer> parts = new ArrayList();

	public ModelRobeSkirted(EntityLivingBase entity)
	{
		super(.5F, 0, 128, 64);
		this.bipedHeadwear.showModel = false;
		if(entity!=null)
		{
			this.isSneak = entity.isSneaking();
			this.isRiding = entity.isRiding();
			this.isChild = entity.isChild();
			this.heldItemRight = (entity.getHeldItem() != null ? 1 : 0);
			if ((entity instanceof EntityPlayer))
				this.aimedBow = (((EntityPlayer)entity).getItemInUseDuration() > 0);
			if( ((EntityLivingBase)entity).getEquipmentInSlot(4)!=null && !( entity.getEquipmentInSlot(4).getUnlocalizedName().contains("goggles")||entity.getEquipmentInSlot(4).getUnlocalizedName().contains("Goggles")||entity.getEquipmentInSlot(4).getUnlocalizedName().contains("glasses") ) )
				this.bipedHead.showModel = false;
		}
		parts.clear();

		ModelRenderer temp;

		//ARM R
		this.bipedRightArm.cubeList.clear();
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 11, 4, .5f);
		temp = new ModelRenderer(this, 40,32);
		temp.addBox(-2.5F, 4.2F, 2F, 3, 5, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		temp.setTextureSize(128,64);
		setRotation(temp ,0,0,0);
		this.bipedRightArm.addChild(temp);
		temp = new ModelRenderer(this, 48,32);
		temp.addBox(-2F, 6.4F, 2f, 2, 3, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		temp.setTextureSize(128,64);
		setRotation(temp ,.125f,0,0);
		this.bipedRightArm.addChild(temp);

		//ARM L
		this.bipedLeftArm.cubeList.clear();
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 11, 4, .5f);
		temp = new ModelRenderer(this, 40,32);
		temp.addBox(-.5F, 4.2F, 2F, 3, 5, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		temp.setTextureSize(128,64);
		setRotation(temp ,0,0,0);
		this.bipedLeftArm.addChild(temp);
		temp = new ModelRenderer(this, 48,32);
		temp.addBox(-0F, 6.4F, 2f, 2, 3, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		temp.setTextureSize(128,64);
		setRotation(temp ,.125f,0,0);
		this.bipedLeftArm.addChild(temp);

		//HEAD
		temp = new ModelRenderer(this, 32,0);
		temp.addBox(-4F, -8F,-4f, 8, 8, 7, 1f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		temp.setTextureSize(128,64);
		this.bipedHead.addChild(temp);
		temp = new ModelRenderer(this, 64,0);
		temp.addBox(-4F, -7.8F,3f, 8, 6, 2, .5f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,-.0875f,0,0);
		this.bipedHead.addChild(temp);
		temp = new ModelRenderer(this, 64,8);
		temp.addBox(-3F, -7.5F,5f, 6, 6, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,-.125f,0,0);
		this.bipedHead.addChild(temp);

		//LEG R
		temp = new ModelRenderer(this, 4,32);
		temp.addBox(-.5F, 0F,1.4f, 2, 8, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,.125f,0,0);
		this.bipedRightLeg.addChild(temp);
		temp = new ModelRenderer(this, 0,32);
		temp.addBox(-2F, 0F,1.4f, 1, 7, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,.125f,0,0);
		this.bipedRightLeg.addChild(temp);
		temp = new ModelRenderer(this, 10,32);
		temp.addBox(-2F, 12.5F,-2f, 1, 6, 4, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,0,0,.1875f);
		this.bipedBody.addChild(temp);
		temp = new ModelRenderer(this, 10,32);
		temp.addBox(-4.3F, 11F,-2f, 1, 1, 4, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addChild(temp);

		//LEG L
		temp = new ModelRenderer(this, 4,32);
		temp.addBox(-1.5F, 0F,1.4f, 2, 8, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,.125f,0,0);
		this.bipedLeftLeg.addChild(temp);
		temp = new ModelRenderer(this, 0,32);
		temp.addBox(1F, 0F,1.4f, 1, 7, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,.125f,0,0);
		this.bipedLeftLeg.addChild(temp);
		temp = new ModelRenderer(this, 10,32);
		temp.addBox(1F, 12.5F,-2f, 1, 6, 4, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotation(temp ,0,0,-.1875f);
		this.bipedBody.addChild(temp);
		temp = new ModelRenderer(this, 10,32);
		temp.addBox(3.3F, 11F,-2f, 1, 1, 4, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addChild(temp);

		temp = new ModelRenderer(this, 64,8);
		temp.addBox(-3F, 4F,1f, 6, 8, 1, .25f);
		temp.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addChild(temp);
	}

	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		if (((entity instanceof EntitySkeleton)) || ((entity instanceof EntityZombie)))
		{
			float f6 = MathHelper.sin(this.onGround * 3.141593F);
			float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * 3.141593F);
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = (-(0.1F - f6 * 0.6F));
			this.bipedLeftArm.rotateAngleY = (0.1F - f6 * 0.6F);
			this.bipedRightArm.rotateAngleX = -1.570796F;
			this.bipedLeftArm.rotateAngleX = -1.570796F;
			this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(par4 * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par4 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(par4 * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par4 * 0.067F) * 0.05F;
		}

		if (this.isChild)
		{
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
			GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
			this.bipedHead.render(par7);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
			this.bipedBody.render(par7);
			this.bipedRightArm.render(par7);
			this.bipedLeftArm.render(par7);
			this.bipedRightLeg.render(par7);
			this.bipedLeftLeg.render(par7);
			this.bipedHeadwear.render(par7);
			GL11.glPopMatrix();
		}
		else
		{
			this.bipedHead.render(par7);
			this.bipedBody.render(par7);
			this.bipedRightArm.render(par7);
			this.bipedLeftArm.render(par7);
			this.bipedRightLeg.render(par7);
			this.bipedLeftLeg.render(par7);
			this.bipedHeadwear.render(par7);
		}
	}

	void setRotation(ModelRenderer mr, float x, float y, float z)
	{
		mr.rotateAngleX = x;
		mr.rotateAngleY = y;
		mr.rotateAngleZ = z;
	}

}