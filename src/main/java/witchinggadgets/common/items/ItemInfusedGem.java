package witchinggadgets.common.items;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.tiles.TileMirror;
import thaumcraft.common.tiles.TileMirrorEssentia;
import thaumcraft.common.tiles.TileNode;
import thaumcraft.common.tiles.TileVisRelay;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.api.IInfusedGem;
import witchinggadgets.client.ClientTickHandler;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityTempLight;
import witchinggadgets.common.util.Lib;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInfusedGem extends Item implements IInfusedGem
{
	IIcon[] icons = new IIcon[GemCut.values().length];

	public ItemInfusedGem()
	{
		super();
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		int brittle = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstoneBrittle.effectId, stack);
		if(brittle>=3 || world.rand.nextInt(20)<brittle)
		{
			if(!world.isRemote)
			{
				if (!player.inventory.addItemStackToInventory(new ItemStack(WGContent.ItemMaterial,1,13)))
					player.dropPlayerItemWithRandomChoice(new ItemStack(WGContent.ItemMaterial,1,13), false);
				player.addChatMessage(new ChatComponentTranslation(Lib.CHAT+"gem.shatter"));
			}
			stack.stackSize--;
			return stack;
		}
		int potency = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstonePotency.effectId, stack);
		if(getCut(stack)==GemCut.POINT)
		{
			if(!this.performEffect(GemCut.POINT.toString(), getAspect(stack), potency, brittle, player))
				return stack;

			if(!world.isRemote)
				if (!player.inventory.addItemStackToInventory(new ItemStack(WGContent.ItemMaterial,1,13)))
					player.dropPlayerItemWithRandomChoice(new ItemStack(WGContent.ItemMaterial,1,13), false);
			stack.stackSize--;
		}
		if(getCut(stack)==GemCut.OVAL && getAspect(stack)!=null && this.getDamage(stack)+getConsumedCharge(GemCut.OVAL.toString(), getAspect(stack), player)<=getMaxDamage(stack))
		{
			boolean dmg = this.performEffect(GemCut.OVAL.toString(), getAspect(stack), potency, brittle, player);
			if(dmg && !player.capabilities.isCreativeMode)
				this.setDamage(stack, getDamage(stack)+getConsumedCharge(GemCut.OVAL.toString(), getAspect(stack), player));
		}
		return stack;
	}

	@Override
	public boolean performEffect(String cut, Aspect aspect, int potency, int brittle, EntityPlayer player)
	{
		if(aspect==null || !aspect.isPrimal())
			return false;

		World world = player.worldObj;
		if(cut==GemCut.POINT.toString())
		{
			int x = (int) Math.floor(player.posX);
			int y = (int) player.posY + 1;
			int z = (int) Math.floor(player.posZ);
			if(aspect.equals(Aspect.AIR))
				player.addPotionEffect(new PotionEffect(Potion.jump.id,300+20*potency,4+(potency>1?1:0)));
			if(aspect.equals(Aspect.FIRE))
			{
				TileEntity te;
				int[] dist = potency>2?new int[]{-8,-6,-4,4,6,8}: potency>1?new int[]{-6,-4,4,6,}: potency>0?new int[]{-4,4}: new int[]{-2,2};
				for(int xoff:dist)
					for(int zoff:dist)
						if(world.isAirBlock(x+xoff, y, z+zoff) && world.getBlockLightValue(x+xoff, y, z+zoff)<10)
						{
							if(!world.isRemote)
								world.setBlock(x+xoff, y, z+zoff, WGContent.BlockCustomAiry);
							world.scheduleBlockUpdate(x+xoff, y, z+zoff, WGContent.BlockCustomAiry, 10);
							te = world.getTileEntity(x+xoff, y, z+zoff);
							if(te!=null && te instanceof TileEntityTempLight)
								((TileEntityTempLight)te).tickMax=3600 + potency*800;//3m + Potency*40s
						}
				if(world.isAirBlock(x, y+2, z) && world.getBlockLightValue(x, y+2, z)<10)
				{
					if(!world.isRemote)
						world.setBlock(x, y+2, z, WGContent.BlockCustomAiry);
					world.scheduleBlockUpdate(x, y+2, z, WGContent.BlockCustomAiry, 10);
					te = world.getTileEntity(x, y+2, z);
					if(te!=null && te instanceof TileEntityTempLight)
						((TileEntityTempLight)te).tickMax=3600 + potency*800;//3m + Potency*40s
				}
			}
			if(aspect.equals(Aspect.WATER))
			{
				int dist = 2+potency;
				for(int yOff=-3;yOff<=0;yOff++)
					for(int xOff=-dist;xOff<=dist;xOff++)
						for(int zOff=-dist;zOff<=dist;zOff++)
							if(world.getBlock(x+xOff, y+yOff, z+zOff)!=null)
							{
								if(world.getBlock(x+xOff, y+yOff, z+zOff).equals(Blocks.water))
									world.setBlock(x+xOff, y+yOff, z+zOff, Blocks.ice);
								if(world.getBlock(x+xOff, y+yOff, z+zOff).equals(Blocks.lava))
									world.setBlock(x+xOff, y+yOff, z+zOff, Blocks.obsidian);
								if(world.getBlock(x+xOff, y+yOff, z+zOff).equals(Blocks.flowing_lava))
									world.setBlock(x+xOff, y+yOff, z+zOff, Blocks.cobblestone);
							}
			}
			if(aspect.equals(Aspect.EARTH) && !world.isRemote)
				player.addPotionEffect(new PotionEffect(WGContent.pot_knockbackRes.id,100+potency*100,1+(potency>0?1:0)));
			if(aspect.equals(Aspect.ORDER))
				player.heal(6+potency*6);
			if(aspect.equals(Aspect.ENTROPY))
				Thaumcraft.addWarpToPlayer(player, 3+(potency*3), true);

			return true;
		}
		if(cut==GemCut.OVAL.toString())
		{
			MovingObjectPosition mop = EntityUtils.getMovingObjectPositionFromPlayer(world, player, false);
			int targetX = mop==null?0: mop.blockX+(mop.sideHit==4?-1:mop.sideHit==5?1:0);
			int targetY = mop==null?0: mop.blockY+(mop.sideHit==0?-1:mop.sideHit==1?1:0);
			int targetZ = mop==null?0: mop.blockZ+(mop.sideHit==2?-1:mop.sideHit==3?1:0);
			boolean dmg = false;
			if(aspect.equals(Aspect.FIRE))
			{
				if(mop!=null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
					if(world.isAirBlock(targetX,targetY,targetZ))
					{
						world.playSoundEffect(targetX+5f, targetY+5f, targetZ+.5f, "mob.ghast.fireball", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
						world.setBlock(targetX, targetY, targetZ, Blocks.fire,0,3);
						dmg = true;
					}
			}
			if(aspect.equals(Aspect.WATER))
			{
				if(mop!=null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
					if(world.isAirBlock(targetX,targetY,targetZ) || world.getBlock(targetX, targetY, targetZ).isReplaceable(world, targetX, targetY, targetZ))
					{
						if(world.provider.isHellWorld)
						{
							world.playSoundEffect(targetX+5f, targetY+5f, targetZ+.5f, "random.fizz", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
							for(int l = 0; l < 8; ++l)
								world.spawnParticle("largesmoke", (double)targetX + Math.random(), (double)targetY + Math.random(), (double)targetZ + Math.random(), 0.0D, 0.0D, 0.0D);
						}
						else
						{
							if (!world.isRemote && !world.getBlock(targetX, targetY, targetZ).getMaterial().isSolid() && !!world.getBlock(targetX, targetY, targetZ).getMaterial().isLiquid())
								world.func_147480_a(targetX, targetY, targetZ, true);
							world.setBlock(targetX, targetY, targetZ, Blocks.water,0,3);
						}
						dmg = true;
					}
			}
			if(aspect.equals(Aspect.EARTH) && mop!=null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				List<ChunkCoordinates> ores = this.getOres(world, mop.blockX, mop.blockY, mop.blockZ);
				if(world.isRemote)
					for(ChunkCoordinates cc : ores)
						ClientTickHandler.oreHighlightMap.put(cc, 1000);
				dmg = true;
			}
			if(aspect.equals(Aspect.AIR))
			{
				Vec3 lookVec = player.getLookVec();
				float mod = 1+potency;
				player.addVelocity(lookVec.xCoord*mod, lookVec.yCoord*mod, lookVec.zCoord*mod);
				player.fallDistance = 0;
				dmg = true;
			}
			if(aspect.equals(Aspect.ORDER))
			{
				int x = (int) Math.floor(player.posX);
				int y = (int) player.posY + 1;
				int z = (int) Math.floor(player.posZ);

				String node = Utilities.findCloseNode(world, new ChunkCoordinates(x,y,z));
				if(TileNode.locations.get(node)!=null)
				{
					int nX = TileNode.locations.get(node).get(1);
					int nY = TileNode.locations.get(node).get(2);
					int nZ = TileNode.locations.get(node).get(3);

					float fnX = nX+.5f;
					float fnY = nY+.5f;
					float fnZ = nZ+.5f;
					double xOff = fnX - player.posX;
					double zOff = fnZ - player.posZ;
					double yOff = fnY - (player.posY + (double)player.getEyeHeight());
					double d3 = (double)MathHelper.sqrt_double(xOff * xOff + zOff * zOff);
					float yaw = (float)(Math.atan2(zOff, xOff) * 180.0D / Math.PI) - 90.0F;
					float f3 = MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw);
					yaw = player.rotationYaw + f3;
					float pitch = (float)(-(Math.atan2(yOff, d3) * 180.0D / Math.PI));
					f3 = MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch);
					pitch = player.rotationPitch + f3;
					player.rotationPitch = pitch;
					player.rotationYaw = yaw;

					if(world.isRemote)
					{
						AspectList al = world.getTileEntity(nX,nY,nZ) instanceof TileNode? ((TileNode)world.getTileEntity(nX,nY,nZ)).getAspects(): new AspectList();
						int col = 0xffffff;
						for(Aspect a : al.getAspects())
							if(a!=null)
								col = ClientUtilities.blendColoursToInt(col, a.getColor());
						double[] hand = ClientUtilities.getPlayerHandPos(player, true);
						WitchingGadgets.proxy.createTargetedWispFx(player.worldObj, hand[0],hand[1],hand[2], fnX,fnY,fnZ, col, .5f, 0, true,true);
					}
					dmg = true;
				}
			}
			if(aspect.equals(Aspect.ENTROPY))
			{
				if(Config.allowMirrors)
				{
					if(mop!=null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
					{
						TileEntity tile = world.getTileEntity(targetX,targetY,targetZ);
						if(tile instanceof TileMirror || tile instanceof TileMirrorEssentia)
						{
							boolean link = tile instanceof TileMirror? ((TileMirror)world.getTileEntity(targetX,targetY,targetZ)).linked : ((TileMirrorEssentia)world.getTileEntity(targetX,targetY,targetZ)).linked;
							if(link)
							{
								int dim = tile instanceof TileMirror? ((TileMirror)world.getTileEntity(targetX,targetY,targetZ)).linkDim : ((TileMirrorEssentia)world.getTileEntity(targetX,targetY,targetZ)).linkDim;
								int tx= tile instanceof TileMirror? ((TileMirror)world.getTileEntity(targetX,targetY,targetZ)).linkX : ((TileMirrorEssentia)world.getTileEntity(targetX,targetY,targetZ)).linkX;
								int ty= tile instanceof TileMirror? ((TileMirror)world.getTileEntity(targetX,targetY,targetZ)).linkY : ((TileMirrorEssentia)world.getTileEntity(targetX,targetY,targetZ)).linkY;
								int tz= tile instanceof TileMirror? ((TileMirror)world.getTileEntity(targetX,targetY,targetZ)).linkZ : ((TileMirrorEssentia)world.getTileEntity(targetX,targetY,targetZ)).linkZ;
								ForgeDirection fd = ForgeDirection.getOrientation(world.getBlockMetadata(tx, ty, tz)%6);
								float rot = fd.ordinal()==2?180: fd.ordinal()==4?90: fd.ordinal()==5?270 : 0;
								if(player.dimension!=dim)
									player.travelToDimension(dim);
								player.setLocationAndAngles(tx+.5+fd.offsetX*.5, ty+fd.offsetY, tz+.5+fd.offsetZ*.5, rot, player.rotationPitch);
								dmg = true;
							}
						}
					}
				}else
				{
					AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX-4,player.posY-2,player.posZ-4, player.posX+4,player.posY+2,player.posZ+4);
					for(EntityLivingBase entT : (List<EntityLivingBase>)world.getEntitiesWithinAABB(EntityLivingBase.class, aabb))
						if(entT!=null && !entT.equals(player))
						{
							entT.addVelocity((entT.posX-player.posX)*.4, .4, (entT.posZ-player.posZ)*.4);
							entT.addPotionEffect(new PotionEffect(Potion.blindness.id,15,0));
						}
					dmg = true;
				}
			}
			return dmg;
		}
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getDamage(stack)>0;
	}
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return getDamage(stack) / (float)getMaxDamage(stack);
	}
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return 32;
	}
	@Override
	public int getConsumedCharge(String cut, Aspect aspect, EntityPlayer player)
	{
		if(cut == GemCut.OVAL.toString())
			return aspect==Aspect.FIRE||aspect==Aspect.AIR||aspect==Aspect.EARTH?1: aspect==Aspect.WATER||aspect==Aspect.ORDER?2: aspect==Aspect.ENTROPY?(Config.allowMirrors?16:2): 0;
			return 32;
	}


	static HashMap<Integer,Object> powerBeams = new HashMap();
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getByte("GemCut")>1)
			stack.getTagCompound().setByte("GemCut",(byte)1);

		int brittle = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstoneBrittle.effectId, stack);
		if(entity instanceof EntityPlayer)
			if(world.rand.nextInt(1000)<brittle || (getCut(stack)==GemCut.POINT&&stack.getItemDamage()!=0))
			{
				if(!world.isRemote)
				{
					if (!((EntityPlayer)entity).inventory.addItemStackToInventory(new ItemStack(WGContent.ItemMaterial,1,13)))
						((EntityPlayer)entity).dropPlayerItemWithRandomChoice(new ItemStack(WGContent.ItemMaterial,1,13), false);
					((EntityPlayer)entity).addChatMessage(new ChatComponentTranslation(Lib.CHAT+"gem.shatter"));
				}
				stack.stackSize--;
				if(stack.stackSize<=0)
					stack = null;
				((EntityPlayer)entity).inventory.setInventorySlotContents(slot, stack);
				return;
			}
		if(world.rand.nextInt(20)<brittle)
			return;
		if(getCut(stack)==GemCut.OVAL && getAspect(stack)!=null && entity instanceof EntityPlayer && selected && entity.ticksExisted%4==0 && this.getDamage(stack)>0)
		{
			if (TileVisRelay.nearbyPlayers.containsKey(Integer.valueOf(entity.getEntityId())))
				if ((((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get() != null) && (((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).getDistanceFrom(entity.posX, entity.posY, entity.posZ) < 26.0D))
				{
					Aspect aspect = getAspect(stack);
					int amt = ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).consumeVis(aspect, 1);
					if (amt>0)
					{
						this.setDamage(stack, getDamage(stack)-amt);
						((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).triggerConsumeEffect(aspect);
						if(world.isRemote)
						{
							ForgeDirection d2 = ForgeDirection.getOrientation(((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).orientation);
							double x = ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).xCoord+.5 + d2.offsetX*.05;
							double y = ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).yCoord+.5 + d2.offsetY*.05;
							double z = ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(Integer.valueOf(entity.getEntityId()))).get()).zCoord+.5 + d2.offsetZ*.05;
							double[] playerPos = ClientUtilities.getPlayerHandPos((EntityPlayer)entity,true);
							powerBeams.put(entity.getEntityId(), Thaumcraft.proxy.beamPower(world, x, y, z, playerPos[0],playerPos[1],playerPos[2], ((aspect.getColor()>>16)&0xff)/255f, ((aspect.getColor()>>8)&0xff)/255f, (aspect.getColor()&0xff)/255f, false, powerBeams.get(entity.getEntityId())));
						}
					}
				}
				else
				{
					powerBeams.remove(entity.getEntityId());
					TileVisRelay.nearbyPlayers.remove(Integer.valueOf(entity.getEntityId()));
				}
		}
	}


	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		list.add(createGem(null, ItemInfusedGem.GemCut.POINT, true));
		list.add(createGem(Aspect.ORDER, ItemInfusedGem.GemCut.POINT, false));
		list.add(createGem(Aspect.ENTROPY, ItemInfusedGem.GemCut.POINT, false));
		list.add(createGem(Aspect.FIRE, ItemInfusedGem.GemCut.POINT, false));
		list.add(createGem(Aspect.WATER, ItemInfusedGem.GemCut.POINT, false));
		list.add(createGem(Aspect.AIR, ItemInfusedGem.GemCut.POINT, false));
		list.add(createGem(Aspect.EARTH, ItemInfusedGem.GemCut.POINT, false));
		list.add(createGem(Aspect.ORDER, ItemInfusedGem.GemCut.OVAL, false));
		list.add(createGem(Aspect.ENTROPY, ItemInfusedGem.GemCut.OVAL, false));
		list.add(createGem(Aspect.FIRE, ItemInfusedGem.GemCut.OVAL, false));
		list.add(createGem(Aspect.WATER, ItemInfusedGem.GemCut.OVAL, false));
		list.add(createGem(Aspect.AIR, ItemInfusedGem.GemCut.OVAL, false));
		list.add(createGem(Aspect.EARTH, ItemInfusedGem.GemCut.OVAL, false));
	}
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if(getCut(stack)!=null)
			list.add(StatCollector.translateToLocal(Lib.DESCRIPTION+"gemcut."+getCut(stack)));
	}
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return super.getItemStackDisplayName(stack)+(getAspect(stack)!=null? " ("+getAspect(stack).getName()+")" : "");
	}
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack)+(getAspect(stack)==null? ".innert" : "");
	}
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		Aspect a = getAspect(stack);
		if(a != null)
			return a.getColor();
		else
			return 16777215;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return getIconIndex(stack);
	}
	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		return getIconIndex(stack);
	}
	@Override
	public IIcon getIconIndex(ItemStack stack)
	{
		if(getCut(stack)!=null)
			return icons[getCut(stack).ordinal()];
		return icons[0];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir)
	{
		for(int i=0;i<icons.length;i++)
			this.icons[i] = ir.registerIcon("witchinggadgets:infusedGem_"+GemCut.values()[i].toString().toLowerCase());
	}
	@Override
	public int getItemEnchantability(ItemStack stack)
	{
		return 10;
	}
	@Override
	public boolean isItemTool(ItemStack stack)
	{
		return stack.stackSize == 1;
	}

	List<ChunkCoordinates> getOres(World world, int x, int y, int z)
	{
		Block search = world.getBlock(x, y, z);
		List<ChunkCoordinates> ores = new ArrayList();

		List<ChunkCoordinates> openList = new ArrayList();
		List<ChunkCoordinates> closedList = new ArrayList();
		List<ChunkCoordinates> checked = new ArrayList();

		openList.add(new ChunkCoordinates(x,y,z));

		ChunkCoordinates next = null;
		final int closedListMax = 400;
		while(closedList.size()<closedListMax && !openList.isEmpty())
		{
			next = openList.get(0);
			closedList.add(next);
			if(Utilities.isOre( world, next.posX,next.posY,next.posZ))
			{
				ores.add(next);
			}
			for(ChunkCoordinates cc2 : getConnected(next.posX, next.posY, next.posZ))
				if(!checked.contains(cc2) && !closedList.contains(cc2) && !openList.contains(cc2) && (world.getBlock(cc2.posX,cc2.posY,cc2.posZ).equals(search)||Utilities.isOre( world, cc2.posX,cc2.posY,cc2.posZ))/*.getMaterial()==Material.rock*/)
					if(cc2.getDistanceSquared(x, y, z)<32)
						openList.add(cc2);
			openList.remove(0);
		}
		return ores;
	}
	ChunkCoordinates[] getConnected(int x, int y, int z)
	{
		return new ChunkCoordinates[]{new ChunkCoordinates(x-1,y,z),new ChunkCoordinates(x+1,y,z),new ChunkCoordinates(x,y,z-1),new ChunkCoordinates(x,y,z+1),new ChunkCoordinates(x,y-1,z),new ChunkCoordinates(x,y+1,z)};
	}

	public static ItemStack createGem(Aspect asp, ItemInfusedGem.GemCut cut, boolean forceCreate)
	{
		if(!forceCreate && (asp==null || cut == null))
			return null;
		ItemStack stack = new ItemStack(WGContent.ItemInfusedGem);
		stack.setTagCompound(new NBTTagCompound());
		if(cut!=null)
			stack.getTagCompound().setByte("GemCut", (byte) cut.ordinal());
		if(asp!=null)
			stack.getTagCompound().setString("Aspect", asp.getTag());
		return stack;
	}
	public static Aspect getAspect(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Aspect"))
			return null;
		return Aspect.getAspect(stack.getTagCompound().getString("Aspect"));
	}
	public static ItemInfusedGem.GemCut getCut(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("GemCut"))
			return null;
		return GemCut.getValue(stack.getTagCompound().getByte("GemCut"));
	}

	public enum GemCut
	{
		POINT,
		//		TABLE,
		//		STEP,
		//		ROSE,
		OVAL;

		public static GemCut getValue(byte b) 
		{
			if(b>=0&&b<values().length)
				return values()[b];
			return values()[0];
		}
	}

	@Override
	public boolean isGemEnchantable(ItemStack stack)
	{
		return ItemInfusedGem.getCut(stack)!=null && ItemInfusedGem.getAspect(stack)!=null;
	}
}