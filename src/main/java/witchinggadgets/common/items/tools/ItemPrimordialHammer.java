package witchinggadgets.common.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;
import travellersgear.api.IActiveAbility;
import witchinggadgets.api.IPrimordial;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.WGDamageSources;

public class ItemPrimordialHammer extends ItemPickaxe implements IPrimordial, IActiveAbility, IRepairable
{
	IIcon overlay;
	public static Material[] validMats = {Material.anvil,Material.clay,Material.craftedSnow,Material.glass,Material.grass,Material.ground,Material.ice,Material.iron,Material.packedIce,Material.piston,Material.rock,Material.sand,Material.snow};

	public ItemPrimordialHammer(ToolMaterial mat)
	{
		super(mat);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase user)
	{
		if(target instanceof EntitySlime || target instanceof EntitySkeleton)
			target.attackEntityFrom(WGDamageSources.crushing, 6);
		stack.damageItem(1, user);
		return true;
	}
	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand)
	{
		return true;
	}

	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{

	}

	@Override
	public int getReturnedPearls(ItemStack stack)
	{
		return 2;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialHammer");
		this.overlay = iconRegister.registerIcon("witchinggadgets:primordialHammer_overlay");
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getRenderPasses(int meta)
	{
		return 2;
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		if(pass == 0)
			return itemIcon;
		return overlay;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int ix, int iy, int iz, EntityPlayer player)
	{
		World world = player.worldObj;
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
		if(mop == null)
			return false;
		int side = mop.sideHit;
		int[] range = new int[3];
		range[0] = side==4||side==5?0: 1;
		range[1] = side==0||side==1?0: 1;
		range[2] = side==2||side==3?0: 1;
		for(int yy=-range[1]; yy<=range[1]; yy++)
			for(int zz=-range[2]; zz<=range[2]; zz++)
				for(int xx=-range[0]; xx<=range[0]; xx++)
				{
					int x = ix+xx;
					int y = iy+yy;
					int z = iz+zz;
					if(!world.blockExists(x, y, z))
						continue;
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);
					Material mat = world.getBlock(x, y, z).getMaterial();

					if(!world.isRemote && block != null && !block.isAir(world, x, y, z) && block.getPlayerRelativeBlockHardness(player, world, x, y, z) != 0)
					{
						if(!block.canHarvestBlock(player, meta) || !Utilities.isRightMaterial(mat, validMats))
							continue;
						if(!player.capabilities.isCreativeMode && block != Blocks.bedrock)
						{
							int localMeta = world.getBlockMetadata(x, y, z);
							if (block.removedByPlayer(world, player, x, y, z, true))
								block.onBlockDestroyedByPlayer(world, x, y, z, localMeta);
							block.harvestBlock(world, player, x, y, z, localMeta);
							block.onBlockHarvested(world, x, y, z, localMeta, player);
						} 
						else
							world.setBlockToAir(x, y, z);
						if(!world.isRemote)
							world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
					}
				}
		return false;
	}
}