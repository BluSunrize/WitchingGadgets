package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractPacket
{
  public abstract void encodeInto(ChannelHandlerContext context, ByteBuf buffer);
  
  public abstract void decodeInto(ChannelHandlerContext context, ByteBuf buffer);
  
  public abstract void handleClientSide(EntityPlayer clientPlayer);
  
  public abstract void handleServerSide(EntityPlayer p2);
}