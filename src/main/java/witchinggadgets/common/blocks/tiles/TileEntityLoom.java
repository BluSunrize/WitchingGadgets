package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.common.lib.FakeThaumcraftPlayer;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.util.recipe.WeavingRecipe;

import com.mojang.authlib.GameProfile;

public class TileEntityLoom extends TileEntityWGBase implements IInventory
{
	public int[] masterPosition = new int[3];
	public boolean isDummy = false;
	public int facing = 0; // 2: x-, 3: x+, 4: z-, 5: z+
	public boolean shufflePos = true;
	public int processingTick = 0;
	public int processingMax = 12;
	public boolean isActive = false;

	public int[] originalBlock = new int[2];

	public boolean isSetUp = false;

	EntityPlayer user;

	public AspectList storedAspects = new AspectList();

	public double progress = 0;
	public ItemStack[] inv;

	public TileEntityLoom()
	{
		super();
		inv = new ItemStack[9];
	}

	@Override
	public boolean canUpdate()
	{
		return !isDummy;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(this.isDummy)return;
		if((isSetUp == false || inv == null) && !this.isDummy)
		{
			inv = new ItemStack[9];
			this.isSetUp = true;
			return;
		}
		if(inv == null)
		{
			inv = new ItemStack[9];
		}
		// Cleans storedAspect list
		for(Aspect a: this.storedAspects.getAspects())
		{
			if(this.storedAspects.getAmount(a)<1)this.storedAspects.remove(a);
		}

		if(isActive && hasWork() && canWork())
		{	
			WeavingRecipe w = getRecipe();
			/*
			 * Compares Aspects inside the loom to those required by the recipe
			 */
			AspectList requiredAspects = w.getAspects();
			//			if(this.storedAspects.visSize() > 0)System.out.println("I still have stored aspects!!!");

			if(this.storedAspects.visSize() > 0)
			{
				for(Aspect a: this.storedAspects.getAspects())
				{
					//System.out.println("Using Stored "+a.getName()+": "+this.storedAspects.getAmount(a));
					requiredAspects.remove(a, this.storedAspects.getAmount(a));
				}
			}

			boolean aspectsGathered = false;

			// If Aspects in the loom don't suffice:
			if(requiredAspects.visSize() > 0)
			{
				//				System.out.println("I don't have the aspects, so I'll take them.");
				aspectsGathered = false;

				List<TileEntity> souroundingSources = this.findAspectSources(requiredAspects);
				for(TileEntity te: souroundingSources)
				{
					IAspectSource ias = (IAspectSource)te;
					AspectList ias_AspectList = ias.getAspects();
					for(Aspect ias_Aspect: ias_AspectList.getAspects())
					{
						int amountRequired = requiredAspects.getAmount(ias_Aspect);
						int ias_Amount = ias_AspectList.getAmount(ias_Aspect);
						if(ias_Amount < amountRequired)
						{
							if(ias.takeFromContainer(ias_Aspect, ias_Amount));
							{
								this.storedAspects.add(ias_Aspect, ias_Amount);
								if(worldObj.isRemote)
									WitchingGadgets.proxy.createEssentiaTrailFx(getWorldObj(), te.xCoord, te.yCoord, te.zCoord, xCoord, yCoord, zCoord, ias_Amount, ias_Aspect.getColor(), 1);
							}
						}
						else
						{
							if(ias.takeFromContainer(ias_Aspect, amountRequired));
							{
								this.storedAspects.add(ias_Aspect, amountRequired);
								if(worldObj.isRemote)
									WitchingGadgets.proxy.createEssentiaTrailFx(getWorldObj(), te.xCoord, te.yCoord, te.zCoord, xCoord, yCoord, zCoord, amountRequired, ias_Aspect.getColor(), 1);
							}
						}
					}
				}
			}
			else //If all aspects have been gathered
			{
				//				System.out.println("No Aspects required =)");
				aspectsGathered = true;
			}

			if(aspectsGathered)
			{
				if(processingTick < processingMax)
				{
					processingTick ++;
				}
				else
				{
					processingTick = 0;
					if(progress < 1)
						progress += 0.05;
					else
						progress = 0;

					if(shufflePos)
						shufflePos = false;
					else
						shufflePos = true;
				}
				if(progress >= 1)
				{
					WeavingRecipe w2 = getRecipe();
					AspectList takenAspects = w2.getAspects();

					ItemStack output = w.getOutput();

					if (this.inv[8] == null)
					{
						this.inv[8] = output.copy();
					}
					else if (this.inv[8].isItemEqual(output))
					{
						inv[8].stackSize += output.stackSize;
					}

					this.decrStackSize(0, 1);
					this.decrStackSize(1, 1);
					this.decrStackSize(2, 1);
					this.decrStackSize(3, 1);
					this.decrStackSize(4, 1);
					this.decrStackSize(5, 1);
					this.decrStackSize(6, 1);
					this.decrStackSize(7, 1);
//DEBUG
//					for(Aspect a2: takenAspects.getAspects())
//					{
//						if(a2 != null)
//							System.out.println("Should take: "+a2.getName()+": "+takenAspects.getAmount(a2));
//					}
					this.consumeAspects(takenAspects);
					progress = 0;
					isActive = false;
				}
				this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord,this.xCoord, this.yCoord, this.zCoord);
				this.markDirty();
			}
		}
		else
		{

			if(this.processingTick != 0 || this.progress != 0)
			{
				this.processingTick = 0;
				this.progress = 0;
				this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord,this.xCoord, this.yCoord, this.zCoord);
			}
		}
	}

	private boolean hasWork()
	{
		return getRecipe()!=null;
	}

	private boolean canWork()
	{
		if(!hasWork())return false;
		if(user == null)return false;
		WeavingRecipe s = getRecipe();
		ItemStack out = s.getOutput();
		if(!this.canStack(inv[8], out))return false;

		return true;
	}

	public WeavingRecipe getRecipe()
	{
		if(isSetUp && inv != null)
		{
			ItemStack[] inputs = {inv[0],inv[1],inv[2],inv[3],inv[4],inv[5],inv[6],inv[7]};
			return WitchingGadgets.instance.customRecipeHandler.getWeavingRecipe(user, inputs);
		}
		return null;
	}

	public boolean isWorking()
	{
		return progress > 0;
	}

	public List<TileEntityLoom> getSlaves()
	{
		if(this.isDummy)return null;
		List<TileEntityLoom> result = new ArrayList<TileEntityLoom>();
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		switch(this.facing)
		{
		case 2: //x-
			if(worldObj.getTileEntity(x, y, z-1) != null && worldObj.getTileEntity(x, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y, z-1));
			if(worldObj.getTileEntity(x, y, z+1) != null && worldObj.getTileEntity(x, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y, z+1));
			if(worldObj.getTileEntity(x, y+1, z-1) != null && worldObj.getTileEntity(x, y+1, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z-1));
			if(worldObj.getTileEntity(x, y+1, z) != null && worldObj.getTileEntity(x, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z));
			if(worldObj.getTileEntity(x, y+1, z+1) != null && worldObj.getTileEntity(x, y+1, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z+1));
			if(worldObj.getTileEntity(x-1, y, z-1) != null && worldObj.getTileEntity(x-1, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z-1));
			if(worldObj.getTileEntity(x-1, y, z) != null && worldObj.getTileEntity(x-1, y, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z));
			if(worldObj.getTileEntity(x-1, y, z+1) != null && worldObj.getTileEntity(x-1, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z+1));
		case 3: //x+
			if(worldObj.getTileEntity(x, y, z-1) != null && worldObj.getTileEntity(x, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y, z-1));
			if(worldObj.getTileEntity(x, y, z+1) != null && worldObj.getTileEntity(x, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y, z+1));
			if(worldObj.getTileEntity(x, y+1, z-1) != null && worldObj.getTileEntity(x, y+1, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z-1));
			if(worldObj.getTileEntity(x, y+1, z) != null && worldObj.getTileEntity(x, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z));
			if(worldObj.getTileEntity(x, y+1, z+1) != null && worldObj.getTileEntity(x, y+1, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z+1));
			if(worldObj.getTileEntity(x+1, y, z-1) != null && worldObj.getTileEntity(x+1, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z-1));
			if(worldObj.getTileEntity(x+1, y, z) != null && worldObj.getTileEntity(x+1, y, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z));
			if(worldObj.getTileEntity(x+1, y, z+1) != null && worldObj.getTileEntity(x+1, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z+1));
		case 4: //z-
			if(worldObj.getTileEntity(x-1, y, z) != null && worldObj.getTileEntity(x-1, y, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z));
			if(worldObj.getTileEntity(x+1, y, z) != null && worldObj.getTileEntity(x+1, y, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z));
			if(worldObj.getTileEntity(x-1, y+1, z) != null && worldObj.getTileEntity(x-1, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y+1, z));
			if(worldObj.getTileEntity(x, y+1, z) != null && worldObj.getTileEntity(x, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z));
			if(worldObj.getTileEntity(x+1, y+1, z) != null && worldObj.getTileEntity(x+1, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y+1, z));
			if(worldObj.getTileEntity(x-1, y, z-1) != null && worldObj.getTileEntity(x-1, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z-1));
			if(worldObj.getTileEntity(x, y, z-1) != null && worldObj.getTileEntity(x, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y, z-1));
			if(worldObj.getTileEntity(x+1, y, z-1) != null && worldObj.getTileEntity(x+1, y, z-1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z-1));
		case 5: //z+
			if(worldObj.getTileEntity(x-1, y, z) != null && worldObj.getTileEntity(x-1, y, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z));
			if(worldObj.getTileEntity(x+1, y, z) != null && worldObj.getTileEntity(x+1, y, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z));
			if(worldObj.getTileEntity(x-1, y+1, z) != null && worldObj.getTileEntity(x-1, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y+1, z));
			if(worldObj.getTileEntity(x, y+1, z) != null && worldObj.getTileEntity(x, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y+1, z));
			if(worldObj.getTileEntity(x+1, y+1, z) != null && worldObj.getTileEntity(x+1, y+1, z) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y+1, z));
			if(worldObj.getTileEntity(x-1, y, z+1) != null && worldObj.getTileEntity(x-1, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x-1, y, z+1));
			if(worldObj.getTileEntity(x, y, z+1) != null && worldObj.getTileEntity(x, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x, y, z+1));
			if(worldObj.getTileEntity(x+1, y, z+1) != null && worldObj.getTileEntity(x+1, y, z+1) instanceof TileEntityLoom)
				result.add((TileEntityLoom) worldObj.getTileEntity(x+1, y, z+1));
		}
		return result;
	}

	public void restoreStructureToOriginal()
	{
		List<TileEntityLoom> slaves = this.getSlaves();
		for(TileEntityLoom slave : slaves)
		{
			slave.restoreOriginalBlock();
		}
		this.restoreOriginalBlock();
	}

	public void restoreOriginalBlock()
	{
		worldObj.setBlock(xCoord, yCoord, zCoord, Block.getBlockById(originalBlock[0]), originalBlock[1], 3);
		this.invalidate();
	}

	private List<TileEntity> findAspectSources(AspectList asp)
	{
		List<TileEntity> result = new ArrayList<TileEntity>();
		Aspect[] array = asp.getAspectsSorted();

		for(int ix=-10;ix<=10;ix++)
			for(int iy=-5;iy<=7;iy++)
				for(int iz=-10;iz<=10;iz++)
				{
					TileEntity tile = this.worldObj.getTileEntity(xCoord+ix, yCoord+iy, zCoord+iz);
					if(tile != null && (tile instanceof IAspectSource))
					{
						for(int iterator=0;iterator<array.length;iterator++)
						{
							Aspect a = array[iterator];
							if(checkAspectSource(tile,a))
								result.add(tile);
						}
					}
				}
		return result;
	}

	private boolean checkAspectSource(TileEntity t, Aspect tag)
	{
		if(!(t instanceof IAspectSource))return false;
		IAspectSource a = (IAspectSource)t;
		return a.doesContainerContainAmount(tag, 1);
	}

	private void consumeAspects(AspectList aspects)
	{
		for(Aspect a:aspects.getAspects())
		{
			if(a != null)
			{
				int amount = aspects.getAmount(a);
				boolean flag = this.storedAspects.reduce(a, amount);
			}
		}
	}

	public TileEntityLoom getMaster()
	{
		if(this.isDummy)
			return (TileEntityLoom) this.worldObj.getTileEntity(this.masterPosition[0],this.masterPosition[1],this.masterPosition[2]);
		return this;
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
		return (int)Math.ceil(this.progress * max);
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		isSetUp = tags.getBoolean("isSetUp");
		isDummy = tags.getBoolean("isDummy");
		if(!isDummy)
		{
			NBTTagList tagList = tags.getTagList("Inventory",10);
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound tag = tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < inv.length) {
					inv[slot] = ItemStack.loadItemStackFromNBT(tag);
				}
			}
		}
		masterPosition = tags.getIntArray("masterPosition");
		facing = tags.getInteger("facing");
		originalBlock = tags.getIntArray("originalBlock");
		if(!isDummy && tags.hasKey("user"))
		try{user = new FakeThaumcraftPlayer(worldObj, new GameProfile(null, tags.getString("user")));}catch(Exception e){}//(worldObj, tags.getString("user"));}catch(Exception e){}
		this.storedAspects.readFromNBT(tags);
		isActive = tags.getBoolean("isActive");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		if(inv!=null)
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
		}
		tags.setIntArray("masterPosition", masterPosition);
		tags.setBoolean("isDummy", isDummy);
		tags.setInteger("facing", facing);
		tags.setBoolean("isSetUp", isSetUp);
		tags.setIntArray("originalBlock", originalBlock);
		if(user != null)
			tags.setString("user", user.getCommandSenderName());
		this.storedAspects.writeToNBT(tags);
		tags.setBoolean("isActive", isActive);
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
		return "Loom";
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

	public boolean setActive(EntityPlayer player)
	{
		boolean result = true;

		if(player == null)result = false;
		this.user = player;
		if(!this.hasWork())result = false;
		if(!this.canWork())result = false;
		if(getRecipe() == null)result = false;
		

		this.isActive = result;

		return isActive;
	}

}