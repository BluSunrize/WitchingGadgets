package witchinggadgets.common.items;

import thaumcraft.common.Thaumcraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityItemReforming extends EntityItem
{
	public int renderDelay = 120;

	public EntityItemReforming(World world, double x, double y, double z, ItemStack stack)
	{
		super(world, x, y, z, stack);
		this.delayBeforeCanPickup = 125;
	}

	public EntityItemReforming(World world)
	{
		super(world);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(renderDelay>0)
		{
			renderDelay--;
			if(renderDelay<=20)
				for(int i=0;i<6;i++)
					Thaumcraft.proxy.drawInfusionParticles1(worldObj, posX+rand.nextInt(3)-1.5, posY+rand.nextFloat(), posZ+rand.nextInt(3)-1.5, (int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ), getEntityItem().getItem(), getEntityItem().getItemDamage());
		}

	}


	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("renderDelay", this.renderDelay);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.renderDelay = par1NBTTagCompound.getInteger("renderDelay");
	}
}