package witchinggadgets.common.util.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import witchinggadgets.common.WGContent;

public class KamaRecipe extends ShapedArcaneRecipe
{
	public KamaRecipe(int meta)
	{
		super("CLOAKKAMA", new ItemStack(WGContent.ItemKama,1,meta), new AspectList().add(Aspect.AIR,5).add(Aspect.ORDER,5), new Object[]{"B","C", 'B', "baubleBeltBase", 'C', new ItemStack(WGContent.ItemCloak,1,meta)});
	}

	@Override
	public boolean matches(IInventory inv, World world, EntityPlayer player)
	{
		boolean match = super.matches(inv, world, player);
		if(match)
			System.out.println("match");
		return match;
	}

	@Override
	public ItemStack getCraftingResult(IInventory iinventoryCrafting)
	{
		System.out.println("get Result");
		ItemStack cloak = null;

		for (int i = 0; i < iinventoryCrafting.getSizeInventory(); i++)
			if(i!=10 && i!=9)
			{
				ItemStack stackInSlot = iinventoryCrafting.getStackInSlot(i);
				if (stackInSlot != null && stackInSlot.getItem().equals(WGContent.ItemCloak))
					cloak = stackInSlot;
			}
		if(cloak==null)
		{
			System.out.println("no cloak");
			return null;
		}
		ItemStack output = this.getRecipeOutput();
		output.setTagCompound(cloak.getTagCompound());
		return output;
	}

}