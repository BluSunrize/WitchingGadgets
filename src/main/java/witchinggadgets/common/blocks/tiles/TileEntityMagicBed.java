package witchinggadgets.common.blocks.tiles;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class TileEntityMagicBed extends TileEntity
{
	public boolean isHead = false;
	public boolean isOccupied = false;

	public boolean activate(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;

		if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell)
		{
			if (isOccupied)
			{
				EntityPlayer entityplayer1 = null;
				Iterator iterator = world.playerEntities.iterator();

				while (iterator.hasNext())
				{
					EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();

					if (entityplayer2.isPlayerSleeping())
					{
						ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;

						if (chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z)
						{
							entityplayer1 = entityplayer2;
						}
					}
				}

				if (entityplayer1 != null)
				{
//					player.addChatMessage("tile.bed.occupied");
					return true;
				}

				this.isOccupied = false;
			}

			EnumStatus enumstatus = player.sleepInBedAt(x, y, z);

			if (enumstatus == EnumStatus.OK)
			{
				this.isOccupied = true;
				return true;
			}
//			if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW)
//				player.addChatMessage("tile.bed.noSleep");
//			else if (enumstatus == EnumStatus.NOT_SAFE)
//				player.addChatMessage("tile.bed.notSafe");

			return true;
		}
		return true;
	}

	/**
	 * Gets the nearest empty chunk coordinates for the player to wake up from a bed into.
	 */
	public static ChunkCoordinates getNearestEmptyChunkCoordinates(World par0World, int par1, int par2, int par3, int par4)
	{
		//int i1 = par0World.getBlockMetadata(par1, par2, par3);
		//int j1 = BlockDirectional.getDirection(i1);

		for (int k1 = 0; k1 <= 1; ++k1)
		{
//			int l1 = par1 - footBlockToHeadBlockMap[j1][0] * k1 - 1;
//			int i2 = par3 - footBlockToHeadBlockMap[j1][1] * k1 - 1;
//			int j2 = l1 + 2;
//			int k2 = i2 + 2;
//
//			for (int l2 = l1; l2 <= j2; ++l2)
//			{
//				for (int i3 = i2; i3 <= k2; ++i3)
//				{
//					if (par0World.doesBlockHaveSolidTopSurface(l2, par2 - 1, i3) && !par0World.getBlockMaterial(l2, par2, i3).isOpaque() && !par0World.getBlockMaterial(l2, par2 + 1, i3).isOpaque())
//					{
//						if (par4 <= 0)
//						{
//							return new ChunkCoordinates(l2, par2, i3);
//						}
//
//						--par4;
//					}
//				}
//			}
		}

		return null;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
	{
//		if (!isBlockHeadOfBed(par5))
//		{
//			super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
//		}
	}

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
	 * and stop pistons
	 */
	public int getMobilityFlag()
	{
		return 1;
	}

//	@SideOnly(Side.CLIENT)
//
//	/**
//	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
//	 */
//	public int idPicked(World par1World, int par2, int par3, int par4)
//	{
//		return Item.bed.itemID;
//	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
	{
//		if (par6EntityPlayer.capabilities.isCreativeMode && isBlockHeadOfBed(par5))
//		{
//			int i1 = getDirection(par5);
//			par2 -= footBlockToHeadBlockMap[i1][0];
//			par4 -= footBlockToHeadBlockMap[i1][1];
//
//			if (par1World.getBlockId(par2, par3, par4) == this.blockID)
//			{
//				par1World.setBlockToAir(par2, par3, par4);
//			}
//		}
	}
}
