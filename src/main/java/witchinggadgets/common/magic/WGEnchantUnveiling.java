package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;

public class WGEnchantUnveiling extends Enchantment
{
	public WGEnchantUnveiling(int id)
	{
		super(id, 0, EnumEnchantmentType.all);
		this.setName("wg.unveiling");
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
		return 1;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack!=null && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType==0 && (stack.getItem() instanceof IRevealer || stack.getItem() instanceof IGoggles);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
}
