package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import witchinggadgets.WitchingGadgets;

public class PacketPrimordialGlove extends AbstractPacket
{
	int dim;
	int playerid;
	byte type;
	int selected;

	public PacketPrimordialGlove(){}

	public PacketPrimordialGlove(EntityPlayer player, byte type, int i)
	{
		this.dim = player.worldObj.provider.dimensionId;
		this.playerid = player.getEntityId();
		this.type=type;
		this.selected = i;
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.playerid = buffer.readInt();
		this.type = buffer.readByte();
		this.selected = buffer.readInt();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.dim);
		buffer.writeInt(this.playerid);
		buffer.writeByte(this.type);
		buffer.writeInt(this.selected);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
	}

	@Override
	public void handleServerSide(EntityPlayer player2)
	{
		World world = DimensionManager.getWorld(this.dim);
		if (world == null)
			return;
		Entity ent = world.getEntityByID(this.playerid);
		if(!(ent instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) ent;
		if(type==0 && player.getCurrentEquippedItem()!=null)
		{
			if(!player.getCurrentEquippedItem().hasTagCompound())
				player.getCurrentEquippedItem().setTagCompound(new NBTTagCompound());
			player.getCurrentEquippedItem().getTagCompound().setInteger("selected", selected);
		}
		else if(type==1)
			player.openGui(WitchingGadgets.instance, 7, world, (int)player.posX,(int)player.posY,(int)player.posZ);
	}

}
