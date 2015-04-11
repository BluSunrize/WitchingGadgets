package witchinggadgets.common.items.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import thaumcraft.api.IRepairable;
import thaumcraft.api.aspects.Aspect;
import travellersgear.api.IActiveAbility;
import travellersgear.api.IEventGear;
import witchinggadgets.api.IPrimordialCrafting;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPrimordialAxe extends ItemAxe implements IPrimordialCrafting, IActiveAbility, IRepairable, IEventGear, IPrimordialGear
{
	IIcon overlay;
	public static Material[] validMats = {Material.cactus,Material.gourd,Material.leaves,Material.plants,Material.vine,Material.wood};

	public ItemPrimordialAxe(ToolMaterial mat)
	{
		super(mat);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean equipped)
	{
		super.onUpdate(stack, world, entity, slot, equipped);
		if ((stack.isItemDamaged()) && (entity != null) && (entity.ticksExisted % 40 == 0) && ((entity instanceof EntityLivingBase)))
			stack.damageItem(-1, (EntityLivingBase)entity);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target)
	{
		if(target instanceof EntityLivingBase)
		{
			for(int i=1;i<4;i++)
				if(((EntityLivingBase) target).getEquipmentInSlot(i)!=null && ((EntityLivingBase) target).getEquipmentInSlot(i).getItem() instanceof ItemArmor)
				{
					ItemStack armor = ((EntityLivingBase) target).getEquipmentInSlot(i);
					if(armor.getItem() instanceof ISpecialArmor)
						((ISpecialArmor)armor.getItem()).damageArmor((EntityLivingBase)target, armor, DamageSource.causePlayerDamage(player), 4, EntityLiving.getArmorPosition(armor)-1);
					else
						stack.damageItem(5, (EntityLivingBase) target);
					if (stack.stackSize <= 0)
						((EntityLivingBase) target).setCurrentItemOrArmor(i,null);
				}
			if(getAbility(stack)==0)
			{
				for(EntityLivingBase e : (List<EntityLivingBase>)player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(target.posX-2,target.posY-2,target.posZ-2, target.posX+2,target.posY+2,target.posZ+2)))
					if(e.canAttackWithItem() && !e.hitByEntity(player) && !e.equals(player))
					{
						float f = (float)player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
						int i = EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase)e);
						float f1 = EnchantmentHelper.getEnchantmentModifierLiving(player, (EntityLivingBase)e);
						if (player.isSprinting())
							++i;
						if(f>0 || f1>0)
						{
							boolean flag = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null && e instanceof EntityLivingBase;
							if(flag && f>0)
								f *= 1.5F;

							f += f1;
							boolean flag1 = false;
							int j = EnchantmentHelper.getFireAspectModifier(player);

							if(j>0 && !e.isBurning())
							{
								flag1 = true;
								e.setFire(1);
							}

							boolean flag2 = e.attackEntityFrom(DamageSource.causePlayerDamage(player), f);

							if(flag2)
							{
								if(i>0)
								{
									e.addVelocity((double)(-MathHelper.sin(player.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(player.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
									player.motionX *= 0.6D;
									player.motionZ *= 0.6D;
									player.setSprinting(false);
								}

								if(flag)
									player.onCriticalHit(e);
								if(f1>0)
									player.onEnchantmentCritical(e);
								if(f>=18)
									player.triggerAchievement(AchievementList.overkill);

								player.setLastAttacker(e);
								EnchantmentHelper.func_151384_a((EntityLivingBase)e, player);
								EnchantmentHelper.func_151385_b(player, e);
								player.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
								if(j>0)
									e.setFire(j * 4);
								player.addExhaustion(0.3F);
							}
							else if(flag1)
								e.extinguish();
						}
					}
			}
			if(getAbility(stack)==2)
			{
				((EntityLivingBase) target).addPotionEffect(new PotionEffect(WGContent.pot_cinderCoat.id,80,1));
				target.setFire(4);
			}
			if(getAbility(stack)==3)
				((EntityLivingBase) target).addPotionEffect(new PotionEffect(WGContent.pot_dissolve.id,80,2));
			if(getAbility(stack)==5)
			{
				((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.weakness.getId(), 60));
				((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.hunger.getId(), 120));
			}
		}
		return false;
	}
	@Override
	public void onUserDamaged(LivingHurtEvent event, ItemStack stack)
	{
		if(getAbility(stack)==1 && ((EntityPlayer)event.entityLiving).isBlocking())
		{
			int time = event.entityLiving.getActivePotionEffect(Potion.resistance)!=null?event.entityLiving.getActivePotionEffect(Potion.resistance).getDuration():0;
			time = Math.min(time+30, 80);
			int amp = event.entityLiving.getActivePotionEffect(Potion.resistance)!=null?event.entityLiving.getActivePotionEffect(Potion.resistance).getAmplifier():-1;
			amp = Math.min(amp+1, 2);
			event.entityLiving.addPotionEffect(new PotionEffect(Potion.resistance.id,time,amp));
		}

	}



	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand)
	{
		return true;
	}
	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{
		if(!player.worldObj.isRemote)
			cycleAbilities(stack);
	}

	@Override
	public int getReturnedPearls(ItemStack stack)
	{
		return 2;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		int ab = getAbility(stack);
		String add = ab>=0&&ab<6? " "+EnumChatFormatting.DARK_GRAY+"- \u00a7"+Aspect.getPrimalAspects().get(ab).getChatcolor()+Aspect.getPrimalAspects().get(ab).getName()+EnumChatFormatting.RESET : "";
		return super.getItemStackDisplayName(stack)+add;
	}
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialAxe");
		this.overlay = iconRegister.registerIcon("witchinggadgets:primordialAxe_overlay");
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getRenderPasses(int meta)
	{
		return 2;
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		if(pass == 0)
			return itemIcon;
		return overlay;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass==1)
		{
			int ab = getAbility(stack);
			if(ab>=0&&ab<6)
				return Aspect.getPrimalAspects().get(getAbility(stack)).getColor();
		}
		return 0xffffff;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int ix, int iy, int iz, EntityPlayer player)
	{
		World world = player.worldObj;
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
		if(mop == null)
			return false;
		int side = mop.sideHit;
		int[] range = new int[3];
		range[0] = side==4||side==5?0: 1;
		range[1] = side==0||side==1?0: 1;
		range[2] = side==2||side==3?0: 1;
		for(int yy=-range[1]; yy<=range[1]; yy++)
			for(int zz=-range[2]; zz<=range[2]; zz++)
				for(int xx=-range[0]; xx<=range[0]; xx++)
				{
					int x = ix+xx;
					int y = iy+yy;
					int z = iz+zz;
					if(!world.blockExists(x, y, z))
						continue;
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);
					Material mat = world.getBlock(x, y, z).getMaterial();

					if(!world.isRemote && block != null && !block.isAir(world, x, y, z) && block.getPlayerRelativeBlockHardness(player, world, x, y, z) != 0)
					{
						if(!block.canHarvestBlock(player, meta) || !Utilities.isRightMaterial(mat, validMats))
							continue;
						if(!player.capabilities.isCreativeMode && block != Blocks.bedrock)
						{
							int localMeta = world.getBlockMetadata(x, y, z);
							if (block.removedByPlayer(world, player, x, y, z, true))
								block.onBlockDestroyedByPlayer(world, x, y, z, localMeta);
							block.onBlockHarvested(world, x, y, z, localMeta, player);
							block.harvestBlock(world, player, x, y, z, localMeta);
						} 
						else
							world.setBlockToAir(x, y, z);
						if(!world.isRemote)
							world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
					}
				}
		return false;
	}
	@Override
	public float getDigSpeed (ItemStack stack, Block block, int meta)
	{
		if(block.getMaterial()==Material.leaves||block.getMaterial()==Material.vine)
			return 6;
		return super.getDigSpeed(stack, block, meta);
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
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.block;
	}
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
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
}