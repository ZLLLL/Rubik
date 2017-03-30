package com.logiccube;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.recoverCross.RecoverTask;


public class CubeService extends Service {

	private static final String TAG = "CubeService";

	private ServiceHandler mServiceHandler;
	private Looper mServiceLooper;

	public static final String CUBE_CHANGE_FILTER = "com.logiccube.CubeService";

	public static final String ACTION_KEY = "CubeService_ACTION_KEY";
	public static final String ACTION_CHANGE_VALUE_KEY = "CubeService_ACTION_CHANGE_VALUE_KEY";
	public static final String ACTION_INVALID_CUBE_VALUE_KEY = "CubeService_ACTION_INVALID_CUBE_VALUE_KEY";
	public static final String ACTION_INITIAL_VALUE_KEY = "CubeService_ACTION_INITIAL_VALUE_KEY";
	public static final String CUBE_INT_VALUE_KEY = "CubeService_CUBE_INT_VALUE_KEY";
	public static final String CUBE_HISTORY_VALUE_KEY = "CubeService_CUBE_HISTORY_VALUE_KEY";
	public static final String CUBE_CUR_STEP_NUM_VALUE_KEY = "CubeService_CUBE_CUR_STEP_NUM_VALUE_KEY";
	public static final String NEXT_BTN_STATUS_KEY = "CubeService_NEXT_BTN_STATUS_KEY";
	public static final String FALLBACK_BTN_STATUS_KEY = "CubeService_FALLBACK_BTN_STATUS_KEY";
	public static final String ALL_BTN_STATUS_KEY = "CubeService_ALL_BTN_STATUS_KEY";
	public static final String ALL_ACTION_BTN_STATUS_KEY = "CubeService_ALL_ACTION_BTN_STATUS_KEY";
	public static final String STOP_ACTION_BTN_STATUS_KEY = "CubeService_STOP_ACTION_BTN_STATUS_KEY";
	public static final String CUBE_RECOVER_VALUE_KEY = "CubeService_CUBE_RECOVER_VALUE_KEY";

	public static final String ACTIVITY_ACTION_UPDATE_CUBE = "CubeService_ACTIVITY_ACTION_UPDATE_CUBE";
	public static final String ACTIVITY_ACTION_SET_ALL_BTN = "CubeService_ACTIVITY_ACTION_SET_ALL_BTN";
	public static final String ACTIVITY_ACTION_SET_ALL_ACTION_BTN = "CubeService_ACTIVITY_ACTION_SET_ALL_ACTION_BTN";
	public static final String ACTIVITY_ACTION_SET_STOP_BTN = "CubeService_ACTIVITY_ACTION_SET_STOP_BTN";

	public static final String ACTION_INITIAL = "CubeService_cube_initial";
	public static final String ACTION_NEXT = "CubeService_cube_next";
	public static final String ACTION_FALLBACK = "CubeService_cube_fallback";
	public static final String ACTION_START = "CubeService_cube_start";
	public static final String ACTION_STOP = "CubeService_cube_stop";
	public static final String ACTION_CHANGE = "CubeService_cube_change";
	public static final String ACTION_CHANGE_PATCH = "CubeService_cube_change_patch";
	
	private boolean mIsValidCube = false;
	private static boolean mCubeRecoverStatus = true;

	private static final int HANDLER_START_RECOVER_TASK = 1001;

	public static final String[] ALLOW_ACTION = { ACTION_INITIAL, ACTION_NEXT,
			ACTION_FALLBACK, ACTION_START, ACTION_CHANGE, ACTION_CHANGE_PATCH };

	private Cube mCube = null;

	public Cube getCube() {
		return mCube;
	}

