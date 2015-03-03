package witchinggadgets.common;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.BlockModifiedAiry;
import witchinggadgets.common.blocks.BlockRoseVines;
import witchinggadgets.common.blocks.BlockVoidWalkway;
import witchinggadgets.common.blocks.BlockWGMetalDevice;
import witchinggadgets.common.blocks.BlockWGStoneDevice;
import witchinggadgets.common.blocks.BlockWGWoodenDevice;
import witchinggadgets.common.blocks.BlockWallMirror;
import witchinggadgets.common.blocks.ItemBlockMetalDevice;
import witchinggadgets.common.blocks.ItemBlockStoneDevice;
import witchinggadgets.common.blocks.ItemBlockWoodenDevice;
import witchinggadgets.common.blocks.tiles.TileEntityAgeingStone;
import witchinggadgets.common.blocks.tiles.TileEntityBlastfurnace;
import witchinggadgets.common.blocks.tiles.TileEntityCobbleGen;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.blocks.tiles.TileEntityEssentiaPump;
import witchinggadgets.common.blocks.tiles.TileEntityEtherealWall;
import witchinggadgets.common.blocks.tiles.TileEntityLabelLibrary;
import witchinggadgets.common.blocks.tiles.TileEntityMagicalTileLock;
import witchinggadgets.common.blocks.tiles.TileEntitySarcophagus;
import witchinggadgets.common.blocks.tiles.TileEntitySaunaStove;
import witchinggadgets.common.blocks.tiles.TileEntitySnowGen;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;
import witchinggadgets.common.blocks.tiles.TileEntityTempLight;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformFocus;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformer;
import witchinggadgets.common.blocks.tiles.TileEntityVoidWalkway;
import witchinggadgets.common.blocks.tiles.TileEntityWallMirror;
import witchinggadgets.common.items.EntityItemReforming;
import witchinggadgets.common.items.ItemClusters;
import witchinggadgets.common.items.ItemCrystalCapsule;
import witchinggadgets.common.items.ItemInfusedGem;
import witchinggadgets.common.items.ItemMagicFood;
import witchinggadgets.common.items.ItemMaterials;
import witchinggadgets.common.items.ItemThaumiumShears;
import witchinggadgets.common.items.armor.ItemAdvancedRobes;
import witchinggadgets.common.items.armor.ItemPrimordialArmor;
import witchinggadgets.common.items.baubles.ItemCloak;
import witchinggadgets.common.items.baubles.ItemKama;
import witchinggadgets.common.items.baubles.ItemMagicalBaubles;
import witchinggadgets.common.items.tools.ItemBag;
import witchinggadgets.common.items.tools.ItemPrimordialAxe;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;
import witchinggadgets.common.items.tools.ItemPrimordialHammer;
import witchinggadgets.common.items.tools.ItemPrimordialSword;
import witchinggadgets.common.items.tools.ItemScanCamera;
import witchinggadgets.common.magic.WGEnchantBackstab;
import witchinggadgets.common.magic.WGEnchantGemBrittle;
import witchinggadgets.common.magic.WGEnchantGemPotency;
import witchinggadgets.common.magic.WGEnchantInvisibleGear;
import witchinggadgets.common.magic.WGEnchantRideProtect;
import witchinggadgets.common.magic.WGEnchantStealth;
import witchinggadgets.common.magic.WGEnchantUnveiling;
import witchinggadgets.common.magic.WGPotion;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.handler.WGMultiPartHandler;
import witchinggadgets.common.util.recipe.BagColourizationRecipe;
import witchinggadgets.common.util.recipe.CloakColourizationRecipe;
import witchinggadgets.common.util.recipe.InfernalBlastfurnaceRecipe;
import witchinggadgets.common.util.recipe.RobeColourizationRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class WGContent
{
	public static Block BlockWallMirror;
	public static Block BlockVoidWalkway;
	public static Block BlockPortal;
	public static Block BlockStoneDevice;
	public static Block BlockWoodenDevice;
	public static Block BlockMetalDevice;
	public static Block BlockMagicBed;
	public static Block BlockRoseVine;
	public static Block BlockCustomAiry;

	public static Item ItemMaterial;
	public static Item ItemCluster;
	public static Item ItemCrystalCapsule;
	public static Item ItemBag;
	public static Item ItemCloak;
	public static Item ItemKama;
	public static Item ItemThaumiumShears;
	public static Item ItemAdvancedRobeChest;
	public static Item ItemAdvancedRobeLegs;
	public static Item ItemMagicFoodstuffs;
	public static Item ItemMagicBed;

	//  public static Item ItemAdvancedScribingTools;
	//	public static Item ItemEliteArmorHelm;
	//	public static Item ItemEliteArmorChest;
	//	public static Item ItemEliteArmorLegs;
	//	public static Item ItemEliteArmorBoots;
	public static Item ItemPrimordialGlove;
	public static Item ItemPrimordialHammer;
	public static Item ItemPrimordialAxe;
	public static Item ItemPrimordialSword;

	public static Item ItemPrimordialHelm;
	public static Item ItemPrimordialChest;
	public static Item ItemPrimordialLegs;
	public static Item ItemPrimordialBoots;

	public static Item ItemInfusedGem;
	public static Item ItemMagicalBaubles;
	public static Item ItemScanCamera;
	public static Item ItemRelic;

	public static Potion pot_knockbackRes;
	public static Potion pot_dissolve;
	public static Potion pot_cinderCoat;
	public static Enchantment enc_gemstonePotency;
	public static Enchantment enc_gemstoneBrittle;
	public static Enchantment enc_invisibleGear;
	public static Enchantment enc_unveiling;
	public static Enchantment enc_stealth;
	public static Enchantment enc_backstab;
	public static Enchantment enc_rideProtect;

	public static ArmorMaterial armorMatSpecialRobe = EnumHelper.addArmorMaterial("WG:ADVANCEDCLOTH", 25, new int[] { 2, 4, 3, 2 }, 25);
	public static ToolMaterial primordialTool = EnumHelper.addToolMaterial("WG:PRIMORDIALTOOL",4, 1500, 8, 6, 25);
	public static ArmorMaterial primordialArmor = EnumHelper.addArmorMaterial("WG:PRIMORDIALARMOR", 40, new int[] {3,7,6,3}, 30);
	//	public static HashMap<String,Cloak> cloakRegistry = new HashMap<String, Cloak>();

	public static void preInit()
	{
		preInitItems();
		preInitBlocks();

	}
	final static String UUIDBASE = "424C5553-5747-1694-4452-";
	public static void init()
	{
		initializeItems();
		initializeBlocks();
		int k = Potion.potionTypes.length;
		int l = 1;
		if(k<128-l)
			Utilities.extendPotionArray(l);
		String s = new UUID(109406002307L, 01L).toString();
		int id = Utilities.getNextPotionId(32);
		if(id >= 0)
			pot_knockbackRes = new WGPotion(id, false, 0x6e6e6e, 0, false, 1).setPotionName("wg.potionKnockbackRes").func_111184_a(SharedMonsterAttributes.knockbackResistance, s, 0.34D, 0);
		id = Utilities.getNextPotionId(id);
		if(id >= 0)
			pot_dissolve = new WGPotion(id, true, 0x450b45, 40, true, 2).setPotionName("wg.potionDissolve");
		id = Utilities.getNextPotionId(id);
		if(id >= 0)
			pot_cinderCoat = new WGPotion(id, true, 0x8f3f1f, 0, false, 3).setPotionName("wg.potionCinderCoat");


		int eid = Utilities.getNextEnchantmentId(64);
		if(eid >= 0)
		{
			enc_gemstonePotency = new WGEnchantGemPotency(eid, 4);
			Enchantment.addToBookList(enc_gemstonePotency);
		}
		eid = Utilities.getNextEnchantmentId(eid);
		if(eid >= 0)
			enc_gemstoneBrittle = new WGEnchantGemBrittle(eid, 1);
		eid = Utilities.getNextEnchantmentId(eid);
		if(eid >= 0)
			enc_invisibleGear = new WGEnchantInvisibleGear(eid);
		eid = Utilities.getNextEnchantmentId(eid);
		if(eid >= 0)
			enc_unveiling = new WGEnchantUnveiling(eid);
		eid = Utilities.getNextEnchantmentId(eid);
		if(eid >= 0)
			enc_stealth = new WGEnchantStealth(eid);
		eid = Utilities.getNextEnchantmentId(eid);
		if(eid >= 0)
			enc_backstab = new WGEnchantBackstab(eid);
		eid = Utilities.getNextEnchantmentId(eid);
		if(eid >= 0)
			enc_rideProtect = new WGEnchantRideProtect(eid);
	}
	public static void postInit()
	{
		postInitItems();
		postInitBlocks();
		postInitThaumcraft();
	}

	private static void preInitBlocks()
	{
		BlockWallMirror = new BlockWallMirror().setBlockName("WG_WallMirror");
		GameRegistry.registerBlock(BlockWallMirror, BlockWallMirror.getLocalizedName());

		BlockVoidWalkway = new BlockVoidWalkway().setBlockName("WG_VoidWalkway");
		GameRegistry.registerBlock(BlockVoidWalkway, BlockVoidWalkway.getLocalizedName());

		BlockStoneDevice = new BlockWGStoneDevice().setBlockName("WG_StoneDevice");
		GameRegistry.registerBlock(BlockStoneDevice, ItemBlockStoneDevice.class, BlockStoneDevice.getLocalizedName());

		BlockWoodenDevice = new BlockWGWoodenDevice().setBlockName("WG_WoodenDevice");
		GameRegistry.registerBlock(BlockWoodenDevice, ItemBlockWoodenDevice.class, BlockWoodenDevice.getLocalizedName());

		BlockMetalDevice = new BlockWGMetalDevice().setBlockName("WG_MetalDevice");
		GameRegistry.registerBlock(BlockMetalDevice, ItemBlockMetalDevice.class, BlockMetalDevice.getLocalizedName());

		//BlockMagicBed = new BlockMagicBed(WGConfig.BlockMagicBedID).setUnlocalizedName("WG_MagicBed");
		//GameRegistry.registerBlock(BlockMagicBed, BlockMagicBed.getLocalizedName());

		BlockRoseVine = new BlockRoseVines().setBlockName("WG_RoseVine");
		GameRegistry.registerBlock(BlockRoseVine, BlockRoseVine.getLocalizedName());

		BlockCustomAiry = new BlockModifiedAiry().setBlockName("WG_CustomAir");
		GameRegistry.registerBlock(BlockCustomAiry, BlockCustomAiry.getLocalizedName());

		OreDictionary.registerOre("blockVoid", new ItemStack(BlockMetalDevice,1,7));
	}
	private static void initializeBlocks()
	{
		if(Loader.isModLoaded("ForgeMultipart"))
			WGMultiPartHandler.instance.init();

		//UNIQUE
		registerTile(TileEntityWallMirror.class);
		registerTile(TileEntityVoidWalkway.class);
		registerTile(TileEntityTempLight.class);
		//STONE
		registerTile(TileEntityEtherealWall.class);
		registerTile(TileEntityMagicalTileLock.class);
		registerTile(TileEntitySarcophagus.class);
		registerTile(TileEntityAgeingStone.class);
		registerTile(TileEntityBlastfurnace.class);
		//WOODEN
		registerTile(TileEntitySpinningWheel.class);
		registerTile(TileEntitySnowGen.class);
		registerTile(TileEntityCobbleGen.class);
		registerTile(TileEntityCuttingTable.class);
		registerTile(TileEntitySaunaStove.class);
		registerTile(TileEntityLabelLibrary.class);
		//METAL
		registerTile(TileEntityEssentiaPump.class);
		registerTile(TileEntityTerraformer.class);
		registerTile(TileEntityTerraformFocus.class);

		//GameRegistry.registerTileEntity(TileEntityTotem.class, "TileEntityTotem");
		//GameRegistry.registerTileEntity(TileEntityEssentiaVapourizer.class, "TileEntityEssentiaVapourizer");
	}
	private static void registerTile(Class<? extends TileEntity> c)
	{
		GameRegistry.registerTileEntity(c, "WitchingGadgets_"+c.getCanonicalName().substring(c.getCanonicalName().lastIndexOf(".")));
	}
	private static void postInitBlocks()
	{
		boolean rc = WGModCompat.railcraftAllowBlastFurnace();
		for(int yy=0;yy<=1;yy++)
			for(int zz=0;zz<=2;zz++)
				for(int xx=0;xx<=2;xx++)
				{
					int pos = yy*9 + zz*3 + xx;
					if(rc)
						TileEntityBlastfurnace.brickBlock[pos] = GameRegistry.findBlock("Railcraft","brick.infernal");
					else			
						TileEntityBlastfurnace.brickBlock[pos] = pos<9&&pos!=4?Blocks.nether_brick: pos==10||pos==12||pos==13||pos==14||pos==16?Blocks.soul_sand: Blocks.obsidian;
				}

		TileEntityBlastfurnace.stairBlock = rc? GameRegistry.findBlock("Railcraft", "stair"): Blocks.nether_brick_stairs;
	}

	private static void preInitItems()
	{
		ItemMaterial = new ItemMaterials().setUnlocalizedName("WG_Material");
		GameRegistry.registerItem(ItemMaterial, ItemMaterial.getUnlocalizedName());

		ItemBag = new ItemBag().setUnlocalizedName("WG_Bag");
		GameRegistry.registerItem(ItemBag, ItemBag.getUnlocalizedName());

		ItemThaumiumShears = new ItemThaumiumShears().setUnlocalizedName("WG_ThaumiumShears");
		GameRegistry.registerItem(ItemThaumiumShears, ItemThaumiumShears.getUnlocalizedName());

		ItemAdvancedRobeChest = new ItemAdvancedRobes(armorMatSpecialRobe,2,1).setUnlocalizedName("WG_AdvancedRobeChest");
		GameRegistry.registerItem(ItemAdvancedRobeChest, ItemAdvancedRobeChest.getUnlocalizedName());
		ItemAdvancedRobeLegs = new ItemAdvancedRobes(armorMatSpecialRobe,2,2).setUnlocalizedName("WG_AdvancedRobeLegs");
		GameRegistry.registerItem(ItemAdvancedRobeLegs, ItemAdvancedRobeLegs.getUnlocalizedName());

		ItemMagicFoodstuffs = new ItemMagicFood().setUnlocalizedName("WG_MagicFood");
		GameRegistry.registerItem(ItemMagicFoodstuffs, ItemMagicFoodstuffs.getUnlocalizedName());

		ItemCloak = (ItemCloak) new ItemCloak().setUnlocalizedName("WG_Cloak");
		GameRegistry.registerItem(ItemCloak, ItemCloak.getUnlocalizedName());
		ItemKama = (ItemKama) new ItemKama().setUnlocalizedName("WG_Kama");
		GameRegistry.registerItem(ItemKama, ItemKama.getUnlocalizedName());

		ItemInfusedGem = new ItemInfusedGem().setUnlocalizedName("WG_InfusedGem");
		GameRegistry.registerItem(ItemInfusedGem, ItemInfusedGem.getUnlocalizedName());

		ItemMagicalBaubles = new ItemMagicalBaubles().setUnlocalizedName("WG_Baubles");
		GameRegistry.registerItem(ItemMagicalBaubles, ItemMagicalBaubles.getUnlocalizedName());

		ItemScanCamera = new ItemScanCamera().setUnlocalizedName("WG_ScanCamera");
		GameRegistry.registerItem(ItemScanCamera, ItemScanCamera.getUnlocalizedName());

		ItemPrimordialGlove = new ItemPrimordialGlove().setUnlocalizedName("WG_PrimordialGlove");
		GameRegistry.registerItem(ItemPrimordialGlove, ItemPrimordialGlove.getUnlocalizedName());
		ItemPrimordialHammer = new ItemPrimordialHammer(primordialTool).setUnlocalizedName("WG_PrimordialHammer");
		GameRegistry.registerItem(ItemPrimordialHammer, ItemPrimordialHammer.getUnlocalizedName());
		ItemPrimordialAxe = new ItemPrimordialAxe(primordialTool).setUnlocalizedName("WG_PrimordialAxe");
		GameRegistry.registerItem(ItemPrimordialAxe, ItemPrimordialAxe.getUnlocalizedName());
		ItemPrimordialSword = new ItemPrimordialSword(primordialTool).setUnlocalizedName("WG_PrimordialSword");
		GameRegistry.registerItem(ItemPrimordialSword, ItemPrimordialSword.getUnlocalizedName());

		ItemPrimordialHelm = new ItemPrimordialArmor(primordialArmor, 4, 0).setUnlocalizedName("WG_PrimordialHelm");
		GameRegistry.registerItem(ItemPrimordialHelm, ItemPrimordialHelm.getUnlocalizedName());
		ItemPrimordialChest = new ItemPrimordialArmor(primordialArmor, 4, 1).setUnlocalizedName("WG_PrimordialChest");
		GameRegistry.registerItem(ItemPrimordialChest, ItemPrimordialChest.getUnlocalizedName());
		ItemPrimordialLegs = new ItemPrimordialArmor(primordialArmor, 4, 2).setUnlocalizedName("WG_PrimordialLegs");
		GameRegistry.registerItem(ItemPrimordialLegs, ItemPrimordialLegs.getUnlocalizedName());
		ItemPrimordialBoots = new ItemPrimordialArmor(primordialArmor, 4, 3).setUnlocalizedName("WG_PrimordialBoots");
		GameRegistry.registerItem(ItemPrimordialBoots, ItemPrimordialBoots.getUnlocalizedName());

		ItemCrystalCapsule = new ItemCrystalCapsule().setUnlocalizedName("WG_CrystalFlask");
		GameRegistry.registerItem(ItemCrystalCapsule, ItemCrystalCapsule.getUnlocalizedName());
		if(WGConfig.allowClusters)
		{
			ItemCluster = new ItemClusters().setUnlocalizedName("WG_Cluster");
			GameRegistry.registerItem(ItemCluster, ItemCluster.getUnlocalizedName());
		}
		//ItemMagicBed = new ItemMagicBed(WGConfig.ItemMagicBedID).setUnlocalizedName("WG_MagicBed");
		//GameRegistry.registerItem(ItemMagicBed, ItemMagicBed.getUnlocalizedName());
		OreDictionary.registerOre("crystalNetherQuartz", new ItemStack(Items.quartz));
		OreDictionary.registerOre("scribingTools", new ItemStack(ConfigItems.itemInkwell,1,OreDictionary.WILDCARD_VALUE));

	}
	private static void initializeItems()
	{
		WGResearch.recipeList.put("THAUMIUMSHEARS", GameRegistry.addShapedRecipe(new ItemStack(ItemThaumiumShears), " t", "t ", 't', ItemApi.getItem("itemResource", 2)));

		BlockDispenser.dispenseBehaviorRegistry.putObject(ItemCrystalCapsule, new ItemCrystalCapsule.CapsuleDispenserBehaviour());	    

		GameRegistry.addRecipe(new RobeColourizationRecipe());
		GameRegistry.addRecipe(new CloakColourizationRecipe());
		GameRegistry.addRecipe(new BagColourizationRecipe());
		RecipeSorter.register("WitchingGadgets:advrobedye", RobeColourizationRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
		RecipeSorter.register("WitchingGadgets:cloakdye", CloakColourizationRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
		RecipeSorter.register("WitchingGadgets:bagdye", BagColourizationRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockMetalDevice,1,7), "vvv","vvv","vvv", 'v',"ingotVoid"));

		GameRegistry.addShapelessRecipe(new ItemStack(ItemMagicFoodstuffs,1,0), Items.nether_wart,Items.sugar);
		GameRegistry.addShapedRecipe(new ItemStack(ItemMagicFoodstuffs,1,1), "nnn","www", 'n',new ItemStack(ItemMagicFoodstuffs,1,0), 'w', Items.wheat);

		EntityRegistry.registerModEntity(EntityItemReforming.class, "reformingItem", 0, WitchingGadgets.instance, 64, 1, true);

		if(WGConfig.allowClusters)
		{
			for(int iOre=0; iOre<ItemClusters.subNames.length; iOre++)
				OreDictionary.registerOre("cluster"+ItemClusters.subNames[iOre], new ItemStack(ItemCluster,1,iOre));
		}

		//FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", new ItemStack(ItemCloak));
	}
	private static void postInitItems()
	{
		ChestGenHooks.getInfo("towerChestContents").addItem(new WeightedRandomChestContent(new ItemStack(ItemMaterial,1,8),1,1,8));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(ItemMaterial,1,8),1,1,8));

		ItemStack emptyCapsule = new ItemStack(ItemCrystalCapsule);
		for(Map.Entry<String,Fluid> f : FluidRegistry.getRegisteredFluids().entrySet())
		{
			ItemStack filledCapsule = new ItemStack(ItemCrystalCapsule);
			filledCapsule.setTagCompound(new NBTTagCompound());
			filledCapsule.getTagCompound().setString("fluid", f.getKey());
			FluidContainerRegistry.registerFluidContainer(new FluidStack(f.getValue(),1000), filledCapsule, emptyCapsule);
		}

		InfernalBlastfurnaceRecipe.tryAddIngotImprovement("Iron", "Steel", false);
		InfernalBlastfurnaceRecipe.tryAddSpecialOreMelting("Tungsten","Tungsten",true);
		InfernalBlastfurnaceRecipe.tryAddSpecialOreMelting("Rutile","Titanium",true);
	}


	private static void postInitThaumcraft()
	{
		//Add aspects where needed
		AspectList addAspects = new AspectList().add(Aspect.TREE, 4).add(Aspect.CLOTH, 2).add(Aspect.MECHANISM, 2).add(Aspect.AIR, 2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockWoodenDevice,1,1),addAspects);
		addAspects = new AspectList().add(Aspect.TREE, 2).add(Aspect.CRYSTAL, 2).add(Aspect.CRAFT, 2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockWoodenDevice,1,3),addAspects);

		addAspects = new AspectList().add(Aspect.MECHANISM,1).add(Aspect.EARTH,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,0),addAspects);
		addAspects = new AspectList().add(Aspect.MECHANISM,3).add(Aspect.ELDRITCH,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,1),addAspects);
		addAspects = new AspectList().add(Aspect.EARTH,1).add(Aspect.ELDRITCH,2).add(Aspect.DARKNESS,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,2),addAspects);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,3),addAspects);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,4),addAspects);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,5),addAspects);
		addAspects = new AspectList().add(Aspect.VOID,2).add(Aspect.ELDRITCH,1).add(Aspect.DARKNESS,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,6),addAspects);

		addAspects = new AspectList().add(Aspect.PLANT,6).add(Aspect.ENTROPY,4).add(Aspect.MAGIC,4).add(Aspect.LIFE, 2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockRoseVine,1, 32767),addAspects);

		addAspects = new AspectList().add(Aspect.CLOTH,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,0),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,1).add(Aspect.GREED,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,1),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,1).add(Aspect.METAL,1).add(Aspect.MAGIC,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,2),addAspects);

		addAspects = new AspectList().add(Aspect.CLOTH,3).add(Aspect.VOID,3);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,3),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,3).add(Aspect.GREED,3);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,4),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,3).add(Aspect.MAGIC,2).add(Aspect.TAINT,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,5),addAspects);

		addAspects = new AspectList().add(Aspect.CLOTH,2).add(Aspect.BEAST,3);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,6),addAspects);

		addAspects = new AspectList().add(Aspect.CRYSTAL,5).add(Aspect.SENSES,3).add(Aspect.EXCHANGE,2).add(Aspect.POISON,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,9),addAspects);
		addAspects = new AspectList().add(Aspect.AIR,2).add(Aspect.WATER,2).add(Aspect.ORDER,2).add(Aspect.SENSES,2).add(Aspect.MIND,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,10),addAspects);

		addAspects = new AspectList().add(Aspect.CRYSTAL,4).add(Aspect.VOID,4);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,12),addAspects);

		addAspects = new AspectList().add(Aspect.FIRE,1).add(Aspect.HUNGER,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMagicFoodstuffs,1,0),addAspects);
		addAspects = new AspectList().add(Aspect.MIND,3).add(Aspect.HUNGER,2).add(Aspect.FLESH,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMagicFoodstuffs,1,2),addAspects);

