package witchinggadgets.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import witchinggadgets.common.items.baubles.ItemCloak;
import witchinggadgets.common.util.Utilities;

public class ContainerCloak extends Container
{
	private World worldObj;
	public IInventory input = new InventoryCloak(this);
	ItemStack cloak = null;
	EntityPlayer player = null;
	private int pouchSlotAmount = 27;

	public ContainerCloak(InventoryPlayer iinventory, World world, ItemStack cloak)
	{
		this.worldObj = world;
		this.player = iinventory.player;
		this.cloak = cloak;

		for (int a = 0; a < pouchSlotAmount; a++)
			this.addSlotToContainer(new Slot(this.input, a, 8 + a % 9 * 18, 9 + a/9 * 18));

		bindPlayerInventory(iinventory);

		if (!world.isRemote)
			try {
				//System.out.println("Getting Stacks in the ItemInventory");
				((InventoryCloak)this.input).stackList = ((ItemCloak)this.cloak.getItem()).getStoredItems(this.cloak);
			}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		this.onCraftMatrixChanged(this.input);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 82 + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 140));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot)
	{
		ItemStack stack = null;
		Slot slotObject = (Slot)this.inventorySlots.get(slot);

		if ((slotObject != null) && (slotObject.getHasStack()))
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (slot < pouchSlotAmount)
			{
				if (!this.mergeItemStack(stackInSlot, pouchSlotAmount, this.inventorySlots.size(), true))
					return null;
			}
			else if (!this.mergeItemStack(stackInSlot, 0, pouchSlotAmount, false))
			{
				return null;
			}

			if (stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();
		}

		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if (!this.worldObj.isRemote)
		{
			((ItemCloak)this.cloak.getItem()).setStoredItems(this.cloak, ((InventoryCloak)this.input).stackList);

			Utilities.updateActiveMagicalCloak(player, cloak);
		}
	}

}