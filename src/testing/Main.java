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
import util.ImageExporter;
import util.Display;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

//things to check if working:
//matrix v matrix, matrix scale v vector, cross product, magnitude

public class Main {
    
    public static void main(String[] args) throws IOException{
        int w = 800;
        int h = 600;
        
        FrameBuffer<Color> colorBuffer = new FrameBuffer<Color>(new Color[w][h],FrameBufferAdder.getSubstitute(),Color.black);
        DepthTestRasterizeAction depthTest = new DepthTestRasterizeAction(colorBuffer);
        colorBuffer.setRasterizeAction(depthTest);
        
        FrameBuffer<Float> depth = depthTest.getDepthBuffer();
        
        /*for(int x = 0; x < depth.getWidth(); x++){
            for(int y = 0; y < depth.getHeight(); y++){
                depth.addFragment(x, y, 2f);
            }
        }*/
        
        List<VertexAttribute<Color>> vertices = new ArrayList<VertexAttribute<Color>>();
        vertices.add(new VertexAttribute<Color>(new Vec4(-0.5f,-0.5f,4,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0.5f,-0.5f,4,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0f,0.5f,4,1), Color.RED));
        
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
            
            Matrix r = Matrix.getRotationX(0.001f);
            Matrix m = Matrix.getRotationX(-0.5f);
            
            @Override
            public VertexAttribute<Color> apply(VertexAttribute<Color> attributes) {
                m = r.times(m);
                return new VertexAttribute<Color>(p.times(m.times(attributes.getPos())),attributes.getAttributes());
            }
            
        };
        
        Shader<VertexAttribute<Color>,Color> fs = new Shader<VertexAttribute<Color>,Color>(){
            @Override
            public Color apply(VertexAttribute<Color> attributes) {
                return new Color(Math.min((attributes.getPos().z()/5.0f),1),0,0);
            }
        };
        
        Mesh<Color> m = new Mesh<Color>(vertices,new int[]{0,1,2},vs,Interpolator.colorI);
        
        Display d = new Display(w,h,":D");
        while(true){
            colorBuffer.rasterize(m,fs);
            BufferedImage img = ImageExporter.convert(colorBuffer);
            d.draw(img);
            colorBuffer.clear();
        }
    }
}
