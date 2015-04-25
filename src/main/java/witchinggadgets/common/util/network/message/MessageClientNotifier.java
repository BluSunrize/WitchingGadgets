package witchinggadgets.common.util.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.research.ResearchCategories;
import witchinggadgets.common.WGResearch;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageClientNotifier implements IMessage
{
	int packetid;
	public MessageClientNotifier(){}

	public MessageClientNotifier(int id)
	{
		this.packetid = id;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		this.packetid = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(this.packetid);
	}

	public static class HandlerClient implements IMessageHandler<MessageClientNotifier, IMessage>
	{
		@Override
		public IMessage onMessage(MessageClientNotifier message, MessageContext ctx)
		{
			switch(message.packetid)
			{
			case 0:
				if(Minecraft.getMinecraft().thePlayer!=null)
					if(ThaumcraftApiHelper.isResearchComplete(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), "WGFAKEELDRITCHMINOR"))
						ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[1];
					else
						ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[0];
				break;
			}
			return null;
		}
	}
}