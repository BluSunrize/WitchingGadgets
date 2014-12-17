package witchinggadgets.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidDarkIron extends BlockFluidFinite
{
	public BlockFluidDarkIron(Fluid fluid)
	{
		super(fluid, Material.lava);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{	
		this.blockIcon = par1IconRegister.registerIcon("witchinggadgets:darkIronFluid");
	}
}