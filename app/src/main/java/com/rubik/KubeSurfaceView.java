package com.rubik;

import com.assist.AppConfig;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class KubeSurfaceView extends GLSurfaceView {	//用于显示3D图形
    private float mPreviousX;	//之前的x坐标
    private float mPreviousY;	//之前的y坐标
    private float mDownX;	//按下的x坐标
    private float mDownY;	//按下的y坐标
    
    private KubeRenderer mRenderer;		//绘制3D图形的Renderer
    
	public KubeSurfaceView(Context context,KubeRenderer renderer) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mRenderer = renderer;
		this.setRenderer(this.mRenderer);
		
	    //设置渲染模式为主动渲染   
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);   
	}

	/**
	 * 手机屏幕事件的处理方法onTouchEvent。该方法在View类中的定义，并且所有的View子类全部重写了
	 * 该方法，应用程序可以通过该方法处理手机屏幕的触摸事件。
	 * @param e 参数event：参数event为手机屏幕触摸事件封装类的对象，其中封装了该事件的所有信息，
	 * 例如触摸的位置、触摸的类型以及触摸的时间等。该对象会在用户触摸手机屏幕时被创建。
	 * */
	@Override 
	 public boolean onTouchEvent(MotionEvent e) {
		
		if(KubeActivity.isRotateByFinger) //如果可以让用户手动控制魔方的层的旋转就需要执行下面的程序，但是现在只提供给用户还原魔方的功能。
		{
	        float x = e.getX();
	        float y = e.getY();
	        AppConfig.setTouchPosition(x, y);
	        
	        if(!AppConfig.Turning){
	        	AppConfig.gbNeedPick = true;
	        }
	        switch (e.getAction()) {
	        case MotionEvent.ACTION_DOWN:	//屏幕被按下
	        	mRenderer.clearPickedCubes();
	        	mDownX = x;
	        	mDownY = y;
	        	break;
	        case MotionEvent.ACTION_MOVE:	//在屏幕中拖动
	            // 手势距离
	            //float d = (float) (Math.sqrt(dx * dx + dy * dy));
		        //绕X轴旋转
	            float dx = y - mPreviousY;
	            //绕y轴旋转
	            float dy = x - mPreviousX;
         
	            mRenderer.offsetX = dx;
	            mRenderer.offsetY = dy;
	            //Request that the renderer render a frame. This method is typically used
	            //when the render mode has been set to RENDERMODE_WHEN_DIRTY, so that frames
	            //are only rendered on demand. May be called from any thread. Must not be
	            //called before a renderer has been set.
	            //要求renderer渲染一帧
	            requestRender();
	            break;
	        case MotionEvent.ACTION_UP:		//屏幕被抬起，当触控笔离开屏幕时触发
	            boolean direction = false;
	        	if(Math.abs(x-mDownX) > Math.abs(y-mDownY)){ //抬起的x坐标到按下的x坐标的距离比抬起的y坐标到按下的y坐标的距离大
	            	if (x - mDownX>0) {	//如果在x方向上是向右移动
						direction = true;
					}
	            	else{	//如果在x方向上市向左移动
	            		direction = false;
	            	}
	            }
	            else{	//抬起的y坐标到按下的y坐标的距离比抬起的x坐标到按下的x坐标的距离大
	            	if (y - mDownY>0) {	//如果在y方向上是向上移动
						direction = true;
					}
	            	else{	//如果在y方向上是向下移动
	            		direction = false;
	            	}	            	
	            }
	            
	            mRenderer.offsetX = 0;
	            mRenderer.offsetY = 0;
	            
	            final boolean direct = direction; //如果在x或y方向上面坐标增加了direct为true否则direct为false

	            //先选中的方块，判断魔方转向
	            //根据当前屏幕接触点做射线来判断接触面
	            mRenderer.decideTurning(direct);

	            mDownX = 0;
	            mDownY = 0;
		        AppConfig.setTouchPosition(0, 0);
	        }
	        mPreviousX = x;
	        mPreviousY = y;
	        return true;
	    }
		else {
			return false;
		}
	}
}

