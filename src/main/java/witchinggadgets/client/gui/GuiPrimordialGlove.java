package witchinggadgets.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.gui.ContainerPrimordialGlove;

public class GuiPrimordialGlove extends GuiContainer
{

	String tx1 = "textures/misc/node.png";
	String tx_c_n = "textures/misc/node_core_normal.png";
	String tx_c_d = "textures/misc/node_core_dark.png";
	String tx_c_u = "textures/misc/node_core_unstable.png";
	String tx_c_t = "textures/misc/node_core_taint.png";
	String tx_c_p = "textures/misc/node_core_pure.png";
	String tx_c_h = "textures/misc/node_core_hungry.png";

	InventoryPlayer invPlayer;
	EntityPlayer player;
	protected Slot theSlot;

	public GuiPrimordialGlove(InventoryPlayer inventoryPlayer, World world, int x, int y, int z)
	{
		super(new ContainerPrimordialGlove(inventoryPlayer, world, x, y, z));
		invPlayer = inventoryPlayer;
		player = invPlayer.player;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		if( (par1>=k+75 && par1<k+75+26) && (par2>=l+26 && par2<l+26+26) )
		{
			AspectList aspects = new AspectList();
			ItemStack bracelet = player.getCurrentEquippedItem();
			if(bracelet!=null && bracelet.hasTagCompound() && bracelet.getTagCompound().hasKey("storedNode"))
			{
				NBTTagCompound nodeTag = bracelet.getTagCompound().getCompoundTag("storedNode");
				NodeType type = NodeType.values()[nodeTag.getInteger("type")];
				NodeModifier modifier = NodeModifier.values()[nodeTag.getInteger("modifier")];
				aspects.readFromNBT(nodeTag);
				ArrayList<String> nodeInfo = new ArrayList<String>();
				String s = "\u00A7"+ClientUtilities.nodeTypeChatColour[type.ordinal()]+StatCollector.translateToLocal("nodetype." + type + ".name")+"\u00A77";
				if(modifier != null)
					s = s+", \u00A7"+ClientUtilities.nodeModifierChatColour[modifier.ordinal()]+StatCollector.translateToLocal("nodemod." + modifier + ".name")+"\u00A7r";
				nodeInfo.add(s);
				nodeInfo.add("  \u00A75"+StatCollector.translateToLocal("wg.gui.visSize")+": "+aspects.visSize());

				if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					for(Aspect a: aspects.getAspects())
						nodeInfo.add("   "+a.getName()+" "+aspects.getAmount(a)+"\u00A7r");
				else
					nodeInfo.add("  "+StatCollector.translateToLocal("wg.gui.shiftForAspectList"));

				UtilsFX.drawCustomTooltip(this, itemRender, this.fontRendererObj, nodeInfo, par1-k, par2-l, 7);
				RenderHelper.enableStandardItemLighting();
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glEnable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/primordialGlove.png");
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		AspectList aspects = new AspectList();
		ItemStack bracelet = player.getCurrentEquippedItem();
		if(bracelet!=null && bracelet.hasTagCompound())
		{
			if(bracelet.getTagCompound().hasKey("storedNode"))
			{
				NBTTagCompound nodeTag = bracelet.getTagCompound().getCompoundTag("storedNode");
				int nodeType = nodeTag.getInteger("type");
				aspects.readFromNBT(nodeTag);

				ClientUtilities.bindTexture("thaumcraft:textures/misc/nodes.png");

				Tessellator tes = Tessellator.instance;
				int count = 0;
				float average = aspects.visSize()/(aspects.size()>0?aspects.size():1);
				for(Aspect a : aspects.aspects.keySet())
				{
					//float radius = 16* (aspects.getAmount(a)/average)  * ((.5f-1)+((System.currentTimeMillis()%64)/64f));
					float mod = 2f * (((System.currentTimeMillis()+count*512)%4096)/4096f);
					if(mod>1)
						mod = 2-mod;
					float radius = 10+ 8*mod*(aspects.getAmount(a)/average);
					int perm = (int) ((System.currentTimeMillis()/64)%32)+count*4;
					GL11.glPushMatrix();
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);

					tes.startDrawingQuads();
					tes.setColorRGBA_I(a.getColor(), 64);
					tes.addVertexWithUV(k+88-radius, l+39+radius, zLevel, (perm+0)*.03125, 1/32f);
					tes.addVertexWithUV(k+88+radius, l+39+radius, zLevel, (perm+1)*.03125, 1/32f);
					tes.addVertexWithUV(k+88+radius, l+39-radius, zLevel, (perm+1)*.03125, 0);
					tes.addVertexWithUV(k+88-radius, l+39-radius, zLevel, (perm+0)*.03125, 0);
					tes.draw();

					GL11.glDisable(3042);
					GL11.glPopMatrix();
					count++;
				}


				GL11.glPushMatrix();
				GL11.glEnable(3042);
				if(nodeType!=0)
					GL11.glBlendFunc(770, nodeType==3||nodeType==4?771 : 1);

				float radius = 10;
				int perm = (int) ((System.currentTimeMillis()/64)%32);
				int overl = nodeType==2?6: nodeType==3?2: nodeType==4?5: nodeType==5?4: nodeType==6?3: 1;

				tes.startDrawingQuads();
				tes.addVertexWithUV(k+88-radius, l+39+radius, zLevel+100, (perm+0)*.03125, (overl+1)*.03125);
				tes.addVertexWithUV(k+88+radius, l+39+radius, zLevel+100, (perm+1)*.03125, (overl+1)*.03125);
				tes.addVertexWithUV(k+88+radius, l+39-radius, zLevel+100, (perm+1)*.03125, (overl+0)*.03125);
				tes.addVertexWithUV(k+88-radius, l+39-radius, zLevel+100, (perm+0)*.03125, (overl+0)*.03125);
				tes.draw();

				GL11.glDisable(3042);
				GL11.glPopMatrix();
			}
		}
	}
}