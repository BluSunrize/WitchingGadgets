package witchinggadgets.client.nei;

import witchinggadgets.WitchingGadgets;
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
