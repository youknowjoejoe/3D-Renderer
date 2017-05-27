package core;

import java.util.List;

import math.Matrix;
import math.Vec4;

public class FrameBuffer<T> {
	
	private static final RasterizeAction defaultAction = new RasterizeAction(){
		@Override
		public <T, A> void fragment(int x, int y, VertexAttribute<A> va, FrameBuffer<T> fb,
				Shader<VertexAttribute<A>, T> fragmentShader){
			fb.addFragment(x,y,fragmentShader.apply(va));
		}
		
		public void reset(){
		    //hi
		}
	};
	
	private T[][] fragments;
	private T clearValue;
	
	private int width;
	private int height;
	private Matrix screenSpace;
	private Matrix inverseScreenSpace;
	
	private FrameBufferAdder<T> adder;
	private RasterizeAction ra;
	
	public FrameBuffer(T[][] array,FrameBufferAdder<T> adder, T clearValue){
		this(array,adder,defaultAction,clearValue);
	}
	
	public FrameBuffer(T[][] array,FrameBufferAdder<T> adder, RasterizeAction ra, T clearValue){
		fragments = array;
		this.adder = adder;
		width = array.length;
		height = array[0].length;
		screenSpace = Matrix.getScale(new Vec4(width/2.0f,-height/2.0f,1,1)).times(Matrix.getTranslation(new Vec4(1,-1,0,0)));
		inverseScreenSpace = screenSpace.getInverseMatrix();
		this.ra = ra;
		this.clearValue = clearValue;
		this.clear();
		this.ra.reset();
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
	
	public <A> void rasterize(ResultingTriangle<A> input, Shader<VertexAttribute<A>,T> fragmentShader){
		ResultingTriangle<A> t = input.transform(screenSpace);
		int minX = (int)Math.min(Math.min(t.getA1().getPos().x(), t.getA2().getPos().x()), t.getA3().getPos().x());
		int minY = (int)Math.min(Math.min(t.getA1().getPos().y(), t.getA2().getPos().y()), t.getA3().getPos().y());
		int maxX = (int)Math.max(Math.max(t.getA1().getPos().x(), t.getA2().getPos().x()), t.getA3().getPos().x());
		int maxY = (int)Math.max(Math.max(t.getA1().getPos().y(), t.getA2().getPos().y()), t.getA3().getPos().y());
		
		for(int x = Math.max(minX,0); x < Math.min(width-1,maxX); x++){
			for(int y = Math.max(minY,0); y < Math.min(height-1,maxY); y++){
				if(t.contains(new Vec4(x,y,0,0))){
					Vec4 fragPos = (new Vec4(x,y,0,1));
				    float depth = t.getDepth(fragPos);
				    fragPos = inverseScreenSpace.times(new Vec4(fragPos.x(),fragPos.y(),depth,1));
				    VertexAttribute<A> va = new VertexAttribute<A>(fragPos, input.getA1().getAttributes());
				    ra.fragment(x, y, va, this, fragmentShader);
				}
			}
		}
	}
	
	public void addFragment(int x, int y, T value){
		fragments[x][y] = adder.add(fragments[x][y], value);
	}
	
	public void clear(){
	    for(int x = 0; x < width; x++){
	        for(int y = 0; y < height; y++){
	            fragments[x][y] = clearValue;
	        }
	    }
	    ra.reset();
	}
	
	public void setRasterizeAction(RasterizeAction ra){
		this.ra = ra;
	}
	
	public Matrix getScreenSpaceTransformation(){
		return screenSpace;
	}
}
