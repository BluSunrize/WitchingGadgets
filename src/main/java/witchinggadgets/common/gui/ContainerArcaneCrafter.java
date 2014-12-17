package witchinggadgets.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.container.ContainerGhostSlots;
import thaumcraft.common.container.SlotLimitedByWand;
import witchinggadgets.common.blocks.tiles.TileEntityArcaneCrafter;

public class ContainerArcaneCrafter extends ContainerGhostSlots
{
	protected TileEntityArcaneCrafter tileEntity;
	//private int slotCount;

	public ContainerArcaneCrafter (InventoryPlayer inventoryPlayer, TileEntityArcaneCrafter te)
	{
		this.tileEntity = te;
		this.tileEntity.eventHandler = this;
//		System.out.println(this.tileEntity.);
		//Crafting Matrix
		for (int l = 0; l < 3; ++l)
		{
			for (int i1 = 0; i1 < 3; ++i1)
			{
				Slot slotGhost = new SlotGhostSingleItem(tileEntity, i1 + l * 3, 16 + i1 * 18, 12 + l * 18);
				this.addSlotToContainer(slotGhost);
			}
		}
//		for(int i = 0; i < 9; i++)
//			this.addSlotToContainer(new SlotGhost(tileEntity, i, 16+18*(i/3), 12+18*(i%3)));
		//Wand
		this.addSlotToContainer(new SlotLimitedByWand(tileEntity, 9, 88, 12));
		for(int i=10;i<19;i++)
			this.addSlotToContainer(new Slot(tileEntity, i, 16+18*(i-10), 82));
		//this.slotCount = 6;
		this.bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUseableByPlayer(player);
	}


	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						16 + j * 18, 118 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 16 + i * 18, 176));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return null;
		//		ItemStack stack = null;
		//		Slot slotObject = (Slot) inventorySlots.get(slot);
		//
		//		//null checks and checks if the item can be stacked (maxStackSize > 1)
		//		if (slotObject != null && slotObject.getHasStack()) {
		//			ItemStack stackInSlot = slotObject.getStack();
		//			stack = stackInSlot.copy();
		//
		//			//merges the item into player inventory since its in the tileEntity
		//			if (slot < slotCount) {
		//				if (!this.mergeItemStack(stackInSlot, slotCount, (slotCount + 36), true)) {
		//					return null;
		//				}
		//			}
		//			//places it into the tileEntity is possible since its in the player inventory
		//			else if (!this.mergeItemStack(stackInSlot, 0, slotCount, false)) {
		//				return null;
		//			}
		//
		//			if (stackInSlot.stackSize == 0) {
		//				slotObject.putStack(null);
		//			} else {
		//				slotObject.onSlotChanged();
		//			}
		//
		//			if (stackInSlot.stackSize == stack.stackSize) {
		//				return null;
		//			}
		//			slotObject.onPickupFromSlot(player, stackInSlot);
		//		}
		//		return stack;
	}

	//	@Override
	//	public ItemStack slotClick(int slotClicked, int button, int mod, EntityPlayer player)
	//	{
	//	    ItemStack itemstack = null;
	//	    InventoryPlayer inventoryplayer = player.inventory;
	//		System.out.println("slotClicked: "+slotClicked);
	//		if(slotClicked >= 0)
	//			System.out.println(this.getSlot(slotClicked));
	//		System.out.println("button: "+button);
	//		System.out.println("mod: "+mod);
	//		//System.out.println("player: "+player);
	////		if (slotClicked == -999)
	////		{
	////			if ((inventoryplayer.getItemStack() != null) && (slotClicked == -999))
	////			{
	////				if (button == 0)
	////				{
	////					player.dropPlayerItem(inventoryplayer.getItemStack());
	////					inventoryplayer.setItemStack((ItemStack)null);
	////				}
	////				if (button == 1)
	////				{
	////					player.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));
	////					if (inventoryplayer.getItemStack().stackSize == 0) {
	////						inventoryplayer.setItemStack((ItemStack)null);
	////					}
	////				}
	////			}
	////		}
	////		if(slotClicked >= 0 && this.getSlot(slotClicked) instanceof SlotGhost)
	////		{
	////			this.getSlot(slotClicked).putStack(inventoryplayer.getItemStack());
	////		}
	//		return super.slotClick(slotClicked, button, mod, player);
	//	}
}