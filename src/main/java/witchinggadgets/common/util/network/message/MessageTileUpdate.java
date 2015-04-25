package witchinggadgets.common.util.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import witchinggadgets.common.blocks.tiles.TileEntityWGBase;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageTileUpdate implements IMessage
{
	int worldId;
	int x;
	int y;
	int z;
	NBTTagCompound tag;

	public MessageTileUpdate(){}

	public MessageTileUpdate(TileEntityWGBase te)
	{
		this.worldId = te.getWorldObj().provider.dimensionId;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.tag = new NBTTagCompound();
		te.writeCustomNBT(this.tag);
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		this.worldId = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.tag = ByteBufUtils.readTag(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(worldId);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeTag(buffer, tag);
	}

	public static class HandlerClient implements IMessageHandler<MessageTileUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(MessageTileUpdate message, MessageContext ctx)
		{
			World world = DimensionManager.getWorld(message.worldId);
			if(world!=null && world.getTileEntity(message.x, message.y, message.z)!=null && world.getTileEntity(message.x, message.y, message.z) instanceof TileEntityWGBase)
				((TileEntityWGBase)world.getTileEntity(message.x, message.y, message.z)).readCustomNBT(message.tag);
			return null;
		}
	}
	public static class HandlerServer implements IMessageHandler<MessageTileUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(MessageTileUpdate message, MessageContext ctx)
		{
			World world = DimensionManager.getWorld(message.worldId);
			if(world!=null && world.getTileEntity(message.x, message.y, message.z)!=null && world.getTileEntity(message.x, message.y, message.z) instanceof TileEntityWGBase)
				((TileEntityWGBase)world.getTileEntity(message.x, message.y, message.z)).readCustomNBT(message.tag);
			return null;
		}
	}
}