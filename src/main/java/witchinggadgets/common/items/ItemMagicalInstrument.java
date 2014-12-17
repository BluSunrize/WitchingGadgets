package witchinggadgets.common.items;

import net.minecraft.item.Item;
import witchinggadgets.WitchingGadgets;

public class ItemMagicalInstrument extends Item
{
	public ItemMagicalInstrument()
	{
		maxStackSize = 1;
		setCreativeTab(WitchingGadgets.tabWG);
		setHasSubtypes(true);
	}
	
	
}