package witchinggadgets.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.tiles.TileEntityMagicalTileLock;
import witchinggadgets.common.util.network.message.MessageTileUpdate;

public class GuiMagicalTileLock extends GuiScreen
{
	List<Integer> stored = new ArrayList();
	public static final HashMap<String,int[]> presets = new HashMap();
	static{
		presets.put("0,7,6,2,4,3,5,1", new int[]{7,4,5,7,4,6,3,5,6,4,7,2,1,6,2,7,4,2,7,1,6,7,2,3,5,2,3,4,1,3,4,1});
		presets.put("5,2,3,7,0,1,6,4", new int[]{7,4,5,2,1,5,3,0,5,3,0,6,4,0,2,1,3,2,0,7,1,0,7,4,6,7,4,1,1,4,0,1});
		presets.put("5,2,6,1,7,0,4,3", new int[]{5,2,1,4,7,6,3,7,4,0,7,4,0,1,2,0,6,5,0,6,5,3,4,5,1,7,5,1,7,2,6,0});
		presets.put("0,5,6,1,2,3,4,7", new int[]{7,4,1,0,3,1,0,3,1,6,4,7,5,2,3,0,6,1,0,6,7,5,2,7,5,2,7,3,6,5,2,7});
		presets.put("4,1,2,7,0,6,3,5", new int[]{7,4,5,2,1,5,3,0,5,3,4,6,0,4,3,5,4,3,5,1,2,7,6,5,7,6,5,0,3,7,0,5});
		presets.put("4,0,2,6,3,1,7,5", new int[]{7,6,3,4,1,0,4,3,6,1,0,2,5,0,2,5,0,2,5,0,2,7,1,5,7,1,5,7,3,6,7,5});
		presets.put("2,4,0,7,5,6,3,1", new int[]{5,4,7,6,3,7,1,0,7,1,0,2,4,0,2,7,1,2,7,1,2,7,1,4,0,1,6,5,1,6,5,1});
		presets.put("6,2,4,0,7,5,1,3", new int[]{5,4,7,6,3,7,6,3,7,0,1,6,0,1,6,2,4,0,3,5,0,3,5,0,3,5,0,7,1,0,7,3});
		presets.put("6,4,0,5,2,7,1,3", new int[]{5,4,3,6,7,3,1,0,6,1,0,2,4,0,2,4,0,5,3,2,5,3,2,7,1,5,7,2,3,7,2,3});
		presets.put("4,3,5,7,6,2,1,0", new int[]{7,6,3,4,1,0,4,1,5,2,0,5,6,7,2,6,7,3,1,7,3,2,6,0,5,3,0,6,2,0,6,2});
		presets.put("5,1,4,2,7,0,6,3", new int[]{7,4,5,2,1,5,3,0,5,3,4,7,2,4,0,6,7,0,3,1,4,2,0,3,2,0,3,7,6,2,7,3});
		presets.put("4,7,0,6,3,5,1,2", new int[]{7,6,3,4,1,0,4,1,0,2,5,7,6,0,2,5,7,2,0,6,2,0,6,3,1,6,5,7,0,5,3,2});
		presets.put("3,1,2,7,4,5,6,0", new int[]{5,4,3,0,1,3,7,5,4,2,3,1,0,7,1,0,7,1,0,3,2,0,1,7,3,1,5,4,0,5,4,0});
		presets.put("7,4,0,5,2,3,1,6", new int[]{5,4,7,6,3,7,1,0,7,1,0,2,4,0,2,4,0,5,6,2,5,6,2,3,1,5,6,2,3,6,2,3});
		presets.put("6,5,2,7,1,0,3,4", new int[]{5,4,1,0,3,6,7,1,0,3,6,0,3,2,4,5,1,3,5,4,2,5,0,7,3,0,4,1,0,4,1,0});
		presets.put("3,7,0,6,4,1,5,2", new int[]{5,4,1,0,3,1,0,2,4,0,7,5,0,4,2,7,1,6,5,1,4,2,7,4,2,0,1,2,4,7,0,1});
		//										 2,5,6,4,7,0,1,2,5,7,4,3,0,1,2,5,
	}
	ResourceLocation texture = new ResourceLocation("witchinggadgets:textures/gui/tileLock.png");

	boolean unlocked = false;
	int step = -1;
	int[] solving = new int[0];
	public static GuiButtonMagicTile currentTile = null;
	TileEntityMagicalTileLock tileentity;
	int xSize = 128;
	int ySize = 128;

	public GuiMagicalTileLock(TileEntityMagicalTileLock tileentity)
	{
		currentTile = null;
		this.tileentity = tileentity;
		this.unlocked = this.tileentity.unlocked;
	}

