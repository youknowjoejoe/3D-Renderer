package core;

import java.util.List;

import math.Matrix;
import math.Vec4;

public class FrameBuffer<T> {
	
	private T[][] fragments;
	
	private int width;
	private int height;
	private Matrix screenSpace;
	
	private FrameBufferAdder<T> adder;
	
	public FrameBuffer(T[][] array,FrameBufferAdder<T> adder){
		fragments = array;
		this.adder = adder;
		width = array.length;
		height = array[0].length;
		screenSpace = Matrix.getScale(new Vec4(width/2.0f,-height/2.0f,1,1)).times(Matrix.getTranslation(new Vec4(1,-1,0,0)));
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public T get(int x, int y){
		return fragments[x][y];
	}
	
	public <A> void rasterize(Mesh<A> m, Shader<VertexAttribute<A>,T> fragmentShader){
		List<ResultingTriangle<A>> list = m.get();
		for(ResultingTriangle<A> t: list){
			rasterize(t,fragmentShader);
		}
	}
	
	public <A> void rasterize(ResultingTriangle<A> t, Shader<VertexAttribute<A>,T> fragmentShader){
		t.transform(screenSpace);
		int minX = (int)Math.min(Math.min(t.getA1().getPos().x(), t.getA2().getPos().x()), t.getA3().getPos().x());
		int minY = (int)Math.min(Math.min(t.getA1().getPos().y(), t.getA2().getPos().y()), t.getA3().getPos().y());
		int maxX = (int)Math.max(Math.max(t.getA1().getPos().x(), t.getA2().getPos().x()), t.getA3().getPos().x());
		int maxY = (int)Math.max(Math.max(t.getA1().getPos().y(), t.getA2().getPos().y()), t.getA3().getPos().y());
		
		for(int x = Math.max(minX,0); x < Math.min(width-1,maxX); x++){
			for(int y = Math.max(minY,0); y < Math.min(height-1,maxY); y++){
				if(t.contains(new Vec4(x,y,0,0))){
				    float depth = t.getDepth((new Vec4(x,y,0,0)));
				    System.out.println(depth);
				    VertexAttribute<A> va = new VertexAttribute<A>( new Vec4(0,0,depth,1), t.getA1().getAttributes());
					addFragment(x,y,fragmentShader.apply(va));
				}
			}
		}
	}
	
	public void addFragment(int x, int y, T value){
		fragments[x][y] = adder.add(fragments[x][y], value);
	}
}
