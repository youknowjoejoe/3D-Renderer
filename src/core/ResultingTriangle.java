package core;

import math.Matrix;
import math.Vec4;

public class ResultingTriangle<T> {
	
	private VertexAttribute<T> a1, a2, a3;
	
	public ResultingTriangle(VertexAttribute<T> a1, VertexAttribute<T> a2, VertexAttribute<T> a3){
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
	}

	public VertexAttribute<T> getA1() {
		return a1;
	}

	public VertexAttribute<T> getA2() {
		return a2;
	}

	public VertexAttribute<T> getA3() {
		return a3;
	}
	
	public void transform(Matrix m){
		a1 = new VertexAttribute<T>(m.times(a1.getPos()),a1.getAttributes());
		a2 = new VertexAttribute<T>(m.times(a2.getPos()),a2.getAttributes());
		a3 = new VertexAttribute<T>(m.times(a3.getPos()),a3.getAttributes());
	}
	
	public boolean contains(Vec4 v){
		Vec4 a12d = new Vec4(a1.getPos().x(),a1.getPos().y(),0,0);
		Vec4 a22d = new Vec4(a2.getPos().x(),a2.getPos().y(),0,0);
		Vec4 a32d = new Vec4(a3.getPos().x(),a3.getPos().y(),0,0);
		float cross1 = a22d.minus(a12d).cross2D(v.minus(a12d));
		float cross2 = a32d.minus(a22d).cross2D(v.minus(a22d));
		float cross3 = a12d.minus(a32d).cross2D(v.minus(a32d));
		return (cross1>0 && cross2 > 0 && cross3 > 0) || (cross1<0 && cross2 < 0 && cross3 < 0);
	}
	
	public float getDepth(Vec4 v){
	    Vec4 va1 = new Vec4(a1.getPos().x()-v.x(),a1.getPos().y()-v.y(),0,0);
	    Vec4 va2 = new Vec4(a2.getPos().x()-v.x(),a2.getPos().y()-v.y(),0,0);
	    Vec4 va3 = new Vec4(a3.getPos().x()-v.x(),a3.getPos().y()-v.y(),0,0);
	    
	    float w1 = Math.abs(va2.cross2D(va3)/2.0f);
	    float w2 = Math.abs(va3.cross2D(va1)/2.0f);
	    float w3 = Math.abs(va1.cross2D(va2)/2.0f);
	    
	    float total = w1+w2+w3;
	    w1/=total;
	    w2/=total;
	    w3/=total;
	    
	    return 1.0f/Interpolator.floatI.interpolate(a1.getPos().z(),a2.getPos().z(),a3.getPos().z(),w1,w2,w3);
	}
}
