package witchinggadgets.common.blocks.tiles;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;

import org.apache.logging.log4j.Level;

import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.util.network.PacketTileUpdate;
import witchinggadgets.common.world.TeleporterMirror;


public class TileEntityWallMirror extends TileEntityWGBase
{
	public int activationAnimation = 0;
	public int animation = 0;
	public boolean isActive = false;
	public boolean temp_isActivating = false;
	public boolean temp_isDeActivating = false;

	public int facing = 0;

	public boolean isDummy = false;

	public void updateEntity()
	{
		super.updateEntity();
		if(this.isDummy)return;//Dummy Blocks don't do anything :P
		if(temp_isActivating)
		{
			if(activationAnimation < 31)
				activationAnimation ++;
			else
			{
				isActive = true;
				temp_isActivating = false;
			}
			animation=0;
		}
		if(temp_isDeActivating)
		{
			if(activationAnimation > 0)
			{
				if(activationAnimation < 17)activationAnimation --;
				activationAnimation --;
			}
			else
			{
				isActive = false;
				temp_isDeActivating = false;
			}
			animation=0;
		}

		if(isActive && !temp_isActivating && !temp_isDeActivating)
		{
			if(animation < 32)animation++;
			else animation=0;

			int l = this.facing;

			double minX;
			double maxX;
			double minZ;
			double maxZ;
			double minY = this.yCoord;
			double maxY = this.yCoord+2;

			switch (l)
			{ 
			case 2:
			default:
				minX = this.xCoord;
				maxX = this.xCoord+1;
				minZ = this.zCoord+0.75;
				maxZ = this.zCoord+1;
				break;
			case 3:
				minX = this.xCoord;
				maxX = this.xCoord+1;
				minZ = this.zCoord;
				maxZ = this.zCoord+0.25;
				break;
			case 4:
				minX = this.xCoord+0.75;
				maxX = this.xCoord+1;
				minZ = this.zCoord;
				maxZ = this.zCoord+1;
				break;
			case 5:
				minX = this.xCoord;
				maxX = this.xCoord+0.25;
				minZ = this.zCoord;
				maxZ = this.zCoord+1;
				break;
			}

			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(minX,minY,minZ,maxX,maxY,maxZ);

			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			Iterator i = list.iterator();
			while(i.hasNext())
			{
				EntityPlayer p = (EntityPlayer)i.next();
				this.teleportPlayer(p);
			}
		}
	}

	public void teleportPlayer(EntityPlayer player)
	{
		if ((player.ridingEntity == null) && (player.riddenByEntity == null) && ((player instanceof EntityPlayerMP)))
		{
			//System.out.println("Heyo!");
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			MinecraftServer mServer = MinecraftServer.getServer();

			if (playerMP.timeUntilPortal > 0)
			{
				playerMP.timeUntilPortal = 10;
			}
			else
			{		
				int dimID = playerMP.dimension;
				playerMP.timeUntilPortal = 10;
				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, WGConfig.dimensionMirrorID, new TeleporterMirror(mServer.worldServerForDimension(WGConfig.dimensionMirrorID)));
				TileEntityMirrorPortal tile = null;
				try{
					tile = (TileEntityMirrorPortal)playerMP.worldObj.getTileEntity((int)playerMP.posX,(int)playerMP.posY,(int)playerMP.posZ);
				} catch (ClassCastException e) {
					WitchingGadgets.logger.log(Level.ERROR, "[WitchingGadgets] No Portal Tile found on player Arrival in Crystal Void");
					e.printStackTrace();
				}
				if(tile != null)
				{
					tile.linkedDimension = dimID;
					if(!player.worldObj.isRemote)
					{
						tile.linkedDimensionName = MinecraftServer.getServer().worldServers[dimID].provider.getDimensionName();
						WitchingGadgets.packetPipeline.sendToAll(new PacketTileUpdate(tile));
					}
				}
			}
		}
	}

	public void toggleState()
	{
		if(this.isActive && !this.temp_isDeActivating)
			this.temp_isDeActivating = true;
		else if(!this.isActive && !this.temp_isActivating)
			this.temp_isActivating = true;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		isActive = tags.getBoolean("isActive");
		isDummy = tags.getBoolean("isDummy");
		facing = tags.getInteger("facing");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		tags.setBoolean("isActive", isActive);
		tags.setBoolean("isDummy", isDummy);
		tags.setInteger("facing", facing);
	}
	
	public List<EntityPlayer> getMirroredPlayers()
	{
//		System.out.println(facing);
		double minX = facing==2||facing==3?-2: facing==4?-8: -.5;
		double maxX = facing==2||facing==3? 2: facing==4?.5:   8;
		double minZ = facing==4||facing==5?-2: facing==2?-8: -.5;
		double maxZ = facing==4||facing==5? 2: facing==2?.5:   8;
		
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(minX,-1,minZ, maxX,2,maxZ);
		aabb = aabb.getOffsetBoundingBox(xCoord+.5,yCoord+1,zCoord+.5);
		
		return worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
	}

}
