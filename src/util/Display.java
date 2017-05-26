package util;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Display extends Canvas {
	
	private JFrame frame;
	
	public Display(int width, int height){
		setPreferredSize(new Dimension(width, height));
		frame = new JFrame();
		frame.add(this);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		this.createBufferStrategy(1);
	}
	
	public void draw(BufferedImage b){
		BufferStrategy bs = this.getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(b, 0, 0, this.getWidth(), this.getHeight(), null);
		
		g.dispose();
		
		bs.show();
	}
}
