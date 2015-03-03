package witchinggadgets.client;

import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXEssentiaTrail;
import thaumcraft.client.fx.particles.FXWisp;
import travellersgear.api.TravellersGearAPI;
import witchinggadgets.asm.pouch.GuiPatchedFocusPouch;
import witchinggadgets.client.fx.EntityFXSweat;
import witchinggadgets.client.gui.GuiBag;
import witchinggadgets.client.gui.GuiCloakBag;
import witchinggadgets.client.gui.GuiCuttingTable;
import witchinggadgets.client.gui.GuiLabelLibrary;
import witchinggadgets.client.gui.GuiMagicalTileLock;
import witchinggadgets.client.gui.GuiPrimordialGlove;
import witchinggadgets.client.gui.GuiSpinningWheel;
import witchinggadgets.client.gui.GuiVoidBag;
import witchinggadgets.client.render.BlockRenderMetalDevice;
import witchinggadgets.client.render.BlockRenderRoseVine;
import witchinggadgets.client.render.BlockRenderStoneDevice;
import witchinggadgets.client.render.BlockRenderWoodenDevice;
import witchinggadgets.client.render.EntityRenderReforming;
import witchinggadgets.client.render.ItemRenderCapsule;
import witchinggadgets.client.render.ItemRenderMagicalBaubles;
import witchinggadgets.client.render.ItemRenderMaterial;
import witchinggadgets.client.render.ItemRenderPrimordialGauntlet;
import witchinggadgets.client.render.ItemRenderScanCamera;
import witchinggadgets.client.render.ItemRenderWallMirror;
import witchinggadgets.client.render.TileRenderCobbleGen;
import witchinggadgets.client.render.TileRenderCuttingTable;
import witchinggadgets.client.render.TileRenderEssentiaPump;
import witchinggadgets.client.render.TileRenderLabelLibrary;
import witchinggadgets.client.render.TileRenderMagicalTileLock;
import witchinggadgets.client.render.TileRenderSarcophagus;
import witchinggadgets.client.render.TileRenderSaunaStove;
import witchinggadgets.client.render.TileRenderSnowGen;
import witchinggadgets.client.render.TileRenderSpinningWheel;
import witchinggadgets.client.render.TileRenderTerraformFocus;
import witchinggadgets.client.render.TileRenderTerraformer;
import witchinggadgets.client.render.TileRenderWallMirror;
import witchinggadgets.common.CommonProxy;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.blocks.tiles.TileEntityCobbleGen;
import witchinggadgets.common.blocks.tiles.TileEntityCuttingTable;
import witchinggadgets.common.blocks.tiles.TileEntityEssentiaPump;
import witchinggadgets.common.blocks.tiles.TileEntityLabelLibrary;
import witchinggadgets.common.blocks.tiles.TileEntityMagicalTileLock;
import witchinggadgets.common.blocks.tiles.TileEntitySarcophagus;
import witchinggadgets.common.blocks.tiles.TileEntitySaunaStove;
import witchinggadgets.common.blocks.tiles.TileEntitySnowGen;
import witchinggadgets.common.blocks.tiles.TileEntitySpinningWheel;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformFocus;
import witchinggadgets.common.blocks.tiles.TileEntityTerraformer;
import witchinggadgets.common.blocks.tiles.TileEntityWallMirror;
import witchinggadgets.common.items.EntityItemReforming;
import witchinggadgets.common.util.WGKeyHandler;
import baubles.api.BaublesApi;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{

	public static IModelCustom eliteArmorModel;
	public static IModelCustom cameraModel;
	public static IModelCustom gauntletModel;
	public static IModelCustom gemModel;
	public static IModelCustom terraformerModel;

	public void registerRenders()
	{
		RenderingRegistry.registerBlockHandler(new BlockRenderRoseVine());
		RenderingRegistry.registerBlockHandler(new BlockRenderWoodenDevice());
		RenderingRegistry.registerBlockHandler(new BlockRenderStoneDevice());
		RenderingRegistry.registerBlockHandler(new BlockRenderMetalDevice());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallMirror.class, new TileRenderWallMirror());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpinningWheel.class, new TileRenderSpinningWheel());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySnowGen.class, new TileRenderSnowGen());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCobbleGen.class, new TileRenderCobbleGen());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagicalTileLock.class, new TileRenderMagicalTileLock());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySarcophagus.class, new TileRenderSarcophagus());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCuttingTable.class, new TileRenderCuttingTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLabelLibrary.class, new TileRenderLabelLibrary());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaunaStove.class, new TileRenderSaunaStove());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTerraformer.class, new TileRenderTerraformer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTerraformFocus.class, new TileRenderTerraformFocus());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEssentiaPump.class, new TileRenderEssentiaPump());

		eliteArmorModel = ClientUtilities.bindModel("witchinggadgets","models/EliteRunicArmor.obj");
		cameraModel = ClientUtilities.bindModel("witchinggadgets","models/ScanCamera.obj");
		gauntletModel = ClientUtilities.bindModel("witchinggadgets","models/gauntlet.obj");
		gemModel = ClientUtilities.bindModel("witchinggadgets","models/gems.obj");
		terraformerModel = ClientUtilities.bindModel("witchinggadgets", "models/terraformer.obj");


		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(WGContent.BlockWallMirror), new ItemRenderWallMirror());
		MinecraftForgeClient.registerItemRenderer(WGContent.ItemMaterial, new ItemRenderMaterial());
		MinecraftForgeClient.registerItemRenderer(WGContent.ItemScanCamera, new ItemRenderScanCamera());
		MinecraftForgeClient.registerItemRenderer(WGContent.ItemPrimordialGlove, new ItemRenderPrimordialGauntlet());
		//MinecraftForgeClient.registerItemRenderer(WGContent.ItemInfusedGem, new ItemRenderInfusedGem());
		MinecraftForgeClient.registerItemRenderer(WGContent.ItemMagicalBaubles, new ItemRenderMagicalBaubles());
		MinecraftForgeClient.registerItemRenderer(WGContent.ItemCrystalCapsule, new ItemRenderCapsule());

		RenderingRegistry.registerEntityRenderingHandler(EntityItemReforming.class, new EntityRenderReforming());
		//MinecraftForgeClient.registerItemRenderer(WGContent.BlockWoodenDevice.blockID, new ItemRenderSpinningWheel());
	}

	public void registerHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		FMLCommonHandler.instance().bus().register(new WGKeyHandler());
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());

		ThaumonomiconIndexSearcher.init();
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		//System.out.println("Time to Open a gui, Clientside, ID: "+ID);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(ID == 0)return new GuiSpinningWheel(player.inventory, (TileEntitySpinningWheel)tile);

		if(ID == 3)return new GuiBag(player.inventory, world);
		if(ID==4||ID==5)return new GuiCloakBag(player.inventory, world, ID==4?TravellersGearAPI.getExtendedInventory(player)[0]:BaublesApi.getBaubles(player).getStackInSlot(3) );
		
		if(ID == 6)return new GuiPatchedFocusPouch(player.inventory, world, x, y, z);

		if(ID == 7)return new GuiPrimordialGlove(player.inventory, world, x, y, z);
		
		if(ID == 8)return new GuiLabelLibrary(player.inventory, (TileEntityLabelLibrary)tile);

		if(ID == 9)return new GuiCuttingTable(player.inventory, (TileEntityCuttingTable)tile);

		if(ID == 10)return new GuiMagicalTileLock((TileEntityMagicalTileLock) tile);

		if(ID == 11)return new GuiVoidBag(player.inventory, world);

		return null;
	}

	@Override
	public void createEssentiaTrailFx(World worldObj, int x, int y, int z, int tx, int ty, int tz, int count, int colour, float scale)
	{
		FXEssentiaTrail fx = new FXEssentiaTrail(worldObj, x + 0.5D, y + 1, z + 0.5D, tx + 0.5D, ty + 0.5D, tz + 0.5D, count, colour, scale);
		ParticleEngine.instance.addEffect(worldObj, fx);
	}
	@Override
	public void createTargetedWispFx(World worldObj, double x, double y, double z, double tx, double ty, double tz, int colour, float scale, float gravity, boolean tinkle, boolean noClip)
	{
		FXWisp fx = new FXWisp(worldObj, x,y,z, tx,ty,tz, scale, 0);
		fx.setAlphaF(1);
		fx.tinkle= tinkle;
		fx.noClip = noClip;
		fx.setRBGColorF((colour>>16&255)/255f, (colour>>8&255)/255f, (colour&255)/255f);
		fx.setGravity(0);
		ParticleEngine.instance.addEffect(worldObj, fx);
	}
	@Override
	public void createSweatFx(EntityPlayer player)
	{
		EntityFXSweat fx = new EntityFXSweat(player);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
	}
	@Override
	public void createFurnaceOutputBlobFx(World worldObj, int x, int y, int z, ForgeDirection facing)
	{	
		float xx = x+.5f+facing.offsetX*1.66f + worldObj.rand.nextFloat()*.3f;
		float zz = z+.5f+facing.offsetZ*1.66f + worldObj.rand.nextFloat()*.3f;

		EntityLavaFX fb = new EntityLavaFX(worldObj, xx,y+1.3f,zz);
		fb.motionY = .2f*worldObj.rand.nextFloat();
		float mx = facing.offsetX!=0?(worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f : facing.offsetX*worldObj.rand.nextFloat();
		float mz = facing.offsetZ!=0?(worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f : facing.offsetZ*worldObj.rand.nextFloat();
		fb.motionX = (0.15f * mx);
		fb.motionZ = (0.15f * mz);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
	}
	@Override
	public void createFurnaceDestructionBlobFx(World worldObj, int x, int y, int z)
	{	
		float xx = x+.5f+ worldObj.rand.nextFloat()*.3f;
		float zz = z+.5f+ worldObj.rand.nextFloat()*.3f;

		EntityLavaFX fb = new EntityLavaFX(worldObj, xx,y+1.5f,zz);
		fb.motionY = .2F;
		fb.motionX = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f*.15f;
		fb.motionZ = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat())*.5f*.15f;
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
	}
}
