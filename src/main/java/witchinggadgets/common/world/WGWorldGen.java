package witchinggadgets.common.world;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ChestGenHooks;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.ComponentWizardTower;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityMagicalTileLock;
import witchinggadgets.common.blocks.tiles.TileEntitySarcophagus;
import cpw.mods.fml.common.IWorldGenerator;

public class WGWorldGen implements IWorldGenerator
{
	void generateOverworld(World world, Random rand, int chunkX, int chunkZ)
	{
		generateTomb(world, rand, chunkX, chunkZ);
	}
	void generateNether(World world, Random rand, int chunkX, int chunkZ)
	{

	}
	void generateEnd(World world, Random rand, int chunkX, int chunkZ)
	{

	}



	void generateTomb(World world, Random rand, int chunkX, int chunkZ)
	{
		int x = chunkX * 16 + rand.nextInt(16);
		int z = chunkZ * 16 + rand.nextInt(16);
		int y = world.getHeightValue(x, z);
		if(world.getBlock(x, y, z).getMaterial()==Material.grass||world.getBlock(x, y, z).getMaterial()==Material.sand)
		{
			y-=8;
			for(int yy=0;yy<tombShape.length;yy++)
				for(int xx=0;xx<12;xx++)
					for(int zz=0;zz<13;zz++)
					{
						switch(tombShape[yy][xx][zz])
						{
						case 0:
							world.setBlockToAir(x+xx,y+yy,z+zz);
							break;
						case 1://plate
							world.setBlock(x+xx,y+yy,z+zz, WGContent.BlockStoneDevice,2,0);
							break;
						case 2://rune block (random)
							world.setBlock(x+xx,y+yy,z+zz, WGContent.BlockStoneDevice,3+rand.nextInt(3),0);
							break;
						case 3://tile
							world.setBlock(x+xx,y+yy,z+zz, ConfigBlocks.blockCosmeticSolid,1,0);
							break;
						case 4://totem
							world.setBlock(x+xx,y+yy,z+zz, ConfigBlocks.blockCosmeticSolid,8,0);
							break;
						case 5://sarcophagus
							world.setBlock(x+xx,y+yy,z+zz, WGContent.BlockStoneDevice,6,0);
							((TileEntitySarcophagus)world.getTileEntity(x+xx,y+yy,z+zz)).facing = 5;
							((TileEntitySarcophagus)world.getTileEntity(x+xx,y+yy,z+zz)).inv[0] = new ItemStack(WGContent.ItemMaterial,2,5);
							
							world.setBlock(x+xx,y+yy,z+zz+1, WGContent.BlockStoneDevice,6, 0);
							((TileEntitySarcophagus)world.getTileEntity(x+xx,y+yy,z+zz+1)).facing = 5;
							((TileEntitySarcophagus)world.getTileEntity(x+xx,y+yy,z+zz+1)).dummyRight = true;
							world.setBlock(x+xx,y+yy,z+zz-1, WGContent.BlockStoneDevice,6, 0);
							((TileEntitySarcophagus)world.getTileEntity(x+xx,y+yy,z+zz-1)).facing = 5;
							((TileEntitySarcophagus)world.getTileEntity(x+xx,y+yy,z+zz-1)).dummyLeft = true;
							break;
						case 6://chest
							world.setBlock(x+xx,y+yy,z+zz, Blocks.chest,4,0);
							ChestGenHooks cgh = new ChestGenHooks("towerChestContents", ComponentWizardTower.towerChestContents, 4, 9);
							WeightedRandomChestContent.generateChestContents(rand, cgh.getItems(rand), (TileEntityChest)world.getTileEntity(x+xx,y+yy,z+zz), cgh.getCount(rand));
							break;
						case 7://nitor
							world.setBlock(x+xx,y+yy,z+zz, ConfigBlocks.blockAiry,1,0);
							break;
						case 8://lock
							world.setBlock(x+xx,y+yy,z+zz, WGContent.BlockStoneDevice,1,0);
							((TileEntityMagicalTileLock)world.getTileEntity(x+xx,y+yy,z+zz)).lockPreset = rand.nextInt(16);
							break;
						}
					}
		}

	}



	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.dimensionId)
		{
		case -1:
			generateNether(world, random, chunkX, chunkZ);
			break;
		case 1:
			generateEnd(world, random, chunkX, chunkZ);
			break;
		default:
			generateOverworld(world, random, chunkX, chunkZ);
			break;
		}
		world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
	}

	//1==plate
	//2==rune block (random)
	//3==tile
	//4==totem
	//5==sarcophagus
	//6==chest
	//7==nitor
	//8==lock
	static int n=-1;
	static int[][][] tombShape = {
		{
			{n,n,n,n,n,n,n,n,n,n,n,n,0},
			{n,n,n,n,n,n,3,n,n,n,n,n,n},
			{n,n,n,n,1,3,1,3,1,n,n,n,n},
			{n,n,n,1,3,1,2,1,3,1,n,n,n},
			{n,3,1,3,2,2,1,2,2,3,1,3,n},
			{n,1,3,2,3,1,3,1,3,2,3,1,n},
			{n,n,2,3,1,3,1,3,1,3,2,n,n},
			{n,n,2,1,3,1,3,1,3,1,2,n,n},
			{n,n,1,2,1,3,1,3,1,2,1,n,n},
			{n,n,3,1,2,1,3,1,2,1,3,n,n},
			{n,n,n,3,1,2,2,2,1,3,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
		},
		{
			{n,n,n,n,n,n,1,n,n,n,n,n,n},
			{n,n,n,n,1,1,0,1,1,n,n,n,n},
			{n,n,n,1,0,0,0,0,0,1,n,n,n},
			{n,1,1,0,4,n,5,n,4,0,1,1,n},
			{1,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,6,0,0,0,0,0,6,0,0,1},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,1,0,6,0,0,0,0,0,6,0,1,n},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,1,0,0,4,0,0,0,4,0,0,1,n},
			{n,n,1,0,0,0,0,0,0,0,1,n,n},
			{n,n,n,1,1,1,1,1,1,1,n,n,n},
		},
		{
			{n,n,n,n,n,n,2,n,n,n,n,n,n},
			{n,n,n,n,2,2,0,2,2,n,n,n,n},
			{n,n,n,2,0,0,0,0,0,2,n,n,n},
			{n,2,2,0,7,0,0,0,7,0,2,2,n},
			{2,0,0,0,0,0,0,0,0,0,0,0,2},
			{2,0,0,0,0,0,0,0,0,0,0,0,2},
			{n,2,0,0,0,0,0,0,0,0,0,2,n},
			{n,2,0,0,0,0,0,0,0,0,0,2,n},
			{n,2,0,0,0,0,0,0,0,0,0,2,n},
			{n,2,0,0,7,0,0,0,7,0,0,2,n},
			{n,n,2,0,0,0,0,0,0,0,2,n,n},
			{n,n,n,2,2,2,2,2,2,2,n,n,n},
		},
		{
			{n,n,n,n,n,n,1,n,n,n,n,n,n},
			{n,n,n,n,1,1,0,1,1,n,n,n,n},
			{n,n,n,1,0,0,0,0,0,1,n,n,n},
			{n,1,1,0,0,0,0,0,0,0,1,1,n},
			{1,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,1},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,n,1,0,0,0,0,0,0,0,1,n,n},
			{n,n,n,1,1,1,1,1,1,1,n,n,n},
		},
		{
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,2,n,n,n,n,n,n},
			{n,n,n,n,1,1,0,1,1,n,n,n,n},
			{n,n,1,1,0,0,0,0,0,1,1,n,n},
			{n,2,0,0,0,0,0,0,0,0,0,2,n},
			{n,1,0,0,0,0,0,0,0,0,0,1,n},
			{n,n,1,0,0,0,0,0,0,0,1,n,n},
			{n,n,1,0,0,0,0,0,0,0,1,n,n},
			{n,n,1,1,0,0,0,0,0,1,1,n,n},
			{n,n,1,2,0,0,0,0,0,2,1,n,n},
			{n,n,n,1,1,1,1,1,1,1,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
		},
		{
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,2,n,n,n,n,n,n},
			{n,n,n,n,1,1,0,1,1,0,0,n,n},
			{n,n,2,1,0,0,0,0,0,1,2,n,n},
			{n,n,1,0,0,0,0,0,0,0,1,n,n},
			{n,n,1,0,0,0,0,0,0,0,1,n,n},
			{n,n,n,1,0,0,0,0,0,1,n,n,n},
			{n,n,n,1,2,0,0,0,2,1,n,n,n},
			{n,n,n,n,1,1,1,1,1,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
		},
		{
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,2,n,n,n,n,n,n},
			{n,n,n,n,1,1,1,1,1,n,n,n,n},
			{n,n,n,1,2,0,0,0,2,1,n,n,n},
			{n,n,n,1,1,0,0,0,1,1,n,n,n},
			{n,n,n,n,1,0,0,0,1,n,n,n,n},
			{n,n,n,n,n,1,1,1,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
		},
		{
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,1,1,1,n,n,n,n,n},
			{n,n,n,n,n,1,8,1,n,n,n,n,n},
			{n,n,n,n,n,1,1,1,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
			{n,n,n,n,n,n,n,n,n,n,n,n,n},
		}
	};
}