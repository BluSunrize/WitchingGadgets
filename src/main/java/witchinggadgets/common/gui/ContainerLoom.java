package witchinggadgets.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import witchinggadgets.common.blocks.tiles.TileEntityLoom;

public class ContainerLoom extends Container
{
	protected TileEntityLoom tileEntity;
	private int slotCount;

	public ContainerLoom (InventoryPlayer inventoryPlayer, TileEntityLoom te)
	{
		this.tileEntity = te;

		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		
		for (int i= 0; i < 4; i++)
		{
			this.addSlotToContainer(new Slot(tileEntity, i, 16, 40+24*i));
		}
		for (int i= 0; i < 4; i++)
		{
			this.addSlotToContainer(new Slot(tileEntity, i+4, 51+24*i, 9));
		}
		this.addSlotToContainer(new SlotOutput(tileEntity, 8, 123, 112));
		this.slotCount = 9;
		//commonly used vanilla code that adds the player's inventory
		this.bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUseableByPlayer(player);
	}


	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						16 + j * 18, 151 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 16 + i * 18, 209));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			//merges the item into player inventory since its in the tileEntity
			if (slot < slotCount) {
				if (!this.mergeItemStack(stackInSlot, slotCount, (slotCount + 36), true)) {
					return null;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, 0, slotCount, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}
}
