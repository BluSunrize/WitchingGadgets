package witchinggadgets.common.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockAiry;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.tiles.TileEntityTempLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockModifiedAiry extends BlockAiry
{

	public BlockModifiedAiry()
	{
		super();
		this.setCreativeTab(WitchingGadgets.tabWG);
	}
	

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		super.registerBlockIcons(iconRegister);
		this.blockIcon = iconRegister.registerIcon("thaumcraft:blank");
	}

	@SideOnly(Side.CLIENT)
	public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		return false;
	}

	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		return false;
	}

	public float getBlockHardness(World world, int x, int y, int z)
	{
		return 0;
	}

	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		return 0;
	}

	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return 14;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess ba, int x, int y, int z)
	{
		setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		super.setBlockBoundsBasedOnState(ba, x, y, z);
	}

	public boolean isBlockReplaceable(World world, int x, int y, int z)
	{
		return true;
	}

	public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
	{
		return true;
	}

	public boolean isLeaves(World world, int x, int y, int z)
	{
		return true;
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
	}

	public int idPicked(World world, int x, int y, int z)
	{
		return 0;
	}

	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
	{
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World w, int i, int j, int k, Random r)
	{
//		int md = w.getBlockMetadata(i, j, k);
		if (r.nextInt(50) == 0)
		{
			int x1 = i + r.nextInt(2) - r.nextInt(2);
			int y1 = j + r.nextInt(2) - r.nextInt(2);
			int z1 = k + r.nextInt(2) - r.nextInt(2);
			int x2 = x1 + r.nextInt(2) - r.nextInt(2);
			int y2 = y1 + r.nextInt(2) - r.nextInt(2);
			int z2 = z1 + r.nextInt(2) - r.nextInt(2);
			Thaumcraft.proxy.wispFX3(w, x1, y1, z1, x2, y2, z2, 0.1F + r.nextFloat() * 0.1F, 7, false, r.nextBoolean() ? -0.033F : 0.033F);
		}
	}

	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityTempLight();
	}

	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
	}

	public boolean isAirBlock(World world, int x, int y, int z)
	{
		return true;
	}
}
