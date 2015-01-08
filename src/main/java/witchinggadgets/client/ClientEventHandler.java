package witchinggadgets.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import thaumcraft.api.IGoggles;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import travellersgear.api.RenderTravellersGearEvent;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.WGResearch;
import witchinggadgets.common.blocks.tiles.TileEntitySaunaStove;
import witchinggadgets.common.items.ItemClusters;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;
import witchinggadgets.common.util.Lib;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.WGKeyHandler;
import witchinggadgets.common.util.handler.InfusedGemHandler;
import witchinggadgets.common.util.network.PacketPrimordialGlove;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientEventHandler
{
	boolean headgearDisabled = true;
	boolean armDisabled = true;
	boolean capeDisabled = true;
	boolean shouldResetSpecialRenders = false;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event)
	{
		AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;
		ModelBiped playerModel = null;
		try
		{
			Object model = ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, (RenderPlayer)RenderManager.instance.getEntityRenderObject(player), 1);
			if(model instanceof ModelBiped)
				playerModel = (ModelBiped) model;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		if(playerModel != null)
		{
			//			System.out.println("hi.");
			playerModel.bipedLeftLeg.rotateAngleX = (float) Math.toRadians(70f);
			//			playerModel.bipedBody.showModel = true;
			//			playerModel.bipedLeftLeg.showModel = true;
			//			playerModel.bipedRightLeg.showModel = true;
			//			if(player.getCurrentArmor(3)!=null && player.getCurrentArmor(3).getItem().equals(WGContent.ItemEliteArmorHelm))
			//			{
			//				playerModel.bipedHeadwear.isHidden=true;
			//				headgearDisabled = true;
			//			}
			//			else if(headgearDisabled)
			//			{
			//				playerModel.bipedHeadwear.isHidden=false;
			//				headgearDisabled = false;
			//			}
			//
			//			if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem().equals(WGContent.ItemPrimordialBracelet))
			//			{
			//				playerModel.bipedRightArm.isHidden=true;
			//				armDisabled = true;
			//			}
			//			else if(armDisabled)
			//			{
			//				playerModel.bipedRightArm.isHidden=false;
			//				armDisabled = false;
			//			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void renderPlayerSpecials(RenderPlayerEvent.Specials.Pre event)
	{
		if(Minecraft.getMinecraft().thePlayer!=null)
			if(!event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer) && event.entityPlayer.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && EnchantmentHelper.getEnchantmentLevel(WGContent.enc_unveiling.effectId, Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4))>0 )
			{
				float x = (float)event.entityPlayer.posX+event.entityPlayer.getRNG().nextFloat()-.5f;
				float y = (float)event.entityPlayer.posY+1+event.entityPlayer.getRNG().nextFloat()-.5f;
				float z = (float)event.entityPlayer.posZ+event.entityPlayer.getRNG().nextFloat()-.5f;
				Thaumcraft.proxy.sparkle(x,y,z, 1, 0, 0);
			}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void getTooltip(ItemTooltipEvent event)
	{
		//		String mod = GameRegistry.findUniqueIdentifierFor(event.itemStack.getItem())!=null?GameRegistry.findUniqueIdentifierFor(event.itemStack.getItem()).modId:"Minecraft";
		//		String cS = Block.getBlockFromItem(event.itemStack.getItem())!=null?Block.getBlockFromItem(event.itemStack.getItem()).getClass().getCanonicalName():event.itemStack.getItem().getClass().getCanonicalName();
		//		event.toolTip.add(mod+":"+event.itemStack.getItem().getUnlocalizedName()/*+"__"+cS*/);

		if(Block.getBlockFromItem(event.itemStack.getItem())!=null)
			for(Class intf : Block.getBlockFromItem(event.itemStack.getItem()).getClass().getInterfaces())
				if(intf.getCanonicalName().endsWith("InfusionStabiliser"))
					event.toolTip.add("Works as  Infusion Stabilizer");
		//for(int o: OreDictionary.getOreIDs(event.itemStack))
		//	event.toolTip.add(OreDictionary.getOreName(o));

		if(event.itemStack.getTagCompound()!=null && event.itemStack.getTagCompound().hasKey("AdvancedTooltipInfo"))
		{
			NBTTagList taglist = event.itemStack.getTagCompound().getTagList("AdvancedTooltipInfo",8);
			if(taglist.tagCount()>0)
				for(int i=0;i<taglist.tagCount();i++)
				{
					String[] unformattedCode = {"!0!"    ,"!1!"    ,"!2!"    ,"!3!"    ,"!4!"    ,"!5!"    ,"!6!"    ,"!7!"    ,"!8!"    ,"!9!"    ,"!a!"    ,"!b!"    ,"!c!"    ,"!d!"    ,"!e!"    ,"!f!"    ,"!k!"    ,"!l!"    ,"!m!"    ,"!n!"    ,"!o!"    ,"!r!"};
					String[] formattedCode = {  "\u00A70","\u00A71","\u00A72","\u00A73","\u00A74","\u00A75","\u00A76","\u00A77","\u00A78","\u00A79","\u00A7a","\u00A7b","\u00A7c","\u00A7d","\u00A7e","\u00A7f","\u00A7k","\u00A7l","\u00A7m","\u00A7n","\u00A7o","\u00A7r"};

					String unformatted = taglist.getStringTagAt(i);

					for(int j=0;j<unformattedCode.length;j++)
						if(unformatted.contains(unformattedCode[j]))
							unformatted = unformatted.replaceAll(unformattedCode[j], formattedCode[j]);
					if(unformatted.contains("\u00A7") && !unformatted.endsWith("\u00A7r"))
						unformatted += "\u00A7r";
					event.toolTip.add(1+i,unformatted);
				}
		}

		try{
			if(event.entityPlayer!=null)
				if(ThaumcraftApiHelper.isResearchComplete(event.entityPlayer.getCommandSenderName(), "GEMCUTTING") && InfusedGemHandler.isGem(event.itemStack) && GuiScreen.isShiftKeyDown())
				{
					if(InfusedGemHandler.getNaturalAffinities(event.itemStack)!=null && InfusedGemHandler.getNaturalAffinities(event.itemStack).length>0)
					{
						event.toolTip.add(EnumChatFormatting.DARK_GREEN+StatCollector.translateToLocal(Lib.DESCRIPTION+"gemaffinity"));
						for(Aspect a : InfusedGemHandler.getNaturalAffinities(event.itemStack))
							if(a!=null)
								event.toolTip.add(" "+EnumChatFormatting.DARK_GREEN+a.getName());
					}
					if(InfusedGemHandler.getNaturalAversions(event.itemStack)!=null && InfusedGemHandler.getNaturalAversions(event.itemStack).length>0)
					{
						event.toolTip.add(EnumChatFormatting.RED+StatCollector.translateToLocal(Lib.DESCRIPTION+"gemaversion"));
						for(Aspect a : InfusedGemHandler.getNaturalAversions(event.itemStack))
							if(a!=null)
								event.toolTip.add(" "+EnumChatFormatting.RED+a.getName());
					}
				}
		}catch(Exception e){e.printStackTrace();}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void handleMouse(MouseEvent event)
	{
		//		System.out.println("Mouse Event");
		if(event.button==0 && inGemSearch)
		{
			inGemSearch=false;
			WGKeyHandler.gemLock=false;
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			mc.setIngameFocus();

			int mx = event.x-mc.displayWidth/2;
			int my = event.y-mc.displayHeight/2;
			double radius = Math.sqrt(mx*mx + my*my);

			//			System.out.println(radius);

			double cx = mx/radius;//Math.cos(Math.toRadians(angle));
			double angle = (mx<0?180:0)+Math.abs((mx<0?-180:0)+ (my<0?90:0)+Math.abs((my<0?-90:0)+Math.abs(Math.toDegrees(Math.acos(cx))-90)));
			int sel = angle>288?0: angle<72?1: 2+(int)((288-angle)/72);
			//System.out.println("Selected: "+sel);
			WitchingGadgets.packetPipeline.sendToServer(new PacketPrimordialGlove(player, (byte)0, sel));
		}
		//		if(thaumSearchField!=null)
		//		{
		//			Minecraft mc = Minecraft.getMinecraft();
		//
		//			int mx = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
		//			int my = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;
		//			//			int k = Mouse.getEventButton();
		//			System.out.println("click at "+mx+", "+my);
		//			thaumSearchField.mouseClicked(mx, my, event.button);
		//		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderGameOverlay(RenderGameOverlayEvent.Pre event)
	{
		//		if(true)
		//			return;
		if(TileEntitySaunaStove.targetedPlayers.containsKey(Minecraft.getMinecraft().thePlayer.getEntityId()) && event.type == RenderGameOverlayEvent.ElementType.HELMET)
		{
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			ClientUtilities.bindTexture("witchinggadgets:textures/gui/steam_overlay.png");
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, (double)event.resolution.getScaledHeight(), -90.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)event.resolution.getScaledWidth(), (double)event.resolution.getScaledHeight(), -90.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)event.resolution.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		if(event.type == RenderGameOverlayEvent.ElementType.TEXT && WGKeyHandler.gemRadial>0)
		{
			Minecraft mc = Minecraft.getMinecraft();
			RenderItem ri = RenderItem.getInstance();

			if(WGKeyHandler.gemLock && (mc.thePlayer.getCurrentEquippedItem()==null || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove)))
				WGKeyHandler.gemLock=false;	

			GL11.glEnable(3042);
			double rad = 50*WGKeyHandler.gemRadial;
			int x = event.resolution.getScaledWidth()/2;
			int y = event.resolution.getScaledHeight()/2;
			ClientUtilities.bindTexture("witchinggadgets:textures/gui/gauntletRadial0.png");

			GL11.glTranslatef(x, y, 0);
			GL11.glRotatef(180+180*WGKeyHandler.gemRadial, 0, 0, 1);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(0xffffff);
			tessellator.addVertexWithUV(-rad, +rad, 0.0D, 0, 1);
			tessellator.addVertexWithUV(+rad, +rad, 0.0D, 1, 1);
			tessellator.addVertexWithUV(+rad, -rad, 0.0D, 1, 0);
			tessellator.addVertexWithUV(-rad, -rad, 0.0D, 0, 0);
			tessellator.draw();

			ClientUtilities.bindTexture("witchinggadgets:textures/gui/gauntletRadial1.png");
			float mod = (int)((System.currentTimeMillis()%10000))/10000f;
			//			System.out.println(mod);
			GL11.glRotatef(360*mod, 0, 0, 1);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(0xffffff);
			tessellator.addVertexWithUV(-rad, +rad, 0.0D, 0, 1);
			tessellator.addVertexWithUV(+rad, +rad, 0.0D, 1, 1);
			tessellator.addVertexWithUV(+rad, -rad, 0.0D, 1, 0);
			tessellator.addVertexWithUV(-rad, -rad, 0.0D, 0, 0);
			tessellator.draw();
			GL11.glRotatef(360*mod, 0, 0,-1);



			if(mc.thePlayer.getCurrentEquippedItem()!=null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove)
			{
				ItemStack[] gems = ItemPrimordialGlove.getSetGems(mc.thePlayer.getCurrentEquippedItem());

				int mx = Mouse.getX()-mc.displayWidth/2;
				int my = Mouse.getY()-mc.displayHeight/2;
				double reverseRadius = Math.sqrt(mx*mx + my*my);
				double reverseAngle = (mx<0?180:0)+Math.abs((mx<0?-180:0)+ (my<0?90:0)+Math.abs((my<0?-90:0)+Math.abs(Math.toDegrees(Math.acos(mx/reverseRadius))-90)));
				int sel = reverseAngle>288?0: reverseAngle<72?1: 2+(int)((288-reverseAngle)/72);
				//				
				//				mc.fontRenderer.drawString("mPos: "+mx+", "+my+", sel: "+sel, x, y, 0xffffff);
				GL11.glPushMatrix();
				for(int g=0;g<gems.length;g++)
					if(gems[g]!=null)
					{
						int ix = (int)( ((g==0?-54: g==1?13: g==3?-22: g==2?-76 :35)/256f)*rad*2);
						int iy = (int)( ((g==0||g==1?-64: g==3?36 :-6)/256f)*rad*2);
						ri.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), gems[g],ix,iy);
						if(sel!=g)
						{
							GL11.glDepthFunc(GL11.GL_EQUAL);
							GL11.glDisable(GL11.GL_LIGHTING);
							GL11.glDepthMask(false);
							ClientUtilities.bindTexture("witchinggadgets:textures/models/white.png");
							GL11.glEnable(3042);
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glBlendFunc(770,771);

							for (int j1 = 0; j1 < 2; ++j1)
							{
								tessellator.startDrawingQuads();
								tessellator.setColorRGBA_I(0, 64);
								tessellator.addVertexWithUV(ix-2+00, iy-2+20, 50, 0,1);//((f2 + (float)p5 * f4) * f), )((f3 + (float)p5) * f1));
								tessellator.addVertexWithUV(ix-2+20, iy-2+20, 50, 1,1);//((f2 + (float)p4 + (float)p5 * f4) * f), ((f3 + (float)p5) * f1));
								tessellator.addVertexWithUV(ix-2+20, iy-2+00, 50, 1,0);//((f2 + (float)p4) * f), ((f3 + 0.0F) * f1));
								tessellator.addVertexWithUV(ix-2+00, iy-2+00, 50, 0,0);//((f2 + 0.0F) * f), ((f3 + 0.0F) * f1));
								tessellator.draw();
							}

							GL11.glDepthMask(true);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glDisable(GL11.GL_ALPHA_TEST);
							GL11.glEnable(GL11.GL_LIGHTING);
							GL11.glDepthFunc(GL11.GL_LEQUAL);
						}
					}
				GL11.glPopMatrix();
			}

			GL11.glRotatef(180+180*WGKeyHandler.gemRadial, 0, 0,-1);
			GL11.glTranslatef(-x,-y,0);
			GL11.glDisable(GL11.GL_LIGHTING);

			if(WGKeyHandler.gemLock)
			{
				if((!inGemSearch || mc.inGameHasFocus))
				{
					inGemSearch=true;
					mc.setIngameFocus();
					mc.setIngameNotInFocus();
				}
			}
			else if(inGemSearch)
			{
				inGemSearch=false;
				mc.setIngameFocus();
			}

		}
	}
	public static boolean inGemSearch=false;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderArmor(SetArmorModel event)
	{
		if(event.stack!=null && EnchantmentHelper.getEnchantmentLevel(WGContent.enc_invisibleGear.effectId, event.stack)>0)
		{	
			boolean unveiling = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_unveiling.effectId, Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4))>0;
			if(event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer) || !unveiling )
				event.result=-2;
		}
		for(ItemStack cloak : Utilities.getActiveMagicalCloak(event.entityPlayer))
			if(cloak!=null && cloak.hasTagCompound() && cloak.getTagCompound().getBoolean("isSpectral"))
				event.result=-2;
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderTravellersGear(RenderTravellersGearEvent event)
	{
		if(event.stack!=null && EnchantmentHelper.getEnchantmentLevel(WGContent.enc_invisibleGear.effectId, event.stack)>0)
		{	
			boolean unveiling = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_unveiling.effectId, Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4))>0;
			if(event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer) || !unveiling )
				event.shouldRender=false;
		}
		for(ItemStack cloak : Utilities.getActiveMagicalCloak(event.entityPlayer))
			if(cloak!=null && cloak.hasTagCompound() && cloak.getTagCompound().getBoolean("isSpectral"))
				event.shouldRender=false;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		if(Minecraft.getMinecraft().thePlayer!=null && event.gui instanceof GuiResearchBrowser)
		{
			if(ThaumcraftApiHelper.isResearchComplete(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), "WGFAKEELDRITCHMINOR"))
				ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[1];
			else
				ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[0];
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void initializeIcons(TextureStitchEvent.Post event)
	{
		if(Minecraft.getMinecraft().thePlayer != null)
		{
			if(ThaumcraftApiHelper.isResearchComplete(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), "WGELDRITCHBASE"))
				ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[1];
			else
				ResearchCategories.researchCategories.get("WITCHGADG").background = WGResearch.wgbackgrounds[0];
		}
		ItemClusters.setupClusters();
	}

	static float spectralAlpha = .5f;
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void setSpecialRendersLiving(RenderLivingEvent.Pre event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer pl = Minecraft.getMinecraft().thePlayer.worldObj.getPlayerEntityByName(event.entity.getCommandSenderName());
			if(pl!=null)
				for(ItemStack cloak : Utilities.getActiveMagicalCloak(pl))
					if(cloak!=null && cloak.hasTagCompound() && cloak.getTagCompound().getBoolean("isSpectral"))
					{
						GL11.glEnable(3042);
						boolean goggles = Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4)!=null && (Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4).getItem() instanceof IRevealer || Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4).getItem() instanceof IGoggles);
						boolean unveiling = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_unveiling.effectId, Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4))>0;

						if(event.entity.equals(Minecraft.getMinecraft().thePlayer))
							GL11.glColor4f(.5f,.5f,.5f,spectralAlpha);
						else if(unveiling)
							GL11.glColor4f(1,1,1,.75f);
						else if(goggles)
							GL11.glColor4f(.25f,.25f,.25f,spectralAlpha);
						else
							GL11.glColor4f(1,1,1, 0);
					}
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void resetResetSpecialLiving(RenderLivingEvent.Post event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer pl = Minecraft.getMinecraft().thePlayer.worldObj.getPlayerEntityByName(event.entity.getCommandSenderName());
			if(pl!=null)
				for(ItemStack cloak : Utilities.getActiveMagicalCloak(pl))
					if(cloak!=null && cloak.hasTagCompound() && cloak.getTagCompound().getBoolean("isSpectral"))
					{
						GL11.glDisable(3042);
						GL11.glColor4f(1,1,1,1);
					}
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void renderPlayerSpecials(RenderLivingEvent.Specials.Pre event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer pl = Minecraft.getMinecraft().thePlayer.worldObj.getPlayerEntityByName(event.entity.getCommandSenderName());
			if(pl!=null)
				for(ItemStack cloak : Utilities.getActiveMagicalCloak(pl))
					if(cloak!=null && cloak.hasTagCompound() && cloak.getTagCompound().getBoolean("isSpectral"))
					{
						boolean unveiling = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_unveiling.effectId, Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4))>0;
						if(!unveiling)
						{

							event.setCanceled(true);
						}
					}
		}
	}
}