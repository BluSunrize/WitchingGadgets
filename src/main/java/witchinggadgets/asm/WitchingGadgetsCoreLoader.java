package witchinggadgets.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name(WitchingGadgetsCoreLoader.NAME)
public class WitchingGadgetsCoreLoader implements IFMLLoadingPlugin
{
	public static final String NAME = "WitchingGadgets Core";
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{WGCoreTransformer.class.getName()};
	}
	@Override
	public String getModContainerClass() {
		return WitchingGadgetsCore.class.getName();
	}
	@Override
	public String getSetupClass() {
		return null;
	}
	@Override
	public void injectData(Map<String, Object> data) {
	}
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}