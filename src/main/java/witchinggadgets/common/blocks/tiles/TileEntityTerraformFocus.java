package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTerraformFocus extends TileEntityWGBase
{
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
	}
}