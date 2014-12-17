package witchinggadgets.common.items.tools;

import java.util.Iterator;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.IRepairable;
import travellersgear.api.IActiveAbility;
import witchinggadgets.api.IPrimordial;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemPrimordialSword extends ItemSword implements IPrimordial, IActiveAbility, IRepairable
{
	IIcon overlay;

	public ItemPrimordialSword(ToolMaterial mat)
	{
		super(mat);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean canActivate(EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Override
	public void activate(EntityPlayer player, ItemStack stack)
	{

	}
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onLivingDrop(LivingDropsEvent event)
	{
		if(event.recentlyHit && event.source!=null && event.source.getSourceOfDamage() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage(); 
			if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem().equals(this))
			{

				System.out.println("Event!");
				ItemStack head=null;
				if(event.entityLiving instanceof EntitySkeleton)
					head = new ItemStack(Items.skull,1, ((EntitySkeleton)event.entityLiving).getSkeletonType());
				else if(event.entityLiving instanceof EntityZombie)
					head = new ItemStack(Items.skull,1, 2);
				else if(event.entityLiving instanceof EntityCreeper)
					head = new ItemStack(Items.skull,1, 4);
				else if(event.entityLiving instanceof EntityPlayer)
				{
					head = new ItemStack(Items.skull,1, 3);
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("SkullOwner", player.getDisplayName());
					head.setTagCompound(tag);
				}
				else if(Loader.isModLoaded("IguanaTweaksTConstruct"))
				{
					Item ith = GameRegistry.findItem("IguanaTweaksTConstruct", "skullItem");
					if(event.entityLiving instanceof EntityEnderman)
						head = new ItemStack(ith,1, 0);
					else if(event.entityLiving instanceof EntityPigZombie)
						head = new ItemStack(ith,1, 1);
					else if(event.entityLiving instanceof EntityBlaze)
						head = new ItemStack(ith,1, 2);
					else if(EntityList.getEntityString(event.entityLiving).equals("Blizz"))
						head = new ItemStack(ith,1, 3);
				}

				if(head!=null)
				{
					Iterator<EntityItem> i = event.drops.iterator();
					while (i.hasNext())
					{
						EntityItem eitem = i.next();
						if(eitem!=null && OreDictionary.itemMatches(eitem.getEntityItem(), head, true))
							return;
					}
					event.entityLiving.worldObj.spawnEntityInWorld(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX,event.entityLiving.posY,event.entityLiving.posZ, head));
				}
			}
		}
	}


	@Override
	public int getReturnedPearls(ItemStack stack)
	{
		return 2;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:primordialSword");
		this.overlay = iconRegister.registerIcon("witchinggadgets:primordialSword_overlay");
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
		return overlay;
	}
}