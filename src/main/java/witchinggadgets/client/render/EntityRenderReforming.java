package witchinggadgets.client.render;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import witchinggadgets.common.items.EntityItemReforming;

public class EntityRenderReforming extends RenderItem
{
	public void doRender(Entity entity, double x, double y, double z, float par8, float par9)
    {
		if( ((EntityItemReforming)entity).renderDelay>0 )
		{
			this.shadowOpaque = 0.4f;
			return;
		}
		this.shadowOpaque = 1;
		super.doRender(entity, x, y, z, par8, par9);
    }
}
