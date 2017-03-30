package com.rubik;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.assist.AppConfig;
import com.assist.IBufferFactory;
import com.assist.Matrix4f;
import com.assist.PickFactory;
import com.assist.Ray;
import com.assist.Vector3f;
import com.assist.Vector4f;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

public class GLWorld {
	/**
	 * 被拾取的三角形
	 */
	private FloatBuffer mBufPickedTriangle = IBufferFactory.newFloatBuffer(3 * 3);
	/**
	 * 三角形三个顶点的坐标
	 */
	private Vector3f[] mpTriangle = { new Vector3f(), new Vector3f(),new Vector3f()};
	/**
	 * CopyOnWriteArrayList是ArrayList的一个线程安全的变体
	 */
	private CopyOnWriteArrayList<Cube> pickedList = new CopyOnWriteArrayList<Cube>();
	/**
	 * 绘制魔方的小立方体的图像，只是小立方体上面的数字的绘制
	 * */
	public void createCubeImage(){
		for(Cube cube : mShapeList){
	    	int imgSize = 64;
	    	int fontSize = 20;
	    	//Config.ARGB_8888表示由4个8位组成的AGRB位图
	    	Bitmap bitmap = Bitmap.createBitmap(imgSize, imgSize, Bitmap.Config.ARGB_8888); 
	    	
	    	Canvas canvas = new Canvas(bitmap);
	    	//整张画布绘制成白色
	    	canvas.drawColor(Color.WHITE);
	    	Paint p = new Paint();
	
	    	//设置字体、字体大小和字体颜色
	    	String familyName = "Times New Roman";
	    	Typeface font = Typeface.create(familyName, Typeface.NORMAL);
	    	p.setColor(Color.BLACK);	//设置颜色
	    	p.setTypeface(font);	//设置字形
	    	p.setTextSize(fontSize);
	    	
	    	//在Bitmap上绘制文字
	    	String text = cube.id;
	    	float textWidth = p.measureText(text);	//取得字符串显示的宽度值
	    	canvas.drawText(cube.id,(imgSize - textWidth)/2,imgSize - fontSize, p); 
	    	
	    	cube.loadBitmap(bitmap);
		}
	}
	
	/**
	 * 每一次触摸滑动的时候，手指和魔方接触的小立方体都会被添加到pickedList
	 * 添加cube到pickedList中，如果pickedList中已经有了就不用添加，如果没有就添加
	 * @param cube
	 */
	private void addPickedList(Cube cube){
		boolean has = false;
		
		for (Cube _cube : pickedList) {
			if (_cube.id.equals(cube.id)) {
				has = true;
				break;
			}
		}
		if (!has) {
			pickedList.add(cube);
		}
	}
	
	/**
	 * 清空pcikedList
	 */
	public void clearPickedCubes(){
		Log.d("GLWorld", "清空选取列表");
		pickedList.clear();
		
		for (int i = 0; i < mpTriangle.length; i++) {
			mpTriangle[i] = new Vector3f();
		}
	}
	/**
	 * 添加Cube对象到mShapeList
	 * @param shape
	 */
	public void addShape(Cube shape) {
		mShapeList.add(shape);
	}
	
	/**
	 * 生成mShapeList中每个小立方体的buffer数据
	 * 顶点坐标和顶点颜色的buffer
	 */
	public void generate() {		
		Iterator<Cube> iter3 = mShapeList.iterator();
		while (iter3.hasNext()) {
			GLShape shape = iter3.next();
			shape.generate();
		}
	}
		
    /**
     * 绘制各个小立方体
     * @param gl
     */
    public void draw(GL10 gl)
    {   
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);	//启用顶点坐标数据

    	/* 剔除操作
    	 * 1.glCullFace()参数包括GL_FRONT和GL_BACK。表示禁用多边形正面或者背面上的光照、阴影
    	 * 和颜色计算及操作，消除不必要的渲染计算。例如某对象无论如何位置变化，我们都只能看到
    	 * 构成其组成的多边形的某一面时，可使用该函数。*/
	    gl.glEnable(GL10.GL_CULL_FACE);	//开启剔除操作效果
	    //控制多边形的正面是如何决定的
	    //GL_CCW表示窗口坐标上投影多边形的顶点顺序为逆时针方向的表面为正面。
		gl.glFrontFace(GL10.GL_CCW);
	    gl.glCullFace(GL10.GL_BACK);
	    //启用深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST); //不会画被挡住的图形部分
	    