	@Override
	public void initGui()
	{
		int guiLeft = (this.width-xSize)/2;
		int guiTop= (this.height-xSize)/2;
		this.buttonList.clear();
		int r = tileentity.lockPreset;
		for(int ii=0;ii<8;ii++)
		{
			int id = this.unlocked?ii:Integer.parseInt(presets.keySet().toArray(new String[0])[r].split(",")[ii]);
			this.buttonList.add(new GuiButtonMagicTile(id, guiLeft+19+(ii%3)*30,guiTop+19+(ii/3)*30));
		}
//		this.buttonList.add(new GuiButton(10, guiLeft+34,guiTop+128,60,20,"Print"));
//		this.buttonList.add(new GuiButton(11, guiLeft+34,guiTop-20,60,20,"Solve"));
	}

	@Override
	public void drawScreen(int mX, int mY, float f)
	{
		int guiLeft = (this.width-xSize)/2;
		int guiTop= (this.height-xSize)/2;
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft+0,guiTop+0, 0,0, xSize,xSize);
		if(!unlocked)
			GL11.glColor3f(.1f, 0, .2f);
		this.drawTexturedModalRect(guiLeft+19,guiTop+19, 128,30, 90,90);
		super.drawScreen(mX, mY, f);
		if(unlocked)
			this.drawCenteredString(fontRendererObj, "UNLOCKED", guiLeft+64, guiTop+6, 0xffffff);

		if(!unlocked)
		{
			if(solving!=null && step>=0 && step<solving.length)
				if(currentTile==null)
				{
					//System.out.println(solving[31-step]+",");
					for(int i=0;i<8;i++)
						if( ((GuiButton)buttonList.get(i)).id == solving[31-step] )
							this.actionPerformed( (GuiButton)buttonList.get(i));
					step++;
				}
			if(currentTile==null)
			{
				boolean b = true;
				this.tileentity.tiles = new byte[9];
				for(int i=0;i<8;i++)
				{
					GuiButton button = (GuiButton)buttonList.get(i);
					int bX = button.xPosition-guiLeft-19;
					int bY = button.yPosition-guiTop-19;
					if( bX != (button.id%3)*30 )
						b = false;
					if( bY != (button.id/3)*30 )
						b = false;
					if(bX==0&&bY==0)
						this.tileentity.tiles[0]=1;
					if(bX==30&&bY==0)
						this.tileentity.tiles[1]=1;
					if(bX==60&&bY==0)
						this.tileentity.tiles[2]=1;
					if(bX==0&&bY==30)
						this.tileentity.tiles[3]=1;
					if(bX==30&&bY==30)
						this.tileentity.tiles[4]=1;
					if(bX==60&&bY==30)
						this.tileentity.tiles[5]=1;
					if(bX==0&&bY==60)
						this.tileentity.tiles[6]=1;
					if(bX==30&&bY==60)
						this.tileentity.tiles[7]=1;
					if(bX==60&&bY==60)
						this.tileentity.tiles[8]=1;
				}
				if(b)
				{
					this.unlocked = true;
					this.tileentity.unlocked = true;
					this.tileentity.tick = 0;
					WitchingGadgets.packetHandler.sendToServer(new MessageTileUpdate(this.tileentity));
					//WGPacketPipeline.INSTANCE.sendToServer(new PacketTileUpdate(this.tileentity));
				}
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id==10)
		{
			for(int ii : stored)
				System.out.print(ii+",");
			System.out.println();
		}
		else if(button.id==11)
		{
			String iA = "";
			for(int ii=0;ii<8;ii++)
			{
				GuiButton b = (GuiButton) this.buttonList.get(ii);
				iA += b.id+",";
			}
			iA = iA.substring(0, 15);
			step=0;
			solving = presets.get(iA);
		}
		else if(currentTile==null && !unlocked)
		{
			GuiButtonMagicTile tile = (GuiButtonMagicTile) button;
			int guiLeft = (this.width-xSize)/2;
			int guiTop= (this.height-xSize)/2;
			int x = button.xPosition;
			int y = button.yPosition;
			boolean hasLeft=false;
			boolean hasRight=false;
			boolean hasTop=false;
			boolean hasBot=false;
			for(Object o : this.buttonList)
			{
				if( ((GuiButton)o).xPosition-x==-30 && ((GuiButton)o).yPosition==y)
					hasLeft=true;
				if( ((GuiButton)o).xPosition-x==30 && ((GuiButton)o).yPosition==y)
					hasRight=true;
				if( ((GuiButton)o).xPosition==x && ((GuiButton)o).yPosition-y==-30)
					hasTop=true;
				if( ((GuiButton)o).xPosition==x && ((GuiButton)o).yPosition-y==30)
					hasBot=true;
			}
			//System.out.println("top: "+hasTop+", bot: "+hasBot+", left: "+hasLeft+", right: "+hasRight);

			x -= guiLeft;
			y -= guiTop;
			boolean moveLeft = x>19 && !hasLeft;
			boolean moveRight = x<79 && !hasRight;
			boolean moveTop = y>19 && !hasTop;
			boolean moveBot = y<79 && !hasBot;

			tile.moveTop = moveTop;
			tile.moveBottom = moveBot;
			tile.moveLeft = moveLeft;
			tile.moveRight = moveRight;
			tile.moveProgress=1;
			currentTile = tile;

			if(moveTop||moveBot||moveLeft||moveRight)
				stored.add(button.id);
		}
	}

}