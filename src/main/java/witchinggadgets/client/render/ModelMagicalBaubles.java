package witchinggadgets.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import travellersgear.api.ITravellersGear;
import witchinggadgets.common.items.baubles.ItemMagicalBaubles;

public class ModelMagicalBaubles extends ModelBiped
{
	List<ModelRenderer> parts = new ArrayList();

	public ModelMagicalBaubles(EntityLivingBase entity, ItemStack stack)
	{
		super(.01F, 0, 64, 32);
		this.bipedBody.isHidden=true;
		this.bipedHead.isHidden=true;
		this.bipedHeadwear.isHidden=true;
		this.bipedLeftLeg.isHidden=true;
		this.bipedRightLeg.isHidden=true;

		int slot = ((ITravellersGear)stack.getItem()).getSlot(stack);
		int meta = stack.getItemDamage();
		float sizeMod = stack.getItemDamage()==6?.5f: slot==1?1.125f: .125f;

		int u = meta==1||meta==2?40: 24;
		int v = meta==2||meta==3?24: 16;
		int yOff = slot==2?7:0;

		this.boxList.clear();
		if(stack.getItemDamage()==6)
		{
			this.bipedBody = new ModelRenderer(this, 0, 0);
			this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 7, 4, sizeMod);
			this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
			bipedRightArm.isHidden=true;
			bipedLeftArm.isHidden=true;
		}
		else
		{
			this.bipedRightArm = new ModelRenderer(this, u, v);
			this.bipedRightArm.addBox(-3.0F, -2.0F+yOff, -2.0F, 4, 4, 4, sizeMod);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
			this.bipedLeftArm = new ModelRenderer(this, u, v);
			this.bipedLeftArm.mirror = true;
			this.bipedLeftArm.addBox(-1.0F, -2.0F+yOff, -2.0F, 4, 4, 4, sizeMod);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		}
	}

	static ModelBiped[] modelMap = new ModelBiped[ItemMagicalBaubles.subNames.length];
	public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack)
	{
		if(stack==null || !(stack.getItem() instanceof ITravellersGear))
			return null;
		int slot = ((ITravellersGear)stack.getItem()).getSlot(stack);
		if(slot<1 || slot>2)
			return null;
		if(modelMap[stack.getItemDamage()]==null)
			modelMap[stack.getItemDamage()] = new ModelMagicalBaubles(entity,stack);
		return modelMap[stack.getItemDamage()];
	}
}