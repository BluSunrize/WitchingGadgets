package witchinggadgets.asm;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import witchinggadgets.common.WGConfig;

public class WGCoreTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String className, String newClassName, byte[] origCode)
	{
		if (className.equals("thaumcraft.common.items.armor.ItemBootsTraveller"))
			return patchBoots(className, origCode);
		return origCode;
	}

	private byte[] patchBoots(String className, byte[] origCode)
	{
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
}