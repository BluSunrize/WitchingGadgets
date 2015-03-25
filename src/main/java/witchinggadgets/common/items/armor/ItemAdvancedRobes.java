package witchinggadgets.common.items.armor;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.render.ModelRobeSkirted;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdvancedRobes extends ItemArmor implements IRepairable, IVisDiscountGear
{
	public IIcon iconChest;
	public IIcon iconChestOverlay;
	public IIcon iconLegs;
	public IIcon iconLegsOverlay;

	public ItemAdvancedRobes(ArmorMaterial armorMaterial, int par3, int par4)
	{
		super(armorMaterial, par3, par4);
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.iconChest = iconRegister.registerIcon("witchinggadgets:chestplateRobeAdvanced");
		this.iconChestOverlay = iconRegister.registerIcon("witchinggadgets:chestplateRobeAdvanced_overlay");
		this.iconLegs = iconRegister.registerIcon("witchinggadgets:leggingsRobeAdvanced");
		this.iconLegsOverlay = iconRegister.registerIcon("witchinggadgets:leggingsRobeAdvanced_overlay");
	}

	@Override
	public IIcon getIconFromDamage(int par1)
	{
		return this.armorType == 2 ? this.iconLegs : this.iconChest;
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		switch(pass)
		{
		case 0:
			switch(this.armorType)
			{
			case 1:
				return iconChest;
			case 2:
				return iconLegs;
			}
		case 1:
			switch(this.armorType)
			{
			case 1:
				return iconChestOverlay;
			case 2:
				return iconLegsOverlay;
			}
		}
		return iconChest;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		if (slot == 1)
		{
			return type == null ? "witchinggadgets:textures/models/advancedRobes_1.png" : "witchinggadgets:textures/models/advancedRobes_1_overlay.png";
		}
		if (slot == 2) {
			return type == null ? "witchinggadgets:textures/models/advancedRobes_2.png" : "witchinggadgets:textures/models/advancedRobes_2_overlay.png";
		}
		return type == null ? "witchinggadgets:textures/models/advancedRobes_1.png" : "witchinggadgets:textures/models/advancedRobes_1_overlay.png";
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(stack, par2EntityPlayer, null) + "%");
	}


	@Override
	public boolean hasColor(ItemStack par1ItemStack)
	{
		return true;
	}

	@Override
	public int getColor(ItemStack par1ItemStack)
	{
		NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();

		if (nbttagcompound == null)
		{
			return 6961280;
		}
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
		return nbttagcompound1 == null ? 6961280 : (nbttagcompound1.hasKey("color") ? nbttagcompound1.getInteger("color") : 6961280);
	}

	@Override
	public void removeColor(ItemStack par1ItemStack)
	{
		NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color"))
			{
				nbttagcompound1.removeTag("color");
			}
		}
	}

	@Override
	public void func_82813_b(ItemStack par1ItemStack, int par2)
	{
		NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();

		if (nbttagcompound == null)
		{
			nbttagcompound = new NBTTagCompound();
			par1ItemStack.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display"))
		{
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", par2);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
	{
		if(armorSlot==1)
			return ModelRobeSkirted.getModel();
		return null;
	}

	@Override
	public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect)
	{
		return this.armorType == 2 ? 4 : 5;
	}
}
