package witchinggadgets.common.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.api.ITerraformFocus;
import witchinggadgets.client.render.BlockRenderMetalDevice;
import witchinggadgets.common.blocks.tiles.TileEntityEssentiaPump;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformFocus;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWGMetalDevice extends BlockContainer implements ITerraformFocus
{
	public static String[] subNames = {"essentiaPump","terraformer","tfFocusPlains","tfFocusColdTaiga","tfFocusDesert","tfFocusJungle","tfFocusHell","voidmetalBlock","tfFocusTaint","tfFocusMushroom"};
	IIcon[] icons = new IIcon[subNames.length];

	public BlockWGMetalDevice()
	{
		super(Material.iron);
		this.setHardness(4F);
		this.setResistance(15);
		setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return worldObj.getBlockMetadata(x, y, z)==7;
    }
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{	
		for(int i=0;i<icons.length;i++)
		{
			if(i<7||i>7)
				icons[i] = iconRegister.registerIcon("thaumcraft:metalbase");
			else
				icons[i] = iconRegister.registerIcon("witchinggadgets:"+subNames[i]);
		}
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if(meta<icons.length)
			return icons[meta];
		return null;
	}
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta<icons.length)
			return icons[meta];
		return null;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
	{
		return false;
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

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		return super.shouldSideBeRendered(iBlockAccess, x, y, z, side);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityEssentiaPump)
		{
			ForgeDirection fd = ((TileEntityEssentiaPump)world.getTileEntity(x, y, z)).facing;
			this.setBlockBounds(fd==ForgeDirection.EAST?.25f:0,fd==ForgeDirection.UP?.25f:0,fd==ForgeDirection.SOUTH?.25f:0, fd==ForgeDirection.WEST?.75f:1,fd==ForgeDirection.DOWN?.75f:1,fd==ForgeDirection.SOUTH?.75f:1);
		}
		else if(world.getBlockMetadata(x, y, z)>=2&&world.getBlockMetadata(x, y, z)<=6)
			this.setBlockBounds(.125f,0,.125f, .875f,.75f,.875f);
		else
			this.setBlockBounds(0,0,0,1,1,1);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		switch(metadata)
		{
		case 0:
			return new TileEntityEssentiaPump();
		case 1:
			return new TileEntityTerraformer();
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return new TileEntityTerraformFocus();
		case 7:
			return null;
		case 8:
		case 9:
			return new TileEntityTerraformFocus();
		}
		return null;
	}
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i=0; i<subNames.length; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
	{
		int playerViewQuarter = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int meta = world.getBlockMetadata(x, y, z);
		int f = playerViewQuarter==0 ? 2:playerViewQuarter==1 ? 5:playerViewQuarter==2 ? 3: 4;
		if(meta == 0)
			((TileEntityEssentiaPump)world.getTileEntity(x,y,z)).facing = ForgeDirection.getOrientation(f).getOpposite();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
	{
	}

	@Override
	public int getRenderType()
	{
		return BlockRenderMetalDevice.renderID;
	}

	@Override
	public Aspect requiredAspect(int meta)
	{
		if(meta<subNames.length && subNames[meta].startsWith("tfFocus"))
		{
			if(subNames[meta].equalsIgnoreCase("tfFocusPlains"))
				return Aspect.PLANT;
			if(subNames[meta].equalsIgnoreCase("tfFocusColdTaiga"))
				return Aspect.COLD;
			if(subNames[meta].equalsIgnoreCase("tfFocusDesert"))
				return Aspect.FIRE;
			if(subNames[meta].equalsIgnoreCase("tfFocusJungle"))
				return Aspect.TREE;
			if(subNames[meta].equalsIgnoreCase("tfFocusHell"))
				return Aspect.DARKNESS;
			if(subNames[meta].equalsIgnoreCase("tfFocusTaint"))
				return Aspect.TAINT;
			if(subNames[meta].equalsIgnoreCase("tfFocusMushroom"))
				return Aspect.SLIME;
		}
		return null;
	}
	@Override
	public Aspect requiredAspect(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x,y,z);
		if(meta<subNames.length && subNames[meta].startsWith("tfFocus"))
		{
			if(subNames[meta].equalsIgnoreCase("tfFocusPlains"))
				return Aspect.PLANT;
			if(subNames[meta].equalsIgnoreCase("tfFocusColdTaiga"))
				return Aspect.COLD;
			if(subNames[meta].equalsIgnoreCase("tfFocusDesert"))
				return Aspect.FIRE;
			if(subNames[meta].equalsIgnoreCase("tfFocusJungle"))
				return Aspect.TREE;
			if(subNames[meta].equalsIgnoreCase("tfFocusHell"))
				return Aspect.DARKNESS;
			if(subNames[meta].equalsIgnoreCase("tfFocusTaint"))
				return Aspect.TAINT;
			if(subNames[meta].equalsIgnoreCase("tfFocusMushroom"))
				return Aspect.SLIME;
		}
		return null;
	}

	@Override
	public BiomeGenBase getCreatedBiome(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x,y,z);
		if(meta<subNames.length && subNames[meta].startsWith("tfFocus"))
		{
			if(subNames[meta].equalsIgnoreCase("tfFocusPlains"))
				return BiomeGenBase.plains;
			if(subNames[meta].equalsIgnoreCase("tfFocusColdTaiga"))
				return BiomeGenBase.coldTaiga;
			if(subNames[meta].equalsIgnoreCase("tfFocusDesert"))
				return BiomeGenBase.desert;
			if(subNames[meta].equalsIgnoreCase("tfFocusJungle"))
				return BiomeGenBase.jungle;
			if(subNames[meta].equalsIgnoreCase("tfFocusHell"))
				return BiomeGenBase.hell;
			if(subNames[meta].equalsIgnoreCase("tfFocusTaint"))
				return ThaumcraftWorldGenerator.biomeTaint;
			if(subNames[meta].equalsIgnoreCase("tfFocusMushroom"))
				return BiomeGenBase.mushroomIsland;
		}
		return null;
	}
	@Override
	public ItemStack getDisplayedBlock(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x,y,z);
		if(meta<subNames.length && subNames[meta].startsWith("tfFocus"))
		{
			if(subNames[meta].equalsIgnoreCase("tfFocusPlains"))
				return new ItemStack(Blocks.grass);
			if(subNames[meta].equalsIgnoreCase("tfFocusColdTaiga"))
				return new ItemStack(Blocks.ice);
			if(subNames[meta].equalsIgnoreCase("tfFocusDesert"))
				return new ItemStack(Blocks.sand);
			if(subNames[meta].equalsIgnoreCase("tfFocusJungle"))
				return new ItemStack(Blocks.log,1,3);
			if(subNames[meta].equalsIgnoreCase("tfFocusHell"))
				return new ItemStack(Blocks.nether_brick);
			if(subNames[meta].equalsIgnoreCase("tfFocusTaint"))
				return new ItemStack(ConfigBlocks.blockTaint);
			if(subNames[meta].equalsIgnoreCase("tfFocusMushroom"))
				return new ItemStack(Blocks.mycelium);
		}
		return null;
	}
}