package witchinggadgets.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.blocks.tiles.TileEntityLabelLibrary;
import witchinggadgets.common.gui.ContainerLabelLibrary;
import witchinggadgets.common.util.network.message.MessageTileUpdate;

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
		int row = (int)(8/scale);
		int size = (int) Math.floor(17*scale);
		Aspect old = tile.aspect;
		for(Aspect a: Aspect.aspects.values())
			if(ThaumcraftApiHelper.hasDiscoveredAspect(player.getCommandSenderName(), a))
			{
				if(mX>=size*(i%row) && mX<size*(i%row+1))
				{
					if(mY>=size*(i/row) && mY<size*((i+row)/row))
					{
						System.out.println(a.getTag());
						tile.aspect=a;
					}
				}
				i++;
			}
		if(tile.aspect != old)
			WitchingGadgets.packetHandler.sendToServer(new MessageTileUpdate(this.tile));
			//WGPacketPipeline.INSTANCE.sendToServer(new PacketTileUpdate(tile));
	}
}