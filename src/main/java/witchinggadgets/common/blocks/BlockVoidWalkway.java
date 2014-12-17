package witchinggadgets.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import witchinggadgets.common.blocks.tiles.TileEntityVoidWalkway;

public class BlockVoidWalkway extends BlockContainer {

	public BlockVoidWalkway()
	{
		super(Material.circuits);
		this.setHardness(0.0F);
		this.setResistance(0.0F);
		this.setStepSound(soundTypeStone);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(!player.isSneaking() && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemBlock)
		{
			ItemBlock ib = (ItemBlock)player.getCurrentEquippedItem().getItem();
			if (ib.placeBlockAt(player.getCurrentEquippedItem(),player,world, x, y, z, side, hitX, hitY, hitZ, player.getCurrentEquippedItem().getItemDamage()))
			{
				if(!player.capabilities.isCreativeMode)--player.getCurrentEquippedItem().stackSize;
				return true;
			}			
		}	
		return false;
	}

	@Override
	public int getRenderBlockPass()
	{	
		return 1;
	}
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		return super.shouldSideBeRendered(iBlockAccess, x, y, z, side) && !iBlockAccess.getBlock(x,y,z).equals(this);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{	
		this.blockIcon = iconRegister.registerIcon("witchinggadgets:voidWalkway");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityVoidWalkway();
	}
}
