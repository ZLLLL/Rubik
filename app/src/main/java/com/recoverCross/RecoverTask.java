package com.recoverCross;

import android.util.Log;

import com.logiccube.Cube;

public class RecoverTask {
	private static final String TAG = "RecoverTask";
	private static int TEST_STEP = 7;
	
	private Cube mCube = null;
	private static final int MAX_STEP = 10;
	
	public RecoverTask(Cube cube){
		if(cube == null || !cube.isValidCube()){
			Log.e(TAG,"cube is invalid!");
			return;
		}
		mCube = cube;
	}
	
	public boolean start(){
		if(mCube == null || !mCube.isValidCube()){
			Log.e(TAG,"cube is invalid!");
			return false;
		}
		int step = 0;
		Log.d(TAG,"Recover cube begin......");
		if(TEST_STEP <= 0){
			return true;
		}
		boolean isCrossRightD = RecoverCube.isCrossRightD(mCube.getCurrentPic().getCurPic());
		Log.d(TAG,"doing CrossRightD");
		while(!isCrossRightD){
			if(step > MAX_STEP){
				Log.e(TAG,"[CrossRightD]over max step! stop it.");
				return false;
			}
			RuleCrossRightD ruleCrossRightD = new RuleCrossRightD(mCube);
			String action = ruleCrossRightD.getAction();
			Log.d(TAG,"[CrossRightD]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[CrossRightD]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isCrossRightD = RecoverCube.isCrossRightD(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"CrossRightD finished.");
		
		
		
		if(!isCrossRightD){
			Log.e(TAG,"CrossRightD is not complete.");
			return false;
		}
		if(TEST_STEP <= 1){
			return true;
		}
		Log.d(TAG,"doing FloorRightD.");
		step = 0;
		boolean isFloorRightD = RecoverCube.isFloorRightD(mCube.getCurrentPic().getCurPic());
		while(!isFloorRightD){
			if(step > MAX_STEP){
				Log.e(TAG,"[FloorRightD]over max step! stop it.");
				return false;
			}
			RuleFloorRightD ruleFloorRightD = new RuleFloorRightD(mCube);
			String action = ruleFloorRightD.getAction();
			Log.d(TAG,"[FloorRightD]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[FloorRightD]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isFloorRightD = RecoverCube.isFloorRightD(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"FloorRightD finished.");
		
		if(!isFloorRightD){
			Log.e(TAG,"FloorRightD is not complete.");
			return false;
		}
		if(TEST_STEP <= 2){
			return true;
		}
		Log.d(TAG,"doing FloorRightD2");
		step = 0;
		boolean isFloorRightD2 = RecoverCube.isFloorRightD2(mCube.getCurrentPic().getCurPic());
		while(!isFloorRightD2){
			if(step > MAX_STEP){
				Log.e(TAG,"[FloorRightD2]over max step! stop it.");
				return false;
			}
			RuleFloorRightD2 ruleFloorRightD2 = new RuleFloorRightD2(mCube);
			String action = ruleFloorRightD2.getAction();
			Log.d(TAG,"[FloorRightD2]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[FloorRightD2]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isFloorRightD2 = RecoverCube.isFloorRightD2(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"FloorRightD2 finished.");

		if(!isFloorRightD2){
			Log.e(TAG,"FloorRightD2 is not complete.");
			return false;
		}
		if(TEST_STEP <= 3){
			return true;
		}
		Log.d(TAG,"doing CrossRightU");
		boolean isCrossRightU = RecoverCube.isCrossRightU(mCube.getCurrentPic().getCurPic());
		step = 0;
		while(!isCrossRightU){
			if(step > MAX_STEP){
				Log.e(TAG,"[CrossRightU]over max step! stop it.");
				return false;
			}
			RuleCrossRightU ruleCrossRightU = new RuleCrossRightU(mCube);
			String action = ruleCrossRightU.getAction();
			Log.d(TAG,"[CrossRightU]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[CrossRightU]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isCrossRightU = RecoverCube.isCrossRightU(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"CrossRightU finished.");
	
		if(!isCrossRightU){
			Log.e(TAG,"CrossRightU is not complete.");
			return false;
		}
		if(TEST_STEP <= 4){
			return true;
		}
		Log.d(TAG,"doing RightU");
		boolean isRightU = RecoverCube.isRightU(mCube.getCurrentPic().getCurPic());
		step = 0;
		while(!isRightU){
			if(step > MAX_STEP){
				Log.e(TAG,"[RightU]over max step! stop it.");
				return false;
			}
			RuleRightU ruleRightU = new RuleRightU(mCube);
			String action = ruleRightU.getAction();
			Log.d(TAG,"[RightU]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[RightU]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isRightU = RecoverCube.isRightU(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"RightU finished.");
		
		if(!isRightU){
			Log.d(TAG,"RightU is not complete.");
			return false;
		}
		if(TEST_STEP <= 5){
			return true;
		}
		Log.d(TAG,"doing JiaoKuaiRightU");
		boolean isJiaoKuaiRightU = RecoverCube.isJiaoKuaiRightU(mCube.getCurrentPic().getCurPic());
		step = 0;
		while(!isJiaoKuaiRightU){
			if(step > MAX_STEP){
				Log.e(TAG,"[JiaoKuaiRightU]over max step! stop it.");
				return false;
			}
			RuleJiaoKuaiRightU ruleJiaoKuaiRightU = new RuleJiaoKuaiRightU();
			String action = ruleJiaoKuaiRightU.getAction(mCube);
			Log.d(TAG,"[JiaoKuaiRightU]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[JiaoKuaiRightU]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isJiaoKuaiRightU = RecoverCube.isJiaoKuaiRightU(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"JiaoKuaiRightU finished.");
		
		if(TEST_STEP <= 6){
			return true;
		}
		Log.d(TAG,"doing LengKuaiRightU");
		boolean isLengKuaiRightU = RecoverCube.isLengKuaiRightU(mCube.getCurrentPic().getCurPic());
		step = 0;
		while(!isLengKuaiRightU){
			if(step > 0){
				Log.e(TAG,"[LengKuaiRightU]over max step! stop it.");
				return false;
			}
			RuleLengKuaiRightU ruleLengKuaiRightU = new RuleLengKuaiRightU();
			String action = ruleLengKuaiRightU.getAction(mCube);
			Log.d(TAG,"[LengKuaiRightU]step["+step+"] do action:"+action);
			if(!mCube.doAction(action)){
				Log.e(TAG,"[LengKuaiRightU]step["+step+"] do action failed:"+action);
				return false;
			}
			step++;
			isLengKuaiRightU = RecoverCube.isLengKuaiRightU(mCube.getCurrentPic().getCurPic());
		}
		Log.d(TAG,"LengKuaiRightU finished.");
		
		Log.d(TAG,"Recover cube complete!");
		return true;
	}
}
