package com.rubik;

/** 
 * 
 * A 4x4 float matrix
 * 4*4的矩阵
 */
public class M4 {
	public float[][] m = new float[4][4];
	
	public M4() {
	}
	
	public M4(M4 other) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m[i][j] = other.m[i][j];
			}
		}		
	}
	/**
	 * 乘法运算
	 * @param src
	 * @param dest 存储结果
	 */
	public void multiply(GLVertex src, GLVertex dest) {
		dest.x = src.x * m[0][0] + src.y * m[1][0] + src.z * m[2][0] + m[3][0];
		dest.y = src.x * m[0][1] + src.y * m[1][1] + src.z * m[2][1] + m[3][1];
		dest.z = src.x * m[0][2] + src.y * m[1][2] + src.z * m[2][2] + m[3][2];
	}
	/**
	 * 矩阵的乘法
	 * @param other 矩阵乘法中的第二个矩阵
	 * @return
	 */
	public M4 multiply(M4 other) {
		M4 result = new M4();
		float[][] m1 = m;
		float[][] m2 = other.m;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.m[i][j] = m1[i][0]*m2[0][j] + m1[i][1]*m2[1][j] + m1[i][2]*m2[2][j] + m1[i][3]*m2[3][j];
			}
		}
		return result;
	}
	/**
	 * 设置一致性
	 * 当矩阵的行列角标一样的时候，矩阵该点的值为1，否则矩阵该点的值为0
	 * */
	public void setIdentity() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m[i][j] = (i == j ? 1f : 0f);
			}
		}
	}
	/**
	 * 打印出矩阵
	 * */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[ ");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				builder.append(m[i][j]);
				builder.append(" ");
			}
			if (i < 2)
				builder.append("\n  ");
		}
		builder.append(" ]");
		return builder.toString();
	}
}

