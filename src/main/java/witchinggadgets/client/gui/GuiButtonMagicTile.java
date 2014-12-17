package witchinggadgets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;

public class GuiButtonMagicTile extends GuiButton
{
	public boolean moveTop = false;
	public boolean moveBottom = false;
	public boolean moveLeft = false;
	public boolean moveRight = false;
	int moveProgress = 0;

	public GuiButtonMagicTile(int id, int x, int y)
	{
		super(id, x,y, 30,30, ""+id);
	}

	@Override
	public void drawButton(Minecraft mc, int mX, int mY)
	{
		if(moveProgress>0&&moveProgress<16)
		{
			if(moveTop)
				this.yPosition-=2;
			else if(moveBottom)
				this.yPosition+=2;
			else if(moveLeft)
				this.xPosition-=2;
			else if(moveRight)
				this.xPosition+=2;
			moveProgress++;
		}
		else if(GuiMagicalTileLock.currentTile==this)
		{
			moveProgress = 0;
			moveTop = moveBottom = moveLeft = moveRight = false;
			GuiMagicalTileLock.currentTile=null;
		}
		
		if (this.visible)
		{
			FontRenderer fontrenderer = mc.fontRenderer;
			//mc.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = mX >= this.xPosition && mY >= this.yPosition && mX < this.xPosition + this.width && mY < this.yPosition + this.height;
			int k = this.getHoverState(this.field_146123_n);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 128,0, this.width, this.height);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 128+(id%3)*30,30+(id/3)*30, this.width, this.height);
			this.mouseDragged(mc, mX, mY);
			int l = 14737632;

			if (packedFGColour != 0)
			{
				l = packedFGColour;
			}
			else if (!this.enabled)
			{
				l = 10526880;
			}
			else if (this.field_146123_n)
			{
				l = 16777120;
			}

			//this.drawCenteredString(fontrenderer, ""+this.id, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
		}
	}
}