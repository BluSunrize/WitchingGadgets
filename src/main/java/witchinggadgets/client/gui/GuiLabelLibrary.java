package witchinggadgets.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityLabelLibrary;
import witchinggadgets.common.gui.ContainerLabelLibrary;
import witchinggadgets.common.util.network.PacketTileUpdate;
import witchinggadgets.common.util.network.WGPacketPipeline;

public class GuiLabelLibrary extends GuiContainer
{
	private TileEntityLabelLibrary tile;
	private EntityPlayer player;

	public GuiLabelLibrary (InventoryPlayer inventoryPlayer, TileEntityLabelLibrary tileEntity)
	{
		super(new ContainerLabelLibrary(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
		this.player = inventoryPlayer.player;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mX, int mY)
	{
		//		if(this.tile.getOutput()!=null)
		//			this.drawVirtualItem(tile.getOutput(), 118, 24, false, mX, mY, true);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int mX, int mY)
	{
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);   
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/labelLibrary.png");
		//Inventory
		this.drawTexturedModalRect(guiLeft,guiTop-5, 0, 0, xSize+18, ySize+5);

		float scale = .65f;
		GL11.glScalef(scale,scale,1);
		int i=0;
		int x = (int)Math.floor((guiLeft+36)/scale);
		int y = (int)Math.floor((guiTop+8)/scale);
		int row = (int)(8/scale);

		for(Aspect a: Aspect.aspects.values())
			if(ThaumcraftApiHelper.hasDiscoveredAspect(player.getCommandSenderName(), a))
			{
				UtilsFX.drawTag(x+17*(i%row),y+17*(i/row), a, 0.0F, tile.aspect==a?1:0, zLevel);
				i++;
			}
		GL11.glScalef(1/scale,1/scale,1);

		UtilsFX.drawTag(guiLeft+8,guiTop+30, tile.aspect, 0.0F, 0, zLevel);

		//		if(this.tile.getOutput()!=null)
		//			this.drawVirtualItem(tile.getOutput(), guiLeft+118,guiTop+24, false, mX, mY, false);
	}

	@Override
	protected void mouseClicked(int mX, int mY, int button)
	{
		super.mouseClicked(mX, mY, button);
		mX-= ((width - xSize)/2+36);
		mY-= ((height - ySize)/2+8);

		float scale = .65f;
		int i=0;
		int x = (int)Math.floor((guiLeft+36)/scale);
		int y = (int)Math.floor((guiTop+8)/scale);
		int row = (int)(8/scale);
		int size = (int) Math.floor(17*scale);
		//		mX-= x;
		//		mY-= y;
		//System.out.println(mX+", "+mY);
		Aspect old = tile.aspect;
		for(Aspect a: Aspect.aspects.values())
			if(ThaumcraftApiHelper.hasDiscoveredAspect(player.getCommandSenderName(), a))
			{
				if(mX>=size*(i%row) && mX<size*(i%row+1))
				{
					//System.out.println("fitting X between "+size*(i%row)+" | "+size*(i%row+1)+", check Y between "+(size*(i/row)) +" | "+ (size*((i+row)/row)) );
					if(mY>=size*(i/row) && mY<size*((i+row)/row))
					{
						System.out.println(a.getTag());
						tile.aspect=a;
					}
				}
				i++;
			}
		if(tile.aspect != old)
			WGPacketPipeline.INSTANCE.sendToServer(new PacketTileUpdate(tile));

		if(mY>36 && mY<24)
		{
			//			int old = this.tile.aspect;
			//			if(mX>106 && mX<117)
			//				this.tile.targetGemCut = (byte) (old==0?4:0);
			////				this.tile.targetGemCut++;
			//			if(mX>59 && mX<70)
			//				this.tile.targetGemCut = (byte) (old==0?4:0);
			////				this.tile.targetGemCut--;
			////			if(this.tile.targetGemCut < 0)
			////				this.tile.targetGemCut = (byte) (ItemInfusedGem.GemCut.values().length-1);
			////			else if(this.tile.targetGemCut >= ItemInfusedGem.GemCut.values().length)
			////				this.tile.targetGemCut=0;
			//			
			//			if(this.tile.targetGemCut != old)
			//				WGPacketPipeline.INSTANCE.sendToServer(new PacketTileUpdate(this.tile));
		}
	}

	//	public void drawVirtualItem(ItemStack stack, int x, int y, boolean isDull, int mouseX, int mouseY, boolean foreground)
	//	{
	//		itemRender.renderWithColor = false;
	//		if(!foreground)
	//		{
	//			GL11.glPushMatrix();
	//			if(isDull)
	//				itemRender.renderWithColor = true;
	//			GL11.glEnable(2896);
	//			GL11.glEnable(2884);
	//			GL11.glEnable(3042);
	//			GL11.glColor4f(1,1,1, 0.2F);
	//			itemRender.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);
	//			itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, x, y);
	//			GL11.glDisable(3042);
	//			GL11.glDisable(2896);
	//			GL11.glPopMatrix();
	//		}
	//		else
	//		{
	//			GL11.glPushMatrix();
	//			int xx = mouseX - this.guiLeft - x;
	//			int yy = mouseY - this.guiTop - y;
	//			if ((xx >= 0) && (yy >= 0) && (xx < 16) && (yy < 16))
	//			{
	//				this.renderToolTip(stack, mouseX-guiLeft, mouseY-guiTop);
	//				List tooltip = stack.getTooltip(this.player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
	//				RenderHelper.enableGUIStandardItemLighting();
	//				//				WGGraphicUtilities.drawTooltipHoveringText(this, tooltip, mouseX-guiLeft, mouseY-guiTop, fontRendererObj, 0, 0);
	//				//				UtilsFX.drawCustomTooltip(this, itemRender, this.fontRendererObj, tooltip, mouseX-guiLeft, mouseY-guiTop, 11);
	//			}
	//			GL11.glPopMatrix();
	//			//			itemRender.renderWithCoolor = true;
	//		}
	//	}
}