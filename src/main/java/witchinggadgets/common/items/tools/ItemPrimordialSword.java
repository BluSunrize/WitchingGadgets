package witchinggadgets.common.items.tools;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairable;
import travellersgear.api.IActiveAbility;
import witchinggadgets.api.IPrimordial;

public class ItemPrimordialSword extends ItemSword implements IPrimordial, IActiveAbility, IRepairable
{
	IIcon overlay;

	public ItemPrimordialSword(ToolMaterial mat)
	{
		super(mat);
	}

	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand)
	{
		return true;
	}

	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{

	}

	@Override
	public int getReturnedPearls(ItemStack stack)
	{
		return 2;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialSword");
		this.overlay = iconRegister.registerIcon("witchinggadgets:primordialSword_overlay");
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getRenderPasses(int meta)
	{
		return 2;
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		if(pass == 0)
			return itemIcon;
		return overlay;
	}
}