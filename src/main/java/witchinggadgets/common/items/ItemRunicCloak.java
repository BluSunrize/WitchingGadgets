//package witchinggadgets.common.items;
//
//import java.util.List;
//
//import net.minecraft.client.model.ModelBiped;
//import net.minecraft.client.renderer.texture.IIconRegister;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.IIcon;
//import net.minecraft.util.StatCollector;
//import net.minecraft.world.World;
//import thaumcraft.api.IVisDiscountGear;
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.aspects.AspectList;
//import thaumcraft.common.items.armor.ItemRunicArmor;
//import witchinggadgets.WitchingGadgets;
//import witchinggadgets.client.ModelCloak;
//import witchinggadgets.client.WGGraphicUtilities;
//import witchinggadgets.common.WGContent;
//import witchinggadgets.common.util.Cloak;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
//public class ItemRunicCloak extends ItemRunicArmor implements IVisDiscountGear
//{
//	IIcon iconRuneBase;
//
//	public ItemRunicCloak()
//	{
//		super(WGContent.standardCloak, 2, 1);
//		this.setCreativeTab(WitchingGadgets.tabWG);
//	}
//
//	@Override
//	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
//	{
//		super.onArmorTick(world, player, stack);
//		Cloak cloak = getCloakFromStack(stack);
//		if(cloak != null)
//			cloak.onItemUpdate(player, stack);
//	}
//
//	@Override
//	public boolean requiresMultipleRenderPasses()
//	{
//		return true;
//	}
//	@Override
//	public int getRenderPasses(int meta)
//	{
//		return 2;
//	}
//
//	@Override
//	public void registerIcons(IIconRegister iconRegister)
//	{
//		this.itemIcon = iconRegister.registerIcon("witchinggadgets:smallCloak");
//		this.iconRuneBase = iconRegister.registerIcon("witchinggadgets:runicBase");
//	}
//	@SideOnly(Side.CLIENT)
//	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
//	{
//		return pass <= 0 ? iconRuneBase : itemIcon;
//	}
//
//	@Override
//	public boolean hasColor(ItemStack stack)
//	{
//		if(getCloakFromStack(stack).getUnlocalizedName() == Cloak.standard.getUnlocalizedName())
//			return true;
//
//		return false;
//	}
//
//	@SideOnly(Side.CLIENT)
//	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
//	{
//		if (par2 <= 0)
//		{
//			return -1;
//		}
//		int j = this.getColor(par1ItemStack);
//
//		if (j < 0)
//		{
//			j = 16777215;
//		}
//
//		return j;
//	}
//
//	@Override
//	public int getColor(ItemStack stack)
//	{
//		NBTTagCompound tag = stack.getTagCompound();
//
//		if (tag == null)
//			return WGGraphicUtilities.colour_CloakBlue;
//		Cloak cloak = getCloakFromStack(stack);
//		if(cloak.getUnlocalizedName() != Cloak.standard.getUnlocalizedName())
//			return cloak.getDefaultColour();
//
//		NBTTagCompound tagDisplay = tag.getCompoundTag("display");
//		return tagDisplay == null ? WGGraphicUtilities.colour_CloakBlue : (tagDisplay.hasKey("color") ? tagDisplay.getInteger("color") : WGGraphicUtilities.colour_CloakBlue);
//	}
//
//	@Override
//	public void removeColor(ItemStack stack)
//	{
//		NBTTagCompound tag = stack.getTagCompound();
//
//		if (tag != null)
//		{
//			NBTTagCompound tagDisplay = tag.getCompoundTag("display");
//
//			if (tagDisplay.hasKey("color"))
//			{
//				tagDisplay.removeTag("color");
//			}
//		}
//	}
//
//	@Override
//	public void func_82813_b(ItemStack stack, int par2)
//	{
//		NBTTagCompound nbttagcompound = stack.getTagCompound();
//
//		if (nbttagcompound == null)
//		{
//			nbttagcompound = new NBTTagCompound();
//			stack.setTagCompound(nbttagcompound);
//		}
//
//		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
//
//		if (!nbttagcompound.hasKey("display"))
//		{
//			nbttagcompound.setTag("display", nbttagcompound1);
//		}
//
//		nbttagcompound1.setInteger("color", par2);
//	}
//
//	@Override
//	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String layer)
//	{
//		if(layer != null)
//			return "witchinggadgets:textures/blocks/nil.png";
//
//		Cloak cloak = getCloakFromStack(itemstack);
//		if(cloak != null)
//			return cloak.getTexture();
//		return "witchinggadgets:textures/models/cloak.png";
//	}
//
//	@SideOnly(Side.CLIENT)
//	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
//	{
//		return new ModelCloak();
//	}
//
//	@Override
//	public String getUnlocalizedName(ItemStack itemstack)
//	{
//		Cloak cloak = getCloakFromStack(itemstack);
//		if(cloak != null)
//			return getUnlocalizedName() + "." + cloak.getUnlocalizedName();
//		return getUnlocalizedName();
//	}
//
//	@Override
//	public void getSubItems(Item item, CreativeTabs tab, List itemList)
//	{
//		for(String tag: WGContent.cloakRegistry.keySet())
//			if(tag != null)
//			{
//				itemList.add(getCloakWithTag(tag));
//			}
//	}
//
//	public ItemStack getCloakWithTag(String tag)
//	{
//		Cloak c = WGContent.cloakRegistry.get(tag);
//		ItemStack cStack = new ItemStack(this);
//		NBTTagCompound nbt = new NBTTagCompound();
//		nbt.setString("cloak", c.getUnlocalizedName());
//		cStack.setTagCompound(nbt);
//		return cStack;
//	}
//
//	public static Cloak getCloakFromStack(ItemStack stack)
//	{
//		if(!(stack.getItem() instanceof ItemRunicCloak))return null;
//		if(stack.getTagCompound() == null)return null;
//		String tag = stack.getTagCompound().getString("cloak");
//		return WGContent.cloakRegistry.get(tag);
//	}
//
//	public ItemStack[] getStoredItems(ItemStack item)	
//	{
//		ItemStack[] stackList = new ItemStack[27];
//
//		if (item.hasTagCompound()) {
//			NBTTagList inv = item.getTagCompound().getTagList("InternalInventory",10);
//
//			for (int i = 0; i < inv.tagCount(); i++)
//			{
//				NBTTagCompound tag = inv.getCompoundTagAt(i);
//				int slot = tag.getByte("Slot") & 0xFF;
//
//				if ((slot >= 0) && (slot < stackList.length))
//				{
//					stackList[slot] = ItemStack.loadItemStackFromNBT(tag);
//				}
//			}
//		}
//		return stackList;
//	}
//
//	public void setStoredItems(ItemStack item, ItemStack[] stackList)
//	{
//		NBTTagList inv = new NBTTagList();
//
//		for (int i = 0; i < stackList.length; i++)
//		{
//			if (stackList[i] != null)
//			{
//				NBTTagCompound tag = new NBTTagCompound();
//				tag.setByte("Slot", (byte)i);
//				stackList[i].writeToNBT(tag);
//				inv.appendTag(tag);
//			}
//		}
//		if(!item.hasTagCompound())
//		{
//			item.setTagCompound(new NBTTagCompound());
//		}
//		for (int i = 0; i < inv.tagCount(); i++)
//		{
//			//NBTTagCompound tag = (NBTTagCompound)inv.tagAt(i);
//			//ItemStack testStack = ItemStack.loadItemStackFromNBT(tag);
//		}
//		item.getTagCompound().setTag("InternalInventory",inv);
//	}
//
//	@Override
//	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4)
//	{
//		super.addInformation(stack, par2EntityPlayer, list, par4);
//		//list.add(stack.getTagCompound().toString());
//		if(hasCloakUniqueDiscounter(getCloakFromStack(stack)))
//		{
//			AspectList al = getCloakFromStack(stack).getVisDiscount();
//			list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ":");
//			for(Aspect a:al.getAspects())
//				list.add("  \u00a7"+a.getChatcolor()+a.getName()+": "+al.getAmount(a) + "%");
//		}
//		else
//			list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(stack, par2EntityPlayer, Aspect.AIR) + "%");
//	}
//
//	@Override
//	public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect)
//	{
//		Cloak cloak = getCloakFromStack(stack);
//		AspectList al = null;
//		if(cloak != null)
//			al = cloak.getVisDiscount();
//		if(al != null && aspect != null)
//			return al.getAmount(aspect);
//		return 0;
//	}
//
//	private boolean hasCloakUniqueDiscounter(Cloak cloak)
//	{
//		AspectList al = cloak.getVisDiscount();
//		int aer = al.getAmount(Aspect.AIR);
//		int terra = al.getAmount(Aspect.EARTH);
//		int ignis = al.getAmount(Aspect.FIRE);
//		int aqua = al.getAmount(Aspect.WATER);
//		int ordo = al.getAmount(Aspect.ORDER);
//		int perdito = al.getAmount(Aspect.ENTROPY);
//		return !(aer==terra&&aer==ignis&&aer==aqua&&aer==ordo&&aer==perdito);
//	}
//
//}