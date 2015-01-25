package witchinggadgets.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import travellersgear.api.ITravellersGear;

public class ModelPrimordialBaubles extends ModelBiped
{
	List<ModelRenderer> parts = new ArrayList();

	public ModelPrimordialBaubles(EntityLivingBase entity, ItemStack stack)
	{
		super(.01F, 0, 64, 32);
		this.bipedBody.isHidden=true;
		this.bipedHead.isHidden=true;
		this.bipedHeadwear.isHidden=true;
		this.bipedLeftLeg.isHidden=true;
		this.bipedRightLeg.isHidden=true;

		int slot = ((ITravellersGear)stack.getItem()).getSlot(stack);
		int meta = stack.getItemDamage();
		float sizeMod = slot==1?1.125f:.125f;
		
		int u = meta==1||meta==2?40: 24;
		int v = meta==2||meta==3?24: 16;
		int yOff = slot==2?7:0;
		
		this.boxList.clear();
		this.bipedRightArm = new ModelRenderer(this, u, v);
		this.bipedRightArm.addBox(-3.0F, -2.0F+yOff, -2.0F, 4, 4, 4, sizeMod);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		ModelRenderer mr = new ModelRenderer(this, 40,28);
		mr.addBox(-3.0F, -2.0F+yOff, 4.0F, 2,1,1, sizeMod);
		
		this.bipedLeftArm = new ModelRenderer(this, u, v);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F+yOff, -2.0F, 4, 4, 4, sizeMod);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	}

	static HashMap<Integer,ModelBiped> modelMap = new HashMap();
	public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack)
	{
		return new ModelPrimordialBaubles(entity,stack);
//		if(stack==null || !(stack.getItem() instanceof ITravellersGear))
//			return null;
//		int slot = ((ITravellersGear)stack.getItem()).getSlot(stack);
//		if(slot<0 || slot>2)
//			return null;
//		if(!modelMap.containsKey(stack.getItemDamage()))
//			modelMap.put(stack.getItemDamage(), new ModelPrimordialBaubles(entity,stack));
//		return modelMap.get(stack.getItemDamage());
	}
}