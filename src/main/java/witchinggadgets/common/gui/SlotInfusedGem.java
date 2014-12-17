package witchinggadgets.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.container.SlotLimitedByClass;
import witchinggadgets.api.IInfusedGem;

public class SlotInfusedGem extends SlotLimitedByClass
{
	public SlotInfusedGem(IInventory par2iInventory, int id, int x, int y)
	{
		super(IInfusedGem.class, 1, par2iInventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return super.isItemValid(stack);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		super.onPickupFromSlot(player, stack);
		if(stack!=null)
		{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("BraceletSlot", -1);
		}
	}

	@Override
	public void putStack(ItemStack stack)
	{
		super.putStack(stack);
		if(stack!=null)
		{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("BraceletSlot", this.inventory instanceof InventoryPrimordialRing && this.slotNumber!=0 ? this.slotNumber+2 : this.slotNumber);
		}
	}
}
