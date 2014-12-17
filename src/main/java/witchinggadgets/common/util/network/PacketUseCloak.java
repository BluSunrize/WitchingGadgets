package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import travellersgear.api.IActiveAbility;
import witchinggadgets.common.util.Utilities;

public class PacketUseCloak extends AbstractPacket
{
	int dim;
	int playerid;

	public PacketUseCloak(){}

	public PacketUseCloak(EntityPlayer player)
	{
		this.dim = player.worldObj.provider.dimensionId;
		this.playerid = player.getEntityId();
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.dim = buffer.readInt();
		this.playerid = buffer.readInt();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
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
		if(cloakStack != null)
		{
			((IActiveAbility)cloakStack.getItem()).activate(player, cloakStack);
//			if(cloakStack.getItem() instanceof ItemCloak)
//				ItemCloak.getCloakFromStack(cloakStack).onCloakUsed(player, cloakStack);
		}
	}

}