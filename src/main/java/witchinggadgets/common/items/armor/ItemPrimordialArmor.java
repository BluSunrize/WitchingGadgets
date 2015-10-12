package witchinggadgets.common.items.armor;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.armor.Hover;
import thaumcraft.common.items.armor.ItemFortressArmor;
import travellersgear.api.IActiveAbility;
import travellersgear.api.IEventGear;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.api.IPrimordialCrafting;
import witchinggadgets.client.render.ModelPrimordialArmor;
import witchinggadgets.common.items.tools.IPrimordialGear;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPrimordialArmor extends ItemFortressArmor implements IActiveAbility, IPrimordialCrafting, IEventGear, IPrimordialGear
{
	IIcon rune;

	public ItemPrimordialArmor(ArmorMaterial mat, int idx, int type)
	{
		super(mat, idx, type);
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public boolean showNodes(ItemStack stack, EntityLivingBase living)
	{
		return this.armorType==0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("goggles");
	}
	@Override
	public boolean showIngamePopups(ItemStack stack, EntityLivingBase living)
	{
		return this.armorType==0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("goggles");
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean equipped)
	{
		super.onUpdate(stack, world, entity, slot, equipped);
		if ((!world.isRemote) && (stack.isItemDamaged()) && (entity.ticksExisted % 40 == 0) && ((entity instanceof EntityLivingBase)))
			stack.damageItem(-1, (EntityLivingBase)entity);
	}
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		if ((!world.isRemote) && (stack.getItemDamage() > 0) && (player.ticksExisted % 20 == 0))
			stack.damageItem(-1, player);

		if(this.armorType==3)
		{
			if(!player.capabilities.isFlying && player.moveForward>0.0F)
			{
				if(player.worldObj.isRemote && !player.isSneaking())
				{
					if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(Integer.valueOf(player.getEntityId())))
						Thaumcraft.instance.entityEventHandler.prevStep.put(Integer.valueOf(player.getEntityId()), Float.valueOf(player.stepHeight));
					player.stepHeight = 1.0F;
				}
				if (player.onGround)
				{
					float bonus = 0.055F;
					if (player.isInWater())
						bonus /= 4.0F;
					player.moveFlying(0.0F, 1.0F, bonus);
				}
				else if (Hover.getHover(player.getEntityId()))
					player.jumpMovementFactor = 0.03F;
				else
					player.jumpMovementFactor = 0.05F;
			}
			if (player.fallDistance > 0.0F)
				player.fallDistance -= 0.25F;
		}

		switch(getAbility(stack))
		{
		case 0:
			//Thanks WayOfFlowingTime =P
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX-.5,player.posY-.5,player.posZ-.5, player.posX+.5,player.posY+.5,player.posZ+.5).expand(4,4,4);
			for(Entity projectile : (List<Entity>)world.getEntitiesWithinAABB(Entity.class, aabb))
			{
				if(projectile==null)
					continue;
				if(!(projectile instanceof IProjectile) || projectile.getClass().getSimpleName().equalsIgnoreCase("IManaBurst"))
					continue;

				Entity shooter = null;
				if(projectile instanceof EntityArrow)
					shooter = ((EntityArrow) projectile).shootingEntity;
				else if(projectile instanceof EntityThrowable)
					shooter = ((EntityThrowable) projectile).getThrower();

				if(shooter!=null && shooter.equals(player))
					continue;

				double delX = projectile.posX - player.posX;
				double delY = projectile.posY - player.posY;
				double delZ = projectile.posZ - player.posZ;
				
				double angle = (delX*projectile.motionX + delY*projectile.motionY + delZ*projectile.motionZ)/ (Math.sqrt(delX * delX + delY * delY + delZ * delZ)*Math.sqrt(projectile.motionX*projectile.motionX + projectile.motionY* projectile.motionY + projectile.motionZ*projectile.motionZ));
				angle = Math.acos(angle);
				if(angle < 3*(Math.PI/4)) //angle is < 135 degrees
					continue;
				
				if(shooter != null)
				{
					delX = -projectile.posX + shooter.posX;
					delY = -projectile.posY + (shooter.posY + shooter.getEyeHeight());
					delZ = -projectile.posZ + shooter.posZ;
				}
				
				
				double curVel = Math.sqrt(delX * delX + delY * delY + delZ * delZ);
				delX /= curVel;
				delY /= curVel;
				delZ /= curVel;
				double newVel = Math.sqrt(projectile.motionX*projectile.motionX + projectile.motionY*projectile.motionY + projectile.motionZ*projectile.motionZ);
				projectile.motionX = newVel * delX;
				projectile.motionY = newVel * delY;
				projectile.motionZ = newVel * delZ;
			}
			break;
		case 3:
			int[] curedPotions = {Potion.blindness.id,Potion.poison.id,Potion.wither.id,Potion.confusion.id,Config.potionTaintPoisonID};
			for(int c : curedPotions)
				if(world.isRemote)
					player.removePotionEffectClient(c);
				else
					player.removePotionEffect(c);
			break;
		default:
			break;
		}
	}
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
	{
		int priority = 0;
		double ratio = this.damageReduceAmount / 25.0D;
		if(source.isMagicDamage())
		{
			priority = 1;
			ratio = this.damageReduceAmount / 35.0D;
		}
		else if(source.isFireDamage() || source.isExplosion())
		{
			if(source.isFireDamage() && getAbility(armor)==2)
			{
				if(player.isBurning())
					player.extinguish();
			}
			priority = 1;
			ratio = getAbility(armor)==2? .75f : (this.damageReduceAmount / 20.0D);
		}
		else if (source.isUnblockable())
		{
			int ab = getAbility(armor);
			priority = ab==1?1:0;
			ratio = ab==1?this.damageReduceAmount/50.0D :0.0D;
		}
		if ((player instanceof EntityPlayer))
		{
			double set = 0.875D;
			for(int a=1;a<=4;a++)
			{
				ItemStack piece = player.getEquipmentInSlot(a);
				if(piece!=null && piece.getItem() instanceof ItemPrimordialArmor)
				{
					set += 0.125D;
					if(piece.hasTagCompound() && piece.stackTagCompound.hasKey("mask"))
						set += 0.05D;
				}
			}
			ratio *= set;
		}
		return new ISpecialArmor.ArmorProperties(priority, ratio, armor.getMaxDamage() + 1 - armor.getItemDamage());
	}

	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand)
	{
		return true;
		//		return getUpgrade(stack)!=null;
	}

	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{
		if(!player.worldObj.isRemote)
			cycleAbilities(stack);
		//		toggleActive(stack);
	}

	//	public PrimordialArmorUpgrade getUpgrade(ItemStack stack)
	//	{
	//		return PrimordialArmorUpgrade.AIR;
	//		//		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("primordialUpgrade"))
	//		//		{
	//		//			int i = stack.getTagCompound().getByte("primordialUpgrade");
	//		//			if(i>=0&&i<PrimordialArmorUpgrade.values().length)
	//		//				return PrimordialArmorUpgrade.values()[i];
	//		//		}
	//		//		return null;
	//	}
	//	boolean effectActive(ItemStack stack)
	//	{
	//		return !stack.hasTagCompound() || !stack.getTagCompound().getBoolean("disabled");
	//	}
	//	void toggleActive(ItemStack stack)
	//	{
	//		if(!stack.hasTagCompound())
	//			stack.setTagCompound(new NBTTagCompound());
	//		stack.getTagCompound().setBoolean("disabled", !stack.getTagCompound().getBoolean("disabled"));
	//	}
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		int ab = getAbility(stack);
		String add = ab>=0&&ab<6? " "+EnumChatFormatting.DARK_GRAY+"- \u00a7"+Aspect.getPrimalAspects().get(ab).getChatcolor()+Aspect.getPrimalAspects().get(ab).getName()+EnumChatFormatting.RESET : "";
		return super.getItemStackDisplayName(stack)+add;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
	{
		return ModelPrimordialArmor.getModel(entityLiving, itemStack);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return "witchinggadgets:textures/models/primordialArmor.png";
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialArmor"+this.armorType);
		this.rune = iconRegister.registerIcon("witchinggadgets:primordialArmorRune");
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		return pass==0?rune:itemIcon;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass==0)
		{
			int ab = getAbility(stack);
			if(ab>=0&&ab<6)
				return Aspect.getPrimalAspects().get(getAbility(stack)).getColor();
		}
		return 0xffffff;
	}
	@Override
	public int getReturnedPearls(ItemStack stack)
	{
		return 3;
	}

	@Override
	public void cycleAbilities(ItemStack stack)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		int cur = stack.getTagCompound().getInteger("currentMode");
		cur++;
		if(cur>=6)
			cur=0;
		stack.getTagCompound().setInteger("currentMode",cur);
	}
	@Override
	public int getAbility(ItemStack stack)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound().getInteger("currentMode");
	}

	@Override
	public void onUserDamaged(LivingHurtEvent event, ItemStack stack)
	{
		if(event.entityLiving instanceof EntityPlayer)
		{
			switch(getAbility(stack))
			{
			case 0:
				if(event.source.isProjectile())
					event.setCanceled(true);
				break;
			case 5:
				if(event.source.getSourceOfDamage() instanceof EntityLivingBase)
					if(event.entityLiving.getRNG().nextInt(4)==0)
					{
						((EntityLivingBase)event.source.getSourceOfDamage()).addPotionEffect(new PotionEffect(Potion.blindness.id,10,0));
						((EntityLivingBase)event.source.getSourceOfDamage()).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,10,3));
					}
				break;
				//			case FIRE:
				//				//Nova?
				//				break;
				//			case ORDER:
				//				//Something something Healing?
				//				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack stack1, ItemStack stack2)
	{
		return Utilities.compareToOreName(stack2, "ingotVoid");
	}

	@Override
	public void onUserAttacking(AttackEntityEvent event, ItemStack stack)
	{
	}
	@Override
	public void onUserJump(LivingJumpEvent event, ItemStack stack)
	{
	}
	@Override
	public void onUserFall(LivingFallEvent event, ItemStack stack)
	{
	}
	@Override
	public void onUserTargeted(LivingSetAttackTargetEvent event, ItemStack stack)
	{
	}
	//	public enum PrimordialArmorUpgrade
	//	{
	//		AIR(new AspectList().add(Aspect.AIR,32).add(Aspect.MOTION,32).add(Aspect.ARMOR,32), new ItemStack(ConfigItems.itemShard,1,0),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(ConfigItems.itemShard,1,0),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence)),
	//		FIRE(new AspectList().add(Aspect.FIRE,32).add(Aspect.LIGHT,32).add(Aspect.ENERGY,32), new ItemStack(ConfigItems.itemShard,1,1),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(ConfigItems.itemShard,1,1),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence)),
	//		EARTH(new AspectList().add(Aspect.EARTH,32).add(Aspect.ARMOR,32).add(Aspect.METAL,32), new ItemStack(ConfigItems.itemShard,1,2),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.obsidian),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.obsidian),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(ConfigItems.itemShard,1,2),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.obsidian),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.obsidian),new ItemStack(ConfigItems.itemWispEssence)),
	//		WATER(new AspectList().add(Aspect.WATER,32).add(Aspect.POISON,32).add(Aspect.HEAL,32), new ItemStack(ConfigItems.itemShard,1,3),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.milk_bucket),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.milk_bucket),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(ConfigItems.itemShard,1,3),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.milk_bucket),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.milk_bucket),new ItemStack(ConfigItems.itemWispEssence)),
	//		ORDER(new AspectList().add(Aspect.ORDER,32).add(Aspect.HEAL,32).add(Aspect.EXCHANGE,32), new ItemStack(ConfigItems.itemShard,1,4),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(ConfigItems.itemShard,1,4),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Items.arrow),new ItemStack(ConfigItems.itemWispEssence)),
	//		ENTROPY(new AspectList().add(Aspect.ENTROPY,32).add(Aspect.TRAP,32).add(Aspect.DARKNESS,32), new ItemStack(ConfigItems.itemShard,1,5),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.soul_sand),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.soul_sand),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(ConfigItems.itemShard,1,5),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.soul_sand),new ItemStack(ConfigItems.itemWispEssence),new ItemStack(Blocks.soul_sand),new ItemStack(ConfigItems.itemWispEssence));
	//
	//		final ItemStack[] components;
	//		final AspectList aspects;
	//		PrimordialArmorUpgrade(AspectList aspects, ItemStack... components)
	//		{
	//			this.components = components;
	//			this.aspects = aspects;
	//		}
	//
	//		public ItemStack[] getCompenents()
	//		{
	//			return components;
	//		}
	//		public AspectList getAspects()
	//		{
	//			return aspects;
	//		}
	//	}
}