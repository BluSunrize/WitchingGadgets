package witchinggadgets.common.util.handler;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.potions.PotionWarpWard;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.WGModCompat;
import witchinggadgets.common.blocks.tiles.TileEntitySaunaStove;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class PlayerTickHandler
{
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;

		if(player != null && event.phase.equals(TickEvent.Phase.START))
		{
			World world = player.worldObj;
			if(TileEntitySaunaStove.targetedPlayers.containsKey(player.getEntityId()))
			{
				TileEntitySaunaStove stove = TileEntitySaunaStove.targetedPlayers.get(player.getEntityId());
				boolean flag = false;
				for(AxisAlignedBB aabb : stove.boundingBoxes)
					if(world.getEntitiesWithinAABB(EntityPlayer.class, aabb).contains(player))
						flag = true;

				if(flag && !stove.isInvalid() && stove.tick>0)
				{
					if(world.rand.nextInt(100)==0)
					{
						WGModCompat.enviromineDoSaunaStuff(player, .01f,.01f);
						if(player.getFoodStats().getFoodLevel()>6)
							player.getFoodStats().addStats(-1, .1f);
					}

					if(world.isRemote && world.rand.nextInt(3)==0)
						WitchingGadgets.proxy.createSweatFx(player);
					if(!world.isAABBInMaterial(player.boundingBox, Material.water))
						player.addPotionEffect(new PotionEffect(PotionWarpWard.instance.id,20,0,true));
					if(world.rand.nextInt(200)==0)
						Thaumcraft.addWarpToPlayer(player, -1, true);
				}
				else
					TileEntitySaunaStove.targetedPlayers.remove(player.getEntityId());	
			}
			if(!player.worldObj.isRemote && player.riddenByEntity!=null && player.riddenByEntity instanceof EntityLivingBase && EnchantmentHelper.getEnchantmentLevel(WGContent.enc_rideProtect.effectId, player.getCurrentArmor(3))>0)
			{
				player.riddenByEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), 1);
				player.riddenByEntity.addVelocity(player.getRNG().nextFloat()*.4f, .1f, player.getRNG().nextFloat()*.4f);
				((EntityLivingBase)player.riddenByEntity).dismountEntity(player);
				player.riddenByEntity.ridingEntity=null;
				player.riddenByEntity=null;
			}

		}
	}

}