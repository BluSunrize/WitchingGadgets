package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class WGEnchantGemBrittle extends Enchantment
{
	public WGEnchantGemBrittle(int id, int weight)
	{
		super(id, weight, EnumEnchantmentType.all);
		this.setName("wg.gemstoneBrittle");
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 10 + 11 * (par1 - 1);
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 50;
	}

	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
	
	@Override
    public boolean isAllowedOnBooks()
    {
        return false;
    }
}
