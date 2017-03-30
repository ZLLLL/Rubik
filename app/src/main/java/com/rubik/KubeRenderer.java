package com.rubik;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.assist.AppConfig;
import com.assist.LabelMaker;
import com.assist.Matrix4f;
import com.assist.NumericSprite;
import com.assist.PickFactory;
import com.assist.Ray;
import com.assist.Vector3f;

import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;


/**
 * Example of how to use OpenGL|ES in a custom view
 */
class KubeRenderer implements GLSurfaceView.Renderer {    //绘制3D图形的工具
    private LabelMaker mLabels;
    private Paint mLabelPaint;
    private int mLabelMsPF;
    private NumericSprite mNumericSprite;
    private long mStartTime;
    private int mWidth,mHeight;    //3D视窗的宽高
    private int mFrames;
    private int mMsPerFrame;
    private final static int SAMPLE_PERIOD_FRAMES = 12;
    private final static float SAMPLE_FACTOR = 1.0f / SAMPLE_PERIOD_FRAMES;
    
    private GLWorld mWorld;
    private AnimationCallback mCallback;
    private float mMoveX = 0;   //在X轴方向上移动的距离
    private float mMoveY = 0;  //在Y轴方向上移动的距离
    
    public float offsetX;
    public float offsetY;
    
    //add by leizhong 
    private float mAngleY = 0;    //在Y轴上转动的角度
    private float mAngleX = 0;    //在X轴上转动的角度
    public boolean isAnimate = false;    //是否正在动画
    
    public interface AnimationCallback {
        void animate();
    }
    /**
     * Renderer的构造函数，设置画笔的属性
     * */
    public KubeRenderer(GLWorld world, AnimationCallback callback) {
        mWorld = world;
        mCallback = callback;
        
        mLabelPaint = new Paint();
        mLabelPaint.setTextSize(32);    //Set the paint's text size. This value must be > 0
        mLabelPaint.setAntiAlias(true);        //去锯齿
        //Helper to setColor(), that takes a,r,g,b and constructs the color int
        mLabelPaint.setARGB(0xff, 0x00, 0x00, 0x00);
    }
    
    private Vector3f mvEye = new Vector3f(0, 0, 7);
    private Vector3f mvCenter = new Vector3f(0, 0, 0);
    private Vector3f mvUp = new Vector3f(0, 1, 0);
    
    /**
     * 矩阵旋转
     * @param gl
     */
    private void rotate(GL10 gl){
        //缩放
//      gl.glScalef(0.5f, 0.5f, 0.5f);
//        Matrix4f matScale = new Matrix4f();
//        matScale.scale(0.5f,0.5f, 0.5f);
//        AppConfig.gMatModel.mul(matScale);

//      gl.glRotatef(mAngle,        0, 1, 0);
//      gl.glRotatef(mAngle*0.25f,  1, 0, 0);
        //mMoveX mMoveY 是在GLSurfaceView的onTouchEvent中手指在屏幕上的移动距离
        //矩阵旋转
        Matrix4f matRotX = new Matrix4f();
        matRotX.setIdentity();
        matRotX.rotX(mAngleX);
        AppConfig.gMatModel.mul(matRotX);
        
        Matrix4f matRotY = new Matrix4f();
        matRotY.setIdentity();
        matRotY.rotY(mAngleY);
        AppConfig.gMatModel.mul(matRotY);
        
        gl.glMultMatrixf(AppConfig.gMatModel.asFloatBuffer());
    }
    
    /**
     * 转换弧度值到角度值并设置角度值在0~2pi范围内 add by leizhong 2015-10-19
     * @param angle
     */
    private float setAngleInRange(float value) {
        float angle = (float)(value / 180 * Math.PI);
        while(angle >= (float) Math.PI * 2) {    //超过0到2pi的范围
            angle -= (float) Math.PI * 2;
        }
        while(angle < 0) {    //超过0到2pi的范围
            angle += (float)Math.PI * 2;
        }
        return angle;
    }
    
    /**
     * 设置第一次魔方显示的角度值
     */
    private void initAngle() {
        mMoveX = 30;
        mMoveY = 30;
        mAngleX = setAngleInRange(mMoveX);
        mAngleY = setAngleInRange(mMoveY);
    }
    
    /**
     * 转换角度值到弧度值
     */
    private void setAngle() {
        mMoveX += offsetX;
        if(Math.cos(setAngleInRange(mMoveX)) <= 0) {
            mMoveY -= offsetY;
        }
        else {
            mMoveY += offsetY;
        }
        mAngleX = setAngleInRange(mMoveX);
        mAngleY = setAngleInRange(mMoveY);
    }
    
