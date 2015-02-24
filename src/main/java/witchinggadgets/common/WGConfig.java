package witchinggadgets.common;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class WGConfig
{
	public static int dimensionMirrorID;
	public static int BiomeMirrorID;
	
	public static int cloakAnimationMode;
	public static boolean enableEnchFabLoom;
	public static boolean limitBookSearchToCategory;
	
	public static boolean coremod_allowBootsRepair;
	public static boolean coremod_allowFocusPouchActive;
	public static boolean coremod_allowEnchantModifications;
	
	public static int smelteryResultForClusters;
	
	
	public static void loadConfig(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();

		BiomeMirrorID = config.get("Biome IDs", "Crystal Void ID", 30).getInt();
		
		dimensionMirrorID = config.get("Dimension ID", "Crystal Void", 19).getInt();
		
		// Random Config Options
		cloakAnimationMode = config.get("Other Options", "Cloak Animation Mod", 2, "0 = no animation, 1 = rotate cloak when legs move, 2 = stretch cloak when legs move").getInt();
		smelteryResultForClusters = config.get("Other Options", "Smeltery Result for Clusters", 144*3, "How many milliBuckets of molten Metal a cluster should give. 144mB equal 1 ingot. Set to 0 to disable smeltery recipes.").getInt();
		limitBookSearchToCategory = config.get("Other Options", "Limit Book Search", false, "Thaumonomicon Search to currently active category").getBoolean(false);
		coremod_allowBootsRepair = config.get("Other Options", "Allow Boot repair", true, "Dis-/enable repairing the Boots of the Traveller with leather").getBoolean(true);
		coremod_allowFocusPouchActive = config.get("Other Options", "Allow FocusPouch active ability", true, "Dis-/enable the IActiveAbiltiy on the FocusPouch. With this enabled, TGs active ability menu will allow you to open the pouch.").getBoolean(true);

		config.save();
	}
	
}
