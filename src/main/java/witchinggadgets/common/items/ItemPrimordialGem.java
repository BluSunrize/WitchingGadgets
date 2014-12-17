package witchinggadgets.common.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import witchinggadgets.WitchingGadgets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPrimordialGem extends Item
{
	String[] subNames = {"perfodio","vacuos","sano","aer"};
	HashMap<String,Object[]> subItems = new HashMap<String,Object[]>();

	IIcon iconFrame;

	public ItemPrimordialGem()
	{
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(WitchingGadgets.tabWG);

		subItems.put(subNames[0], new Object[]{true, false});
		subItems.put(subNames[1], new Object[]{false, true});
		subItems.put(subNames[2], new Object[]{true, true});
		subItems.put(subNames[3], new Object[]{true, false});
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		String sA = null;
		String sP = null;
		if(isActiveGem(stack))
			sA = StatCollector.translateToLocal("wg.gui.primordialGem.activeAbility")+" "+StatCollector.translateToLocal("wg.gui.primordialGem."+subNames[stack.getItemDamage()]+".desc.active");
		if(isPassiveGem(stack))
			sP = StatCollector.translateToLocal("wg.gui.primordialGem.passiveAbility")+" "+StatCollector.translateToLocal("wg.gui.primordialGem."+subNames[stack.getItemDamage()]+".desc.passive");
		if(sA != null)
			list.add(sA);
		if(sP != null)
			list.add(sP);
		//if(stack.hasTagCompound() && stack.getTagCompound().hasKey("BraceletSlot"))
		//	list.add("Slot: "+stack.getTagCompound().getInteger("BraceletSlot"));
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass==0)
		{
			String key = subNames[stack.getItemDamage()];
			Aspect a = Aspect.getAspect(key);
			if(a != null)
				return a.getColor();
			else
				if(subItems.get(key).length>2 && subItems.get(key)[2] != null && subItems.get(key)[2] instanceof Integer)
					return (Integer) subItems.get(key)[2];
			return 16777215;
		}
		return super.getColorFromItemStack(stack,pass);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialGem");
		this.iconFrame = iconRegister.registerIcon("witchinggadgets:primordialGem_frame");
	}

	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getRenderPasses(int meta)
	{
		return 2;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		if(pass == 0)
			return itemIcon;
		return iconFrame;
	}


	public void tickGem(World world, ItemStack bracelet, ItemStack gem, EntityLivingBase living)
	{
		String tag = subNames[gem.getItemDamage()];
		if(tag.equalsIgnoreCase("sano"))
		{
			if(living.getActivePotionEffect(Potion.regeneration)==null)
			{
				living.addPotionEffect(new PotionEffect(Potion.regeneration.id,20));
			}
		}
	}
	public boolean useGem(World world, ItemStack bracelet, ItemStack gem, EntityPlayer player)
	{
		String tag = subNames[gem.getItemDamage()];
		if(tag.equalsIgnoreCase("aer"))
		{
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX-6, player.posY-2, player.posZ-6, player.posX+6, player.posY+3, player.posZ+6);
			aabb.expand(6, 6, 6);
			List<Entity> targets = world.getEntitiesWithinAABBExcludingEntity(player, aabb);
			for(Entity ent:targets)
				if(player.canEntityBeSeen(ent))
				{
					double distX = ent.posX - player.posX;
					double distZ = ent.posZ - player.posZ;
					double dist = Math.sqrt(distX * distX + distZ * distZ);
					double force = Math.max(0, 1.0 - dist / 10.0);
					Vec3 look = player.getLookVec();
					ent.motionX += force * look.xCoord;
					ent.motionY = 0.25;
					ent.motionZ += force * look.zCoord;
				}

		}
		return false;
	}
	public boolean useGemOnEntity(World world, ItemStack bracelet, ItemStack gem, EntityPlayer player, Entity target)
	{

		return false;
	}
	public boolean useGemOnBlock(World world, ItemStack bracelet, ItemStack gem, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		String tag = subNames[gem.getItemDamage()];

		if(tag.equalsIgnoreCase("perfodio"))
		{
			int xMin = side==0||side==1||side==2||side==3? x-1 : x;
			int xMax = side==0||side==1||side==2||side==3? x+1 : x;
			int yMin = side==2||side==3||side==4||side==5? y-1 : y;
			int yMax = side==2||side==3||side==4||side==5? y+1 : y;
			int zMin = side==0||side==1||side==4||side==5? z-1 : z;
			int zMax = side==0||side==1||side==4||side==5? z+1 : z;
			for(int xx=xMin;xx<=xMax;xx++)
				for(int yy=yMin;yy<=yMax;yy++)
					for(int zz=zMin;zz<=zMax;zz++)
					{
						Block b = world.getBlock(xx, yy, zz);
						int bMeta = world.getBlockMetadata(xx, yy, zz);
						if(b!=null)
							if (!player.capabilities.isCreativeMode && b.canEntityDestroy(world, xx, yy, zz, player))
							{
								if(b.removedByPlayer(world, player, xx, yy, zz, true))
									b.onBlockDestroyedByPlayer(world, xx, yy, zz, bMeta);
								b.harvestBlock(world, player, xx, yy, zz, bMeta);
								b.onBlockHarvested(world, xx, yy, zz, bMeta, player);
							}
							else
								world.setBlockToAir(xx, yy, zz);
					}
		}
		return false;
	}

	public boolean isActiveGem(ItemStack gem)
	{
		return (Boolean) subItems.get(subNames[gem.getItemDamage()])[0];
	}
	public boolean isPassiveGem(ItemStack gem)
	{
		return (Boolean) subItems.get(subNames[gem.getItemDamage()])[1];
	}


	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int i=0;i<subNames.length;i++)
		{
			itemList.add(new ItemStack(this,1,i));
		}
	}
}
