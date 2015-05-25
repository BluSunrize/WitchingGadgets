package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntitySaunaStove extends TileEntityWGBase implements IFluidHandler
{
	public static HashMap<Integer,TileEntitySaunaStove> targetedPlayers = new HashMap();

	public AxisAlignedBB[] boundingBoxes = new AxisAlignedBB[0];

	public FluidTank tank = new FluidTank(4000);
	public int tick;

	public void prepareAreaCheck()
	{
		recheck = true;
		openList.clear();
		closedList.clear();
		checked.clear();
		outlineMinX=xCoord;
		outlineMaxX=xCoord;
		outlineMinY=yCoord;
		outlineMaxY=yCoord;
		outlineMinZ=zCoord;
		outlineMaxZ=zCoord;
		openList.add(new ChunkCoordinates(xCoord+1,yCoord,zCoord));
		openList.add(new ChunkCoordinates(xCoord-1,yCoord,zCoord));
		openList.add(new ChunkCoordinates(xCoord,yCoord,zCoord+1));
		openList.add(new ChunkCoordinates(xCoord,yCoord,zCoord-1));
		openList.add(new ChunkCoordinates(xCoord,yCoord+1,zCoord));
	}
	public void updateEntity()
	{
		super.updateEntity();
		if(tick>0 && worldObj.getTotalWorldTime()%10==0)
		{
			tick--;
			if(boundingBoxes.length<=0 && !recheck)
				prepareAreaCheck();
		}
		if(!recheck && tick<=0)
		{
			if(this.tank.getFluidAmount()>=1000)
			{
				this.tank.drain(1000, true);
				prepareAreaCheck();
				tick = 1200;
			}
		}
		if(recheck)
			checkArea();
		if(recreate)
			createAABBs();
		if(worldObj.getTotalWorldTime()%20==0 && tick>0)
		{
			for(AxisAlignedBB aabb : boundingBoxes)
			{
				List<EntityPlayer> l = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				for(EntityPlayer pl : l)
					targetedPlayers.put(pl.getEntityId(),this);
			}
		}
	}

	boolean recheck = false;
	List<ChunkCoordinates> openList = new ArrayList();
	List<ChunkCoordinates> closedList = new ArrayList();
	List<ChunkCoordinates> checked = new ArrayList();
	int outlineMinX=xCoord;
	int outlineMaxX=xCoord;
	int outlineMinY=yCoord;
	int outlineMaxY=yCoord;
	int outlineMinZ=zCoord;
	int outlineMaxZ=zCoord;
	public void checkArea()
	{
		ChunkCoordinates next = null;
		final int closedListMax = 1200;
		int timeout = 0;
		while(timeout<30 && closedList.size()<closedListMax && !openList.isEmpty())
		{
			timeout++;
			next = openList.get(0);
			if(!checked.contains(next))
			{
				boolean overWater = worldObj.isAABBInMaterial(AxisAlignedBB.getBoundingBox(next.posX,yCoord-6,next.posZ, next.posX+1,yCoord+6,next.posZ+1), Material.water);//waterColumns.contains(new ChunkCoordIntPair(cc.posX,cc.posZ));
				if(isBlockValidForSteamPassing(next.posX,next.posY,next.posZ) && ((xCoord-next.posX)*(xCoord-next.posX)+(zCoord-next.posZ)*(zCoord-next.posZ))<64 &&(next.posY-yCoord<(overWater?2:6) && next.posY-yCoord>-6 ))
				{
					closedList.add(next);
					if(next.posX<outlineMinX)
						outlineMinX=next.posX;
					if(next.posX>outlineMaxX)
						outlineMaxX=next.posX;

					if(next.posY<outlineMinY)
						outlineMinY=next.posY;
					if(next.posY>outlineMaxY)
						outlineMaxY=next.posY;

					if(next.posZ<outlineMinZ)
						outlineMinZ=next.posZ;
					if(next.posZ>outlineMaxZ)
						outlineMaxZ=next.posZ;

					for(ChunkCoordinates cc2 : getConnected(next.posX, next.posY, next.posZ))
						if(!checked.contains(cc2) && !closedList.contains(cc2) && !openList.contains(cc2) && isBlockValidForSteamPassing(cc2.posX, cc2.posY, cc2.posZ))
							openList.add(cc2);
				}
				checked.add(next);
			}
			openList.remove(0);
		}
		if(closedList.size()>=closedListMax || openList.isEmpty())
		{
			recheck = false;

			aabbList.clear();
			aabbUsedBlocks.clear();
			recreate=true;
		}
	}

	List<AxisAlignedBB> aabbList = new ArrayList();
	Set<ChunkCoordinates> aabbUsedBlocks = new HashSet();
	Vec3 start=null;
	boolean recreate = false;
	void createAABBs()
	{
		int timeoutAABB=0;
		while(aabbUsedBlocks.size()<closedList.size() && timeoutAABB<1)
		{
			start=null;
			timeoutAABB++;
			//int boxCol=0xffffff;
			boolean flag=false;
			for(int yy=outlineMinY; yy<=outlineMaxY; yy++)
			{
				for(int zz=outlineMinZ; zz<=outlineMaxZ; zz++)
				{	
					for(int xx=outlineMinX; xx<=outlineMaxZ; xx++)
						if( closedList.contains(new ChunkCoordinates(xx,yy,zz)) && !aabbUsedBlocks.contains(new ChunkCoordinates(xx,yy,zz)))
						{
							start = Vec3.createVectorHelper(xx,yy,zz);
							//boxCol = pixels[yy][zz][xx];
							flag = true;
							break;
						}
					if(flag)
						break;
				}
				if(flag)
					break;
			}
			if(start!=null)
			{
				int minX = (int)start.xCoord;
				int minY = (int)start.yCoord;
				int minZ = (int)start.zCoord;

				int maxX = (int)start.xCoord;
				int maxY = (int)start.yCoord;
				int maxZ = (int)start.zCoord;
				for(;maxY<minY+32;maxY++)
					//					if(pixels[maxY][minZ][minX]<0 || usedBlocks.contains(new PixelCoords(minX,maxY,minZ)))
					if(!closedList.contains(new ChunkCoordinates(minX,maxY,minZ)) || aabbUsedBlocks.contains(new ChunkCoordinates(minX,maxY,minZ)))
						break;
				for(;maxZ<minZ+32;maxZ++)
					if(/*pixels[minY][maxZ][minX]<0*/!closedList.contains(new ChunkCoordinates(minX,maxY,minZ)) || aabbUsedBlocks.contains(new ChunkCoordinates(minX,minY,maxZ)))
						if(!closedList.contains(new ChunkCoordinates(minX,minY,maxZ)) || aabbUsedBlocks.contains(new ChunkCoordinates(minX,minY,maxZ)))
							break;
				for(;maxX<minX+32;maxX++)
					//					if(pixels[minY][minZ][maxX]<0 || usedBlocks.contains(new PixelCoords(maxX,minY,minZ)))
					if(!closedList.contains(new ChunkCoordinates(maxX,minY,minZ)) || aabbUsedBlocks.contains(new ChunkCoordinates(maxX,minY,minZ)))
						break;

				for(int zz=minZ;zz<maxZ;zz++)
				{
					boolean row = true;
					for(int xx=minX;xx<maxX;xx++)
						//						if(pixels[minY][zz][xx]<0 || usedBlocks.contains(new PixelCoords(xx,minY,zz)))
						if(!closedList.contains(new ChunkCoordinates(xx,minY,zz)) || aabbUsedBlocks.contains(new ChunkCoordinates(xx,minY,zz)))
							row=false;
					if(!row)
					{
						maxZ = zz;
						break;
					}
				}
				for(int yy=minY;yy<maxY;yy++)
				{
					boolean layer = true;
					for(int zz=minZ;zz<maxZ;zz++)
						for(int xx=minX;xx<maxX;xx++)
							//							if(pixels[yy][zz][xx]<0 || usedBlocks.contains(new PixelCoords(xx,yy,zz)))
							if(!closedList.contains(new ChunkCoordinates(xx,yy,zz)) || aabbUsedBlocks.contains(new ChunkCoordinates(xx,yy,zz)))
								layer=false;
					if(!layer)
					{
						maxY = yy;
						break;
					}
				}

				for(int yy=minY;yy<maxY;yy++)
					for(int zz=minZ;zz<maxZ;zz++)
						for(int xx=minX;xx<maxX;xx++)
							aabbUsedBlocks.add(new ChunkCoordinates(xx, yy, zz));
				aabbList.add(AxisAlignedBB.getBoundingBox(minX,minY,minZ,maxX,maxY,maxZ));
			}
		}
		if(aabbUsedBlocks.size()>=closedList.size())
		{
			recreate = false;
			boundingBoxes = aabbList.toArray(new AxisAlignedBB[0]);
		}
	}

	boolean isBlockValidForSteamPassing(int x, int y ,int z)
	{
		return worldObj.isAirBlock(x, y, z) || worldObj.getBlock(x, y, z).getCollisionBoundingBoxFromPool(worldObj, x, y, z)==null;
	}

	ChunkCoordinates[] getConnected(int x, int y, int z)
	{
		return new ChunkCoordinates[]{new ChunkCoordinates(x-1,y,z),new ChunkCoordinates(x+1,y,z),new ChunkCoordinates(x,y,z-1),new ChunkCoordinates(x,y,z+1),new ChunkCoordinates(x,y-1,z),new ChunkCoordinates(x,y+1,z)};
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("tick", this.tick);
		tank.writeToNBT(tag);
	}
	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		this.tick = tag.getInteger("tick");
		tank.readFromNBT(tag);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(from != ForgeDirection.UP && resource.getFluid()==FluidRegistry.WATER)
		{
			int filled = tank.fill(resource, doFill);
			if(filled>0 && doFill)
				worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
			return filled;
		}
		return 0;
	}
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return drain(from,resource.amount,doDrain);
	}
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if(from != ForgeDirection.UP)
		{
			FluidStack drained = tank.drain(maxDrain, doDrain);
			if(drained != null && doDrain)
				worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
			return drained;
		}
		return null;
	}
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return from!=ForgeDirection.UNKNOWN&&from!=ForgeDirection.UP&&fluid==FluidRegistry.WATER;
	}
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from!=ForgeDirection.UNKNOWN&&from!=ForgeDirection.UP;
	}
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}
}