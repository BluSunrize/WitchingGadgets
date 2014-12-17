package witchinggadgets.common.blocks.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.lib.utils.InventoryUtils;

public class TileEntitySnowGen extends TileEntityWGBase
{
	public ForgeDirection facing = ForgeDirection.getOrientation(2);
	public int tick = 0;
	int tickGoal = 40;

	public void updateEntity()
	{
		super.updateEntity();
		if(canWork())
		{
			tick++;
			if(tick==32 && worldObj.isRemote)
				worldObj.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, "random.fizz", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
			if(tick>=tickGoal)
			{
				if(!worldObj.isRemote)
					createSnow();
				else
					worldObj.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, "dig.stone", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
				tick=0;
			}
		}
		else
		{
			if(tick > 0)
				tick = 0;
		}

	}

	private void createSnow()
	{
		ItemStack snow = new ItemStack(Items.snowball);
		TileEntity inventory = this.worldObj.getTileEntity(this.xCoord + this.facing.offsetX, this.yCoord, this.zCoord + this.facing.offsetZ);
		if ((inventory != null) && ((inventory instanceof IInventory)))
			snow = InventoryUtils.placeItemStackIntoInventory(snow, (IInventory)inventory, this.facing.getOpposite().ordinal(), true);

		if (snow != null)
			if(facing.equals(ForgeDirection.UP)||facing.equals(ForgeDirection.DOWN))
			{
				EntityItem ei = new EntityItem(this.worldObj, this.xCoord + 0.5D, this.yCoord + 0.5 + this.facing.offsetY * 0.66D, this.zCoord + 0.5D, snow.copy());
				ei.motionX = 0.025000000372529D;;
				ei.motionY = (0.075F * this.facing.offsetY);
				ei.motionZ = 0.025000000372529D;;
				this.worldObj.spawnEntityInWorld(ei);
			}
			else
			{
				EntityItem ei = new EntityItem(this.worldObj, this.xCoord + 0.5D + this.facing.offsetX * 0.66D, this.yCoord + 0.4D + this.facing.getOpposite().offsetY, this.zCoord + 0.5D + this.facing.offsetZ * 0.66D, snow.copy());
				ei.motionX = (0.075F * this.facing.offsetX);
				ei.motionY = 0.025000000372529D;
				ei.motionZ = (0.075F * this.facing.offsetZ);
				this.worldObj.spawnEntityInWorld(ei);
			}
	}

	private boolean canOutput()
	{
		TileEntity inventory = this.worldObj.getTileEntity(this.xCoord + this.facing.offsetX, this.yCoord, this.zCoord + this.facing.offsetZ);
		if ((inventory != null) && ((inventory instanceof IInventory)))
			return InventoryUtils.insertStack((IInventory) inventory, new ItemStack(Items.snowball), this.facing.getOpposite().ordinal(), false) == null;
		return true;
	}
	public boolean canWork()
	{
		return canOutput() && worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)<=0 && !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("facing", this.facing.ordinal());
	}
	@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		this.facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
	}
}