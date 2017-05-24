package testing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.FrameBuffer;
import core.Shader;
import core.VertexAttribute;
import core.Mesh;
import core.FrameBufferAdder;
import core.Interpolator;
import math.Vec4;
import math.Matrix;
import util.ImageExporter;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

//things to check if working:
//matrix v matrix, matrix scale v vector, cross product, magnitude

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
        FrameBuffer<Color> frame = new FrameBuffer<Color>(fragments,adder);
        
        List<VertexAttribute<Color>> vertices = new ArrayList<VertexAttribute<Color>>();
        vertices.add(new VertexAttribute<Color>(new Vec4(-0.5f,-0.5f,2,1), Color.RED));
        vertices.add(new VertexAttribute<Color>(new Vec4(0.5f,-0.5f,1,1), Color.RED));
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
                return new Color(Math.min(1,attributes.getPos().z()/2.0f),0,0);
            }
        };
        
        Mesh<Color> m = new Mesh<Color>(vertices,new int[]{0,1,2},vs,Interpolator.colorI);
        
        frame.rasterize(m,fs);
        
        BufferedImage img = ImageExporter.convert(frame);
        ImageIO.write(img,"png",new File("dog.png"));
    }
}
