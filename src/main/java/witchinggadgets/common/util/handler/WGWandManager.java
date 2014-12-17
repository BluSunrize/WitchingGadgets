package witchinggadgets.common.util.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.common.Thaumcraft;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityBlastfurnace;
import witchinggadgets.common.blocks.tiles.TileEntityLoom;
import witchinggadgets.common.util.Utilities;

public class WGWandManager implements IWandTriggerManager
{

	@Override
	public boolean performTrigger(World world, ItemStack wand, EntityPlayer player, int x, int y, int z, int side, int event)
	{
		switch(event)
		{
		case 0:
			//if(ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "LOOM"))
			return createLoom(wand,player,world,x,y,z);
			//break;
		case 1:
			//			return false;
			return createBlastFurnace(wand,player,world,x,y,z);
		}

		return false;
	}



	private boolean createLoom(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z)
	{
		if(world.isRemote)return false;
		List fences = this.getNearbyIronFence(world, x, y, z);
		if(fences.size() < 1)return false;
		String structure = "";
		boolean flagZ=false;
		boolean flagX=false;
		Iterator i = fences.iterator();
		int[] fit = new int[3];
		while(i.hasNext())
		{
			Object blah = i.next();
			if(blah == null)break;
			int[] posF = (int[]) blah;
			boolean flag2 = false;
			flagZ = 
					world.getBlock(posF[0],posF[1],posF[2]-1).equals(Blocks.fence)
					&& world.getBlock(posF[0],posF[1],posF[2]+1).equals(Blocks.fence);
			flagX = 
					world.getBlock(posF[0]-1,posF[1],posF[2]).equals(Blocks.fence)
					&& world.getBlock(posF[0]+1,posF[1],posF[2]).equals(Blocks.fence);
			if(flagZ ^ flagX)		
			{
				//System.out.println("IronFence found at: "+posF[0]+","+posF[1]+","+posF[2]);
				//System.out.println("Z is width: "+flagZ);
				if(flagZ)flag2= (this.structureFitsLoom(world, posF[0],posF[1],posF[2], false)=="x-" || this.structureFitsLoom(world, posF[0],posF[1],posF[2], false)=="x+");
				if(flagX)flag2= (this.structureFitsLoom(world, posF[0],posF[1],posF[2], true )=="z-" || this.structureFitsLoom(world, posF[0],posF[1],posF[2], true )=="z+");
			}
			if(flag2)
			{
				fit = posF;
				structure = this.structureFitsLoom(world, fit[0],fit[1],fit[2], flagX);
				//System.out.println("Checked in direction: "+structure);
				break;
			}
		}

		//thaumcraft.common.items.wands.ItemWandCasting wand = (thaumcraft.common.items.wands.ItemWandCasting)itemstack.getItem();

		if( ((structure == "x-")||(structure == "x+")||(structure == "z-")||(structure == "z+"))
				&& (Utilities.consumeVisFromWand(itemstack, player, new AspectList().add(Aspect.AIR, 15).add(Aspect.ORDER, 15), true))
				)

		{
			if(flagZ)
			{
				TileEntityLoom master;
				TileEntityLoom slave;
				int[] origBlock = new int[2];
				//Master
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1]-1,fit[2]));origBlock[1]=world.getBlockMetadata(fit[0],fit[1]-1,fit[2]);
				world.setBlock(fit[0],fit[1]-1,fit[2], WGContent.BlockWoodenDevice, 1, 3);
				master = (TileEntityLoom)world.getTileEntity(fit[0],fit[1]-1,fit[2]);
				master.originalBlock = origBlock;
				master.setActive(player);
				//IronFence
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1],fit[2]));origBlock[1]=world.getBlockMetadata(fit[0],fit[1],fit[2]);
				world.setBlock(fit[0],fit[1],fit[2], WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1],fit[2]);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;
				//Fence
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1],fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0],fit[1],fit[2]-1);
				world.setBlock(fit[0],fit[1],fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1],fit[2]-1);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;

				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1],fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0],fit[1],fit[2]+1);
				world.setBlock(fit[0],fit[1],fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1],fit[2]+1);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;
				//Wood1
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1]-1,fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0],fit[1]-1,fit[2]-1);
				world.setBlock(fit[0],fit[1]-1,fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1]-1,fit[2]-1);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;

				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1]-1,fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0],fit[1]-1,fit[2]+1);
				world.setBlock(fit[0],fit[1]-1,fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1]-1,fit[2]+1);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;
				//Wood2
				if(structure == "x-")
				{
					master.facing = 2;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1]-1,fit[2]));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]);
					world.setBlock(fit[0]-1,fit[1]-1,fit[2], WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1]-1,fit[2]);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1]-1,fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]-1);
					world.setBlock(fit[0]-1,fit[1]-1,fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1]-1,fit[2]-1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1]-1,fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]+1);
					world.setBlock(fit[0]-1,fit[1]-1,fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1]-1,fit[2]+1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;
				}
				else if(structure == "x+")
				{
					master.facing = 3;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1]-1,fit[2]));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1]-1,fit[2]);
					world.setBlock(fit[0]+1,fit[1]-1,fit[2], WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1]-1,fit[2]);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1]-1,fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1]-1,fit[2]-1);
					world.setBlock(fit[0]+1,fit[1]-1,fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1]-1,fit[2]-1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1]-1,fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1]-1,fit[2]+1);
					world.setBlock(fit[0]+1,fit[1]-1,fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1]-1,fit[2]+1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;
				}
				return true;
			}
			if(flagX)
			{
				TileEntityLoom master;
				TileEntityLoom slave;
				int[] origBlock = new int[2];
				//Master
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1]-1,fit[2]));origBlock[1]=world.getBlockMetadata(fit[0],fit[1]-1,fit[2]);
				world.setBlock(fit[0],fit[1]-1,fit[2], WGContent.BlockWoodenDevice, 1, 3);
				master = (TileEntityLoom)world.getTileEntity(fit[0],fit[1]-1,fit[2]);
				master.originalBlock = origBlock;
				master.setActive(player);
				//IronFence
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1],fit[2]));origBlock[1]=world.getBlockMetadata(fit[0],fit[1],fit[2]);
				world.setBlock(fit[0],fit[1],fit[2], WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1],fit[2]);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;
				//Fence
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1],fit[2]));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]);
				world.setBlock(fit[0]-1,fit[1],fit[2], WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1],fit[2]);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;

				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1],fit[2]));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1],fit[2]);
				world.setBlock(fit[0]+1,fit[1],fit[2], WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1],fit[2]);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;
				//Wood1
				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1]-1,fit[2]));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]);
				world.setBlock(fit[0]-1,fit[1]-1,fit[2], WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1]-1,fit[2]);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;

				origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1]-1,fit[2]));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1]-1,fit[2]);
				world.setBlock(fit[0]+1,fit[1]-1,fit[2], WGContent.BlockWoodenDevice, 1, 3);
				slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1]-1,fit[2]);
				slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
				slave.originalBlock = origBlock;
				//Wood2
				if(structure == "z-")
				{
					master.facing = 4;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1]-1,fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0],fit[1]-1,fit[2]-1);
					world.setBlock(fit[0],fit[1]-1,fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1]-1,fit[2]-1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1]-1,fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]-1);
					world.setBlock(fit[0]-1,fit[1]-1,fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1]-1,fit[2]-1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1]-1,fit[2]-1));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1]-1,fit[2]-1);
					world.setBlock(fit[0]+1,fit[1]-1,fit[2]-1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1]-1,fit[2]-1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;
				}
				else if(structure == "z+")
				{
					master.facing = 5;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0],fit[1]-1,fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0],fit[1]-1,fit[2]);
					world.setBlock(fit[0],fit[1]-1,fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0],fit[1]-1,fit[2]+1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]-1,fit[1]-1,fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0]-1,fit[1]-1,fit[2]+1);
					world.setBlock(fit[0]-1,fit[1]-1,fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]-1,fit[1]-1,fit[2]+1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;

					origBlock = new int[2];origBlock[0]=Block.getIdFromBlock(world.getBlock(fit[0]+1,fit[1]-1,fit[2]+1));origBlock[1]=world.getBlockMetadata(fit[0]+1,fit[1]-1,fit[2]+1);
					world.setBlock(fit[0]+1,fit[1]-1,fit[2]+1, WGContent.BlockWoodenDevice, 1, 3);
					slave = (TileEntityLoom)world.getTileEntity(fit[0]+1,fit[1]-1,fit[2]+1);
					slave.isDummy = true; slave.masterPosition[0] = master.xCoord; slave.masterPosition[1] = master.yCoord; slave.masterPosition[2] = master.zCoord;
					slave.originalBlock = origBlock;
				}
				return true;
			}
		}
		return false;
	}

	private List<int[]> getNearbyIronFence(World world, int x, int y, int z)
	{
		List<int[]> result = new ArrayList<int[]>();
		for(int iY = -1; iY <= 1; iY++)
			for(int iX = -1; iX <= 1; iX++)
				for(int iZ = -1; iZ <= 1; iZ++)
				{
					if(world.getBlock(x+iX, y+iY, z+iZ)==Blocks.iron_bars)
						result.add(new int[]{x+iX, y+iY, z+iZ});
				}
		return result;
	}

	/**
	 * Checks if the loom structure can be found, based around the sole iron fence in the structure.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param xzSwitch Exchanges x and z coordinates, normally z is the width of the loom while x is the depth
	 * @return
	 */
	private String structureFitsLoom(World world, int x, int y, int z, boolean xzSwitch)
	{
		int sW = 1; //Wooden Planks
		int sS = 2; //Wooden Slabs
		int sF = Block.getIdFromBlock(Blocks.fence); //Fence
		int sI = Block.getIdFromBlock(Blocks.iron_bars); //Iron Bars
		int sN = 0; //Air
		int[][][] structure = { { {sW,sS,sW} , {sW,sS,sW} } , { {sN,sN,sN} , {sF,sI,sF} } };

		if(!xzSwitch)
		{
			boolean flag = true;
			for(int iY = 0; iY <= 1; iY++)
				for(int iX = 0; iX < 2; iX++)
					for(int iZ = 0; iZ < 3; iZ++)
					{
						Block block = world.getBlock(x-1+iX, y-1+iY, z-1+iZ);
						int meta = world.getBlockMetadata(x-1+iX, y-1+iY, z-1+iZ);

						if(structure [iY][iX][iZ] == sW)//Wood Plank
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "plankWood"))
							{
								//System.out.println("Checking Wooden Plank at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iX][iZ] == sS)//Wood Slab
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "slabWood"))
							{
								//System.out.println("Checking Wooden Slab at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iX][iZ] == 0)//Air
						{
							//System.out.println("Checking Air at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
							flag = flag&&world.isAirBlock(x-1+iX, y-1+iY, z-1+iZ);
							//if(!world.isAirBlock(x-1+iX, y-1+iY, z-1+iZ))System.out.println("Missing Air at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
						}
						else
						{
							//System.out.println("Checking BlockID: "+structure [iY][iX][iZ]+" at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
							flag = flag&&Block.getIdFromBlock(block) == structure[iY][iX][iZ];
						}
					}
			if(flag)return "x-";

			flag = true;
			for(int iY = 0; iY <= 1; iY++)
				for(int iX = 0; iX < 2; iX++)
					for(int iZ = 0; iZ < 3; iZ++)
					{
						Block block = world.getBlock(x+1-iX, y-1+iY, z-1+iZ);
						int meta = world.getBlockMetadata(x+1-iX, y-1+iY, z-1+iZ);

						if(structure [iY][iX][iZ] == sW)//Wood Plank
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "plankWood"))
							{
								//System.out.println("Checking Wooden Plank at"+(x+1-iX)+","+(y-1+iY)+","+(z-1+iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iX][iZ] == sS)//Wood Slab
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "slabWood"))
							{
								//System.out.println("Checking Wooden Slab at"+(x+1-iX)+","+(y-1+iY)+","+(z-1+iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iX][iZ] == 0)//Air
						{
							//System.out.println("Checking Air at"+(x+1-iX)+","+(y-1+iY)+","+(z-1+iZ));
							flag = flag&&world.isAirBlock(x+1-iX, y-1+iY, z-1+iZ);
							//if(!world.isAirBlock(x+1-iX, y-1+iY, z-1+iZ))System.out.println("Missing Air at"+(x+1-iX)+","+(y-1+iY)+","+(z-1+iZ));
						}
						else
						{
							//System.out.println("Checking BlockID: "+structure [iY][iX][iZ]+" at"+(x+1-iX)+","+(y-1+iY)+","+(z-1+iZ));
							flag = flag&&Block.getIdFromBlock(block)==structure [iY][iX][iZ];
						}
					}
			if(flag)return "x+";
		}
		else
		{
			//System.out.println("");
			boolean flag = true;
			for(int iY = 0; iY <= 1; iY++)
			{
				for(int iX = 0; iX < 3; iX++)
				{
					for(int iZ = 0; iZ < 2; iZ++)
					{
						Block block = world.getBlock(x-1+iX, y-1+iY, z-1+iZ);
						int meta = world.getBlockMetadata(x-1+iX, y-1+iY, z-1+iZ);

						//System.out.println("iX:"+iX+" iY:"+iY+" iZ:"+iZ);

						if(structure [iY][iZ][iX] == sW)//Wood Plank
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "plankWood"))
							{
								//System.out.println("Checking Wooden Plank at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iZ][iX] == sS)//Wood Slab
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "slabWood"))
							{
								//System.out.println("Checking Wooden Slab at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iZ][iX] == 0)//Air
						{
							//System.out.println("Checking Air at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
							flag = flag&&world.isAirBlock(x-1+iX, y-1+iY, z-1+iZ);
							//if(!world.isAirBlock(x-1+iX, y-1+iY, z-1+iZ))System.out.println("Missing Air at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
						}
						else
						{
							//System.out.println("Checking BlockID: "+structure [iY][iZ][iX]+" at"+(x-1+iX)+","+(y-1+iY)+","+(z-1+iZ));
							flag = flag&&Block.getIdFromBlock(block)==structure[iY][iZ][iX];
						}
					}
				}
			}
			if(flag)return "z-";

			flag = true;
			for(int iY = 0; iY <= 1; iY++)
			{
				for(int iX = 0; iX < 3; iX++)
				{
					for(int iZ = 0; iZ < 2; iZ++)
					{
						Block block = world.getBlock(x-1+iX, y-1+iY, z+1-iZ);
						int meta = world.getBlockMetadata(x+1-iX, y-1+iY, z+1-iZ);

						//System.out.println("iX:"+iX+" iY:"+iY+" iZ:"+iZ);

						if(structure [iY][iZ][iX] == sW)//Wood Plank
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "plankWood"))
							{
								//System.out.println("Checking Wooden Plank at"+(x-1+iX)+","+(y-1+iY)+","+(z+1-iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iZ][iX] == sS)//Wood Slab
						{
							if(!Utilities.compareToOreName(new ItemStack(block, 1, meta), "slabWood"))
							{
								//System.out.println("Checking Wooden Slab at"+(x-1+iX)+","+(y-1+iY)+","+(z+1-iZ));
								flag = flag&&false;
							}
						}
						else if(structure [iY][iZ][iX] == 0)//Air
						{
							//System.out.println("Checking Air at"+(x-1+iX)+","+(y-1+iY)+","+(z+1-iZ));
							flag = flag&&world.isAirBlock(x-1+iX, y-1+iY, z+1-iZ);
							//if(!world.isAirBlock(x-1+iX, y-1+iY, z+1-iZ))System.out.println("Missing Air at"+(x-1+iX)+","+(y-1+iY)+","+(z+1-iZ));
						}
						else
						{
							//System.out.println("Checking BlockID: "+structure [iY][iZ][iX]+" at"+(x-1+iX)+","+(y-1+iY)+","+(z+1-iZ));
							flag = flag&&Block.getIdFromBlock(block)==structure[iY][iZ][iX];
						}
					}
				}
			}
			if(flag)return "z+";
		}
		return "false";
	}

	private boolean createBlastFurnace(ItemStack itemstack, EntityPlayer player, World world, int clickedX, int clickedY, int clickedZ)
	{
		if(world.isRemote)
			return false;
		if(!Utilities.consumeVisFromWand(itemstack, player, new AspectList().add(Aspect.FIRE, 50).add(Aspect.EARTH, 50).add(Aspect.ENTROPY, 50), false))
			return false;
		
		int playerViewQuarter = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int f = playerViewQuarter==0 ? 2:playerViewQuarter==1 ? 5:playerViewQuarter==2 ? 3: 4;

		List<int[]> lavas = getNearbyLava(world, clickedX, clickedY, clickedZ);
		if(lavas.isEmpty())
			return false;
		int lava = -1;
		for(lava=0;lava<lavas.size();lava++)
		{
			int x = lavas.get(lava)[0];
			int y = lavas.get(lava)[1]-2;
			int z = lavas.get(lava)[2];
			boolean structure = true;
			//Stairs
			structure &= isValidBFStair(world, x-1,y+2,z-1, 18, 0,2);// && (world.getBlockMetadata(x-1,y,z-1)==0||world.getBlockMetadata(x-1,y,z-1)==2);
			structure &= isValidBFStair(world, x-1,y+2,z+0, 21, 0);//world.getBlock(x-1,y,z+0).equals(Blocks.stone_brick_stairs) && world.getBlockMetadata(x-1,y,z+0)==0;
			structure &= isValidBFStair(world, x-1,y+2,z+1, 24, 0,3);//world.getBlock(x-1,y,z+1).equals(Blocks.stone_brick_stairs) && (world.getBlockMetadata(x-1,y,z+1)==0||world.getBlockMetadata(x-1,y,z+1)==3);
			structure &= isValidBFStair(world, x+0,y+2,z-1, 19, 2);//world.getBlock(x+0,y,z-1).equals(Blocks.stone_brick_stairs) && world.getBlockMetadata(x+0,y,z-1)==2;
			structure &= isValidBFStair(world, x+0,y+2,z+1, 25, 3);//world.getBlock(x+0,y,z+1).equals(Blocks.stone_brick_stairs) && world.getBlockMetadata(x+0,y,z+1)==3;
			structure &= isValidBFStair(world, x+1,y+2,z-1, 20, 1,2);//world.getBlock(x+1,y,z-1).equals(Blocks.stone_brick_stairs) && (world.getBlockMetadata(x+1,y,z-1)==1||world.getBlockMetadata(x+1,y,z-1)==2);
			structure &= isValidBFStair(world, x+1,y+2,z+0, 23, 1);//world.getBlock(x+1,y,z+0).equals(Blocks.stone_brick_stairs) && world.getBlockMetadata(x+1,y,z+0)==1;
			structure &= isValidBFStair(world, x+1,y+2,z+1, 26, 1,3);//world.getBlock(x+1,y,z+1).equals(Blocks.stone_brick_stairs) && (world.getBlockMetadata(x+1,y,z+1)==1||world.getBlockMetadata(x+1,y,z+1)==3);
			if(!structure)
				System.out.println("broken on stairs");
			
			for(int yy=0;yy<=1;yy++)
				for(int xx=-1;xx<=1;xx++)
					for(int zz=-1;zz<=1;zz++)
					{
						structure &= isValidBFBrick(world, x+xx,y+yy,z+zz, ( (yy*9)+((zz+1)*3)+(xx+1)) );//world.getBlock(x+xx,y+yy,z+zz).equals(Blocks.stonebrick);
						if(!structure)
							System.out.println("broken at "+xx+","+yy+","+zz);
					}
			if(structure)
				break;
		}
		if(lava>=0 && lava<lavas.size())
		{
			int x = lavas.get(lava)[0];
			int y = lavas.get(lava)[1]-2;
			int z = lavas.get(lava)[2];
			//System.out.println("HEYO!, lava at "+x+", "+y+", "+z);

			for(int yy=0;yy<=2;yy++)
				for(int zz=-1;zz<=1;zz++)
					for(int xx=-1;xx<=1;xx++)
					{
						world.setBlock(x+xx,y+yy,z+zz, WGContent.BlockStoneDevice,8, 3);
						TileEntityBlastfurnace tile = (TileEntityBlastfurnace)world.getTileEntity(x+xx,y+yy,z+zz);
						tile.masterPos = new int[]{x,y,z};
						byte pos = (byte)( (yy*9)+((zz+1)*3)+(xx+1) );
						tile.position = pos;
						tile.facing = ForgeDirection.getOrientation(f);
					}


			for(int yy=-2;yy<=0;yy++)
				for(int zz=-1;zz<=1;zz++)
					for(int xx=-1;xx<=1;xx++)
						world.markBlockForUpdate(x+xx,y+yy,z+zz);

			Utilities.consumeVisFromWand(itemstack, player, new AspectList().add(Aspect.FIRE, 50).add(Aspect.EARTH, 50).add(Aspect.ENTROPY, 50), true);
			Thaumcraft.addStickyWarpToPlayer(player, 3);
			//			return true;
		}
		return false;
	}
	List<int[]> getNearbyLava(World world, int x, int y, int z)
	{
		List<int[]> result = new ArrayList<int[]>();
		for(int yy=0;yy<=2;yy++)
			for(int xx=-1;xx<=1;xx++)
				for(int zz=-1;zz<=1;zz++)
				{
					if(world.getBlock(x+xx, y+yy, z+zz)==Blocks.lava)
						result.add(new int[]{x+xx, y+yy, z+zz});
				}
		return result;
	}
	boolean isValidBFBrick(World world, int x, int y, int z, int pos)
	{
		return world.getBlock(x,y,z).equals(TileEntityBlastfurnace.brickBlock[pos])&&world.getBlockMetadata(x,y,z)==0;
	}
	boolean isValidBFStair(World world, int x, int y, int z, int pos, int... requestedMeta)
	{
		boolean b = world.getBlock(x,y,z).equals(TileEntityBlastfurnace.stairBlock);
		if(TileEntityBlastfurnace.stairBlock != Blocks.stonebrick && world.getTileEntity(x,y,z)!=null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			world.getTileEntity(x,y,z).writeToNBT(tag);
			b &= tag.getString("stair").equalsIgnoreCase("INFERNAL_BRICK");
		}
		boolean meta = false;
		for(int rm : requestedMeta)
			if(rm==world.getBlockMetadata(x,y,z))
				meta = true;
		b &= meta;
		return b;
	}
}