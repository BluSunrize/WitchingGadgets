package witchinggadgets.client;

import java.awt.image.BufferedImage;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.data.AnimationMetadataSection;

import com.google.common.collect.LinkedListMultimap;

public class RecolourableIcon extends TextureAtlasSprite
{
	Integer[] shadedColouration;
	protected RecolourableIcon(String key, List<Integer> list)
	{
		super(key);
		shadedColouration = list.toArray(new Integer[0]);
	}

//	@Override
//	public void loadSprite(BufferedImage[] images, AnimationMetadataSection animation, boolean antisopFiltering)
//	{
//		System.out.println();
//		System.out.println("LOADING RECOLOURABLE IMAGE!!!!");
//		System.out.println();
//		
//		for(int k=0;k<images.length;k++)
//		{
//			BufferedImage image = images[k];
//			List<Integer> imageShade = ClientUtilities.getImageColourRangeUnsorted(image);
//			List<Integer> imageShadeSorted = ClientUtilities.getImageColourRangeUnsorted(image);
//			assert(shadedColouration.length == imageShadeSorted.size());
//			
//			LinkedListMultimap<Integer, Integer> colourToPos = LinkedListMultimap.create();
//			for(int l=0;l<imageShade.size();l++)
//				colourToPos.put(imageShade.get(l), l);
//			
//			for(int i=0;i<imageShadeSorted.size();i++)
//			{
//				for(int pos : colourToPos.get(imageShadeSorted.get(i)))
//				{
//					int x = pos%16;
//					int y = pos/16;
//					//System.out.println("Position "+pos+", X="+x+", Y="+y);
//					image.setRGB(x,y, shadedColouration[i]);
//				}
//			}
//			
//		}
//		
//		super.loadSprite(images, animation, antisopFiltering);
//		System.out.println();
//		System.out.println("DONE LOADING RECOLOURABLE IMAGE!!!!");
//		System.out.println();
//	}
}