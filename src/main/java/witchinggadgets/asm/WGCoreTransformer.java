package witchinggadgets.asm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	@Override
	public byte[] transform(String className, String newClassName, byte[] origCode)
	{
		boolean deobf = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		if (className.equals("thaumcraft.common.items.armor.ItemBootsTraveller"))
			return patchBoots(className, origCode, deobf);
		if (className.equals("thaumcraft.common.items.wands.ItemFocusPouchBauble"))
		{
			byte[] newCode = patchFocusPouch_Interface(className, origCode);
			return patchFocusPouch_Methods(className, newCode);
		}
		if(className.equals(deobf?"net.minecraft.enchantment.EnchantmentHelper":"a"))
		{
			byte[] newCode = patchGetFortuneModifier(origCode, deobf);
			return newCode;
		}
		if(className.equals(deobf?"net.minecraft.entity.EntityLivingBase":"sv"))
		{
			byte[] newCode = patchOnNewPotionEffect(origCode, deobf);
			return newCode;
		}

		return origCode;
	}


	private byte[] patchBoots(String className, byte[] origCode, boolean deobf)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Boots");

		final String methodToPatch = "getIsRepairable";
		final String methodToPatch_obf = "func_82789_a";
		String name = deobf?methodToPatch:methodToPatch_obf;

		ClassReader cr = new ClassReader(origCode);
		ClassWriter cw = new ClassWriter(cr, 0);

		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "boots_getIsRepairable", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
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
	private byte[] patchFocusPouch_Methods(String className, byte[] origCode)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Pouch - Methods");
		final String methodToPatch1 = "canActivate";
		ClassReader cr = new ClassReader(origCode);
		ClassWriter cw = new ClassWriter(cr, 0);

		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC,methodToPatch1, "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Z)Z", null, null);
		mv.visitCode();
		mv.visitFieldInsn(Opcodes.GETSTATIC, "witchinggadgets/common/WGConfig", "coremod_allowFocusPouchActive", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(3, 1);
		mv.visitEnd();

		final String methodToPatch2 = "activate";
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC,methodToPatch2, "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "pouch_activate", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V", false);
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
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching getEnchantmentLevel");

		final String methodToPatch1 = "getFortuneModifier";
		final String methodToPatch_obf1 = "func_77517_e";
		final String desc = "(Lnet/minecraft/entity/EntityLivingBase;)I";
		String name1 = deobf?methodToPatch1:methodToPatch_obf1;

		final String methodToPatch2 = "getLootingModifier";
		final String methodToPatch_obf2 = "func_77519_f";
		String name2 = deobf?methodToPatch2:methodToPatch_obf2;


		ClassReader cr = new ClassReader(origCode);

		ClassNode classNode=new ClassNode();
		cr.accept(classNode, 0);

		for(MethodNode methodNode : classNode.methods)
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
		final String methodToPatch_obf = "func_70670_a";
		final String desc = "(Lnet/minecraft/potion/PotionEffect;)V";
		String name1 = deobf?methodToPatch:methodToPatch_obf;

		ClassReader cr = new ClassReader(origCode);

		ClassNode classNode=new ClassNode();
		cr.accept(classNode, 0);
		for(MethodNode methodNode : classNode.methods)
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
							||insn.getOpcode()==Opcodes.DRETURN)
					{
						InsnList endList=new InsnList();
						endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "witchinggadgets/asm/WGCoreTransformer", "living_onPotionApplied", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/potion/PotionEffect;)V", false));
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
						f_potionAmplifier = PotionEffect.class.getDeclaredField("amplifier");
					if(!f_potionAmplifier.isAccessible())
						f_potionAmplifier.setAccessible(true);
					if(f_potionDuration==null)
						f_potionDuration = PotionEffect.class.getDeclaredField("duration");
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
}