package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import travellersgear.api.TravellersGearAPI;
import baubles.api.IBauble;

public class WGEnchantRideProtect extends Enchantment
{
	public WGEnchantRideProtect(int id)
	{
		super(id, 0, EnumEnchantmentType.armor_head);
		this.setName("wg.rideProtect");
	}

	@Override
	public int getMinEnchantability(int lvl)
	{
		return 9;
	}

	@Override
	public int getMaxEnchantability(int lvl)
	{
	    return getMinEnchantability(lvl) + 20;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack!=null && (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof IBauble || TravellersGearAPI.isTravellersGear(stack));
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
}
