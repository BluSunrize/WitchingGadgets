package witchinggadgets.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.world.World;
import witchinggadgets.common.WGContent;

public class BlockMagicBed extends BlockBed
{

	public BlockMagicBed()
	{
		super();
	}

	@Override
	public int getRenderType()
	{
		return -1;
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

//	@Override
//	public int idDropped(int par1, Random par2Random, int par3)
//	{
//		return isBlockHeadOfBed(par1) ? 0 : WGContent.ItemMagicBed.itemID;
//	}
//
//	@Override
//	public int idPicked(World par1World, int par2, int par3, int par4)
//	{
//		return WGContent.ItemMagicBed.itemID;
//	}
}
