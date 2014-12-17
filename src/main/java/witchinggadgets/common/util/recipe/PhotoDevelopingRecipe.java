package witchinggadgets.common.util.recipe;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.util.Utilities;

public class PhotoDevelopingRecipe extends ShapelessArcaneRecipe
{

	public PhotoDevelopingRecipe()
	{
		super("SCANCAMERA", new ItemStack(WGContent.ItemMaterial,1,10), new AspectList().add(Aspect.AIR,5).add(Aspect.WATER,5).add(Aspect.ORDER,5), new Object[]{new ItemStack(WGContent.ItemMaterial,1,9),"dyeBlack",Items.paper});
	}

	@Override
	public AspectList getAspects(IInventory iinventoryCrafting)
	{
		ArrayList<ItemStack> paper = new ArrayList<ItemStack>();

		for (int i = 0; i < iinventoryCrafting.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = iinventoryCrafting.getStackInSlot(i);

			if (stackInSlot != null)
			{
				if(stackInSlot.getItem().equals(Items.paper))
					paper.add(stackInSlot);
			}
		}
		return new AspectList().add(Aspect.AIR,5*paper.size()).add(Aspect.WATER,5*paper.size()).add(Aspect.ORDER,5*paper.size());
	}

	@Override
	public ItemStack getCraftingResult(IInventory iinventoryCrafting)
	{
		ItemStack photoplate = null;
		ArrayList<ItemStack> paper = new ArrayList<ItemStack>();

		for (int i = 0; i < iinventoryCrafting.getSizeInventory(); i++)
		{
			if(i==10 || i==9)
				break;
			ItemStack stackInSlot = iinventoryCrafting.getStackInSlot(i);
			if (stackInSlot != null)
			{
				if(stackInSlot.getItem().equals(WGContent.ItemMaterial) && stackInSlot.getItemDamage()==9)
					photoplate = stackInSlot;
				else if(stackInSlot.getItem().equals(Items.paper))
					paper.add(stackInSlot);
			}
		}
		ItemStack developed = new ItemStack(WGContent.ItemMaterial,paper.size(),10);
		developed.setTagCompound(photoplate.getTagCompound());
		return developed;
	}

	@Override
	public boolean matches(IInventory iinventoryCrafting, World world, EntityPlayer player)
	{
		//System.out.println("YUCK");
		ItemStack photoplate = null;
		ArrayList<ItemStack> paper = new ArrayList<ItemStack>();
		ArrayList<ItemStack> ink = new ArrayList<ItemStack>();

		for (int i = 0; i < iinventoryCrafting.getSizeInventory(); i++)
		{
			if(i==10 || i==9)
				break;
//			System.out.println("i: "+i);
			ItemStack stackInSlot = iinventoryCrafting.getStackInSlot(i);
//			System.out.println("stackInSlot: "+stackInSlot);
			if (stackInSlot != null)
			{
				if(stackInSlot.getItem().equals(WGContent.ItemMaterial) && stackInSlot.getItemDamage()==9)
				{
					if(!stackInSlot.hasTagCompound())
						return false;
					if(photoplate != null)
						return false;
					photoplate = stackInSlot;
				}
				else if(stackInSlot.getItem().equals(Items.paper))
					paper.add(stackInSlot);
				else if(Utilities.compareToOreName(stackInSlot, "dyeBlack"))
					ink.add(stackInSlot);
				else
				{
					//System.out.println(OreDictionary.itemMatches(new ItemStack(Item.dyePowder,1,0), stackInSlot, false));
					//System.out.println("Stopping at i: "+i);
					//System.out.println(stackInSlot.getDisplayName());
					return false;
				}
			}
		}
		//System.out.println(photoplate!=null && !paper.isEmpty() && !ink.isEmpty() && (paper.size()==ink.size()));
		return photoplate!=null && !paper.isEmpty() && !ink.isEmpty() && (paper.size()==ink.size());
	}

}
