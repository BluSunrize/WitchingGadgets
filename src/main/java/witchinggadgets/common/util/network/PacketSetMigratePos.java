package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import witchinggadgets.common.util.Utilities;

public class PacketSetMigratePos extends AbstractPacket
{
	int dim;
	int playerid;
	public PacketSetMigratePos(){}
	
	public PacketSetMigratePos(EntityPlayer player)
	{
		this.dim = player.worldObj.provider.dimensionId;
		this.playerid = player.getEntityId();
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.playerid = buffer.readInt();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.dim);
		buffer.writeInt(this.playerid);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
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
		ItemStack cloakStack = Utilities.getActiveMagicalCloak(player);
		NBTTagCompound tag = cloakStack.getTagCompound();
		tag.setDouble("WG.migratePosX",Math.floor(player.posX)+0.5);
		tag.setDouble("WG.migratePosY",Math.floor(player.posY)+0.5);
		tag.setDouble("WG.migratePosZ",Math.floor(player.posZ)+0.5);
	}

}
