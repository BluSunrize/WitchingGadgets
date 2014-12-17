package witchinggadgets.common.util.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import witchinggadgets.common.util.Utilities;

public class InfusedGemHandler
{
	
	
	public static Aspect[] getNaturalAffinities(ItemStack gem)
	{
		//		if(gem.)
		AspectList l = new AspectList();
		if(Utilities.compareToOreName(gem, "gemDiamond"))
		{
			l.add(Aspect.ORDER,1);
			l.add(Aspect.ENTROPY,1);
		}
		if(Utilities.compareToOreName(gem, "gemEmerald"))
		{
			l.add(Aspect.ORDER,1);
			l.add(Aspect.EARTH,1);
		}
		if(Utilities.compareToOreName(gem, "crystalNetherQuartz"))
		{
			l.add(Aspect.ENTROPY,1);
			l.add(Aspect.FIRE,1);
		}
		if(Utilities.compareToOreName(gem, "crystalNetherQuartz"))
		{
			l.add(Aspect.ORDER,1);
			l.add(Aspect.AIR,1);
		}
		if(Utilities.compareToOreName(gem, "gemTopaz") || Utilities.compareToOreName(gem, "gemAmber"))
			l.add(Aspect.AIR,1);
		if(Utilities.compareToOreName(gem, "gemPeridot") || Utilities.compareToOreName(gem, "gemMalachite") || Utilities.compareToOreName(gem, "gemAmber"))
			l.add(Aspect.EARTH,1);
		if(Utilities.compareToOreName(gem, "gemRuby") || Utilities.compareToOreName(gem, "gemTopaz"))
			l.add(Aspect.FIRE,1);
		if(Utilities.compareToOreName(gem, "gemSapphire") || Utilities.compareToOreName(gem, "gemMalachite") || Utilities.compareToOreName(gem, "gemTanzanite"))
			l.add(Aspect.WATER,1);
		if(Utilities.compareToOreName(gem, "gemPrimordial"))
		{
			l.add(Aspect.AIR,1);
			l.add(Aspect.EARTH,1);
			l.add(Aspect.FIRE,1);
			l.add(Aspect.WATER,1);
			l.add(Aspect.ORDER,1);
			l.add(Aspect.ENTROPY,1);
		}
		if(l.size()<1)
			return null;
		return l.getPrimalAspects();
	}
	public static Aspect[] getNaturalAversions(ItemStack gem)
	{
		//		if(gem.)
		AspectList l = new AspectList();
		if(Utilities.compareToOreName(gem, "gemEmerald"))
		{
			l.add(Aspect.WATER,1);
			l.add(Aspect.ENTROPY,1);
		}
		if(Utilities.compareToOreName(gem, "crystalNetherQuartz"))
		{
			l.add(Aspect.ORDER,1);
			l.add(Aspect.WATER,1);
		}
		if(Utilities.compareToOreName(gem, "crystalCertusQuartz"))
		{
			l.add(Aspect.ENTROPY,1);
			l.add(Aspect.FIRE,1);
		}
		if(Utilities.compareToOreName(gem, "gemRuby") || Utilities.compareToOreName(gem, "gemPeridot") || Utilities.compareToOreName(gem, "gemTopaz") || Utilities.compareToOreName(gem, "gemTanzanite") || Utilities.compareToOreName(gem, "gemMalachite") || Utilities.compareToOreName(gem, "gemSapphire") || Utilities.compareToOreName(gem, "gemAmber"))
			l.add(Aspect.ENTROPY,1);
		
		if(Utilities.compareToOreName(gem, "gemRuby") || Utilities.compareToOreName(gem, "gemTopaz"))
			l.add(Aspect.WATER,1);
		if(Utilities.compareToOreName(gem, "gemPeridot") || Utilities.compareToOreName(gem, "gemMalachite") || Utilities.compareToOreName(gem, "gemSapphire") || Utilities.compareToOreName(gem, "gemTanzanite"))
			l.add(Aspect.FIRE,1);
		if(l.size()<1)
			return null;
		return l.getPrimalAspects();
	}
	
	public static boolean isGem(ItemStack stack)
	{
		for(int oid : OreDictionary.getOreIDs(stack))
			if(OreDictionary.getOreName(oid).startsWith("gem") || OreDictionary.getOreName(oid).startsWith("crystal"))
				return true;
		return false;
	}
}