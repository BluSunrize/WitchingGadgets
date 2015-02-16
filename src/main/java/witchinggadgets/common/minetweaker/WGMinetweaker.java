package witchinggadgets.common.minetweaker;

import static minetweaker.api.minecraft.MineTweakerMC.getItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;

public class WGMinetweaker
{
	public static void init()
	{
		MineTweakerAPI.registerClass(SpinningWheel.class);
		MineTweakerAPI.registerClass(InfernalBlastfurnace.class);
	}

	/** Helper Methods */
	public static ItemStack toStack(IItemStack iStack)
	{
		return getItemStack(iStack);
	}
	public static Object toObject(IIngredient iStack)
	{
		if (iStack == null)
			return null;
		else
		{
			if(iStack instanceof IOreDictEntry)
				return ((IOreDictEntry)iStack).getName();
			else if(iStack instanceof IItemStack)
				return getItemStack((IItemStack) iStack);
			else
				return null;
		}
	}
	public static Object[] toObjects(IIngredient[] iStacks)
	{
		Object[] oA = new Object[iStacks.length];
		for(int i=0; i<iStacks.length; i++)
			oA[i] = toObject(iStacks[i]);
		return oA;
	}
}
