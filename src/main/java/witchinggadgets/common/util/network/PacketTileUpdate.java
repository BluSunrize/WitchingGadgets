package witchinggadgets.common.util.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import witchinggadgets.common.blocks.tiles.TileEntityWGBase;

public class PacketTileUpdate extends AbstractPacket
{
	int worldId;
	int x;
	int y;
	int z;
	NBTTagCompound tag;

	public PacketTileUpdate(){}
	public PacketTileUpdate(TileEntityWGBase te)
	{
		this.worldId = te.getWorldObj().provider.dimensionId;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.tag = new NBTTagCompound();
		te.writeCustomNBT(this.tag);
	}


	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(worldId);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.worldId = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.tag = ByteBufUtils.readTag(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer)
	{
		World world = DimensionManager.getWorld(this.worldId);
		if (world == null)
			return;
		if(world.getTileEntity(x, y, z)!=null && world.getTileEntity(x, y, z) instanceof TileEntityWGBase)
			((TileEntityWGBase)world.getTileEntity(x, y, z)).readCustomNBT(tag);
	}

	@Override
	public void handleServerSide(EntityPlayer p2)
	{
		World world = DimensionManager.getWorld(this.worldId);
		if (world == null)
			return;
		if(world.getTileEntity(x, y, z)!=null && world.getTileEntity(x, y, z) instanceof TileEntityWGBase)
			((TileEntityWGBase)world.getTileEntity(x, y, z)).readCustomNBT(tag);
	}

}
