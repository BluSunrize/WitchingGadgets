package witchinggadgets.asm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import thaumcraft.common.config.Config;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.items.armor.ItemPrimordialArmor;
import witchinggadgets.common.items.baubles.ItemMagicalBaubles;
import baubles.api.BaublesApi;

public class WGCoreTransformer implements IClassTransformer
{
	static boolean isDeobfEnvironment;
	@Override
	public byte[] transform(String className, String newClassName, byte[] origCode)
	{
		isDeobfEnvironment = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		if (className.equals("thaumcraft.common.items.armor.ItemBootsTraveller"))
			return patchBoots(className, origCode, isDeobfEnvironment);
		if (className.equals("thaumcraft.common.items.wands.ItemFocusPouchBauble"))
		{
			byte[] newCode = patchFocusPouch_Interface(className, origCode);
			return patchFocusPouch_Methods(className, newCode,isDeobfEnvironment);
		}
		if (className.equals("thaumcraft.common.lib.world.WorldGenEldritchRing"))
			return patchThaumcraftWorldgen(origCode, isDeobfEnvironment, "EldritchRing");
		if (className.equals("thaumcraft.common.lib.world.WorldGenHilltopStones"))
			return patchThaumcraftWorldgen(origCode, isDeobfEnvironment, "HilltopStones");

		if(className.equals(isDeobfEnvironment?"net.minecraft.enchantment.EnchantmentHelper":"afv"))
		{
			byte[] newCode = patchGetFortuneModifier(origCode, isDeobfEnvironment);
			return newCode;
		}
		if(className.equals(isDeobfEnvironment?"net.minecraft.entity.EntityLivingBase":"sv"))
		{
			byte[] newCode = patchOnNewPotionEffect(origCode, isDeobfEnvironment);
			return newCode;
		}

		return origCode;
	}


	private byte[] patchBoots(String className, byte[] origCode, boolean deobf)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Boots");

		final String methodToPatch = "getIsRepairable";
		final String methodToPatch_obf = "a";
		String name = deobf?methodToPatch:methodToPatch_obf;
		String desc = deobf?"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z":"(Ladd;Ladd;)Z";

		ClassReader cr = new ClassReader(origCode);
		ClassWriter cw = new ClassWriter(cr, 0);

		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, desc, null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "boots_getIsRepairable", desc, false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(2, 1);
		mv.visitEnd();

