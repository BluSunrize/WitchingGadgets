package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.research.ResearchCategories;
import witchinggadgets.common.WGResearch;

public class PacketClientNotifier extends AbstractPacket
{
	int packetid;
	public PacketClientNotifier(){}

	public PacketClientNotifier(int id)
	{
		this.packetid = id;
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.packetid = buffer.readInt();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.packetid);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		switch(packetid)
		{
		case 0:
			if(ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "WGFAKEELDRITCHMINOR"))
				ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[1];
			else
				ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[0];
			break;
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player2)
	{
	}

}
