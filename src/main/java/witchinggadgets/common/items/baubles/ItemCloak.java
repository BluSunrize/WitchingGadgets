package witchinggadgets.common.items.baubles;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.armor.Hover;
import travellersgear.api.IActiveAbility;
import travellersgear.api.ITravellersGear;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.client.render.ModelCloak;
import witchinggadgets.common.WGModCompat;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCloak extends Item implements ITravellersGear, IActiveAbility
{
	String[] subNames = {"standard","spectral","storage","wolf","raven"};
	int[] defaultColours = {};
	IIcon iconRaven;
	IIcon iconWolf;

	public ItemCloak()
	{
		//		super(WGContent.standardCloak, 2, 1);
		this.setHasSubtypes(true);
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:cloak");
		this.iconRaven = iconRegister.registerIcon("witchinggadgets:cloak_raven");
		this.iconWolf = iconRegister.registerIcon("witchinggadgets:cloak_wolf");
	}
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		if(meta==3)
			return this.iconWolf;
		if(meta==4)
			return this.iconRaven;
		return this.itemIcon;
	}

	public boolean hasColor(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return getColor(stack);
	}
	public int getColor(ItemStack stack)
	{
		if(stack==null)
			return 0xffffff;
		int meta = stack.getItemDamage();
		if(meta==0)
		{
			NBTTagCompound tag = stack.getTagCompound();
			if(tag == null)
				return ClientUtilities.colour_CloakBlue;
			NBTTagCompound tagDisplay = tag.getCompoundTag("display");
			return tagDisplay == null ? ClientUtilities.colour_CloakBlue : (tagDisplay.hasKey("color") ? tagDisplay.getInteger("color") : ClientUtilities.colour_CloakBlue);
		}
		return meta==1?Aspect.DARKNESS.getColor(): meta==2?Aspect.VOID.getColor(): 0xffffff;
	}

	public void removeColor(ItemStack stack)
	{
		if(stack==null)
			return;
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null)
		{
			NBTTagCompound tagDisplay = tag.getCompoundTag("display");

			if (tagDisplay.hasKey("color"))
			{
				tagDisplay.removeTag("color");
			}
		}
	}

	public void setColour(ItemStack stack, int colour)
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

		nbttagcompound1.setInteger("color", colour);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String layer)
	{
		//System.out.println("error: "+layer);
		//if(layer != null)
		//	return "witchinggadgets:textures/blocks/nil.png";

		//		Cloak cloak = getCloakFromStack(itemstack);
		//		if(cloak != null)
		//			return cloak.getTexture();cloakRaven
		if(itemstack.getItemDamage()<subNames.length)
			if(subNames[itemstack.getItemDamage()].equals("wolf"))
				return "witchinggadgets:textures/models/cloakWolf.png";
			else if(subNames[itemstack.getItemDamage()].equals("raven"))
				return "witchinggadgets:textures/models/cloakRaven.png";
		return "witchinggadgets:textures/models/cloak.png";
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return 0;
		//		Cloak cloak = getCloakFromStack(stack);
		//		if(cloak != null)
		//			return cloak.getMaxDamage();
		//		return getMaxDamage();
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
	{
		return new ModelCloak(getColor(itemStack));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		//		Cloak cloak = getCloakFromStack(itemstack);
		//		if(cloak != null)
		return getUnlocalizedName() + "." + subNames[stack.getItemDamage()];
		//		return getUnlocalizedName();
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int i=0; i<subNames.length; i++)
			if(i!=4 || WGModCompat.loaded_Twilight)
				itemList.add(new ItemStack(item,1,i));
		//		for(String tag: WGContent.cloakRegistry.keySet())
		//			if(tag != null)
		//			{
		//				itemList.add(getCloakWithTag(tag));
		//			}
	}

	//	public static ItemStack getCloakWithTag(String tag)
	//	{
	//		Cloak c = WGContent.cloakRegistry.get(tag);
	//		if(c==null)
	//		{
	//			WitchingGadgets.logger.log(Level.ERROR, "Cloak with tag '"+tag+"' not found.");
	//			return null;
	//		}
	//		ItemStack cStack = new ItemStack(WGContent.ItemCloak);
	//		NBTTagCompound nbt = new NBTTagCompound();
	//		nbt.setString("cloak", c.getUnlocalizedName());
	//		cStack.setTagCompound(nbt);
	//		return cStack;
	//	}

	//	public static Cloak getCloakFromStack(ItemStack stack)
	//	{
	//		if(!(stack.getItem() instanceof ItemCloak))
	//			return null;
	//		if(stack.getTagCompound() == null)
	//			return null;
	//		String tag = stack.getTagCompound().getString("cloak");
	//		return WGContent.cloakRegistry.get(tag);
	//	}

	public ItemStack[] getStoredItems(ItemStack item)	
	{
		ItemStack[] stackList = new ItemStack[27];

		if (item.hasTagCompound()) {
			NBTTagList inv = item.getTagCompound().getTagList("InternalInventory",10);

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
		{
			item.setTagCompound(new NBTTagCompound());
		}
		for (int i = 0; i < inv.tagCount(); i++)
		{
			//NBTTagCompound tag = (NBTTagCompound)inv.tagAt(i);
			//ItemStack testStack = ItemStack.loadItemStackFromNBT(tag);
		}
		item.getTagCompound().setTag("InternalInventory",inv);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		//		if(hasCloakUniqueDiscounter(getCloakFromStack(stack)))
		//		{
		//			AspectList al = getCloakFromStack(stack).getVisDiscount();
		//			list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ":");
		//			for(Aspect a:al.getAspects())
		//				list.add("\u00a7"+a.getChatcolor()+a.getName()+": "+al.getAmount(a) + "%");
		//		}
		//		else
	}

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

	@Override
	public int getSlot(ItemStack stack)
	{
		return 0;
	}

	@Override
	public void onTravelGearTick(EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemDamage()<subNames.length)
		{
			if(subNames[stack.getItemDamage()].equals("spectral") && !player.worldObj.isRemote && stack.hasTagCompound() && stack.getTagCompound().getBoolean("isSpectral"))
				if(player.ticksExisted%100==0)
					if(!Utilities.consumeVisFromInventoryWithoutDiscount(player, new AspectList().add(Aspect.AIR,1)))
						stack.getTagCompound().setBoolean("isSpectral",false);

			if(subNames[stack.getItemDamage()].equals("raven"))
			{
				if(!player.onGround)
				{
					if(player.capabilities.isFlying || Hover.getHover(player.getEntityId()))
					{
						if(player.moveForward>0)
							player.moveFlying(0,1, .05f);
						player.motionY *= 1.125;
					}
					else if(player.motionY<0)
					{
						float mod = player.isSneaking()?.1f:.05f;
						player.motionY *= player.isSneaking()?.75:.5;
						double x = Math.cos(Math.toRadians(player.rotationYawHead + 90)) * mod;
						double z = Math.sin(Math.toRadians(player.rotationYawHead + 90)) * mod;
						player.motionX += x;
						player.motionZ += z;
					}
					player.fallDistance = 0f;
				}

			}
		}
	}
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		this.onTravelGearTick(player, stack);
	}

	@Override
	public void onTravelGearEquip(EntityPlayer player, ItemStack stack)
	{

	}

	@Override
	public void onTravelGearUnequip(EntityPlayer player, ItemStack stack)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("isSpectral"))
			stack.getTagCompound().setBoolean("isSpectral",false);
	}

	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemDamage()<subNames.length)
			if(subNames[stack.getItemDamage()].equals("storage") && !player.worldObj.isRemote)
				player.openGui(WitchingGadgets.instance, 4, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			else if(subNames[stack.getItemDamage()].equals("raven") && !player.worldObj.isRemote)
				player.openGui(WitchingGadgets.instance, 5, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			else if(subNames[stack.getItemDamage()].equals("spectral") && !player.worldObj.isRemote && Utilities.consumeVisFromInventoryWithoutDiscount(player, new AspectList().add(Aspect.AIR,1)))
			{
				if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("isSpectral", !stack.getTagCompound().getBoolean("isSpectral"));
				if(stack.getTagCompound().getBoolean("isSpectral"))
				{
					for(EntityCreature e : (List<EntityCreature>)player.worldObj.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.getBoundingBox(player.posX-16,player.posY-16,player.posZ-16, player.posX+16,player.posY+16,player.posZ+16)))
						if(e!=null && !(e instanceof IBossDisplayData) && player.equals(e.getAttackTarget()))
							e.setAttackTarget(null);
				}
			}
	}

}