package witchinggadgets.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import witchinggadgets.common.blocks.tiles.TileEntityMirrorPortal;

public class BlockMirrorPortal extends BlockContainer {

	public BlockMirrorPortal()
	{
		super(Material.cloth);
		this.setBlockUnbreakable();
		this.setResistance(10);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
	{
		if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemNameTag && player.getCurrentEquippedItem().hasDisplayName())
		{
			if(world.getTileEntity(x,y,z) instanceof TileEntityMirrorPortal)
			{
				((TileEntityMirrorPortal)world.getTileEntity(x, y, z)).name = player.getCurrentEquippedItem().getDisplayName();
				if(!player.capabilities.isCreativeMode)
					--player.getCurrentEquippedItem().stackSize;
				return true;
			}
		}
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		//System.out.println(world.isRemote);
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entity;
			TileEntityMirrorPortal tileEntity = (TileEntityMirrorPortal)world.getTileEntity(x, y, z);
			tileEntity.teleportPlayer(player);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return null;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{	
		blockIcon = iconRegister.registerIcon("witchinggadgets:nil");
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z)
	{	
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 1.5F, 1F);
		//this.setBlockBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		if (entity instanceof EntityWither || entity instanceof EntityDragon)
		{
			return false;
		}

		return true;
	}


	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{	
		return 0;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityMirrorPortal();
	}

}