package core;

public interface RasterizeAction {
	public<T,A> void fragment(int x, int y, VertexAttribute<A> va, FrameBuffer<T> fb, Shader<VertexAttribute<A>,T> fragmentShader);
}
