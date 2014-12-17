package witchinggadgets.common.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.util.Lib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemVoidBag extends ItemBag
{
	@Override
	public int getColorFromItemStack(ItemStack stack, int par2)
	{
		return 0xffffff;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.openGui(WitchingGadgets.instance, 11, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
		return stack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		list.add(EnumChatFormatting.DARK_PURPLE+StatCollector.translateToLocal(Lib.DESCRIPTION+"filteredItems"));
		for(ItemStack stack : this.getStoredItems(item))
			if(stack!=null)
				list.add(EnumChatFormatting.DARK_GRAY+" "+stack.getDisplayName());
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:voidBag");
	}
	@Override
	public EnumRarity getRarity(ItemStack itemstack)
	{
		return EnumRarity.epic;
	}
}
