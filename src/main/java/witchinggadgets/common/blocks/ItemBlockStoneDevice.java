package witchinggadgets.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockStoneDevice extends ItemBlock
{
	public ItemBlockStoneDevice(Block b)
	{
		super(b);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata (int damageValue)
	{
		return damageValue;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		itemList.add(new ItemStack(item,1,0));
		itemList.add(new ItemStack(item,1,1));
		itemList.add(new ItemStack(item,1,2));
		itemList.add(new ItemStack(item,1,3));
		itemList.add(new ItemStack(item,1,6));
		itemList.add(new ItemStack(item,1,7));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName()+"."+BlockWGStoneDevice.subNames[itemstack.getItemDamage()];
	}
}
