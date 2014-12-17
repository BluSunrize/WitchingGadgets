//package witchinggadgets.common.world;
//
//import net.minecraft.entity.Entity;
//import net.minecraft.util.MathHelper;
//import net.minecraft.util.Vec3;
//import net.minecraft.world.WorldProvider;
//import net.minecraft.world.biome.WorldChunkManagerHell;
//import net.minecraft.world.chunk.Chunk;
//import net.minecraft.world.chunk.IChunkProvider;
//import net.minecraftforge.client.IRenderHandler;
//import witchinggadgets.WitchingGadgets;
//import witchinggadgets.client.render.NotRenderer;
//import witchinggadgets.common.WGConfig;
//
//public class WorldProviderMirror extends WorldProvider
//{
//	@Override
//	public String getDimensionName()
//	{
//		return "Crystal Void";
//	}
//
//	@Override
//	public void registerWorldChunkManager()
//	{
//		this.worldChunkMgr = new WorldChunkManagerHell(WitchingGadgets.BiomeMirror, 0.0F);
//		this.dimensionId = WGConfig.dimensionMirrorID;
//	}
//
//
//
//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderMirror(worldObj, worldObj.getSeed(), true);
//	}
//
//	@Override
//	public float calculateCelestialAngle(long par1, float par3)
//	{
//		//this.worldObj.setRainStrength(0);
//		//return super.calculateCelestialAngle(par1, par3);
//		return -0.28F;
//	}
//
//	@Override
//	public int getMoonPhase(long par1)
//	{
//		return 4;
//		//return (int)(par1 / 24000L) % 8;
//	}
//
//	@Override
//	public Vec3 drawClouds(float partialTicks)
//	{
//		int x = 0;
//		int y = 0;
//		return Vec3.createVectorHelper(x, y, y);
//		//return worldObj.drawCloudsBody(partialTicks);
//	}
//
//	@Override
//	public long getWorldTime()
//	{
//		return 18000;
//	}
//
//	@Override
//	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
//	{
//		double r = 176;
//		double g = 230;
//		double b = 224;
//
//		float f3 = (float) (r/255.0);
//		float f4 = (float) (g/255.0);
//		float f5 = (float) (b/255.0);
//
//		//return super.getSkyColor(cameraEntity, partialTicks);
//
//		return Vec3.createVectorHelper(f3, f4, f5);
//	}
//
//	@Override
//	public Vec3 getFogColor(float par1, float par2)
//	{
//		//System.out.println("FogColour: par1="+par1+", par2="+par2);
//		float f2 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
//
//		if (f2 < 0.0F)
//		{
//			f2 = 0.0F;
//		}
//
//		if (f2 > 1.0F)
//		{
//			f2 = 1.0F;
//		}
//
//		float f3 = 0.7529412F;
//		float f4 = 0.84705883F;
//		float f5 = 1.0F;
//		f3 *= f2 * 0.94F + 0.06F;
//		f4 *= f2 * 0.94F + 0.06F;
//		f5 *= f2 * 0.91F + 0.09F;
//
//		double r = 45;
//		double g = 119;
//		double b = 161;
//
//		f3 = (float) (r/255.0);
//		f4 = (float) (g/255.0);
//		f5 = (float) (b/255.0);
//
//		//DimensionManager.getProvider(dim)
//
//		//this.worldObj.provider.getFogColor(par1, par2)
//
//		return Vec3.createVectorHelper(f3, f4, f5);
//	}
//
//	@Override
//	public float[] calcSunriseSunsetColors(float par1, float par2)
//	{
//		//return super.calcSunriseSunsetColors(par1, par2);
//		return null;
//	}
//
//	@Override
//	public boolean isSkyColored()
//	{
//		//return super.isSkyColored();
//		return false;
//	}
//
//	@Override
//	public boolean canRespawnHere()
//	{
//		return false;
//	}
//
//	@Override
//	public boolean isSurfaceWorld()
//	{
//		return super.isSurfaceWorld();
//		//return false;
//	}
//
//	@Override
//	public float getCloudHeight()
//	{
//		//return super.getCloudHeight();
//		return -200.0F;
//	}
//
//	@Override
//	public IRenderHandler getSkyRenderer()
//	{
//		return null;//new BetweenSkyRenderer(this);
//	}
//
//	@Override
//	public IRenderHandler getCloudRenderer()
//	{
//		return new NotRenderer();
//	}
//
//	@Override
//	public double getHorizon()
//	{
//		return -256.0;
//		//return worldObj.worldInfo.getTerrainType().getHorizon(worldObj);
//	}
//
//	@Override
//	public boolean doesXZShowFog(int par1, int par2)
//	{
//		//return true;
//		//return super.doesXZShowFog(par1, par2);
//		return false;
//	}
//
//	@Override
//	public boolean getWorldHasVoidParticles()
//	{
//		return false;
//	}
//
//	@Override
//	public double getVoidFogYFactor()
//	{
//		return 1f;
//	}
//
//
//	@Override
//	public boolean canDoRainSnowIce(Chunk chunk)
//	{
//		return false;
//	}
//
//	@Override
//	public boolean canSnowAt(int x, int y, int z, boolean checkLight)
//	{
//		return false;
//	}
//
//}