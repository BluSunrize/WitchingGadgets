package witchinggadgets.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGModCompat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagicFood extends ItemFood
{
	private int[] hungerHealed = {2,6,6};
	private float[] saturation = {0.4f,1.5f,1.0f};

	private final static String[] subNames = {
		"sweetwart", "nethercake", "brainjerky"
	};
	private IIcon[] icon = new IIcon[subNames.length];


	public ItemMagicFood()
	{
		super(0,0,false);
		setCreativeTab(WitchingGadgets.tabWG);
		setHasSubtypes(true);
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		--stack.stackSize;
		int meta = stack.getItemDamage();
		player.getFoodStats().addStats(hungerHealed[meta], saturation[meta]);
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		this.onFoodEaten(stack, world, player);
		return stack;
	}

	@Override
	public void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if(stack.getItemDamage()==2 && !world.isRemote)
		{
			int luck = world.rand.nextInt(3);
			for(int pass=0;pass<=luck;pass++)
			{
				Aspect a = Aspect.getPrimalAspects().get(world.rand.nextInt(Aspect.getPrimalAspects().size()));
				short q = (short)(world.rand.nextInt(2) + 1);
				Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), a, q);
				Thaumcraft.proxy.getResearchManager();
				ResearchManager.scheduleSave(player);
				PacketHandler.INSTANCE.sendTo(new PacketAspectPool(a.getTag(), Short.valueOf(q), Short.valueOf(Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), a))), (EntityPlayerMP)player);
			}
		}
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		for(int i=0;i<subNames.length;i++)
			this.icon[i] = iconRegister.registerIcon("witchinggadgets:food_"+subNames[i]);
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
			if(i!=2 || WGModCompat.loaded_TCon)
				itemList.add(new ItemStack(item,1,i));
	}
}
