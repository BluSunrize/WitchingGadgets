package witchinggadgets.common.gui;

import net.minecraft.inventory.IInventory;
import thaumcraft.common.container.SlotGhost;

public class SlotGhostSingleItem extends SlotGhost {

	public SlotGhostSingleItem(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	public int getSlotStackLimit()
    {
		return 1;
    }
}
