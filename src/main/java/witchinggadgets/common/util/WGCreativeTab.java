package witchinggadgets.common.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import witchinggadgets.common.WGContent;

public class WGCreativeTab extends CreativeTabs
{
	public WGCreativeTab(int par1, String par2Str)
	{
		super(par1, par2Str);
	}

	@Override
	public Item getTabIconItem()
	{
		return new ItemStack(WGContent.BlockWallMirror).getItem();
	}
}