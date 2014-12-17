package witchinggadgets.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanResult;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ScanManager;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.util.Utilities;

public class ItemRenderMaterial implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type)
	{
		int meta = stack.getItemDamage();
		if(meta != 9 && meta != 10)
			return false;
		switch(type)
		{
		case ENTITY:
			return true;
		case EQUIPPED:
			return false;
		case EQUIPPED_FIRST_PERSON:
			return true;
		case FIRST_PERSON_MAP:
			return false;
		case INVENTORY:
			return false;
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
	{
		if(stack==null)
			return;
		String itemTexture = "witchingGadgets:textures/items/mat_"+(stack.getItemDamage()==9?"photoPlate.png":"developedPhoto.png");

		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP entityclientplayermp = mc.thePlayer;

		ItemStack scanStack = null;
		Entity scanEntity = null;
		int aspectColour=0xffffff;
		if(stack.getTagCompound()!=null)
		{
			ScanResult scan = Utilities.readScanResultFromNBT(stack.getTagCompound().getCompoundTag("scanResult"));
			AspectList scanAspects = new AspectList();
			if(scan!=null)
			{
				switch(scan.type)
				{
				case 1:
					scanStack = new ItemStack(Item.getItemById(scan.id), 1, scan.meta);
					scanAspects = ThaumcraftCraftingManager.getObjectTags(scanStack);
					scanAspects = ThaumcraftCraftingManager.getBonusTags(scanStack, scanAspects);
					break;
				case 2:
					if ((scan.entity instanceof EntityItem))
					{
						scanStack = new ItemStack(((EntityItem)scan.entity).getEntityItem().getItem(), 1, ((EntityItem)scan.entity).getEntityItem().getItemDamage());
						scanAspects = ThaumcraftCraftingManager.getObjectTags(scanStack);
						scanAspects = ThaumcraftCraftingManager.getBonusTags(scanStack, scanAspects);
					}
					else
					{
						scanEntity = scan.entity;
						scanAspects = ScanManager.generateEntityAspects(scan.entity);
					}
					break;
				case 3:
					if(scan.phenomena.startsWith("NODE"))
						scanAspects = Utilities.generateNodeAspects(entityclientplayermp.worldObj, scan.phenomena.replace("NODE", ""));
					break;
				}
			}

			int asp = entityclientplayermp.ticksExisted % (68*scanAspects.size()) / 68;
			aspectColour = scanAspects.getAspectsSorted()[asp].getColor();
		}

		GL11.glPushMatrix();
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			float timer = UtilsFX.getTimer(mc).renderPartialTicks;
			float f1 = UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer) + (UtilsFX.getEquippedProgress(mc.entityRenderer.itemRenderer) - UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer)) * timer;
			float f2 = entityclientplayermp.prevRotationPitch + (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * timer;

			//TRANSFORMS BEFORE CUSTOM RENDER
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			//GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
			//GL11.glRotatef(335.0F, 0.0F, 0.0F, -1.0F);
			GL11.glRotatef(45.0F, 0.0F, -1.0F, 0.0F);
			GL11.glScalef(2f,2f,2f);
			GL11.glTranslatef(-0.35F, 0.875F, 1.0F);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);

			float f6;
			float f7;
			float f8;
			float f9;
			float f10;
			float f11;
			float f12 = 0.8F;


			int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entityclientplayermp.posX), MathHelper.floor_double(entityclientplayermp.posY), MathHelper.floor_double(entityclientplayermp.posZ), 0);
			int k = i / 65536;
			//
			Render render;
			RenderPlayer renderplayer;

			f12 = 0.8F;
			f7 = entityclientplayermp.getSwingProgress(timer);
			f8 = MathHelper.sin(f7 * (float)Math.PI);
			f6 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
			GL11.glTranslatef(-f6 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI * 2.0F) * 0.2F, -f8 * 0.2F);
			f7 = 1.0F - f2 / 45.0F + 0.1F;

			if (f7 < 0.0F)
			{
				f7 = 0.0F;
			}

			if (f7 > 1.0F)
			{
				f7 = 1.0F;
			}

			f7 = -MathHelper.cos(f7 * (float)Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * f12 - (1.0F - f1) * 1.2F - f7 * 0.5F + 0.04F, -0.9F * f12);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(f7 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			ClientUtilities.bindTexture(entityclientplayermp.getLocationSkin().getResourceDomain()+":"+entityclientplayermp.getLocationSkin().getResourcePath());

			for (k = 0; k < 2; ++k)
			{
				int l = k * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * l);
				GL11.glRotatef(-45 * l, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-65 * l, 0.0F, 1.0F, 0.0F);
				render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
				renderplayer = (RenderPlayer)render;
				f11 = 1.0F;
				GL11.glScalef(f11, f11, f11);
				renderplayer.renderFirstPersonArm(mc.thePlayer);
				GL11.glPopMatrix();
			}

			f8 = entityclientplayermp.getSwingProgress(timer);
			f6 = MathHelper.sin(f8 * f8 * (float)Math.PI);
			f9 = MathHelper.sin(MathHelper.sqrt_float(f8) * (float)Math.PI);
			GL11.glRotatef(-f6 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f9 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f9 * 80.0F, 1.0F, 0.0F, 0.0F);
			f10 = 0.38F;
			GL11.glScalef(f10, f10, f10);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			f11 = 0.015625F;
			GL11.glScalef(f11, f11, f11);

			ClientUtilities.bindTexture(itemTexture);
			Tessellator tessellator = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);

			//		int timerSteps = entityclientplayermp.ticksExisted % 68;
			//		double step = 1.0/9.0;
			//		int frame = timerSteps<60 ? 0 : timerSteps-59;

			GL11.glEnable(3042);
			GL11.glBlendFunc(770,771);
			tessellator.startDrawingQuads();
			byte b0 = 7;
			//		tessellator.addVertexWithUV(0   - b0, 128 + b0, 0.0D, 0.0D, frame*step);
			//		tessellator.addVertexWithUV(128 + b0, 128 + b0, 0.0D, 1.0D, frame*step);
			//		tessellator.addVertexWithUV(128 + b0, 0   - b0, 0.0D, 1.0D, (frame-1)*step);
			//		tessellator.addVertexWithUV(0   - b0, 0   - b0, 0.0D, 0.0D, (frame-1)*step);
			//		tessellator.draw();
			IIcon ic = stack.getIconIndex();
			ClientUtilities.bindTexture(mc.getTextureManager().getResourceLocation(stack.getItemSpriteNumber()).getResourceDomain()+":"+mc.getTextureManager().getResourceLocation(stack.getItemSpriteNumber()).getResourcePath());
			tessellator.addVertexWithUV(0   - b0, 128 + b0, 0.0D, ic.getMinU(), ic.getMaxV());
			tessellator.addVertexWithUV(128 + b0, 128 + b0, 0.0D, ic.getMaxU(), ic.getMaxV());
			tessellator.addVertexWithUV(128 + b0, 0   - b0, 0.0D, ic.getMaxU(), ic.getMinV());
			tessellator.addVertexWithUV(0   - b0, 0   - b0, 0.0D, ic.getMinU(), ic.getMinV());
			tessellator.draw();
		}
		else
		{
			IIcon ic = stack.getIconIndex();
			float f = ic.getMinU();
			float f1 = ic.getMaxU();
			float f2 = ic.getMinV();
			float f3 = ic.getMaxV();
			GL11.glRotatef(90,0,1,0);
			GL11.glTranslatef(-.5f,-.5f, 0);
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, ic.getIconWidth(), ic.getIconHeight(), 0.0625F);

		}


		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glColor4f((aspectColour>>16&255)/255f, (aspectColour>>8&255)/255f,(aspectColour&255)/255f, (aspectColour>>32&255)/255f);
			IIcon ic = stack.getItem().getIconFromDamageForRenderPass(stack.getItemDamage(), 99);
			ClientUtilities.bindTexture(mc.getTextureManager().getResourceLocation(stack.getItemSpriteNumber()).getResourceDomain()+":"+mc.getTextureManager().getResourceLocation(stack.getItemSpriteNumber()).getResourcePath());

			byte b0 = 7;
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0   - b0, 128 + b0, 0.0D, ic.getMinU(), ic.getMaxV());
			tessellator.addVertexWithUV(128 + b0, 128 + b0, 0.0D, ic.getMaxU(), ic.getMaxV());
			tessellator.addVertexWithUV(128 + b0, 0   - b0, 0.0D, ic.getMaxU(), ic.getMinV());
			tessellator.addVertexWithUV(0   - b0, 0   - b0, 0.0D, ic.getMinU(), ic.getMinV());
			tessellator.draw();
			GL11.glColor4d(1,1,1,1);
			GL11.glDisable(3042);
		}

		if(scanStack!=null)
		{
			ItemRenderer ir = RenderManager.instance.itemRenderer;

			if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)
				GL11.glScalef(20, 20, 20);
			else
			{
				GL11.glScalef(.25f,-.25f,-.25f);
				GL11.glTranslatef(-1.25f,-4.5f,1.625f);
				GL11.glRotatef(-25, 1,0,0);
			}
			if(scanStack.getItem() instanceof ItemBlock)
			{
				GL11.glRotatef(-90, 1,0,0);
				GL11.glRotatef(-90, 0,1,0);
				GL11.glRotatef(65f, 0,0,1);
				GL11.glTranslatef(1.5f,-2.4f,-3.25f);
			}
			else if(MinecraftForgeClient.getItemRenderer(scanStack, ItemRenderType.INVENTORY)==null)
			{
				GL11.glRotatef(180f, 1,0,0);
				GL11.glRotatef(-45f, 0,1,0);
				GL11.glRotatef(-35f, 0,0,1);
				GL11.glTranslatef(3.625f,-1.5f,-1.75f);
				GL11.glScalef(-1,1,-1);
			}
			else
			{
				GL11.glRotatef(-100, 1,0,0);
				GL11.glRotatef(-60, 0,1,0);
				GL11.glRotatef(30, 0,0,1);
				GL11.glRotatef(20, 0,1,0);
				GL11.glTranslatef(3f,-2.5f,-.5f);
			}
			for(int p=0; p<scanStack.getItem().getRenderPasses(scanStack.getItemDamage()); p++)
			{
				GL11.glPushMatrix();
				int col = scanStack.getItem().getColorFromItemStack(scanStack, p);
				GL11.glColor4f((col>>16&255)/255f, (col>>8&255)/255f,(col&255)/255f, (col>>32&255)/255f);
				ir.renderItem(entityclientplayermp, scanStack, p, ItemRenderType.INVENTORY);
				GL11.glPopMatrix();
			}
		}
		else if(scanEntity!=null)
		{
			Render entRender = RenderManager.instance.getEntityRenderObject(scanEntity);
			float scaleMod = Math.max(scanEntity.height/1.8f, scanEntity.width/1.5f);
			if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				GL11.glRotatef(180, 1,0,0);
				GL11.glTranslatef(65,-85,2);
				GL11.glScalef(20/scaleMod, 20/scaleMod, 0.1f);
			}
			else
			{
				GL11.glTranslatef(.5f,.25f,.05f);
				GL11.glScalef(.25f/scaleMod,.25f/scaleMod,.1f/scaleMod);
			}
			if(scanEntity instanceof EntityDragon)
			{
				GL11.glScalef(2,2,1);
				GL11.glRotatef(180, 0,1,0);
				GL11.glTranslatef(0,2,0);
			}

			entRender.doRender(scanEntity, 0, 0, 0, 0, 0);
			if(scanEntity instanceof IBossDisplayData)
			{
				IBossDisplayData boss = (IBossDisplayData)scanEntity;
				if(BossStatus.bossName.equals(boss.func_145748_c_().getFormattedText()) && BossStatus.healthScale==(boss.getHealth()/boss.getMaxHealth()))
					if(BossStatus.statusBarTime>0)
						BossStatus.statusBarTime=0;
			}
		}

		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}
}