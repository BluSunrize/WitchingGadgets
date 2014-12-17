package witchinggadgets.common.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import org.apache.logging.log4j.Level;

import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityMirrorPortal;
import witchinggadgets.common.util.Utilities;

public class TeleporterMirror extends Teleporter
{
	private final WorldServer worldServerInstance;

	/** Stores successful portal placement locations for rapid lookup. */
	private final LongHashMap destinationCoordinateCache = new LongHashMap();


	/**
	 * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
	 * location.
	 */
	private final List destinationCoordinateKeys = new ArrayList();

	public TeleporterMirror(WorldServer par1WorldServer)
	{
		super(par1WorldServer);
		this.worldServerInstance = par1WorldServer;
	}

	/**
	 * Place an entity in a nearby portal, creating one if necessary.
	 */
	@Override
	public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
	{
		int x = MathHelper.floor_double(par1Entity.posX);
		int y = MathHelper.floor_double(par1Entity.posY);
		int z = MathHelper.floor_double(par1Entity.posZ);

		if(!this.placeInExistingPortal(par1Entity, par2, par4, par6, par8))
		{
			if(this.makePortal(par1Entity))
				this.placeInExistingPortal(par1Entity, par2, par4, par6, par8);
			else
			{
				WitchingGadgets.logger.log(Level.ERROR, "Creation of Portal unsuccesful!");
				return;
			}
		}

		if(this.worldServerInstance.provider.dimensionId == WGConfig.dimensionMirrorID)
		{
			for(int i = -2; i <= 2; ++i)
				for(int j = -2; j <= 2; ++j)
				{
					int xx = x+i;
					int yy = y-1;
					int zz = z+j;
					if(Utilities.isBlockPlaceable(this.worldServerInstance, xx, yy, zz))this.worldServerInstance.setBlock(xx, yy, zz, WGContent.BlockVoidWalkway);
				}
		}

	}

	/**
	 * Place an entity in a nearby portal which already exists.
	 */
	@Override
	public boolean placeInExistingPortal(Entity entity, double par2, double par4, double par6, float par8)
	{
		int x = (int)entity.posX;
		int y = (int)entity.posY;
		int z = (int)entity.posZ;
		if(entity.dimension == WGConfig.dimensionMirrorID)
		{
			for (int i = -4; i <= 4; ++i)
			{
				for (int j = -3; j <= 3; ++j)
				{
					for (int k = -4; k <= 4; ++k)
					{
						Block b = this.worldServerInstance.getBlock(x+i, y+j, z+k);
						//System.out.println("Id at x="+(x+i)+", y="+(y+j)+", z="+(z+k)+": "+id);
						if(b.equals(WGContent.BlockPortal))
						{
							//System.out.println("Existing Portal Found");
							//entity.setLocationAndAngles((double)x+i, (double)y+j, (double)z+k, entity.rotationYaw, 0.0F);
							entity.motionX = entity.motionY = entity.motionZ = 0.0D;
							return true;
						}
					}
				}
			}
		}
		else
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean makePortal(Entity entity)
	{
		int x = (int)entity.posX;
		int y = (int)entity.posY;
		int z = (int)entity.posZ;

		//System.out.println("Forcing Portal creation");
		if(entity.dimension == WGConfig.dimensionMirrorID)
		{
			if(Utilities.isBlockPlaceable(this.worldServerInstance, x, y, z) && Utilities.isBlockPlaceable(this.worldServerInstance, x, y+1, z))
			{

				if(this.worldServerInstance.setBlock(x, y, z, WGContent.BlockPortal))
				{
					TileEntityMirrorPortal tile = (TileEntityMirrorPortal)this.worldServerInstance.getTileEntity(x, y, z);
					if(tile != null)
					{
						tile.tickCounter = 120;
						return true;
					}
				}

			}

			for (int i = -4; i <= 4; ++i)
			{
				for (int j = -4; j <= 4; ++j)
				{
					if(Utilities.isBlockPlaceable(this.worldServerInstance, x+i, y, z+j) && Utilities.isBlockPlaceable(this.worldServerInstance, x+i, y+1, z+j))
					{
						if(this.worldServerInstance.setBlock(x+i, y, z+j, WGContent.BlockPortal))
						{
							TileEntityMirrorPortal tile = (TileEntityMirrorPortal)this.worldServerInstance.getTileEntity(x+i, y, z+j);
							if(tile != null)
							{
								tile.tickCounter = 120;
								return true;
							}
						}
					}
				}
			}
		}
		else
		{
			return true;
		}
		return false;
	}

	/**
	 * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
	 * WorldServer.getTotalWorldTime() value.
	 */
	@Override
	public void removeStalePortalLocations(long par1)
	{
		if (par1 % 100L == 0L)
		{
			Iterator iterator = this.destinationCoordinateKeys.iterator();
			long j = par1 - 600L;

			while (iterator.hasNext())
			{
				Long olong = (Long)iterator.next();
				PortalPosition portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(olong.longValue());

				if (portalposition == null || portalposition.lastUpdateTime < j)
				{
					iterator.remove();
					this.destinationCoordinateCache.remove(olong.longValue());
				}
			}
		}
	}
}