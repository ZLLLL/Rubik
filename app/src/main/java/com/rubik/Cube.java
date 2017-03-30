package com.rubik;

import com.assist.Vector3f;

/**
 * 魔方的小立方体的类
 */
public class Cube extends GLShape {	//立方体 类
	
	//魔方的立方体的各个平面的索引
	public static final int kBottom = 0;
    public static final int kFront = 1;
    public static final int kLeft = 2;
    public static final int kRight = 3;
    public static final int kBack = 4;
    public static final int kTop = 5;
	
	private float sphereRadius;	//立方体的边长
	
	/**
	 * 构造函数，参数分别为 world， 还有6个float值，分别表示x,y,z轴上面的位置（前3个是x,y,z轴
	 * 最小值,后3个位对应的最大值）通过这6个值来构造立方体的顶点、平面
	 * */
	public Cube(GLWorld world, float left, float bottom, float back, float right, float top, float front) {
		super(world);
		//小立方体的边长
		sphereRadius = 0.6f;
		/* 
		 *   2 3
		 *  6 7
		 *   0 1
		 *  4 5
		 * */
		//定义立方体的8个顶点
       	GLVertex leftBottomBack = addVertex(left, bottom, back);	//0
        GLVertex rightBottomBack = addVertex(right, bottom, back);	//1
       	
        GLVertex leftTopBack = addVertex(left, top, back);	//2
        GLVertex rightTopBack = addVertex(right, top, back);	//3
       	
        GLVertex leftBottomFront = addVertex(left, bottom, front);	//4
        GLVertex rightBottomFront = addVertex(right, bottom, front);	//5
       	
        GLVertex leftTopFront = addVertex(left, top, front);	//6
        GLVertex rightTopFront = addVertex(right, top, front);	//7
        
        //总共添加6个面
        // 按照顺时针的方向添加顶点来构造立方体的各个平面
        // vertices are added in a clockwise orientation (when viewed from the outside)
        GLFace bottomFace = new GLFace(leftBottomBack, leftBottomFront, rightBottomFront, rightBottomBack);
//        bottomFace.setColor(GLColor.ORANGE);
        bottomFace.setColor(GLColor.BLACK);
        //绘制正方形，需要4个点，每3个相邻的点构成一个三角形，由两个三角形拼接形成一个正方形
        bottomFace.setIndices(new short[] { 4, 0, 5, 1, });
        //在本项目中由于顶点全部写到mVertextList中，所以设置坐标时要设置8个点，在贴图时不属于该面的点设为0,0即可
        bottomFace.setTextureCoordinates(new float[] { 0,1 , 1,1, 0,0, 0,0, 0,0, 1,0, 0,0, 0,0});
        addFace(bottomFace);

        //1 2
        //0 3
        GLFace frontFace = new GLFace(leftBottomFront, leftTopFront, rightTopFront, rightBottomFront);
//        frontFace.setColor(GLColor.RED);
        frontFace.setColor(GLColor.BLACK);
        frontFace.setIndices(new short[] { 4, 5, 6, 7, });
        frontFace.setTextureCoordinates(new float[] { 0,0 , 0,0, 0,0, 0,0, 0,1 ,1,1, 0,0, 1,0});
        addFace(frontFace);

        GLFace leftFace = new GLFace(leftBottomBack, leftTopBack, leftTopFront, leftBottomFront);
//        leftFace.setColor(GLColor.YELLOW);
        leftFace.setColor(GLColor.BLACK);
        leftFace.setIndices(new short[] { 4, 6, 0, 2, });
        leftFace.setTextureCoordinates(new float[]{ 0,1 , 0,0, 0,0, 0,0, 1,1, 0,0, 1,0, 0,0});       
        addFace(leftFace);

        GLFace rightFace = new GLFace(rightBottomBack, rightBottomFront, rightTopFront, rightTopBack);
//        rightFace.setColor(GLColor.WHITE);
        rightFace.setColor(GLColor.BLACK);
        rightFace.setIndices(new short[] { 1, 3, 5, 7, });
        rightFace.setTextureCoordinates(new float[] { 0,0 , 1,1, 0,0, 1,0, 0,0, 0,1, 0,0, 0,0});         
        addFace(rightFace);

        GLFace backFace = new GLFace(leftBottomBack, rightBottomBack, rightTopBack, leftTopBack);
//        backFace.setColor(GLColor.BLUE);
        backFace.setColor(GLColor.BLACK);
        backFace.setIndices(new short[] { 0, 2, 1, 3, });
        backFace.setTextureCoordinates(new float[] { 1,1 , 0,1, 1,0, 0,0, 0,0, 0,0, 0,0, 0,0});  
        addFace(backFace);
        
        GLFace topFace = new GLFace(leftTopBack, rightTopBack, rightTopFront, leftTopFront);
//        topFace.setColor(GLColor.GREEN);
        topFace.setColor(GLColor.BLACK);
        topFace.setIndices(new short[] { 6, 7, 2, 3, });
        topFace.setTextureCoordinates(new float[] { 0,0 , 0,0, 1,1, 0,1, 0,0, 0,0, 1,0, 0,0});  
        addFace(topFace);
	}
    
    /**
     * 获得最小的和最大的x,y,z
	 * @return {minX,maxX,minY,maxY,minZ,maxZ}
	 */
	public float[] getMinMax(){
		float[] arr = new float[6];
		
		boolean init = true;
		
		for(GLVertex vertex : mVertexList){
			if (init) {
				arr[0] = vertex.tempX;
				arr[1] = vertex.tempX;
				arr[2] = vertex.tempY;
				arr[3] = vertex.tempY;
				arr[4] = vertex.tempZ;
				arr[5] = vertex.tempZ;
				
				init = false;
			}
			else{
				arr[0] = arr[0]>vertex.tempX ? vertex.tempX: arr[0];
				arr[1] = arr[1]<vertex.tempX ? vertex.tempX: arr[1];
				arr[2] = arr[2]>vertex.tempY ? vertex.tempY: arr[2];
				arr[3] = arr[3]<vertex.tempY ? vertex.tempY: arr[3];
				arr[4] = arr[4]>vertex.tempZ ? vertex.tempZ: arr[4];
				arr[5] = arr[5]<vertex.tempZ ? vertex.tempZ: arr[5];
			}
		}
		
		return arr;
	}
	
	/**
	 * 获得小立方体的中心
	 * @return 返回的是小立方体中心点的坐标
	 * */
	public Vector3f getSphereCenter() {
		// TODO Auto-generated method stub
		float[] arr = getMinMax();
		
		return new Vector3f((arr[0]+arr[1])/2,(arr[2]+arr[3])/2,(arr[4]+arr[5])/2);
	}

	/**
	 * 获得小立方体的边长
	 * */
	public float getSphereRadius() {
		// TODO Auto-generated method stub
		return sphereRadius;
	}	
}
