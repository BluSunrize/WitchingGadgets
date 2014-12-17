package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import witchinggadgets.common.WGModCompat;

public class PacketRequestMigrate extends AbstractPacket
{
	int dim;
	int playerid;
	double migX;
	double migY;
	double migZ;

	public PacketRequestMigrate(){}

	public PacketRequestMigrate(EntityPlayer player, double x, double y, double z)
	{
		dim = player.worldObj.provider.dimensionId;
		playerid = player.getEntityId();
		migX = x;
		migY = y;
		migZ = z;
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.playerid = buffer.readInt();
		this.migX = buffer.readDouble();
		this.migY = buffer.readDouble();
		this.migZ = buffer.readDouble();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.dim);
		buffer.writeInt(this.playerid);
		buffer.writeDouble(this.migX);
		buffer.writeDouble(this.migY);
		buffer.writeDouble(this.migZ);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		// TODO Auto-generated method stub
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
		boolean migrate = false;
		if(player.inventory.consumeInventoryItem(WGModCompat.tfRavensFeather))
			migrate=true;
		else
		{
			int feathers = 0;
			for(ItemStack s:player.inventory.mainInventory)
				if(s!=null && s.equals(Items.feather))
					feathers += s.stackSize;
			if(feathers >= 3)
			{
				for(int i=0;i<3;i++)
					player.inventory.consumeInventoryItem(Items.feather);
				migrate=true;
			}
		}
		if(migrate)
			player.setPositionAndUpdate(migX,migY,migZ);
		else
			player.addChatComponentMessage(new ChatComponentTranslation("wg.gui.noFeathersToMigrate"));
	}

}
