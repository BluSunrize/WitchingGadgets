package witchinggadgets.common.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.tiles.TileEntityWallMirror;

public class BlockWallMirror extends BlockContainer
{
	public BlockWallMirror()
	{
		super(Material.glass);
		setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{	
		blockIcon = iconRegister.registerIcon("witchinggadgets:nil");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(!player.isSneaking())
		{
			TileEntityWallMirror tile;
			if(!((TileEntityWallMirror)world.getTileEntity(x,y,z)).isDummy)
				tile = (TileEntityWallMirror)world.getTileEntity(x,y,z);
			else
				tile = (TileEntityWallMirror)world.getTileEntity(x,y-1,z);

			tile.toggleState();
		}	
		return true;
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
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		super.onNeighborBlockChange(world, x, y, z, par5);
		TileEntityWallMirror tile = (TileEntityWallMirror)world.getTileEntity(x,y,z);
		if(tile.isDummy)
		{
			if(world.isAirBlock(x, y-1, z))world.setBlockToAir(x, y, z);//,false);
		}
		else
		{
			if(world.isAirBlock(x, y+1, z))world.setBlockToAir(x, y, z);//,true);
		}
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int par5, EntityPlayer player)
	{
		dropBlockAsItem(world, x, y, z, par5, 0);
		super.onBlockHarvested(world, x, y, z, par5, player);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		TileEntityWallMirror tile = (TileEntityWallMirror)world.getTileEntity(x,y,z);

		if(tile != null && !tile.isDummy)
			ret.add(new ItemStack(this, 1, damageDropped(metadata)));
		return ret;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		int l = ((TileEntityWallMirror)par1IBlockAccess.getTileEntity(par2, par3, par4)).facing;

		switch (l)
		{ 
		case 2:
		default:
			this.setBlockBounds(0F, 0F, 0.9375F, 1F, 1F, 1F);
			break;
		case 3:
			this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 0.0625F);
			break;
		case 4:
			this.setBlockBounds(0.9375F, 0F, 0F, 1F, 1F, 1F);
			break;
		case 5:
			this.setBlockBounds(0F, 0F, 0F, 0.0625F, 1F, 1F);
			break;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.isAirBlock(x, y+1, z);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
	{
		int l = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		if (l == 0)
		{
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3,par4)).facing = 2;
			par1World.setBlock(par2,par3+1,par4,this);
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).facing = 2;
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).isDummy = true;
		}
		if (l == 1)
		{
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3,par4)).facing = 5;
			par1World.setBlock(par2,par3+1,par4,this);
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).facing = 5;
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).isDummy = true;
		}
		if (l == 2)
		{
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3,par4)).facing = 3;
			par1World.setBlock(par2,par3+1,par4,this);
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).facing = 3;
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).isDummy = true;
		}
		if (l == 3)
		{
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3,par4)).facing = 4;
			par1World.setBlock(par2,par3+1,par4,this);
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).facing = 4;
			((TileEntityWallMirror)par1World.getTileEntity(par2,par3+1,par4)).isDummy = true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityWallMirror();
	}
}
