package com.rubik;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.assist.IBufferFactory;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * 魔方的面
 *
 */
public class GLFace {
    private ShortBuffer indicesBuffer;	//索引的缓存
    private FloatBuffer textureBuffer;	//纹理的缓存
	private ArrayList<GLVertex> mVertexList = new ArrayList<GLVertex>(); //存储顶点的集合
	private GLColor mColor;		//平面的颜色
	
	// The bitmap we want to load as a texture. 
	private int mTextureId = -1; 	//纹理的代号
	private Bitmap mBitmap; 	//装载成纹理的图片
	private boolean bInitTexture = false;	//纹理有没有被使用bitmap装载的标志位，true表示装载了
	/**
	 * 由4个顶点构造一个平面，添加4个顶点到存储顶点的集合中
	 * */
	public GLFace(GLVertex v1, GLVertex v2, GLVertex v3, GLVertex v4) {
		addVertex(v1);
		addVertex(v2);
		addVertex(v3);
		addVertex(v4);
	}
	/**
	 * 设置索引
	 * */
	public void setIndices(short[] indices){
		indicesBuffer = IBufferFactory.newShortBuffer(indices);
	}
	/**
	 * 设置纹理
	 * */
	public void setTextureCoordinates(float[] textureCoords){
		textureBuffer = IBufferFactory.newFloatBuffer(textureCoords);
	}
	/**
	 * 获取索引
	 * */
	public ShortBuffer getIndicesBuffer(){
		return indicesBuffer;
	}
	/**
	 * 获取纹理
	 * */
	public FloatBuffer getTextureBuffer(){
		return textureBuffer;
	}
	/**
	 * 添加顶点到List中
	 * */
	public void addVertex(GLVertex v) {
		mVertexList.add(v);
	}
	/**
	 * 设置颜色
	 * 应该在所有的顶点添加完成之后，再调用
	 * */
	public void setColor(GLColor c) {
		mColor = c;
	}
	/**
	 * 获取颜色
	 * */
	public GLColor getColor(){
		return mColor;
	}
	/**
	 * 获取索引的个数
	 * */
	public int getIndexCount() {
		//根据putIndices只画两个三角型
		return (mVertexList.size() - 2) * 3;
	}
	/**
	 * 获取指定位置的顶点
	 * */	
	public GLVertex getVertex(int index){
		return mVertexList.get(index);
	}
			
	/** 
	 * Set the bitmap to load into a texture. 
	 * 设置位图加载纹理
	 * @param bitmap 
	 */
	public void loadBitmap(Bitmap bitmap) { 
		 this.mBitmap = bitmap; 
		 bInitTexture = true; 
	} 
	  
	/** 
	 * Loads the texture. 
	 * 装载纹理
	 * @param gl 
	 */
	private void loadGLTexture(GL10 gl) { 
		 // Generate one texture pointer... 
		 int[] textures = new int[1]; 
		 //制定生成N个纹理，第一个参数制定生成一个纹理
		 //textures数组将负责存储所有纹理的代号
		 gl.glGenTextures(1, textures, 0);
		 //获取textures纹理数组中的第一个纹理
		 mTextureId = textures[0]; 
		  
		 //通知OpenGL将mTextureId纹理绑定到GL_TEXTURE_2D目标中
		 gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId); 
		 //设置纹理被缩小（距离视点很远时被缩小）时候的滤波方式
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); 
		 //设置纹理被放大（距离视点很近时被放大）时候的滤波方式
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR); 
		 //设置纹理在纵向上是边界截取
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE); 
		 //设置纹理在横向上是平铺纹理
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT); 
		  
		 //纹理贴图和材质混合的方式
	     gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,GL10.GL_MODULATE);
	        
		 //加载位图生成纹理
		 GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0); 
	}
	
	/**
	 * 绘制平面上的数字
	 * @param gl
	 */
	public void draw(GL10 gl){
        //增加贴图
		if (bInitTexture) { //纹理已经使用bitmap装载好了
			 loadGLTexture(gl); 
			 bInitTexture = false;
		}
		
		GLColor color = getColor();	//获取颜色
		gl.glColor4f(color.red, color.green, color.blue, color.alpha); //gl设置颜色（纯色）
		
		if (!color.equals(GLColor.BLACK)) {  //如果表面的color不是黑色则贴图，黑色面位不可见区域
			 gl.glEnable(GL10.GL_TEXTURE_2D); 	//启用二维纹理
			 // 启用贴图坐标数组数据
			 gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
			// 通知OpenGL将mTextureId纹理绑定到GL_TEXTURE_2D目标中
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId); 
			  			 
			FloatBuffer textureBuffer = getTextureBuffer();
			textureBuffer.position(0);
			//设置贴图的坐标数据
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		}
		 
		ShortBuffer indicesBuffer = getIndicesBuffer();
		indicesBuffer.position(0);
		//按indicesBuffer制定的面绘制三角形
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        
        if (!color.equals(GLColor.BLACK)) { 
		   	 gl.glDisable(GL10.GL_TEXTURE_2D); 
		   	 gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
        }
	}
}

