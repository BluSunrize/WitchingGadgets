package witchinggadgets.common.blocks.tiles;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.config.ConfigBlocks;
import witchinggadgets.common.util.EtherealWallMaster;

public class TileEntityEtherealWall extends TileEntityWGBase
{

	public EtherealWallMaster master;

	public Block camoID = null;
	public int camoMeta = -1;
	public int camoRenderType = 0;

	public EtherealWallMaster getMaster()
	{
		EtherealWallMaster masterOV = null;
		EtherealWallMaster masterYmin = null;
		EtherealWallMaster masterYmax = null;
		EtherealWallMaster masterZmin = null;
		EtherealWallMaster masterZmax = null;
		EtherealWallMaster masterXmin = null;
		EtherealWallMaster masterXmax = null;
		if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null
				&& worldObj.getTileEntity(xCoord, yCoord-1, zCoord)instanceof TileEntityEtherealWall
				&& ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).master != null)
		{
			masterYmin = ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).master;
		}
		if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord)instanceof TileEntityEtherealWall && ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).master != null)
		{
			masterYmax = ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).master;
		}
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord-1)instanceof TileEntityEtherealWall && ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).master != null)
		{
			masterZmin = ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).master;
		}
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord+1)instanceof TileEntityEtherealWall && ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).master != null)
		{
			masterZmax = ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).master;
		}
		if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord)instanceof TileEntityEtherealWall && ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).master != null)
		{
			masterXmin = ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).master;
		}
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord)instanceof TileEntityEtherealWall && ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).master != null)
		{
			masterXmax = ((TileEntityEtherealWall)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).master;
		}
		//get Overall Master
		if(masterYmin != null)masterOV = masterYmin;
		else if(masterYmax != null)masterOV = masterYmax;
		else if(masterZmin != null)masterOV = masterZmin;
		else if(masterZmax != null)masterOV = masterZmax;
		else if(masterXmin != null)masterOV = masterXmin;
		else if(masterXmax != null)masterOV = masterXmax;
		//unify where necessary
		if(masterYmin != null && masterYmin!= masterOV)
		{
			masterOV.integrateOtherNet(masterYmin);
			((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).master = masterOV;
		}
		if(masterYmax != null && masterYmax!= masterOV)
		{
			masterOV.integrateOtherNet(masterYmax);
			((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).master = masterOV;
		}
		if(masterZmin != null && masterZmin!= masterOV)
		{
			masterOV.integrateOtherNet(masterZmin);
			((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).master = masterOV;
		}
		if(masterZmax != null && masterZmax!= masterOV)
		{
			masterOV.integrateOtherNet(masterZmax);
			((TileEntityEtherealWall)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).master = masterOV;
		}
		if(masterXmin != null && masterXmin!= masterOV)
		{
			masterOV.integrateOtherNet(masterXmin);
			((TileEntityEtherealWall)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).master = masterOV;
		}
		if(masterXmax != null && masterXmax!= masterOV)
		{
			masterOV.integrateOtherNet(masterXmax);
			((TileEntityEtherealWall)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).master = masterOV;
		}

		return masterOV;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(this.master == null)// || !this.master.tileMap.containsKey(this))
		{
			this.master = getMaster();
			if(this.master == null)this.master = new EtherealWallMaster();
			this.master.addTileToNet(this);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		if(tag.hasKey("camo"))
			camoID = Block.getBlockFromName(tag.getString("camo"));
		camoMeta = tag.getInteger("camoMeta");
		camoRenderType = tag.getInteger("camoRenderType");
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		if(camoID!=null)
			tag.setString("camo",Block.blockRegistry.getNameForObject(camoID));
		tag.setInteger("camoMeta",camoMeta);
		tag.setInteger("camoRenderType",camoRenderType);
	}

	public boolean isRenderTypeValid(int renderType, int blockMeta)
	{
		if(renderType == 0 || renderType == 31 || renderType == 39)
			return true;
		if(renderType == ConfigBlocks.blockWoodenDeviceRI)
			return blockMeta == 2 || blockMeta == 6 || blockMeta == 7;
		if(renderType == ConfigBlocks.blockStoneDeviceRI)
			return blockMeta == 0;
		if(renderType == ConfigBlocks.blockMetalDeviceRI)
			return blockMeta == 9;
		if(renderType == ConfigBlocks.blockCustomOreRI)
			return blockMeta == 0 || blockMeta == 7;
		if(renderType == ConfigBlocks.blockCosmeticOpaqueRI)
			return true;
		return false;
	}

}