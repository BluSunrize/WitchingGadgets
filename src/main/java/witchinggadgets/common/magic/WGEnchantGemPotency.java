package witchinggadgets.common.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import witchinggadgets.api.IInfusedGem;

public class WGEnchantGemPotency extends Enchantment
{
	public WGEnchantGemPotency(int id, int weight)
	{
		super(id, weight, EnumEnchantmentType.all);
		this.setName("gemstonePotency");
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
		return (stack!=null && stack.getItem() instanceof IInfusedGem && ((IInfusedGem)stack.getItem()).isGemEnchantable(stack));
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return canApply(stack);
	}
}
