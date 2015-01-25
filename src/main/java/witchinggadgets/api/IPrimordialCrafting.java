package witchinggadgets.api;

import net.minecraft.item.ItemStack;

public interface IPrimordialCrafting
{
	/**
	 * @return the amount of Primordial Pearls refunded upon crafting
	 */
	public int getReturnedPearls(ItemStack stack);
}
