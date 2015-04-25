package witchinggadgets.common.util.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessagePlaySound implements IMessage
{
	int dim;
	double x;
	double y;
	double z;
	float volume;
	float pitch;
	String sound;
	public MessagePlaySound(){}

	public MessagePlaySound(World world, double xx, double yy ,double zz, String s, float f, float g)
	{
		this.dim = world.provider.dimensionId;
		this.x = xx;
		this.y = yy;
		this.z = zz;
		this.sound = s;
		this.volume = f;
		this.pitch = g;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.x = buffer.readDouble();
		this.y = buffer.readDouble();
		this.z = buffer.readDouble();
		this.volume = buffer.readFloat();
		this.pitch = buffer.readFloat();

		String tempString = "";
		int charLength = buffer.readInt();
		for(int i=0;i<charLength;i++)
			for(char c : Character.toChars(buffer.readInt()))
				tempString += Character.valueOf(c);
		this.sound = tempString;
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(this.dim);
		buffer.writeDouble(this.x);
		buffer.writeDouble(this.y);
		buffer.writeDouble(this.z);
		buffer.writeFloat(this.volume);
		buffer.writeFloat(this.pitch);

		char[] chars = this.sound.toCharArray();
		buffer.writeInt(chars.length);
		for(int i=0;i<chars.length;i++)
			buffer.writeInt(Character.codePointAt(chars, i));
	}

	public static class HandlerClient implements IMessageHandler<MessagePlaySound, IMessage>
	{
		@Override
		public IMessage onMessage(MessagePlaySound message, MessageContext ctx)
		{
			World world = DimensionManager.getWorld(message.dim);
			if(world != null)
				world.playSound(message.x, message.y, message.z, message.sound, message.volume, message.pitch, true);

			return null;
		}
	}
}