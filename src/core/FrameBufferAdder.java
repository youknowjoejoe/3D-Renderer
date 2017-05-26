package core;

public interface FrameBufferAdder<T> {
	public static <A> FrameBufferAdder<A> getSubstitute(){
		return new FrameBufferAdder<A>(){
			@Override
			public A add(A attrib1, A attrib2) {
				return attrib2;
			}
		};
	}
	
	public T add(T attrib1, T attrib2);
}
