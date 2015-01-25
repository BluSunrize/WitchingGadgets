package witchinggadgets.common.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileJarNode;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.api.IInfusedGem;
import witchinggadgets.api.IPrimordialCrafting;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.gui.ContainerPrimordialGlove;
import witchinggadgets.common.gui.InventoryPrimordialGlove;
import witchinggadgets.common.items.ItemInfusedGem;

import com.google.common.collect.Multimap;

public class ItemPrimordialGlove extends Item implements IPrimordialCrafting
{

	public ItemPrimordialGlove()
	{
		super();
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:researchIcon");
	}

	@Override
	public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		super.addInformation(item, par2EntityPlayer, list, par4);
	}


	public static ItemStack[] getSetGems(ItemStack bracelet)
	{
		ItemStack[] gems = new ItemStack[5];
		if( (!(bracelet.getItem() instanceof ItemPrimordialGlove)) || bracelet.getTagCompound() == null)
			return gems;

		NBTTagList list = bracelet.getTagCompound().getTagList("gems", 10);
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			int slot = tag.getByte("Slot") & 0xFF;
			if ((slot >= 0) && (slot < gems.length))
				gems[slot] = ItemStack.loadItemStackFromNBT(tag);
		}
		return gems;
	}
	public static ItemStack setSetGems(ItemStack bracelet, ItemStack[] gems)
	{
		if( (!(bracelet.getItem() instanceof ItemPrimordialGlove)) || gems.length>5)
			return bracelet;

		if(bracelet.getTagCompound() == null)
			bracelet.setTagCompound(new NBTTagCompound());

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < gems.length; i++)
			if (gems[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				gems[i].writeToNBT(tag);
				list.appendTag(tag);
			}
		bracelet.getTagCompound().setTag("gems", list);
		return bracelet;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		super.onUpdate(stack, world, entity, par4, par5);

		if(entity.ticksExisted%10==0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("storedNode"))
		{
			NBTTagCompound nodeTag = stack.getTagCompound().getCompoundTag("storedNode");
			AspectList al = new AspectList();
			al.readFromNBT(nodeTag);

			ItemStack[] gems = ItemPrimordialGlove.getSetGems(stack);
			if(!world.isRemote && ContainerPrimordialGlove.map.containsKey(entity.getEntityId()))
				gems = ((InventoryPrimordialGlove)ContainerPrimordialGlove.map.get(entity.getEntityId()).input).stackList;

			for(ItemStack g: gems)
				if(g!=null && g.isItemDamaged())
				{
					int restored = al.getAmount(ItemInfusedGem.getAspect(g));
					//					System.out.println("restoring: "+restored);
					int newDmg = Math.max(g.getItemDamage()-restored, 0);
					g.setItemDamage(newDmg);
				}
			ItemPrimordialGlove.setSetGems(stack,gems);

			if(!world.isRemote && ContainerPrimordialGlove.map.containsKey(entity.getEntityId()))
			{
				ContainerPrimordialGlove c = ContainerPrimordialGlove.map.get(entity.getEntityId());

				((InventoryPrimordialGlove)c.input).stackList = gems;
				ContainerPrimordialGlove.map.get(entity.getEntityId()).onCraftMatrixChanged(c.input);
				c.detectAndSendChanges();
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		//		if(!world.isRemote)
		//		{
		if(player.isSneaking())
		{
			NoteBlockEvent.Play e = new NoteBlockEvent.Play(world, (int)player.posX,(int)player.posY,(int)player.posZ, 0, 3, 0);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e);
			return stack;
		}

		int sel = stack.hasTagCompound()?stack.getTagCompound().getInteger("selected"):0;
		ItemStack[] gems = getSetGems(stack);
		boolean b = false;
		if(gems!=null && sel>=0 && sel<gems.length && gems[sel]!=null)
		{
			ItemStack gem = gems[sel];
			if(gem.getItem() instanceof IInfusedGem && gem.getItemDamage()+((IInfusedGem)gem.getItem()).getConsumedCharge(ItemInfusedGem.getCut(gem).toString(), ItemInfusedGem.getAspect(gem), player)<=gem.getMaxDamage())
			{
				int brittle = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstoneBrittle.effectId, gem);
				int potency = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstonePotency.effectId, gem);
				b = ((IInfusedGem)gem.getItem()).performEffect(ItemInfusedGem.getCut(gem).toString(), ItemInfusedGem.getAspect(gem), potency, brittle, player);

				if(b && !player.capabilities.isCreativeMode)
				{
					gem.getItem().setDamage(gem, gem.getItemDamage()+((IInfusedGem)gem.getItem()).getConsumedCharge(ItemInfusedGem.getCut(gem).toString(), ItemInfusedGem.getAspect(gem), player) );
					gems[sel] = gem;
					setSetGems(stack, gems);
				}
			}
		}
		if(player.isSneaking() && !b)
		{
			player.openGui(WitchingGadgets.instance, 7, world, (int)player.posX,(int)player.posY,(int)player.posZ);
		}
		//		}
	return super.onItemRightClick(stack, world, player);
	}

	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap multimap = super.getAttributeModifiers(stack);
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 5+ThaumcraftApi.toolMatVoid.getDamageVsEntity(), 0));
		return multimap;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		//		if(tile instanceof TileMirror)
		//		{
		//			TileMirror tm = (TileMirror)world.getTileEntity(x, y, z);
		//			if(tm.linked)
		//			{
		//				int tx=tm.linkX;
		//				int ty=tm.linkY;
		//				int tz=tm.linkZ;
		//				ForgeDirection fd = ForgeDirection.getOrientation(world.getBlockMetadata(tx, ty, tz)%6);
		//				float rot = fd.ordinal()==2?180: fd.ordinal()==4?90: fd.ordinal()==5?270 : 0;
		//				player.setLocationAndAngles(tx+.5+fd.offsetX*.5, ty+fd.offsetY, tz+.5+fd.offsetZ*.5, rot, player.rotationPitch);
		//			}
		//			return !world.isRemote;
		//		}
		if(tile!=null && tile instanceof INode && !(tile instanceof TileJarNode))
		{
			INode node = (INode) tile;
			AspectList primals = new AspectList();
			AspectList temp = ResearchManager.reduceToPrimals(node.getAspects(), true);
			for (Aspect aspect : temp.getAspects())
			{
				int amt = temp.getAmount(aspect);
				if (node.getNodeModifier() == NodeModifier.BRIGHT)
					amt = (int)(amt * 1.2F);
				if (node.getNodeModifier() == NodeModifier.PALE)
					amt = (int)(amt * 0.8F);
				if (node.getNodeModifier() == NodeModifier.FADING)
					amt = (int)(amt * 0.5F);
				amt = MathHelper.floor_double(MathHelper.sqrt_double(amt));
				if (node.getNodeType() == NodeType.UNSTABLE)
					amt += world.rand.nextInt(5) - 2;
				if (amt >= 1)
					primals.merge(aspect, amt);
			}

			setContainedNode(stack, node.getNodeType(), node.getNodeModifier(), primals);
			world.setBlockToAir(x, y, z);
			return true;
		}
		//		else
		//		{
		//			if(!world.isRemote)
		//			{
		//				int sel = stack.hasTagCompound()?stack.getTagCompound().getInteger("selected"):0;
		//				ItemStack[] gems = getSetGems(stack);
		//				boolean b = false;
		//				if(gems!=null && sel>=0 && sel<gems.length && gems[sel]!=null)
		//				{
		//					System.out.println(gems[sel]);
		//					ItemStack gem = gems[sel];
		//					if(gem.getItem() instanceof IInfusedGem && gem.getItemDamage()==0)
		//					{
		//						int brittle = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstoneBrittle.effectId, gem);
		//						int potency = EnchantmentHelper.getEnchantmentLevel(WGContent.enc_gemstonePotency.effectId, gem);
		//						b = ((IInfusedGem)gem.getItem()).performEffect(ItemInfusedGem.getCut(gem).toString(), ItemInfusedGem.getAspect(gem), potency, brittle, player);
		//
		//						if(b && !player.capabilities.isCreativeMode)
		//						{
		//							gem.getItem().setDamage(gem, gem.getMaxDamage());
		//							System.out.println("New Damage "+gem.getItemDamage());
		//							gems[sel] = gem;
		//							setSetGems(stack, gems);
		//						}
		//
		//					}
		//				}
		//				return b;
		////				if(player.isSneaking() && !b)
		////				{
		////					player.openGui(WitchingGadgets.instance, 7, world, (int)player.posX,(int)player.posY,(int)player.posZ);
		////				}
		//			}
		//			
		////			onItemRightClick(stack, world, player);
		//		}

		return false;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{

		//		if(!player.worldObj.isRemote && getMaxItemUseDuration(stack)-count>=100 && stack.getTagCompound()!=null && stack.getTagCompound().hasKey("IIUX") && player.worldObj.getTileEntity(stack.getTagCompound().getInteger("IIUX"),stack.getTagCompound().getInteger("IIUY"),stack.getTagCompound().getInteger("IIUZ")) instanceof INode)
		//		{
		//			INode node = (INode)player.worldObj.getTileEntity(stack.getTagCompound().getInteger("IIUX"),stack.getTagCompound().getInteger("IIUY"),stack.getTagCompound().getInteger("IIUZ"));
		//			setContainedNode(stack, node.getNodeType(), node.getNodeModifier(), node.getAspects());
		//			player.worldObj.setBlockToAir(stack.getTagCompound().getInteger("IIUX"),stack.getTagCompound().getInteger("IIUY"),stack.getTagCompound().getInteger("IIUZ"));
		//		}
	}
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeUsed)
	{
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().removeTag("IIUX");
		stack.getTagCompound().removeTag("IIUY");
		stack.getTagCompound().removeTag("IIUZ");
	}
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.bow;
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		if(!stack.hasTagCompound())
			return 0;
		return stack.getTagCompound().getInteger("visCapacity");
	}

	public ItemStack setContainedNode(ItemStack bracelet, NodeType type, NodeModifier modifier, AspectList aspects)
	{
		if(!bracelet.hasTagCompound())
			bracelet.setTagCompound(new NBTTagCompound());

		NBTTagCompound nodeTag = new NBTTagCompound();
		nodeTag.setInteger("type", type.ordinal());
		if(modifier != null)
			nodeTag.setInteger("modifier", modifier.ordinal());
		aspects.writeToNBT(nodeTag);
		bracelet.getTagCompound().setTag("storedNode", nodeTag);
		bracelet.getTagCompound().setInteger("visCapacity", aspects.visSize());
		return bracelet;
	}

	@Override
	public int getReturnedPearls(ItemStack stack)
	{
		return 1;
	}
}