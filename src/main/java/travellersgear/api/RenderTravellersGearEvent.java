package travellersgear.api;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * This event will be fire when a Traveller's Gear Item is rendered (when the item returns something for getArmorModel())
 */
public class RenderTravellersGearEvent extends RenderPlayerEvent
{
	 /**
     * Set this to false to make the gear not render
     */
	public boolean shouldRender=true;
	public ItemStack stack;
	public RenderTravellersGearEvent(EntityPlayer player, RenderPlayer renderer, ItemStack stack, float partialRenderTick)
	{
		super(player, renderer, partialRenderTick);
		this.stack = stack;
	}
}
