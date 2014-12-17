package witchinggadgets.common.util.research;


import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import witchinggadgets.WitchingGadgets;

public class WGResearchItem extends ResearchItem
{
	public WGResearchItem(String key, String category, AspectList tags, int displayX, int displayY, int complexity, ResourceLocation icon)
	{
		super(key,category,tags,displayX,displayY,complexity,icon);
	}

	public WGResearchItem(String key, String category, AspectList tags, int displayX, int displayY, int complexity, ItemStack icon)
	{
		super(key,category,tags,displayX,displayY,complexity,icon);
	}

	@Override
	public String getName()
	{
		return StatCollector.translateToLocal("witchinggadgets_research_name."+key);
	}

	@Override
	public String getText()
	{
		return StatCollector.translateToLocal("witchinggadgets_research_text."+key);
	}

	@Override
	public ResearchItem setParents(String... par)
	{
		for(String p:par)
			if(ResearchCategories.getResearch(p) == null)
			{
				WitchingGadgets.logger.log(Level.ERROR, "Invalid Parent for Item "+this.key+". Parent "+p+"doesn't exist!");
				return null;
			}

		this.parents = par;
		return this;
	}

	@Override
	public ResearchItem setParentsHidden(String... par)
	{
		for(String p:par)
			if(ResearchCategories.getResearch(p) == null)
			{
				WitchingGadgets.logger.log(Level.ERROR, "Invalid HiddenParent for Item "+this.key+". Parent "+p+"doesn't exist!");
				return null;
			}
		this.parentsHidden = par;
		return this;
	}
	
	public ResearchItem addWarp(int warp)
	{
	    ThaumcraftApi.addWarpToResearch(this.key, warp);
		return this;
	}
}