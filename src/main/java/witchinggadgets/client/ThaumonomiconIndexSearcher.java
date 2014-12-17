package witchinggadgets.client;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchRecipe;
import thaumcraft.common.config.ConfigItems;
import witchinggadgets.common.WGConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ThaumonomiconIndexSearcher
{
	final static int mouseBufferIdent = 17;
	static ByteBuffer mouseBuffer;

	final static int selectedCategoryIdent = 21;
	static Field f_selectedCategory = null;
	public static ThaumonomiconIndexSearcher instance;
	public static void init()
	{
		instance = new ThaumonomiconIndexSearcher();
		MinecraftForge.EVENT_BUS.register(instance);
		FMLCommonHandler.instance().bus().register(instance);

		if(mouseBuffer==null)
			try {
				Field f_buf = Mouse.class.getDeclaredFields()[mouseBufferIdent];
				if(!f_buf.getName().equalsIgnoreCase("readBuffer"))
					f_buf = Mouse.class.getDeclaredField("readBuffer");
				f_buf.setAccessible(true);
				mouseBuffer = (ByteBuffer) f_buf.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		try {
			f_selectedCategory = GuiResearchBrowser.class.getDeclaredFields()[selectedCategoryIdent];
			if(!f_selectedCategory.getName().equalsIgnoreCase("selectedCategory"))
				f_selectedCategory = GuiResearchBrowser.class.getDeclaredField("selectedCategory");
			f_selectedCategory.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static GuiTextField thaumSearchField;

	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
	{
		if(event.gui.getClass().getName().endsWith("GuiResearchBrowser"))
		{
			int guiLeft = event.gui.width/2;
			int guiTop = event.gui.height/2-110;
			//			System.out.println("heyo?");
			thaumSearchField = new GuiTextField(event.gui.mc.fontRenderer, guiLeft, guiTop, 103, 13);
			thaumSearchField.setTextColor(-1);
			thaumSearchField.setDisabledTextColour(-1);
			thaumSearchField.setEnableBackgroundDrawing(false);
			thaumSearchField.setMaxStringLength(40);
			Keyboard.enableRepeatEvents(true);
		}
	}

	@SubscribeEvent
	public void onGuiPreDraw(GuiScreenEvent.DrawScreenEvent.Pre event)
	{
		if(thaumSearchField!=null)
		{
			boolean cont = mouseBuffer.hasRemaining();
			int to=0;
			if(Mouse.isCreated())
				while(cont&&to<40)	
				{
					to++;
					int mx = Mouse.getEventX() * event.gui.width / event.gui.mc.displayWidth;
					int my = event.gui.height - Mouse.getEventY() * event.gui.height / event.gui.mc.displayHeight - 1;
					int button = Mouse.getEventButton();
					int wheel = Mouse.getEventDWheel();
					if(Mouse.getEventButtonState())
					{
						thaumSearchField.mouseClicked(mx, my, button);
						if(thaumSearchField.isFocused() && button==1)
						{
							thaumSearchField.setText("");
							searchResults.clear();
						}
						else if(mx>(event.gui.width/2+128+(ResearchCategories.researchCategories.size()>9?24:2)) && my>event.gui.height/2-115&&my<event.gui.height/2+115)
						{
							int clicked = my-(event.gui.height/2-109);
							clicked /= 11;
							int selected = clicked+listDisplayOffset;
							if(selected<searchResults.size())
							{
								ResearchItem item  = ResearchCategories.getResearch(searchResults.get(selected).research);
								event.gui.mc.displayGuiScreen(new GuiResearchRecipe(item,0,item.displayColumn,item.displayRow));
							}
							return;
						}
						cont = mouseBuffer.hasRemaining();
						return;
					} else if(wheel!=0 && mx>(event.gui.width/2+128+(ResearchCategories.researchCategories.size()>9?24:2)))
					{
						if(wheel<0)
							listDisplayOffset++;
						else
							listDisplayOffset--;
						if(listDisplayOffset>searchResults.size()-20)
							listDisplayOffset=searchResults.size()-20;
						if(listDisplayOffset<0)
							listDisplayOffset=0;
						return;
					}
					else
						return;
				}
		}
	}
	static int listDisplayOffset = 0;
	@SubscribeEvent
	public void onGuiPostDraw(GuiScreenEvent.DrawScreenEvent.Post event)
	{
		if(thaumSearchField!=null)
		{
			int x = event.gui.width/2+128+(ResearchCategories.researchCategories.size()>9?24:0);
			int y = event.gui.height/2-115;
			int maxWidth = Math.min(event.gui.width-x, 224);

			if(!searchResults.isEmpty())
			{
				ClientUtilities.bindTexture("thaumcraft:textures/misc/parchment3.png");
				GL11.glEnable(3042);
				Tessellator tes = Tessellator.instance;
				tes.startDrawingQuads();
				tes.setColorOpaque_I(0xffffff);
				tes.addVertexWithUV(x, y+230, 0, 0, 150/256f);
				tes.addVertexWithUV(x+maxWidth, y+230, 0, 150/256f, 150/256f);
				tes.addVertexWithUV(x+maxWidth, y, 0, 150/256f, 0);
				tes.addVertexWithUV(x, y, 0, 0, 0);
				tes.draw();
			}
			ClientUtilities.bindTexture("thaumcraft:textures/gui/guiresearchtable2.png");
			event.gui.drawTexturedModalRect(thaumSearchField.xPosition-2, thaumSearchField.yPosition-4, 94,8, thaumSearchField.width+8, thaumSearchField.height);
			event.gui.drawTexturedModalRect(thaumSearchField.xPosition-2, thaumSearchField.yPosition+thaumSearchField.height-4, 138,158, thaumSearchField.width+8, 2);
			event.gui.drawTexturedModalRect(thaumSearchField.xPosition+thaumSearchField.width+6, thaumSearchField.yPosition-4, 244,136, 2, thaumSearchField.height+2);

			if((searchResults==null||searchResults.isEmpty())&&!thaumSearchField.isFocused())
				event.gui.drawString(event.gui.mc.fontRenderer, StatCollector.translateToLocal("wg.gui.search"), thaumSearchField.xPosition, thaumSearchField.yPosition, 0x777777);
			else
				for(int i=0; i<20; i++)
					if(i+listDisplayOffset<searchResults.size())
					{
						String name = searchResults.get(listDisplayOffset+i).display!=null?searchResults.get(listDisplayOffset+i).display:ResearchCategories.getResearch(searchResults.get(listDisplayOffset+i).research).getName();
						name = searchResults.get(listDisplayOffset+i).modifier + event.gui.mc.fontRenderer.trimStringToWidth(name, maxWidth-10);
						event.gui.mc.fontRenderer.drawString(name, x+6, y+6+i*11, 0xffffff, false);
					}

			thaumSearchField.drawTextBox();
		}
	}
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		if(thaumSearchField!=null)
		{
			thaumSearchField=null;
			Keyboard.enableRepeatEvents(false);
		}
	}

	@SubscribeEvent
	public void renderTick(TickEvent.ClientTickEvent event)
	{
		if(thaumSearchField!=null)
			if(Keyboard.isCreated() && thaumSearchField.isFocused())
				while(Keyboard.next())
					if(Keyboard.getEventKeyState())
					{
						if(Keyboard.getEventKey()==1)
							Minecraft.getMinecraft().displayGuiScreen(null);
						else
						{
							thaumSearchField.textboxKeyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
							listDisplayOffset=0;
							if(WGConfig.limitBookSearchToCategory)
								searchCategory = getActiveCategory();
							buildEntryList(thaumSearchField.getText());
						}
					}
	}


	static String searchCategory;
	static String lastKeyword;
	static List<SearchQuery> searchResults = new ArrayList();
	static void buildEntryList(String query)
	{
		if(query==null||query.isEmpty())
		{
			searchResults.clear();
			return;
		}
		query = query.toLowerCase();
		List<SearchQuery> valids = new ArrayList();
		Set<String> keys;
		if(searchCategory!=null&&!searchCategory.isEmpty())
			keys = ResearchCategories.getResearchList(searchCategory).research.keySet();
		else
		{
			keys = new HashSet<String>();
			for(ResearchCategoryList cat : ResearchCategories.researchCategories.values())
				keys.addAll(cat.research.keySet());
		}

		Set<SearchQuery> recipeBased = new HashSet();
		Set<String> usedResearches = new HashSet();
		for(String key : keys)
			if(key!=null&&!key.isEmpty()&&ResearchCategories.getResearch(key)!=null&&ThaumcraftApiHelper.isResearchComplete(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), key))
			{
				if(ResearchCategories.getResearch(key).getName().startsWith("tc.research_name"))
					continue;
				recipeBased.clear();
				ResearchPage[] pages = ResearchCategories.getResearch(key).getPages();
				int iPage=0;
				if(pages!=null)
					for(ResearchPage page : pages)
					{
						if(page.recipeOutput!=null && page.recipeOutput.getDisplayName().toLowerCase().contains(query))
						{
							String dn = "";
							if(page.recipeOutput.getItem() == ConfigItems.itemGolemCore)
								for(String info : (List<String>)page.recipeOutput.getTooltip(Minecraft.getMinecraft().thePlayer, false))
									dn+=info+" ";
							else
								dn = page.recipeOutput.getDisplayName();
							if(!usedResearches.contains(dn))
							{
								recipeBased.add(new SearchQuery(key,"Item: "+dn,iPage));
								usedResearches.add(dn);
							}
						}
						iPage++;
					}
				boolean rAdded=false;
				if(recipeBased.size()<=1)
				{
					if(!usedResearches.contains(ResearchCategories.getResearch(key).getName()))
						if(key.toLowerCase().contains(query) || ResearchCategories.getResearch(key).getName().toLowerCase().contains(query))
						{
							valids.add(new SearchQuery(key,null,0));
							usedResearches.add(ResearchCategories.getResearch(key).getName());
							rAdded=true;
						}
				}
				if(!rAdded)
					valids.addAll(recipeBased);
			}
		Collections.sort(valids, ResearchSorter.instance);
		searchResults=valids;
	}
	static String getActiveCategory()
	{
		String s = null;
		try{
			s= (String)f_selectedCategory.get(null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return s;
	}
	static class ResearchSorter implements Comparator<SearchQuery>
	{
		static ResearchSorter instance = new ResearchSorter();
		@Override
		public int compare(SearchQuery o1, SearchQuery o2)
		{
			String c1 = o1.display!=null?o1.display:ResearchCategories.getResearch(o1.research).getName();
			String c2 = o2.display!=null?o2.display:ResearchCategories.getResearch(o2.research).getName();
			return c1.compareToIgnoreCase(c2);
		}
	}
	static class SearchQuery
	{
		public final String research;
		public final String display;
		public final int page;
		String modifier;
		public SearchQuery(String research, String display, int page)
		{
			this.research=research;
			this.display=display;
			this.page=page;
			modifier = display!=null?EnumChatFormatting.DARK_GRAY.toString():"";
		}
	}
}