package witchinggadgets.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class WGDamageSources extends DamageSource
{

	public static DamageSource sacrifice = new WGDamageSources("wgSacrifice").setDamageBypassesArmor().setMagicDamage();
	public static DamageSource crushing = new WGDamageSources("wgCrush").setDamageIsAbsolute();
//	public static DamageSource backstab = new WGDamageSources("wgBackstab").setDamageIsAbsolute();

	protected WGDamageSources(String tag)
	{
		super(tag);
	}
	
	public static DamageSource getBackstabDamage(EntityPlayer player)
	{
		return new EntityDamageSource("wgBackstab", player);
	}

}
