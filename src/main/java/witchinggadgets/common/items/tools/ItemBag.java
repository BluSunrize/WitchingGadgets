package witchinggadgets.common.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.util.Lib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBag extends Item
{
	public ItemBag()
	{
		super();
		setMaxStackSize(1);
		setCreativeTab(WitchingGadgets.tabWG);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int par2)
	{
		NBTTagCompound tag = stack.getTagCompound();

		if (tag == null)
		{
			return ClientUtilities.colour_BagDefault;
		}
		NBTTagCompound tagDisplay = tag.getCompoundTag("display");
		return tagDisplay == null ? ClientUtilities.colour_BagDefault : (tagDisplay.hasKey("color") ? tagDisplay.getInteger("color") : ClientUtilities.colour_BagDefault);
	}
	
	public void removeColorFromItemStack(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color"))
			{
				nbttagcompound1.removeTag("color");
			}
		}
	}

	public void modifyColorOnItemStack(ItemStack stack, int par2)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound == null)
		{
			nbttagcompound = new NBTTagCompound();
			stack.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display"))
		{
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", par2);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:bag");
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());		
		}
		if(!stack.getTagCompound().hasKey("Inventory"))
		{
			NBTTagList inv = new NBTTagList();
			stack.setTagInfo("Inventory", inv);
		}
		if(!stack.getTagCompound().hasKey("Owner") && entity instanceof EntityPlayer)
		{
			stack.getTagCompound().setString("Owner", ((EntityPlayer)entity).getCommandSenderName());
		}
		super.onUpdate(stack, world, entity, par4, par5);
	}

	@Override
	public IIcon getIconFromDamage(int par1)
	{
		return this.itemIcon;
	}

	@Override
	public EnumRarity getRarity(ItemStack itemstack)
	{
		return EnumRarity.rare;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			if(!stack.getTagCompound().getString("Owner").equalsIgnoreCase(player.getCommandSenderName()))
			{
				player.attackEntityFrom(DamageSource.magic, 4f);
				player.dropOneItem(true);
				String s = StatCollector.translateToLocalFormatted(Lib.CHAT+"notyourbag", stack.getTagCompound().getString("Owner"));
				player.addChatComponentMessage(new ChatComponentTranslation(s,new Object[0]));
				return stack;
			}
			
			player.openGui(WitchingGadgets.instance, 3, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
		}
		
		return super.onItemRightClick(stack, world, player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{

	}

	public ItemStack[] getStoredItems(ItemStack item)	
	{
		ItemStack[] stackList = new ItemStack[18];
		//System.out.println("Starting getting Items from NBT");

		if (item.hasTagCompound()) {
			NBTTagList inv = item.getTagCompound().getTagList("Inventory",10);
			//			NBTTagList inv = (NBTTagList)item.getTagCompound().getTag("Inventory");

			//System.out.println("Getting Items from NBT");
			for (int i = 0; i < inv.tagCount(); i++)
			{
				NBTTagCompound tag = inv.getCompoundTagAt(i);
				int slot = tag.getByte("Slot") & 0xFF;

				if ((slot >= 0) && (slot < stackList.length))
				{
					//System.out.println("Loading Stack in Slot "+slot);
					stackList[slot] = ItemStack.loadItemStackFromNBT(tag);
				}
			}
		}
		return stackList;
	}

	public void setStoredItems(ItemStack item, ItemStack[] stackList)
	{
		NBTTagList inv = new NBTTagList();

		for (int i = 0; i < stackList.length; i++)
		{
			if (stackList[i] != null)
			{
				//System.out.println("Saving:");
				//System.out.println(stackList[i].getDisplayName()+": "+stackList[i].stackSize+" to Slot "+i);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stackList[i].writeToNBT(tag);
				inv.appendTag(tag);
			}
		}
		//item.setTagInfo("Inventory", inv);
		if(!item.hasTagCompound())
		{
			//System.out.println("creating new Tag Compound");
			item.setTagCompound(new NBTTagCompound());
		}
		for (int i = 0; i < inv.tagCount(); i++)
		{
			//NBTTagCompound tag = (NBTTagCompound)inv.tagAt(i);
			//ItemStack testStack = ItemStack.loadItemStackFromNBT(tag);
			//System.out.println("Saved:");
			//System.out.println(testStack.getDisplayName()+": "+testStack.stackSize+" to Slot "+tag.getByte("Slot"));
		}
		item.getTagCompound().setTag("Inventory",inv);
	}
}