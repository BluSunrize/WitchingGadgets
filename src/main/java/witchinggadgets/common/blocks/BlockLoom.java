package witchinggadgets.common.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.tiles.TileEntityLoom;

public class BlockLoom extends BlockContainer
{

	public BlockLoom()
	{
		super(Material.wood);
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
	public void registerBlockIcons(IIconRegister iconRegister)
	{	
		blockIcon = iconRegister.registerIcon("witchinggadgets:loom");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
	{
		TileEntityLoom tile = (TileEntityLoom)world.getTileEntity(x,y,z);
		TileEntityLoom master = tile.getMaster();

		if(master == null)return false;
		if(!player.isSneaking())
		{
			if(master.isSetUp)
				player.openGui(WitchingGadgets.instance, 1, world, master.xCoord, master.yCoord, master.zCoord);
			return true;
		}
		for(Aspect a: master.storedAspects.getAspectsSorted())
		{
			if(a != null)
			{
				String aspects = a.getName()+": "+master.storedAspects.getAmount(a);
				player.addChatComponentMessage(new ChatComponentText(aspects));
			}
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		TileEntityLoom tile = (TileEntityLoom)world.getTileEntity(x,y,z);

		int count = quantityDropped(metadata, fortune, world.rand);
		for(int i = 0; i < count; i++)
		{
			if(tile != null && tile.originalBlock != null)
			{
				int id = tile.originalBlock[0];
				if (id > 0)
				{
					ret.add(new ItemStack(Item.getItemById(id), 1, tile.originalBlock[1]));
				}
			}
		}
		return ret;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		TileEntityLoom tile = (TileEntityLoom)world.getTileEntity(x,y,z);
		TileEntityLoom master;
		if(tile.isDummy)
			master = (TileEntityLoom)world.getTileEntity(tile.masterPosition[0],tile.masterPosition[1],tile.masterPosition[2]);
		else
			master = tile;
		if(master!=null)
			master.restoreStructureToOriginal();

		super.breakBlock(world, x, y, z, block, par6);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		this.setBlockBounds(0,0,0,1,1,1);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityLoom();
	}

}