package core;

import java.awt.Color;

import math.Vec4;

public class DepthTestRasterizeAction implements RasterizeAction {
    
    private int width;
    private int height;
    
    private FrameBuffer<Float> depthBuffer;
    
    
    public DepthTestRasterizeAction(FrameBuffer<?> fb){
        this.width = fb.getWidth();
        this.height = fb.getHeight();
        
        Float[][] values = new Float[width][height];
        depthBuffer = new FrameBuffer<Float>(values, FrameBufferAdder.getSubstitute(),Float.POSITIVE_INFINITY);
    }
    
    public void reset(){
        depthBuffer.clear();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, A> void fragment(int x, int y, VertexAttribute<A> va, FrameBuffer<T> fb,
           Shader<VertexAttribute<A>, T> fragmentShader) {
        if(va.getPos().z() >= 1f){
        	//System.out.println(va.getPos().z());
            if(Math.abs(va.getPos().z()-depthBuffer.get(x, y)) <= Vec4.epsilon){
                ((FrameBuffer<Color>)fb).addFragment(x, y, Color.black);
            } else if(va.getPos().z() < depthBuffer.get(x, y)){
                depthBuffer.addFragment(x, y, va.getPos().z());
                fb.addFragment(x, y, fragmentShader.apply(va));
            }
        }
    }
    
    public FrameBuffer<Float> getDepthBuffer(){
        return depthBuffer;
    }
}
