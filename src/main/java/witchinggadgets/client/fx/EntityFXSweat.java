package witchinggadgets.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class EntityFXSweat extends EntityFX
{
	public EntityFXSweat(EntityPlayer player)
	{
		super(player.worldObj, player.posX - 0.2F + (player.getRNG().nextFloat() / 2F), player.boundingBox.minY + 0.5F + player.getRNG().nextFloat(), player.posZ - 0.2F + (player.getRNG().nextFloat() / 2F));
		this.particleBlue = MathHelper.randomFloatClamp(player.getRNG(), 0.3F, 0.8F);
		this.particleRed = this.particleGreen = 0.2F;
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = 20;
		this.motionX = this.motionY = this.motionZ = 0.0D;
	}
}