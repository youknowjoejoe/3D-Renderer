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
	
	public FrameBuffer(T[][] array){
		fragments = array;
		width = array.length;
		height = array[0].length;
		screenSpace = Matrix.getScale(new Vec4(width,-height,1,1)).times(Matrix.getTranslation(new Vec4(1,1,0,0)));
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
		
		for(int x = minX; x < maxX; x++){
			for(int y = minY; y < maxY; y++){
				if(t.contains(new Vec4(x,y,0,0))){
					addFragment(x,y,fragmentShader.apply(t.getA1())); //add interpolation
				}
			}
		}
	}
	
	public void addFragment(int x, int y, T value){
		fragments[x][y] = adder.add(fragments[x][y], value);
	}
}
