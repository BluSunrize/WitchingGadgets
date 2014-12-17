package witchinggadgets.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import witchinggadgets.common.world.TeleporterMirror;

public class TileEntityMirrorPortal extends TileEntityWGBase
{
	public int animation = 1;
	public int tickCounter = 0;
	public int linkedDimension = 0;
	public String linkedDimensionName = "";
	public String name = null;


	@Override
	public void updateEntity()
	{
		if(animation < 32)animation++;
		else animation=0;
	}

	public void teleportPlayer(EntityPlayer player)
	{
		if ((player.ridingEntity == null) && (player.riddenByEntity == null) && ((player instanceof EntityPlayerMP)))
		{
			//System.out.println("Heyo!");
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			MinecraftServer mServer = MinecraftServer.getServer();

			if (playerMP.timeUntilPortal > 0)
			{
				playerMP.timeUntilPortal = 10;
			}
			else
			{
				//System.out.println("Target dimension="+this.linkedDimension);
				playerMP.timeUntilPortal = 10;
				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, this.linkedDimension, new TeleporterMirror(mServer.worldServerForDimension(this.linkedDimension)));
			}
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound tags)
	{
		this.tickCounter = tags.getInteger("tickCounter");
		this.linkedDimension = tags.getInteger("linkedDimension");
		this.linkedDimensionName = tags.getString("linkedDimensionName");
		if(tags.hasKey("name"))
			this.name = tags.getString("name");
	}
	@Override
	public void writeCustomNBT(NBTTagCompound tags)
	{
		tags.setInteger("tickCounter", this.tickCounter);
		tags.setInteger("linkedDimension", this.linkedDimension);
		tags.setString("linkedDimensionName", this.linkedDimensionName);
		if(this.name != null)
			tags.setString("name", this.name);
	}

}