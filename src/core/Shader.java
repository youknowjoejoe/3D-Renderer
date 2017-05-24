package core;

public interface Shader<T,V> {
	public V apply(T attributes);
}
