package witchinggadgets.common.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import witchinggadgets.common.WGContent;

public class WGPotion extends Potion
{
	static ResourceLocation tex = new ResourceLocation("witchinggadgets","textures/gui/potioneffects.png");
	final int tickrate;
	final boolean halfTickRateWIthAmplifier;
	public WGPotion(int id, boolean isBad, int colour, int tick, boolean halveTick, int icon)
	{
		super(id, isBad, colour);
		this.tickrate = tick;
		this.halfTickRateWIthAmplifier = halveTick;
		this.setIconIndex(icon%8, icon/8);
	}

	@Override
	public int getStatusIconIndex()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
		return super.getStatusIconIndex();
	}
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		if(tickrate<0)
			return false;
		int k = tickrate >> amplifier;
		return k>0 ? duration%k == 0 : true;
	}
	@Override
	public void performEffect(EntityLivingBase living, int amplifier)
	{
		if(this.equals(WGContent.pot_dissolve))
			living.attackEntityFrom(DamageSourceThaumcraft.dissolve, 1);
	}
}