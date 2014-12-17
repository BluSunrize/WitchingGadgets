package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTelescope extends TileEntityWGBase
{
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
	}
}