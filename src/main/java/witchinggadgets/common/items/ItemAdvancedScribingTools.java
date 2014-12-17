package witchinggadgets.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IScribeTools;
import witchinggadgets.WitchingGadgets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdvancedScribingTools extends Item implements IScribeTools
{
	public ItemAdvancedScribingTools()
	{
		super();
		setMaxStackSize(1);
		this.canRepair = true;
		setMaxDamage(1000);
		setCreativeTab(WitchingGadgets.tabWG);
	}

	@SideOnly(Side.CLIENT)
	public void func_94581_a(IIconRegister ir)
	{
		this.itemIcon = ir.registerIcon("witchinggadgets:scribingTools");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		itemList.add(new ItemStack(item, 1, 0));
	}
}