        for(int i = 0;i < mShapeList.size(); i++) {		//绘制小立方体的表面数字
        	GLShape shape = mShapeList.get(i);
			shape.draw(gl);
		}

        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glDisable(GL10.GL_CULL_FACE);
//        gl.glDisableClientState(GL10.GL_CULL_FACE);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    
    /**
	 * 渲染选中的三角形
	 */
	public void drawPickedTriangle(GL10 gl) {
		if (!AppConfig.gbTrianglePicked) {	//如果gbTrianglePicked为false 方法就执行结束
			return;
		}
		/**
		 * 模型坐标和世界坐标解释如下：
		 * 世界坐标是以真实的坐标系来作为参照的
		 * 模型坐标是以相对该物体本身的坐标
		 * 例如有两个点A B A相对与原点的坐标值就是世界坐标 采用B模型的坐标来描述A 那就是A-B的坐标
		 * */
		// 由于返回的拾取三角形数据是处于模型坐标系中
		// 因此需要经过模型变换，将它们变换到世界坐标系中进行渲染
		// 设置模型变换矩阵
		gl.glMultMatrixf(AppConfig.gMatModel.asFloatBuffer());
		
		// 设置三角形颜色，alpha为0.7
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.7f);
		// 开启Blend混合模式
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// 禁用无关属性，仅仅使用纯色填充
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
	    gl.glEnable(GL10.GL_CULL_FACE);
	    //注意这里与向交检测的点顺序方向有关！否则画到背面无法看见!
		gl.glFrontFace(GL10.GL_CW);
	    gl.glCullFace(GL10.GL_BACK);
		
