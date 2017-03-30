package com.rubik;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

/**
 * 魔方的大立方体的类
 * 作为魔方的小立方体的基类
 */
public class GLShape {	//模型类
	public static int COUNT = 0;
	public String id = "";	//模型的id
	public M4 mTransform;
	public M4 mAnimateTransform;
	
	/**
	 * 立方体拥有的平面List
	 */
	protected ArrayList<GLFace> mFaceList = new ArrayList<GLFace>();
	/**
	 * 立方体拥有的顶点List
	 */
	protected ArrayList<GLVertex> mVertexList = new ArrayList<GLVertex>();
	protected GLWorld mWorld;
	
    /**
     * 存储模型顶点的buffer
     */
    private FloatBuffer mVertexBuffer;
    
	public GLShape(GLWorld world) {
		mWorld = world;
		
		synchronized (GLShape.class) {	//同步代码块
			//缺中心一个点
			if (COUNT==13) {
				COUNT++;
			}
			id = String.valueOf(COUNT);
			COUNT++;
		}
	}
	/**
	 * 添加平面到集合
	 * */
	public void addFace(GLFace face) {
		mFaceList.add(face);
	}
	/**
	 * 设置制定位置的平面的颜色
	 * @param face 平面在集合中的index
	 * @param color 颜色
	 */
	public void setFaceColor(int face, GLColor color) {
		mFaceList.get(face).setColor(color);
	}
	/**
	 * 设置顶点的坐标值到FloatBuffer，也就是要构造顶点的buffer，可以参考CrazyAndroid讲义上的顶点数据的结构
	 * */
	public void putVertex(FloatBuffer buffer) {
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			buffer.put(vertex.x);
			buffer.put(vertex.y);
			buffer.put(vertex.z);
		}		
	}
	/**
	 * 添加顶点
	 * 先在顶点的集合中查找有无该坐标的顶点，有就返回该顶点，没有就创建一个新顶点
	 */
	public GLVertex addVertex(float x, float y, float z) {
		// look for an existing GLVertex first
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			if (vertex.x == x && vertex.y == y && vertex.z == z) {	//如果在顶点集合中找到该顶点返回该顶点
				return vertex;
			}
		}
		//在顶点集合中找不到该顶点就新建一个顶点并添加到顶点集合中
		GLVertex vertex = new GLVertex(x, y, z,mVertexList.size());
		mVertexList.add(vertex);
		return vertex;
	}

	/**
	 * 方法实现逻辑待考证
	 * @param transform
	 */
	public void animateTransform(M4 transform) {
		mAnimateTransform = transform;
		
		if (mTransform != null) {
			transform = mTransform.multiply(transform);
		}
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			//mWorld.transformVertex(vertex, transform);
			vertex.update(mVertexBuffer, transform);
		}
	}
	
	/**
	 * 开始动画，方法没有方法体
	 */
	public void startAnimation() {
	}

	/**
	 * 停止动画
	 */
	public void endAnimation() {
		//旋转角度累计
		if (mTransform == null) {
			mTransform = new M4(mAnimateTransform);
		} else {
			mTransform = mTransform.multiply(mAnimateTransform);
		}
	}
	
	/**
	 * 立方体的各个平面设置图片
	 * 这里的图片指的是表面上面的字
	 * @param bitmap 设置的图片
	 */
	public void loadBitmap(Bitmap bitmap) { 
		for (GLFace face : mFaceList) {
			face.loadBitmap(bitmap);
		}
	}

	/**
	 * 绘制立方体，也即是绘制立方体的各个平面
	 * @param gl
	 */
	public void draw(GL10 gl) {
		mVertexBuffer.position(0);
		//设置顶点的位置数据
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);		
		
		for (GLFace face : mFaceList) {
			face.draw(gl);
		}
	}

	/**
	 * 生成顶点坐标和顶点颜色的buffer
	 * 由于加载顶点数据、顶点颜色数据都需要buffer对象
	 */
	public void generate() {
		
		//allocateDirect:分配新的直接字节缓冲区, 新缓冲区的位置将为零，其界限将为其容量
		//顶点颜色
	    ByteBuffer bb = ByteBuffer.allocateDirect(mVertexList.size()*4*4);

		//顶点坐标
	    bb = ByteBuffer.allocateDirect(mVertexList.size()*3*4);
	    //数组排序
	    bb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = bb.asFloatBuffer();

		Iterator<GLVertex> iter2 = mVertexList.iterator();
		
		while (iter2.hasNext()) {
			GLVertex vertex = iter2.next();
			vertex.put(mVertexBuffer);
		}
	}
}

