package witchinggadgets.common.minetweaker;

import java.util.ArrayList;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.aspects.Aspect;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.handler.InfusedGemHandler;

@ZenClass("mods.witchinggadgets.GemCutting")
public class GemCutting
{
	@ZenMethod
	public static void addAffinity(IIngredient gem, String[] aspects)
	{
		Object oInput = WGMinetweaker.toObject(gem);
		if(oInput==null)
			return;
		ArrayList<Aspect> aspectList = new ArrayList<Aspect>();
		for(String s : aspects)
		{
			Aspect a = Aspect.getAspect(s);
			if(a!=null)
				aspectList.add(a);
		}
		MineTweakerAPI.apply(new Add((byte)0, oInput, aspectList.toArray(new Aspect[aspectList.size()])));
	}
	@ZenMethod
	public static void removeAffinity(IIngredient gem)
	{
		MineTweakerAPI.apply(new Remove((byte)0, WGMinetweaker.toObject(gem)));
	}
	
	@ZenMethod
	public static void addAversion(IIngredient gem, String[] aspects)
	{
		Object oInput = WGMinetweaker.toObject(gem);
		if(oInput==null)
			return;
		ArrayList<Aspect> aspectList = new ArrayList<Aspect>();
		for(String s : aspects)
		{
			Aspect a = Aspect.getAspect(s);
			if(a!=null)
				aspectList.add(a);
		}
		MineTweakerAPI.apply(new Add((byte)1, oInput, aspectList.toArray(new Aspect[aspectList.size()])));
	}
	@ZenMethod
	public static void removeAversion(IIngredient gem)
	{
		MineTweakerAPI.apply(new Remove((byte)1, WGMinetweaker.toObject(gem)));
	}

	private static class Add implements IUndoableAction
	{
		byte type;
		Object key;
		Aspect[] aspects;
		public Add(byte type, Object key, Aspect... aspects)
		{
			this.type = type;
			this.key = key;
			this.aspects = aspects;
		}
		@Override
		public void apply()
		{
			if(type==0)
			InfusedGemHandler.registerAffinities(key, aspects);
			else
				InfusedGemHandler.registerAversions(key, aspects);
		}
		@Override
		public boolean canUndo()
		{
			return true;
		}
		@Override
		public void undo()
		{
			if(type==0)
				InfusedGemHandler.removeAffinities(key);
			else
				InfusedGemHandler.removeAversions(key);
		}
		@Override
		public String describe()
		{
			return "Adding Gem "+(type==0?"Affinities":"Aversions")+" for " + key;
		}
		@Override
		public String describeUndo()
		{
			return "Removing Gem "+(type==0?"Affinities":"Aversions")+" for " + key;
		}
		@Override
		public Object getOverrideKey()
		{
			return null;
		}
	}
	private static class Remove implements IUndoableAction
	{
		byte type;
		Object key;
		Aspect[] removedAspects;
		public Remove(byte type, Object key)
		{
			this.type = type;
			this.key = key;
		}
		@Override
		public void apply()
		{
			if(type==0)
			{
				if(key instanceof String)
					removedAspects = InfusedGemHandler.naturalAffinities.get((String)key);
				else if(key instanceof ItemStack)
					removedAspects = InfusedGemHandler.naturalAffinities.get(Utilities.getItemStackString((ItemStack)key));
				InfusedGemHandler.removeAffinities(key);
			}
			else
			{
				if(key instanceof String)
					removedAspects = InfusedGemHandler.naturalAversions.get((String)key);
				else if(key instanceof ItemStack)
					removedAspects = InfusedGemHandler.naturalAversions.get(Utilities.getItemStackString((ItemStack)key));
				InfusedGemHandler.removeAversions(key);
			}
		}
		@Override
		public void undo()
		{
			if(removedAspects!=null)
				if(type==0)
					InfusedGemHandler.registerAffinities(key, removedAspects);
				else
					InfusedGemHandler.registerAversions(key, removedAspects);
		}
		@Override
		public String describe()
		{
			return "Removing Gem "+(type==0?"Affinities":"Aversions")+" for " + key;
		}
		@Override
		public String describeUndo()
		{
			return "Re-Adding Gem "+(type==0?"Affinities":"Aversions")+" for " + key;
		}
		@Override
		public Object getOverrideKey()
		{
			return null;
		}
		@Override
		public boolean canUndo()
		{
			return true;
		}
	}
}
