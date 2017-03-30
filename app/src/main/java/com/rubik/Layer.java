package com.rubik;

import java.util.HashMap;

public class Layer {	//魔方的层 类
	
	GLShape[] mShapes = new GLShape[9];	//魔方的每层有9个小立方体
	M4 mTransform = new M4();
	//float mAngle;
	//按照哪个轴旋转的常量
	// which axis do we rotate around?
	// 0 for X, 1 for Y, 2 for Z
	final int mAxis;
	static public final int kAxisX = 0;
	static public final int kAxisY = 1;
	static public final int kAxisZ = 2;
	
	public final int index;
	
	/**
	 * 魔方的层的构造函数
	 * @param axis 轴线
	 * @param index 索引
	 */
	public Layer(int axis,int index) {
		// start with identity matrix for transformation
		//构造函数中可以对final字段赋值
		mAxis = axis;
		this.index = index;
		mTransform.setIdentity();
	}
	/**
	 * 魔方的层开始动画，也就是说该层的所有小立方体要开始动画
	 * 开始动画
	 * */
	public void startAnimation() {
		for (int i = 0; i < mShapes.length; i++) {
			GLShape shape = mShapes[i];
			if (shape != null) {
				shape.startAnimation();
			}	
		}
	}
	/**
	 * 结束动画
	 * */
	public void endAnimation() {
		for (int i = 0; i < mShapes.length; i++) {
			GLShape shape = mShapes[i];
			if (shape != null) {
				shape.endAnimation();
			}	
		}
	}
	/**
	 * 设置魔方某层的旋转的角度
	 * 这里的旋转是模型矩阵，也就是说旋转是按照魔方的模型坐标来进行旋转的
	 * */
	public void setAngle(float angle) {
		//设置角度值的范围在0~2pi之间
		float twopi = (float)Math.PI *2f;
		while (angle >= twopi) angle -= twopi;
		while (angle < 0f) angle += twopi;
		
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		
		//sin(A+B) = sinA*cosB + cosA*sinB
		//cos(A+B) =-sinA*sinB + cosA*cosB
		
		float[][] m = mTransform.m;	//这是一个4*4的矩阵
		switch (mAxis) {
			case kAxisX:	//按照X轴旋转
				m[1][1] = cos;		//1  0    0
				m[1][2] = sin;		//0 cos  sin
				m[2][1] = -sin;		//0 -sin cos
				m[2][2] = cos;
				m[0][0] = 1f;
				m[0][1] = m[0][2] = m[1][0] = m[2][0] = 0f;
				break;
			case kAxisY:	//按照Y轴旋转
				m[0][0] = cos;		//cos  0 sin
				m[0][2] = sin;		//0    1  0
				m[2][0] = -sin;		//-sin 0 cos
				m[2][2] = cos;
				m[1][1] = 1f;
				m[0][1] = m[1][0] = m[1][2] = m[2][1] = 0f;
				break;
			case kAxisZ:	//按照Z轴旋转
				m[0][0] = cos;		//cos  sin 0
				m[0][1] = sin;		//-sin cos 0
				m[1][0] = -sin;		//0	    0  1
				m[1][1] = cos;
				m[2][2] = 1f;
				m[2][0] = m[2][1] = m[0][2] = m[1][2] = 0f;
				break;
		}
		
		for (int i = 0; i < mShapes.length; i++) {	//以此对该层中的全部小立方体进行处理
			GLShape shape = mShapes[i];
			if (shape != null) {
				shape.animateTransform(mTransform);
			}
		}
	}
	
	/**
	 * 返回该层上全部小立方体的x y z坐标的最小值和最大值
	 * @return {minX,maxX,minY,maxY,minZ,maxZ}
	 */
	public float[] getMinMax(){
		float[] arr = new float[6];
		
		boolean init = true;
		
		for (GLShape shape : mShapes) {
			if (shape == null) {
				continue;
			}
			for(GLVertex vertex : shape.mVertexList){
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
		}
		return arr;
	}
	/**
	 * 判断mShapes中有没有id为cube.id的模型，如果有就把该cude.id在mShapes中的位置存储到retMap中
	 * 并返回true，否则在retMap中存储0,并返回false
	 * @param cube 
	 * @param retMap 
	 */
	public boolean hashCube(Cube cube, HashMap<String, Object> retMap) {
		// TODO Auto-generated method stub
		boolean rs = false;
		
		retMap.put("index",0);
		
		for (int i = 0; i < mShapes.length; i++) {
			if (mShapes[i]!=null && mShapes[i].id.equals(cube.id)) {
				rs = true;
				retMap.put("index",i);
				break;
			}
		}
		return rs;
	}
	/**
	 * 打印出该层上全部小立方体的id
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (GLShape shape : mShapes) {
			if (shape!=null) {
				sb.append(shape.id + ",");	
			}
		}
		return sb.toString();
	}
}

