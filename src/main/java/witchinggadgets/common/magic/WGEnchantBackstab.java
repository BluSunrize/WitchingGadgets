package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class WGEnchantBackstab extends Enchantment
{
	public WGEnchantBackstab(int id)
	{
		super(id, 0, EnumEnchantmentType.weapon);
		this.setName("wg.backstab");
	}

	@Override
	public int getMinEnchantability(int lvl)
	{
	    return 5+(lvl-1)*11;
	}
	@Override
	public int getMaxEnchantability(int lvl)
	{
	    return getMinEnchantability(lvl) + 20;
	}

	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench)
	{
		return ench!=Enchantment.smite && ench!=Enchantment.baneOfArthropods;
	}
	@Override
	public boolean canApply(ItemStack stack)
	{
		return (stack!=null&&stack.getItem() instanceof ItemAxe)||super.canApply(stack);
	}
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
}
