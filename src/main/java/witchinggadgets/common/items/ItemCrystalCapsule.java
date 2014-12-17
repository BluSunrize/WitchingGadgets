package witchinggadgets.common.items;

import java.util.List;
import java.util.Map;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import thaumcraft.common.lib.utils.InventoryUtils;
import witchinggadgets.WitchingGadgets;

public class ItemCrystalCapsule extends Item implements IFluidContainerItem
{
	public ItemCrystalCapsule()
	{
		super();
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	public ItemStack getContainerItem(ItemStack itemStack)
	{
		return new ItemStack(this);  
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		MovingObjectPosition localMovingObjectPosition = getMovingObjectPositionFromPlayer(world, player, false);
		if ((localMovingObjectPosition == null) || (localMovingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK))
			return stack;

		int x = localMovingObjectPosition.blockX;
		int y = localMovingObjectPosition.blockY;
		int z = localMovingObjectPosition.blockZ;
		switch (localMovingObjectPosition.sideHit)
		{
		case 0: 
			y--;
			break;
		case 1: 
			y++;
			break;
		case 2: 
			z--;
			break;
		case 3: 
			z++;
			break;
		case 4: 
			x--;
			break;
		case 5: 
			x++;
		}
		if( !player.canPlayerEdit(x, y, z, localMovingObjectPosition.sideHit, stack) || (!world.isAirBlock(x, y, z) && world.getBlock(x, y, z).getMaterial().isSolid()) )
			return stack;

		ItemStack used = useCapsule(world, x, y, z, stack);
		if (!player.inventory.addItemStackToInventory(used))
			player.dropPlayerItemWithRandomChoice(used, false);

		return stack;
	}

	ItemStack useCapsule(World world, int x, int y, int z, ItemStack stack)
	{
		//		System.out.println("use in "+world+", at "+x+", "+y+", "+z+" on Block "+world.getBlock(x, y, z)+" "+(world.getBlock(x, y, z)!=null?world.getBlock(x, y, z).getLocalizedName():""));
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid"))
		{
			Fluid f = FluidRegistry.getFluid(stack.getTagCompound().getString("fluid"));
			if(f==null || f.getBlock()==null || !f.canBePlacedInWorld())
				return stack;
			world.setBlock(x, y, z, f.getBlock());
			stack.stackSize-=1;
			world.notifyBlockOfNeighborChange(x, y, z, world.getBlock(x,y,z));
//			world.scheduleBlockUpdate(x,y,z, world.getBlock(x,y,z), 10);
			return this.getContainerItem(stack);
		}
		else
		{
			//			System.out.println(world.getBlock(x, y, z));
			Fluid f = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));
			if(f==null && world.getBlock(x, y, z) instanceof BlockDynamicLiquid && world.getBlockMetadata(x, y, z)==0)
				if(world.getBlock(x, y, z).getMaterial().equals(Material.water))
					f = FluidRegistry.WATER;
				else if(world.getBlock(x, y, z).getMaterial().equals(Material.lava))
					f = FluidRegistry.LAVA;
			if(world.getBlock(x, y, z) instanceof IFluidBlock && !((IFluidBlock)world.getBlock(x, y, z)).canDrain(world, x, y, z))
				return stack;
			if(world.getBlock(x, y, z) instanceof BlockStaticLiquid && world.getBlockMetadata(x, y, z)!=0)
				return stack;
			if(f==null)
				return stack;
			stack.stackSize-=1;
			ItemStack filled = this.getContainerItem(stack);
			if(!filled.hasTagCompound())
				filled.setTagCompound(new NBTTagCompound());
			filled.getTagCompound().setString("fluid", FluidRegistry.getFluidName(new FluidStack(f,1000)));
			world.setBlockToAir(x, y, z);
			return filled;
		}
	}

	@Override
	public FluidStack getFluid(ItemStack container)
	{
		if(!container.hasTagCompound() || container.getTagCompound().hasKey("fluid"))
			return null;
		Fluid f = FluidRegistry.getFluid(container.getTagCompound().getString("fluid"));
		if(f==null)
			return null;
		return new FluidStack(f,1000);
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return 1000;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		//		System.out.println("attempt fill");
		if(resource==null || resource.amount<1000)
			return 0;
		if(!container.hasTagCompound())
			container.setTagCompound(new NBTTagCompound());
		if(doFill)
			container.getTagCompound().setString("fluid", FluidRegistry.getFluidName(resource));
		return 1000;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		//		System.out.println("attempt drain");
		if(maxDrain<1000 || !container.hasTagCompound() || container.getTagCompound().hasKey("fluid"))
			return null;
		Fluid f = FluidRegistry.getFluid(container.getTagCompound().getString("fluid"));
		if(f==null)
			return null;
		if(doDrain)
			container.getTagCompound().removeTag("fluid");
		return new FluidStack(f,1000);
	}

	@Override
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean adv)
	{
		if(item.hasTagCompound() && item.getTagCompound().hasKey("fluid"))
			if(FluidRegistry.getFluid(item.getTagCompound().getString("fluid"))!=null)
				list.add( FluidRegistry.getFluid(item.getTagCompound().getString("fluid")).getLocalizedName(new FluidStack(FluidRegistry.getFluid(item.getTagCompound().getString("fluid")),1000)) );
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item));
		for(Map.Entry<String,Fluid> f : FluidRegistry.getRegisteredFluids().entrySet())
		{
			ItemStack s = new ItemStack(item);
			s.setTagCompound(new NBTTagCompound());
			s.getTagCompound().setString("fluid", f.getKey());
			list.add(s);
		}
	}

	public IIcon overlay;
	public IIcon fluidMask;
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:crystalCapsule");
		this.overlay = iconRegister.registerIcon("witchinggadgets:crystalCapsule_overlay");
		this.fluidMask = iconRegister.registerIcon("witchinggadgets:crystalCapsule_mask");
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		return pass==0?this.itemIcon:this.overlay;
	}

	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	public static class CapsuleDispenserBehaviour extends BehaviorDefaultDispenseItem
	{
		private final BehaviorDefaultDispenseItem deafultBehaviour = new BehaviorDefaultDispenseItem();

		@Override
		public ItemStack dispenseStack(IBlockSource dispenser, ItemStack stack)
		{
			ItemCrystalCapsule capsule = (ItemCrystalCapsule)stack.getItem();
			int x = dispenser.getXInt();
			int y = dispenser.getYInt();
			int z = dispenser.getZInt();
			EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
			ItemStack s = capsule.useCapsule(dispenser.getWorld(), x+enumfacing.getFrontOffsetX(), y+enumfacing.getFrontOffsetY(), z+enumfacing.getFrontOffsetZ(), stack);
			s = InventoryUtils.insertStack((IInventory)dispenser.getBlockTileEntity(), s, enumfacing.ordinal(), true);
			if(s!=null)
				this.deafultBehaviour.dispense(dispenser, s);
			//			if(!dispenser.getWorld().isRemote)
			//			dispenser.getWorld().spawnEntityInWorld(new EntityFireworkRocket(dispenser.getWorld(), x+enumfacing.getFrontOffsetX(), y+enumfacing.getFrontOffsetY(), z+enumfacing.getFrontOffsetZ(), null));
			//			if(s == stack)
			return stack;
			//			else
			//				return this.deafultBehaviour.dispense(dispenser, capsule.useCapsule(dispenser.getWorld(), x+enumfacing.getFrontOffsetX(), y+enumfacing.getFrontOffsetY(), z+enumfacing.getFrontOffsetZ(), stack));
		}
	}
}