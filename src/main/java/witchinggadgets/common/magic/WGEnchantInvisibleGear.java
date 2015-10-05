package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class WGEnchantInvisibleGear extends Enchantment
{
	public WGEnchantInvisibleGear(int id)
	{
		super(id, 0, EnumEnchantmentType.all);
		this.setName("wg.invisibleGear");
	}

	@Override
	public int getMinEnchantability(int lvl)
	{
		return 6;
	}

	@Override
	public int getMaxEnchantability(int lvl)
	{
	    return getMinEnchantability(lvl) + 20;
	}

	@Override
	public int getMaxLevel()
	{
		return 2;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
}
