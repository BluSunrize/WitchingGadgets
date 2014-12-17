package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;

public class WGEnchantUnveiling extends Enchantment
{
	public WGEnchantUnveiling(int id)
	{
		super(id, 0, EnumEnchantmentType.all);
		this.setName("unveiling");
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
		return stack!=null && (stack.getItem() instanceof IRevealer || stack.getItem() instanceof IGoggles);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return true;
	}
}
