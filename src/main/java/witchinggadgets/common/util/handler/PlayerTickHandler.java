package witchinggadgets.common.util.handler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.potions.PotionWarpWard;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;
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
					{
						WitchingGadgets.proxy.createSweatFx(player);
					}
					if(!world.isAABBInMaterial(player.boundingBox, Material.water) && (!player.isPotionActive(PotionWarpWard.instance) || player.getActivePotionEffect(PotionWarpWard.instance).getDuration()<5))
					{
						//System.out.println(player.isInsideOfMaterial(Material.water));
						player.addPotionEffect(new PotionEffect(PotionWarpWard.instance.id,20,0,true));
					}
					if(world.rand.nextInt(200)==0)
						Thaumcraft.addWarpToPlayer(player, -1, true);
				}
				else
					TileEntitySaunaStove.targetedPlayers.remove(player.getEntityId());	
			}
//			System.out.println("Hi "+world.isRemote);
			if(!world.isRemote && world.provider.dimensionId == WGConfig.dimensionMirrorID)
			{
//				int x = (int)Math.round(player.posX);
//				int y = (int)player.posY;
//				int z = (int)Math.round(player.posZ);
//				if(player.isSneaking())y--;
//				if(!player.isRiding() && !player.capabilities.isFlying)
//				{
//					for(int xx=-2;xx<=2;xx++)
//						for(int zz=-2;zz<=2;zz++)
//							if(world.isAirBlock(x+xx, y-1, z+zz))
//								world.setBlock(x+xx, y-1, z+zz, WGContent.BlockVoidWalkway);
//				}
			}
		}
	}

}