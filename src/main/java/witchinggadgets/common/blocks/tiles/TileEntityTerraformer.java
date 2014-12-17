package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.Utils;
import witchinggadgets.api.ITerraformFocus;

public class TileEntityTerraformer extends TileEntityWGBase implements IAspectContainer, IEssentiaTransport
{
	private Aspect currentSuction;
	private AspectList essentia = new AspectList();
	int tick=0;

	@Override
	public void updateEntity()
	{
		tick+=1;
		if(worldObj.getBlock(xCoord, yCoord+1, zCoord) instanceof ITerraformFocus || worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof ITerraformFocus)
		{
			ITerraformFocus focus = null;
			if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof ITerraformFocus)
				focus = (ITerraformFocus) worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
			else
				focus = (ITerraformFocus) worldObj.getBlock(xCoord, yCoord+1, zCoord);
			if(this.getSuctionType(null)!=focus.requiredAspect(worldObj, xCoord, yCoord+1, zCoord))
				this.setSuction( focus.requiredAspect(worldObj, xCoord, yCoord+1, zCoord), 0 );

			if(!worldObj.isRemote && this.currentSuction!=null)
			{
				if(drawEssentia())
					this.addToContainer(this.currentSuction, 1);

				if(tick>=20 && this.essentia.getAmount(currentSuction) >= 4)
				{
					BiomeGenBase transformBiome = focus.getCreatedBiome(worldObj, xCoord, yCoord+1, zCoord);
					int x = xCoord + worldObj.rand.nextInt(16) - worldObj.rand.nextInt(16);
					int z = zCoord + worldObj.rand.nextInt(16) - worldObj.rand.nextInt(16);
					int to = 0;
					while(to<40 && this.worldObj.getBiomeGenForCoords(x, z).biomeID == transformBiome.biomeID)
					{
						to++;
						x = xCoord + worldObj.rand.nextInt(16) - worldObj.rand.nextInt(16);
						z = zCoord + worldObj.rand.nextInt(16) - worldObj.rand.nextInt(16);
					}
					if(worldObj.getBiomeGenForCoords(x, z).biomeID != transformBiome.biomeID && VisNetHandler.drainVis(worldObj, xCoord, yCoord, zCoord, Aspect.EARTH, 4)==4)
					{
						Utils.setBiomeAt(worldObj, x, z, transformBiome);
						this.takeFromContainer(currentSuction, 4);
						for(int j=0; j<4; j++)
							for(int i=0; i<4; i++)
							{
								ForgeDirection fd = ForgeDirection.getOrientation(2+i);
								double x1 = xCoord+.5+ (.6875*fd.offsetX);
								double y1 = yCoord+.875;
								double z1 = zCoord+.5+ (.6875*fd.offsetZ);
								double x2 = .1875;
								double y2 = .25;
								double z2 = .1875;
								Thaumcraft.proxy.drawVentParticles(worldObj, x1,y1,z1, x2,y2,z2, currentSuction.getColor(), 2);
							}
					}
				}
			}
		}
		else if(this.getSuctionType(null)!=null)
			this.setSuction(null,0);
		if(tick>=20)
			tick=0;
	}

	int drawDelay = 0;
	boolean drawEssentia()
	{
		if (++this.drawDelay % 5 != 0)
			return false;
		TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.DOWN);
		if(te!=null)
		{
			IEssentiaTransport ic = (IEssentiaTransport)te;
			if (!ic.canOutputTo(ForgeDirection.UP))
				return false;
			if ((ic.getSuctionAmount(ForgeDirection.UP) < getSuctionAmount(ForgeDirection.DOWN)) && (ic.takeEssentia(this.currentSuction, 1, ForgeDirection.UP) == 1))
				return true;
		}
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		this.essentia.readFromNBT(tags);
		tick = tags.getInteger("tick");
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		this.essentia.writeToNBT(tags);
		tags.setInteger("tick", tick);
		//		System.out.println("reading tick: "+tick);
	}

	@Override
	public int addEssentia(Aspect a, int amount, ForgeDirection fd)
	{
		return canInputFrom(fd) ? amount - addToContainer(a, amount) : 0;
	}
	@Override
	public boolean canInputFrom(ForgeDirection fd)
	{
		return fd==ForgeDirection.DOWN;
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
	public int getMinimumSuction()
	{
		return 0;
	}
	@Override
	public int getSuctionAmount(ForgeDirection fd)
	{
		return this.currentSuction != null ? 128 : 0;
	}
	@Override
	public Aspect getSuctionType(ForgeDirection fd)
	{
		return this.currentSuction;
	}
	@Override
	public boolean isConnectable(ForgeDirection fd)
	{
		return fd==ForgeDirection.DOWN;
	}
	@Override
	public boolean renderExtendedTube()
	{
		return false;
	}
	@Override
	public void setSuction(Aspect a, int amount)
	{
		this.currentSuction = a;
	}
	@Override
	public int takeEssentia(Aspect a, int amount, ForgeDirection fd)
	{
		return (canOutputTo(fd)) && (takeFromContainer(a, amount)) ? amount : 0;
	}
	@Override
	public int addToContainer(Aspect a, int amount)
	{
		this.essentia.add(a, amount);
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		markDirty();
		return 0;
	}
	@Override
	public int containerContains(Aspect a)
	{
		return this.essentia.getAmount(a);
	}
	@Override
	public boolean doesContainerAccept(Aspect a)
	{
		return a.equals(currentSuction);
	}
	@Override
	public boolean doesContainerContain(AspectList al)
	{
		return false;
	}
	@Override
	public boolean doesContainerContainAmount(Aspect a, int amount)
	{
		return this.essentia.getAmount(a) >= amount;
	}
	@Override
	public AspectList getAspects()
	{
		return this.essentia;
	}
	@Override
	public void setAspects(AspectList al)
	{
		this.essentia = al;
	}
	@Override
	public boolean takeFromContainer(AspectList al)
	{
		return false;
	}
	@Override
	public boolean takeFromContainer(Aspect a, int amount)
	{
		if (this.essentia.getAmount(a) >= amount)
		{
			this.essentia.remove(a, amount);
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			markDirty();
			return true;
		}
		return false;
	}

}
