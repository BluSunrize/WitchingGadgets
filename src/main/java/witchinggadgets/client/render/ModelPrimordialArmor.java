package witchinggadgets.client.render;

import java.util.HashMap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

/**
 * Created using Tabula 4.1.0
 */
public class ModelPrimordialArmor extends ModelBiped
{
	public ModelRenderer ll_plate_o0;
	public ModelRenderer ll_plate_o1;
	public ModelRenderer ll_plate_b0;
	public ModelRenderer ll_plate_b1;
	public ModelRenderer ll_plate_f0;
	public ModelRenderer ll_plate_f1;
	public ModelRenderer ll_plate_f2;
	public ModelRenderer ll_plate_f3;
	public ModelRenderer lb_base;
	public ModelRenderer ll_chainmail;
	public ModelRenderer lb_front;
	public ModelRenderer lb_side0;
	public ModelRenderer lb_side1;
	public ModelRenderer rl_plate_o0;
	public ModelRenderer rl_plate_o1;
	public ModelRenderer rl_plate_b0;
	public ModelRenderer rl_plate_b1;
	public ModelRenderer rl_plate_f0;
	public ModelRenderer rl_plate_f1;
	public ModelRenderer rl_plate_f2;
	public ModelRenderer rl_plate_f3;
	public ModelRenderer rb_base;
	public ModelRenderer rl_chainmail;
	public ModelRenderer rb_front;
	public ModelRenderer rb_side0;
	public ModelRenderer rb_side1;
	public ModelRenderer la_plate0;
	public ModelRenderer la_plate1;
	public ModelRenderer la_shoulder0;
	public ModelRenderer la_shoulder1;
	public ModelRenderer la_shoulder2;
	public ModelRenderer la_shoulder3;
	public ModelRenderer la_shoulder4;
	public ModelRenderer ra_plate0;
	public ModelRenderer ra_plate1;
	public ModelRenderer ra_shoulder0;
	public ModelRenderer ra_shoulder1;
	public ModelRenderer ra_shoulder2;
	public ModelRenderer ra_shoulder3;
	public ModelRenderer ra_shoulder4;
	public ModelRenderer bd_belt;
	public ModelRenderer bd_chainmail;
	public ModelRenderer bd_chest;
	public ModelRenderer bd_strap0;
	public ModelRenderer bd_strap1;
	public ModelRenderer bd_strap2;
	public ModelRenderer bd_add0;
	public ModelRenderer bd_add1;
	public ModelRenderer bd_add2;
	public ModelRenderer bd_add3;
	public ModelRenderer bd_back;
	public ModelRenderer bd_strap3;
	public ModelRenderer bd_strap4;
	public ModelRenderer bd_strap5;
	public ModelRenderer bd_add4;
	public ModelRenderer bd_add5;
	public ModelRenderer bd_add6;
	public ModelRenderer bd_add7;
	public ModelRenderer hm_top;
	public ModelRenderer hm_l0;
	public ModelRenderer hm_r0;
	public ModelRenderer hm_b0;
	public ModelRenderer hm_b1;
	public ModelRenderer hm_f0;
	public ModelRenderer face_0;
	public ModelRenderer face_mask;
	public ModelRenderer face_goggles;
	public ModelRenderer face_l1;
	public ModelRenderer face_l2;
	public ModelRenderer face_r1;
	public ModelRenderer face_r2;

