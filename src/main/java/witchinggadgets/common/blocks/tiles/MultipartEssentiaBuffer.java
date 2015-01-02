package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.relics.ItemResonator;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileBellows;
import thaumcraft.common.tiles.TileTube;
import thaumcraft.common.tiles.TileTubeBuffer;
import witchinggadgets.client.ClientUtilities;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.McMetaPart;
import codechicken.multipart.minecraft.PartMetaAccess;

public class MultipartEssentiaBuffer extends McMetaPart
implements IAspectContainer, IEssentiaTransport, IWandable
{
	public AspectList aspects = new AspectList();
	public final int MAXAMOUNT = 8;
	public boolean[] openSides = { true, true, true, true, true, true };
	public byte[] chokedSides = { 0, 0, 0, 0, 0, 0 };

	int count = 0;
	int bellows = -1;

	public MultipartEssentiaBuffer(int meta)
	{
		super(meta);
	}

	@Override
	public Cuboid6 getBounds()
	{
		float minx = .375F;float maxx = .625F;
		float miny = .375F;float maxy = .625F;
		float minz = .375F;float maxz = .625F;
		ForgeDirection fd = null;
		for (int side = 0; side < 6; side++)
		{
			fd = ForgeDirection.getOrientation(side);
			TileEntity te = ThaumcraftApiHelper.getConnectableTile(world(),x(),y(),z(), fd);
			if (te != null) {
				switch (side)
				{
				case 0: 
					miny = 0.0F; break;
				case 1: 
					maxy = 1.0F; break;
				case 2: 
					minz = 0.0F; break;
				case 3: 
					maxz = 1.0F; break;
				case 4: 
					minx = 0.0F; break;
				case 5: 
					maxx = 1.0F;
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
			t.add(new IndexedCuboid6(null, getBounds()));
		else
		{
			for(int i=0; i<6; i++)
			{
				ForgeDirection fd = ForgeDirection.getOrientation(i);
				if(world().getTileEntity(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ) instanceof IEssentiaTransport)
					t.add(getConnectionPipe(ForgeDirection.getOrientation(i)));
			}
			t.add(new IndexedCuboid6(null, new Cuboid6(.25,.25,.25, .75,.75,.75)));
		}

		return t;
	}

	@Override
	public Iterable<Cuboid6> getOcclusionBoxes()
	{
		ArrayList<Cuboid6> t = new ArrayList();
		t.add(new Cuboid6(.25,.25,.25, .75,.75,.75));
		return t;
	}
	@Override
	public Iterable<Cuboid6> getCollisionBoxes()
	{
		ArrayList<Cuboid6> t = new ArrayList();
		t.add(getBounds());
		return t;
	}

	@Override
	public void invalidateConvertedTile()
	{
		super.invalidateConvertedTile();
		TileEntity te = world().getTileEntity(x(), y(), z());
		if(te instanceof TileTubeBuffer)
		{
			this.aspects = ((TileTubeBuffer)te).aspects;
			this.openSides = ((TileTubeBuffer)te).openSides;
			this.chokedSides = ((TileTubeBuffer)te).chokedSides;
		}
	}

	@Override
	public boolean doesTick()
	{
		return true;
	}

	@Override
	public void update()
	{
		super.update();
		this.count += 1;
		if (this.bellows<0 || count%20==0)
		{
			getBellows();
		}
		if(!world().isRemote)
			if(count%5==0)
			{
				if(aspects.visSize()<8)
					fillBuffer();
			}
	}

	void fillBuffer()
	{
		TileEntity te = null;
		IEssentiaTransport ic = null;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			te = ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), dir);
			if(te!=null)
			{
				ic = (IEssentiaTransport)te;
				if(ic.getEssentiaAmount(dir.getOpposite())>0 && ic.getSuctionAmount(dir.getOpposite())<getSuctionAmount(dir) && getSuctionAmount(dir)>=ic.getMinimumSuction())
				{
					Aspect ta = ic.getEssentiaType(dir.getOpposite());
					addToContainer(ta, ic.takeEssentia(ta, 1, dir.getOpposite()));
					return;
				}
			}
		}
	}

	public void getBellows()
	{
		this.bellows = TileBellows.getBellows(world(), x(), y(), z(), ForgeDirection.VALID_DIRECTIONS);
	}


	@Override
	public boolean renderStatic(Vector3 pos, int pass)
	{
		RenderBlocks renderer = new RenderBlocks(new PartMetaAccess(this));
		BlockTube b = (BlockTube) ConfigBlocks.blockTube;
		
		renderer.setRenderBounds(.5,.5,.5, .5,.5,.5);
		renderer.renderStandardBlock(this.getWorld().getBlock(x(),y(),z()), x(),y(),z());
		
		boolean hasConnections = false;
		for(int i=0; i<6; i++)
		{
			ForgeDirection fd = ForgeDirection.getOrientation(i);
			if(isConnectable(fd))
			{
				TileEntity te = getConnectableTile(renderer.blockAccess, x(), y(), z(), fd);
				if(te!=null)
				{
					if(!hasConnections)
						hasConnections=true;
					double xMin = fd==ForgeDirection.WEST?0:fd==ForgeDirection.EAST?.75: .4375;
					double yMin = fd==ForgeDirection.DOWN?0:fd==ForgeDirection.UP?.75: .4375;
					double zMin = fd==ForgeDirection.NORTH?0:fd==ForgeDirection.SOUTH?.75: .4375;
					double xMax = fd==ForgeDirection.WEST?.25:fd==ForgeDirection.EAST?1: .5625;
					double yMax = fd==ForgeDirection.DOWN?.25:fd==ForgeDirection.UP?1: .5625;
					double zMax = fd==ForgeDirection.NORTH?.25:fd==ForgeDirection.SOUTH?1: .5625;
					ClientUtilities.addBoxToBlockrender(xMin,yMin,zMin, xMax,yMax,zMax, b.icon[5],x(),y(),z());
				
					if(te instanceof IEssentiaTransport && ((IEssentiaTransport)te).renderExtendedTube() )
					{
						xMin = fd==ForgeDirection.WEST?0:fd==ForgeDirection.EAST?.625: .4375F;
						yMin = fd==ForgeDirection.DOWN?0:fd==ForgeDirection.UP?.625: .4375F;
						zMin = fd==ForgeDirection.NORTH?0:fd==ForgeDirection.SOUTH?.625: .4375F;
						xMax = fd==ForgeDirection.WEST?.375:fd==ForgeDirection.EAST?1: .5625F;
						yMax = fd==ForgeDirection.DOWN?.375:fd==ForgeDirection.UP?1: .5625F;
						zMax = fd==ForgeDirection.NORTH?.375:fd==ForgeDirection.SOUTH?1: .5625F;
						ClientUtilities.addBoxToBlockrender(Vec3.createVectorHelper(fd.offsetX*.25,fd.offsetY*.375,fd.offsetZ*.375), xMin,yMin,zMin, xMax,yMax,zMax, b.icon[5], x(),y(),z());
					}
				}
			}
		}
		ClientUtilities.addBoxToBlockrender(.25,.25,.25, .75,.75,.75, b.icon[5],x(),y(),z());

		return true;
	}
	@Override
	public void renderDynamic(Vector3 pos, float partialTickTime, int pass)
	{
		ClientUtilities.bindTexture("thaumcraft:textures/models/valve.png");
		for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
			if(chokedSides[fd.ordinal()]!=0 && openSides[fd.ordinal()] && ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), fd)!=null)
			{
				GL11.glPushMatrix();
				GL11.glTranslated(pos.x+.5, pos.y+.5, pos.z+.5);
				if(fd.getOpposite().offsetY == 0)
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				else
				{
					GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
					GL11.glRotatef(90.0F, fd.getOpposite().offsetY, 0.0F, 0.0F);
				}
				GL11.glRotatef(90.0F, fd.getOpposite().offsetX, fd.getOpposite().offsetY, fd.getOpposite().offsetZ);

				GL11.glPushMatrix();
				if (chokedSides[fd.ordinal()] == 2)
					GL11.glColor3f(1.0F, 0.3F, 0.3F);
				else
					GL11.glColor3f(0.3F, 0.3F, 1.0F);
				GL11.glScaled(1.2D, 1.0D, 1.2D);
				GL11.glTranslated(0.0D, -0.5D, 0.0D);
				MultipartEssentiaTube_Valve.valveModel.render();
				GL11.glPopMatrix();
				GL11.glPopMatrix();
			}
	}

	public TileEntity getConnectableTile(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		TileEntity te = world.getTileEntity(x + face.offsetX, y + face.offsetY, z + face.offsetZ);
		if (((te instanceof IEssentiaTransport)) && (((IEssentiaTransport)te).isConnectable(face.getOpposite()))) {
			return te;
		}
		if (((te instanceof TileBellows)) && (((TileBellows)te).orientation == face.getOpposite().ordinal())) {
			return te;
		}
		return null;
	}

	@Override
	public void save(NBTTagCompound tag)
	{
		super.save(tag);
		this.aspects.writeToNBT(tag);
		byte[] sides = new byte[6];
		for (int a = 0; a < 6; a++)
			sides[a] = (byte) (this.openSides[a]?1:0);

		tag.setByteArray("open", sides);
		tag.setByteArray("choke", this.chokedSides);
	}
	@Override
	public void load(NBTTagCompound tag)
	{
		super.load(tag);
		this.aspects.readFromNBT(tag);
		byte[] sides = tag.getByteArray("open");
		if ((sides != null) && (sides.length == 6))
			for (int a = 0; a < 6; a++)
				this.openSides[a] = sides[a]==1;

		this.chokedSides = tag.getByteArray("choke");
		if ((this.chokedSides == null) || (this.chokedSides.length < 6))
			this.chokedSides = new byte[] { 0, 0, 0, 0, 0, 0 };

	}

	@Override
	public void writeDesc(MCDataOutput packet)
	{
		super.writeDesc(packet);
		NBTTagCompound tag = new NBTTagCompound();
		save(tag);
		packet.writeNBTTagCompound(tag);
	}
	@Override
	public void readDesc(MCDataInput packet)
	{
		super.readDesc(packet);
		NBTTagCompound tag = packet.readNBTTagCompound();
		load(tag);
	}

	@Override
	public Iterable<ItemStack> getDrops()
	{
		return Arrays.asList(new ItemStack(getBlock(),1,meta));
	}
	@Override
	public ItemStack pickItem(MovingObjectPosition hit)
	{
		return new ItemStack(getBlock(),1,meta);
	}
	@Override
	public Block getBlock()
	{
		return ConfigBlocks.blockTube;
	}
	@Override
	public String getType()
	{
		return "witchingGadgets:essentia_buffer";
	}

	//	@Override
	public int onWandRightClick(World world, ItemStack wand, EntityPlayer player, MovingObjectPosition mop)
	{
		Vec3 localHit = mop.hitVec.addVector(-x(), -y(), -z());
		for(IndexedCuboid6 ibox : getSubParts())
		{
			if(ibox.min.x<=localHit.xCoord && ibox.max.x>=localHit.xCoord)
				if(ibox.min.y<=localHit.yCoord && ibox.max.y>=localHit.yCoord)
					if(ibox.min.z<=localHit.zCoord && ibox.max.z>=localHit.zCoord)
					{
						if(ibox.data instanceof ForgeDirection)
						{
							ForgeDirection fd = (ForgeDirection) ibox.data;
							player.swingItem();

							if(player.isSneaking())
							{
								player.worldObj.playSound(x()+.5, y()+.5, z()+.5, "thaumcraft:tool", .6f, 1.1f+player.worldObj.rand.nextFloat()*.2f, false);
								this.chokedSides[fd.ordinal()]+=1;
								if (this.chokedSides[fd.ordinal()] > 2)
									this.chokedSides[fd.ordinal()] = 0;
								world.markBlockForUpdate(x(), y(), z());
							}
							else
							{
								player.worldObj.playSound(x()+.5, y()+.5, z()+.5, "thaumcraft:tool", .5f, .9f+player.worldObj.rand.nextFloat()*.2f, false);

								this.openSides[fd.ordinal()] = !this.openSides[fd.ordinal()];
								world.markBlockForUpdate(x(), y(), z());

								TileEntity tile = world().getTileEntity(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ);
								if(tile != null)
								{
									if(tile instanceof TileTube)
									{
										((TileTube)tile).openSides[fd.getOpposite().ordinal()] = openSides[fd.ordinal()];
										world.markBlockForUpdate(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ);
										tile.markDirty();
									}
									if(tile instanceof TileTubeBuffer)
									{
										((TileTubeBuffer)tile).openSides[fd.getOpposite().ordinal()] = openSides[fd.ordinal()];
										world.markBlockForUpdate(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ);
										tile.markDirty();
									}
									if(tile instanceof TileMultipart)
									{
										for(TMultiPart part : ((TileMultipart)tile).jPartList())
											if(part instanceof MultipartEssentiaTube)
											{
												((MultipartEssentiaTube)part).openSides[fd.getOpposite().ordinal()] = openSides[fd.ordinal()];
												world.markBlockForUpdate(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ);
											}
											else if(part instanceof MultipartEssentiaBuffer)
											{
												((MultipartEssentiaBuffer)part).openSides[fd.getOpposite().ordinal()] = openSides[fd.ordinal()];
												world.markBlockForUpdate(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ);
											}
									}
								}
							}
						}
						return world.isRemote?0:1;
					}
		}
		return 0;
	}

	@Override
	public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack stack)
	{
		return false;
	}

	IndexedCuboid6 getConnectionPipe(ForgeDirection fd)
	{
		double xMin = fd==ForgeDirection.WEST?0:fd==ForgeDirection.EAST?.578125: .421875;
		double yMin = fd==ForgeDirection.DOWN?0:fd==ForgeDirection.UP?.578125: .421875;
		double zMin = fd==ForgeDirection.NORTH?0:fd==ForgeDirection.SOUTH?.578125: .421875;
		double xMax = fd==ForgeDirection.WEST?.421875:fd==ForgeDirection.EAST?1: .578125;
		double yMax = fd==ForgeDirection.DOWN?.421875:fd==ForgeDirection.UP?1: .578125;
		double zMax = fd==ForgeDirection.NORTH?.421875:fd==ForgeDirection.SOUTH?1: .578125;
		return new IndexedCuboid6(fd, new Cuboid6(xMin,yMin,zMin, xMax,yMax,zMax));
	}
	@Override
	public boolean isConnectable(ForgeDirection fd)
	{
		if (fd==ForgeDirection.UNKNOWN)
			return false;
		boolean wall = false;
		Cuboid6 pipe = getConnectionPipe(fd);
		for(TMultiPart otherPart : tile().jPartList())
			if(otherPart!=this)
				for(Cuboid6 box : otherPart.getCollisionBoxes())
					if(box.intersects(pipe))
						wall = true;
		return this.openSides[fd.ordinal()] && !wall;
	}
	@Override
	public boolean canInputFrom(ForgeDirection fd)
	{
		if (fd==ForgeDirection.UNKNOWN)
			return false;
		return isConnectable(fd) && this.openSides[fd.ordinal()];
	}
	@Override
	public boolean canOutputTo(ForgeDirection fd)
	{
		if (fd==ForgeDirection.UNKNOWN)
			return false;
		return isConnectable(fd) && this.openSides[fd.ordinal()];
	}

	@Override
	public void setSuction(Aspect aspect, int amount){}
	@Override
	public Aspect getSuctionType(ForgeDirection fd)
	{
		return null;
	}
	@Override
	public int getSuctionAmount(ForgeDirection fd)
	{
		return this.chokedSides[fd.ordinal()]==(byte)2?0: (this.bellows<=0 || this.chokedSides[fd.ordinal()]==1)?1: this.bellows*32;
	}

	@Override
	public Aspect getEssentiaType(ForgeDirection fd)
	{
		return this.aspects.size() > 0 ? this.aspects.getAspects()[world().rand.nextInt(this.aspects.getAspects().length)] : null;
	}
	@Override
	public int getEssentiaAmount(ForgeDirection fd)
	{
		return this.aspects.visSize();
	}


	@Override
	public int takeEssentia(Aspect aspect, int amount, ForgeDirection fd)
	{
		if(!canOutputTo(fd))
			return 0;
		TileEntity te = null;
		IEssentiaTransport ic = null;
		int suction = 0;
		te = ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), fd);
		if(te!=null)
		{
			ic = (IEssentiaTransport)te;
			suction = ic.getSuctionAmount(fd.getOpposite());
		}
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			if(canOutputTo(dir) && dir!=fd)
			{
				te = ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), dir);
				if(te!=null)
				{
					ic = (IEssentiaTransport)te;
					int sa = ic.getSuctionAmount(dir.getOpposite());
					Aspect su = ic.getSuctionType(dir.getOpposite());
					if((su==aspect || su==null) && suction<sa && getSuctionAmount(dir)<sa)
						return 0;
				}
			}
		return takeFromContainer(aspect, amount) ? amount : 0;
	}
	@Override
	public int addEssentia(Aspect aspect, int amount, ForgeDirection fd)
	{
		return canInputFrom(fd) ? amount - addToContainer(aspect, amount) : 0;
	}

	@Override
	public int getMinimumSuction()
	{
		return 0;
	}
	@Override
	public boolean renderExtendedTube()
	{
		return false;
	}

	void markDirty()
	{
		tile().markDirty();
	}

	@Override
	public int addToContainer(Aspect aspect, int amount)
	{
		if (amount != 1)
			return amount;
		if(this.aspects.visSize()<8)
		{
			this.aspects.add(aspect, amount);
			markDirty();
			world().markBlockForUpdate(x(), y(), z());
			return 0;
		}
		return amount;
	}
	@Override
	public boolean takeFromContainer(Aspect aspect, int amount)
	{
		if(aspects.getAmount(aspect)>=amount)
		{
			aspects.remove(aspect, amount);
			markDirty();
			world().markBlockForUpdate(x(), y(), z());
			return true;
		}
		return false;
	}
	@Override
	public boolean takeFromContainer(AspectList arg0)
	{
		return false;
	}

	@Override
	public boolean doesContainerContainAmount(Aspect aspect, int amount)
	{
		return this.aspects.getAmount(aspect) >= amount;
	}
	@Override
	public boolean doesContainerContain(AspectList arg0)
	{
		return false;
	}
	@Override
	public int containerContains(Aspect aspect)
	{
		return this.aspects.getAmount(aspect);
	}
	@Override
	public boolean doesContainerAccept(Aspect aspect)
	{
		return true;
	}

	@Override
	public AspectList getAspects()
	{
		return aspects;
	}
	@Override
	public void setAspects(AspectList aspects) {}

	@Override
	public void onUsingWandTick(ItemStack wand, EntityPlayer player, int time)
	{
	}
	@Override
	public ItemStack onWandRightClick(World world, ItemStack wand, EntityPlayer player)
	{
		return null;
	}
	@Override
	public int onWandRightClick(World world, ItemStack wand, EntityPlayer player, int x, int y, int z, int side, int meta)
	{
		return onWandRightClick(world, wand, player, RayTracer.retraceBlock(world(), player, x(), y(), z()));
	}
	@Override
	public void onWandStoppedUsing(ItemStack wand, World arg1, EntityPlayer player, int time)
	{
	}
}