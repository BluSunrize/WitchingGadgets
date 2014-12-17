package travellersgear.api;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TravellersGearAPI
{
	/**
	 * @return The NBTTagCompound under which all TRPG data is saved
	 */
	public static NBTTagCompound getTravellersNBTData(EntityPlayer player)
	{
		if(!player.getEntityData().hasKey("TravellersRPG"))
			player.getEntityData().setTag("TravellersRPG", new NBTTagCompound());
		return player.getEntityData().getCompoundTag("TravellersRPG");
	}

	static Method m_isStackPseudoTravellersGear=null;
	/**This method returns whether the given stack is an item valid for Traveller's Gear slots.
	 * It will prioritize the ITravellersGear API, but will fall back to reflection,
	 * using the method isStackPseudoTravellersGear in ModCompatability.class, to check whether the item was registered via IMC
	 */
	public static boolean isTravellersGear(ItemStack stack)
	{
		if(stack==null)
			return false;
		if(stack.getItem() instanceof ITravellersGear)
			return true;
		try
		{
			if(m_isStackPseudoTravellersGear==null)
			{
				Class c_ModCompatability = Class.forName("travellersgear.common.util.ModCompatability") ;
				m_isStackPseudoTravellersGear = c_ModCompatability.getMethod("isStackPseudoTravellersGear", ItemStack.class);
			}
			return (Boolean) m_isStackPseudoTravellersGear.invoke(null, stack);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * ====== INVENTORY ======
	 */

	/**@param player The targeted player
	 * @return The TRPG Extended inventory, consisting of Cloak(0), Shoulders(1), Vambraces(2), TitleScroll(3
	 */
	public static ItemStack[] getExtendedInventory(EntityPlayer player)
	{
		ItemStack[] ret = new ItemStack[4];
		NBTTagList inv = getTravellersNBTData(player).getTagList("Inventory", 10);
		if(inv!=null)
		{
			for (int i=0; i<inv.tagCount(); i++)
			{
				NBTTagCompound nbttagcompound = inv.getCompoundTagAt(i);
				int slot = nbttagcompound.getByte("Slot") & 0xFF;
				ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
				if (itemstack != null && slot<ret.length)
					ret[slot] = itemstack;
			}
		}
		return ret;
	}
	/**@param player The targeted player
	 * @param inv The TRPG Extended inventory, consisting of Cloak(0), Shoulders(1), Vambraces(2), TitleScroll(3
	 */
	public static void setExtendedInventory(EntityPlayer player, ItemStack[] inv)
	{
		if(player==null||inv==null)
			return;

		NBTTagList list = new NBTTagList();
		for (int i=0; i<inv.length; i++)
			if(inv[i]!=null)
			{
				NBTTagCompound invSlot = new NBTTagCompound();
				invSlot.setByte("Slot", (byte)i);
				inv[i].writeToNBT(invSlot);
				list.appendTag(invSlot);
			}
		getTravellersNBTData(player).setTag("Inventory", list);
	}

	/**@param player The targeted player
	 * @return The tile currently equipped on the player
	 */
	public static String getTitleForPlayer(EntityPlayer player)
	{
		ItemStack scroll = getExtendedInventory(player)[3];
		if(scroll!=null)
		{
			if(scroll.hasTagCompound() && scroll.getTagCompound().hasKey("title"))
				return scroll.getTagCompound().getString("title");
			if(scroll.hasTagCompound() && scroll.getTagCompound().hasKey("display") && scroll.getTagCompound().getCompoundTag("display").hasKey("Lore"))
            {
                NBTTagList loreList = scroll.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
                if(loreList.tagCount()>0)
    				return loreList.getStringTagAt(0);
            }
			if(scroll.hasDisplayName())
				return scroll.getDisplayName();
		}
		return null;
	}


}