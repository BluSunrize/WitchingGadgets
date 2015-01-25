package witchinggadgets.asm.pouch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.container.ContainerFocusPouch;
import thaumcraft.common.container.InventoryFocusPouch;
import thaumcraft.common.items.wands.ItemFocusPouch;
import baubles.api.BaublesApi;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ContainerPatchedFocusPouch extends ContainerFocusPouch
{
	public ContainerPatchedFocusPouch(InventoryPlayer iinventory, World world, int par3, int par4, int par5)
	{
		super(iinventory,world,par3,par4,par5);
		ReflectionHelper.setPrivateValue(ContainerFocusPouch.class,this, -1, "blockSlot");
		
		ItemStack beltPouch = null;
		IInventory baubles = BaublesApi.getBaubles(iinventory.player);
		for(int a = 0; a < 4; a++)
			if(baubles.getStackInSlot(a)!=null && baubles.getStackInSlot(a).getItem() instanceof ItemFocusPouch)
				beltPouch = baubles.getStackInSlot(a);

		if(beltPouch!=null)
		{
			ReflectionHelper.setPrivateValue(ContainerFocusPouch.class,this, beltPouch, "pouch");
			if(!world.isRemote)
				((InventoryFocusPouch)this.input).stackList = ((ItemFocusPouch)beltPouch.getItem()).getInventory(beltPouch);
		}
		onCraftMatrixChanged(this.input);
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		if(!player.worldObj.isRemote)
		{
			//			((ItemFocusPouch)this.pouch.getItem()).setInventory(this.pouch, ((InventoryFocusPouch)this.input).stackList);
			//			if (this.player == null) {
			//				return;
			//			}
			//			if ((this.player.getHeldItem() != null) && (this.player.getHeldItem().isItemEqual(this.pouch))) {
			//				this.player.setCurrentItemOrArmor(0, this.pouch);
			//			}
			//			this.player.inventory.markDirty();
			ItemStack beltPouch = ReflectionHelper.getPrivateValue(ContainerFocusPouch.class,this, "pouch");
			if(beltPouch!=null)
				if(BaublesApi.getBaubles(player).getStackInSlot(3)!=null && BaublesApi.getBaubles(player).getStackInSlot(3).isItemEqual(beltPouch))
				{
					BaublesApi.getBaubles(player).setInventorySlotContents(3, beltPouch);
					BaublesApi.getBaubles(player).markDirty();
				}
		}
	}
}
