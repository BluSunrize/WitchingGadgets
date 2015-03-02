package witchinggadgets.client.nei;

import net.minecraft.item.ItemStack;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.items.ItemClusters;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIWGConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		API.registerRecipeHandler(new NEISpinningWheelHandler());
		API.registerUsageHandler(new NEISpinningWheelHandler());

		API.registerRecipeHandler(new NEIInfernalBlastfurnaceHandler());
		API.registerUsageHandler(new NEIInfernalBlastfurnaceHandler());

		if(ItemClusters.materialMap.isEmpty())
			API.hideItem(new ItemStack(WGContent.ItemCluster));
		API.hideItem(new ItemStack(WGContent.BlockWallMirror));
		API.hideItem(new ItemStack(WGContent.BlockVoidWalkway));
	}


	@Override
	public String getName()
	{
		return "Witching Gadgets NEI";
	}
	@Override
	public String getVersion()
	{
		return WitchingGadgets.VERSION;
	}
}
