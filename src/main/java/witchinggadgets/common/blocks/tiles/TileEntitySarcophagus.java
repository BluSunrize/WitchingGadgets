package witchinggadgets.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntitySarcophagus extends TileEntityWGBase implements IInventory
{
	public int facing = 0;
	public boolean open = false;
	public boolean dummyLeft = false;
	public boolean dummyRight = false;
	public ItemStack[] inv = new ItemStack[6];

	public TileEntitySarcophagus()
	{
		super();
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		NBTTagList tagList = tags.getTagList("Inventory",10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		open = tags.getBoolean("open");
		dummyLeft = tags.getBoolean("dummyLeft");
		dummyRight = tags.getBoolean("dummyRight");
		facing = tags.getInteger("facing");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tags.setTag("Inventory", itemList);
		tags.setBoolean("open", open);
		tags.setBoolean("dummyLeft", dummyLeft);
		tags.setBoolean("dummyRight", dummyRight);
		tags.setInteger("facing", facing);
	}


	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inv[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}              
	}

	@Override
	public String getInventoryName()
	{
		return "WG.Sarcophagus";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

}