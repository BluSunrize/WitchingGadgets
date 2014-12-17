package witchinggadgets.common.blocks.tiles;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import scala.actors.threadpool.Arrays;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.items.ItemInfusedGem;
import witchinggadgets.common.util.handler.InfusedGemHandler;

public class TileEntityCuttingTable extends TileEntityWGBase implements IInventory
{
	int tick = 0;
	int tickMax = 20*20;
	ItemStack[] inventory = new ItemStack[5];
	public int facing = 2;
	public byte targetGemCut = 0;

	public void updateEntity()
	{
		if(!this.worldObj.isRemote)
		{
			this.inventory[4] = this.getOutput();
		}
	}
	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		this.tick = tag.getInteger("tickCount");

		NBTTagList tagList = tag.getTagList("Inventory",10);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length)
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
		}
		this.facing = tag.getInteger("facing");
		this.targetGemCut = tag.getByte("targetGemCut");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("tickCount", this.tick);
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
		tag.setInteger("facing", this.facing);
		tag.setByte("targetGemCut", this.targetGemCut);
	}

	public ItemStack getOutput()
	{
		if(this.inventory[0]==null || !InfusedGemHandler.isGem(this.inventory[0]))
			return null;
		ItemStack stack = new ItemStack(WGContent.ItemInfusedGem);
		//			System.out.println(l.getAmount(aspect));
		Aspect aspect = getInfusingAspect();
		if(aspect != null)
		{
			stack = ItemInfusedGem.createGem(aspect, ItemInfusedGem.GemCut.getValue(targetGemCut), false);
			if( Arrays.asList(InfusedGemHandler.getNaturalAffinities(inventory[0])).contains(aspect) )
			{
				stack.addEnchantment(WGContent.enc_gemstonePotency, 1);
			}
			return stack;
		}
		return null;
	}

	public Aspect getInfusingAspect()
	{
		AspectList l = new AspectList();
		for(int i=1;i<=3;i++)
			if(this.inventory[i] != null)
			{
				AspectList as = new AspectList();
				as.readFromNBT(inventory[i].getTagCompound());
				for(Aspect a : as.getAspects())
					l.add(a, as.getAmount(a));
			}
		return l.getAspectsSortedAmount()[0];
	}

	static Set<Aspect> acceptedAspects = new HashSet();
	static{
		acceptedAspects.add(Aspect.AIR);
		acceptedAspects.add(Aspect.EARTH);
		acceptedAspects.add(Aspect.FIRE);
		acceptedAspects.add(Aspect.WATER);
		acceptedAspects.add(Aspect.ORDER);
		acceptedAspects.add(Aspect.ENTROPY);
		acceptedAspects.add(Aspect.LIGHT);
		acceptedAspects.add(Aspect.HEAL);
	}
	public boolean canAcceptAspect(Aspect a)
	{
		return a!=null && (getInfusingAspect()==null || a.equals(getInfusingAspect())) && acceptedAspects.contains(a);
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
		return "CuttingTable";
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
