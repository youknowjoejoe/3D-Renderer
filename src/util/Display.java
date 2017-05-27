package util;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Display extends Canvas{
    
    private JFrame frame;
    
    public Display(int width, int height, String name){
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        this.setPreferredSize(new Dimension(width,height));
        frame.pack();
        frame.setVisible(true);
        
        this.createBufferStrategy(2);
    }
    
    public void draw(BufferedImage img){
        BufferStrategy bs = this.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        
        g.drawImage(img,0,0,null);
        
        g.dispose();
        bs.show();
    }
}