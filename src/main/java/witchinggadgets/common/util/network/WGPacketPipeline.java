package witchinggadgets.common.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import org.apache.logging.log4j.Level;

import witchinggadgets.WitchingGadgets;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@ChannelHandler.Sharable
public class WGPacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket>
{
	private EnumMap<Side, FMLEmbeddedChannel>           channels;
	private LinkedList<Class<? extends AbstractPacket>> packets           = new LinkedList<Class<? extends AbstractPacket>>();
	private boolean                                     isPostInitialised = false;

	public boolean registerPacket(Class<? extends AbstractPacket> clazz)
	{
		if (this.packets.size() > 256)
		{
			WitchingGadgets.logger.log(Level.ERROR, "Can't register Packet: Size of PacketList is too big!");
			return false;
		}
		if (this.packets.contains(clazz))
		{
			WitchingGadgets.logger.log(Level.ERROR, "Can't register Packet: Packet registered already!");
			return false;
		}
		if (this.isPostInitialised)
		{
			WitchingGadgets.logger.log(Level.ERROR, "Can't register Packet: Pipeline is already post-initialized!");
			return false;
		}
		this.packets.add(clazz);
		return true;
	}

	// In line encoding of the packet, including discriminator setting
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends AbstractPacket> clazz = msg.getClass();
        if (!this.packets.contains(msg.getClass())) {
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
        }

