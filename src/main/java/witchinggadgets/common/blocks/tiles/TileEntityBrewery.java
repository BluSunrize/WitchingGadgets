package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBrewery extends TileEntityWGBase
{
	int tick;
	
	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		tick = tag.getInteger("tick");
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("tick", tick);
	}
}