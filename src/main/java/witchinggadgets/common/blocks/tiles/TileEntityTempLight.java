package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.tiles.TileArcaneLampLight;

public class TileEntityTempLight extends TileArcaneLampLight
{
	int tick = 0;
	public int tickMax = 20*20;

	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
			if (this.tick < tickMax)
				tick++;
			else
				this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
	}

	public void readCustomNBT(NBTTagCompound tag)
	{
		this.tick = tag.getInteger("tickCount");
	}

	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("tickCount", this.tick);
	}
}
