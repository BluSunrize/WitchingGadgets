package witchinggadgets.common.util;

import net.minecraft.util.DamageSource;

public class WGDamageSources extends DamageSource
{

	public static DamageSource sacrifice = new WGDamageSources("wgSacrifice").setDamageBypassesArmor().setMagicDamage();
	public static DamageSource crushing = new WGDamageSources("wgCrush").setDamageIsAbsolute();

	protected WGDamageSources(String tag) {
		super(tag);
	}

}
