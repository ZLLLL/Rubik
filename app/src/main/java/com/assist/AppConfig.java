package com.assist;

/**
 * 应用设置的类，定义了一些需要使用到的变量
 */
public class AppConfig {
	public static boolean Turning = false;
	/**
	 * 投影矩阵
	 */
	public static Matrix4f gMatProject = new Matrix4f();
	/**
	 * 视图矩阵
	 */
	public static Matrix4f gMatView = new Matrix4f();
	/**
	 * 模型矩阵
	 */
	public static Matrix4f gMatModel = new Matrix4f();
	/**
	 * 视口参数
	 */
	public static int[] gpViewport = new int[4];
	/**
	 * 当前系统的投影矩阵，列序填充
	 */
	public static float[] gpMatrixProjectArray = new float[16];
	/**
	 * 当前系统的视图矩阵，列序填充
	 */
	public static float[] gpMatrixViewArray = new float[16];
	/**
	 * 是否需要进行拾取检测（当触摸事件发生时）
	 */
	public static boolean gbNeedPick = false;
	/**
	 * 是否有三角形被选中
	 */
	public static boolean gbTrianglePicked = false;
	/**
	 * 在屏幕中触摸的位置
	 */
	public static float gScreenX, gScreenY;
	/**
	 * 设置触摸的位置
	 * @param x 触摸的x坐标
	 * @param y 触摸的y坐标
	 */
	public static void setTouchPosition(float x, float y) {
		gScreenX = x;
		gScreenY = y;
	}
}
