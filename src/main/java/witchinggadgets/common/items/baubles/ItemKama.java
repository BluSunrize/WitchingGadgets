package witchinggadgets.common.items.baubles;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.render.ModelKama;
import witchinggadgets.common.util.Utilities;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKama extends ItemCloak implements IBauble
{
	IIcon overlay;
	public ItemKama()
	{
		super();
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:kama");
		this.iconRaven = iconRegister.registerIcon("witchinggadgets:kama_raven");
		this.iconWolf = iconRegister.registerIcon("witchinggadgets:kama_wolf");

		this.overlay = iconRegister.registerIcon("witchinggadgets:kama_overlay");
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		if(pass==1)
			return overlay;
		return super.getIconFromDamageForRenderPass(meta, pass);
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass==1)
			return 0xffffff;
		return super.getColorFromItemStack(stack, pass);
	}
	   
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
	{
		return new ModelKama(getColor(itemStack));
	}

	@Override
	public int getSlot(ItemStack stack)
	{
		return -1;
	}
	@Override
	public BaubleType getBaubleType(ItemStack stack)
	{
		return BaubleType.BELT;
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase living)
	{
		if(living instanceof EntityPlayer)
			this.onGearEquip((EntityPlayer) living, stack);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase living)
	{
		if(living instanceof EntityPlayer)
			this.onGearUnequip((EntityPlayer) living, stack);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living)
	{
		if(living instanceof EntityPlayer)
			this.onGearTick((EntityPlayer) living, stack);
	}
	
	@Override
	public void onTravelGearTick(EntityPlayer player, ItemStack stack)
	{
	}
	@Override
	public void onTravelGearEquip(EntityPlayer player, ItemStack stack)
	{
	}
	@Override
	public void onTravelGearUnequip(EntityPlayer player, ItemStack stack)
	{
	}

	
	
	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand)
	{
		return !isInHand;
	}

	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemDamage()<subNames.length)
			if(subNames[stack.getItemDamage()].equals("storage") && !player.worldObj.isRemote)
				player.openGui(WitchingGadgets.instance, 4, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			else if(subNames[stack.getItemDamage()].equals("raven") && !player.worldObj.isRemote)
			{
				if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("noGlide", !stack.getTagCompound().getBoolean("noGlide"));
			}
			else if(subNames[stack.getItemDamage()].equals("spectral") && !player.worldObj.isRemote && Utilities.consumeVisFromInventoryWithoutDiscount(player, new AspectList().add(Aspect.AIR,1)))
			{
				if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("isSpectral", !stack.getTagCompound().getBoolean("isSpectral"));
				if(stack.getTagCompound().getBoolean("isSpectral"))
				{
					for(EntityCreature e : (List<EntityCreature>)player.worldObj.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.getBoundingBox(player.posX-16,player.posY-16,player.posZ-16, player.posX+16,player.posY+16,player.posZ+16)))
						if(e!=null && !(e instanceof IBossDisplayData) && player.equals(e.getAttackTarget()))
							e.setAttackTarget(null);
				}
			}
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase living)
	{
		return true;
	}
	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase living)
	{
		return true;
	}
}