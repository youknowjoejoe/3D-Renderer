package core;

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
}
