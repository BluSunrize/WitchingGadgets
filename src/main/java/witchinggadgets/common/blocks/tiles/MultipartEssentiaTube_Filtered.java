package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileTubeFilter;
import witchinggadgets.client.ClientUtilities;
import codechicken.lib.vec.Vector3;

public class MultipartEssentiaTube_Filtered extends MultipartEssentiaTube
//implements IAspectContainer
{
	public Aspect aspectFilter = null;

	public MultipartEssentiaTube_Filtered(int meta)
	{
		super(meta);
	}

	@Override
	public void invalidateConvertedTile()
	{
		super.invalidateConvertedTile();
		TileEntity te = world().getTileEntity(x(), y(), z());
		if(te instanceof TileTubeFilter)
		{
			this.aspectFilter = ((TileTubeFilter)te).aspectFilter;
			((TileTubeFilter)te).aspectFilter = null;
		}
	}

	@Override
	public boolean renderStatic(Vector3 pos, int pass)
	{
		super.renderStatic(pos, pass);
		Tessellator tes = Tessellator.instance;
		BlockTube b = (BlockTube) ConfigBlocks.blockTube;
//		RenderBlocks renderer = new RenderBlocks(new PartMetaAccess(this));
//		IIcon icon = b.icon[0];
//		tes.setColorOpaque_I(0xffffff);
//		tes.setBrightness(b.getMixedBrightnessForBlock(renderer.blockAccess, x(), y(), z()));
//		boolean hasConnections = false;
//		for(int i=0; i<6; i++)
//		{
//			ForgeDirection fd = ForgeDirection.getOrientation(i);
//			TileEntity te = getConnectableTile(renderer.blockAccess, x(), y(), z(), fd);
//			if(te!=null)
//			{
//				if(!hasConnections)
//					hasConnections=true;
//				double xMin = fd==ForgeDirection.WEST?0:fd==ForgeDirection.EAST?.5625F: .4375F;
//				double yMin = fd==ForgeDirection.DOWN?0:fd==ForgeDirection.UP?.5625F: .4375F;
//				double zMin = fd==ForgeDirection.NORTH?0:fd==ForgeDirection.SOUTH?.5625F: .4375F;
//				double xMax = fd==ForgeDirection.WEST?.4375F:fd==ForgeDirection.EAST?1: .5625F;
//				double yMax = fd==ForgeDirection.DOWN?.4375F:fd==ForgeDirection.UP?1: .5625F;
//				double zMax = fd==ForgeDirection.NORTH?.4375F:fd==ForgeDirection.SOUTH?1: .5625F;
//				ClientUtilities.addBoxToBlockrender(xMin,yMin,zMin, xMax,yMax,zMax, icon, x(),y(),z());
//			}
//		}
		if(aspectFilter!=null)
			tes.setColorOpaque_I(aspectFilter.getColor());
		ClientUtilities.addBoxToBlockrender(.34375,.34375,.34375, .65625,.65625,.65625, b.icon[4], x(),y(),z());
		tes.setColorOpaque_I(0xffffff);
		ClientUtilities.addBoxToBlockrender(.34375,.34375,.34375, .65625,.65625,.65625, b.icon[3], x(),y(),z());
		return true;
	}

	@Override
	void calculateSuction(Aspect filter, boolean restrict, boolean dir)
	{
		super.calculateSuction(this.aspectFilter, restrict, dir);
	}

	//	@Override
	public AspectList getAspects()
	{
		if (this.aspectFilter != null) {
			return new AspectList().add(this.aspectFilter, -1);
		}
		return null;
	}

	//	@Override
	public void setAspects(AspectList aspects) {}

	//	@Override
	public boolean doesContainerAccept(Aspect tag)
	{
		return false;
	}

	//	@Override
	public int addToContainer(Aspect tag, int amount)
	{
		return 0;
	}

	//	@Override
	public boolean takeFromContainer(Aspect tag, int amount)
	{
		return false;
	}

	//	@Override
	public boolean takeFromContainer(AspectList ot)
	{
		return false;
	}

	//	@Override
	public boolean doesContainerContainAmount(Aspect tag, int amount)
	{
		return false;
	}

	//	@Override
	public boolean doesContainerContain(AspectList ot)
	{
		return false;
	}

	//	@Override
	public int containerContains(Aspect tag)
	{
		return 0;
	}

	@Override
	public void save(NBTTagCompound tag)
	{
		super.save(tag);
		if(this.aspectFilter != null)
			tag.setString("AspectFilter", this.aspectFilter.getTag());
	}
	@Override
	public void load(NBTTagCompound tag)
	{
		super.load(tag);
		this.aspectFilter = Aspect.getAspect(tag.getString("AspectFilter"));
	}

	@Override
	public String getType()
	{
		return "witchingGadgets:essentia_tube_filtered";
	}
	@Override
	public Iterable<ItemStack> getDrops()
	{
		List<ItemStack> drops = new ArrayList();
		for(ItemStack is : super.getDrops())
			drops.add(is);
		if(aspectFilter!=null)
			drops.add(new ItemStack(ConfigItems.itemResource, 1, 13));
		return drops;
	}

	@Override
	public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack stack)
	{
		if(aspectFilter!=null)
		{
			if(player.isSneaking())
			{
				aspectFilter = null;
				if(world().isRemote)
					world().playSound(x()+.5, y()+.5, z()+.5, "thaumcraft:page", 1.0F, 1.0F, false);
				else
				{
					ForgeDirection fd = ForgeDirection.getOrientation(hit.sideHit);
					world().spawnEntityInWorld(new EntityItem(world(), x()+.5+fd.offsetX/3f, y()+.5, z()+.5+fd.offsetZ/3f, new ItemStack(ConfigItems.itemResource, 1, 13)));
					sendDescUpdate();
				}
				return true;
			}
		}
		else if(player.getHeldItem()!=null && player.getHeldItem().getItem().equals(ConfigItems.itemResource) && player.getHeldItem().getItemDamage()==13)
		{
			if(((IEssentiaContainerItem)player.getHeldItem().getItem()).getAspects(player.getHeldItem()) != null)
			{
				aspectFilter = ((IEssentiaContainerItem)player.getHeldItem().getItem()).getAspects(player.getHeldItem()).getAspects()[0];
				player.getHeldItem().stackSize -= 1;
				if (world().isRemote)
					world().playSound(x()+.5, y()+.5, z()+.5, "thaumcraft:page", 1f, 1f, false);
				else
					sendDescUpdate();
			}
		}
		return false;
	}
}