    /**
     * 射线与魔方的相交检测
     * @return
     */
    private boolean touchInCubeSphere(){
        //是否点击在方块区域内
        PickFactory.update(AppConfig.gScreenX, AppConfig.gScreenY);
        // 获得最新的拾取射线
        Ray ray = PickFactory.getPickRay();
        
        Ray transformedRay = new Ray();
        
        // 如果射线与绑定球发生相交，则不旋转
        Matrix4f matInvertModel = new Matrix4f();
        matInvertModel.set(AppConfig.gMatModel);
        matInvertModel.invert();
        // 把射线变换到模型坐标系中，把结果存储到transformedRay中
        ray.transform(matInvertModel, transformedRay);
        
        return transformedRay.intersectSphere(mWorld.worldCenter,mWorld.worldRadius);
    }
    /**
     * Renderer对象调用该方法绘制GLSurfaceView的当前帧
     * */
    public void onDrawFrame(GL10 gl) {
        if (mCallback!=null) {
            mCallback.animate();
        }
        
        gl.glClearColor(0.5f,0.5f,0.5f,1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /*
         * Now we're ready to draw some 3D object
         */
        gl.glMatrixMode(GL10.GL_MODELVIEW);    //设置矩阵为模式矩阵
        gl.glLoadIdentity();
        
        //系统api调用
//        gl.glTranslatef(0, 0, -3.0f);
        Matrix4f.gluLookAt(mvEye,mvCenter,mvUp, AppConfig.gMatView);
        gl.glLoadMatrixf(AppConfig.gMatView.asFloatBuffer());
        
        AppConfig.gMatModel.setIdentity();
        //BEGIN delete by leizhong 2015-10-20
        if (AppConfig.gbNeedPick) {
            AppConfig.gbNeedPick = false;
            setAngle();
        }
        //END delete by leizhong 2015-10-20
        
        gl.glPushMatrix();
        rotate(gl);
        mWorld.draw(gl);
        gl.glPopMatrix();
        
        //BEGIN delete 2015-11-30
//        mWorld.intersectDetect();    //射线与模型的精确碰撞检测 从魔方相交检测到小立方体相交检测到小立方体面的相交检测
//        gl.glPushMatrix();
//        mWorld.drawPickedTriangle(gl);
//        gl.glPopMatrix();
        //END delete 2015-11-30
        
        //BEGIN delete by leizhong 2015-10-15
/*        //写文字
        mLabels.beginDrawing(gl, mWidth, mHeight);
        float msPFX = mWidth - mLabels.getWidth(mLabelMsPF) - 1;
        mLabels.draw(gl, msPFX, 0, mLabelMsPF);
        mLabels.endDrawing(gl);
        drawMsPF(gl, msPFX);*/
        //END delete by leizhong 2015-10-15
    }
    
    /**
     * 当GLSurfaceView大小改变时回调该方法
     * */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);    //设置3D视窗的大小以及位置

        AppConfig.gpViewport[0] = 0;
        AppConfig.gpViewport[1] = 0;
        AppConfig.gpViewport[2] = width;
        AppConfig.gpViewport[3] = height;
        
        mWidth = width;
        mHeight = height;
        
        /*
         * Set our projection matrix. This doesn't have to be done
         * each time we draw, but usually a new projection needs to be set
         * when the viewport is resized.
         */
        float ratio = (float)width / height;
        //对投影矩阵应用随后的矩阵操作
        gl.glMatrixMode(GL10.GL_PROJECTION);
        //重置当前的模型视图矩阵
        gl.glLoadIdentity();
        
        //GLU.gluPerspective(gl, 45f, ratio, 2,12);        
        //改为托管矩阵运行
        Matrix4f.gluPersective(35.0f, ratio, 0.1f, 100, AppConfig.gMatProject);
        gl.glLoadMatrixf(AppConfig.gMatProject.asFloatBuffer());
        AppConfig.gMatProject.fillFloatArray(AppConfig.gpMatrixProjectArray);
        //设置当前的矩阵模型堆栈为模型堆栈
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        //重置当前的模型视图矩阵
        gl.glLoadIdentity();
        //关闭抗抖动
        gl.glDisable(GL10.GL_DITHER);
//      gl.glActiveTexture(GL10.GL_TEXTURE0);
//        mWorld.createCubeImage();    //创建魔方的小立方体的编号
        
        initAngle();
    }
    /**
     * 当GLSurfaceView被创建时回调该方法
     * */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);    //设置阴影平滑模式
        // Depth buffer setup.
//        gl.glClearDepthf(1.0f);
        // Enables depth testing.
//        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
//        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
//        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
                
        AppConfig.gMatModel.setIdentity();    //模型矩阵初始化
        
        //BEGIN delete by leizhong 2015-10-15
/*        //绘制文字代码
        if (mLabels != null) {
            mLabels.shutdown(gl);
        } else {
            mLabels = new LabelMaker(true, 256, 64);
        }
        
        mLabels.initialize(gl);
        mLabels.beginAdding(gl);
        
        mLabelMsPF = mLabels.add(gl, "ms/frame", mLabelPaint);
        mLabels.endAdding(gl);

        if (mNumericSprite != null) {
            mNumericSprite.shutdown(gl);
        } else {
            mNumericSprite = new NumericSprite();
        }
        mNumericSprite.initialize(gl, mLabelPaint);*/
        //END delete by leizhong 2015-10-15
    }

    /**
     * 判断旋转的方向
     * @param direction
     */
    public void decideTurning(boolean direction) {
        KubeActivity kubeAct = (KubeActivity)mCallback;
        kubeAct.turningDirection = direction;
        mWorld.decideTurning(kubeAct);
    }
    
    /**
     * 清空pcikedList
     */
    public void clearPickedCubes(){
        mWorld.clearPickedCubes();
    }
}
