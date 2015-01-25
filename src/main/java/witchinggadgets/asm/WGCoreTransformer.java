package witchinggadgets.asm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.MathHelper;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;

public class WGCoreTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String className, String newClassName, byte[] origCode)
	{
		if (className.equals("thaumcraft.common.items.armor.ItemBootsTraveller"))
			return patchBoots(className, origCode);
		if (className.equals("thaumcraft.common.items.wands.ItemFocusPouchBauble"))
		{
			byte[] newCode = patchFocusPouch_Interface(className, origCode);
			return patchFocusPouch_Methods(className, newCode);
		}
		//		if (className.equals("thaumcraft.common.items.wands.ItemFocusPouch"))
		//		{
		//			ClassReader cr = new ClassReader(origCode); 
		//			ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw0)
		//			{
		//				@Override
		//				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
		//				{
		//					for(String i : interfaces)
		//						System.out.println(i);
		//				}
		//			};
		//		}
		return origCode;
	}

	private byte[] patchBoots(String className, byte[] origCode)
	{
		WitchingGadgets.logger.log(Level.INFO, "[CORE] Patching Boots");

		final String methodToPatch = "getIsRepairable";
		final String methodToPatch_obf = "func_82789_a";
		boolean deobf = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
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
		return WGConfig.allowBootsRepair && stack2.isItemEqual(new ItemStack(Items.leather));
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
		mv.visitFieldInsn(Opcodes.GETSTATIC, "witchinggadgets/common/WGConfig", "allowFocusPouchActive", "Z");
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
}