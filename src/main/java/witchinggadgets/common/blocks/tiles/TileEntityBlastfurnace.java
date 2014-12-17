package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileBellows;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.recipe.InfernalBlastfurnaceRecipe;

public class TileEntityBlastfurnace extends TileEntityWGBase implements IEssentiaTransport
{
	public byte position = -1;
	public int[] masterPos = {-1,-1,-1};
	int speedupTick = 0;
	int processTick = 0;
	int recipeTime = 0;
	boolean specialFuel;
	public ForgeDirection facing = ForgeDirection.UNKNOWN;
	public boolean active = false;
	List<ItemStack> inputs = new ArrayList();

	@Override
	public void updateEntity()
	{
		if((position!=1&&position!=3&&position!=4&&position!=5&&position!=7 &&position!=10&&position!=12&&position!=14&&position!=16) || this.worldObj.isRemote)
			return;

		if(worldObj.getWorldTime()%5==0 && position==1||position==3||position==5||position==7 && masterPos!=null && worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2]) instanceof TileEntityBlastfurnace)
		{
			TileEntityBlastfurnace master = (TileEntityBlastfurnace) worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2]);
			if(drawEssentia())
				master.speedupTick += 600;
		}

		if(position==10||position==12||position==14||position==16)
		{
			TileEntityBlastfurnace master = (TileEntityBlastfurnace) worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2]);
			if(master.speedupTick <= 0)
				master.speedupTick =  VisNetHandler.drainVis(worldObj, xCoord,yCoord,zCoord, Aspect.FIRE, 5);
		}

		if(position!=4)
			return;

		boolean cooking = false;
		if(processTick>0)
		{
			processTick--;
			cooking = true;
			if(speedupTick>0)
				speedupTick--;

			int calc = calculateTime();
			if(processTick > calc)
				processTick = calc;
		}

		if(processTick<=0 && !inputs.isEmpty())
			if(cooking)
			{
				ItemStack inputStack = inputs.get(0);
				InfernalBlastfurnaceRecipe recipe = InfernalBlastfurnaceRecipe.getRecipe(inputStack);
				ItemStack outputStack = recipe.getOutput();
				outputItem(outputStack);

				if(recipe.getBonus()!=null)
				{
					ItemStack bonus = Utilities.copyStackWithSize(recipe.getBonus(), 0);
					if(getBellows()==0)
					{
						if(this.worldObj.rand.nextInt(4) == 0)
							bonus.stackSize += 1;
					}
					else
						for (int b=0;b<getBellows();b++)
							if (this.worldObj.rand.nextFloat() < 0.44F)
								bonus.stackSize += 1;
					outputItem(bonus);
				}

				inputStack.stackSize--;
				if(inputStack.stackSize>0)
					inputs.set(0, inputStack);
				else
					inputs.remove(0);
				cooking = false;
				this.specialFuel = false;
			}
			else
			{
				ItemStack inputStack = inputs.get(0);
				InfernalBlastfurnaceRecipe recipe = InfernalBlastfurnaceRecipe.getRecipe(inputStack);
				if(recipe!=null)
				{
					this.recipeTime = recipe.getSmeltingTime();
					this.processTick = calculateTime();
					if(recipe.isSpecial())
						this.specialFuel = true;
				}
				else
					inputs.remove(0);
				cooking = true;
			}
		if(cooking!=this.active)
		{
			this.active = cooking;
			this.worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 3, specialFuel?1:0);
			this.worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 4, active?1:0);
		}

	}

	int calculateTime()
	{
		return (this.recipeTime/(this.speedupTick>0?2:1)) - (getBellows()*40);
	}
	boolean drawEssentia()
	{
		TileEntity tile = ThaumcraftApiHelper.getConnectableTile(worldObj, xCoord,yCoord,zCoord, this.facing);
		if(tile!=null)
		{
			ForgeDirection fd = position==1?ForgeDirection.NORTH: position==7?ForgeDirection.SOUTH: position==3?ForgeDirection.WEST: ForgeDirection.EAST;
			IEssentiaTransport et = (IEssentiaTransport)tile;
			if (!et.canOutputTo(fd.getOpposite()))
				return false;
			if ((et.getSuctionAmount(fd.getOpposite()) < getSuctionAmount(fd)) && (et.takeEssentia(getSuctionType(fd), 1, fd.getOpposite())==1))
				return true;
		}
		return false;
	}
	void outputItem(ItemStack item)
	{
		TileEntity inventory = this.worldObj.getTileEntity(this.xCoord+facing.offsetX*2, this.yCoord+1, this.zCoord+facing.offsetZ*2);
		if ((inventory != null) && ((inventory instanceof IInventory)))
			item = InventoryUtils.placeItemStackIntoInventory(item, (IInventory)inventory, this.facing.getOpposite().ordinal(), true);

		if(item != null)
		{
			EntityItem ei = new EntityItem(this.worldObj, this.xCoord+.5D+this.facing.offsetX*1.66D, this.yCoord+1.4D, this.zCoord+.5D+this.facing.offsetZ*1.66D, item.copy());
			ei.motionX = (0.075F * this.facing.offsetX);
			ei.motionY = 0.025000000372529D;
			ei.motionZ = (0.075F * this.facing.offsetZ);
			this.worldObj.spawnEntityInWorld(ei);
		}
		worldObj.addBlockEvent(xCoord,yCoord,zCoord, getBlockType(), 3,0);
	}
	int getBellows()
	{
		int bellows = 0;
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
			if(!fd.equals(ForgeDirection.UP) && !fd.equals(ForgeDirection.DOWN))
			{
				int bx = xCoord + fd.offsetX*2;
				int by = yCoord + fd.offsetY*2;
				int bz = zCoord + fd.offsetZ*2;
				if( worldObj.getTileEntity(bx,by,bz) instanceof TileBellows && (((TileBellows)worldObj.getTileEntity(bx,by,bz)).orientation==fd.getOpposite().ordinal()) && !worldObj.isBlockIndirectlyGettingPowered(bx,by,bz) )
					bellows++;
			}
		return bellows;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		position = tags.getByte("position");
		speedupTick = tags.getInteger("speedupTick");
		processTick = tags.getInteger("processTick");
		recipeTime = tags.getInteger("recipeTime");
		masterPos = tags.getIntArray("masterPos");
		facing = ForgeDirection.getOrientation(tags.getInteger("facing"));
		active = tags.getBoolean("active");
		specialFuel = tags.getBoolean("specialFuel");

		NBTTagList invList = tags.getTagList("inputs", 10);
		inputs.clear();
		for(int i=0;i<invList.tagCount();i++)
			inputs.add( ItemStack.loadItemStackFromNBT(invList.getCompoundTagAt(i)));

	}
	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		tags.setByte("position", position);
		tags.setInteger("speedupTick", speedupTick);
		tags.setInteger("processTick", processTick);
		tags.setInteger("recipeTime", recipeTime);
		tags.setIntArray("masterPos", masterPos);
		tags.setInteger("facing", facing.ordinal());
		tags.setBoolean("active", active);
		tags.setBoolean("specialFuel", specialFuel);

		NBTTagList invList = new NBTTagList();
		for(ItemStack s : inputs)
			invList.appendTag(s.writeToNBT(new NBTTagCompound()));
		//System.out.println("Position: "+position);
	}

	public boolean addStackToInputs(ItemStack stack)
	{
		for(int i=0;i<inputs.size();i++)
			if(this.inputs.get(i)!=null && this.inputs.get(i).isItemEqual(stack) && (this.inputs.get(i).stackSize+stack.stackSize <= stack.getMaxStackSize()))
			{
				this.inputs.get(i).stackSize+=stack.stackSize;
				return true;
			}
		this.inputs.add(stack);
		return true;
	}

	@Override
	public boolean isConnectable(ForgeDirection fd)
	{
		return (position==1&&fd==ForgeDirection.NORTH)||(position==3&&fd==ForgeDirection.WEST)||(position==5&&fd==ForgeDirection.EAST)||(position==7&&fd==ForgeDirection.SOUTH);
	}
	@Override
	public boolean canInputFrom(ForgeDirection fd)
	{
		return position==1||position==3||position==4||position==5||position==7;
	}
	@Override
	public int getSuctionAmount(ForgeDirection fd)
	{
		if(masterPos!=null && worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2]) instanceof TileEntityBlastfurnace && ((TileEntityBlastfurnace)worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2])).speedupTick < 40)
			return 128;
		return 0;
	}
	@Override
	public Aspect getSuctionType(ForgeDirection fd)
	{
		return Aspect.FIRE;
	}
	@Override
	public int getMinimumSuction()
	{
		return 0;
	}


	@Override
	public boolean canOutputTo(ForgeDirection fd)
	{
		return false;
	}
	@Override
	public int getEssentiaAmount(ForgeDirection fd)
	{
		return 0;
	}
	@Override
	public Aspect getEssentiaType(ForgeDirection fd)
	{
		return null;
	}

	@Override
	public boolean renderExtendedTube()
	{
		return false;
	}

	@Override
	public void setSuction(Aspect aspect, int amount){}
	@Override
	public int addEssentia(Aspect arg0, int arg1, ForgeDirection arg2)
	{
		return 0;
	}
	@Override
	public int takeEssentia(Aspect aspect, int amount, ForgeDirection fd) 
	{
		return 0;
	}


	@Override
	public boolean receiveClientEvent(int eventNum, int arg)
	{
		if(eventNum==3)
		{
			this.specialFuel = arg==1;
			worldObj.markBlockRangeForRenderUpdate(xCoord-1,yCoord,zCoord-1, xCoord+1,yCoord+2,zCoord+1);
			return true;
		}
		if(eventNum==4)
		{
			this.active = arg==1;
			worldObj.markBlockRangeForRenderUpdate(xCoord-1,yCoord,zCoord-1, xCoord+1,yCoord+2,zCoord+1);
			return true;
		}
		if(eventNum==3)
		{
			for(int i=0;i<5;i++)
			{
				WitchingGadgets.proxy.createFurnaceOutputBlobFx(worldObj, xCoord, yCoord, zCoord, facing);
				//				float xx = xCoord+.5f+facing.offsetX*1.66f + worldObj.rand.nextFloat()*.3f;
				//				float zz = zCoord+.5f+facing.offsetZ*1.66f + worldObj.rand.nextFloat()*.3f;
				//
				//				EntityLavaFX fb = new EntityLavaFX(worldObj, xx,yCoord+1.3f,zz);
				//				fb.motionY = .2f*worldObj.rand.nextFloat();
				//				float mx = facing.offsetX!=0?(worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f : facing.offsetX*worldObj.rand.nextFloat();
				//				float mz = facing.offsetZ!=0?(worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f : facing.offsetZ*worldObj.rand.nextFloat();
				//				fb.motionX = (0.15f * mx);
				//				fb.motionZ = (0.15f * mz);
				//				FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
			}
			worldObj.playSound(xCoord+.5f+facing.offsetX*1.66f, yCoord+1.3f, zCoord+.5f+facing.offsetZ*1.66f, "liquid.lavapop", 0.1f, 1f+worldObj.rand.nextFloat()*.8f, false);
			return true;
		}
		if(eventNum==5)
		{
			for(int i=0;i<3;i++)
			{
				WitchingGadgets.proxy.createFurnaceDestructionBlobFx(worldObj, xCoord, yCoord, zCoord);
				//				float xx = xCoord+.5f+ worldObj.rand.nextFloat()*.3f;
				//				float zz = zCoord+.5f+ worldObj.rand.nextFloat()*.3f;
				//
				//				EntityLavaFX fb = new EntityLavaFX(worldObj, xx,yCoord+1.5f,zz);
				//				fb.motionY = .2F;
				//				fb.motionX = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f*.15f;
				//				fb.motionZ = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f*.15f;
				//				FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
			}
			worldObj.playSound(xCoord+.5f, yCoord+2.5f, zCoord+.5f, "random.fizz", 0.5f, 2.6f+(worldObj.rand.nextFloat()-worldObj.rand.nextFloat())*.8f, false);
			return true;
		}
		return false;
	}

	public static IIcon icon_bricks;
	public static IIcon[] icon_sideBottom;
	public static IIcon[] icon_sideTop;
	public static IIcon[] icon_cornerBottomL;
	public static IIcon[] icon_cornerBottomR;
	public static IIcon[] icon_cornerTopL;
	public static IIcon[] icon_cornerTopR;
	public static IIcon icon_bottom;
	public static IIcon[] icon_bottomTBLR;
	public static IIcon icon_internal;
	public static IIcon icon_lava;
	public IIcon getTexture(int side)
	{

		int i = (masterPos==null||masterPos.length<3)?0: (worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2]) instanceof TileEntityBlastfurnace && ((TileEntityBlastfurnace)worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2])).active)? (((TileEntityBlastfurnace)worldObj.getTileEntity(masterPos[0],masterPos[1],masterPos[2])).specialFuel?2:1): 0;
		//		if(position == 10 & side ==0)
		//			System.out.println("Texture used: "+i);
		switch(position)
		{
		case 0:
			return side==2?icon_cornerBottomR[i]: side==4?icon_cornerBottomL[i]: side==0?icon_cornerTopL[0]: icon_bricks;
		case 1:
			return side==2?icon_sideBottom[i]: side==0?icon_bottomTBLR[0]: icon_bricks;
		case 2:
			return side==2?icon_cornerBottomL[i]: side==5?icon_cornerBottomR[i]: side==0?icon_cornerTopR[0]: icon_bricks;
		case 3:
			return side==4?icon_sideBottom[i]: side==0?icon_bottomTBLR[2]: icon_bricks;
		case 4:
			return icon_bottom;
		case 5:
			return side==5?icon_sideBottom[i]: side==0?icon_bottomTBLR[3]: icon_bricks;
		case 6:
			return side==3?icon_cornerBottomL[i]: side==4?icon_cornerBottomR[i]: side==0?icon_cornerBottomL[0]: icon_bricks;
		case 7:
			return side==3?icon_sideBottom[i]: side==0?icon_bottomTBLR[1]: icon_bricks;
		case 8:
			return side==3?icon_cornerBottomR[i]: side==5?icon_cornerBottomL[i]: side==0?icon_cornerBottomR[0]: icon_bricks;

		case 9:
			return side==2?icon_cornerTopR[i]: icon_cornerTopL[i];
		case 10:
			return side==2?icon_sideTop[i]: i==1?icon_internal: icon_bricks;
		case 11:
			return side==2?icon_cornerTopL[i]: icon_cornerTopR[i];
		case 12:
			return side==4?icon_sideTop[i]: i==1?icon_internal: icon_bricks;
		case 14:
			return side==5?icon_sideTop[i]: i==1?icon_internal: icon_bricks;
		case 15:
			return side==3?icon_cornerTopL[i]: icon_cornerTopR[i];
		case 16:
			return side==3?icon_sideTop[i]: i==1?icon_internal: icon_bricks;
		case 17:
			return side==3?icon_cornerTopR[i]: icon_cornerTopL[i];
		case 22:
			return icon_lava;
		}

		return icon_bricks;
	}

	public static Block[] brickBlock = new Block[18];
	public static Block stairBlock;
}
