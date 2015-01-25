package witchinggadgets.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.config.ConfigItems;

public class TileEntityLabelLibrary extends TileEntityWGBase implements IInventory
{
	ItemStack[] inventory = new ItemStack[2];
	public Aspect aspect;
	public int facing = 2;

	@Override
	public void updateEntity()
	{
		if(!this.worldObj.isRemote)
		{
			this.inventory[1] = this.getOutput();
		}
	}
	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		NBTTagList tagList = tag.getTagList("Inventory",10);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length)
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
		}
		if(tag.hasKey("aspect"))
			aspect = Aspect.getAspect(tag.getString("aspect"));
		facing = tag.getInteger("facing");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		if(inventory!=null)
		{
			NBTTagList itemList = new NBTTagList();
			for (int i = 0; i < inventory.length; i++)
			{
				ItemStack stack = inventory[i];
				if (stack != null)
				{
					NBTTagCompound itemTag = new NBTTagCompound();
					itemTag.setByte("Slot", (byte) i);
					stack.writeToNBT(itemTag);
					itemList.appendTag(itemTag);
				}
			}
			tag.setTag("Inventory", itemList);
		}
		if(aspect!=null)
			tag.setString("aspect", aspect.getTag());
		tag.setInteger("facing",facing);
	}

	public ItemStack getOutput()
	{
		if(aspect != null && this.inventory[0]!=null)
		{
			ItemStack stack = new ItemStack(ConfigItems.itemResource,1,13);
			((IEssentiaContainerItem)stack.getItem()).setAspects(stack, new AspectList().add(aspect, 0));
			return stack;
		}
		return null;
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inventory[i];
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
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}              
	}

	@Override
	public String getInventoryName()
	{
		return "LabelLibrary";
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
