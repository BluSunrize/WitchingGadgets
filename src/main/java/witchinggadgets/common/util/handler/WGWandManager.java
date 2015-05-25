package witchinggadgets.common.util.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.common.Thaumcraft;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityBlastfurnace;
import witchinggadgets.common.util.Utilities;

public class WGWandManager implements IWandTriggerManager
{

	@Override
	public boolean performTrigger(World world, ItemStack wand, EntityPlayer player, int x, int y, int z, int side, int event)
	{
		switch(event)
		{
		case 1:
			//			return false;
			return createBlastFurnace(wand,player,world,x,y,z);
		}

		return false;
	}

	private boolean createBlastFurnace(ItemStack itemstack, EntityPlayer player, World world, int clickedX, int clickedY, int clickedZ)
	{
		if(world.isRemote)
			return false;
		if(!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "INFERNALBLASTFURNACE"))
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
			
			for(int yy=0;yy<=1;yy++)
				for(int xx=-1;xx<=1;xx++)
					for(int zz=-1;zz<=1;zz++)
					{
						structure &= isValidBFBrick(world, x+xx,y+yy,z+zz, ( (yy*9)+((zz+1)*3)+(xx+1)) );//world.getBlock(x+xx,y+yy,z+zz).equals(Blocks.stonebrick);
					}
			if(structure)
				break;
		}
		if(lava>=0 && lava<lavas.size())
		{
			int x = lavas.get(lava)[0];
			int y = lavas.get(lava)[1]-2;
			int z = lavas.get(lava)[2];

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
						tile.markDirty();
					}


			for(int yy=-2;yy<=0;yy++)
				for(int zz=-1;zz<=1;zz++)
					for(int xx=-1;xx<=1;xx++)
						world.markBlockForUpdate(x+xx,y+yy,z+zz);

			Utilities.consumeVisFromWand(itemstack, player, new AspectList().add(Aspect.FIRE, 50).add(Aspect.EARTH, 50).add(Aspect.ENTROPY, 50), true);
			Thaumcraft.addStickyWarpToPlayer(player, 3);
			return true;
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