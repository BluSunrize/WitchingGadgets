package witchinggadgets.common.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.wands.IWandable;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.render.BlockRenderRoseVine;

public class BlockRoseVines extends Block implements IWandable, IShearable
{
	public BlockRoseVines()
	{
		super(Material.plants);
		this.setTickRandomly(true);
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)// && par1World.rand.nextInt(4) == 0)
		{
			spreadVines(world, x, y, z, random, false);
		}
	}

	@Override
	public int getRenderType()
	{
		return BlockRenderRoseVine.renderID;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{	this.blockIcon = iconRegister.registerIcon("witchinggadgets:roseVineBlock");
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		//world.markBlockForRenderUpdate(x, y, z);
		if(entity instanceof EntityLivingBase)
			entity.attackEntityFrom(DamageSource.generic, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return null;
	}

//	@SuppressWarnings("unused")
//	private boolean canBePlacedOn(int id)
//	{
//		if (id == 0)
//		{
//			return false;
//		}
//		Block block = Block.blocksList[id];
//		return block.renderAsNormalBlock() && block.blockMaterial.blocksMovement();
//	}

	public void spreadVines(World world, int x, int y, int z, Random random, boolean ignoreRandom)
	{
		int growthStage = world.getBlockMetadata(x, y, z);
		if(growthStage >= 5)
		{
			//			if(isRoseVinePlaceable(world, x, y, z+1))
			//				if(random.nextInt(4)==0 || ignoreRandom)
			//					world.setBlock(x, y, z+1, this.blockID);
			//
			//			if(isRoseVinePlaceable(world, x, y, z-1))
			//				if(random.nextInt(4)==0 || ignoreRandom)
			//					world.setBlock(x, y, z-1, this.blockID);
			//
			//			if(isRoseVinePlaceable(world, x+1, y, z))
			//				if(random.nextInt(4)==0 || ignoreRandom)
			//					world.setBlock(x+1, y, z, this.blockID);
			//
			//			if(isRoseVinePlaceable(world, x-1, y, z))
			//				if(random.nextInt(4)==0 || ignoreRandom)
			//					world.setBlock(x-1, y, z, this.blockID);

			if(isRoseVinePlaceable(world, x, y+1, z))
				if(random.nextInt(4)==0 || ignoreRandom)
					if(world.isSideSolid(x, y+1, z-1, ForgeDirection.SOUTH, false)
							||world.isSideSolid(x, y+1, z+1, ForgeDirection.NORTH, false)
							||world.isSideSolid(x-1, y+1, z, ForgeDirection.WEST, false)
							||world.isSideSolid(x+1, y+1, z, ForgeDirection.EAST, false)
							)
						world.setBlock(x, y+1, z, this);

			//			if(isRoseVinePlaceable(world, x, y-1, z))
			//				if(random.nextInt(4)==0 || ignoreRandom)
			//					world.setBlock(x, y-1, z, this.blockID);
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, growthStage+1, 2);
		}
	}

	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		float minX = 0.0F;
		float minY = 0.0F;
		float minZ = 0.0F;
		float maxX = 1.0F;
		float maxY = 1.0F;
		float maxZ = 1.0F;

		if(world.isSideSolid(x, y-1, z, ForgeDirection.UP, false))
			maxY = 0.375F;

		if(world.isSideSolid(x, y, z+1, ForgeDirection.NORTH, false))
		{
			maxY = 1.0F;
			minZ = 0.75F;
		}
		if(world.isSideSolid(x+1, y, z, ForgeDirection.EAST, false))
		{
			maxY = 1.0F;
			minX = 0.75F;
		}

		if(world.isSideSolid(x, y, z-1, ForgeDirection.SOUTH, false))
		{
			maxY = 1.0F;
			maxZ = 0.25F;
		}
		if(world.isSideSolid(x-1, y, z, ForgeDirection.WEST, false))
		{
			maxY = 1.0F;
			maxX = 0.25F;
		}

		if(world.isSideSolid(x, y, z+1, ForgeDirection.NORTH, false))
			maxZ = 1.0F;
		if(world.isSideSolid(x+1, y, z, ForgeDirection.EAST, false))
			maxX = 1.0F;

		if(world.isSideSolid(x, y-1, z, ForgeDirection.UP, false))
		{
			minX = 0.0F;
			minZ = 0.0F;
			maxX = 1.0F;
			maxZ = 1.0F;
		}

		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

	private boolean hasClimbableWall(World world, int x, int y, int z)
	{
		return world.isSideSolid(x, y, z+1, ForgeDirection.NORTH, false)
				|| world.isSideSolid(x+1, y, z, ForgeDirection.EAST, false)
				|| world.isSideSolid(x, y, z-1, ForgeDirection.SOUTH, false)
				|| world.isSideSolid(x-1, y, z, ForgeDirection.WEST, false);
	}
	private boolean canRoot(World world, int x, int y, int z)
	{
		return world.isSideSolid(x,y-1,z, ForgeDirection.UP, false);
	}
	private boolean isRoseVinePlaceable(World world, int x, int y, int z)
	{
		boolean flagBasic = world.isAirBlock(x, y, z) && (world.getBlock(x,y,z)!=this);
		return flagBasic && (canRoot(world,x,y,z)||hasClimbableWall(world,x,y,z));
	}

	@Override
	public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md)
	{
		Random random = new Random();
		if(player.capabilities.isCreativeMode)
			spreadVines(world, x, y, z, random, true);
		return 1;
	}

	@Override
	public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count){}

	@Override
	public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count){}

	@Override
	public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
	{
		super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
	}

	@Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1));
        return ret;
    }

}