        byte discriminator = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    // In line decoding and handling of the packet
    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();
        Class<? extends AbstractPacket> clazz = this.packets.get(discriminator);
        if (clazz == null) {
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);
        }

        AbstractPacket pkt = clazz.newInstance();
        pkt.decodeInto(ctx, payload.slice());

        EntityPlayer player;
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT:
                player = this.getClientPlayer();
                pkt.handleClientSide(player);
                break;

            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                player = ((NetHandlerPlayServer) netHandler).playerEntity;
                pkt.handleServerSide(player);
                break;

            default:
        }

        out.add(pkt);
    }

    // Method to call from FMLInitializationEvent
    public void initialise()
    {
        this.channels = NetworkRegistry.INSTANCE.newChannel("WitchingGadgets", this);
        registerPackets();
    }
    
    public void registerPackets()
    {
        registerPacket(PacketPrimordialGlove.class);
        registerPacket(PacketTileUpdate.class);
        registerPacket(PacketClientNotifier.class);
    }

    // Method to call from FMLPostInitializationEvent
    // Ensures that packet discriminators are common between server and client by using logical sorting
    public void postInitialise()
    {
        if (this.isPostInitialised) {
            return;
        }

        this.isPostInitialised = true;
        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>() {

            @Override
            public int compare(Class<? extends AbstractPacket> clazz1, Class<? extends AbstractPacket> clazz2) {
                int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
                if (com == 0) {
                    com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
                }

                return com;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    /**
     * Send this message to everyone.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToAll(AbstractPacket message)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the specified player.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public void sendTo(AbstractPacket message, EntityPlayerMP player)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param point   The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
     */
    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension(AbstractPacket message, int dimensionId)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the server.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToServer(AbstractPacket message)
    {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message);
    }
	/*
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));

		byte packetId;
		try
		{
			packetId = data.readByte();
		} catch(IOException e)
		{
			e.printStackTrace();
			return;
		}

		//		System.out.println("Packet received, handling");
		//		System.out.println(packetId);
		//		System.out.println(player.toString());
		switch (packetId)
		{
		case 0:
			this.handleCloakUsePacket(data);
			break;
		case 1:
			this.handleMigratePosPacket(data);
			break;
		case 2:
			this.handleMigrateRequestPacket(data);
			break;
		case 3:
			this.handleGemRotationPacket(data);
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			this.handleLycanSprintPacket(data);
			break;
		}
	}

	private static Packet250CustomPayload createPacket(int id, EntityPlayer player, Object[] additionalData) throws IOException
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		Packet250CustomPayload packet = new Packet250CustomPayload();

		dataStream.writeByte(id); //ID
		dataStream.writeInt(player.entityId); //PlayerEntity ID
		dataStream.writeInt(player.worldObj.provider.dimensionId); //WorldProvider ID
		if(additionalData != null)
			for(Object o : additionalData)
			{
				if(o == null)
					break;
				if(o instanceof Boolean)
					dataStream.writeBoolean((Boolean) o);
				else if(o instanceof Byte)
					dataStream.writeByte((Integer) o);
				else if(o instanceof Double)
					dataStream.writeDouble((Double) o);
				else if(o instanceof Float)
					dataStream.writeFloat((Float) o);
				else if(o instanceof Short)
					dataStream.writeShort((Short) o);
				else if(o instanceof Integer)
					dataStream.writeInt((Integer) o);
				else if(o instanceof String)
					dataStream.writeUTF((String) o);
			}
		dataStream.close();
		packet.channel = "WitchingGadgets";
		packet.data = byteStream.toByteArray();
		packet.length = byteStream.size();
		return packet;
	}

	public static void sendCloakUsePacket(EntityPlayer player)
	{
		try
		{
			ItemStack cloakStack = player.getCurrentArmor(2);
			if(cloakStack != null)
			{	
				Packet250CustomPayload packet = createPacket(0,player,null);
				PacketDispatcher.sendPacketToServer(packet);

				if(cloakStack.getItem() instanceof ItemCloak)
					ItemCloak.getCloakFromStack(cloakStack).onCloakUsed(player, cloakStack);
				if(cloakStack.getItem() instanceof ItemRunicCloak)
					ItemRunicCloak.getCloakFromStack(cloakStack).onCloakUsed(player, cloakStack);
				if(cloakStack.getItem() instanceof ItemEliteRunicArmor && ItemEliteRunicArmor.getAdvancedArmor(cloakStack).getName().contains("ANGELIC"))
					player.openGui(WitchingGadgets.instance, 4, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void sendMigratePosPacket(EntityPlayer player)
	{
		try
		{
			Packet250CustomPayload packet = createPacket(1,player,null);
			PacketDispatcher.sendPacketToServer(packet);

			ItemStack cloakStack = player.getCurrentArmor(2);
			NBTTagCompound tag = cloakStack.getTagCompound();
			tag.setDouble("WG.migratePosX",Math.floor(player.posX)+0.5);
			tag.setDouble("WG.migratePosY",Math.floor(player.posY)+0.5);
			tag.setDouble("WG.migratePosZ",Math.floor(player.posZ)+0.5);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void sendMigrateRequestPacket(EntityPlayer player, double migX, double migY, double migZ)
	{
		try
		{
			Packet250CustomPayload packet = createPacket(2,player,new Object[]{migX,migY,migZ});
			PacketDispatcher.sendPacketToServer(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private void handleMigrateRequestPacket(DataInputStream data)
	{
		int playerID;
		int worldID;
		double migX;
		double migY;
		double migZ;
		try
		{
			playerID = data.readInt();
			worldID = data.readInt();
			migX = data.readDouble();
			migY = data.readDouble();
			migZ = data.readDouble();
		}catch(IOException e)
		{
			e.printStackTrace();
			return;
		}

		World world = DimensionManager.getWorld(worldID);
		if (world == null) return;
		Entity ent = world.getEntityByID(playerID);

		if (ent != null && ent instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)ent;
			boolean migrate = false;
			if(player.inventory.consumeInventoryItem(WGModCompat.tfRavensFeather.itemID))
				migrate=true;
			else
			{
				int feathers = 0;
				for(ItemStack s:player.inventory.mainInventory)
					if(s!=null && s.itemID == Item.feather.itemID)
						feathers += s.stackSize;
				if(feathers >= 3)
				{
					for(int i=0;i<3;i++)
						player.inventory.consumeInventoryItem(Item.feather.itemID);
					migrate=true;
				}

			}
			if(migrate)
				player.setPositionAndUpdate(migX,migY,migZ);
			else
				player.addChatMessage("You don't have the Items required to return to your nest");

		}
	}

	public static void sendGemRotationPacket(EntityPlayer player, boolean forward)
	{
		try
		{
			Packet250CustomPayload packet = createPacket(3,player,new Object[]{forward});
			PacketDispatcher.sendPacketToServer(packet);
			ItemPrimordialBracelet.rotateActives(player.getCurrentEquippedItem(), forward);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private void handleGemRotationPacket(DataInputStream data)
	{
		int playerID;
		int worldID;
		boolean forward;
		try
		{
			playerID = data.readInt();
			worldID = data.readInt();
			forward = data.readBoolean();
		}catch(IOException e)
		{
			e.printStackTrace();
			return;
		}

		World world = DimensionManager.getWorld(worldID);
		if (world == null) return;
		Entity ent = world.getEntityByID(playerID);

		if (ent != null && ent instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)ent;
			ItemPrimordialBracelet.rotateActives(player.getCurrentEquippedItem(), forward);
		}
	}

	public static void sendLycanSprintPacket(EntityPlayer player, boolean pressed)
	{
		try
		{
			Packet250CustomPayload packet = createPacket(6,player,new Object[]{pressed});
			PacketDispatcher.sendPacketToServer(packet);
			if(pressed)
				WorldTickHandler.lycanSprintList.add(player.getCommandSenderName());
			else
				WorldTickHandler.lycanSprintList.remove(player.getCommandSenderName());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private void handleLycanSprintPacket(DataInputStream data)
	{
		int playerID;
		int worldID;
		boolean pressed;
		try
		{
			playerID = data.readInt();
			worldID = data.readInt();
			pressed = data.readBoolean();
		}catch(IOException e)
		{
			e.printStackTrace();
			return;
		}

		World world = DimensionManager.getWorld(worldID);
		if (world == null) return;
		Entity ent = world.getEntityByID(playerID);

		if (ent != null && ent instanceof EntityPlayer)
		{
			if(pressed)
				WorldTickHandler.lycanSprintList.add(((EntityPlayer)ent).getCommandSenderName());
			else
				WorldTickHandler.lycanSprintList.remove(((EntityPlayer)ent).getCommandSenderName());
		}
	}
*/
}