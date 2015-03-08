package witchinggadgets.common;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class WGConfig
{
	public static int cloakAnimationMode;
	public static boolean limitBookSearchToCategory;
	public static boolean allowClusters;
	public static boolean allowTransmutations;

	public static boolean coremod_allowBootsRepair;
	public static boolean coremod_allowFocusPouchActive;
	public static boolean coremod_allowEnchantModifications;
	public static boolean coremod_allowPotionApplicationMod;
	public static Block[] coremod_worldgenValidBase_HilltopStones;
	public static Block[] coremod_worldgenValidBase_EldritchRing;

	public static int smelteryResultForClusters;
	public static float radialSpeed;

	static Configuration config;
	public static void loadConfig(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		// Random Config Options
		cloakAnimationMode = config.get("Other Options", "Cloak Animation Mod", 2, "0 = no animation, 1 = rotate cloak when legs move, 2 = stretch cloak when legs move").getInt();
		smelteryResultForClusters = config.get("Other Options", "Smeltery Result for Clusters", 144*3, "How many milliBuckets of molten Metal a cluster should give. 144mB equal 1 ingot. Set to 0 to disable smeltery recipes.").getInt();
		allowClusters = config.get("Other Options", "Enable clusters", true, "Set this to false to disable clusters, useful when you are usign AOBD.").getBoolean(true);
		allowTransmutations = config.get("Other Options", "Enable transmutations", true, "Set this to false to disable nugget transmutations, this should fix the infinite loop glitch").getBoolean(true);
		limitBookSearchToCategory = config.get("Other Options", "Limit Book Search", false, "Thaumonomicon Search to currently active category").getBoolean(false);
		radialSpeed = config.getFloat("Other Options", "Selection Radial Speed", .15f, .15f, 1, "The speed at which the gem-selection for the primordial glove opens. 15% is the minimum.");

		coremod_allowBootsRepair = config.get("Other Options", "Allow Boot repair", true, "Dis-/enable repairing the Boots of the Traveller with leather").getBoolean(true);
		coremod_allowFocusPouchActive = config.get("Other Options", "Allow FocusPouch active ability", true, "Dis-/enable the IActiveAbiltiy on the FocusPouch. With this enabled, TGs active ability menu will allow you to open the pouch.").getBoolean(true);
		coremod_allowEnchantModifications = config.get("Other Options", "Allow Enchantment modifications", true, "Dis-/enable the modification of looting and fortune modifications with the Ring of the Covetous Coin").getBoolean(true);
		coremod_allowPotionApplicationMod = config.get("Other Options", "Allow modifications to newly applied PotionEffects", true, "Dis-/enable the modification of newly applied PotionEffects. (Primordial Armor affects newly applied Warp Effects)").getBoolean(true);

		String[] cm_allowedSpawnblocks_HilltopStones = config.getStringList("Valid generation bases: HilltopStones", "Other", new String[]{"minecraft:stone","minecraft:sand","minecraft:packed_ice","minecraft:grass","minecraft:gravel","minecraft:dirt"}, "A list of valid blocks that Thaumcraft's hilltop stones can spawn upon");
		Set<Block> validBlocks = new HashSet();
		for(int ss=0; ss<cm_allowedSpawnblocks_HilltopStones.length; ss++)
		{
			String[] ssA = cm_allowedSpawnblocks_HilltopStones[ss].split(":",2);
			if(ssA.length>1)
			{
				Block b = GameRegistry.findBlock(ssA[0], ssA[1]);
				if(b!=null)
					validBlocks.add(b);
			}
		}
		coremod_worldgenValidBase_HilltopStones = validBlocks.toArray(new Block[0]);

		String[] cm_allowedSpawnblocks_EldritchRing = config.getStringList("Valid generation bases: EldritchRing", "Other", new String[]{"minecraft:stone","minecraft:sand","minecraft:packed_ice","minecraft:grass","minecraft:gravel","minecraft:dirt"}, "A list of valid blocks that Thaumcraft's eldritch obelisks can spawn upon");
		validBlocks = new HashSet();
		for(int ss=0; ss<cm_allowedSpawnblocks_EldritchRing.length; ss++)
		{
			String[] ssA = cm_allowedSpawnblocks_EldritchRing[ss].split(":",2);
			if(ssA.length>1)
			{
				Block b = GameRegistry.findBlock(ssA[0], ssA[1]);
				if(b!=null)
					validBlocks.add(b);
			}
		}
		coremod_worldgenValidBase_EldritchRing = validBlocks.toArray(new Block[0]);

		config.save();
	}

	public static int getPotionID(int base, String key)
	{
		config.load();
		int i = config.get("Other Options", "Potion ID: "+key, Utilities.getNextPotionId(base)).getInt();
		config.save();
		return i;
	}
	public static int getEnchantmentID(int base, String key)
	{
		config.load();
		int i = config.get("Other Options", "Enchantment ID: "+key, Utilities.getNextEnchantmentId(base)).getInt();
		config.save();
		return i;
	}
}