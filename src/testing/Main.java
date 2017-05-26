package testing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.DepthTestRasterizeAction;
import core.FrameBuffer;
import core.Shader;
import core.VertexAttribute;
import core.Mesh;
import core.FrameBufferAdder;
import core.Interpolator;
import math.Vec4;
import math.Matrix;
import util.Display;
import util.ImageExporter;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
    
    public static void main(String[] args) throws IOException{
        
        FrameBufferAdder<Color> adder = new FrameBufferAdder<Color>(){
            @Override
            public Color add(Color c1, Color c2){
                return c2;
            }
        };
        Color[][] fragments = new Color[1000][1000];
        for(int x = 0; x < fragments.length; x++){
            for(int y = 0; y < fragments[0].length; y++){
                fragments[x][y] = Color.black;
            }
        }
        FrameBuffer<Color> colorBuffer = new FrameBuffer<Color>(fragments,adder);
        DepthTestRasterizeAction depthTest = new DepthTestRasterizeAction(colorBuffer);
        colorBuffer.setRasterizeAction(depthTest);
        
        FrameBuffer<Float> depth = depthTest.getDepthBuffer();
        
        /*for(int x = 0; x < depth.getWidth(); x++){
            for(int y = 0; y < depth.getHeight(); y++){
                depth.addFragment(x, y, 2f);
            }
        }*/
        
        List<VertexAttribute<Color>> vertices = new ArrayList<VertexAttribute<Color>>();
        vertices.add(new VertexAttribute<Color>(new Vec4(-0.5f,-0.5f,3,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0.5f,-0.5f,1,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0f,0.5f,1,1), Color.RED));
        
        vertices.add(new VertexAttribute<Color>(new Vec4(-0.5f,-0.5f,1,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0.5f,-0.5f,3,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0f,0.5f,1,1), Color.RED));
        
        Shader<VertexAttribute<Color>,VertexAttribute<Color>> vs = new Shader<VertexAttribute<Color>,VertexAttribute<Color>>(){
            Matrix p = new Matrix(
                new float[][]{
                    {1,0,0,0},
                    {0,1,0,0},
                    {0,0,1,0},
                    {0,0,1,0}
                }
            );
            
            @Override
            public VertexAttribute<Color> apply(VertexAttribute<Color> attributes) {
                return new VertexAttribute<Color>(p.times(attributes.getPos()),attributes.getAttributes());
            }
            
        };
        
        Shader<VertexAttribute<Color>,Color> fs = new Shader<VertexAttribute<Color>,Color>(){
            @Override
            public Color apply(VertexAttribute<Color> attributes) {
                return new Color(1.0f-Math.min(1,attributes.getPos().z()/5.0f),(1f+attributes.getPos().x())/2.0f,(1f+attributes.getPos().y())/2.0f);
            }
        };
        
        Mesh<Color> m = new Mesh<Color>(vertices,new int[]{0,1,2,3,4,5},vs,Interpolator.colorI);
        
        colorBuffer.rasterize(m,fs);
        
        BufferedImage img = ImageExporter.convert(colorBuffer);
        Display d = new Display(1000,1000);
        while(true){
        	d.draw(img);
        }
    }
}
