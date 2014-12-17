package witchinggadgets.common.world;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenMirror extends BiomeGenBase
{
	public int colour_Sky = 0;

	public BiomeGenMirror(int par1)
	{
		super(par1);

		this.rainfall = 0;
		this.theBiomeDecorator = this.createBiomeDecorator();
		this.theBiomeDecorator.bigMushroomsPerChunk = 0;
		this.theBiomeDecorator.cactiPerChunk = 0;
		this.theBiomeDecorator.clayPerChunk = 0;
		this.theBiomeDecorator.deadBushPerChunk = 0;
		this.theBiomeDecorator.flowersPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 0;
		this.theBiomeDecorator.generateLakes = false;

		resetSpawnLists();
	}

	public int getSkyColorByTemp(float par1)
    {
        return this.colour_Sky;
    }
	
	public void resetSpawnLists()
	{
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
	}
	
}
