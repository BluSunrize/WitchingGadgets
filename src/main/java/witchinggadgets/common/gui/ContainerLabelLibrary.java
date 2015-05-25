package witchinggadgets.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;
import witchinggadgets.common.blocks.tiles.TileEntityLabelLibrary;

public class ContainerLabelLibrary extends Container
{
	protected final TileEntityLabelLibrary tileEntity;
	//private int slotCount;

	public ContainerLabelLibrary (InventoryPlayer inventoryPlayer, TileEntityLabelLibrary te)
	{
		this.tileEntity = te;

		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen

		this.addSlotToContainer(new Slot(tileEntity, 0, 8, 8)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				if(stack==null)
					return true;
				return stack.getItem().equals(ConfigItems.itemResource) && stack.getItemDamage()==13;
			}
		});
		this.addSlotToContainer(new SlotOutput(tileEntity, 1, 8, 51)
		{
			@Override
			public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
			{
				this.inventory.decrStackSize(0, 1);
			}
		});

		//this.slotCount = 2;
		this.bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUseableByPlayer(player);
	}


	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));

		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 142));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		return null;
	}
}