	private final class ServiceHandler extends Handler {
		// constructor
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			//Context context = getApplicationContext();
			switch (msg.what) {
			case HANDLER_START_RECOVER_TASK:
				Log.d(TAG,"receive HANDLER_START_RECOVER_TASK.");
				RecoverTask recoverTask = new RecoverTask(mCube);
				if(!recoverTask.start()){
					Log.e(TAG,"Recover failed!");
					mCubeRecoverStatus = false;
				}else{
				    Log.e(TAG,"Recover successed!");
				    mCubeRecoverStatus = true;
				}
				setAllBtn(true);
				updateCube();
				setAllActionBtn(false);
				break;
			default:
				break;
			}
		}
	}

	@Override
	// TODO: startId need to pass , and finally stop the service.
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Context context = getApplicationContext();
		String action = intent.getStringExtra(ACTION_KEY);
		
		Log.d(TAG, "onStartCommand action:"+action);
		
		if(!isValidAction(action)){
			return Service.START_NOT_STICKY;
		}
		
		if(ACTION_INITIAL.equals(action)){
			mIsValidCube = true;
			try{
				String[] cubeColorOneArray = intent.getStringArrayExtra(ACTION_INITIAL_VALUE_KEY);
				String[][] cubeColorArray = new CubeColorSnapshot(cubeColorOneArray).getmCubeStrTwoArray();
				mCube = new Cube(cubeColorArray);
				if(!mCube.isValidCube()){
					Log.e(TAG,"================invalid cube============");
					Log.e(TAG,CubeUtil.getPrintCubeTwoArrayInfo(cubeColorArray));
					Log.e(TAG,"================invalid cube============");
					//mCube = new Cube(CubeUtil.DEFAULT_CUBE_COLOR_ARRAY);
					mIsValidCube = false;
				}
			}catch(Exception e){
				Log.e(TAG,"Exception:"+e.toString());
				e.printStackTrace();
				//mCube = new Cube(CubeUtil.DEFAULT_CUBE_COLOR_ARRAY);
				mIsValidCube = false;
			}
			
			updateCube();
		}else if(ACTION_CHANGE.equals(action)){
			if(mCube == null){
				Log.e(TAG, "cube is not initial!!");
				return Service.START_NOT_STICKY;
			}
			
			if(!mIsValidCube){
				Log.e(TAG, "cube is invalid!!");
				return Service.START_NOT_STICKY;
			}
			
			String step = intent.getStringExtra(ACTION_CHANGE_VALUE_KEY);
			if(!CubeUtil.isValidOneStep(step)){
				return Service.START_NOT_STICKY;
			}
			mCube.doAction(step);
			//Log.d(TAG, mCube.toString());
			//Log.d(TAG, mCube.getCurrentPic().toString());
			updateCube();
		}else if(ACTION_NEXT.equals(action)){
			if(mCube == null){
				Log.e(TAG, "cube is not initial!!");
				return Service.START_NOT_STICKY;
			}
			if(!mIsValidCube){
				Log.e(TAG, "cube is invalid!!");
				return Service.START_NOT_STICKY;
			}
			if(!mCube.redo()){
				Log.e(TAG, "redo failed!");
			}
			updateCube();
		}else if(ACTION_FALLBACK.equals(action)){
			if(mCube == null){
				Log.e(TAG, "cube is not initial!!");
				return Service.START_NOT_STICKY;
			}
			if(!mIsValidCube){
				Log.e(TAG, "cube is invalid!!");
				return Service.START_NOT_STICKY;
			}
			if(!mCube.fallback()){
				Log.e(TAG, "fallback failed!");
			}
			updateCube();
		}else if(ACTION_START.equals(action)){
			setAllBtn(false);
			if(mCube == null){
				Log.e(TAG, "cube is not initial!!");
				return Service.START_NOT_STICKY;
			}
			if(!mIsValidCube){
				Log.e(TAG, "cube is invalid!!");
				return Service.START_NOT_STICKY;
			}
			startRecoverTask();

			return Service.START_NOT_STICKY;
		}else if(ACTION_STOP.equals(action)){
			if(mCube == null){
				Log.e(TAG, "cube is not initial!!");
				return Service.START_NOT_STICKY;
			}
			if(!mIsValidCube){
				Log.e(TAG, "cube is invalid!!");
				return Service.START_NOT_STICKY;
			}
			setAllActionBtn(true);
			setStopBtn(false);
			updateCube();
			return Service.START_NOT_STICKY;
		}

		// if the service was killed , not restart again
		return Service.START_NOT_STICKY;

	}
	
	private void startRecoverTask(){
		Message msg = mServiceHandler.obtainMessage(HANDLER_START_RECOVER_TASK);
        mServiceHandler.sendMessage(msg);
        return;
	}
	
	private boolean isValidAction(String action){
		for (int i = 0; i < ALLOW_ACTION.length; i++){
			if (ALLOW_ACTION[i].equals(action)){
				return true;
			}
		}
		Log.e(TAG, "Invalid action:"+action);
		return false;
	}
	
	private void updateCube(){
		if(mCube == null){
			Log.e(TAG, "mCube is null");
			Intent intent = new Intent();
			intent.setAction(CubeService.CUBE_CHANGE_FILTER);
			intent.putExtra(ACTION_KEY, ACTION_INVALID_CUBE_VALUE_KEY);
			sendBroadcast(intent);
			return;
		}
		if(mCube.getCurrentPic() == null){
			Log.e(TAG, "mCube.getCurrentPic() is null");
			Intent intent = new Intent();
			intent.setAction(CubeService.CUBE_CHANGE_FILTER);
			intent.putExtra(ACTION_KEY, ACTION_INVALID_CUBE_VALUE_KEY);
			sendBroadcast(intent);
			return;
		}
		if(!mIsValidCube){
			Log.e(TAG, "mCube is invalid.");
			Intent intent = new Intent();
			intent.setAction(CubeService.CUBE_CHANGE_FILTER);
			intent.putExtra(ACTION_KEY, ACTION_INVALID_CUBE_VALUE_KEY);
			sendBroadcast(intent);
			return;
		}
		int[][] curCubeIntArray = mCube.getCurrentPic().getCurPic();
		mCube.formatHistory();
		String history = mCube.getCurCubeHistory();

		boolean nextBtnStatus = mCube.hasNexStep();
		boolean fallbackBtnStatus = mCube.hasLastStep();
		
		Intent intent = new Intent();
		intent.setAction(CubeService.CUBE_CHANGE_FILTER);
		intent.putExtra(ACTION_KEY, ACTIVITY_ACTION_UPDATE_CUBE);
		intent.putExtra(NEXT_BTN_STATUS_KEY, nextBtnStatus);
		intent.putExtra(FALLBACK_BTN_STATUS_KEY, fallbackBtnStatus);
		intent.putExtra(CUBE_HISTORY_VALUE_KEY, history);
		intent.putExtra(CUBE_RECOVER_VALUE_KEY, mCubeRecoverStatus);
		intent.putExtra(CUBE_CUR_STEP_NUM_VALUE_KEY, mCube.getCubeCurStepNum());
		intent.putExtra(CUBE_INT_VALUE_KEY, new CubeIntSnapshot(curCubeIntArray).getCubeIntOneArray());
		sendBroadcast(intent);
		return;
	}
	
	private void setAllBtn(boolean status){
		Intent intent = new Intent();
		intent.setAction(CubeService.CUBE_CHANGE_FILTER);
		intent.putExtra(ACTION_KEY, ACTIVITY_ACTION_SET_ALL_BTN);
		intent.putExtra(ALL_BTN_STATUS_KEY, status);
		sendBroadcast(intent);
		return;
	}
	
	private void setStopBtn(boolean status){
		Intent intent = new Intent();
		intent.setAction(CubeService.CUBE_CHANGE_FILTER);
		intent.putExtra(ACTION_KEY, ACTIVITY_ACTION_SET_STOP_BTN);
		intent.putExtra(STOP_ACTION_BTN_STATUS_KEY, status);
		sendBroadcast(intent);
		return;
	}
	
	private void setAllActionBtn(boolean status){
		Intent intent = new Intent();
		intent.setAction(CubeService.CUBE_CHANGE_FILTER);
		intent.putExtra(ACTION_KEY, ACTIVITY_ACTION_SET_ALL_ACTION_BTN);
		intent.putExtra(ALL_ACTION_BTN_STATUS_KEY, status);
		sendBroadcast(intent);
		return;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.
		HandlerThread thread = new HandlerThread(TAG,
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		if (null != mServiceLooper) {
			mServiceHandler = new ServiceHandler(mServiceLooper);
		}

	}

	@Override
	public void onDestroy() {
		mServiceLooper.quit();
	}

}
