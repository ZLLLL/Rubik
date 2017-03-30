package com.assist;

/**
 * 向量
 */
public class Vector3f {

	public static final Vector3f UP = new Vector3f(0, 1, 0);
	public static final Vector3f ZERO = new Vector3f(0, 0, 0);

	public static Vector3f TEMP = new Vector3f();
	public static Vector3f TEMP1 = new Vector3f();

	public float x, y, z;

	public Vector3f() {

	}
	
	@Override 
	public String toString() {
		return x + "," + y + "," + z;
	};

	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}

	/**
	 * 设置顶点对象的x，y，z的值
	 * @param x
	 * @param y
	 * @param z
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * 把顶点V的x，y，z赋值给调用的顶点对象
	 * @param v
	 */
	public void set(Vector3f v) {
		set(v.x, v.y, v.z);
	}
	/**
	 * 按照参数来修改点的x,y,z的值
	 * */
	public final void interpolate(Vector3f t1, Vector3f t2, float alpha) {
		this.x = (1 - alpha) * t1.x + alpha * t2.x;
		this.y = (1 - alpha) * t1.y + alpha * t2.y;
		this.z = (1 - alpha) * t1.z + alpha * t2.z;
	}
	/**
	 * 调用该方法的点的x,y,z的值分别加上参数的点的x,y,z的值
	 * */
	public final void add(Vector3f t1) {
		this.x += t1.x;
		this.y += t1.y;
		this.z += t1.z;
	}
	/**
	 * 调用该方法的对象的x,y,z的值分别加上参数的值
	 * */
	public final void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	/**
	 * 两个顶点做减法
	 * @param t1
	 */
	public final void sub(Vector3f t1) {
		sub(this, t1);
	}
	/**
	 * 两个点做减法
	 * */
	public final void sub(Vector3f t1, Vector3f t2) {
		this.x = t1.x - t2.x;
		this.y = t1.y - t2.y;
		this.z = t1.z - t2.z;
	}
	/**
	 * 按照参数来设置x,y,z的比例
	 * */
	public final void scale(float s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
	}

	/**
	 * 计算v1和顶点对象的乘法
	 * @param v1
	 * @return 积
	 */
	public final float dot(Vector3f v1) {
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z);
	}
	/**
	 * 方法的逻辑待考证
	 * */
	public final void cross(Vector3f v1, Vector3f v2) {
		float x, y;

		x = v1.y * v2.z - v1.z * v2.y;
		y = v2.x * v1.z - v2.z * v1.x;
		this.z = v1.x * v2.y - v1.y * v2.x;
		this.x = x;
		this.y = y;
	}
	/**
	 * 将x,y,z都置为0
	 * */
	public void zero() {
		x = y = z = 0.0f;
	}
	/**
	 * 标准化x,y,z
	 * 实现的处理是： x = x / sqrt(x*x + y*y + z*z);
	 * */
	public void normalize() {
		float len = (float) Math.sqrt(x * x + y * y + z * z);
		//下面的操作相当于是  x = x / len
		len = 1.0f / len;
		x *= len;
		y *= len;
		z *= len;
	}
	/**
	 * 计算两个矢量点之间的距离
	 * */
	public static float distance(Vector3f v0, Vector3f v1) {
		float dx = v0.x - v1.x;
		float dy = v0.y - v1.y;
		float dz = v0.z - v1.z;

		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

}

