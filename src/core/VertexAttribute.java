package core;

import math.Vec4;

public class VertexAttribute<T> {
	
	private Vec4 pos;
	private T attributes;
	
	public VertexAttribute(Vec4 pos, T attributes){
		this.pos = pos;
		this.attributes = attributes;
	}
	
	public Vec4 getPos(){
		return pos;
	}
	
	public T getAttributes(){
		return attributes;
	}
	
	public VertexAttribute<T> homogenous(){
		return new VertexAttribute<T>(pos.homogenous(),attributes);
	}
}
