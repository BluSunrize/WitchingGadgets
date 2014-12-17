package witchinggadgets.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityWGBase extends TileEntity
{
	@Override
	public void readFromNBT(NBTTagCompound tags)
	{
		super.readFromNBT(tags);
		readCustomNBT(tags);
	}

	public abstract void readCustomNBT(NBTTagCompound tags);

	@Override
	public void writeToNBT(NBTTagCompound tags)
	{
		super.writeToNBT(tags);
		writeCustomNBT(tags);
	}

	public abstract void writeCustomNBT(NBTTagCompound tags);
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
	}
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		this.readFromNBT(pkt.func_148857_g());
    }
}