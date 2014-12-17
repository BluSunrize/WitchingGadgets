package witchinggadgets.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import travellersgear.api.ITravellersGear;

public class ModelMagicalBaubles extends ModelBiped
{
	List<ModelRenderer> parts = new ArrayList();

	public ModelMagicalBaubles(EntityLivingBase entity, ItemStack stack)
	{
		super(.01F, 0, 64, 32);
		if(stack==null || !(stack.getItem() instanceof ITravellersGear))
			return;
		this.bipedBody.isHidden=true;
		this.bipedHead.isHidden=true;
		this.bipedHeadwear.isHidden=true;
		this.bipedLeftLeg.isHidden=true;
		this.bipedRightLeg.isHidden=true;
		
		int slot = ((ITravellersGear)stack.getItem()).getSlot(stack);
		float sizeMod = slot==1?1.125f:.125f;
		this.boxList.clear();
		this.bipedRightArm = new ModelRenderer(this, 40, slot==1?16:24);
		this.bipedRightArm.addBox(-3.0F, -2.0F+(slot==2?7:0), -2.0F, 4, 4, 4, sizeMod);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, slot==1?16:24);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F+(slot==2?7:0), -2.0F, 4, 4, 4, sizeMod);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	}

	static HashMap<Integer,ModelBiped> modelMap = new HashMap();
	public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack)
	{
		if(!modelMap.containsKey(stack.getItemDamage()))
			modelMap.put(stack.getItemDamage(), new ModelMagicalBaubles(entity,stack));
		return modelMap.get(stack.getItemDamage());
	}
}