//package witchinggadgets.common.util;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.MathHelper;
//
//import org.apache.logging.log4j.Level;
//
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.aspects.AspectList;
//import witchinggadgets.WitchingGadgets;
//import witchinggadgets.client.ClientUtilities;
//
//public class Cloak
//{
//	public static Cloak standard = new Cloak("STANDARD",100,"witchinggadgets:textures/models/cloak.png", ClientUtilities.colour_CloakBlue, 3);
//	public static Cloak invisible = new Cloak("INVISIBILITY",30,"witchinggadgets:textures/models/cloakInvisibility.png", ClientUtilities.colour_CloakNil, new AspectList().add(Aspect.ORDER, 4).add(Aspect.ENTROPY, 4).add(Aspect.AIR, 2));
//	public static Cloak raven = new Cloak("RAVEN",0,"witchinggadgets:textures/models/cloakRaven.png", ClientUtilities.colour_CloakRaven, new AspectList().add(Aspect.ENTROPY, 5).add(Aspect.AIR, 6));
//	public static Cloak storage = new Cloak("STORAGE",0,"witchinggadgets:textures/models/cloak.png", ClientUtilities.colour_CloakStorage, new AspectList().add(Aspect.ORDER, 5).add(Aspect.EARTH, 5));
//	public static Cloak wolf = new Cloak("WOLF",0,"witchinggadgets:textures/models/cloakWolf.png", ClientUtilities.colour_CloakStorage, new AspectList().add(Aspect.ORDER, 6).add(Aspect.ENTROPY, 6));
//
//	public static String[] cloakTagList = {"STANDARD","INVISIBILITY","RAVEN","STORAGE","WOLF"};
//
//	private String unlocalizedName;
//	private int maxDurability;
//	private String texture;
//	private int defaultColour;
//	private AspectList visDiscount;
//
//
//	public Cloak(String tag, int maxDmg, String texPath, int c, Object vD)
//	{
//		this.unlocalizedName = tag;
//		this.maxDurability = maxDmg;
//		this.texture = texPath;
//		this.defaultColour = c;
//		if(vD instanceof AspectList)
//			this.visDiscount = (AspectList)vD;
//		else if(vD instanceof Integer)
//		{
//			AspectList al = new AspectList();
//			for(Aspect a:Aspect.getPrimalAspects())
//				al.add(a, (Integer)vD);
//			this.visDiscount = al;
//		}
//		else
//		{
//			String error = "Error on initializing Cloak with tag: "+tag+". Vis Discounter must either be an AspectList or an Integer for overall discount.";
//			WitchingGadgets.logger.log(Level.ERROR, error);
//		}
//	}
//
//	public void onItemUpdate(EntityPlayer user, ItemStack stack)
//	{
//		if(this.getUnlocalizedName().equals(invisible.getUnlocalizedName()))
//			if(user.getActivePotionEffect(Potion.invisibility) == null || user.getActivePotionEffect(Potion.invisibility).getDuration()<2)
//				user.addPotionEffect(new PotionEffect(Potion.invisibility.id, 3));
//	}
//
//	public void onPlayerTakenDamage(EntityPlayer user, ItemStack stack, DamageSource source, float damage)
//	{
//		if(source == null || damage <= 0)
//			return;
//		if(this.getUnlocalizedName().equals(wolf.getUnlocalizedName()))
//		{
//			int amp = 1;
//			if(damage >= 8)
//				amp++;
//			if(damage >= 12)
//				amp++;
//			if(user.getActivePotionEffect(Potion.damageBoost) == null || user.getActivePotionEffect(Potion.damageBoost).getDuration()<2)
//				user.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 60, amp));
//			if(user.getActivePotionEffect(Potion.moveSpeed) == null || user.getActivePotionEffect(Potion.moveSpeed).getDuration()<2)
//				user.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 60, amp));
//			if(user.getActivePotionEffect(Potion.resistance) == null || user.getActivePotionEffect(Potion.resistance).getDuration()<2)
//				user.addPotionEffect(new PotionEffect(Potion.resistance.id, 60, amp));
//		}
//	}
//
//	public void onCloakUsed(EntityPlayer user, ItemStack stack)
//	{
//		//user.addChatMessage("Cloak was used");
//
//		if(this.getUnlocalizedName().equals(storage.getUnlocalizedName()))
//		{
//			user.openGui(WitchingGadgets.instance, 4, user.worldObj, MathHelper.floor_double(user.posX), MathHelper.floor_double(user.posY), MathHelper.floor_double(user.posZ));
//		}
//		if(this.getUnlocalizedName().equals(raven.getUnlocalizedName()))
//		{
//			user.openGui(WitchingGadgets.instance, 5, user.worldObj, MathHelper.floor_double(user.posX), MathHelper.floor_double(user.posY), MathHelper.floor_double(user.posZ));
//		}
//	}
//	public String getUnlocalizedName()
//	{
//		return this.unlocalizedName;
//	}
//	public String getTexture()
//	{
//		return this.texture;
//	}
//	public int getMaxDamage()
//	{
//		return this.maxDurability;
//	}
//	public int getDefaultColour()
//	{
//		return this.defaultColour;
//	}
//	public AspectList getVisDiscount()
//	{
//		return this.visDiscount;
//	}
//}
