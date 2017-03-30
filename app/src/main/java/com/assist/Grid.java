package com.assist;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 一个矩形的网格。可以画成有纹理或者没有纹理 A 2D rectangular mesh. Can be drawn textured or untextured.
 */
class Grid {

	private FloatBuffer mVertexBuffer;	//顶点缓存
	private FloatBuffer mTexCoordBuffer;	//纹理坐标缓存
	private CharBuffer mIndexBuffer;	//

	private int mW;	//宽度
	private int mH;	//高度
	private int mIndexCount;	//索引的数目

	public Grid(int w, int h) {
		if (w < 0 || w >= 65536) {
			throw new IllegalArgumentException("w"); // 传递参数不合法异常
		}
		if (h < 0 || h >= 65536) {
			throw new IllegalArgumentException("h");
		}
		if (w * h >= 65536) {
			throw new IllegalArgumentException("w * h >= 65536");
		}

		mW = w;
		mH = h;
		int size = w * h;
		final int FLOAT_SIZE = 4;
		final int CHAR_SIZE = 2;
		mVertexBuffer = ByteBuffer.allocateDirect(FLOAT_SIZE * size * 3)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTexCoordBuffer = ByteBuffer.allocateDirect(FLOAT_SIZE * size * 2)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		int quadW = mW - 1;
		int quadH = mH - 1;
		int quadCount = quadW * quadH;
		int indexCount = quadCount * 6;
		mIndexCount = indexCount;
		mIndexBuffer = ByteBuffer.allocateDirect(CHAR_SIZE * indexCount)
				.order(ByteOrder.nativeOrder()).asCharBuffer();

		/*
		 * Initialize triangle list mesh.
		 * 
		 * [0]-----[ 1] ... | / | | / | | / | [w]-----[w+1] ... | |
		 */

		{
			int i = 0;
			for (int y = 0; y < quadH; y++) {
				for (int x = 0; x < quadW; x++) {
					char a = (char) (y * mW + x);
					char b = (char) (y * mW + x + 1);
					char c = (char) ((y + 1) * mW + x);
					char d = (char) ((y + 1) * mW + x + 1);

					mIndexBuffer.put(i++, a);
					mIndexBuffer.put(i++, b);
					mIndexBuffer.put(i++, c);

					mIndexBuffer.put(i++, b);
					mIndexBuffer.put(i++, c);
					mIndexBuffer.put(i++, d);
				}
			}
		}

	}

	void set(int i, int j, float x, float y, float z, float u, float v) {
		if (i < 0 || i >= mW) {
			throw new IllegalArgumentException("i");
		}
		if (j < 0 || j >= mH) {
			throw new IllegalArgumentException("j");
		}

		int index = mW * j + i;

		int posIndex = index * 3;
		mVertexBuffer.put(posIndex, x);
		mVertexBuffer.put(posIndex + 1, y);
		mVertexBuffer.put(posIndex + 2, z);

		int texIndex = index * 2;
		mTexCoordBuffer.put(texIndex, u);
		mTexCoordBuffer.put(texIndex + 1, v);
	}

	public void draw(GL10 gl, boolean useTexture) {
		//启用顶点坐标数据
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//设置顶点的位置数据
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

		if (useTexture) {	//要用纹理
			//启用贴图坐标数组数据
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			//设置贴图的坐标数据
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
			//启用2d纹理贴图
			gl.glEnable(GL10.GL_TEXTURE_2D);
		} else {	//不用纹理
			//停用贴图坐标数组数据
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			//停用2d纹理贴图
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		//按照mIndexBuffer制定的面绘制三角形
		gl.glDrawElements(GL10.GL_TRIANGLES, mIndexCount,
				GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		//停用贴图坐标数组数据
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
