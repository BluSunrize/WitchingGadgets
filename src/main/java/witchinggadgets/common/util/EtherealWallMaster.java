package witchinggadgets.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import witchinggadgets.common.blocks.tiles.TileEntityEtherealWall;

public class EtherealWallMaster
{
	public List<TileEntityEtherealWall> tileMap;

	public EtherealWallMaster()
	{
		this.tileMap = new ArrayList<TileEntityEtherealWall>();
	}

	public boolean isAnyTileInNetPowered()
	{
		for(TileEntityEtherealWall tile:tileMap)
			if(tile.getWorldObj().isBlockIndirectlyGettingPowered(tile.xCoord, tile.yCoord, tile.zCoord))return true;
		return false;
	}

	public boolean addTileToNet(TileEntityEtherealWall tile)
	{
		if(this.tileMap.contains(tile))
			return false;
		this.tileMap.add(tile);
		tile.master = this;
		return true;
	}

	public boolean removeTileFromNet(TileEntityEtherealWall tile)
	{
		this.tileMap.remove(tile);
		tile.master = null;
		return true;
	}

	/**
	 * Disbands Net and sets every Tile's master to null.
	 * Allows Tiles to form new nets.
	 * Used to allow an Net to be split.
	 */
	public void freeSlaves()
	{
		for(TileEntityEtherealWall tile:tileMap)
		{
			tile.master = null;
		}
		this.tileMap = new ArrayList<TileEntityEtherealWall>();
	}

	public void checkNetIntegrity(TileEntityEtherealWall tile)
	{
//		//Get adjacent tiles
//		TileEntityEtherealWall adjYneg = null;
//		TileEntityEtherealWall adjYpos = null;
//		TileEntityEtherealWall adjZneg = null;
//		TileEntityEtherealWall adjZpos = null;
//		TileEntityEtherealWall adjXneg = null;
//		TileEntityEtherealWall adjXpos = null;
//
//		if(tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord-1,tile.zCoord) instanceof TileEntityEtherealWall)adjYneg = (TileEntityEtherealWall)tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord-1,tile.zCoord);
//		if(tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord+1,tile.zCoord) instanceof TileEntityEtherealWall)adjYpos = (TileEntityEtherealWall)tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord+1,tile.zCoord);
//		if(tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord,tile.zCoord-1) instanceof TileEntityEtherealWall)adjZneg = (TileEntityEtherealWall)tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord,tile.zCoord-1);
//		if(tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord,tile.zCoord+1) instanceof TileEntityEtherealWall)adjZpos = (TileEntityEtherealWall)tile.getWorldObj().getTileEntity(tile.xCoord,tile.yCoord,tile.zCoord+1);
//		if(tile.getWorldObj().getTileEntity(tile.xCoord-1,tile.yCoord,tile.zCoord) instanceof TileEntityEtherealWall)adjXneg = (TileEntityEtherealWall)tile.getWorldObj().getTileEntity(tile.xCoord-1,tile.yCoord,tile.zCoord);
//		if(tile.getWorldObj().getTileEntity(tile.xCoord+1,tile.yCoord,tile.zCoord) instanceof TileEntityEtherealWall)adjXpos = (TileEntityEtherealWall)tile.getWorldObj().getTileEntity(tile.xCoord+1,tile.yCoord,tile.zCoord);
	}

	public TileEntityEtherealWall[] sortTilesByDistanceTo(int x,int y,int z)
	{
		TileEntityEtherealWall[] result = new TileEntityEtherealWall[tileMap.size()];
		int counter = 0;
		for(TileEntityEtherealWall tile:tileMap)
		{
			result[counter] = tile;
			counter++;
		}
		return result;
	}

	@SuppressWarnings("unused")
	private boolean areTilesAdjacent(TileEntity par1, TileEntity par2)
	{
		boolean sameX = par1.xCoord == par2.xCoord;
		boolean sameY = par1.yCoord == par2.yCoord;
		boolean sameZ = par1.zCoord == par2.zCoord;
		if(sameX&&sameY)
		{
			if(Math.abs(par1.zCoord-par2.zCoord) == 1)return true;
			return false;
		}
		if(sameZ&&sameY)
		{
			if(Math.abs(par1.xCoord-par2.xCoord) == 1)return true;
			return false;
		}
		if(sameX&&sameZ)
		{
			if(Math.abs(par1.yCoord-par2.yCoord) == 1)return true;
			return false;
		}
		return false;
	}

	public void integrateOtherNet(EtherealWallMaster net)
	{
		this.tileMap.addAll(net.tileMap);
	}
}
