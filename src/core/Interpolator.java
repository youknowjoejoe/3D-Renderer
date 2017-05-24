package core;

import java.awt.Color;

import math.Vec4;

public interface Interpolator<T> {
	public T interpolate(T p1, T p2, T p3, float w1, float w2, float w3);
	
	public static Interpolator<Float> floatI = new Interpolator<Float>(){
		@Override
		public Float interpolate(Float p1, Float p2, Float p3, float w1, float w2, float w3) {
			return p1*w1+p2*w2+p3*w3;
		}
	};
	
	public static Interpolator<Integer> integerI = new Interpolator<Integer>(){
		@Override
		public Integer interpolate(Integer p1, Integer p2, Integer p3, float w1, float w2, float w3) {
			return (int)Math.round(p1*w1+p2*w2+p3*w3);
		}
	};
	
	public static Interpolator<Boolean> booleanI = new Interpolator<Boolean>(){
		@Override
		public Boolean interpolate(Boolean p1, Boolean p2, Boolean p3, float w1, float w2, float w3) {
			if(!p1) w1*=-1;
			if(!p2) w2*=-1;
			if(!p3) w3*=-1;
			float sum = w1+w2+w3;
			return sum>0;
		}
	};
	
	public static Interpolator<Vec4> vec4I = new Interpolator<Vec4>(){
		@Override
		public Vec4 interpolate(Vec4 p1, Vec4 p2, Vec4 p3, float w1, float w2, float w3) {
			return p1.scaledBy(w1).plus(p2.scaledBy(w2)).plus(p3.scaledBy(w3));
		}
	};
	
	public static Interpolator<Color> colorI = new Interpolator<Color>(){
		@Override
		public Color interpolate(Color p1, Color p2, Color p3, float w1, float w2, float w3) {
			return new Color(
					(int)((p1.getRed()*w1)+(p2.getRed()*w2)+(p3.getRed()*w3)),
					(int)((p1.getGreen()*w1)+(p2.getGreen()*w2)+(p3.getGreen()*w3)),
					(int)((p1.getBlue()*w1)+(p2.getBlue()*w2)+(p3.getBlue()*w3)),
					(int)((p1.getAlpha()*w1)+(p2.getAlpha()*w2)+(p3.getAlpha()*w3))
					);
		}
	};
}
