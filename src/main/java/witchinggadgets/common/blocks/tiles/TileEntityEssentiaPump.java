package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.tiles.TileMirrorEssentia;
import cpw.mods.fml.common.network.NetworkRegistry;

public class TileEntityEssentiaPump extends TileEntityWGBase implements IEssentiaTransport
{
	int tick;
	public ForgeDirection facing = ForgeDirection.NORTH;
	Aspect aspect  = null;
	int amount = 0;

	@Override
	public boolean canUpdate()
	{
		return true;
	}
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			Aspect a = null;
			if(worldObj.getTileEntity(xCoord+facing.offsetX,yCoord+facing.offsetY,zCoord+facing.offsetZ) instanceof IEssentiaTransport)
				a = ((IEssentiaTransport)worldObj.getTileEntity(xCoord+facing.offsetX,yCoord+facing.offsetY,zCoord+facing.offsetZ)).getSuctionType(facing.getOpposite());
			if(a!=null && (aspect==null||aspect==a))
			{
				for(TileMirrorEssentia mirror : getMirrors())
					if(mirror.takeFromContainer(a,1))
					{
						if(aspect==null)
							aspect = a;
						amount++;
						this.markDirty();
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						PacketHandler.INSTANCE.sendToAllAround(new PacketFXEssentiaSource(xCoord,yCoord,zCoord, (byte)(xCoord-mirror.xCoord), (byte)(yCoord-mirror.yCoord), (byte)(zCoord-mirror.zCoord), aspect.getColor()), new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId,xCoord,yCoord,zCoord, 32.0D));
					}
			}
		}
	}

	ArrayList<TileMirrorEssentia> getMirrors()
	{
		ArrayList<TileMirrorEssentia> list = new ArrayList<TileMirrorEssentia>();
		int range = 8;
		ForgeDirection fd = facing.getOpposite();
		for(int h=-range; h<=range; h++)
			for(int w=-range; w<=range; w++)
				for (int l=1; l<range; l++)
				{
					int xx = xCoord;
					int yy = yCoord;
					int zz = zCoord;
					if(fd.offsetY!=0)
					{
						xx += w;
						yy += l*fd.offsetY;
						zz += h;
					}
					else if(fd.offsetX!=0)
					{
						xx += l*fd.offsetX;
						yy += h;
						zz += w;
					}
					else
					{
						xx += w;
						yy += h;
						zz += l*fd.offsetZ;
					}
					TileEntity te = worldObj.getTileEntity(xx, yy, zz);
					if(te!=null && te instanceof TileMirrorEssentia && canSeeMirror((TileMirrorEssentia) te))
						list.add((TileMirrorEssentia) te);
				}
		return list;
	}

	boolean canSeeMirror(TileMirrorEssentia tile)
	{
		Vec3 tPos = Vec3.createVectorHelper(xCoord+.5+facing.getOpposite().offsetX,yCoord+.5+facing.getOpposite().offsetY,zCoord+.5+facing.getOpposite().offsetZ);
		ForgeDirection fd = ForgeDirection.getOrientation(worldObj.getBlockMetadata(tile.xCoord,tile.yCoord,tile.zCoord)%6);
		Vec3 mPos = Vec3.createVectorHelper(tile.xCoord+.5+fd.offsetX,yCoord+.5+fd.offsetY,zCoord+.5+fd.offsetZ);
		MovingObjectPosition mop =  worldObj.rayTraceBlocks(tPos, mPos);
		return mop==null;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		tick = tag.getInteger("tick");
		facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
		aspect = Aspect.getAspect(tag.getString("aspect"));
		amount = tag.getInteger("amount");
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("tick", tick);
		tag.setInteger("facing", facing.ordinal());
		if(aspect!=null)
			tag.setString("aspect", aspect.getTag());
		tag.setInteger("amount", amount);
	}

	@Override
	public int addEssentia(Aspect arg0, int arg1, ForgeDirection fd)
	{
		return 0;
	}
	@Override
	public boolean canInputFrom(ForgeDirection fd)
	{
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection fd)
	{
		return fd!=null && fd.equals(facing);
	}

	@Override
	public int getEssentiaAmount(ForgeDirection fd)
	{
		return amount;
	}

	@Override
	public Aspect getEssentiaType(ForgeDirection arg0)
	{
		return aspect;
	}

	@Override
	public int getMinimumSuction()
	{
		return 0;
	}
	@Override
	public int getSuctionAmount(ForgeDirection arg0)
	{
		return 0;
	}
	@Override
	public Aspect getSuctionType(ForgeDirection arg0)
	{
		return null;
	}

	@Override
	public boolean isConnectable(ForgeDirection fd)
	{
		return fd!=null && fd.equals(facing);
	}
	@Override
	public boolean renderExtendedTube()
	{
		return false;
	}

	@Override
	public void setSuction(Aspect arg0, int arg1)
	{
	}

	@Override
	public int takeEssentia(Aspect aspect, int am, ForgeDirection fd)
	{
		if(amount>=am)
		{
			amount -= am;
			if(amount<=0)
				this.aspect = null;
			return amount;
		}
		return 0;
	}
}