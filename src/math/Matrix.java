package math;


public class Matrix {
    private float[/*which row*/][/*which col*/] values;
    
    public Matrix(float[][] values){
        this.values = values;
    }
    
    public Matrix times(Matrix m){
        if(!multCompatible(m)) return null;
        
        float[][] temp = new float[getNumRows()][m.getNumCols()];
        
        for(int row = 0; row < temp.length; row++){
            for(int col = 0; col < temp[0].length; col++){
                temp[row][col] = dot(this,m,row,col);
            }
        }
        
        return new Matrix(temp);
    }
    
    private static float dot(Matrix a, Matrix b, int row, int col){
        float sum = 0;
        for(int rep = 0; rep < a.getNumCols(); rep++){
            sum = sum+(a.get(row,rep)*(b.get(rep,col)));
        }
        return sum;
    }
    
    public Vec4 times(Vec4 v){
        Matrix m = new Matrix(
                new float[][]{
                    {v.x()},
                    {v.y()},
                    {v.z()},
                    {v.w()}
                }
        );
        float x = dot(this,m,0,0);
        float y = dot(this,m,1,0);
        float z = dot(this,m,2,0);
        float w = dot(this,m,3,0);
        return new Vec4(x,y,z,w);
    }
    
    public Matrix plus(Matrix m){
        if(!addCompatible(m)) return null;
        
        float[][] temp = new float[values.length][values[0].length];
        
        for(int row = 0; row < temp.length; row++){
            for(int col = 0; col < temp[0].length; col++){
                temp[row][col] = values[row][col]+(m.get(row, col));
            }
        }
        
        return new Matrix(temp);
    }
    
    public Matrix subtract(Matrix m){
        if(!addCompatible(m)) return null;
        
        float[][] temp = new float[values.length][values[0].length];
        
        for(int row = 0; row < temp.length; row++){
            for(int col = 0; col < temp[0].length; col++){
                temp[row][col] = values[row][col]-(m.get(row, col));
            }
        }
        
        return new Matrix(temp);
    }
    
    public Matrix scaledBy(float f){
        float[][] temp = new float[values.length][values[0].length];
        
        for(int row = 0; row < temp.length; row++){
            for(int col = 0; col < temp[0].length; col++){
                temp[row][col] = values[row][col]*(f);
            }
        }
        
        return new Matrix(temp);
    }
    
    public Matrix power(int p){
    	Matrix m = this;
    	for(int rep = 0; rep < p-1; rep++){
    		m = this.times(m);
    	}
    	return m;
    }
    
    public float getDeterminant(){
        if(getNumRows()!=getNumCols()) return 0;
        if(getNumRows() == 1){
            return (values[0][0]);
        }
        
        float sum = 0;
        for(int rep = 0; rep < getNumRows(); rep++){
            sum = sum+(getCofactor(rep,0)*(values[rep][0]));
        }
        
        return sum;
    }
    
    public float getCofactor(int row, int col){
        return getAssociatedSign(row,col)*(getMinorMatrixDeterminant(row,col));
    }
    
    public float getAssociatedSign(int row, int col){
        return (((row+col)%2)*(-2)+1);
    }
    
    public float getMinorMatrixDeterminant(int row, int col){
        int nC = getNumCols()-1;
        int nR = getNumRows()-1;
        
        float[][] temp = new float[nR][];
        
        int i = 0;
        for(int r = 0; r < getNumRows(); r++){
            if(r == row) continue;
            float[] tempRow = new float[nC];
            int i2 = 0;
            for(int c = 0; c < getNumCols(); c++){
                if(c == col) continue;
                tempRow[i2] = values[r][c];
                i2++;
            }
            temp[i] = tempRow;
            i++;
        }
        return new Matrix(temp).getDeterminant();
    }
    
    public Matrix getTransposed(){
        if(getNumCols() != getNumRows()) return null;
        float[][] temp = new float[values[0].length][values.length];
        
        for(int row = 0; row < temp.length; row++){
            for(int col = 0; col < temp[0].length; col++){
                temp[row][col] = values[col][row];
            }
        }
        
        return new Matrix(temp);
    }
    
    public Matrix getInverseMatrix(){
        if(getNumCols() != getNumRows()) return null;
        
        float det = getDeterminant();
        if(det==(0)) return null;
        if(getNumCols() == 1){
            return new Matrix(
                    new float[][]{
                        {1.0f/values[0][0]}
                        }
                    ).scaledBy(1.0f/det);
        }
        float[][] temp = new float[values.length][values[0].length];
        
        for(int row = 0; row < temp.length; row++){
            for(int col = 0; col < temp[0].length; col++){
                temp[row][col] = getCofactor(row,col);
            }
        }
        
        return new Matrix(temp).getTransposed().scaledBy(1.0f/det);
    }
    
    public boolean multCompatible(Matrix m){
        return getNumCols() == m.getNumRows();
    }
    
    public boolean addCompatible(   Matrix m){
        return getNumRows() == m.getNumRows() && getNumCols() == m.getNumCols();
    }
    
    public int getNumCols(){
        return values[0].length;
    }
    
    public int getNumRows(){
        return values.length;
    }
    
    public float get(int row, int col){
        return values[row][col];
    }
    
    @Override
    public String toString(){
        String temp = "";
        temp+="{";
        for(int row = 0; row < values.length; row++){
            temp+="{";
            for(int col = 0; col < values[0].length; col++){
                temp+=values[row][col] + " ";
            }
            temp+="}";
        }
        temp+="}";
        
        return temp;
    }
    
    
    public static Matrix getTranslation(Vec4 v){
        return new Matrix(
                    new float[][]{
                        {1,0,0,v.x()},
                        {0,1,0,v.y()},
                        {0,0,1,v.z()},
                        {0,0,0,1}
                    }
                );
    }
    
    public static Matrix getScale(Vec4 v){
        return new Matrix(
                    new float[][]{
                        {v.x(),0,0,0},
                        {0,v.y(),0,0},
                        {0,0,v.z(),0},
                        {0,0,0,v.w()}
                    }
                );
    }
    
    public static final Matrix identity = new Matrix(
        new float[][]{
            {1,0,0,0},
            {0,1,0,0},
            {0,0,1,0},
            {0,0,0,1}
        }
    );
    
    public static Matrix getRotationX(float theta){
        float cos = (float)(Math.cos(theta));
        float sin = (float)(Math.sin(theta));
        return new Matrix(
                new float[][]{
                    {1,0,0,0},
                    {0,cos,-sin,0},
                    {0,sin,cos,0},
                    {0,0,0,1}
                }
            );
    }
    
    public static Matrix getRotationY(float theta){
        float cos = (float)(Math.cos(theta));
        float sin = (float)(Math.sin(theta));
        return new Matrix(
                new float[][]{
                    {cos,0,sin,0},
                    {0,1,0,0},
                    {sin,0,cos,0},
                    {0,0,0,1}
                }
            );
    }
    
    public static Matrix getRotationZ(float theta){
        float cos = (float)(Math.cos(theta));
        float sin = (float)(Math.sin(theta));
        return new Matrix(
                new float[][]{
                    {cos,-sin,0,0},
                    {sin,cos,0,0},
                    {0,0,0,0},
                    {0,0,0,1}
                }
            );
    }
}

