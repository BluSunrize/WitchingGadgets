package witchinggadgets.common.items.tools;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairable;
import travellersgear.api.IActiveAbility;
import witchinggadgets.api.IPrimordial;
import witchinggadgets.common.util.WGDamageSources;

public class ItemPrimordialHammer extends ItemPickaxe implements IPrimordial, IActiveAbility, IRepairable
{
	IIcon overlay;

	public ItemPrimordialHammer(ToolMaterial mat)
	{
		super(mat);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase user)
    {
		if(target instanceof EntitySlime || target instanceof EntitySkeleton)
			target.attackEntityFrom(WGDamageSources.crushing, 6);
		stack.damageItem(1, user);
        return true;
    }
	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack)
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
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialHammer");
		this.overlay = iconRegister.registerIcon("witchinggadgets:primordialHammer_overlay");
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