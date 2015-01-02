package witchinggadgets.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.relics.ItemResonator;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileBellows;
import thaumcraft.common.tiles.TileTube;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.util.handler.WGMultiPartHandler;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.JPartialOcclusion;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.McMetaPart;
import codechicken.multipart.minecraft.PartMetaAccess;

public class MultipartEssentiaTube extends McMetaPart
//implements ISidedHollowConnect, TSlottedPart
implements IEssentiaTransport, IWandable, TSlottedPart
{
	public ForgeDirection facing = ForgeDirection.NORTH;
	public boolean[] openSides = { true, true, true, true, true, true };
	Aspect essentiaType = null;
	int essentiaAmount = 0;
	Aspect suctionType = null;
	int suction = 0;
	int venting = 0;

	int count = 0;
	static final int freq = 5;
	int ventColor = 0;

	public MultipartEssentiaTube(int meta)
	{
		super(meta);
	}

	@Override
	public int getSlotMask()
	{
		return 64;
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
	public Iterable<Cuboid6> getOcclusionBoxes()
	{
		ArrayList<Cuboid6> t = new ArrayList();
		t.add(new Cuboid6(.375,.375,.375, .625,.625,.625));
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
					//			if(getConnectableTile(world(), x(), y(), z(), ForgeDirection.getOrientation(i))!=null)
					t.add(getConnectionPipe(ForgeDirection.getOrientation(i)));
			}
			t.add(new IndexedCuboid6(null, new Cuboid6(.375,.375,.375, .625,.625,.625)));
		}
		return t;
	}


	@Override
	public boolean drawHighlight(MovingObjectPosition hit, EntityPlayer player, float frame)
	{
		return false;
	}

	@Override
	public void invalidateConvertedTile()
	{
		super.invalidateConvertedTile();
		TileEntity te = world().getTileEntity(x(), y(), z());
		if(te instanceof TileTube)
		{
			this.facing = ((TileTube)te).facing;
			this.openSides = ((TileTube)te).openSides;
			this.essentiaType = ((TileTube)te).getEssentiaType(facing);
			this.essentiaAmount = ((TileTube)te).getEssentiaAmount(facing);
			this.suctionType = ((TileTube)te).getSuctionType(facing);
			this.suction = ((TileTube)te).getSuctionAmount(facing);
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
		//		System.out.println("facing: "+facing);
		if(this.venting > 0)
			this.venting -= 1;
		if (this.count == 0)
			this.count = world().rand.nextInt(10);
		if(!world().isRemote)
		{
			if(venting <= 0)
			{
				if(++count%2 == 0)
				{
					calculateSuction(null, false, false);
					checkVenting();
					if(this.essentiaType != null && this.essentiaAmount == 0)
						this.essentiaType = null;
				}
				if(count % 5 == 0 && suction > 0)
					equalizeWithNeighbours(false);
			}
		}
		else if(this.venting > 0)
		{
			Random r = new Random(hashCode() * 4);
			float rp = r.nextFloat() * 360.0F;
			float ry = r.nextFloat() * 360.0F;
			double fx = -MathHelper.sin(ry / 180.0F * 3.141593F) * MathHelper.cos(rp / 180.0F * 3.141593F);
			double fz = MathHelper.cos(ry / 180.0F * 3.141593F) * MathHelper.cos(rp / 180.0F * 3.141593F);
			double fy = -MathHelper.sin(rp / 180.0F * 3.141593F);

			Thaumcraft.proxy.drawVentParticles(this.world(), this.x() + 0.5D, this.y() + 0.5D, this.z() + 0.5D, fx / 5.0D, fy / 5.0D, fz / 5.0D, this.ventColor);
		}
	}


	void calculateSuction(Aspect filter, boolean restrict, boolean directional)
	{
		if(meta==5)
			restrict=true;
		if(meta==6)
			directional=true;

		this.suction = 0;
		this.suctionType = null;
		ForgeDirection loc = null;
		for(int dir = 0; dir < 6; dir++)
		{
			try
			{
				loc = ForgeDirection.getOrientation(dir);
				if(!directional || this.facing == loc.getOpposite())
					if(isConnectable(loc))
					{
						TileEntity te = ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), loc);
						if (te != null)
						{
							IEssentiaTransport ic = (IEssentiaTransport)te;
							if(filter!=null && ic.getSuctionType(loc.getOpposite())!=null && ic.getSuctionType(loc.getOpposite())!=filter)
								continue;
							if(filter== null && getEssentiaAmount(loc)>0 && ic.getSuctionType(loc.getOpposite())!=null && getEssentiaType(loc)!=ic.getSuctionType(loc.getOpposite()))
								continue;
							if(filter != null && getEssentiaAmount(loc)>0 && getEssentiaType(loc)!=null && ic.getSuctionType(loc.getOpposite())!=null && getEssentiaType(loc)!=ic.getSuctionType(loc.getOpposite()))
								continue;
							int suck = ic.getSuctionAmount(loc.getOpposite());
							if(suck>0 && suck>(suction+1))
							{
								Aspect st = ic.getSuctionType(loc.getOpposite());
								if(st == null)
									st = filter;
								setSuction(st, restrict ? suck / 2 : suck - 1);
							}
						}
					}

			}
			catch (Exception e) {}
		}
	}
	void checkVenting()
	{
		ForgeDirection loc = null;
		for (int dir = 0; dir < 6; dir++)
		{
			try
			{
				loc = ForgeDirection.getOrientation(dir);
				if(isConnectable(loc))
				{
					TileEntity te = ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), loc);
					if(te != null)
					{
						IEssentiaTransport ic = (IEssentiaTransport)te;
						int suck = ic.getSuctionAmount(loc.getOpposite());
						if(suction>0 && (suck==suction || suck==(suction-1)) && suctionType!=ic.getSuctionType(loc.getOpposite()))
						{
							//int c = -1;
							//if(suctionType != null)
							//	c = Config.aspectOrder.indexOf(suctionType);
							//world().addBlockEvent(x(), y(), z(), ConfigBlocks.blockTube, 1, c);
							venting = 40;
						}
					}
				}
			}
			catch (Exception e) {}
		}
	}

	void equalizeWithNeighbours(boolean directional)
	{
		if(meta==6)
			directional=true;
		ForgeDirection fd = null;
		if (essentiaAmount > 0)
			return;
		for (int dir = 0; dir < 6; dir++)
		{
			try
			{
				fd = ForgeDirection.getOrientation(dir);
				if((!directional || facing!=fd.getOpposite()) && isConnectable(fd))
				{
					TileEntity te = ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), fd);
					if(te != null)
					{
						IEssentiaTransport ic = (IEssentiaTransport)te;
						if(!ic.canOutputTo(fd.getOpposite()))
							continue;
						if(((getSuctionType(null) == null) || (getSuctionType(null) == ic.getEssentiaType(fd.getOpposite())) || (ic.getEssentiaType(fd.getOpposite()) == null)) && (getSuctionAmount(null) > ic.getSuctionAmount(fd.getOpposite())) && (getSuctionAmount(null) >= ic.getMinimumSuction()))
						{
							Aspect a = getSuctionType(null);
							if(a==null)
							{
								a = ic.getEssentiaType(fd.getOpposite());
								if (a == null)
									a = ic.getEssentiaType(ForgeDirection.UNKNOWN);
							}
							int am = addEssentia(a, ic.takeEssentia(a, 1, fd.getOpposite()), fd);
							if(am>0)
							{
								//								if (this.world().rand.nextInt(100) == 0)
								//									this.world().addBlockEvent(this.x(), this.y(), this.z(), ConfigBlocks.blockTube, 0, 0);
								return;
							}
						}
					}
				}
			}
			catch (Exception e) {}
		}
	}


	@Override
	public boolean renderStatic(Vector3 pos, int pass)
	{
		RenderBlocks renderer = new RenderBlocks(new PartMetaAccess(this));
		BlockTube b = (BlockTube) ConfigBlocks.blockTube;
		Block bMP = this.getWorld().getBlock(x(),y(),z());
		renderer.setRenderBounds(.375,.375,.375, .625,.625,.625);
		renderer.renderStandardBlock(bMP, x(),y(),z());
		
		IIcon icon = meta==5?b.icon[6]: b.icon[0];

		boolean hasConnections = false;
		boolean overrideCenter = false;
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
			if(isConnectable(fd))
			{
				TileEntity te = getConnectableTile(renderer.blockAccess, x(), y(), z(), fd);
				if(te!=null)
				{
					if(!hasConnections)
						hasConnections=true;
					if(!WGMultiPartHandler.tileIsEssentiaTube(te))
						overrideCenter=true;

					double xMin = fd==ForgeDirection.WEST?0:fd==ForgeDirection.EAST?.5625F: .4375F;
					double yMin = fd==ForgeDirection.DOWN?0:fd==ForgeDirection.UP?.5625F: .4375F;
					double zMin = fd==ForgeDirection.NORTH?0:fd==ForgeDirection.SOUTH?.5625F: .4375F;
					double xMax = fd==ForgeDirection.WEST?.4375F:fd==ForgeDirection.EAST?1: .5625F;
					double yMax = fd==ForgeDirection.DOWN?.4375F:fd==ForgeDirection.UP?1: .5625F;
					double zMax = fd==ForgeDirection.NORTH?.4375F:fd==ForgeDirection.SOUTH?1: .5625F;
					ClientUtilities.addBoxToBlockrender(xMin,yMin,zMin, xMax,yMax,zMax, icon, x(),y(),z());
					if(te instanceof IEssentiaTransport && ((IEssentiaTransport)te).renderExtendedTube() )
					{
						xMin = fd==ForgeDirection.WEST?0:fd==ForgeDirection.EAST?.625: .4375F;
						yMin = fd==ForgeDirection.DOWN?0:fd==ForgeDirection.UP?.625: .4375F;
						zMin = fd==ForgeDirection.NORTH?0:fd==ForgeDirection.SOUTH?.625: .4375F;
						xMax = fd==ForgeDirection.WEST?.375:fd==ForgeDirection.EAST?1: .5625F;
						yMax = fd==ForgeDirection.DOWN?.375:fd==ForgeDirection.UP?1: .5625F;
						zMax = fd==ForgeDirection.NORTH?.375:fd==ForgeDirection.SOUTH?1: .5625F;
						ClientUtilities.addBoxToBlockrender(Vec3.createVectorHelper(fd.offsetX*.25,fd.offsetY*.375,fd.offsetZ*.375), xMin,yMin,zMin, xMax,yMax,zMax, icon, x(),y(),z());
					}
				}
			}
		if(overrideCenter)
			hasConnections = false;

		if(meta==0||meta==6)
			if(hasConnections)
				ClientUtilities.addBoxToBlockrender(.40625,.40625,.40625, .59375,.59375,.59375, b.icon[2], x(),y(),z());
			else
				ClientUtilities.addBoxToBlockrender(.375,.375,.375, .625,.625,.625, b.icon[1], x(),y(),z());
		if(meta==5)
			if(hasConnections)
				ClientUtilities.addBoxToBlockrender(.40625,.40625,.40625, .59375,.59375,.59375, b.icon[6], x(),y(),z());
			else
				ClientUtilities.addBoxToBlockrender(.375,.375,.375, .625,.625,.625, b.icon[1], x(),y(),z());

		return true;
	}
	@Override
	public void renderDynamic(Vector3 pos, float partialRenderTick, int pass)
	{
		if(meta==6 && ThaumcraftApiHelper.getConnectableTile(world(), x(), y(), z(), facing.getOpposite()) != null)
		{
			ClientUtilities.bindTexture("thaumcraft:textures/models/valve.png");
			GL11.glPushMatrix();
			GL11.glTranslated(pos.x+.5, pos.y+.5, pos.z+.5);
			if (facing.offsetY == 0)
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			else
			{
				GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
				GL11.glRotatef(90.0F, facing.offsetY, 0.0F, 0.0F);
			}
			GL11.glRotatef(90.0F, facing.offsetX, facing.offsetY, facing.offsetZ);


			GL11.glPushMatrix();
			GL11.glColor3f(0.45F, 0.5F, 1.0F);
			GL11.glScaled(1.1D, 0.5D, 1.1D);
			GL11.glTranslated(0.0D, -0.5D, 0.0D);
			MultipartEssentiaTube_Valve.valveModel.render();
			GL11.glTranslated(0.0D, -0.25D, 0.0D);
			MultipartEssentiaTube_Valve.valveModel.render();
			GL11.glTranslated(0.0D, -0.25D, 0.0D);
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
		if (this.essentiaType != null)
			tag.setString("type", this.essentiaType.getTag());
		tag.setInteger("amount", this.essentiaAmount);
		byte[] sides = new byte[6];
		for (int a = 0; a < 6; a++)
			sides[a] = (byte) (this.openSides[a]? 1 : 0);
		tag.setInteger("side", this.facing.ordinal());
		tag.setByteArray("open", sides);
	}
	@Override
	public void load(NBTTagCompound tag)
	{
		super.load(tag);
		this.essentiaType = Aspect.getAspect(tag.getString("type"));
		this.essentiaAmount = tag.getInteger("amount");
		this.facing = ForgeDirection.getOrientation(tag.getInteger("side"));
		byte[] sides = tag.getByteArray("open");
		if ((sides != null) && (sides.length == 6))
			for (int a = 0; a < 6; a++)
				this.openSides[a] = sides[a]==1;
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
		return "witchingGadgets:essentia_tube";
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
							player.worldObj.playSound(x()+.5, y()+.5, z()+.5, "thaumcraft:tool", .5f, .9f+player.worldObj.rand.nextFloat()*.2f, false);
							player.swingItem();

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
								if(tile instanceof TileMultipart)
								{
									for(TMultiPart part : ((TileMultipart)tile).jPartList())
										if(part instanceof MultipartEssentiaTube)
										{
											((MultipartEssentiaTube)part).openSides[fd.getOpposite().ordinal()] = openSides[fd.ordinal()];
											world.markBlockForUpdate(x()+fd.offsetX, y()+fd.offsetY, z()+fd.offsetZ);
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
			{
				//				for(Cuboid6 box :)
				//					if(box.intersects(pipe))
				//				if(otherPart.occlusionTest(this))
				if(otherPart instanceof JNormalOcclusion)
					for(Cuboid6 box : ((JNormalOcclusion)otherPart).getOcclusionBoxes())
						if(box.intersects(pipe))
							wall = true;
				if(otherPart instanceof JPartialOcclusion)
					for(Cuboid6 box : ((JPartialOcclusion)otherPart).getPartialOcclusionBoxes())
						if(box.intersects(pipe))
							wall = true;
			}
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
	public void setSuction(Aspect aspect, int amount)
	{
		this.suctionType = aspect;
		this.suction = amount;
	}
	@Override
	public Aspect getSuctionType(ForgeDirection fd)
	{
		return this.suctionType;
	}
	@Override
	public int getSuctionAmount(ForgeDirection fd)
	{
		return this.suction;
	}

	@Override
	public Aspect getEssentiaType(ForgeDirection fd)
	{
		return this.essentiaType;
	}
	@Override
	public int getEssentiaAmount(ForgeDirection fd)
	{
		return this.essentiaAmount;
	}


	@Override
	public int takeEssentia(Aspect aspect, int amount, ForgeDirection fd)
	{
		if(canOutputTo(fd) && this.essentiaType==aspect && this.essentiaAmount>0 && amount>0)
		{
			this.essentiaAmount -= 1;
			if (this.essentiaAmount <= 0) {
				this.essentiaType = null;
			}
			markDirty();
			return 1;
		}
		return 0;
	}
	@Override
	public int addEssentia(Aspect aspect, int amount, ForgeDirection fd)
	{
		if(canInputFrom(fd) && essentiaAmount==0 && amount>0)
		{
			this.essentiaType = aspect;
			this.essentiaAmount += 1;
			markDirty();
			return 1;
		}
		return 0;
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