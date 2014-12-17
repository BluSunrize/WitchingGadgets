//package witchinggadgets.common.blocks.tiles;
//
//import java.util.ArrayList;
//
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.MathHelper;
//
//import org.lwjgl.opengl.GL11;
//
//import thaumcraft.client.lib.UtilsFX;
//import witchinggadgets.client.ClientProxy;
//import witchinggadgets.common.WGContent;
//import witchinggadgets.common.blocks.BlockMPVisRelay;
//import codechicken.lib.vec.Cuboid6;
//import codechicken.lib.vec.Vector3;
//import codechicken.multipart.minecraft.McMetaPart;
//
////public class MultipartVisRelay extends McMetaPart
////implements ISidedHollowConnect, TSlottedPart
//{
//	public MultipartVisRelay(int meta)
//	{
//		super(meta);
//	}
//	
//	@Override
//	public Cuboid6 getBounds()
//	{
//		return new Cuboid6(((BlockMPVisRelay)WGContent.BlockMPVisRelay).getBounds(this.meta));
//	}
//
//	@Override
//	public Iterable<Cuboid6> getCollisionBoxes()
//	{
//		ArrayList t = new ArrayList();
//		t.add(getBounds());
//		return t;
//	}
//
//	//	@Override
//	//	public int getSlotMask()
//	//	{
//	//		return 64;
//	//	}
//	//
//	//	@Override
//	//	public int getHollowSize(int side)
//	//	{
//	//		return 4;
//	//	}
//
//
//
//	@Override
//	public void update()
//	{
//	}
//
//	@Override
//	public void renderDynamic(Vector3 pos, float partialTick, int pass)
//	{
//		//		System.out.println("render!");
//		int ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted;
//
//		GL11.glPushMatrix();
//		GL11.glTranslated(pos.x+.5, pos.y+.5, pos.z+.5);
////		GL11.glScaled(-1,1,1);
//
////		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
//		switch(this.meta)
//		{
//		case 0:
//			GL11.glRotatef( 90.0F, 1.0F, 0.0F, 0.0F);
//			break;
//		case 1:
//			GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
//			break;
//		case 2:
//			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
//			break;
//		case 4:
//			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//			break;
//		case 5:
//			GL11.glRotatef( 90.0F, 0.0F, 1.0F, 0.0F);
//			break;
//		}
//		GL11.glRotatef(45.0F, 0.0F, 0.0F, 1.0F);
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//		GL11.glPushMatrix();
//		UtilsFX.bindTexture("textures/models/vis_relay.png");
//		GL11.glPushMatrix();
//		GL11.glScaled(0.75D, 0.75D, 0.75D);
//		GL11.glTranslated(0.0D, 0.0D, -0.16D);
//		ClientProxy.visRelayModel.renderPart("RingBase");
//		GL11.glPopMatrix();
//
//		ClientProxy.visRelayModel.renderPart("RingFloat");
//
//		GL11.glEnable(3042);
//		GL11.glBlendFunc(770, 771);
//		//		if (tile.color >= 0)
//		//		{
//		//			Color c = new Color(TileVisRelay.colors[tile.color]);
//		//			GL11.glColor3f(c.getRed() / 200.0F, c.getGreen() / 200.0F, c.getBlue() / 200.0F);
//		//		}
//		float scale = MathHelper.sin((ticks + partialTick) / 2.0F) * 0.05F + 0.95F;
//		int j = /*(VisNetHandler.isNodeValid(tile.getParent()) ? 50 : 0) +*/ (int)(150.0F * scale);
//		int k = j % 65536;
//		int l = j / 65536;
//		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
//		ClientProxy.visRelayModel.renderPart("Crystal");
//
//		GL11.glDisable(3042);
//		GL11.glPopMatrix();
//		GL11.glPopMatrix();
//	}
//
//	@Override
//	public void save(NBTTagCompound tag)
//	{
//		super.save(tag);
////		tag.setInteger("facing", facing);
//	}
//	@Override
//	public void load(NBTTagCompound tag)
//	{
//		super.load(tag);
////		facing = tag.getInteger("facing");
//	}
//	
//	@Override
//	public Block getBlock()
//	{
//		return WGContent.BlockMPVisRelay;
//	}
//	@Override
//	public String getType()
//	{
//		return "witchingGadgets:vis_relay";
//	}
//
//}
