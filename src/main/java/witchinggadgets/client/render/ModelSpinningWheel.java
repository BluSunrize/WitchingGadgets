package witchinggadgets.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import witchinggadgets.client.ClientUtilities;

public class ModelSpinningWheel extends ModelBase
{
	private IModelCustom model;

	public ModelSpinningWheel()
	{
		model = ClientUtilities.bindModel("witchinggadgets","models/SpinningWheel.obj");
	}

	public void render(int tick)
	{
		model.renderPart("Frame_Cube");

		double angle = tick * (360.0/64.0);
		GL11.glRotated(angle, 1, 0, 0);
		model.renderPart("Wheel_Circle");
		GL11.glRotated(angle, -1, 0, 0);

		GL11.glTranslatef(0, 0.18F, 0.38F);
		GL11.glRotated(18, 1, 0, 0);
		GL11.glRotated(angle, 0, 0, 1);
		model.renderPart("String_Cylinder");
		GL11.glRotated(angle, 0, 0, -1);
		GL11.glRotated(18, -1, 0, 0);
		GL11.glTranslatef(0, -0.18F, -0.38F);

		GL11.glTranslatef(0, -0.105F, 0.725F);
		GL11.glRotated(angle, 0, 1, 0);
		model.renderPart("Spindle_Cone");
		GL11.glRotated(angle, 0, -1, 0);
		GL11.glTranslatef(0, 0.105F, -0.725F);
	}
}
