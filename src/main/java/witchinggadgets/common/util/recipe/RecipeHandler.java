package witchinggadgets.common.util.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class RecipeHandler
{

	private List<SpinningRecipe> map_spinningWheel = new ArrayList<SpinningRecipe>();
	private List<WeavingRecipe> map_loom = new ArrayList<WeavingRecipe>();

	public void addRecipe(Object recipe)
	{
		if(recipe instanceof SpinningRecipe)
			map_spinningWheel.add((SpinningRecipe) recipe);
		if(recipe instanceof WeavingRecipe)
			map_loom.add((WeavingRecipe) recipe);
	}

	public SpinningRecipe getSpinningRecipe(ItemStack[] input)
	{
		Iterator i = map_spinningWheel.iterator();
		int factualLength = 0;
		for(ItemStack temp : input)
		{
			if(temp!=null)factualLength++;
		}
		ItemStack[] inputCopy = new ItemStack[factualLength];
		int ix=0;
		for(ItemStack temp : input)
		{
			if(temp!=null)
			{
				//System.out.println(temp.getDisplayName());
				inputCopy[ix]=temp;
				ix++;
			}
		}
		
		while(i.hasNext())
		{
			SpinningRecipe s = (SpinningRecipe)i.next();
			if(s.inputsMatch(null, inputCopy))return s;
		}
		return null;
	}
	
	public WeavingRecipe getWeavingRecipe(EntityPlayer player, ItemStack[] input)
	{
		Iterator i = map_loom.iterator();
		int factualLength = 0;
		for(ItemStack temp : input)
		{
			if(temp!=null)factualLength++;
		}
		ItemStack[] inputCopy = new ItemStack[factualLength];
		int ix=0;
		for(ItemStack temp : input)
		{
			if(temp!=null)
			{
				//System.out.println(temp.getDisplayName());
				inputCopy[ix]=temp;
				ix++;
			}
		}
		while(i.hasNext())
		{
			WeavingRecipe w = (WeavingRecipe)i.next();
			if(w.inputsMatch(player, inputCopy))return w;
		}
		return null;
	}
}
