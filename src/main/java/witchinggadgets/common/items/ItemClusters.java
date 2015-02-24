package witchinggadgets.common.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.WGContent;

public class ItemClusters extends Item
{
	public static String[] subNames = {
		//TCon
		"Aluminum", "Cobalt", "Ardite",
		//ThermalFoundation
		"Nickel",
		//Factorization
		"FZDarkIron", 
		//Metallurgy 
		"Manganese",
		"Zinc","Platinum",
		"Ignatius","ShadowIron","Lemurite","Midasium","Vyroxeres","Ceruclase","Alduorite","Kalendrite","Vulcanite","Sanguinite",
		"Prometheum","DeepIron","Infuscolium","Oureclase","AstralSilver","Carmot","Mithril","Rubracium","Orichalcum","Adamantine","Atlarus",
		"Eximite","Meutoite"


	};
	static HashMap<String, Integer[]> materialMap = new HashMap();

	IIcon iconMetal;
	IIcon[] iconOverlay = new IIcon[3];

	public ItemClusters()
	{
		super();
		maxStackSize = 64;
		setCreativeTab(WitchingGadgets.tabWG);
		setHasSubtypes(true);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass==0)
		{
			//System.out.println(subNames[stack.getItemDamage()]+" "+materialMap.get( subNames[stack.getItemDamage()] )[0]);
			if(materialMap.get( subNames[stack.getItemDamage()])!=null)
				return materialMap.get( subNames[stack.getItemDamage()] )[0];
			//			Color col = new Color(  );
			//			return col.getRGB();
		}
		return 0xffffff;
	}

	public static ItemStack getCluster(String ore)
	{
		for(int sn=0; sn<subNames.length; sn++)
			if(subNames[sn].equalsIgnoreCase(ore))
				return new ItemStack(WGContent.ItemCluster,1,sn);
		return null;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.iconMetal = iconRegister.registerIcon("witchinggadgets:cluster_metal");
		this.iconOverlay[0] = iconRegister.registerIcon("witchinggadgets:cluster_overlay");
		this.iconOverlay[1] = iconRegister.registerIcon("witchinggadgets:cluster_overlayNether");
		this.iconOverlay[2] = iconRegister.registerIcon("witchinggadgets:cluster_overlayEnd");
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass)
	{
		if(pass==0)
			return this.iconMetal;
		else if(materialMap.get(subNames[damage])!=null)
			return this.iconOverlay[ materialMap.get(subNames[damage])[1] ];
		else
			return this.iconOverlay[0];
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String ss = !OreDictionary.getOres("ingot"+subNames[stack.getItemDamage()]).isEmpty()? OreDictionary.getOres("ingot"+subNames[stack.getItemDamage()]).get(0).getDisplayName().substring(0, OreDictionary.getOres("ingot"+subNames[stack.getItemDamage()]).get(0).getDisplayName().lastIndexOf(" "))+" "  :"";
		return ss + super.getItemStackDisplayName(stack);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int iOre=0; iOre<subNames.length; iOre++)
			if(!OreDictionary.getOres("ore"+subNames[iOre]).isEmpty() && !OreDictionary.getOres("ingot"+subNames[iOre]).isEmpty())
				itemList.add( new ItemStack(item,1,iOre) );
	}

	public static void setupClusters()
	{
		for(String ore : subNames)
			if(!OreDictionary.getOres("ore"+ore).isEmpty() && !OreDictionary.getOres("ingot"+ore).isEmpty())
			{
				List<Integer> colList = ClientUtilities.getItemColours( OreDictionary.getOres("ore"+ore).get(0) );
				if(!colList.isEmpty())
				{
					int oreBlockColour = colList.get(0);
					int[] rgb = {oreBlockColour>>16&0xff, oreBlockColour>>8&0xff, oreBlockColour&0xff};
					int clustertype = rgb[0]>rgb[2]&&rgb[1]>rgb[2]?2 :rgb[0]>rgb[1]&&rgb[0]>rgb[2]?1 : 0;
					List<Integer> colours = ClientUtilities.getItemColours(OreDictionary.getOres("ingot"+ore).get(0));

					int colour = ClientUtilities.getVibrantColourToInt(colours.get((int)(colours.size()*.65)));
					colour = ClientUtilities.getVibrantColourToInt(colour);

					materialMap.put(ore, new Integer[]{colour, clustertype} );
					//WitchingGadgets.logger.log(Level.INFO, "Initialized Cluster "+ore+": type: "+clustertype+", colour: "+Integer.toHexString(colour));
				}
			}
	}
}	