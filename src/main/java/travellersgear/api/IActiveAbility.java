package travellersgear.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This interface can be implemented by any Armor, Bauble, Traveller's Gear
 * Mariculture Jewelry and TCon Gloves+Knapsacks
 * They will then be eligible for the "Active Ability" menu ingame.
 * @author BluSunrize
 */
public interface IActiveAbility
{
	public boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand);
	public void activate(EntityPlayer player, ItemStack stack);
}