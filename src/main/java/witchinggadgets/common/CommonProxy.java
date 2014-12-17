package witchinggadgets.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.blocks.tiles.TileEntityLoom;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;
import witchinggadgets.common.gui.ContainerBag;
import witchinggadgets.common.gui.ContainerCloak;
import witchinggadgets.common.gui.ContainerCuttingTable;
import witchinggadgets.common.gui.ContainerLoom;
import witchinggadgets.common.gui.ContainerPrimordialGlove;
import witchinggadgets.common.gui.ContainerSpinningWheel;
import witchinggadgets.common.gui.ContainerVoidBag;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void registerRenders(){}
	public void registerHandlers()
	{
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		//System.out.println("Time to Open a gui, Serverside, ID: "+ID);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(ID == 0)return new ContainerSpinningWheel(player.inventory, (TileEntitySpinningWheel)tile);
		if(ID == 1)return new ContainerLoom(player.inventory, (TileEntityLoom)tile);
		
		if(ID == 3)return new ContainerBag(player.inventory, world);
		if(ID == 4)return new ContainerCloak(player.inventory, world, x, y, z);

		if(ID == 7)return new ContainerPrimordialGlove(player.inventory, world, x, y, z);
		//System.out.println("...or just skip it...");
		if(ID == 9)return new ContainerCuttingTable(player.inventory, (TileEntityCuttingTable)tile);
		
		if(ID == 11)return new ContainerVoidBag(player.inventory, world);
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	public void createEssentiaTrailFx(World worldObj, int x, int y, int z, int tx, int ty, int tz, int count, int color, float scale)
	{
	}
	public void createTargetedWispFx(World worldObj, double x, double y, double z, double tx, double ty, double tz, int color, float scale, float gravity, boolean tinkle, boolean noClip)
	{
	}
	public void createSweatFx(EntityPlayer player)
	{
	}
	public void createFurnaceOutputBlobFx(World worldObj, int x, int y, int z, ForgeDirection facing)
	{
	}
	public void createFurnaceDestructionBlobFx(World worldObj, int x, int y, int z)
	{
	}
}