		// 开始绑定渲染顶点数据
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		mBufPickedTriangle.position();
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBufPickedTriangle);
		// 提交渲染
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
		// 重置相关属性
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
    static public float toFloat(int x) {
    	return x/65536.0f;
    }

	private ArrayList<Cube>	mShapeList = new ArrayList<Cube>();	//小立方体的集合
	    
	private Vector4f location = new Vector4f();
	public Vector3f worldCenter = new Vector3f(0, 0, 0); //中心
	public final float worldRadius = 1.7f;	//半径
	
	/**
	 * 射线与模型的精确碰撞检测
	 * @param ray - 转换到模型空间中的射线
	 * @param trianglePosOut - 返回的拾取后的三角形顶点位置
	 * @return 如果相交，返回true
	 */
	public boolean intersectDetect() {
		if (!AppConfig.gbNeedPick || AppConfig.Turning) {
			return false;
		}
		
		AppConfig.gbNeedPick = false;
		// 更新最新的拾取射线
		PickFactory.update(AppConfig.gScreenX, AppConfig.gScreenY);
		// 获得最新的拾取射线
		Ray ray = PickFactory.getPickRay();
		
		boolean bFound = false;
		// 存储着射线原点与三角形相交点的距离
		// 我们最后仅仅保留距离最近的那一个
		float closeDis = 0.0f;

		Vector3f v0, v1, v2;
		
		Ray transformedRay = new Ray();
		Cube mpCube = null;

		// 如果射线与绑定球发生相交，那么就需要进行精确的三角面级别的相交检测
		// 由于我们的模型渲染数据，均是在模型局部坐标系中
		// 而拾取射线是在世界坐标系中
		// 因此需要把射线转换到模型坐标系中
		// 这里首先计算模型矩阵的逆矩阵
		Matrix4f matInvertModel = new Matrix4f();
		matInvertModel.set(AppConfig.gMatModel);
		matInvertModel.invert();
		
		// 把射线变换到模型坐标系中，把结果存储到transformedRay中
		ray.transform(matInvertModel, transformedRay);
		
		// 首先把模型的绑定球通过模型矩阵，由模型局部空间变换到世界空间
//		Vector3f transformedSphereCenter = new Vector3f();
//	    AppConfig.gMatModel.transform(new Vector3f(0, 0, 0),transformedSphereCenter);
	    
//	    long begin = System.currentTimeMillis();
	    
		if (transformedRay.intersectSphere(worldCenter,worldRadius)) { //射线和包围球相交
			//26个方块，正中间不显示
			int cubeCount = mShapeList.size();
//			Vector3f _transformedSphereCenter = new Vector3f();
			
			for (int c = 0; c < cubeCount; c++) {
				Cube cube = (Cube)mShapeList.get(c);
//			    AppConfig.gMatModel.transform(cube.getSphereCenter(),_transformedSphereCenter);
				//射线和立方体相交
			    if (transformedRay.intersectSphere(cube.getSphereCenter(),cube.getSphereRadius())) {
					// 立方体6个面
					for (int i = 0; i < cube.mFaceList.size(); i++) {
						GLFace face = cube.mFaceList.get(i);
						
						//黑色面为不可见，不需要判断
						if (face.getColor().equals(GLColor.BLACK)) {
							continue;
						}
						
						// 每个面两个三角形
						for (int j = 0; j < 2; j++) {
							if(j==0){
								//1 2
						        //0 3
								//顺时针方向为 0 1 3->逆时针方向为0,3,1
								v0 = getVector3f(face.getVertex(0));
								v1 = getVector3f(face.getVertex(1));
								v2 = getVector3f(face.getVertex(3));
							}
							else{
								v0 = getVector3f(face.getVertex(1));
								v1 = getVector3f(face.getVertex(2));
								v2 = getVector3f(face.getVertex(3));			
							}
							
							// 进行转换后的射线和三角形的碰撞检测
							if (transformedRay.intersectTriangle(v0, v1, v2, location)) {
							    
								// 如果发生了相交
								if (!bFound) {	//bFound初始化是false，因此当bFound是false的时候，就表示是第一次检测到
									// 如果是初次检测到，需要存储射线原点与三角形交点的距离值
									bFound = true;
									closeDis = location.w;	//相交点距离射线原点的位置
									mpTriangle[0]=v0;	//相交的三个顶点的坐标
									mpTriangle[1]=v1;
									mpTriangle[2]=v2;

									mpCube = cube;
								} 
								else {	//第一次检测到之后就把bFound设置成了true
									// 如果之前已经检测到相交事件，则需要把新相交点与之前的相交数据相比较
									if (closeDis > location.w) {	// 最终保留离射线原点更近的
										closeDis = location.w;
										mpTriangle[0]=v0;
										mpTriangle[1]=v1;
										mpTriangle[2]=v2;
										
										mpCube = cube;
									}
								}
							}
						}
					}
			    }
			}
		} // end of if (transformedRay.intersectSphere(worldCenter,worldRadius))

		if(bFound){
			// 如果找到了相交的最近的三角形
			AppConfig.gbTrianglePicked = true;
			
			// 填充数据到被选取三角形的渲染缓存中
			mBufPickedTriangle.clear();
			for (int i = 0; i < 3; i++) {
				IBufferFactory.fillBuffer(mBufPickedTriangle, mpTriangle[i]);
			}
			
			mBufPickedTriangle.position(0);
			
			//选择info级则可以不看debug级消息,级别高可以看级别低的消息
			Log.d("GLWorld", "当前已选择方块数：" + pickedList.size() + ",当前选择方块：" + mpCube.id);
//			Log.d("GLWorld", "当前点击点中心点坐标(转换前)：" + mpCube.getSphereCenter().toString());
//			Log.d("GLWorld", "当前点击点中心点坐标(转换后)：" + _transformedSphereCenter.toString());
			
			if(!AppConfig.Turning){
				addPickedList(mpCube);
			}
		}
		else{
			AppConfig.gbTrianglePicked = false;
		}
		
//		Log.i("GLWorld","耗时：" + (System.currentTimeMillis() - begin) + "millis");

		return bFound;
	}

	/**
	 * 返回和vertex坐标值相同的顶点对象
	 * @param vertex
	 */
	private Vector3f getVector3f(GLVertex vertex) {
		return new Vector3f(vertex.tempX,vertex.tempY,vertex.tempZ);
	}
	/**
	 * 决定旋转
	 * @param kubeAct
	 */
	public void decideTurning(KubeActivity kubeAct) {
		if(pickedList.size()<2){	//选择的小立方体的个数 < 2就退出 因为选择一个判断不出动作而且判断不出层
			return;
		}
		
		Log.i("GLWorld", "选择方块：" + pickedList.size());
		
		Layer[] mLayers = kubeAct.mLayers;	//魔方的9层
		int layerID = -1;
		int index = -1;
		
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<Layer> layerList = new ArrayList<Layer>();
		
		for (int i = 0;i < mLayers.length; i++) { // i从 0~9
			Layer layer = mLayers[i];
			indexList.clear();
//			Log.d("GLWorld","当前层" + i + "现有方块：" +  layer.toString());
					
			for (int j = 0;j < pickedList.size(); j++) {
				Cube cube = pickedList.get(j);
				HashMap<String,Object> map = new HashMap<String, Object>();
				
				if(layer.hashCube(cube,map)){	//如果mShapes中有该cude的id
					index = (Integer)map.get("index");
					indexList.add(index);
				}
			}
		
			if (indexList.size() == pickedList.size()) {	//当前点击方块都在该层
				layerID = i;
				Log.d("GLWorld", "所在" + layerID + "层");
				
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < layer.mShapes.length; j++) {
					if (layer.mShapes[j]!=null) {
						sb.append(layer.mShapes[j].id + ";");
					}
				}
				
				layerList.add(layer);
				Log.d("GLWorld", "该层包含：" + sb.toString());
			}
		}	//end for (int i = 0;i < mLayers.length; i++)
		
		Log.d("GLWorld", "层数：" + layerList.size());
		
		AppConfig.gbNeedPick = false;

		// 获得最新的拾取射线
		Ray ray = PickFactory.getPickRay();
		
		boolean bFound = false;
		
		// 存储着射线原点与三角形相交点的距离
		// 我们最后仅仅保留距离最近的那一个
		float closeDis = 0.0f;

		Vector3f v0, v1, v2;
		Vector3f[] nearest = {new Vector3f(),new Vector3f(),new Vector3f()};
		Layer nearstLayer = null;
		
		Ray transformedRay = new Ray();

		// 如果射线与绑定球发生相交，那么就需要进行精确的三角面级别的相交检测
		// 由于我们的模型渲染数据，均是在模型局部坐标系中
		// 而拾取射线是在世界坐标系中
		// 因此需要把射线转换到模型坐标系中
		// 这里首先计算模型矩阵的逆矩阵
		Matrix4f matInvertModel = new Matrix4f();
		matInvertModel.set(AppConfig.gMatModel);
		matInvertModel.invert();
		
		// 把射线变换到模型坐标系中，把结果存储到transformedRay中
		ray.transform(matInvertModel, transformedRay);
		
		for (Layer layer : layerList) {	//判断当前界面中心点到那个层更近（如果点击的是两个点则应该是有两层都包含该这两个点击点）
			float[] ixArr = layer.getMinMax();	//得到的数据是魔方的模型坐标中的坐标值
			
			float minX = ixArr[0];
			float maxX = ixArr[1];
			float minY = ixArr[2];
			float maxY = ixArr[3];
			float minZ = ixArr[4];
			float maxZ = ixArr[5];
			
			//一个立方体有12个三角形组成
			float[][] faces ={
					{minX,maxY,minZ , maxX,maxY,maxZ , minX,maxY,maxZ}, //top0
					{minX,maxY,minZ , maxX,maxY,minZ , maxX,maxY,maxZ}, //top1
					{minX,minY,minZ , maxX,minY,maxZ , minX,minY,maxZ}, //bottom0
					{minX,minY,minZ , maxX,minY,minZ , maxX,minY,maxZ}, //bottom1
					{minX,maxY,minZ , minX,minY,maxZ , minX,minY,minZ}, //left0
					{minX,maxY,minZ , minX,maxY,maxZ , minX,minY,maxZ}, //left1
					{maxX,maxY,maxZ , maxX,minY,minZ , maxX,minY,maxZ}, //right0
					{maxX,maxY,maxZ , maxX,maxY,minZ , maxX,minY,minZ}, //right1
					{minX,maxY,maxZ , maxX,minY,maxZ , minX,minY,maxZ}, //front0
					{minX,maxY,maxZ , maxX,maxY,maxZ , maxX,minY,maxZ}, //front1
					{minX,maxY,minZ , maxX,minY,minZ , minX,minY,minZ}, //back0
					{minX,maxY,minZ , maxX,maxY,minZ , maxX,minY,minZ}, //back1
			};
			
			//8个点组成的6个面(12个三角形)是否与当前点的射线相交
			for (int j = 0; j < faces.length; j++) {
				v0 = new Vector3f(faces[j][0], faces[j][1], faces[j][2]);
				v1 = new Vector3f(faces[j][3], faces[j][4], faces[j][5]);
				v2 = new Vector3f(faces[j][6], faces[j][7], faces[j][8]);

				// 如果发生了相交
				if (transformedRay.intersectTriangle(v0, v1, v2, location)) {
					Log.d("GLWorld", "层" + layer.index + "与射线相交,距离屏幕:" + location.w);
					
					if (!bFound) { // 如果是初次检测到，需要存储射线原点与三角形交点的距离值
						
						bFound = true;
						closeDis = location.w;
						nearstLayer = layer;
						
						nearest[0] = v0;
						nearest[1] = v1;
						nearest[2] = v2;
					} else {	//如果之前已经检测到相交事件，则需要把新相交点与之前的相交数据相比较
						//最终保留离射线原点更近的(如误差在一定范围内就判断相交平面的面积)
						if(Math.abs(closeDis-location.w)<0.0001){	//与平面距离近似相等则判断三角形面积
							//比当前的大则说明面靠前
							double area1 = calculateArea(nearest[0],nearest[1],nearest[2]);
							double area2 = calculateArea(v0,v1,v2);
							
							Log.i("GLWorld","area1=" + String.valueOf(area1));
							Log.i("GLWorld","area2=" + String.valueOf(area2));
							
							if (area2 > area1) {
								nearstLayer = layer;
								nearest[0] = v0;
								nearest[1] = v1;
								nearest[2] = v2;
							}
						}
						else if (closeDis > location.w) {	//与平面距离不近似相等
							closeDis = location.w;
							nearstLayer = layer;
							
							nearest[0] = v0;
							nearest[1] = v1;
							nearest[2] = v2;
						}
					}
				}
			}
			
			if (bFound) {
				Log.d("GLWorld","距离屏幕最近的三角形: (" + nearest[0].toString() + "), (" + nearest[1].toString() + "), (" + nearest[2].toString() + ")");
			}
		} // end for(Layer layer : layerList)
		
		Log.d("GLWorld", "与当前接触点距离最近三角形为: (" + nearest[0].toString() + "), (" + nearest[1].toString() + "), (" + nearest[2].toString() + ")");

		if (nearstLayer==null) {
			Log.d("GLWorld", "未相交");
		}
		else{
			Log.d("GLWorld", "与当前接触点距离最近层为:" + nearstLayer.index);
		}
		
		//最近的不旋转（最近的应为正面对屏幕的面）
		for (Layer layer : layerList) {
			if (nearstLayer != null && nearstLayer.index!= layer.index) {
				layerID = layer.index;
				break;
			}
		}
		
		if(layerID!=-1){
			AppConfig.Turning = true;
		}
		kubeAct.layerID = layerID;
	}	//end of decideTurning

	/**
	 * 计算空间中三角形的面积
	 * @param vector3f
	 * @param vector3f2
	 * @param vector3f3
	 * @return 面积
	 */
	private double calculateArea(Vector3f v0, Vector3f v1,Vector3f v2) {
		// TODO Auto-generated method stub
		double[] arr = new double[3];
		//pow(double x, double y): x的y次方
		arr[0] = Math.pow(v0.x - v1.x, 2) + Math.pow(v0.y - v1.y, 2) + Math.pow(v0.z - v1.z, 2);
		arr[1] = Math.pow(v0.x - v2.x, 2) + Math.pow(v0.y - v2.y, 2) + Math.pow(v0.z - v2.z, 2);
		arr[2] = Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.y, 2) + Math.pow(v2.z - v1.z, 2);
		
		//升序
		Arrays.sort(arr);
		
		return Math.sqrt(arr[0])*Math.sqrt(arr[1])/2;
	}
}