	public ModelPrimordialArmor(ItemStack stack)
	{
		super(((ItemArmor)stack.getItem()).armorType==3?.3125f:.0625f, 0, 128, 64);
		float scale = ((ItemArmor)stack.getItem()).armorType==3?.3125f:.0625f;
		this.bipedHeadwear.setTextureOffset(0, 0);
		this.bipedBody.cubeList.clear();

		int mask = stack.hasTagCompound()&&stack.getTagCompound().hasKey("mask")?stack.getTagCompound().getInteger("mask"):-1;
		boolean goggles= stack.hasTagCompound()&&stack.getTagCompound().hasKey("goggles");
		
		this.bd_chainmail = new ModelRenderer(this, 16, 16);
		this.bd_chainmail.setRotationPoint(0.0F, 0.0F + scale, 0.0F);
		this.bd_chainmail.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
		this.bd_chest = new ModelRenderer(this, 66, 31);
		this.bd_chest.setRotationPoint(0.0F, 1.0F, -2.5F);
		this.bd_chest.addBox(-3.5F, 0.0F, -0.5F, 7, 9, 2, scale);

		this.bd_add0 = new ModelRenderer(this, 66, 46);
		this.bd_add0.setRotationPoint(0.0F, 0.0F, -3.3F);
		this.bd_add0.addBox(-2.0F, 0.0F, -1.0F, 4, 4, 2, scale);
		this.setRotateAngle(bd_add0, 0.3141592653589793F, 0.0F, 0.0F);

		this.bd_add1 = new ModelRenderer(this, 66, 52);
		this.bd_add1.mirror = true;
		this.bd_add1.setRotationPoint(2.5F, 8.7F, -2.5F);
		this.bd_add1.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1, scale);
		this.setRotateAngle(bd_add1, -0.2617993877991494F, -0.3490658503988659F, 0.0F);
		this.bd_add2 = new ModelRenderer(this, 66, 52);
		this.bd_add2.setRotationPoint(-2.5F, 8.7F, -2.5F);
		this.bd_add2.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1, scale);
		this.setRotateAngle(bd_add2, -0.2617993877991494F, 0.3490658503988659F, 0.0F);
		this.bd_add3 = new ModelRenderer(this, 66, 52);
		this.bd_add3.setRotationPoint(0.0F, 8.7F, -2.9F);
		this.bd_add3.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1, scale);
		this.setRotateAngle(bd_add3, -0.2617993877991494F, 0.0F, 0.0F);

		this.bd_strap0 = new ModelRenderer(this, 66, 42);
		this.bd_strap0.setRotationPoint(0.0F, 6.7F, -2.0F);
		this.bd_strap0.addBox(-4.0F, 0.0F, -0.5F, 8, 2, 1, scale);
		this.bd_strap1 = new ModelRenderer(this, 66, 42);
		this.bd_strap1.mirror = true;
		this.bd_strap1.setRotationPoint(2.8F, -0.4F, -2.0F);
		this.bd_strap1.addBox(-1.0F, 0.0F, -0.5F, 2, 2, 1, scale);
		this.setRotateAngle(bd_strap1, 0.0F, 0.0F, 0.39269908169872414F);
		this.bd_strap2 = new ModelRenderer(this, 66, 42);
		this.bd_strap2.setRotationPoint(-2.8F, -0.4F, -2.0F);
		this.bd_strap2.addBox(-1.0F, 0.0F, -0.5F, 2, 2, 1, scale);
		this.setRotateAngle(bd_strap2, 0.0F, 0.0F, -0.39269908169872414F);


		this.bd_back = new ModelRenderer(this, 80, 46);
		this.bd_back.setRotationPoint(0.0F, 1.0F, 1.5F);
		this.bd_back.addBox(-3.5F, 0.0F, -0.5F, 7, 9, 2, scale);
		this.bd_strap3 = new ModelRenderer(this, 66, 42);
		this.bd_strap3.setRotationPoint(0.0F, 5.7F, 2.0F);
		this.bd_strap3.addBox(-4.0F, 0.0F, -0.5F, 8, 2, 1, scale);
		this.bd_strap4 = new ModelRenderer(this, 66, 42);
		this.bd_strap4.mirror = true;
		this.bd_strap4.setRotationPoint(2.8F, -0.4F, 2.0F);
		this.bd_strap4.addBox(-1.0F, 0.0F, -0.5F, 2, 2, 1, scale);
		this.setRotateAngle(bd_strap4, 0.0F, 0.0F, 0.39269908169872414F);
		this.bd_strap5 = new ModelRenderer(this, 66, 42);
		this.bd_strap5.setRotationPoint(-2.8F, -0.4F, 2.0F);
		this.bd_strap5.addBox(-1.0F, 0.0F, -0.5F, 2, 2, 1, scale);
		this.setRotateAngle(bd_strap5, 0.0F, 0.0F, -0.39269908169872414F);

		this.bd_add4 = new ModelRenderer(this, 84, 40);
		this.bd_add4.setRotationPoint(-2.0F, 7.9F, 2.3F);
		this.bd_add4.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(bd_add4, 0.30019663134302466F, -0.13962634015954636F, 0.0F);
		this.bd_add5 = new ModelRenderer(this, 84, 40);
		this.bd_add5.setRotationPoint(-2.0F, 9.8F, 2.1F);
		this.bd_add5.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(bd_add5, 0.30019663134302466F, -0.13962634015954636F, 0.0F);
		this.bd_add6 = new ModelRenderer(this, 84, 40);
		this.bd_add6.mirror = true;
		this.bd_add6.setRotationPoint(2.0F, 7.9F, 2.3F);
		this.bd_add6.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(bd_add6, 0.30019663134302466F, 0.13962634015954636F, 0.0F);
		this.bd_add7 = new ModelRenderer(this, 84, 40);
		this.bd_add7.mirror = true;
		this.bd_add7.setRotationPoint(2.0F, 9.8F, 2.1F);
		this.bd_add7.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(bd_add7, 0.30019663134302466F, 0.13962634015954636F, 0.0F);
		this.bd_belt = new ModelRenderer(this, 66, 56);
		this.bd_belt.setRotationPoint(0.0F, 10.2F, 0.0F);
		this.bd_belt.addBox(-4.0F, 0.0F, -2.0F, 8, 2, 4, scale);


		this.la_shoulder0 = new ModelRenderer(this, 42, 41);
		this.la_shoulder0.mirror = true;
		this.la_shoulder0.setRotationPoint(0.7F, -0.5F, 0.0F);
		this.la_shoulder0.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, scale);
		this.la_shoulder1 = new ModelRenderer(this, 42, 51);
		this.la_shoulder1.setRotationPoint(2.5F, -3.3F, 0.0F);
		this.la_shoulder1.addBox(-0.5F, 0.0F, -2.5F, 1, 5, 5, scale);
		this.setRotateAngle(la_shoulder1, 0.0F, 0.0F, -0.323323129985824F);
		this.la_shoulder2 = new ModelRenderer(this, 42, 51);
		this.la_shoulder2.mirror = true;
		this.la_shoulder2.setRotationPoint(2.5F, -.5F, 0.0F);
		this.la_shoulder2.addBox(-0.5F, 0.0F, -2.5F, 1, 5, 5, scale);
		this.setRotateAngle(la_shoulder2, 0.0F, 0.0F, -0.20943951023931953F);
		this.la_shoulder3 = new ModelRenderer(this, 42, 62);
		this.la_shoulder3.mirror = true;
		this.la_shoulder3.setRotationPoint(-1.0F, -3.2F, 0.0F);
		this.la_shoulder3.addBox(-0.5F, 0.0F, -0.5F, 4, 1, 1, scale);
		this.setRotateAngle(la_shoulder3, 0.0F, 0.0F, -0.07504915783575616F);
		this.la_shoulder4 = new ModelRenderer(this, 42, 62);
		this.la_shoulder4.mirror = true;
		this.la_shoulder4.setRotationPoint(-1.1F, -2.8F, 0.0F);
		this.la_shoulder4.addBox(-0.1F, -0.5F, -0.5F, 3, 1, 1, scale);
		this.setRotateAngle(la_shoulder4, 0.0F, 0.0F, -0.5026548245743669F);
		this.la_plate0 = new ModelRenderer(this, 42, 32);
		this.la_plate0.mirror = true;
		this.la_plate0.setRotationPoint(3.0F, 5.5F, 0.0F);
		this.la_plate0.addBox(-1.5F, 0.0F, -2.5F, 2, 4, 5, scale);
		this.la_plate1 = new ModelRenderer(this, 56, 32);
		this.la_plate1.mirror = true;
		this.la_plate1.setRotationPoint(2.5F, 3.7F, 0.0F);
		this.la_plate1.addBox(-0.5F, 0.0F, -2.0F, 1, 2, 4, scale);
		this.setRotateAngle(la_plate1, 0.0F, 0.0F, -0.17453292519943295F);

		this.ra_shoulder0 = new ModelRenderer(this, 42, 41);
		this.ra_shoulder0.setRotationPoint(-0.7F, -0.5F, 0.0F);
		this.ra_shoulder0.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, scale);
		this.ra_shoulder1 = new ModelRenderer(this, 42, 51);
		this.ra_shoulder1.setRotationPoint(-2.5F, -3.3F, 0.0F);
		this.ra_shoulder1.addBox(-0.5F, 0.0F, -2.5F, 1, 5, 5, scale);
		this.setRotateAngle(ra_shoulder1, 0.0F, 0.0F, 0.323323129985824F);
		this.ra_shoulder2 = new ModelRenderer(this, 42, 51);
		this.ra_shoulder2.setRotationPoint(-2.5F, -.5F, 0.0F);
		this.ra_shoulder2.addBox(-0.5F, 0.0F, -2.5F, 1, 5, 5, scale);
		this.setRotateAngle(ra_shoulder2, 0.0F, 0.0F, 0.20943951023931953F);
		this.ra_shoulder3 = new ModelRenderer(this, 42, 62);
		this.ra_shoulder3.setRotationPoint(1.0F, -3.2F, 0.0F);
		this.ra_shoulder3.addBox(-3.5F, 0.0F, -0.5F, 4, 1, 1, scale);
		this.setRotateAngle(ra_shoulder3, 0.0F, 0.0F, 0.07504915783575616F);
		this.ra_shoulder4 = new ModelRenderer(this, 42, 62);
		this.ra_shoulder4.setRotationPoint(1.1F, -2.8F, 0.0F);
		this.ra_shoulder4.addBox(-2.9F, -0.5F, -0.5F, 3, 1, 1, scale);
		this.setRotateAngle(ra_shoulder4, 0.0F, 0.0F, 0.5026548245743669F);
		this.ra_plate0 = new ModelRenderer(this, 42, 32);
		this.ra_plate0.setRotationPoint(-2.0F, 5.5F, 0.0F);
		this.ra_plate0.addBox(-1.5F, 0.0F, -2.5F, 2, 4, 5, scale);
		this.ra_plate1 = new ModelRenderer(this, 56, 32);
		this.ra_plate1.setRotationPoint(-2.5F, 3.7F, 0.0F);
		this.ra_plate1.addBox(-0.5F, 0.0F, -2.0F, 1, 2, 4, scale);
		this.setRotateAngle(ra_plate1, 0.0F, 0.0F, 0.17453292519943295F);

		this.ll_chainmail = new ModelRenderer(this, 26, 32);
		this.ll_chainmail.mirror = true;
		this.ll_chainmail.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.ll_chainmail.addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4, scale);
		this.ll_plate_f0 = new ModelRenderer(this, 0, 32);
		this.ll_plate_f0.mirror = true;
		this.ll_plate_f0.setRotationPoint(0.4F, 0.2F, -1.7F);
		this.ll_plate_f0.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(ll_plate_f0, -0.17453292519943295F, 0.0F, 0.0F);
		this.ll_plate_f1 = new ModelRenderer(this, 0, 32);
		this.ll_plate_f1.mirror = true;
		this.ll_plate_f1.setRotationPoint(0.0F, 3.0F, -1.7F);
		this.ll_plate_f1.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(ll_plate_f1, -0.17453292519943295F, 0.0F, 0.0F);
		this.ll_plate_f2 = new ModelRenderer(this, 10, 32);
		this.ll_plate_f2.mirror = true;
		this.ll_plate_f2.setRotationPoint(0.0F, 0.0F, -1.7F);
		this.ll_plate_f2.addBox(-2.0F, 0.0F, -0.7F, 2, 5, 1, scale);
		this.setRotateAngle(ll_plate_f2, -0.17453292519943295F, 0.0F, 0.0F);
		this.ll_plate_f3 = new ModelRenderer(this, 10, 32);
		this.ll_plate_f3.mirror = true;
		this.ll_plate_f3.setRotationPoint(0.0F, 3.0F, -1.7F);
		this.ll_plate_f3.addBox(-2.0F, 0.0F, -0.7F, 2, 5, 1, scale);
		this.setRotateAngle(ll_plate_f3, -0.17453292519943295F, 0.0F, 0.0F);
		this.ll_plate_o0 = new ModelRenderer(this, 16, 32);
		this.ll_plate_o0.mirror = true;
		this.ll_plate_o0.setRotationPoint(1.8F, 0.2F, 0.0F);
		this.ll_plate_o0.addBox(-0.5F, 0.0F, -2.0F, 1, 4, 4, scale);
		this.setRotateAngle(ll_plate_o0, 0.0F, 0.0F, -0.3141592653589793F);
		this.ll_plate_o1 = new ModelRenderer(this, 16, 32);
		this.ll_plate_o1.mirror = true;
		this.ll_plate_o1.setRotationPoint(1.9F, 3.0F, 0.0F);
		this.ll_plate_o1.addBox(-0.5F, 0.0F, -2.0F, 1, 4, 4, scale);
		this.setRotateAngle(ll_plate_o1, 0.0F, 0.0F, -0.24434609527920614F);
		this.ll_plate_b0 = new ModelRenderer(this, 0, 32);
		this.ll_plate_b0.mirror = true;
		this.ll_plate_b0.setRotationPoint(0.0F, 0.2F, 2.0F);
		this.ll_plate_b0.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(ll_plate_b0, 0.17453292519943295F, 0.0F, 0.0F);
		this.ll_plate_b1 = new ModelRenderer(this, 0, 32);
		this.ll_plate_b1.mirror = true;
		this.ll_plate_b1.setRotationPoint(0.0F, 3.0F, 2.0F);
		this.ll_plate_b1.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(ll_plate_b1, 0.17453292519943295F, 0.0F, 0.0F);

		this.rl_chainmail = new ModelRenderer(this, 26, 32);
		this.rl_chainmail.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rl_chainmail.addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4, scale);
		this.rl_plate_f0 = new ModelRenderer(this, 0, 32);
		this.rl_plate_f0.setRotationPoint(-.4F, 0.2F, -1.7F);
		this.rl_plate_f0.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(rl_plate_f0, -0.17453292519943295F, 0.0F, 0.0F);
		this.rl_plate_f1 = new ModelRenderer(this, 0, 32);
		this.rl_plate_f1.setRotationPoint(0.0F, 3.0F, -1.7F);
		this.rl_plate_f1.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(rl_plate_f1, -0.17453292519943295F, 0.0F, 0.0F);
		this.rl_plate_f2 = new ModelRenderer(this, 10, 32);
		this.rl_plate_f2.setRotationPoint(0.0F, 0.0F, -1.7F);
		this.rl_plate_f2.addBox(0.0F, 0.0F, -0.7F, 2, 5, 1, scale);
		this.setRotateAngle(rl_plate_f2, -0.17453292519943295F, 0.0F, 0.0F);
		this.rl_plate_f3 = new ModelRenderer(this, 10, 32);
		this.rl_plate_f3.setRotationPoint(0.0F, 3.0F, -1.7F);
		this.rl_plate_f3.addBox(0.0F, 0.0F, -0.7F, 2, 5, 1, scale);
		this.setRotateAngle(rl_plate_f3, -0.17453292519943295F, 0.0F, 0.0F);
		this.rl_plate_o0 = new ModelRenderer(this, 16, 32);
		this.rl_plate_o0.setRotationPoint(-1.8F, 0.2F, 0.0F);
		this.rl_plate_o0.addBox(-0.5F, 0.0F, -2.0F, 1, 4, 4, scale);
		this.setRotateAngle(rl_plate_o0, 0.0F, 0.0F, 0.3141592653589793F);
		this.rl_plate_o1 = new ModelRenderer(this, 16, 32);
		this.rl_plate_o1.setRotationPoint(-1.9F, 3.0F, 0.0F);
		this.rl_plate_o1.addBox(-0.5F, 0.0F, -2.0F, 1, 4, 4, scale);
		this.setRotateAngle(rl_plate_o1, 0.0F, 0.0F, 0.24434609527920614F);
		this.rl_plate_b0 = new ModelRenderer(this, 0, 32);
		this.rl_plate_b0.setRotationPoint(0.0F, 0.2F, 2.0F);
		this.rl_plate_b0.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(rl_plate_b0, 0.17453292519943295F, 0.0F, 0.0F);
		this.rl_plate_b1 = new ModelRenderer(this, 0, 32);
		this.rl_plate_b1.setRotationPoint(0.0F, 3.0F, 2.0F);
		this.rl_plate_b1.addBox(-2.0F, 0.0F, -0.5F, 4, 4, 1, scale);
		this.setRotateAngle(rl_plate_b1, 0.17453292519943295F, 0.0F, 0.0F);


		this.lb_base = new ModelRenderer(this, 0, 40);
		this.lb_base.mirror = true;
		this.lb_base.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.lb_base.addBox(-2.0F, -.5F, -2.0F, 4, 5, 4, scale);
		this.lb_front = new ModelRenderer(this, 0, 50);
		this.lb_front.mirror = true;
		this.lb_front.setRotationPoint(0.0F, 3.0F, -2.8F);
		this.lb_front.addBox(-2.0F, .5F, 0.0F, 4, 1, 1, scale);
		this.lb_side0 = new ModelRenderer(this, 10, 50);
		this.lb_side0.mirror = true;
		this.lb_side0.setRotationPoint(2.0F, 3.0F, -2.1F);
		this.lb_side0.addBox(-0.5F, .5F, 0.0F, 1, 1, 4, scale);
		this.lb_side1 = new ModelRenderer(this, 20, 50);
		this.lb_side1.mirror = true;
		this.lb_side1.setRotationPoint(2.0F, 2.2F, -1.1F);
		this.lb_side1.addBox(-0.5F, .5F, 0.0F, 1, 1, 3, scale);

		this.rb_base = new ModelRenderer(this, 0, 40);
		this.rb_base.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.rb_base.addBox(-2.0F, -.5F, -2.0F, 4, 5, 4, scale);
		this.rb_front = new ModelRenderer(this, 0, 50);
		this.rb_front.setRotationPoint(0.0F, 3.0F, -2.8F);
		this.rb_front.addBox(-2.0F, .5F, 0.0F, 4, 1, 1, scale);
		this.rb_side0 = new ModelRenderer(this, 10, 50);
		this.rb_side0.setRotationPoint(-2.0F, 3.0F, -2.1F);
		this.rb_side0.addBox(-0.5F, .5F, 0.0F, 1, 1, 4, scale);
		this.rb_side1 = new ModelRenderer(this, 20, 50);
		this.rb_side1.setRotationPoint(-2.0F, 2.2F, -1.1F);
		this.rb_side1.addBox(-0.5F, .5F, 0.0F, 1, 1, 3, scale);

		this.hm_top = new ModelRenderer(this, 32, 0);
		this.hm_top.setRotationPoint(-0.5F, -1.0F, -0.5F);
		this.hm_top.addBox(-4.0F, -8.0F, -4.0F, 9, 4, 9, 0.0F);
		this.hm_l0 = new ModelRenderer(this, 68, 0);
		this.hm_l0.mirror = true;
		this.hm_l0.setRotationPoint(4.1F, -6.3F, -1.2F);
		this.hm_l0.addBox(-0.5F, 0.0F, -4.0F, 1, 4, 10, 0.0F);
		this.setRotateAngle(hm_l0, 0.0F, 0.0F, -0.39269908169872414F);
		this.hm_r0 = new ModelRenderer(this, 68, 0);
		this.hm_r0.setRotationPoint(-4.1F, -6.3F, -1.2F);
		this.hm_r0.addBox(-0.5F, 0.0F, -4.0F, 1, 4, 10, 0.0F);
		this.setRotateAngle(hm_r0, 0.0F, 0.0F, 0.39269908169872414F);
		this.hm_b0 = new ModelRenderer(this, 68, 14);
		this.hm_b0.setRotationPoint(0.0F, -4.0F, 3.7F);
		this.hm_b0.addBox(-4.5F, 0.0F, -0.5F, 9, 4, 1, 0.0F);
		this.setRotateAngle(hm_b0, 0.39269908169872414F, 0.0F, 0.0F);
		this.hm_b1 = new ModelRenderer(this, 68, 14);
		this.hm_b1.setRotationPoint(0.0F, -6.3F, 4.1F);
		this.hm_b1.addBox(-4.5F, 0.0F, -0.5F, 9, 4, 1, 0.0F);
		this.setRotateAngle(hm_b1, 0.39269908169872414F, 0.0F, 0.0F);
		this.hm_f0 = new ModelRenderer(this, 68, 19);
		this.hm_f0.setRotationPoint(-0.5F, -6.5F, -4.7F);
		this.hm_f0.addBox(-4.0F, -0.0F, -0.5F, 9, 1, 1, 0.0F);

		this.face_0 = new ModelRenderer(this, 102, 0);
		this.face_0.setRotationPoint(0.0F, -2.8F, -4.1F);
		this.face_0.addBox(-1.0F, 0.0F, -0.5F, 2, 3, 1, 0.0F);
		this.setRotateAngle(face_0, 0.08726646259971647F, 0.0F, 0.0F);
		this.face_l1 = new ModelRenderer(this, 90, 0);
		this.face_l1.mirror = true;
		this.face_l1.setRotationPoint(3.4F, 0.0F, 0.2F);
		this.face_l1.addBox(-1.0F, 0.0F, -0.5F, 2, 3, 4, 0.0F);
		this.setRotateAngle(face_l1, -0.08726646259971647F, 0.0F, 0.0F);
		this.face_l2 = new ModelRenderer(this, 102, 4);
		this.face_l2.mirror = true;
		this.face_l2.setRotationPoint(1.7F, 0.9F, 0.2F);
		this.face_l2.addBox(-1.0F, 0.0F, -0.5F, 2, 2, 1, 0.0F);
		this.setRotateAngle(face_l2, -0.08726646259971647F, 0.0F, 0.0F);
		this.face_r1 = new ModelRenderer(this, 90, 0);
		this.face_r1.setRotationPoint(-3.4F, 0.0F, 0.2F);
		this.face_r1.addBox(-1.0F, 0.0F, -0.5F, 2, 3, 4, 0.0F);
		this.setRotateAngle(face_r1, -0.08726646259971647F, 0.0F, 0.0F);
		this.face_r2 = new ModelRenderer(this, 102, 4);
		this.face_r2.setRotationPoint(-1.7F, 0.9F, 0.2F);
		this.face_r2.addBox(-1.0F, 0.0F, -0.5F, 2, 2, 1, 0.0F);
		this.face_mask = new ModelRenderer(this, 90, 8+mask*7);
		this.face_mask.setRotationPoint(0.0F, -5.0F, -3.6F);
		this.face_mask.addBox(-4.0F, 0.0F, -0.5F, 8, 4, 3, 0.0F);
		this.face_goggles = new ModelRenderer(this, 112, 8);
		this.face_goggles.setRotationPoint(0.0F, -5.0F, -3.5F);
		this.face_goggles.addBox(-4.0F, 0.0F, -0.5F, 8, 4, 3, 0.0F);

		this.bipedBody.addChild(this.bd_chainmail);
		this.bd_chainmail.addChild(this.bd_chest);
		this.bd_chainmail.addChild(this.bd_add0);
		this.bd_chainmail.addChild(this.bd_add1);
		this.bd_chainmail.addChild(this.bd_add2);
		this.bd_chainmail.addChild(this.bd_add3);
		this.bd_chainmail.addChild(this.bd_strap0);
		this.bd_chainmail.addChild(this.bd_strap1);
		this.bd_chainmail.addChild(this.bd_strap2);
		this.bd_chainmail.addChild(this.bd_back);
		this.bd_chainmail.addChild(this.bd_add4);
		this.bd_chainmail.addChild(this.bd_add5);
		this.bd_chainmail.addChild(this.bd_add6);
		this.bd_chainmail.addChild(this.bd_add7);
		this.bd_chainmail.addChild(this.bd_strap3);
		this.bd_chainmail.addChild(this.bd_strap4);
		this.bd_chainmail.addChild(this.bd_strap5);
		this.bipedBody.addChild(this.bd_belt);

		this.bipedLeftArm.addChild(this.la_shoulder0);
		this.la_shoulder0.addChild(this.la_shoulder1);
		this.la_shoulder0.addChild(this.la_shoulder2);
		this.la_shoulder0.addChild(this.la_shoulder3);
		this.la_shoulder0.addChild(this.la_shoulder4);
		this.bipedLeftArm.addChild(this.la_plate0);
		this.bipedLeftArm.addChild(this.la_plate1);

		this.bipedRightArm.addChild(this.ra_shoulder0);
		this.ra_shoulder0.addChild(this.ra_shoulder1);
		this.ra_shoulder0.addChild(this.ra_shoulder2);
		this.ra_shoulder0.addChild(this.ra_shoulder3);
		this.ra_shoulder0.addChild(this.ra_shoulder4);
		this.bipedRightArm.addChild(this.ra_plate0);
		this.bipedRightArm.addChild(this.ra_plate1);

		this.bipedLeftLeg.addChild(this.ll_chainmail);
		this.ll_chainmail.addChild(this.ll_plate_f0);
		this.ll_chainmail.addChild(this.ll_plate_f1);
		this.ll_chainmail.addChild(this.ll_plate_f2);
		this.ll_chainmail.addChild(this.ll_plate_f3);
		this.ll_chainmail.addChild(this.ll_plate_o0);
		this.ll_chainmail.addChild(this.ll_plate_o1);
		this.ll_chainmail.addChild(this.ll_plate_b0);
		this.ll_chainmail.addChild(this.ll_plate_b1);
		
		this.bipedRightLeg.addChild(this.rl_chainmail);
		this.rl_chainmail.addChild(this.rl_plate_f0);
		this.rl_chainmail.addChild(this.rl_plate_f1);
		this.rl_chainmail.addChild(this.rl_plate_f2);
		this.rl_chainmail.addChild(this.rl_plate_f3);
		this.rl_chainmail.addChild(this.rl_plate_o0);
		this.rl_chainmail.addChild(this.rl_plate_o1);
		this.rl_chainmail.addChild(this.rl_plate_b0);
		this.rl_chainmail.addChild(this.rl_plate_b1);

		this.bipedLeftLeg.addChild(this.lb_base);
		this.lb_base.addChild(this.lb_front);
		this.lb_base.addChild(this.lb_side0);
		this.lb_base.addChild(this.lb_side1);
		this.bipedRightLeg.addChild(this.rb_base);
		this.rb_base.addChild(this.rb_front);
		this.rb_base.addChild(this.rb_side0);
		this.rb_base.addChild(this.rb_side1);

		this.bipedHead.addChild(this.hm_top);
		this.bipedHead.addChild(this.hm_l0);
		this.bipedHead.addChild(this.hm_r0);
		this.bipedHead.addChild(this.hm_b0);
		this.bipedHead.addChild(this.hm_b1);
		this.bipedHead.addChild(this.hm_f0);

		this.bipedHead.addChild(this.face_0);
		this.face_0.addChild(this.face_l1);
		this.face_0.addChild(this.face_l2);
		this.face_0.addChild(this.face_r1);
		this.face_0.addChild(this.face_r2);
		if(mask>=0)
			this.bipedHead.addChild(this.face_mask);
		if(goggles)
			this.bipedHead.addChild(this.face_goggles);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{ 
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
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
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		}

		if (this.isChild)
		{
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
			GL11.glTranslatef(0.0F, 16.0F * f5, 0.0F);
			this.bipedHead.render(f5);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
			this.bipedBody.render(f5);
			this.bipedRightArm.render(f5);
			this.bipedLeftArm.render(f5);
			this.bipedRightLeg.render(f5);
			this.bipedLeftLeg.render(f5);
			GL11.glPopMatrix();
		}
		else
		{
			this.bipedHead.render(f5);
			this.bipedBody.render(f5);
			this.bipedRightArm.render(f5);
			this.bipedLeftArm.render(f5);
			this.bipedRightLeg.render(f5);
			this.bipedLeftLeg.render(f5);
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	static ModelPrimordialArmor[][] modelHelm = new ModelPrimordialArmor[4][2];
	static ModelPrimordialArmor modelChest;
	static ModelPrimordialArmor modelLegs;
	static ModelPrimordialArmor modelBoots;
	static HashMap<ItemStack,ModelPrimordialArmor> modelMap = new HashMap();
	public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack)
	{
		if(stack==null || !(stack.getItem() instanceof ItemArmor))
			return null;
		int slot = ((ItemArmor)stack.getItem()).armorType;

		ModelPrimordialArmor armor = null;
		if(slot==0)
		{
			int mask = stack.hasTagCompound()&&stack.getTagCompound().hasKey("mask")?stack.getTagCompound().getInteger("mask"):-1;
			boolean goggles= stack.hasTagCompound()&&stack.getTagCompound().hasKey("goggles");
			if(modelHelm[mask+1][goggles?1:0]!=null)
				return modelHelm[mask+1][goggles?1:0];
		}
		else if(slot==1&&modelChest!=null)
			return modelChest;
		else if(slot==2&&modelLegs!=null)
			return modelLegs;
		else if(slot==3&&modelBoots!=null)
			return modelBoots;

		armor = new ModelPrimordialArmor(stack);

		switch(slot)
		{
		case 0:
			int mask = stack.hasTagCompound()&&stack.getTagCompound().hasKey("mask")?stack.getTagCompound().getInteger("mask"):-1;
			boolean goggles= stack.hasTagCompound()&&stack.getTagCompound().hasKey("goggles");
			armor.bipedBody.isHidden=true;
			armor.bipedLeftArm.isHidden=true;
			armor.bipedRightArm.isHidden=true;
			armor.bipedLeftLeg.isHidden=true;
			armor.bipedRightLeg.isHidden=true;
			modelHelm[mask+1][goggles?1:0] = armor;
			break;
		case 1:
			armor.bipedHead.isHidden=true;
			armor.bipedLeftLeg.isHidden=true;
			armor.bipedRightLeg.isHidden=true;
			armor.bd_belt.isHidden=true;
			modelChest = armor;
			break;
		case 2:
			armor.bd_chainmail.isHidden=true;
			armor.bipedHead.isHidden=true;
			armor.bipedLeftArm.isHidden=true;
			armor.bipedRightArm.isHidden=true;
			armor.lb_base.isHidden=true;
			armor.rb_base.isHidden=true;
			modelLegs = armor;
			break;
		case 3:
			armor.bipedHead.isHidden=true;
			armor.bipedBody.isHidden=true;
			armor.bipedLeftArm.isHidden=true;
			armor.bipedRightArm.isHidden=true;
			armor.rl_chainmail.isHidden=true;
			armor.ll_chainmail.isHidden=true;
			modelBoots = armor;
			break;
		}
		return armor;
	}
}
