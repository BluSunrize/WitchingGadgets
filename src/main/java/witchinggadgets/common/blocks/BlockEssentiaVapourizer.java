package witchinggadgets.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.tiles.TileEntityEssentiaVapourizer;

public class BlockEssentiaVapourizer extends Block
{
	public BlockEssentiaVapourizer()
	{
		super(Material.wood);
		this.setCreativeTab(WitchingGadgets.tabWG);
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return true;
	}

	@Override
	public int getRenderType()
	{
		return 0;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		return true;
	}
	
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return false;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityEssentiaVapourizer();
	}
}