//		//Biomes o' Plenty
//		ThaumcraftApi.registerObjectTag("gemAmber", new AspectList().add(Aspect.TRAP, 2).add(Aspect.CRYSTAL, 2));
//		ThaumcraftApi.registerObjectTag("gemPeridot", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.GREED, 2));
//		ThaumcraftApi.registerObjectTag("gemTopaz", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.GREED, 2));
//		ThaumcraftApi.registerObjectTag("gemTanzanite", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.GREED, 2));
//		ThaumcraftApi.registerObjectTag("gemMalachite", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.GREED, 2));
//
//		//Botania
//		addOreAspects("Manasteel", new AspectList().add(Aspect.MAGIC, 1), false);
//		addOreAspects("Terrasteel", new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1), false);
//		addOreAspects("ElvenElementium", new AspectList().add(Aspect.AURA, 1).add(Aspect.MAGIC, 2), true);
//		ThaumcraftApi.registerObjectTag("livingstone", new AspectList().add(Aspect.EARTH,4).add(Aspect.LIFE,2));
//		ThaumcraftApi.registerObjectTag("livingwood", new AspectList().add(Aspect.TREE,4).add(Aspect.LIFE,2));
//		ThaumcraftApi.registerObjectTag("dreamwood", new AspectList().add(Aspect.MAGIC,1).add(Aspect.TREE,4).add(Aspect.AURA,2));
//		ThaumcraftApi.registerObjectTag("manaPearl", new AspectList().add(Aspect.MAGIC,6).add(Aspect.ELDRITCH,4).add(Aspect.TRAVEL,4));
//		ThaumcraftApi.registerObjectTag("manaDiamond", new AspectList().add(Aspect.MAGIC,4).add(Aspect.CRYSTAL,4).add(Aspect.GREED,4));
//		ThaumcraftApi.registerObjectTag("eternalLifeEssence", new AspectList().add(Aspect.MAGIC,4).add(Aspect.AURA,4));
//		ThaumcraftApi.registerObjectTag("eternalLifeEssence", new AspectList().add(Aspect.MAGIC,8).add(Aspect.AURA,8).add(Aspect.LIFE,8));
//		ThaumcraftApi.registerObjectTag("elvenPixieDust", new AspectList().add(Aspect.MAGIC,6).add(Aspect.AURA,4).add(Aspect.ELDRITCH,4));
//		ThaumcraftApi.registerObjectTag("elvenDragonstone", new AspectList().add(Aspect.MAGIC,6).add(Aspect.AURA,4).add(Aspect.CRYSTAL,6));
//		ThaumcraftApi.registerObjectTag("shardPrismarine", new AspectList().add(Aspect.MAGIC,2).add(Aspect.WATER,4).add(Aspect.CRYSTAL,2));
//		String[] colours = {"White","Orange","Magenta","LightBlue","Yellow","Lime","Pink","Gray",
//				"LightGray","Cyan","Purple","Blue","Brown","Green","Red","Black"};
//		for(String c : colours)
//		{
//			ThaumcraftApi.registerObjectTag("mysticFlower"+c, new AspectList().add(Aspect.SENSES,2).add(Aspect.PLANT,2));
//			ThaumcraftApi.registerObjectTag("petal"+c, new AspectList().add(Aspect.SENSES,1).add(Aspect.PLANT,1));
//			ThaumcraftApi.registerObjectTag("manaPetal"+c, new AspectList().add(Aspect.MAGIC,1).add(Aspect.SENSES,1).add(Aspect.PLANT,1));
//		}
//		ThaumcraftApi.registerObjectTag("rune"+"Water"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.WATER,6));
//		ThaumcraftApi.registerObjectTag("rune"+"Fire"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.FIRE,6));
//		ThaumcraftApi.registerObjectTag("rune"+"Earth"+"B", new AspectList().add(Aspect.EARTH,8).add(Aspect.METAL,2));
//		ThaumcraftApi.registerObjectTag("rune"+"Air"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.AIR,6));
//		ThaumcraftApi.registerObjectTag("rune"+"Spring"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.WATER,3).add(Aspect.FIRE,3).add(Aspect.PLANT, 3));
//		ThaumcraftApi.registerObjectTag("rune"+"Summer"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.EARTH,3).add(Aspect.AIR,3).add(Aspect.ENTROPY, 3));
//		ThaumcraftApi.registerObjectTag("rune"+"Autumn"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.FIRE,3).add(Aspect.AIR,3).add(Aspect.PLANT, 3));
//		ThaumcraftApi.registerObjectTag("rune"+"Winter"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.WATER,3).add(Aspect.EARTH,3).add(Aspect.COLD, 3));
//		ThaumcraftApi.registerObjectTag("rune"+"Mana"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,8).add(Aspect.MAGIC,6));
//
//		ThaumcraftApi.registerObjectTag("rune"+"Lust"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.AIR,6).add(Aspect.EARTH,3).add(Aspect.getAspect("luxuria"),4));
//		ThaumcraftApi.registerObjectTag("rune"+"Gluttony"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.FIRE,3).add(Aspect.EARTH,3).add(Aspect.WATER,3).add(Aspect.getAspect("gula"),4));
//		ThaumcraftApi.registerObjectTag("rune"+"Greed"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.WATER,6).add(Aspect.FIRE,3).add(Aspect.GREED,4));
//		ThaumcraftApi.registerObjectTag("rune"+"Sloth"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.AIR,6).add(Aspect.FIRE,3).add(Aspect.getAspect("desidia"),4));
//		ThaumcraftApi.registerObjectTag("rune"+"Wrath"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.EARTH,6).add(Aspect.WATER,3).add(Aspect.getAspect("ira"),4));
//		ThaumcraftApi.registerObjectTag("rune"+"Envy"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.WATER,6).add(Aspect.EARTH,3).add(Aspect.getAspect("invidia"),4));
//		ThaumcraftApi.registerObjectTag("rune"+"Pride"+"B", new AspectList().add(Aspect.EARTH,2).add(Aspect.METAL,2).add(Aspect.CRYSTAL,4).add(Aspect.EARTH,3).add(Aspect.AIR,3).add(Aspect.FIRE,3).add(Aspect.getAspect("superbia"),4));
//
//		//Tcon
//		addOreAspects("Aluminum", new AspectList().add(Aspect.AIR, 1), false);
//		addOreAspects("Aluminium", new AspectList().add(Aspect.AIR, 1), false);
//		addOreAspects("AluminumBrass", new AspectList().add(Aspect.CRAFT, 1), false);
//		addOreAspects("AluminiumBrass", new AspectList().add(Aspect.CRAFT, 1), false);
//		addOreAspects("Alumite", new AspectList().add(Aspect.TOOL, 1).add(Aspect.AIR, 1), false);
//		addOreAspects("Cobalt", new AspectList().add(Aspect.MOTION, 1).add(Aspect.FIRE, 1), true);
//		addOreAspects("Ardite", new AspectList().add(Aspect.EARTH, 1).add(Aspect.FIRE, 1), true);
//		addOreAspects("Manyullyn", new AspectList().add(Aspect.MAGIC, 2).add(Aspect.FIRE, 1), true);
//		addOreAspects("PigIron", new AspectList().add(Aspect.FLESH, 1), false);
//
//		//Metallurgy
//		addOreAspects("Hepatizon", new AspectList().add(Aspect.GREED, 1).add(Aspect.TOOL, 1), false);
//		addOreAspects("DamascusSteel", new AspectList().add(Aspect.ORDER, 1).add(Aspect.TOOL, 1), false);
//		addOreAspects("Angmallen", new AspectList().add(Aspect.GREED, 1), false);
//		addOreAspects("Manganese", new AspectList().add(Aspect.ORDER, 1), false);
//
//		addOreAspects("Zinc", new AspectList().add(Aspect.ORDER, 1), false);
//		addOreAspects("Brass", new AspectList().add(Aspect.CRAFT, 1), false);
//		addOreAspects("Electrum", new AspectList().add(Aspect.ENERGY, 1), false);
//		addOreAspects("Platinum", new AspectList().add(Aspect.GREED, 1), false);
//
//		addOreAspects("Ignatius", new AspectList().add(Aspect.FIRE, 2), false);
//		addOreAspects("ShadowIron", new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("Lemurite", new AspectList().add(Aspect.ORDER, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("ShadowSteel", new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.ORDER, 1).add(Aspect.FIRE, 1), true);
//		addOreAspects("Midasium", new AspectList().add(Aspect.GREED, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("Vyroxeres", new AspectList().add(Aspect.POISON, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("Ceruclase", new AspectList().add(Aspect.WATER, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("Alduorite", new AspectList().add(Aspect.ORDER, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("Inolashite", new AspectList().add(Aspect.COLD, 2).add(Aspect.FIRE, 1), true);
//		addOreAspects("Kalendrite", new AspectList().add(Aspect.SOUL, 1).add(Aspect.FIRE, 1), false);
//		addOreAspects("Amordrine", new AspectList().add(Aspect.SOUL, 1).add(Aspect.GREED, 1).add(Aspect.FIRE, 1), true);
//		addOreAspects("Vulcanite", new AspectList().add(Aspect.FIRE, 2), false);
//		addOreAspects("Sanguinite", new AspectList().add(Aspect.HUNGER, 1).add(Aspect.FIRE, 1), false);
//
//		addOreAspects("Prometheum", new AspectList().add(Aspect.EARTH, 1), false);
//		addOreAspects("DeepIron", new AspectList().add(Aspect.ENTROPY, 1), false);
//		addOreAspects("Infuscolium", new AspectList().add(Aspect.ENERGY, 1), false);
//		addOreAspects("BlackSteel", new AspectList().add(Aspect.ENTROPY, 1).add(Aspect.ENERGY, 1), true);
//		addOreAspects("Oureclase", new AspectList().add(Aspect.ENERGY, 1), false);
//		addOreAspects("AstralSilver", new AspectList().add(Aspect.GREED, 1), false);
//		addOreAspects("Carmot", new AspectList().add(Aspect.GREED, 2), false);
//		addOreAspects("Mithril", new AspectList().add(Aspect.MOTION, 1).add(Aspect.MAGIC, 1), false);
//		addOreAspects("Rubracium", new AspectList().add(Aspect.VOID, 1), false);
//		addOreAspects("Quicksilver", new AspectList().add(Aspect.VOID, 1).add(Aspect.MOTION, 1), true);
//		addOreAspects("Haderoth", new AspectList().add(Aspect.MOTION, 1).add(Aspect.GREED, 1), true);
//		addOreAspects("Orichalcum", new AspectList().add(Aspect.LIFE, 1), false);
//		addOreAspects("Celenegil", new AspectList().add(Aspect.LIFE, 1).add(Aspect.GREED, 1), true);
//		addOreAspects("Adamantine", new AspectList().add(Aspect.MIND, 1), false);
//		addOreAspects("Atlarus", new AspectList().add(Aspect.FLESH, 1), false);
//		addOreAspects("Tartarite", new AspectList().add(Aspect.FLESH, 1).add(Aspect.HUNGER, 1), false);
//
//		addOreAspects("Eximite", new AspectList().add(Aspect.ELDRITCH, 2), true);
//		addOreAspects("Meutoite", new AspectList().add(Aspect.VOID, 2), true);
//		addOreAspects("Desichalkos", new AspectList().add(Aspect.ELDRITCH, 2).add(Aspect.VOID, 2), true);
		
		WGResearch.setupResearchPages();
		WGResearch.registerRecipes();
		WGResearch.registerResearch();
		WGResearch.modifyStandardThaumcraftResearch();
	}

	static void addOreAspects(String ore, AspectList aspects, boolean isRareOre)
	{
		if(!OreDictionary.getOres("ore"+ore).isEmpty() && !oreHasAspects("ore"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?3:2), (isRareOre?4:3)-aspects.visSize())).add(Aspect.EARTH, 1);
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, 1);

			ThaumcraftApi.registerObjectTag("ore"+ore, al);
		}
		if(!OreDictionary.getOres("ingot"+ore).isEmpty() && !oreHasAspects("ingot"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?3:2), (isRareOre?5:4)-aspects.visSize()));
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, aspects.getAmount(aa));
			ThaumcraftApi.registerObjectTag("ingot"+ore, al);
		}
		if(!OreDictionary.getOres("nugget"+ore).isEmpty() && !oreHasAspects("nugget"+ore))
			ThaumcraftApi.registerObjectTag("nugget"+ore, new AspectList().add(Aspect.METAL, 1));
		if(!OreDictionary.getOres("dust"+ore).isEmpty() && !oreHasAspects("dust"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?3:2), (isRareOre?4:3)-aspects.visSize())).add(Aspect.ENTROPY, 1);
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, 1);
			ThaumcraftApi.registerObjectTag("dust"+ore, al);
		}
		if(!OreDictionary.getOres("block"+ore).isEmpty() && !oreHasAspects("block"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?6:5), (isRareOre?7:6)-aspects.visSize())).add(Aspect.ENTROPY, 1);
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, 1);
			ThaumcraftApi.registerObjectTag("block"+ore, al);
		}
	}
	static boolean oreHasAspects(String ore)
	{
		for(ItemStack stack : OreDictionary.getOres(ore))
			if(stack!=null)
				return ThaumcraftApi.objectTags.get(Arrays.asList(new Object[] { stack.getItem(), Integer.valueOf(stack.getItemDamage()) }))!=null;
		return false;
	}
}