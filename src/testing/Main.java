package testing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.FrameBuffer;
import core.Shader;
import core.VertexAttribute;
import math.Vec4;

//things to check if working:
//matrix v matrix, matrix scale v vector, cross product, magnitude

public class Main {
	
	public static void main(String[] args){
		
		FrameBuffer<Color> frame = new FrameBuffer<Color>(new Color[100][100]);
		
		List<VertexAttribute<Color>> vertices = new ArrayList<VertexAttribute<Color>>();
		vertices.add(new VertexAttribute<Color>(new Vec4(-0.5f,-0.5f,1,1), Color.RED));
		
		Shader<VertexAttribute<Color>,VertexAttribute<Color>> vs = new Shader<VertexAttribute<Color>,VertexAttribute<Color>>(){
			@Override
			public VertexAttribute<Color> apply(VertexAttribute<Color> attributes) {
				return attributes;
			}
			
		};
		
		Shader<VertexAttribute<Color>,Color> fs = new Shader<VertexAttribute<Color>,Color>(){
			@Override
			public Color apply(VertexAttribute<Color> attributes) {
				return attributes.getAttributes();
			}
		};
		
		
	}
}
