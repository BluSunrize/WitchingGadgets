package witchinggadgets.common.items.tools;

import net.minecraft.item.ItemStack;

public interface IPrimordialGear
{
	public void cycleAbilities(ItemStack stack);
	public int getAbility(ItemStack stack);
}