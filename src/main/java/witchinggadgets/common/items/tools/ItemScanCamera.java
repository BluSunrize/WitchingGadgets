package witchinggadgets.common.items.tools;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.util.Utilities;

public class ItemScanCamera extends Item {

	public ItemScanCamera()
	{
		super();
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setCreativeTab(WitchingGadgets.tabWG);
	}
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("thaumcraft:blank");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
			ItemStack photo = new ItemStack(WGContent.ItemMaterial,1,9);
			photo.setTagCompound(new NBTTagCompound());
			ScanResult scan = doScan(stack, world, player, 0);
			if(scan != null && ScanManager.validScan(ScanManager.getScanAspects(scan, world), player))
			{
				NBTTagCompound scanTag = Utilities.writeScanResultToNBT(scan);
				photo.getTagCompound().setTag("scanResult", scanTag);
				boolean takePhoto = false;
				for(int slot = 0;slot<player.inventory.mainInventory.length;slot++)
				{
					ItemStack item = player.inventory.mainInventory[slot];
					if(item != null && item.getItem() == ConfigItems.itemResource	&& item.getItemDamage() == 10)
					{
						player.inventory.decrStackSize(slot, 1);
						takePhoto = true;
						break;
					}
				}
				if(takePhoto)
				{
					if(player.inventory.addItemStackToInventory(photo))
						player.dropPlayerItemWithRandomChoice(photo, false);
					player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:cameradone", 0.3F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
					player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:cameraticks", 0.3F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
				}
			}
		return stack;
	}

	/**
	 * Method completely copied from ItemThaumometer
	 */
	private ScanResult doScan(ItemStack stack, World world, EntityPlayer p, int count)
	{
		Entity pointedEntity = EntityUtils.getPointedEntity(p.worldObj, p, 0.5D, 10.0D, 0.0F, true);
		if (pointedEntity != null)
		{
			ScanResult sr = new ScanResult((byte)2, 0, 0, pointedEntity, "");
			//			if (ScanManager.isValidScanTarget(p, sr, "@"))
			//			{
			Thaumcraft.proxy.blockRunes(world, pointedEntity.posX - 0.5D, pointedEntity.posY + pointedEntity.getEyeHeight() / 2.0F, pointedEntity.posZ - 0.5D, 0.3F + world.rand.nextFloat() * 0.7F, 0.0F, 0.3F + world.rand.nextFloat() * 0.7F, (int)(pointedEntity.height * 15.0F), 0.03F);
			return sr;
			//			}
			//			return null;
		}
		MovingObjectPosition mop = getMovingObjectPositionFromPlayer(p.worldObj, p, true);
		if ((mop != null) && (mop.typeOfHit == MovingObjectType.BLOCK))
		{
			TileEntity tile = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
			if ((tile instanceof INode))
			{
				ScanResult sr = new ScanResult((byte)3, 0, 0, null, "NODE" + ((INode)tile).getId());
				//				if (ScanManager.isValidScanTarget(p, sr, "@"))
				//				{
				Thaumcraft.proxy.blockRunes(world, mop.blockX, mop.blockY + 0.25D, mop.blockZ, 0.3F + world.rand.nextFloat() * 0.7F, 0.0F, 0.3F + world.rand.nextFloat() * 0.7F, 15, 0.03F);
				return sr;
				//				}
				//				return null;
			}
			Block b = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
			if(b != null)
			{
				int bi = Block.getIdFromBlock(p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ));
				int md = b.getDamageValue(world, mop.blockX, mop.blockY, mop.blockZ);
				ItemStack is = b.getPickBlock(mop, p.worldObj, mop.blockX, mop.blockY, mop.blockZ, p);
				ScanResult sr = null;
				try
				{
					if (is == null) {
						is = BlockUtils.createStackedBlock(b, md);
					}
				}
				catch (Exception e) {}
				if (is == null) {
					sr = new ScanResult((byte)1, bi, md, null, "");
				} else {
					sr = new ScanResult((byte)1, Item.getIdFromItem(is.getItem()), is.getItemDamage(), null, "");
				}
				//				if (ScanManager.isValidScanTarget(p, sr, "@"))
				//				{
				Thaumcraft.proxy.blockRunes(world, mop.blockX, mop.blockY + 0.25D, mop.blockZ, 0.3F + world.rand.nextFloat() * 0.7F, 0.0F, 0.3F + world.rand.nextFloat() * 0.7F, 15, 0.03F);
				return sr;
				//				}
				//				return null;
			}
		}
		for (IScanEventHandler seh : ThaumcraftApi.scanEventhandlers)
		{
			ScanResult scan = seh.scanPhenomena(stack, world, p);
			if (scan != null) {
				return scan;
			}
		}
		return null;
	}


}