package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import travellersgear.api.TravellersGearAPI;
import baubles.api.IBauble;

public class WGEnchantInvisibleGear extends Enchantment
{
	public WGEnchantInvisibleGear(int id)
	{
		super(id, 0, EnumEnchantmentType.all);
		this.setName("wg.invisibleGear");
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 0;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return 0;
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
