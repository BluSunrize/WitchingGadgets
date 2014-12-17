//package witchinggadgets.common.blocks.tiles;
//
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.ISidedInventory;
//import net.minecraft.item.ItemBlock;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.util.FakePlayerFactory;
//import net.minecraftforge.common.util.ForgeDirection;
//import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
//import thaumcraft.common.lib.FakeThaumcraftPlayer;
//
//public class TileEntityThaumicApplicator extends TileEntity implements ISidedInventory
//{
//	public ForgeDirection facing;
//	FakeThaumcraftPlayer fakePlayer;
//	ItemStack[] inv = new ItemStack[9];
//
//	@Override
//	public void updateEntity()
//	{
//		if(fakePlayer == null)
//			fakePlayer = FakePlayerFactory.get(worldObj, "wg.fakePlayer.ThaumicApplicator");
//		if(facing == null)
//			facing = ForgeDirection.NORTH;
//		useStack();
//	}
//
//	private boolean useStack()
//	{
//		int targetX = this.xCoord + facing.offsetX;
//		int targetY = this.yCoord + facing.offsetY;
//		int targetZ = this.zCoord + facing.offsetZ;
//		boolean flagNoBlock = false;
//		int side = facing.getOpposite().ordinal();
//		if(worldObj.isAirBlock(targetX, targetY, targetZ))
//		{
//			targetY -= 1;
//			side = 1;
//			if(worldObj.isAirBlock(targetX, targetY, targetZ))
//			{
//				targetY += 2;
//				side = 0;
//				if(worldObj.isAirBlock(targetX, targetY, targetZ))
//				{	
//					flagNoBlock = true;
//					targetY -= 1;
//				}
//			}
//		}
//
//		int slot=0;
//		ItemStack stack = getStackInSlot(slot);
//
//		boolean flag = false;
//		int i1;
//		if (stack != null && stack.getItem() != null && !flagNoBlock)
//			if(stack.getItem().onItemUseFirst(stack, fakePlayer, worldObj, targetX, targetY, targetZ, side, 0.5f, 0.5f, 0.5f))
//				return true;
//
//		if (!fakePlayer.isSneaking() || (fakePlayer.getHeldItem() == null || fakePlayer.getHeldItem().getItem().shouldPassSneakingClickToBlock(worldObj, targetX, targetY, targetZ)))
//		{
//			i1 = worldObj.getBlockId(targetX, targetY, targetZ);
//
//			if (i1 > 0 && Block.blocksList[i1].onBlockActivated(worldObj, targetX, targetY, targetZ, fakePlayer, side, 0.5f, 0.5f, 0.5f))
//			{
//				flag = true;
//			}
//		}
//
//		if (!flag && stack != null && stack.getItem() instanceof ItemBlock)
//		{
//			ItemBlock itemblock = (ItemBlock)stack.getItem();
//
//			if (!itemblock.canPlaceItemBlockOnSide(worldObj, targetX, targetY, targetZ, side, fakePlayer, stack))
//			{
//				return false;
//			}
//		}
//
//		if(worldObj.isRemote)
//			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet15Place(targetX, targetY, targetZ, side, stack, 0.5f, 0.5f, 0.5f));
//
//		if (flag)
//		{
//			return true;
//		}
//		else if (stack == null)
//		{
//			return false;
//		}
//		else
//		{
//			if (!stack.tryPlaceItemIntoWorld(fakePlayer, worldObj, targetX, targetY, targetZ, side, 0.5f, 0.5f, 0.5f))
//			{
//				return false;
//			}
//			if (stack.stackSize <= 0)
//			{
//				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(fakePlayer, stack));
//			}
//			return true;
//		}
//	}
//
//	@Override
//	public int getSizeInventory()
//	{
//		return this.inv.length;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int slot)
//	{
//		return this.inv[slot];
//	}
//
//	@Override
//	public ItemStack decrStackSize(int slot, int amount)
//	{
//		if (this.inv[slot] != null)
//		{
//			if (this.inv[slot].stackSize <= amount)
//			{
//				ItemStack itemstack = this.inv[slot];
//				this.inv[slot] = null;
//				return itemstack;
//			}
//			ItemStack itemstack = this.inv[slot].splitStack(amount);
//			if (this.inv[slot].stackSize == 0) {
//				this.inv[slot] = null;
//			}
//			return itemstack;
//		}
//		return null;
//	}
//
//	@Override
//	public ItemStack getStackInSlotOnClosing(int slot)
//	{
//		if (this.inv[slot] != null)
//		{
//			ItemStack itemstack = this.inv[slot];
//			this.inv[slot] = null;
//			return itemstack;
//		}
//		return null;
//	}
//
//	@Override
//	public void setInventorySlotContents(int slot, ItemStack stack)
//	{
//		this.inv[slot] = stack;
//		if ((stack != null) && (stack.stackSize > getInventoryStackLimit())) {
//			stack.stackSize = getInventoryStackLimit();
//		}
//	}
//
//	@Override
//	public String getInventoryName()
//	{
//		return "container.thaumicapplicator";
//	}
//
//	@Override
//	public boolean hasCustomInventoryName()
//	{
//		return true;
//	}
//
//	@Override
//	public int getInventoryStackLimit()
//	{
//		return 64;
//	}
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer entityplayer)
//	{
//		return true;
//	}
//
//	@Override
//	public void openInventory()
//	{}
//
//	@Override
//	public void closeInventory()
//	{}
//
//	@Override
//	public boolean isItemValidForSlot(int slot, ItemStack stack)
//	{
//		return true;
//	}
//
//	@Override
//	public int[] getAccessibleSlotsFromSide(int side)
//	{
//		return new int[]{0,1,2,3,4,5,6,7,8};
//	}
//
//	@Override
//	public boolean canInsertItem(int slot, ItemStack stack, int side) {
//		return true;
//	}
//
//	@Override
//	public boolean canExtractItem(int slot, ItemStack stack, int side) {
//		return true;
//	}
//
//
//	@Override
//	public void writeToNBT(NBTTagCompound tag)
//	{
//		super.writeToNBT(tag);
//		writeCustomNBT(tag);
//	}
//	private void writeCustomNBT(NBTTagCompound tag)
//	{
//		tag.setInteger("facing", this.facing.ordinal());
//	}
//	@Override
//	public void readFromNBT(NBTTagCompound tag)
//	{
//		super.readFromNBT(tag);
//		readCustomNBT(tag);
//	}
//	private void readCustomNBT(NBTTagCompound tag)
//	{
//		this.facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
//	}
////	@Override
////	public Packet getDescriptionPacket()
////	{
////		NBTTagCompound tag = new NBTTagCompound();
////		writeToNBT(tag);
////		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
////	}
////	@Override
////	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
////	{
////		readCustomNBT(packet.data);
////	}
//}
