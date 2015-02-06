package witchinggadgets.common.items.baubles;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import witchinggadgets.client.render.ModelKama;
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
			this.onItemEquipped((EntityPlayer) living, stack);
		setLastPlayerHashcode(stack, living.hashCode());
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase living)
	{
		if(living instanceof EntityPlayer)
			this.onItemUnequipped((EntityPlayer) living, stack);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living)
	{
		if(living instanceof EntityPlayer)
			this.onItemTicked((EntityPlayer) living, stack);
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
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1)
	{
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1)
	{
		return true;
	}
}