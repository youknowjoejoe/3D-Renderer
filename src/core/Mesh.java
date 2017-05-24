package core;

import java.util.ArrayList;
import java.util.List;

public class Mesh<T> {
	
	private List<VertexAttribute<T>> vertices;
	private int[] indices;
	private Shader<VertexAttribute<T>,VertexAttribute<T>> vertexShader;
	private Interpolator<T> attributeInterpolator;
	
	public Mesh(List<VertexAttribute<T>> vertices, int[] indices, Shader<VertexAttribute<T>,VertexAttribute<T>> vertexShader, Interpolator<T> attributeInterpolator){
		this.vertices = vertices;
		this.indices = indices;
		this.vertexShader = vertexShader;
		this.attributeInterpolator = attributeInterpolator;
	}
	
	public List<ResultingTriangle<T>> get(){
		List<VertexAttribute<T>> rvs = new ArrayList<VertexAttribute<T>>(vertices.size());
		for(int rep = 0; rep < vertices.size(); rep++){
			rvs.add(vertexShader.apply(vertices.get(rep)).homogenous());
		}
		List<ResultingTriangle<T>> list = new ArrayList<ResultingTriangle<T>>(); 
		for(int rep = 0; rep+2 < indices.length; rep+=3){
			list.add(new ResultingTriangle<T>(
					rvs.get(rep),
					rvs.get(rep+1),
					rvs.get(rep+2)
					));
		}
		return list;
	}
}
