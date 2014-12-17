package witchinggadgets.common.blocks.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import witchinggadgets.client.gui.GuiMagicalTileLock;
import witchinggadgets.common.WGContent;

public class TileEntityMagicalTileLock extends TileEntityWGBase
{
	public int tick = -1;
	public int lockPreset=-1;
	public boolean unlocked = false;
	public byte[] tiles = {1,1,1,1,1,1,1,1,0};

	public TileEntityMagicalTileLock()
	{
		super();
	}
	@Override
	public boolean canUpdate()
	{
		return true;
	}
	@Override
	public void updateEntity()
	{
		if(unlocked && tick>=0)
		{
			if(worldObj.isRemote && Minecraft.getMinecraft().currentScreen instanceof GuiMagicalTileLock)
			{
				Minecraft.getMinecraft().currentScreen = null;
				Minecraft.getMinecraft().setIngameFocus();
			}

			if(tick%5==0)
			{
				int t = tick/5;
				switch(t)
				{
				case 0:
					removeBlock(-1,-1,-1);
					removeBlock( 1, 1, 1);
					break;
				case 1:
					removeBlock( 1,-1,-1);
					removeBlock(-1, 1, 1);
					break;
				case 2:
					removeBlock(-1, 1,-1);
					removeBlock( 1,-1, 1);
					break;
				case 3:
					removeBlock( 1, 1,-1);
					removeBlock(-1,-1, 1);
					break;
				case 4:
					removeBlock(-1, 0,-1);
					removeBlock( 1, 0, 1);
					break;
				case 5:
					removeBlock( 1, 0,-1);
					removeBlock(-1, 0, 1);
					break;
				case 6:
					removeBlock( 0,-1,-1);
					removeBlock( 0, 1, 1);
					break;
				case 7:
					removeBlock(-1,-1, 0);
					removeBlock( 1, 1, 0);
					break;
				case 8:
					removeBlock( 0,-1, 1);
					removeBlock( 0, 1,-1);
					break;
				case 9:
					removeBlock(-1, 1, 0);
					removeBlock( 1,-1, 0);
					break;
				case 10:
					removeBlock( 0, 0,-1);
					removeBlock(-1, 0, 0);
					break;
				case 11:
					removeBlock( 0, 0, 1);
					removeBlock( 1, 0, 0);
					break;
				case 12:
					removeBlock( 0,-1, 0);
					break;
				case 13:
					removeBlock( 0, 1, 0);
					break;
				}

				if(tick>=80)
					removeBlock( 0, 0, 0);
			}
			tick++;
		}
	}

	void removeBlock(int x, int y, int z)
	{
		if(worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z).equals(WGContent.BlockStoneDevice) && (worldObj.getBlockMetadata(xCoord+x, yCoord+y, zCoord+z)==2||worldObj.getBlockMetadata(xCoord+x, yCoord+y, zCoord+z)==1))
		{
			worldObj.setBlockToAir(xCoord+x, yCoord+y, zCoord+z);
			this.worldObj.playSoundEffect(xCoord+x, yCoord+y, zCoord+z, "mob.endermen.portal", 1.0F, 1.0F);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		tick = tags.getInteger("tick");
		lockPreset = tags.getInteger("lockPreset");
		unlocked = tags.getBoolean("unlocked");
		tiles = tags.getByteArray("tiles");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		tags.setInteger("tick", tick);
		tags.setInteger("lockPreset", lockPreset);
		tags.setBoolean("unlocked", unlocked);
		tags.setByteArray("tiles", tiles);
	}

}