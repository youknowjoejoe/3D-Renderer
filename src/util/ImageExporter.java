package util;

import java.awt.Color;
import java.awt.image.BufferedImage;

import core.FrameBuffer;

public class ImageExporter {
	
	public static BufferedImage convert(FrameBuffer<Color> b){
		BufferedImage img = new BufferedImage(b.getWidth(),b.getHeight(),BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < b.getWidth(); x++){
			for(int y = 0; y < b.getHeight(); y++){
				img.setRGB(x, y, b.get(x, y).getRGB());
			}
		}
		
		return img;
	}
}
