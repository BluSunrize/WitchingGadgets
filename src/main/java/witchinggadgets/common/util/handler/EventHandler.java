package witchinggadgets.common.util.handler;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.IGoggles;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileInfusionMatrix;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.api.IPrimordial;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.items.ItemAdvancedScribingTools;
import witchinggadgets.common.items.ItemMaterials;
import witchinggadgets.common.items.baubles.ItemCloak;
import witchinggadgets.common.items.tools.ItemVoidBag;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.network.PacketClientNotifier;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler
{
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
	}
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
	}

	@SubscribeEvent
	public void entityHurt(LivingHurtEvent event)
	{
		if ((event.entityLiving instanceof EntityPlayer))
		{
			if(Utilities.getActiveMagicalCloak((EntityPlayer)event.entityLiving) != null)
			{
				EntityPlayer player = (EntityPlayer)event.entityLiving;
				ItemStack cloakStack = Utilities.getActiveMagicalCloak(player);
				if(cloakStack.getItem() instanceof ItemCloak && cloakStack.getItemDamage()==3)
				{	
					int amp = 1;
					if(event.ammount>=8)
						amp++;
					if(event.ammount>=12)
						amp++;
					if(player.getActivePotionEffect(Potion.damageBoost) == null || player.getActivePotionEffect(Potion.damageBoost).getDuration()<2 || player.getActivePotionEffect(Potion.damageBoost).getAmplifier()<amp)
						player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 60, amp));
					if(player.getActivePotionEffect(Potion.moveSpeed) == null || player.getActivePotionEffect(Potion.moveSpeed).getDuration()<2 || player.getActivePotionEffect(Potion.moveSpeed).getAmplifier()<amp)
						player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 60, amp));
					if(player.getActivePotionEffect(Potion.resistance) == null || player.getActivePotionEffect(Potion.resistance).getDuration()<2 || player.getActivePotionEffect(Potion.resistance).getAmplifier()<amp)
						player.addPotionEffect(new PotionEffect(Potion.resistance.id, 60, amp));
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingSetTarget(LivingSetAttackTargetEvent event)
	{
		if (!(event.target instanceof EntityPlayer))
			return;
		if(event.entityLiving instanceof EntityCreature)
		{
			ItemStack cloak = Utilities.getActiveMagicalCloak((EntityPlayer) event.target);
			if(cloak!=null && cloak.hasTagCompound() && cloak.getTagCompound().getBoolean("isSpectral"))
			{
				boolean goggles = event.entityLiving.getEquipmentInSlot(4)!=null && (event.entityLiving.getEquipmentInSlot(4).getItem() instanceof IRevealer || event.entityLiving.getEquipmentInSlot(4).getItem() instanceof IGoggles);
				boolean special = event.entityLiving instanceof IEldritchMob || event.entityLiving instanceof IBossDisplayData;
				if(!goggles && !special)
					((EntityCreature)event.entityLiving).setAttackTarget(null);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrop(LivingDropsEvent event)
	{
		if (event.entityLiving instanceof EntityWolf)
		{
			EntityWolf enemy = (EntityWolf) event.entityLiving;
			for(int i=0; i<2+Math.min(4, event.lootingLevel); i++)
				if (enemy.worldObj.rand.nextInt(Math.max(1, 3 - event.lootingLevel)) == 0)
				{
					EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, new ItemStack(WGContent.ItemMaterial,1,6));
					entityitem.delayBeforeCanPickup = 10;
					event.drops.add(entityitem);
				}
		}
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		for(int i=0; i<event.entityPlayer.inventory.getSizeInventory(); i++)
			if(event.entityPlayer.inventory.getStackInSlot(i)!=null && event.entityPlayer.inventory.getStackInSlot(i).getItem() instanceof ItemVoidBag)
			{
				ItemStack[] filter = ((ItemVoidBag)event.entityPlayer.inventory.getStackInSlot(i).getItem()).getStoredItems(event.entityPlayer.inventory.getStackInSlot(i));
				for(ItemStack f : filter)
					if(OreDictionary.itemMatches(f, event.item.getEntityItem(), true))
					{
						AspectList al = ThaumcraftCraftingManager.getObjectTags(event.item.getEntityItem());
						al = ThaumcraftCraftingManager.getBonusTags(event.item.getEntityItem(), al);
						if(al!=null && al.size()>=0)
						{
							AspectList primals = ResearchManager.reduceToPrimals(al);
							Aspect a = primals.getAspects()[event.entityPlayer.getRNG().nextInt(primals.getAspects().length)];
							if(a!=null)
							{
								int slot = InventoryUtils.isWandInHotbarWithRoom(a, 1, event.entityPlayer);
								if(slot>=0)
								{
									ItemWandCasting wand = (ItemWandCasting)event.entityPlayer.inventory.mainInventory[slot].getItem();
									wand.addVis(event.entityPlayer.inventory.mainInventory[slot], a, primals.getAmount(a), true);
								}
							}
						}
						event.item.setDead();
						event.setCanceled(true);
						return;
					}
			}
	}

	@SubscribeEvent
	public void onPlayerAttacking(AttackEntityEvent event)
	{
		//		Utilities.playerAttacks(event.entityPlayer, event.target);
		//		event.setCanceled(true);
	}

	@SubscribeEvent
	public void onCrafted(ItemCraftedEvent event)
	{
		ItemStack output = event.crafting;
		IInventory craftMatrix = event.craftMatrix;
		if( output.getItem() == WGContent.ItemMaterial && output.getItemDamage() == 7 )
		{
			for(int matrixSlot = 0; matrixSlot < 9; matrixSlot++)
			{
				ItemStack stackInMatrix = craftMatrix.getStackInSlot(matrixSlot);
				if( (stackInMatrix != null) && (stackInMatrix.getItem() instanceof ItemAdvancedScribingTools))
				{
					stackInMatrix.stackSize += 1;
					int newDamage = stackInMatrix.getItemDamage() + 100;
					if(newDamage <= stackInMatrix.getMaxDamage())
						stackInMatrix.setItemDamage(newDamage);
					else
					{
						output.getItem().equals(ConfigItems.itemResource);
						output.setItemDamage(2);
					}
					craftMatrix.setInventorySlotContents(matrixSlot, stackInMatrix);
				}
			}
		}
		if(output.getItem() == WGContent.ItemMaterial && output.getItemDamage() == 10)
		{
			for(int matrixSlot = 0; matrixSlot < 9; matrixSlot++)
			{
				ItemStack stackInMatrix = craftMatrix.getStackInSlot(matrixSlot);
				if( (stackInMatrix != null) && (stackInMatrix.getItem() instanceof ItemMaterials) && stackInMatrix.getItemDamage() == 9)
				{
					stackInMatrix.stackSize += 1;
					craftMatrix.setInventorySlotContents(matrixSlot, stackInMatrix);
				}
			}
		}
		if(output.getItem() instanceof IPrimordial && !event.player.worldObj.isRemote)
		{
			if(((IPrimordial)output.getItem()).getReturnedPearls(output)>0)
			{
				System.out.println("Outputting");
				double iX = event.player.posX;
				double iY = event.player.posY+1;
				double iZ = event.player.posZ;
				for(int yy=-16; yy<=16; yy++)
					for(int zz=-16; zz<=16; zz++)
						for(int xx=-16; xx<=16; xx++)
							if(event.player.worldObj.getTileEntity((int)event.player.posX+xx, (int)event.player.posY+yy, (int)event.player.posZ+zz) instanceof TileInfusionMatrix)
							{
								iX = event.player.posX+xx;
								iY = event.player.posY+yy-.5;
								iZ = event.player.posZ+zz;
							}
				EntitySpecialItem entityitem = new EntitySpecialItem(event.player.worldObj, iX, iY, iZ, new ItemStack(ConfigItems.itemEldritchObject, ((IPrimordial)output.getItem()).getReturnedPearls(output) ,3));
				entityitem.setVelocity(0,0,0);
				event.player.worldObj.spawnEntityInWorld(entityitem);
			}
		}
	}

	@SubscribeEvent
	public void playerLogin(PlayerLoggedInEvent event)
	{
		WitchingGadgets.packetPipeline.sendTo(new PacketClientNotifier(0), (EntityPlayerMP) event.player);
	}
}
