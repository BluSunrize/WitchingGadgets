package witchinggadgets.client.gui;

import java.text.DecimalFormat;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.network.PacketRequestMigrate;
import witchinggadgets.common.util.network.PacketSetMigratePos;

public class GuiRavenCloak extends GuiScreen{

	EntityPlayer user;

	public GuiRavenCloak(EntityPlayer player, World world, int x, int y, int z)
	{
		super();
		user = player;
		this.width = 256;
		//this.height = 133;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);   
		ClientUtilities.bindTexture("witchinggadgets:textures/gui/cloakRaven.png");
		int x = this.width / 2;
		int y = this.height / 2;
		this.drawTexturedModalRect(x-128, y-166/2, 0, 0, 256, 166);

		super.drawScreen(par1, par2, par3);

		ItemStack cloak = user.getCurrentArmor(2);

		double migX;
		double migY;
		double migZ;
		String sMigX = "";
		String sMigY = "";
		String sMigZ = "";
		DecimalFormat df = new DecimalFormat("#");
		int col = 14737632;

		if(cloak != null && cloak.getTagCompound()!=null)
		{
			boolean flag = cloak.getTagCompound().getDouble("WG.migratePosY") < 50;

			migX= cloak.getTagCompound().getDouble("WG.migratePosX");
			migY= cloak.getTagCompound().getDouble("WG.migratePosY");
			migZ= cloak.getTagCompound().getDouble("WG.migratePosZ");
			sMigX = df.format(migX);
			sMigY = df.format(migY);
			sMigZ = df.format(migZ);

			if(Math.abs(migX) < 10000)sMigX = " "+sMigX;
			if(Math.abs(migX) < 1000)sMigX = " "+sMigX;
			if(Math.abs(migX) < 100)sMigX = " "+sMigX;
			if(Math.abs(migX) < 10)sMigX = " "+sMigX;
			if(migX >= 0)sMigX = " "+sMigX;

			if(Math.abs(migY) < 10000)sMigY = " "+sMigY;
			if(Math.abs(migY) < 1000)sMigY = ""+sMigY;
			if(Math.abs(migY) < 100)sMigY = " "+sMigY;
			if(Math.abs(migY) < 10)sMigY = " "+sMigY;
			if(migY >= 0)sMigY = " "+sMigY;

			if(Math.abs(migZ) < 10000)sMigZ = " "+sMigZ;
			if(Math.abs(migZ) < 1000)sMigZ = " "+sMigZ;
			if(Math.abs(migZ) < 100)sMigZ = " "+sMigZ;
			if(Math.abs(migZ) < 10)sMigZ = " "+sMigZ;
			if(migZ>= 0)sMigZ = " "+sMigZ;

			if(!cloak.getTagCompound().hasKey("WG.migratePosX") && flag)
				sMigX="/";
			if(!cloak.getTagCompound().hasKey("WG.migratePosY") && flag)
				sMigY="/";
			if(!cloak.getTagCompound().hasKey("WG.migratePosZ") && flag)
				sMigZ="/";
		}


		this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("wg.gui.textCurNest"), x , y-30 , col);

		this.drawCenteredString(this.fontRendererObj, sMigX, x, y-15 , col);
		this.drawCenteredString(this.fontRendererObj, sMigY, x, y-00 , col);
		this.drawCenteredString(this.fontRendererObj, sMigZ, x, y+15 , col);

	}

	@Override
	public void initGui()
	{
		this.buttonList.clear();

		ItemStack cloak = user.getCurrentArmor(2);
		boolean migFlag = false;

		int x2 =  this.width / 2;
		int y2 =  this.height / 2;

		GuiButton but0 = new GuiButton(0, x2-110, y2-10, 77, 20, StatCollector.translateToLocal("wg.gui.buttonMigrate"));
		GuiButton but1 = new GuiButton(1, x2+ 30, y2-10, 80, 20, StatCollector.translateToLocal("wg.gui.buttonSetNest"));

		if(user.posY < 50)
			but1.enabled = false;

		if(cloak == null || cloak.getTagCompound()==null)migFlag = true;
		if(!cloak.getTagCompound().hasKey("WG.migratePosX"))migFlag = true;
		if(!cloak.getTagCompound().hasKey("WG.migratePosY"))migFlag = true;
		if(!cloak.getTagCompound().hasKey("WG.migratePosZ"))migFlag = true;

		if(migFlag)
			but0.enabled=false;
		buttonList.add(but0);
		buttonList.add(but1);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		ItemStack cloak = Utilities.getActiveMagicalCloak(user);
		if(cloak == null)return;
		if(cloak.getTagCompound()==null)cloak.setTagCompound(new NBTTagCompound());

		NBTTagCompound tag = cloak.getTagCompound();

		switch(button.id)
		{
		case 0:
			double migX = tag.getDouble("WG.migratePosX");
			double migY = tag.getDouble("WG.migratePosY");
			double migZ = tag.getDouble("WG.migratePosZ");
			WitchingGadgets.packetPipeline.sendToServer(new PacketRequestMigrate(user,migX,migY,migZ));
			//this.mc.currentScreen = null;
			break;
		case 1:
			WitchingGadgets.packetPipeline.sendToServer(new PacketSetMigratePos(user));
			tag.setDouble("WG.migratePosX",Math.floor(user.posX)+0.5);
			tag.setDouble("WG.migratePosY",Math.floor(user.posY)+0.5);
			tag.setDouble("WG.migratePosZ",Math.floor(user.posZ)+0.5);
			break;
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}