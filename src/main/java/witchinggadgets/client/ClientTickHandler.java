package witchinggadgets.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientTickHandler
{
	public static HashMap<ChunkCoordinates, Integer> oreHighlightMap = new HashMap();
	public static HashMap<ChunkCoordinates, Object> oreHighlightBeamMap = new HashMap();
	static int highlight;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent event)
	{
		EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
		if(player==null || oreHighlightMap.isEmpty())
		{
			oreHighlightMap.clear();
			oreHighlightBeamMap.clear();
			highlight = 0;
			return;
		}

		Map.Entry<ChunkCoordinates, Integer> e = oreHighlightMap.entrySet().toArray(new Map.Entry[0])[highlight];
		if(player.worldObj.getTotalWorldTime()%30==0)
			//			for(Map.Entry<ChunkCoordinates, Integer> e : oreHighlightMap.entrySet())
		{
			float x = e.getKey().posX+.5f;
			float y = e.getKey().posY+.75f;
			float z = e.getKey().posZ+.5f;

			double[] hand = ClientUtilities.getPlayerHandPos(player, true);

			//Thaumcraft.proxy.wispFX3(player.worldObj, hand[0],hand[1],hand[2], x,y,z, .15f,3,false,0);
			FXWisp ef = new FXWisp(player.worldObj, hand[0],hand[1],hand[2], x,y,z, .05f, 3);
			ef.noClip = true;
			ef.setGravity(0);
			ef.shrink = true;

			ParticleEngine.instance.addEffect(player.worldObj, ef);

			//oreHighlightBeamMap.put(e.getKey(), Thaumcraft.proxy.beamPower(player.worldObj, hand[0],hand[1],hand[2], x,y,z, 0, 1, 0, true, oreHighlightBeamMap.get(e.getKey())));//beamBore(Minecraft.getMinecraft().theWorld, x,y,z, hand[0],hand[1],hand[2], 1,Aspect.EARTH.getColor(), true, .2f, oreHighlightBeamMap.get(e.getKey()), 0));
			highlight++;
			if(highlight >= oreHighlightMap.size())
				highlight=0;
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		Iterator<Map.Entry<ChunkCoordinates, Integer>> it = oreHighlightMap.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<ChunkCoordinates, Integer> e = it.next();
			oreHighlightMap.put(e.getKey(), e.getValue()-1);
			if(oreHighlightMap.get(e.getKey())<=0)
			{
				oreHighlightBeamMap.remove(e.getKey());
				it.remove();
				highlight = 0;
			}
		}
	}
}