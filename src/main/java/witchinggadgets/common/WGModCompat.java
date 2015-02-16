package witchinggadgets.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import witchinggadgets.common.minetweaker.WGMinetweaker;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class WGModCompat
{
	public static Item tConResource;

	public static Item tfRavensFeather;
	public static Item tfMagicMapFocus;
	public static Block tfTowerWood;

	public static boolean loaded_TCon;
	public static boolean loaded_Twilight;
	public static boolean loaded_Enviromine;
	public static boolean loaded_Railcraft;
	public static boolean loaded_TT;
	
	public static void init()
	{
		//Twilight Forest
		tfRavensFeather = GameRegistry.findItem("TwilightForest", "item.tfFeather");
		tfMagicMapFocus = GameRegistry.findItem("TwilightForest", "item.magicMapFocus");
		tfTowerWood = GameRegistry.findBlock("TwilightForest", "tile.TFTowerStone");
		tConResource = GameRegistry.findItem("TConstruct", "materials");
		
		loaded_TCon = Loader.isModLoaded("TConstruct");
		loaded_Twilight = Loader.isModLoaded("TwilightForest");
		loaded_Enviromine = Loader.isModLoaded("enviromine");
		loaded_Railcraft = Loader.isModLoaded("Railcraft");
		loaded_TT = Loader.isModLoaded("ThaumicTinkerer");
		
		if(Loader.isModLoaded("MineTweaker3"))
			WGMinetweaker.init();	
	}

	public static void addTags()
	{
		registerOreDictAspects("nuggetAluminum",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotAluminum",new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE,1));
		registerOreDictAspects(  "dustAluminum",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.EXCHANGE,1));
		registerOreDictAspects(   "oreAluminum",new AspectList().add(Aspect.METAL, 2).add(Aspect.EARTH, 1).add(Aspect.EXCHANGE, 1));

		registerOreDictAspects("nuggetAluminium",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotAluminium",new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE,1));
		registerOreDictAspects(  "dustAluminium",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.EXCHANGE,1));
		registerOreDictAspects(   "oreAluminium",new AspectList().add(Aspect.METAL, 2).add(Aspect.EARTH, 1).add(Aspect.EXCHANGE, 1));

		registerOreDictAspects("nuggetAluminumBrass",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotAluminumBrass",new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE,2));
		registerOreDictAspects(  "dustAluminumBrass",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.EXCHANGE,2));
		registerOreDictAspects(   "oreAluminumBrass",new AspectList().add(Aspect.METAL, 2).add(Aspect.EARTH, 1).add(Aspect.EXCHANGE, 2));

		registerOreDictAspects("nuggetAluminiumBrass",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotAluminiumBrass",new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE,2));
		registerOreDictAspects(  "dustAluminiumBrass",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.EXCHANGE,2));
		registerOreDictAspects(   "oreAluminiumBrass",new AspectList().add(Aspect.METAL, 2).add(Aspect.EARTH, 1).add(Aspect.EXCHANGE, 2));

		registerOreDictAspects("nuggetCobalt",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotCobalt",new AspectList().add(Aspect.METAL, 3).add(Aspect.FIRE, 1).add(Aspect.MOTION,1));
		registerOreDictAspects(  "dustCobalt",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.FIRE, 1).add(Aspect.MOTION,1));
		registerOreDictAspects(   "oreCobalt",new AspectList().add(Aspect.METAL, 2).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(Aspect.MOTION,1));

		registerOreDictAspects("nuggetArdite",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotArdite",new AspectList().add(Aspect.METAL, 3).add(Aspect.FIRE, 1).add(Aspect.EARTH,1));
		registerOreDictAspects(  "dustArdite",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.FIRE, 1).add(Aspect.EARTH,1));
		registerOreDictAspects(   "oreArdite",new AspectList().add(Aspect.METAL, 2).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(Aspect.EARTH,1));

		registerOreDictAspects("nuggetManyullyn",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotManyullyn",new AspectList().add(Aspect.METAL, 3).add(Aspect.FIRE, 2).add(Aspect.MAGIC,2));
		registerOreDictAspects(  "dustManyullyn",new AspectList().add(Aspect.METAL, 2).add(Aspect.ENTROPY, 1).add(Aspect.FIRE, 1).add(Aspect.MAGIC,1));

		registerOreDictAspects("nuggetPigIron",new AspectList().add(Aspect.METAL, 1));
		registerOreDictAspects( "ingotPigIron",new AspectList().add(Aspect.METAL, 3).add(Aspect.FLESH, 1));


		if(tConResource != null)
		{
			ThaumcraftApi.registerObjectTag(new ItemStack(tConResource,1,8),new AspectList().add(Aspect.DEATH, 4).add(Aspect.UNDEAD, 2).add(Aspect.HUNGER,2));
			ThaumcraftApi.registerObjectTag(new ItemStack(tConResource,1,8),new AspectList().add(Aspect.DEATH, 4).add(Aspect.UNDEAD, 2).add(Aspect.HUNGER,2));
		}
	}

	private static void registerOreDictAspects(String oreName, AspectList aspects)
	{
		if(!OreDictionary.getOres(oreName).isEmpty())
			ThaumcraftApi.registerObjectTag(oreName,aspects);
	}

	public static void addTConSmelteryRecipe(String oreName, String blockName, int temperature, String fluidName, int fluidAmount)
	{
		if(!OreDictionary.getOres(blockName).isEmpty())
		{
			ItemStack blockStack = OreDictionary.getOres(blockName).get(0);
			if(blockStack==null || Block.getBlockFromItem(blockStack.getItem())==null)
				blockStack = new ItemStack(Blocks.iron_block);
			Block b = Block.getBlockFromItem(blockStack.getItem());

			if(!OreDictionary.getOres(oreName).isEmpty())
				for(ItemStack oreStack:OreDictionary.getOres(oreName))
					if(oreStack!=null)
						addTConSmelteryRecipe(oreStack,b,blockStack.getItemDamage(),temperature,fluidName,fluidAmount);
		}
	}

	static Class smeltery = null;
	static Method addMelting = null;
	public static void addTConSmelteryRecipe(ItemStack ore, Block block, int blockMeta, int temperature, String fluidName, int fluidAmount)
	{
		if(!loaded_TCon)
			return;
		FluidStack fluid = new FluidStack(FluidRegistry.getFluid(fluidName), fluidAmount);

		try{
			if(smeltery==null)
				smeltery = Class.forName("tconstruct.library.crafting.Smeltery");
			if(addMelting==null)
				addMelting = smeltery.getDeclaredMethod("addMelting", ItemStack.class, Block.class, int.class, int.class, FluidStack.class);
			addMelting.invoke(null, ore, block, blockMeta, temperature, fluid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	static Class dryingRack = null;
	static Method addDryingRecipe = null;
	public static void addTConDryingRecipe(Object input, int time, Object output)
	{
		if(!loaded_TCon)
			return;

		try{
			if(dryingRack==null)
				dryingRack = Class.forName("tconstruct.library.crafting.DryingRackRecipes");
			if(addDryingRecipe==null)
				addDryingRecipe = dryingRack.getDeclaredMethod("addDryingRecipe", Object.class, int.class, Object.class);
			addDryingRecipe.invoke(null, input, time, output);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	static Class enviro_DataTracker = null;
	static Method enviro_lookupTracker = null;
	static Field enviro_temperatue = null;
	static Field enviro_hydration = null;
	static Field enviro_sanity = null;
	static Method enviro_dehydrate = null;
	final static float SANITYBUFF = .02f;
	public static void enviromineDoSaunaStuff(EntityLivingBase player, float deh, float temp)
	{
		if(!loaded_Enviromine)
			return;

		try{
			if(enviro_DataTracker==null)
				enviro_DataTracker = Class.forName("enviromine.trackers.EnviroDataTracker");
			if(enviro_lookupTracker==null)
			{
				Class c_EM_StatusManager = Class.forName("enviromine.handlers.EM_StatusManager");
				enviro_lookupTracker = c_EM_StatusManager.getDeclaredMethod("lookupTracker", EntityLivingBase.class);
			}
			if(enviro_temperatue==null)
				enviro_temperatue = enviro_DataTracker.getField("bodyTemp");
			if(enviro_hydration==null)
				enviro_hydration = enviro_DataTracker.getField("hydration");
			if(enviro_sanity==null)
				enviro_sanity = enviro_DataTracker.getField("sanity");
			if(enviro_dehydrate==null)
				enviro_dehydrate = enviro_DataTracker.getMethod("dehydrate",float.class);
			Object tracker = enviro_lookupTracker.invoke(null, player);
			float curTemp = enviro_temperatue.getFloat(tracker);
			if(curTemp+temp<37.5f)
				enviro_temperatue.set(tracker, curTemp+temp);
			float curSane = enviro_sanity.getFloat(tracker);
			if(curSane+SANITYBUFF<=100f)
				enviro_sanity.set(tracker, curSane+SANITYBUFF);

			float curHyd = enviro_hydration.getFloat(tracker);
			enviro_dehydrate.invoke(tracker, curHyd>80?deh:0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	static Method railcraft_isSubBlockEnabled = null;
	public static boolean railcraftAllowBlastFurnace()
	{
		if(!loaded_Railcraft)
			return false;
		try{
			if(railcraft_isSubBlockEnabled==null)
			{

				Class c_RailcraftConfig = Class.forName("mods.railcraft.common.core.RailcraftConfig");
				railcraft_isSubBlockEnabled = c_RailcraftConfig.getMethod("isSubBlockEnabled", String.class);
			}
			return (Boolean) railcraft_isSubBlockEnabled.invoke(null, "machine.alpha.blast.furnace");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	static Method thaumtink_registerExponentialCostData = null;
	public static void thaumicTinkererRegisterEnchantment(Enchantment enchantment, String texture, AspectList aspects, String research)
	{
		if(!loaded_TT)
			return;
		try{
			if(thaumtink_registerExponentialCostData==null)
			{
				Class c_EnchantmentManager = Class.forName("thaumic.tinkerer.common.enchantment.core.EnchantmentManager");
				thaumtink_registerExponentialCostData = c_EnchantmentManager.getMethod("registerExponentialCostData", Enchantment.class,String.class,boolean.class,AspectList.class,String.class);
			}
			thaumtink_registerExponentialCostData.invoke(null, enchantment,texture,false,aspects,research);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}