package witchinggadgets.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.api.aspects.Aspect;

/**
 * This is to be implemented by Blocks!
 * @author BluSunrize
 */
public interface ITerraformFocus
{
	public Aspect requiredAspect(int metadata);
	public Aspect requiredAspect(World world, int x, int y, int z);
	public BiomeGenBase getCreatedBiome(World world, int x, int y, int z);
	public ItemStack getDisplayedBlock(World world, int x, int y, int z);
}