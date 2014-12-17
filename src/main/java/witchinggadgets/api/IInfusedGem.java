package witchinggadgets.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

public interface IInfusedGem
{
	/**
	 * Handles the performing of the effect. Note that no stack is given here because the effects are also performed by the Primordial Glove
	 * @param cut
	 * @param aspect
	 * @param potency
	 * @param brittle
	 * @param player
	 * @return
	 */
	public boolean performEffect(String cut, Aspect aspect, int potency, int brittle, EntityPlayer player);
	
	/**
	 * Returns the amount of charges consumed upon using a gems effect. This information is important for the Primordial Gauntlet.
	 * @param cut
	 * @param aspect
	 * @param player
	 * @return
	 */
	public int getConsumedCharge(String cut, Aspect aspect, EntityPlayer player);

	public boolean isGemEnchantable(ItemStack stack);
}