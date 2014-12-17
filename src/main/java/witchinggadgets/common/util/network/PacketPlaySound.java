package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PacketPlaySound extends AbstractPacket
{
	int dim;
	double x;
	double y;
	double z;
	float volume;
	float pitch;
	String sound;
	public PacketPlaySound(){}

	public PacketPlaySound(World world, double xx, double yy ,double zz, String s, float f, float g)
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
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
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
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
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

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		World world = DimensionManager.getWorld(this.dim);
		if (world == null)
			return;
		world.playSound(this.x, this.y, this.z, this.sound, this.volume, this.pitch, true);
	}

	@Override
	public void handleServerSide(EntityPlayer player2)
	{
		//	    Entity ent = world.getEntityByID(this.playerid);
		//	    if(!(ent instanceof EntityPlayer))
		//	    	return;
		//	    EntityPlayer player = (EntityPlayer) ent;
		//		ItemStack cloakStack = player.getCurrentArmor(2);
		//		NBTTagCompound tag = cloakStack.getTagCompound();
		//		tag.setDouble("WG.migratePosX",Math.floor(player.posX)+0.5);
		//		tag.setDouble("WG.migratePosY",Math.floor(player.posY)+0.5);
		//		tag.setDouble("WG.migratePosZ",Math.floor(player.posZ)+0.5);
	}

}
