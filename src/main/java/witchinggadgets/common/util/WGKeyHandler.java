package witchinggadgets.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;
import witchinggadgets.common.util.network.PacketPrimordialGlove;
import witchinggadgets.common.util.network.PacketUseCloak;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WGKeyHandler
{
	public static KeyBinding useCloakKey = new KeyBinding("Use Cloak", 34, "key.categories.misc");
	public static KeyBinding thaumcraftFKey;

	public boolean[] keyDown = {false,false};
	public static float gemRadial;
	public static boolean gemLock = false;

	public WGKeyHandler()
	{
		ClientRegistry.registerKeyBinding(useCloakKey);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.side == Side.SERVER)
			return;
		if (event.phase == TickEvent.Phase.START)
		{
			if(thaumcraftFKey==null)
			{
				for(KeyBinding kb : Minecraft.getMinecraft().gameSettings.keyBindings)
					if(kb.getKeyCategory()=="key.categories.misc" && kb.getKeyDescription()=="Change Wand Focus")
						thaumcraftFKey=kb;
			}	

			EntityPlayer player = event.player;
			if (FMLClientHandler.instance().getClient().inGameHasFocus)
			{
				if(useCloakKey.getIsKeyPressed() && !keyDown[0] && Minecraft.getMinecraft().currentScreen==null)//&& (player.getCurrentArmor(2).getItem() instanceof ItemCloak || player.getCurrentArmor(2).getItem() instanceof ItemRunicCloak) )
				{
//					if(Utilities.getActiveMagicalCloak(player)!=null)
//					{
						WitchingGadgets.packetPipeline.sendToServer(new PacketUseCloak(player));
//						ItemStack cloakStack = Utilities.getActiveMagicalCloak(player);
//						if(cloakStack != null && cloakStack.getItem() instanceof ItemCloak)
//							ItemCloak.getCloakFromStack(cloakStack).onCloakUsed(player, cloakStack);
//					}
					keyDown[0] = true;
				}
				else if(keyDown[0])
					keyDown[0] = false;
			}
			float step = .15f;
			if(thaumcraftFKey!=null && thaumcraftFKey.getIsKeyPressed() && !keyDown[1])
			{
				if(player.isSneaking() && player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove)
					WitchingGadgets.packetPipeline.sendToServer(new PacketPrimordialGlove(Minecraft.getMinecraft().thePlayer, (byte)1, 0));
				if(gemLock)
				{
					gemLock=false;
					keyDown[1] = true;
				}
				else if(FMLClientHandler.instance().getClient().inGameHasFocus && player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove)
				{
					if(gemRadial<1)
						gemRadial += step;
					if(gemRadial>1)
						gemRadial=1f;
					if(gemRadial>=1)	
						gemLock=true;
				}
				//System.out.println("radial: "+gemRadial);
			}
			else
			{
				if(keyDown[1])
					keyDown[1]=false;
				if(!gemLock)
				{
					if(gemRadial>0)
						gemRadial -= step;
					if(gemRadial<0)
						gemRadial=0f;
				}

			}
		}
	}

}