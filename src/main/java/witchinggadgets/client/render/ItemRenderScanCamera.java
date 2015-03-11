package witchinggadgets.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import witchinggadgets.client.ClientProxy;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.util.Utilities;

public class ItemRenderScanCamera implements IItemRenderer
{
	String goldTexture = ("thaumcraft:textures/models/scanner.png");
	String leatherTexture = ("witchinggadgets:textures/models/cameraLeather.png");
	String scannerTexture = ("thaumcraft:textures/models/scanscreen.png");
	String woodTexture = ("thaumcraft:textures/blocks/planks_greatwood.png");

	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type)
	{
		//		if(type==ItemRenderType.INVENTORY)
		//			return false;
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
	{
		try{
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP player = mc.thePlayer;

			GL11.glPushMatrix();
			GL11.glEnable(3042);
			GL11.glBlendFunc(770,771);
			if(type == ItemRenderType.ENTITY)
			{
				GL11.glScaled(2, 2, 2);
				GL11.glTranslated(0, 0.125, 0);
				ClientUtilities.bindTexture(goldTexture);
				ClientProxy.cameraModel.renderPart("gold_01");
				ClientUtilities.bindTexture(leatherTexture);
				ClientProxy.cameraModel.renderPart("leather_02");
				ClientUtilities.bindTexture(scannerTexture);
				ClientProxy.cameraModel.renderPart("scanner_03");
				ClientUtilities.bindTexture(woodTexture);
				ClientProxy.cameraModel.renderPart("wood_04");
				GL11.glDisable(3042);
				GL11.glPopMatrix();
				return;
			}
			if(type == ItemRenderType.INVENTORY)
			{
				GL11.glScaled(2, 2, 2);
				ClientUtilities.bindTexture(goldTexture);
				ClientProxy.cameraModel.renderPart("gold_01");
				ClientUtilities.bindTexture(leatherTexture);
				ClientProxy.cameraModel.renderPart("leather_02");
				ClientUtilities.bindTexture(scannerTexture);
				ClientProxy.cameraModel.renderPart("scanner_03");
				ClientUtilities.bindTexture(woodTexture);
				ClientProxy.cameraModel.renderPart("wood_04");
				GL11.glDisable(3042);
				GL11.glPopMatrix();
				return;
			}

			//		RenderHelper.disableStandardItemLighting();


			//TRANSFORMS BEFORE CUSTOM RENDER
			//		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			//GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
			//GL11.glRotatef(335.0F, 0.0F, 0.0F, -1.0F);
			//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			//
			GL11.glPushMatrix();
			if(type == ItemRenderType.EQUIPPED)
			{
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-70.0F, 1.0F, 0.0F, 0.0F);
				GL11.glScalef(2f,1.5f,-1.5f);
				GL11.glTranslatef(-0.5F,-0.875F,-0.1F);
			}

			if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				GL11.glRotatef(45.0F, 0.0F, -1.0F, 0.0F);
				GL11.glScalef(2f,2f,2f);
				/**GL11.glTranslatef(-0.35F, 0.875F, 1.0F);*/
				GL11.glTranslatef(-0.35F, 0.5F, 1.0F);
				float timer = UtilsFX.getTimer(mc).renderPartialTicks;
				//			float f1 = UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer) + (UtilsFX.getEquippedProgress(mc.entityRenderer.itemRenderer) - UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer)) * timer;
				//			float f2 = entityclientplayermp.prevRotationPitch + (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * timer;


				float f6;
				float f7;
				float f8;
				float f9;
				float f10;
				float f11;
				float f12 = 0.8F;

				int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 0);
				int k = i / 65536;

				Render render;
				RenderPlayer renderplayer;

				f12 = 0.8F;
				f7 = player.getSwingProgress(timer);
				f7 = 0;
				f8 = MathHelper.sin(f7 * (float)Math.PI);
				f6 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
				GL11.glTranslatef(-f6 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI * 2.0F) * 0.2F, -f8 * 0.2F);
				//			f7 = 1.0F - f2 / 45.0F + 0.1F;

				GL11.glTranslatef(0.0F, 0.0F * f12 - (1.0F - 1.0f) * 1.2F + 0.04F, -0.9F * f12);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				ClientUtilities.bindTexture(player.getLocationSkin().getResourceDomain()+":"+player.getLocationSkin().getResourcePath());

				//			for (k = 0; k < 2; ++k)
				//			{
				//				int l = k * 2 - 1;
				//				GL11.glPushMatrix();
				//				GL11.glTranslatef(-0.1F, -0.6F, 1.1F * l);
				//				GL11.glRotatef(-25 * l, 1.0F, 0.0F, 0.0F);
				//				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				//				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				//				GL11.glRotatef(-50 * l, 0.0F, 1.0F, 0.0F);
				//				render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
				//				renderplayer = (RenderPlayer)render;
				//				f11 = 1.0F;
				//				GL11.glScalef(f11, f11, f11);
				//				renderplayer.renderFirstPersonArm(mc.thePlayer);
				//				GL11.glPopMatrix();
				//			}

				for (k = 0; k < 2; ++k)
				{
					int l = k * 2 - 1;
					GL11.glPushMatrix();
					GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.4F, -0.5F, 0.4F * l);
					GL11.glRotatef(-35 * l, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(49.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(80 * l, 0.0F, 1.0F, 0.0F);
					render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
					renderplayer = (RenderPlayer)render;
					f11 = 1.0F;
					GL11.glScalef(f11, f11, f11);
					renderplayer.renderFirstPersonArm(mc.thePlayer);
					GL11.glPopMatrix();
				}

				f8 = player.getSwingProgress(timer);
				f6 = MathHelper.sin(f8 * f8 * (float)Math.PI);
				f9 = MathHelper.sin(MathHelper.sqrt_float(f8) * (float)Math.PI);
				GL11.glRotatef(-f6 * 20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-f9 * 20.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-f9 * 80.0F, 1.0F, 0.0F, 0.0F);
				f10 = 0.38F;
				GL11.glScalef(f10, f10, f10);
				GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
				f11 = 0.015625F;
				GL11.glScalef(f11, f11, f11);

				GL11.glScalef(1/f11, 1/f11, 1/f11);
				GL11.glScalef(1/f10, 1/f10, 1/f10);
				GL11.glRotatef(11.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glTranslated(0.6,0.15,0.005);
			GL11.glScalef(0.875f,1.75f,1.75f);

			ClientUtilities.bindTexture(goldTexture);
			ClientProxy.cameraModel.renderPart("gold_01");
			if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				GL11.glColor3d(0.6, 0.6, 0.6);
				GL11.glScalef(1.75f,1,1);
				GL11.glTranslated(0.1,0,0);
				ClientProxy.cameraModel.renderPart("Display_00");
				GL11.glTranslated(-0.1,0,0);
				GL11.glScalef(1/1.75f,1,1);
				GL11.glColor3d(1,1,1);
			}
			ClientUtilities.bindTexture(leatherTexture);
			ClientProxy.cameraModel.renderPart("leather_02");
			ClientUtilities.bindTexture(woodTexture);
			ClientProxy.cameraModel.renderPart("wood_04");

			GL11.glScalef(1,-1,1);
			ClientUtilities.bindTexture(scannerTexture);
			ClientProxy.cameraModel.renderPart("scanner_03");
			GL11.glScalef(0.375f,0.375f,-0.375f);

			GL11.glRotated(90, 0, 1, 0);
			if(type != ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				GL11.glPushMatrix();
				GL11.glColor3d(0.5, 0.5, 0.5);
				GL11.glTranslated(-0.425,-0.425,-0.5625);
				GL11.glScalef(0.85f,0.85f,0.85f);
				IIcon ic = ConfigBlocks.blockWoodenDevice.getIcon(6, 0);
				ClientUtilities.bindTexture(mc.getTextureManager().getResourceLocation(new ItemStack(ConfigBlocks.blockWoodenDevice).getItemSpriteNumber()).getResourceDomain()+":"+mc.getTextureManager().getResourceLocation(stack.getItemSpriteNumber()).getResourcePath());
				Tessellator tes = Tessellator.instance;
				tes.startDrawingQuads();
				tes.addVertexWithUV(0, 1, 0.0D, ic.getMinU(), ic.getMaxV());
				tes.addVertexWithUV(1, 1, 0.0D, ic.getMaxU(), ic.getMaxV());
				tes.addVertexWithUV(1, 0, 0.0D, ic.getMaxU(), ic.getMinV());
				tes.addVertexWithUV(0, 0, 0.0D, ic.getMinU(), ic.getMinV());
				tes.draw();
				GL11.glPopMatrix();

				GL11.glNormal3f(0.0F, 0.0F, -1.0F);
				GL11.glRotated(180, 0, 0, 1);
			}

			if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				ScanResult scan = doScan(stack, player);
				if (scan != null)
				{
					AspectList aspects = null;
					GL11.glScalef(0.45f,0.45f,0.45f);
					GL11.glRotated(-50, 1, 0, 0);
					GL11.glTranslatef(0.0F,-0.2F,-1.8F);
					String text = "?";
					ItemStack scanStack = null;
					if (scan.id > 0)
					{
						scanStack = new ItemStack(Item.getItemById(scan.id), 1, scan.meta);
						if (ScanManager.hasBeenScanned(player, scan)) {
							aspects = ScanManager.getScanAspects(scan, player.worldObj);
						}
					}
					if (scan.type == 2)
					{
						if ((scan.entity instanceof EntityItem)) {
							scanStack = ((EntityItem)scan.entity).getEntityItem();
						} else {
							text = scan.entity.getCommandSenderName();
						}
						if (ScanManager.hasBeenScanned(player, scan)) {
							aspects = ScanManager.getScanAspects(scan, player.worldObj);
						}
					}
					if ((scan.type == 3) && 
							(scan.phenomena.startsWith("NODE")) && (ScanManager.hasBeenScanned(player, scan)))
					{
						MovingObjectPosition mop = null;
						if ((scanStack != null) && (scanStack.getItem() != null)) {
							mop = EntityUtils.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
						}
						if ((mop != null) && (mop.typeOfHit == MovingObjectType.BLOCK))
						{
							TileEntity tile = player.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
							if ((tile != null) && ((tile instanceof INode)))
							{
								aspects = ((INode)tile).getAspects();

								GL11.glPushMatrix();
								GL11.glEnable(3042);
								GL11.glBlendFunc(770, 1);
								String t = StatCollector.translateToLocal("nodetype." + ((INode)tile).getNodeType() + ".name");
								if (((INode)tile).getNodeModifier() != null) {
									t = t + ", " + StatCollector.translateToLocal(new StringBuilder().append("nodemod.").append(((INode)tile).getNodeModifier()).append(".name").toString());
								}
								int sw = mc.fontRenderer.getStringWidth(t);
								float scale = 0.004F;
								GL11.glScalef(scale, scale, scale);
								mc.fontRenderer.drawString(t, -sw / 2, -40, 16777215,true);
								GL11.glDisable(3042);
								GL11.glPopMatrix();
							}
						}
					}
					if (scanStack != null) {
						if (scanStack.getItem() != null)
							text = scanStack.getDisplayName();
					}
					if (aspects != null)
					{
						int posX = 0;
						int posY = 0;
						int aa = aspects.size();
						int baseX = Math.min(5, aa) * 8;
						for (Aspect aspect : aspects.getAspectsSorted())
						{
							GL11.glPushMatrix();
							GL11.glScalef(0.0075F, 0.0075F, 0.0075F);
							int j = (int)(190.0F + MathHelper.sin(posX + player.ticksExisted - player.worldObj.rand.nextInt(2)) * 10.0F + 10.0F);
							int k = j % 65536;
							int l = j / 65536;
							OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
							UtilsFX.drawTag(-baseX + posX * 16, -8 + posY * 16, aspect, aspects.getAmount(aspect), 0, 0.01D, 1, 1.0F, false);
							GL11.glPopMatrix();
							posX++;
							if (posX >= 5 - posY)
							{
								posX = 0;
								posY++;
								aa -= 5 - posY;
								baseX = Math.min(5 - posY, aa) * 8;
							}
						}
					}
					if (text == null) {
						text = "?";
					}
					if (text.length() > 0)
					{
						GL11.glPushMatrix();

						GL11.glEnable(3042);
						GL11.glBlendFunc(770, 1);
						GL11.glTranslatef(0.0F, -0.25F, 0.0F);
						int sw = mc.fontRenderer.getStringWidth(text);
						float scale = 0.005F;
						if (sw > 90) {
							scale -= 2.5E-005F * (sw - 90);
						}
						GL11.glScalef(scale, scale, scale);
						mc.fontRenderer.drawString(text, -sw / 2, 0, 16777215,true);
						GL11.glDisable(3042);
						GL11.glPopMatrix();
					}
				}
			}
			//		int timerSteps = entityclientplayermp.ticksExisted % 68;
			//		double step = 1.0/9.0;
			//		int frame = timerSteps<60 ? 0 : timerSteps-59;

			GL11.glDisable(3042);


			GL11.glPopMatrix();

			//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			////		RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private ScanResult doScan(ItemStack stack, EntityPlayer p)
	{
		if ((stack == null) || (p == null)) {
			return null;
		}
		Entity pointedEntity = EntityUtils.getPointedEntity(p.worldObj, p, 0.5D, 10.0D, 0.0F, true);
		if (pointedEntity != null)
		{
			ScanResult sr = new ScanResult((byte)2, 0, 0, pointedEntity, "");
			return sr;
		}
		MovingObjectPosition mop = EntityUtils.getMovingObjectPositionFromPlayer(p.worldObj, p, true);
		if ((mop != null) && (mop.typeOfHit == MovingObjectType.BLOCK))
		{
			int bi = Block.getIdFromBlock(p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ));

			TileEntity tile = p.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
			if ((tile != null) && ((tile instanceof INode)))
			{
				int md = p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ).getDamageValue(p.worldObj, mop.blockX, mop.blockY, mop.blockZ);
				ScanResult sr = new ScanResult((byte)3, bi, md, null, "NODE" + ((INode)tile).getId());
				return sr;
			}
			if (p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) != null)
			{
				ItemStack is = Utilities.getPickedBlock(p.worldObj, mop.blockX, mop.blockY, mop.blockZ);

				ScanResult sr = null;
				int md = p.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
				try
				{
					if (is == null) {
						is = BlockUtils.createStackedBlock(p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ), md);
					}
				}
				catch (Exception e) {}
				if (is == null) {
					sr = new ScanResult((byte)1, bi, md, null, "");
				} else {
					sr = new ScanResult((byte)1, Item.getIdFromItem(is.getItem()), is.getItemDamage(), null, "");
				}
				return sr;
			}
		}
		for (IScanEventHandler seh : ThaumcraftApi.scanEventhandlers)
		{
			ScanResult scan = seh.scanPhenomena(stack, p.worldObj, p);
			if (scan != null) {
				return scan;
			}
		}
		return null;
	}
}