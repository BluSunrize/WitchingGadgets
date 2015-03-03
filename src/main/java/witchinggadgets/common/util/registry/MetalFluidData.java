package witchinggadgets.common.util.registry;

import java.util.HashMap;

public class MetalFluidData
{
	static HashMap<String, String> oreFluidName = new HashMap();
	static HashMap<String, Integer> oreFluidTemp = new HashMap();

	static{
		addOreFluid("Aluminum", "aluminum.molten", 350);
		addOreFluid("Cobalt", "cobalt.molten", 650);
		addOreFluid("Ardite", "ardite.molten", 650);
		addOreFluid("Nickel", "nickel.molten", 400);
		addOreFluid("FzDarkIron", "fzdarkiron.molten", 600);

		addOreFluid("Manganese", "manganese.molten", 700);

		addOreFluid("Zinc", "zinc.molten", 550);
		addOreFluid("Platinum", "platinum.molten", 550);

		addOreFluid("Ignatius", "ignatius.molten", 550);
		addOreFluid("ShadowIron", "shadow.iron.molten", 550);
		addOreFluid("Lemurite", "lemurite.molten", 550);
		addOreFluid("Midasium", "midasium.molten", 550);
		addOreFluid("Vyroxeres", "vyroxeres.molten", 550);
		addOreFluid("Ceruclase", "ceruclase.molten", 550);
		addOreFluid("Alduorite", "alduorite.molten", 550);
		addOreFluid("Kalendrite", "kalendrite.molten", 550);
		addOreFluid("Vulcanite", "vulcanite.molten", 550);
		addOreFluid("Sanguinite", "sanguinite.molten", 550);

		addOreFluid("Prometheum", "prometheum.molten", 550);
		addOreFluid("DeepIron", "deep.iron.molten", 550);
		addOreFluid("Infuscolium", "infuscolium.molten", 550);
		addOreFluid("Oureclase", "oureclase.molten", 550);
		addOreFluid("AstralSilver", "astral.silver.molten", 550);
		addOreFluid("Carmot", "carmot.molten", 550);
		addOreFluid("Mithril", "mithril.molten", 550);
		addOreFluid("Rubracium", "rubracium.molten", 550);
		addOreFluid("Orichalcum", "orichalcum.molten", 550);
		addOreFluid("Adamantine", "adamantine.molten", 550);
		addOreFluid("Atlarus", "atlarus.molten", 550);

		addOreFluid("Eximite", "eximite.molten", 600);
		addOreFluid("Meutoite", "meutoite.molten", 600);
	}

	public static int getOreFluidTemp(String ore)
	{
		if(oreFluidTemp.get(ore) != null)
			return oreFluidTemp.get(ore);
		return 0;
	}
	public static String getOreFluidName(String ore)
	{
		if(oreFluidTemp.get(ore) != null)
			return oreFluidName.get(ore);
		return "";
	}
	public static void addOreFluid(String ore, String fluidName, int temp)
	{
		oreFluidName.put(ore, fluidName);
		oreFluidTemp.put(ore, temp);
	}
}