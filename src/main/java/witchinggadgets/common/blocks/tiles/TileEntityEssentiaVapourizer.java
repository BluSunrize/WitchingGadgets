package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileEntityEssentiaVapourizer extends TileEntity implements IAspectContainer, IEssentiaTransport
{
	Aspect storedAspect = null;
	AspectList acceptedAspects = new AspectList().add(Aspect.WEAPON, 192);
	int storedAmount = 0;
	int maxAmount = 16;
	int tick = 0;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if ( /*(!this.worldObj.isRemote) &&*/ (++this.tick % 10 == 0) && (this.storedAmount < this.maxAmount))
		{
			drawEssentia(ForgeDirection.UP);
			drawEssentia(ForgeDirection.DOWN);
		}
	}

	private void drawEssentia(ForgeDirection fd)
	{
//		TileEntity tile = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, fd);
//		if (tile != null)
//		{
//			IEssentiaTransport pipe = (IEssentiaTransport)tile;
//			if (!pipe.canOutputTo(fd.getOpposite()))
//				return;
//			Aspect newAspect = null;
//			if ((this.storedAspect != null) && (this.storedAmount > 0))
//			{
//				newAspect = this.storedAspect;
//			}
//			else
//			{
//				AspectList list = pipe.getEssentia(fd.getOpposite());
//				if (list != null)
//					for (Aspect a : list.getAspects())
//						if ((pipe.getSuction(fd.getOpposite()).getAmount(a) < getSuction(fd).getAmount(a)) && (getSuction(fd).getAmount(a) >= pipe.getMinimumSuction()))
//						{
//							newAspect = a;
//							break;
//						}
//			}
//			if ((newAspect != null) && (pipe.getSuction(fd.getOpposite()).getAmount(newAspect) < getSuction(fd).getAmount(newAspect)))
//				addToContainer(newAspect, pipe.takeVis(newAspect, 1));
//		}
	}

	@Override
	public AspectList getAspects()
	{
		AspectList al = new AspectList();
		if ((this.storedAspect != null) && (this.storedAmount > 0))
			al.add(this.storedAspect, this.storedAmount);
		return al;
	}

	@Override
	public void setAspects(AspectList aspects) {}

	@Override
	public boolean doesContainerAccept(Aspect tag)
	{
		return acceptedAspects.aspects.containsKey(tag);
	}

	@Override
	public int addToContainer(Aspect tag, int amount)
	{
		System.out.println("Adding to container: "+amount+" units of "+tag.getLocalizedDescription());
		if (amount == 0)
			return amount;

		if (((this.storedAmount < this.maxAmount) && (tag == this.storedAspect)) || (this.storedAmount == 0))
		{
			this.storedAspect = tag;
			int added = Math.min(amount, this.maxAmount - this.storedAmount);
			this.storedAmount += added;
			amount -= added;
		}
//		this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		return amount;
	}

	@Override
	public boolean takeFromContainer(Aspect tag, int amount) {return false;}

	@Override
	public boolean takeFromContainer(AspectList ot) {return false;}

	@Override
	@Deprecated
	public boolean doesContainerContainAmount(Aspect tag, int amt)
	{
		if ((this.storedAmount >= amt) && (tag == this.storedAspect)) {
			return true;
		}
		return false;
	}

	@Override
	@Deprecated
	public boolean doesContainerContain(AspectList ot)
	{
		for (Aspect a : ot.getAspects()) {
			if ((this.storedAmount > 0) && (a == this.storedAspect)) {
				return true;
			}
		}
		return false;
	}


	@Override
	public int containerContains(Aspect tag)
	{
		return 0;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		writeCustomNBT(tag);
	}
	private void writeCustomNBT(NBTTagCompound tag)
	{
//		if(storedAspect != null && storedAmount > 0)
//		{
//			tag.setString("Aspect", storedAspect.getTag());
//			tag.setInteger("Amount", storedAmount);
//		}
	}
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		readCustomNBT(tag);
	}
	private void readCustomNBT(NBTTagCompound tag)
	{
//		storedAmount = tag.getInteger("Amount");
//		if(storedAmount > 0)
//			storedAspect = Aspect.getAspect(tag.getString("Aspect"));
	}


	@Override
	public boolean isConnectable(ForgeDirection face)
	{
		return face == ForgeDirection.DOWN || face == ForgeDirection.UP;
	}

	@Override
	public boolean canInputFrom(ForgeDirection face)
	{
		return face == ForgeDirection.DOWN || face == ForgeDirection.UP;
	}

	@Override
	public boolean canOutputTo(ForgeDirection face)
	{return false;} 

	@Override
	public void setSuction(Aspect aspect, int amount) {}

	@Override
	public int takeEssentia(Aspect arg0, int arg1, ForgeDirection arg2)
	{
		return 0;
	}
	@Override
	public int getMinimumSuction()
	{return 128;}

	@Override
	public boolean renderExtendedTube()
	{return false;}

//	@Override
//	public Packet getDescriptionPacket()
//	{
//		NBTTagCompound tag = new NBTTagCompound();
//		writeToNBT(tag);
//		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
//	}
//
//	@Override
//	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
//	{
//		super.onDataPacket(net, packet);
//		readCustomNBT(packet.data);
//	}

	@Override
	public Aspect getSuctionType(ForgeDirection face) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSuctionAmount(ForgeDirection face)
	{
		return 0;
//		if (this.storedAmount < this.maxAmount)
//		{
//			if ((this.storedAmount == 0)) {
//				return acceptedAspects;
//			}
//			return new AspectList().merge(this.storedAspect, 192);
//		}
//		return new AspectList();
	}


	@Override
	public int addEssentia(Aspect arg0, int arg1, ForgeDirection arg2)
	{
		return 0;
	}

	@Override
	public Aspect getEssentiaType(ForgeDirection face)
	{
		return (this.storedAspect != null) && (this.storedAmount > 0) ? this.storedAspect : null;
	}

	@Override
	public int getEssentiaAmount(ForgeDirection face)
	{
		return (this.storedAspect != null) && (this.storedAmount > 0) ? this.storedAmount : null;
	}
}