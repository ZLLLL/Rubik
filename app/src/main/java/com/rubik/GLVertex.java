package com.rubik;

import java.nio.FloatBuffer;

public class GLVertex {	//魔方的顶点 类

    public float x,tempX;
    public float y,tempY;
    public float z,tempZ;
    
    final short index; // index in vertex table
    GLColor color;

    /**
     * GLVertex的无参构造函数，设置顶点的各个坐标值为0，索引为-1
     */
    GLVertex() {
        this.tempX = this.x = 0;
        this.tempY = this.y = 0;
        this.tempZ = this.z = 0;
        this.index = -1;
    }

    /**
     * GLVertex的有参构造函数，设置顶点的坐标和索引值
     * @param x x坐标
     * @param y y坐标
     * @param z z坐标
     * @param index 索引
     */
    GLVertex(float x, float y, float z, int index) {
    	this.tempX = this.x = x;
    	this.tempY = this.y = y;
    	this.tempZ = this.z = z;
        this.index = (short)index;
    }
    /**
     * 判断两个顶点是不是相同的，也就是说顶点的各个坐标值相同
     * */
    @Override
    public boolean equals(Object other) {
        if (other instanceof GLVertex) {
            GLVertex v = (GLVertex)other;
            return (x == v.x && y == v.y && z == v.z);
        }
        return false;
    }

    static public int toFixed(float x) {
    	//如果采用GL10.GL_FIXED则需要修正，默认为GL_FLOAT则不需要
        return (int)(x * 65536.0f);
    }
    /**
     * 设置顶点的x,y,z的值到vertexBuffer中
     * @param vertexBuffer
     * */
    public void put(FloatBuffer vertexBuffer) {
        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.put(z);
    }

    /**
     * 当transform为空时，vertexBuffer中添加该顶点对象的x y z坐标值
     * 不为空时进行运算，具体运算逻辑待考证
     * @param vertexBuffer 存储结果的Buffer
     * @param transform
     */
    public void update(FloatBuffer vertexBuffer, M4 transform) {
        // skip to location of vertex in mVertex buffer
        vertexBuffer.position(index * 3);
        if (transform == null) {
            this.tempX = x;
            this.tempY = y;
            this.tempZ = z;
            
            vertexBuffer.put(x);
            vertexBuffer.put(y);
            vertexBuffer.put(z);
        } 
        else {
            GLVertex temp = new GLVertex();
            transform.multiply(this, temp);
            
            this.tempX = temp.x;
            this.tempY = temp.y;
            this.tempZ = temp.z;
            
            vertexBuffer.put(temp.x);
            vertexBuffer.put(temp.y);
            vertexBuffer.put(temp.z);
        }
    }
}
