package witchinggadgets.common.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.util.Lib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBag extends Item
{
	String[] subNames = {"normal","void","ender"};
	IIcon[] overlayIcons = new IIcon[subNames.length];

	public ItemBag()
	{
		super();
		this.setHasSubtypes(true);
		setMaxStackSize(1);
		setCreativeTab(WitchingGadgets.tabWG);
	}

	public static int getDefaultBagColour(int meta)
	{
		return meta==1?0x484848: meta==2?0x2d4741: 0x8a74bd;
	}
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return getBagColorFromItemStack(stack,pass);
	}
	public int getBagColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass>0)
			return 0xffffff;
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null)
			return getDefaultBagColour(stack.getItemDamage());
		NBTTagCompound tagDisplay = tag.getCompoundTag("display");
		return tagDisplay == null ? getDefaultBagColour(stack.getItemDamage()) : (tagDisplay.hasKey("color") ? tagDisplay.getInteger("color") : getDefaultBagColour(stack.getItemDamage()));
	}
	public void removeColorFromItemStack(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();

		if (tag != null)
		{
			NBTTagCompound tag1 = tag.getCompoundTag("display");
			if (tag1.hasKey("color"))
				tag1.removeTag("color");
		}
	}
	public void modifyColorOnItemStack(ItemStack stack, int par2)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null)
		{
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		NBTTagCompound tag1 = tag.getCompoundTag("display");

		if (!tag.hasKey("display"))
			tag.setTag("display", tag1);
		tag1.setInteger("color", par2);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("witchinggadgets:bag");
		for(int i=0; i<overlayIcons.length; i++)
			if(i!=0)
				overlayIcons[i] = iconRegister.registerIcon("witchinggadgets:bagOverlay_"+subNames[i]);
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getRenderPasses(int meta)
	{
		return meta>0?2:1;
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		if(pass==0)
			return this.itemIcon;
		return overlayIcons[meta];
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
	public EnumRarity getRarity(ItemStack stack)
	{
		return stack.getItemDamage()==1?EnumRarity.epic: EnumRarity.rare;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			if(stack.hasTagCompound())
			{
				if(!stack.getTagCompound().getBoolean("unlocked") && !stack.getTagCompound().getString("Owner").equalsIgnoreCase(player.getCommandSenderName()))
				{
					player.attackEntityFrom(DamageSource.magic, 4f);
					player.dropOneItem(true);
					player.addChatComponentMessage(new ChatComponentTranslation(Lib.CHAT+"notyourbag", stack.getTagCompound().getString("Owner")));
					return stack;
				}
				if(stack.getTagCompound().getString("Owner").equalsIgnoreCase(player.getCommandSenderName()) && player.isSneaking())
				{
					stack.getTagCompound().setBoolean("unlocked", !stack.getTagCompound().getBoolean("unlocked"));
					player.addChatComponentMessage(new ChatComponentTranslation(Lib.CHAT+"bag"+(stack.getTagCompound().getBoolean("unlocked")?"un":"")+"locked",new Object[0]));
					return stack;
				}
			}

			if(stack.getItemDamage()==0)
				player.openGui(WitchingGadgets.instance, 3, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			else if(stack.getItemDamage()==1)
				player.openGui(WitchingGadgets.instance, 11, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			else if(stack.getItemDamage()==2)
				player.displayGUIChest(player.getInventoryEnderChest());
		}
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int i=0;i<subNames.length;i++)
			itemList.add(new ItemStack(this,1,i));
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		if(item.getItemDamage()==1)
		{
			list.add(EnumChatFormatting.DARK_PURPLE+StatCollector.translateToLocal(Lib.DESCRIPTION+"filteredItems"));
			for(ItemStack stack : this.getStoredItems(item))
				if(stack!=null)
					list.add(EnumChatFormatting.DARK_GRAY+" "+stack.getDisplayName());
		}
	}

	public ItemStack[] getStoredItems(ItemStack item)	
	{
		ItemStack[] stackList = new ItemStack[18];

		if (item.hasTagCompound())
		{
			NBTTagList inv = item.getTagCompound().getTagList("Inventory",10);

			for (int i = 0; i < inv.tagCount(); i++)
			{
				NBTTagCompound tag = inv.getCompoundTagAt(i);
				int slot = tag.getByte("Slot") & 0xFF;

				if ((slot >= 0) && (slot < stackList.length))
				{
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
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stackList[i].writeToNBT(tag);
				inv.appendTag(tag);
			}
		}
		if(!item.hasTagCompound())
			item.setTagCompound(new NBTTagCompound());
		item.getTagCompound().setTag("Inventory",inv);
	}
}