		cr.accept(cw, 0);
		return cw.toByteArray();
	}
	public static boolean boots_getIsRepairable(ItemStack stack1, ItemStack stack2)
	{
		return WGConfig.coremod_allowBootsRepair && stack2.isItemEqual(new ItemStack(Items.leather));
	}



	private byte[] patchFocusPouch_Interface(String className, byte[] origCode)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Pouch - Interfaces");
		ClassReader cr = new ClassReader(origCode);
		ClassWriter cw = new ClassWriter(cr, 0);
		ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw)
		{
			@Override
			public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
			{
				Set<String> intf = new HashSet();
				intf.addAll(Arrays.asList(interfaces));
				intf.add("travellersgear/api/IActiveAbility");
				super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
			}
		};
		cr.accept(cv, 0);

		return cw.toByteArray();
	}
	private byte[] patchFocusPouch_Methods(String className, byte[] origCode, boolean deobf)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Pouch - Methods");
		final String methodToPatch1 = "canActivate";
		ClassReader cr = new ClassReader(origCode);
		ClassWriter cw = new ClassWriter(cr, 0);
		String desc1 = deobf?"(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Z)Z":"(Lyz;Ladd;Z)Z";

		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC,methodToPatch1, desc1, null, null);
		mv.visitCode();
		mv.visitFieldInsn(Opcodes.GETSTATIC, "witchinggadgets/common/WGConfig", "coremod_allowFocusPouchActive", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(3, 1);
		mv.visitEnd();

		final String methodToPatch2 = "activate";
		String desc2 = deobf?"(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V":"(Lyz;Ladd;)V";
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC,methodToPatch2, desc2, null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "pouch_activate", desc2, false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(2, 1);
		mv.visitEnd();

		cr.accept(cw, 0);
		return cw.toByteArray();
	}
	public static void pouch_activate(EntityPlayer player, ItemStack stack)
	{
		if (!player.worldObj.isRemote)
			player.openGui(WitchingGadgets.instance, 6, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
	}



	private byte[] patchGetFortuneModifier(byte[] origCode, boolean deobf)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching getFortuneModifier & getLootingModifier");

		final String methodToPatch1 = "getFortuneModifier";
		final String methodToPatch_obf1 = "f";
		final String desc = "(Lsv;)I";
		String name1 = deobf?methodToPatch1:methodToPatch_obf1;

		final String methodToPatch2 = "getLootingModifier";
		final String methodToPatch_obf2 = "i";
		String name2 = deobf?methodToPatch2:methodToPatch_obf2;


		ClassReader cr = new ClassReader(origCode);

		ClassNode classNode=new ClassNode();
		cr.accept(classNode, 0);

		for(MethodNode methodNode : classNode.methods)
		{
			if(methodNode.name.equals(name1) && methodNode.desc.equals(desc))
			{
				Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
				while(insnNodes.hasNext())
				{
					AbstractInsnNode insn=insnNodes.next();

					if(insn.getOpcode()==Opcodes.IRETURN
							||insn.getOpcode()==Opcodes.RETURN
							||insn.getOpcode()==Opcodes.ARETURN
							||insn.getOpcode()==Opcodes.LRETURN
							||insn.getOpcode()==Opcodes.DRETURN){
						InsnList endList=new InsnList();
						endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "enchantment_getFortuneLevel", desc, false));
						methodNode.instructions.insertBefore(insn, endList);
					}
				}
			}
			else if(methodNode.name.equals(name2) && methodNode.desc.equals(desc))
			{
				Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
				while(insnNodes.hasNext())
				{
					AbstractInsnNode insn=insnNodes.next();

					if(insn.getOpcode()==Opcodes.IRETURN
							||insn.getOpcode()==Opcodes.RETURN
							||insn.getOpcode()==Opcodes.ARETURN
							||insn.getOpcode()==Opcodes.LRETURN
							||insn.getOpcode()==Opcodes.DRETURN){
						InsnList endList=new InsnList();
						endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "enchantment_getLootingLevel", desc, false));
						methodNode.instructions.insertBefore(insn, endList);
					}
				}
			}
		}
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(cw);

		return cw.toByteArray();
	}
	public static int enchantment_getFortuneLevel(EntityLivingBase living)
	{
		int base = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, living.getHeldItem());
		if(WGConfig.coremod_allowEnchantModifications && living instanceof EntityPlayer)
			for(int i=0; i<BaublesApi.getBaubles((EntityPlayer)living).getSizeInventory(); i++)
			{
				ItemStack bStack = BaublesApi.getBaubles((EntityPlayer)living).getStackInSlot(i);
				if(bStack!=null && bStack.getItem().equals(WGContent.ItemMagicalBaubles)&&ItemMagicalBaubles.subNames[bStack.getItemDamage()].equals("ringLuck"))
				{
					base +=2;
				}
			}
		return base;
	}
	public static int enchantment_getLootingLevel(EntityLivingBase living)
	{
		int base = EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, living.getHeldItem());
		if(WGConfig.coremod_allowEnchantModifications && living instanceof EntityPlayer)
			for(int i=0; i<BaublesApi.getBaubles((EntityPlayer)living).getSizeInventory(); i++)
			{
				ItemStack bStack = BaublesApi.getBaubles((EntityPlayer)living).getStackInSlot(i);
				if(bStack!=null && bStack.getItem().equals(WGContent.ItemMagicalBaubles)&&ItemMagicalBaubles.subNames[bStack.getItemDamage()].equals("ringLuck"))
				{
					base +=2;
				}
			}
		return base;
	}



	private byte[] patchOnNewPotionEffect(byte[] origCode, boolean deobf)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching onNewPotionEffect");

		final String methodToPatch = "onNewPotionEffect";
		final String methodToPatch_obf = "a";
		final String desc1 = deobf?"(Lnet/minecraft/potion/PotionEffect;)V":"(Lrw;)V";
		final String desc2 = deobf?"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/potion/PotionEffect;)V":"(Lsv;Lrw;)V";
		String name1 = deobf?methodToPatch:methodToPatch_obf;

		ClassReader cr = new ClassReader(origCode);

		ClassNode classNode=new ClassNode();
		cr.accept(classNode, 0);
		for(MethodNode methodNode : classNode.methods)
			if(methodNode.name.equals(name1) && methodNode.desc.equals(desc1))
			{
				Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
				while(insnNodes.hasNext())
				{
					AbstractInsnNode insn=insnNodes.next();
					if(insn.getOpcode()==Opcodes.IRETURN
							||insn.getOpcode()==Opcodes.RETURN
							||insn.getOpcode()==Opcodes.ARETURN
							||insn.getOpcode()==Opcodes.LRETURN
							||insn.getOpcode()==Opcodes.DRETURN)
					{
						InsnList endList=new InsnList();
						endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "living_onPotionApplied", desc2, false));
						methodNode.instructions.insertBefore(insn, endList);
					}
				}
			}
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(cw);

		return cw.toByteArray();		
	}
	static Field f_potionAmplifier;
	static Field f_potionDuration;
	public static void living_onPotionApplied(EntityLivingBase living, PotionEffect effect)
	{
		if(WGConfig.coremod_allowPotionApplicationMod && effect!=null && !living.worldObj.isRemote)
		{
			int id = effect.getPotionID();
			if(id==Config.potionVisExhaustID
					||id==Config.potionThaumarhiaID
					||id==Config.potionUnHungerID
					||id==Config.potionBlurredID
					||id==Config.potionSunScornedID
					||id==Config.potionInfVisExhaustID
					||id==Config.potionDeathGazeID)
			{
				int ordo = 0;
				for(int i=1; i<=4; i++)
				{
					ItemStack armor = living.getEquipmentInSlot(i);
					if(armor!=null && armor.getItem()!=null && armor.getItem() instanceof ItemPrimordialArmor && ((ItemPrimordialArmor)armor.getItem()).getAbility(armor)==4)
						ordo++;
				}

				try{
					if(f_potionAmplifier==null)
						f_potionAmplifier = PotionEffect.class.getDeclaredField(isDeobfEnvironment?"amplifier":"c");
					if(!f_potionAmplifier.isAccessible())
						f_potionAmplifier.setAccessible(true);
					if(f_potionDuration==null)
						f_potionDuration = PotionEffect.class.getDeclaredField(isDeobfEnvironment?"duration":"b");
					if(!f_potionDuration.isAccessible())
						f_potionDuration.setAccessible(true);

					int val_Amp = f_potionAmplifier.getInt(effect);
					if(val_Amp>0)
						val_Amp = Math.max(0, val_Amp-ordo);
					f_potionAmplifier.setInt(effect, val_Amp);

					int val_Dur = f_potionDuration.getInt(effect);
					if(val_Dur>0)
						val_Dur /= (ordo+1);
					f_potionDuration.setInt(effect, val_Dur);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}



	private byte[] patchThaumcraftWorldgen(byte[] origCode, boolean deobf, String ident)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Thaumcraft's Worldgen");

		final String methodToPatch = "GetValidSpawnBlocks";
		final String desc = deobf?"()[Lnet/minecraft/block/Block;":"()[Laji;";

		ClassReader cr = new ClassReader(origCode);

		ClassNode classNode=new ClassNode();
		cr.accept(classNode, 0);
		for(MethodNode methodNode : classNode.methods)
			if(methodNode.name.equals(methodToPatch) && methodNode.desc.equals(desc))
			{
				Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
				while(insnNodes.hasNext())
				{
					AbstractInsnNode insn=insnNodes.next();
					if(insn.getOpcode()==Opcodes.ARETURN)
					{
						InsnList endList=new InsnList();
						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "worldGen_getValid"+ident, desc, false));
						methodNode.instructions.insertBefore(insn, endList);
					}
				}
			}
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(cw);

		return cw.toByteArray();	
	}
	public static Block[] worldGen_getValidHilltopStones()
	{
		return WGConfig.coremod_worldgenValidBase_HilltopStones;
	}
	public static Block[] worldGen_getValidEldritchRing()
	{
		return WGConfig.coremod_worldgenValidBase_EldritchRing;
	}
}