package witchinggadgets.common.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketScannedToServer;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TilePedestal;
import thaumcraft.common.tiles.TileTable;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.util.Lib;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMaterials extends Item
{
	public IIcon iconPlateOverlay;
	public IIcon iconPhotoOverlay;

	private final static String[] subNames = {
		"threadSimple", "threadGold", "threadThaumium", "clothSpace", "clothGolden", "clothBewitched",
		"wolfPelt", "calculator", "cuttingTools", "photoPlate", "developedPhoto",
		"bucketFZDarkIron","primordialShard","gemstoneDust"
	};
	public IIcon[] icon = new IIcon[subNames.length];

	public ItemMaterials()
	{
		super();
		maxStackSize = 64;
		setCreativeTab(WitchingGadgets.tabWG);
		setHasSubtypes(true);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int par2)
	{
		if(stack.getItemDamage() == 3)
			return Aspect.VOID.getColor();
		else if(stack.getItemDamage() == 4)
			return Aspect.GREED.getColor();
		else if(stack.getItemDamage() == 5)
			return Aspect.MAGIC.getColor();
		else if(stack.getItemDamage() == 99)
			return Color.HSBtoRGB(0.75F, 0.5F, 1F);
		else
			return super.getColorFromItemStack(stack,par2);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		try{
			if(stack.getItemDamage()==9||stack.getItemDamage()==10)
			{
				if(!stack.hasTagCompound())
					return;
				ScanResult scan = Utilities.readScanResultFromNBT(stack.getTagCompound().getCompoundTag("scanResult"), player.worldObj);
				if(scan!=null)
				{
					String name = "";
					AspectList aspects = new AspectList();
					switch(scan.type)
					{
					case 1:
						ItemStack scanStack = new ItemStack(Item.getItemById(scan.id), 1, scan.meta);
						name = "\u00a7" + scanStack.getRarity().rarityColor.getFormattingCode()+scanStack.getDisplayName();
						aspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta));
						aspects = ThaumcraftCraftingManager.getBonusTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta), aspects);
						break;
					case 2:
						if ((scan.entity instanceof EntityItem))
						{
							EntityItem item = (EntityItem)scan.entity;
							name = "\u00a7" + item.getEntityItem().getRarity().rarityColor.getFormattingCode()+item.getEntityItem().getDisplayName();
							aspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(item.getEntityItem().getItem(), 1, item.getEntityItem().getItemDamage()));
							aspects = ThaumcraftCraftingManager.getBonusTags(new ItemStack(item.getEntityItem().getItem(), 1, item.getEntityItem().getItemDamage()), aspects);
						}
						else
						{
							name = scan.entity.getCommandSenderName();
							aspects = ScanManager.generateEntityAspects(scan.entity);
						}
						break;
					case 3:
						if(scan.phenomena.startsWith("NODE"))
						{
							name = StatCollector.translateToLocal("tile.blockAiry.0.name");
							aspects = Utilities.generateNodeAspects(player.worldObj, scan.phenomena.replace("NODE", ""));
						}
						break;
					}
					list.add(" "+name);
					if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
					{
						for(Aspect a : aspects.getAspectsSorted())
							if(a!=null && aspects.getAmount(a)>0)
								list.add("  \u00a77"+a.getName()+": "+aspects.getAmount(a));
					}
					else
						list.add("  "+StatCollector.translateToLocal(Lib.DESCRIPTION+"ctrlForAspectList"));
					list.add(" "+StatCollector.translateToLocal(Lib.DESCRIPTION+"scanCompleted")+": "+ScanManager.hasBeenScanned(player, scan));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.icon[0] = iconRegister.registerIcon("witchinggadgets:mat_"+subNames[0]);
		this.icon[1] = iconRegister.registerIcon("witchinggadgets:mat_"+subNames[1]);
		this.icon[2] = iconRegister.registerIcon("witchinggadgets:mat_"+subNames[2]);
		this.icon[3] = iconRegister.registerIcon("witchinggadgets:mat_cloth");
		this.icon[4] = iconRegister.registerIcon("witchinggadgets:mat_cloth");
		this.icon[5] = iconRegister.registerIcon("witchinggadgets:mat_cloth");
		for(int i=6;i<subNames.length;i++)
			this.icon[i] = iconRegister.registerIcon("witchinggadgets:mat_"+subNames[i]);

		iconPlateOverlay = iconRegister.registerIcon("witchinggadgets:mat_"+subNames[9]+"_overlay");
		iconPhotoOverlay = iconRegister.registerIcon("witchinggadgets:mat_"+subNames[10]+"_overlay");
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		if(itemStack.getItemDamage() == 16)
			return new ItemStack(Items.bucket);
		return null;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata)
	{
		if(metadata<icon.length)
			return icon[metadata];
		return icon[0];
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		if(pass == 99)
			return meta==9?iconPlateOverlay : iconPlateOverlay;
		return getIconFromDamage(meta);
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
	}

	@Override
	public int getMetadata (int damageValue)
	{
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int i=0;i<subNames.length;i++)
		{
			if(i==11)
			{
				if(WGContent.BlockDarkIronFluid!=null)
					itemList.add(new ItemStack(item,1,i));
			}
			else
				itemList.add(new ItemStack(item,1,i));
		}
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int targetX, int targetY, int targetZ, int side, float hitX, float hitY, float hitZ)
	{
		if(subNames[stack.getItemDamage()]=="calculator" && !world.isRemote)
		{
			if(world.getTileEntity(targetX,targetY,targetZ) instanceof INode)
			{
				AspectList al = ResearchManager.reduceToPrimals(((INode)world.getTileEntity(targetX,targetY,targetZ)).getAspects());
				player.addChatMessage(new ChatComponentTranslation(Lib.CHAT+"nodeContents.primal",al.getAmount(Aspect.AIR),al.getAmount(Aspect.EARTH),al.getAmount(Aspect.FIRE),al.getAmount(Aspect.WATER),al.getAmount(Aspect.ORDER),al.getAmount(Aspect.ENTROPY)));
				return true;
			}
			if(world.getTileEntity(targetX,targetY,targetZ) instanceof TileInfusionMatrix)
			{
				ArrayList<ChunkCoordinates> stabilizers = new ArrayList();
				ArrayList<ChunkCoordinates> pedestals = new ArrayList();
				ArrayList<Object[]> warnings = new ArrayList();
				//				try
				//				{
				for(int xx=-12; xx<=12; xx++)
					for(int zz=-12; zz<=12; zz++)
					{
						boolean skip = false;
						for(int yy=-10; yy<=5; yy++)
							if(xx!=0 || zz!=0)
							{
								int x = targetX+xx;
								int y = targetY+yy;
								int z = targetZ+zz;

								TileEntity te = world.getTileEntity(x, y, z);
								if(!skip && te!=null && te instanceof TilePedestal)
								{
									if(yy>=0)
										warnings.add(new Object[]{"pedestalHeight",x,y,z});
									else if(Math.abs(xx)>8 || Math.abs(zz)>8)
										warnings.add(new Object[]{"pedestalPos",x,y,z});
									else
									{
										pedestals.add(new ChunkCoordinates(x, y, z));
										skip = true;
									}
								}
								else
								{
									Block bi = world.getBlock(x, y, z);
									if(bi == Blocks.skull || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(world, x, y, z)) )
										stabilizers.add(new ChunkCoordinates(x, y, z));
								}
							}
					}
				int symmetry = 0;
				for(ChunkCoordinates cc : pedestals)
				{
					boolean items = false;
					int dx = targetX - cc.posX;
					int dz = targetZ - cc.posZ;

					TileEntity te = world.getTileEntity(cc.posX, cc.posY, cc.posZ);
					if(te!=null && te instanceof TilePedestal)
					{
						symmetry += 2;
						if( ((TilePedestal)te).getStackInSlot(0) != null)
						{
							symmetry += 1;
							items = true;
						}
					}
					int xx = targetX + dx;
					int zz = targetZ + dz;
					te = world.getTileEntity(xx, cc.posY, zz);
					if(te!=null && te instanceof TilePedestal)
					{
						symmetry -= 2;
						if( ((TilePedestal)te).getStackInSlot(0)!=null)
						{
							if(items)
								symmetry -= 1;
							else
								warnings.add(new Object[]{"noPartnerItem",xx, cc.posY, zz});
						}
					}
					else
						warnings.add(new Object[]{"noPartnerPedestal",cc.posX, cc.posY, cc.posZ});
				}
				float sym = 0.0F;
				for (ChunkCoordinates cc : stabilizers)
				{
					int dx = targetX- cc.posX;
					int dz = targetZ- cc.posZ;
					Block bi = world.getBlock(cc.posX, cc.posY, cc.posZ);
					if(bi==Blocks.skull || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(world, cc.posX, cc.posY, cc.posZ)) )
						sym += 0.1F;
					int xx = targetX+ dx;
					int zz = targetZ+ dz;
					bi = world.getBlock(xx, cc.posY, zz);
					if(bi==Blocks.skull || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(world, xx,cc.posY,zz)) )
						sym -= 0.2F;
					else
						warnings.add(new Object[]{"noPartnerStabilizer",cc.posX, cc.posY, cc.posZ});
				}
				symmetry = ((int)(symmetry + sym));
				player.addChatMessage(new ChatComponentTranslation(Lib.CHAT+"infusionInfo.stabilityTotal",(symmetry*-1)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)) );
				for(Object[] warning : warnings)
				{
					String w = Lib.CHAT+"infusionWarning."+warning[0];
					player.addChatMessage(new ChatComponentTranslation(w,warning[1],warning[2],warning[3]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GRAY)) );
				}
				//				}
				//				catch (Exception e) {}
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(subNames[stack.getItemDamage()]=="cuttingTools" && world.getTileEntity(x, y, z) instanceof TileTable)
		{
			world.setBlock(x, y, z, WGContent.BlockWoodenDevice,3,0x3);
			if(world.getTileEntity(x, y, z) instanceof TileEntityCuttingTable)
			{
				int playerViewQuarter = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
				int f = playerViewQuarter==0 ? 2:playerViewQuarter==1 ? 5:playerViewQuarter==2 ? 3: 4;
				((TileEntityCuttingTable)world.getTileEntity(x, y, z)).facing = f;
			}
			return true;
		}
		return false;
	}
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(stack.getItemDamage() == 10)
		{
			if(stack.hasTagCompound())
			{
				ScanResult scan = Utilities.readScanResultFromNBT(stack.getTagCompound().getCompoundTag("scanResult"), world);
				if(scan != null && !ScanManager.hasBeenScanned(player, scan))
				{
					if(world.isRemote && ScanManager.completeScan(player, scan, "@"))
						PacketHandler.INSTANCE.sendToServer(new PacketScannedToServer(scan, player, "@"));
					player.inventory.decrStackSize(player.inventory.currentItem, 1);
				}
			}
		}
		if(stack.getItemDamage() == 11 && WGContent.BlockDarkIronFluid!=null)
		{
			MovingObjectPosition position = this.getMovingObjectPositionFromPlayer(world, player, false);
			if (position == null)
				return stack;
			if (position.typeOfHit == MovingObjectType.BLOCK)
			{
				int hitX = position.blockX;
				int hitY = position.blockY;
				int hitZ = position.blockZ;

				if (!world.canMineBlock(player, hitX, hitY, hitZ))
					return stack;

				switch(position.sideHit)
				{
				case 0:
					--hitY;
					break;
				case 1:
					++hitY;
					break;
				case 2:
					--hitZ;
					break;
				case 3:
					++hitZ;
					break;
				case 4:
					--hitX;
					break;
				case 5:
					++hitX;
					break;
				}

				if (!player.canPlayerEdit(hitX, hitY, hitZ, position.sideHit, stack)
						||(!world.isAirBlock(hitX, hitY, hitZ) && world.getBlock(hitX, hitY, hitZ).getMaterial().isSolid()))
					return stack;

				world.setBlock(hitX, hitY, hitZ, WGContent.BlockDarkIronFluid, 7, 3);
				return new ItemStack(Items.bucket);
			}
		}
		return stack;
	}

}