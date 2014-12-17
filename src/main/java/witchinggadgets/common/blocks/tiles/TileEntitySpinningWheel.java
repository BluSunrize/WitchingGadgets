package witchinggadgets.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.util.recipe.SpinningRecipe;

public class TileEntitySpinningWheel extends TileEntityWGBase implements IInventory
{
	public int facing = 0;
	public int animation = 0;
	public int progress = 0;
	public int maxProgress = 120;
	public ItemStack[] inv = new ItemStack[6];

	public TileEntitySpinningWheel()
	{
		super();
	}

	public void updateEntity()
	{
		super.updateEntity();
		if(isActive())
		{
			if(animation < 63)
				animation ++;
			else
				animation = 0;
		}
		if(hasWork() && canWork())
		{
			if(progress < maxProgress)
				progress ++;
			else
			{
				SpinningRecipe s = getRecipe();
				ItemStack output = s.getOutput();
				
				if (this.inv[5] == null)
	            {
	                this.inv[5] = output.copy();
	            }
	            else if (this.inv[5].isItemEqual(output))
	            {
	            	inv[5].stackSize += output.stackSize;
	            }

	            this.decrStackSize(0, 1);
	            this.decrStackSize(1, 1);
	            this.decrStackSize(2, 1);
	            this.decrStackSize(3, 1);
	            this.decrStackSize(4, 1);
	            
	            progress = 0;
			}
			this.markDirty();
		}
		else
		{
			progress = 0;
		}
	}

	private boolean hasWork()
	{
		return getRecipe()!=null;
	}

	private boolean canWork()
	{
		SpinningRecipe s = getRecipe();
		ItemStack out = s.getOutput();
		if(!this.canStack(inv[5], out))return false;
		return true;
	}

	private SpinningRecipe getRecipe()
	{
		ItemStack[] inputs = {inv[0],inv[1],inv[2],inv[3],inv[4]};
		return WitchingGadgets.instance.customRecipeHandler.getSpinningRecipe(inputs);
	}

	public boolean isActive()
	{
		return progress > 0;
	}

	private boolean canStack(ItemStack par1, ItemStack par2)
	{	
		if(par2 == null)		return true;
		if(par1 == null)		return true;
		if(!(par1.isItemEqual(par2)))	return false;
		if(((par1.stackSize + par2.stackSize) <= par1.getMaxStackSize())
				&& ((par1.stackSize + par2.stackSize) <= par2.getMaxStackSize()))	return true;
		return false;
	}

	public int getProgressScaled(int max)
	{
		double d = (double)this.progress / (double)this.maxProgress;
		return (int)Math.ceil(d * max);
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
		progress = tags.getInteger("progress");
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
		tags.setInteger("progress", progress);
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
		return "SpinningWheel";
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