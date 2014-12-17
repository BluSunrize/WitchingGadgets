package witchinggadgets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import witchinggadgets.common.CommonProxy;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.WGModCompat;
import witchinggadgets.common.util.WGCreativeTab;
import witchinggadgets.common.util.handler.EventHandler;
import witchinggadgets.common.util.handler.PlayerTickHandler;
import witchinggadgets.common.util.handler.WGWandManager;
import witchinggadgets.common.util.network.WGPacketPipeline;
import witchinggadgets.common.util.recipe.RecipeHandler;
import witchinggadgets.common.world.VillageComponentPhotoshop;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

@Mod(modid = WitchingGadgets.MODID, name = WitchingGadgets.MODNAME, version = WitchingGadgets.VERSION, dependencies="required-after:Thaumcraft;required-after:TravellersGear;after:TwilightForest;after:Mystcraft;after:TConstruct;after:MagicBees;after:ForgeMultipart")
public class WitchingGadgets
{
	public static final String MODID = "WitchingGadgets";
	public static final String MODNAME = "Witching Gadgets";
	public static final String VERSION = "${version}";
	//public static BiomeGenBase BiomeMirror;

	public PlayerTickHandler playerTickHandler;

	public RecipeHandler customRecipeHandler;

	public WGWandManager wgWandManager = new WGWandManager();

	public static CreativeTabs tabWG = new WGCreativeTab(CreativeTabs.getNextID(), "witchinggadgets");
	public static final WGPacketPipeline packetPipeline = new WGPacketPipeline();
	public static final Logger logger = LogManager.getLogger("WitchingGadgets");
	public EventHandler eventHandler;

	@Instance("WitchingGadgets")
	public static WitchingGadgets instance = new WitchingGadgets();	

	@SidedProxy(clientSide="witchinggadgets.client.ClientProxy", serverSide="witchinggadgets.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		//TODO CARPENTERS WORKAROUND
		FMLForgePlugin.RUNTIME_DEOBF = true;

		logger.log(Level.INFO, "Setting up 'WitchingGadgets'");

		WGConfig.loadConfig(event);
		WGContent.preInit();

		//BiomeMirror = new BiomeGenMirror(WGConfig.BiomeMirrorID).setColor(0).setBiomeName("Crystal Void").setDisableRain().setTemperatureRainfall(0F, 0.0F).setHeight(new Height(0.1F, 0.1F));
		//BiomeDictionary.registerBiomeType(BiomeMirror, Type.MAGICAL);

		eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		playerTickHandler = new PlayerTickHandler();
		FMLCommonHandler.instance().bus().register(eventHandler);
		FMLCommonHandler.instance().bus().register(playerTickHandler);


		//		if (RailcraftConfig.isWorldGenEnabled("workshop"))
		//		{
		//			int id = RailcraftConfig.villagerID();
		//			VillagerRegistry.instance().registerVillagerId(id);
		//			VillagerRegistry.instance().registerVillageTradeHandler(id, new VillagerTradeHandler());
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageComponentPhotoshop.VillageManager());
		try {
			MapGenStructureIO.func_143031_a(VillageComponentPhotoshop.class, "WGVillagePhotoWorkshop");
			//				MapGenStructureIO.func_143031_a(ComponentWorkshop.class, "railcraft:workshop");
		} catch (Throwable e) {
		}
		//		}
		try
		{
			MapGenStructureIO.func_143031_a(VillageComponentPhotoshop.class, "WGVillagePhotoWorkshop");
		}
		catch (Throwable e)
		{
			logger.log(Level.ERROR, "Photographer's Workshop not added to Villages");
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenders();
		packetPipeline.initialise();

		WGContent.init();

		this.customRecipeHandler = new RecipeHandler();
		//		DimensionManager.registerProviderType(WGConfig.dimensionMirrorID, WorldProviderMirror.class, false);
		//		DimensionManager.registerDimension(WGConfig.dimensionMirrorID, WGConfig.dimensionMirrorID);
		//		GameRegistry.registerWorldGenerator(new WGWorldGen(), 1);

		proxy.registerHandlers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

		//FMLInterModComms.sendMessage("Sync", "treadmill", "thaumcraft.common.entities.golems.EntityGolemBase:4");
		/** TODO Readd this when iChun updates!
		FMLInterModComms.sendMessage("GraviGun", "addBlockIDToGrabList", WGContent.BlockWoodenDevice.blockID + ": 1" + ", " + WGContent.BlockWallMirror.blockID + ", " + WGContent.BlockCustomAiry.blockID + ", " + WGContent.BlockVoidWalkway.blockID + ", " + WGContent.BlockPortal.blockID );
	    FMLInterModComms.sendMessage("PortalGun", "addBlockIDToGrabList", WGContent.BlockWoodenDevice.blockID + ": 1" + ", " + WGContent.BlockWallMirror.blockID + ", " + WGContent.BlockCustomAiry.blockID + ", " + WGContent.BlockVoidWalkway.blockID + ", " + WGContent.BlockPortal.blockID );
		 */

		//		VillageComponentPhotoshop.VillageManager villageManager = new VillageComponentPhotoshop.VillageManager();
		//		VillagerRegistry.instance().registerVillageCreationHandler(villageManager);
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		WGModCompat.init();
		//WGModCompat.addTags();
		//((BiomeGenMirror)BiomeMirror).resetSpawnLists();
		WGContent.postInit();
		packetPipeline.postInitialise();
	}
}