package witchinggadgets.common.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraftforge.common.ChestGenHooks;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityFireBat;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageComponentPhotoshop extends StructureVillagePieces.Village
{
	static ChestGenHooks chestContents = new ChestGenHooks("WG:PHOTOWORKSHOP", new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.paper, 0, 2, 7, 10), new WeightedRandomChestContent(Items.dye, 0, 2, 7, 10), new WeightedRandomChestContent(ConfigItems.itemResource,10, 1, 1, 1)}, 3,9);

	public VillageComponentPhotoshop()
	{
	}
	public VillageComponentPhotoshop(Start villagePiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
	{
		super();
		this.coordBaseMode = par5;
		this.boundingBox = par4StructureBoundingBox;
	}

	static List<ChunkCoordinates> framesHung = new ArrayList();
	private int groundLevel = -1;
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
	{
		if(groundLevel < 0)
		{
			groundLevel = this.getAverageGroundLevel(world, box);
			if(groundLevel<0)
				return true;
			boundingBox.offset(0, groundLevel - boundingBox.maxY+9-2, 0);
		}
		int x = this.boundingBox.minX;
		int y = this.boundingBox.minY;
		int z = this.boundingBox.minZ;


		//Shape
		this.fillWithMetadataBlocks(world, box, 0,1,0, 6,7,6, Blocks.planks,2, Blocks.planks,2, false);
		this.fillWithBlocks(world, box, 1,1,1, 5,7,5, Blocks.air, Blocks.air, false);

		//Logs
		this.fillWithBlocks(world, box, 0,1,0, 0,8,0, Blocks.log, Blocks.log, false); //Air
		this.fillWithBlocks(world, box, 6,1,0, 6,8,0, Blocks.log, Blocks.log, false); //Air
		this.fillWithBlocks(world, box, 0,1,6, 0,8,6, Blocks.log, Blocks.log, false); //Air
		this.fillWithBlocks(world, box, 6,1,6, 6,8,6, Blocks.log, Blocks.log, false); //Air

		int metaHallLogsA = this.coordBaseMode==1||this.coordBaseMode==3?8:4;
		int metaHallLogsB = this.coordBaseMode==1||this.coordBaseMode==3?4:8;
		for (int rz = 1; rz <= 5; rz++)
		{
			placeBlockAtCurrentPosition(world, Blocks.log, metaHallLogsA, rz, 4, 0, box);
			placeBlockAtCurrentPosition(world, Blocks.log, metaHallLogsA, rz, 4, 6, box);
			placeBlockAtCurrentPosition(world, Blocks.log, metaHallLogsB, 0, 4, rz, box);
			placeBlockAtCurrentPosition(world, Blocks.log, metaHallLogsB, 6, 4, rz, box);
		}
		//Second Floor
		this.fillWithMetadataBlocks(world, box, 1,4,1, 5,4,5, Blocks.wooden_slab,8, Blocks.wooden_slab,8, false);
		this.fillWithBlocks(world, box, 2,4,5, 4,4,5, Blocks.air, Blocks.air, false);
		this.placeBlockAtCurrentPosition(world, Blocks.air,0, 3,4,4, box);


		//Carpet
		this.fillWithMetadataBlocks(world, box, 1,0,1, 5,0,5, Blocks.wool,1, Blocks.wool,1, false);
		this.placeBlockAtCurrentPosition(world, Blocks.wool,1, 3,0,0, box);
		//Door
		int doorMeta = coordBaseMode==0?1: coordBaseMode==1?2: coordBaseMode==2?3: 0;
		this.placeDoorAtCurrentPosition(world, box, rand, 3,1,0, doorMeta);
		//Glass
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 1,2,0, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 5,2,0, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 0,2,1, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 0,2,3, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 0,2,5, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 6,2,1, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 6,2,3, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 6,2,5, box);
		this.fillWithBlocks(world, box, 2,6,0, 4,6,0, Blocks.glass_pane, Blocks.glass_pane, false);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 0,6,2, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 0,6,4, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 2,6,6, box);
		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane,0, 4,6,6, box);

		//Counter
		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs,getMetadataWithOffset(Blocks.oak_stairs, 6), 1,1,3, box);
		this.placeBlockAtCurrentPosition(world, Blocks.wooden_slab,8, 2,1,3, box);
		this.placeBlockAtCurrentPosition(world, Blocks.wooden_slab,8, 3,1,3, box);
		//Light
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone,0, 3,4,3, box);
		//Stairs
		int stairMeta = getMetadataWithOffset(Blocks.oak_stairs, 1);
		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs,stairMeta, 4,1,5, box);
		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs,stairMeta, 3,2,5, box);
		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs,stairMeta, 2,3,5, box);
		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs,stairMeta, 1,4,5, box);

		//Roof
		int metaRoofA = getMetadataWithOffset(Blocks.oak_stairs, 0);
		int metaRoofB = getMetadataWithOffset(Blocks.oak_stairs, 1);
		int metaRoofC = getMetadataWithOffset(Blocks.oak_stairs, 3);
		int metaRoofD = getMetadataWithOffset(Blocks.oak_stairs, 2);
		for(int off=1;off<=5;off++)
		{
			placeBlockAtCurrentPosition(world, Blocks.birch_stairs, metaRoofA, 0,8,off, box);
			placeBlockAtCurrentPosition(world, Blocks.birch_stairs, metaRoofB, 6,8,off, box);
			placeBlockAtCurrentPosition(world, Blocks.birch_stairs, metaRoofC, off,8,0, box);
			placeBlockAtCurrentPosition(world, Blocks.birch_stairs, metaRoofD, off,8,6, box);
		}
		this.fillWithMetadataBlocks(world, box, 1,9,1, 5,9,5, Blocks.wooden_slab,2, Blocks.wooden_slab,2, false);

		//Details
		try{
			System.out.println(x+","+y+","+z+": coordBaseMode="+coordBaseMode);

			this.generateStructureChestContents(world,box,rand, 1,1,5, chestContents.getItems(rand), chestContents.getCount(rand));

			int px = this.getXWithOffset(0,3);
			int pz = this.getZWithOffset(0,3);
			int side = coordBaseMode==0?5: coordBaseMode==1?3: coordBaseMode==2?5: 3;
			this.placePainting(rand, world,px,y+6,pz,side, rand.nextInt(7));

			side = coordBaseMode==0?4: coordBaseMode==1?2: coordBaseMode==2?4: 2;
			for(int rz=2;rz<=4;rz++)
			{
				px = this.getXWithOffset(6,rz);
				pz = this.getZWithOffset(6,rz);
				ItemStack photo = new ItemStack(WGContent.ItemMaterial,1,10);
				if(!world.isRemote)
				{
					ScanResult scan = getRandomScan(world, rand);
					photo.setTagCompound(new NBTTagCompound());
					if(scan != null)
					{
						System.out.println(scan.entity);
						NBTTagCompound scanTag = Utilities.writeScanResultToNBT(scan);
						photo.getTagCompound().setTag("scanResult", scanTag);
					}
				}
				this.placeItemframe(rand, world, px, y+6, pz, side, photo);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}


		return true;
	}

	public void placePainting(Random random, World world, int x, int y, int z, int side, int art)
	{
		int i1 = Direction.facingToDirection[side];
		EntityPainting e = new EntityPainting(world, x, y, z, i1);
		e.art = EntityPainting.EnumArt.values()[art];
		if(e.onValidSurface() && world.getEntitiesWithinAABB(EntityHanging.class, AxisAlignedBB.getBoundingBox(x-.5,y,z-.5,x+1.5,y+1,z+1.5)).isEmpty())
		{
			if (!world.isRemote)
				world.spawnEntityInWorld(e);
		}
	}
	public void placeItemframe(Random random, World world, int x, int y, int z, int side, ItemStack stack)
	{
		int i1 = Direction.facingToDirection[side];
		EntityItemFrame e = new EntityItemFrame(world, x, y, z, i1);
		e.setDisplayedItem(stack);
		if(e.onValidSurface() && world.getEntitiesWithinAABB(EntityHanging.class, AxisAlignedBB.getBoundingBox(x-.125,y,z-.125,x+1.125,y+1,z+1.125)).isEmpty())
		{
			if (!world.isRemote)
				world.spawnEntityInWorld(e);
		}
	}

	public ScanResult getRandomScan(World world, Random rand)
	{
		int c = rand.nextInt(33);
		switch(c)
		{
		default:
			return null;
		case 0:
		case 1:
		case 2:
		case 3:
			return new ScanResult((byte)2, 0,0, new EntityZombie(world), "");
		case 4:
		case 5:
		case 6:
		case 7:
			return new ScanResult((byte)2, 0,0, new EntitySkeleton(world), "");
		case 8:
		case 9:
		case 10:
		case 11:
			return new ScanResult((byte)2, 0,0, new EntityCreeper(world), "");
		case 12:
		case 13:
		case 14:
		case 15:
			return new ScanResult((byte)2, 0,0, new EntitySpider(world), "");
		case 16:
		case 17:
		case 18:
			return new ScanResult((byte)2, 0,0, new EntityCaveSpider(world), "");
		case 19:
		case 20:
			return new ScanResult((byte)2, 0,0, new EntityEnderman(world), "");
		case 21:
			EntitySkeleton skel = new EntitySkeleton(world);
			skel.setSkeletonType(1);
			return new ScanResult((byte)2, 0,0, skel, "");
		case 22:
		case 23:
		case 24:
			return new ScanResult((byte)2, 0,0, new EntityPigZombie(world), "");
		case 25:
		case 26:
			return new ScanResult((byte)2, 0,0, new EntitySlime(world), "");
		case 27:
		case 28:
			return new ScanResult((byte)2, 0,0, new EntityBlaze(world), "");
		case 29:
		case 30:
			return new ScanResult((byte)2, 0,0, new EntityBrainyZombie(world), "");
		case 31:
		case 32:
			return new ScanResult((byte)2, 0,0, new EntityFireBat(world), "");
		}
	}

	public static class VillageManager implements IVillageCreationHandler
	{
		@Override
		public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
		{
			StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 7, 9, 7, p4);
			return (!canVillageGoDeeper(box)) || (StructureComponent.findIntersecting(pieces, box) != null) ? null : new VillageComponentPhotoshop(startPiece, p5, random, box, p4);
		}
		@Override
		public PieceWeight getVillagePieceWeight(Random random, int i)
		{
			return new StructureVillagePieces.PieceWeight(VillageComponentPhotoshop.class, 15, MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i));
		}
		@Override
		public Class<?> getComponentClass()
		{
			return VillageComponentPhotoshop.class;
		}
	}
}
