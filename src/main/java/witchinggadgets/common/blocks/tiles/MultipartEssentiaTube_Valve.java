package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.client.renderers.models.ModelTubeValve;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.relics.ItemResonator;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileTubeValve;
import witchinggadgets.client.ClientUtilities;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;

public class MultipartEssentiaTube_Valve extends MultipartEssentiaTube
{
	public boolean allowFlow = true;
	boolean wasPoweredLastTick = false;
	public float rotation = 0.0F;

	public MultipartEssentiaTube_Valve(int meta)
	{
		super(meta);
	}

	@Override
	public void invalidateConvertedTile()
	{
		super.invalidateConvertedTile();
		TileEntity te = world().getTileEntity(x(), y(), z());
		if(te instanceof TileTubeValve)
		{
			this.allowFlow = ((TileTubeValve)te).allowFlow;
			this.rotation = ((TileTubeValve)te).rotation;
		}
	}


	@Override
	public void update()
	{
		super.update();
		if(!world().isRemote && count%5==0)
		{
			boolean gettingPower = world().isBlockIndirectlyGettingPowered(x(), y(), z());;
			if(wasPoweredLastTick && !gettingPower && allowFlow!=true)
			{
				allowFlow = true;
				world().playSoundEffect(x()+.5, y()+.5, z()+.5, "thaumcraft:squeek", .7f, .9f+world().rand.nextFloat()*.2f);
				world().markBlockForUpdate(x(), y(), z());
				markDirty();
				sendDescUpdate();
			}
			if(!wasPoweredLastTick && gettingPower && allowFlow)
			{
				allowFlow = false;
				world().playSoundEffect(x()+.5, y()+.5, z()+.5, "thaumcraft:squeek", .7f, .9f+world().rand.nextFloat()*.2f);
				world().markBlockForUpdate(x(), y(), z());
				markDirty();
				sendDescUpdate();
			}
			wasPoweredLastTick = gettingPower;
		}
		if(world().isRemote)
		{
			if(!allowFlow && rotation<360)
				rotation += 20;
			else if(allowFlow && rotation > 0)
				rotation -= 20;
		}
	}

	@Override
	public boolean renderStatic(Vector3 pos, int pass)
	{
		super.renderStatic(pos, pass);
		ClientUtilities.addBoxToBlockrender(.375,.375,.375, .625,.625,.625, ((BlockTube)ConfigBlocks.blockTube).icon[1], x(),y(),z());
		return true;
	}
	static ModelTubeValve valveModel =  new ModelTubeValve();
	@Override
	public void renderDynamic(Vector3 pos, float partialTickTime, int pass)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(pos.x+.5,pos.y+.5,pos.z+.5);
		if (facing.offsetY==0)
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		else
		{
			GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
			GL11.glRotatef(90.0F, facing.offsetY, 0.0F, 0.0F);
		}
		GL11.glRotatef(90.0F, facing.offsetX, facing.offsetY, facing.offsetZ);
		GL11.glRotated(-rotation * 1.5D, 0.0D, 1.0D, 0.0D);
		GL11.glTranslated(0.0D, -(rotation / 360.0F) * 0.12F, 0.0D);

		ClientUtilities.bindTexture("thaumcraft:textures/models/valve.png");
		valveModel.render();

		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-0.25F, -0.25F, -0.25F);
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		Tessellator tessellator = Tessellator.instance;
		IIcon icon = ((BlockTube)ConfigBlocks.blockTube).iconValve;
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMinU();
		float f4 = icon.getMaxV();
		ClientUtilities.bindTexture("textures/atlas/blocks.png");
		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.1F);
		GL11.glPopMatrix();
	}

	@Override
	public Cuboid6 getBounds()
	{
		float minx = .375F;float maxx = .625F;
		float miny = .375F;float maxy = .625F;
		float minz = .375F;float maxz = .625F;
		for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
			if(fd!=facing)
			{
				TileEntity te = ThaumcraftApiHelper.getConnectableTile(world(),x(),y(),z(), fd);
				if (te != null)
				{
					switch (fd)
					{
					case DOWN:
						miny = 0.0F; break;
					case UP:
						maxy = 1.0F; break;
					case NORTH:
						minz = 0.0F; break;
					case SOUTH: 
						maxz = 1.0F; break;
					case WEST: 
						minx = 0.0F; break;
					case EAST: 
						maxx = 1.0F;
					default:
						break;
					}
				}
			}
		return new Cuboid6(minx, miny, minz, maxx, maxy, maxz);
	}
	@Override
	public Iterable<IndexedCuboid6> getSubParts()
	{
		ArrayList<IndexedCuboid6> t = new ArrayList();
		if(world().isRemote && ( Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem()==null || !(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemWandCasting || Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemResonator)))
		{
			t.add(new IndexedCuboid6(null, getBounds()));
			t.add(new IndexedCuboid6(null, new Cuboid6(.375+facing.offsetX*.375,.375+facing.offsetY*.375,.375+facing.offsetZ*.375, .625+facing.offsetX*.375,.625+facing.offsetY*.375,.625+facing.offsetZ*.375)));
		}
		else
		{
			for(int i=0; i<6; i++)
			{
				ForgeDirection fd = ForgeDirection.getOrientation(i);
				if(world().getTileEntity(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ) instanceof IEssentiaTransport)
					//			if(getConnectableTile(world(), x(), y(), z(), ForgeDirection.getOrientation(i))!=null)
					t.add(getConnectionPipe(ForgeDirection.getOrientation(i)));
			}
			t.add(new IndexedCuboid6(null, new Cuboid6(.375,.375,.375, .625,.625,.625)));
		}
		return t;
	}
	@Override
	public boolean isConnectable(ForgeDirection face)
	{
		return face!=facing && super.isConnectable(face);
	}
	@Override
	public void setSuction(Aspect aspect, int amount)
	{
		if(allowFlow)
			super.setSuction(aspect, amount);
	}

	@Override
	public void save(NBTTagCompound tag)
	{
		super.save(tag);
		tag.setBoolean("allowFlow",allowFlow);
		//		boolean wasPoweredLastTick = false;
		//		tag.setInteger("facing", facing);
	}
	@Override
	public void load(NBTTagCompound tag)
	{
		super.load(tag);
		allowFlow = tag.getBoolean("allowFlow");
		//		facing = tag.getInteger("facing");
	}

	@Override
	public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack stack)
	{
		allowFlow = !allowFlow;
		if (!world().isRemote)
		{
			world().playSoundEffect(x()+.5, y()+.5, z()+.5, "thaumcraft:squeek", 0.7f, 0.9f+world().rand.nextFloat()*0.2f);
			sendDescUpdate();
		}
		return true;
	}

	@Override
	public String getType()
	{
		return "witchingGadgets:essentia_tube_valve";
	}

}