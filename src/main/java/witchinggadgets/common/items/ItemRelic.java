package witchinggadgets.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRelic extends Item
{
	public IIcon[] icon = new IIcon[64];

	private final static String[] subNames = {
		"hourglass", "dawnStone", "duskStone", "homewardBone"
	};

	public ItemRelic()
	{
		super();
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useTime)
	{
		double[] itemPos = {player.posX,player.posY,player.posZ};
		if(useTime > 0)
			return;
		switch(stack.getItemDamage())
		{
		case 0:
			List<Entity> l = world.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(player.posX-4, player.posY-3, player.posZ-4, player.posX+4, player.posY+3, player.posZ+4));
			for(Entity ent: l)
				if(ent instanceof EntityLiving)
					((EntityLiving) ent).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,120,6));
			player.worldObj.playSound(player.posX, player.posY, player.posZ, "random.glass", 0.4F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			player.worldObj.playSound(player.posX, player.posY, player.posZ, "fireworks.twinkle_far", 0.05F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			break;
		case 1:
			if(!world.isRemote)
				MinecraftServer.getServer().worldServers[player.dimension].setWorldTime(0);
			player.worldObj.playSound(player.posX, player.posY, player.posZ, "random.glass", 0.4F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			player.worldObj.playSound(player.posX, player.posY, player.posZ, "fireworks.twinkle_far", 0.05F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			break;
		case 2:
			if(!world.isRemote)
				MinecraftServer.getServer().worldServers[player.dimension].setWorldTime(12500);
			player.worldObj.playSound(player.posX, player.posY, player.posZ, "random.glass", 0.4F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			player.worldObj.playSound(player.posX, player.posY, player.posZ, "fireworks.twinkle_far", 0.05F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			break;
		case 3:
			ChunkCoordinates cc = player.getBedLocation(player.dimension);
			player.setPositionAndUpdate(cc.posX, cc.posY, cc.posZ);
			player.worldObj.playSound(cc.posX, cc.posY, cc.posZ, "portal.trigger", 0.4F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			player.worldObj.playSound(cc.posX, cc.posY, cc.posZ, "fireworks.twinkle_far", 0.05F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
			itemPos = new double[]{cc.posX,cc.posY,cc.posZ};
			break;
		}

		if(!world.isRemote)
			world.spawnEntityInWorld(new EntityItemReforming(world, itemPos[0], itemPos[1], itemPos[2], stack.copy()));
		--stack.stackSize;
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.stopUsingItem();
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 60;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		for(int i=0;i<subNames.length;i++)
			this.icon[i] = iconRegister.registerIcon("witchinggadgets:relic_"+subNames[i]);
	}
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata)
	{
		return icon[metadata];
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int i=0;i<subNames.length;i++)
		{
			itemList.add(new ItemStack(this,1,i));
		}
	}
}