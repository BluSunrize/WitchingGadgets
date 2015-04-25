package witchinggadgets.common.util.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import witchinggadgets.WitchingGadgets;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessagePrimordialGlove implements IMessage
{
	int dim;
	int playerid;
	byte type;
	int selected;
	public MessagePrimordialGlove(){}

	public MessagePrimordialGlove(EntityPlayer player, byte type, int i)
	{
		this.dim = player.worldObj.provider.dimensionId;
		this.playerid = player.getEntityId();
		this.type=type;
		this.selected = i;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.playerid = buffer.readInt();
		this.type = buffer.readByte();
		this.selected = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(this.dim);
		buffer.writeInt(this.playerid);
		buffer.writeByte(this.type);
		buffer.writeInt(this.selected);
	}

	public static class HandlerServer implements IMessageHandler<MessagePrimordialGlove, IMessage>
	{
		@Override
		public IMessage onMessage(MessagePrimordialGlove message, MessageContext ctx)
		{
			World world = DimensionManager.getWorld(message.dim);
			if(world != null)
			{
				Entity ent = world.getEntityByID(message.playerid);
				if(ent instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) ent;
					if(message.type==0 && player.getCurrentEquippedItem()!=null)
					{
						if(!player.getCurrentEquippedItem().hasTagCompound())
							player.getCurrentEquippedItem().setTagCompound(new NBTTagCompound());
						player.getCurrentEquippedItem().getTagCompound().setInteger("selected", message.selected);
					}
					else if(message.type==1)
						player.openGui(WitchingGadgets.instance, 7, world, (int)player.posX,(int)player.posY,(int)player.posZ);
				}
			}
			return null;
		}
	}
}