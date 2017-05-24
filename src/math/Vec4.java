package math;

public class Vec4 {
	private float[] values;
	
	public Vec4(float x, float y, float z, float w){
		values = new float[]{x,y,z,w};
	}
	
	public float get(int index){
		return values[index];
	}
	
	public float[] get(){
		return values;
	}
	
	public float x(){
		return values[0];
	}
	
	public float y(){
		return values[1];
	}
	
	public float z(){
		return values[2];
	}
	
	public float w(){
		return values[3];
	}
	
	public Vec4 plus(Vec4 v){
		return new Vec4(x()+v.x(),y()+v.y(),z()+v.z(),w()+v.w());
	}
	
	public Vec4 minus(Vec4 v){
		return plus(v.scaledBy(-1f));
	}
	
	public Vec4 scaledBy(float f){
		return new Vec4(x()*f,y()*f,z()*f,w()*f);
	}
	
	public float dot(Vec4 v){
		return (x()*v.x()) + (y()*v.y()) + (z()*v.z()) + (w()*v.w());
	}
	
	public Vec4 cross(Vec4 v){
		return new Vec4(
					y()*v.z()-z()*v.y(),
					z()*v.x()-x()*v.z(),
					x()*v.y()-y()*v.x(),
					1f
				);
	}
	
	public float cross2D(Vec4 v){
		return x()*v.y()-y()*v.x();
	}
	
	public float magnitudeSquared(){
		return dot(this);
	}
	
	public float magnitude(){
		return (float) Math.sqrt((float)magnitudeSquared());
	}
	
	public Vec4 homogenous(){
		return new Vec4(x()/w(),y()/w(),1.0f/w(),1.0f);
	}
	
	@Override
	public String toString(){
		return "<"+x()+","+y()+","+z()+","+w()+">";
	}
}
