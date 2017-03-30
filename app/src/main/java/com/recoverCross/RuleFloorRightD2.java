package com.recoverCross;

import java.util.ArrayList;
import java.util.HashMap;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;
import com.logiccube.LengKuai;

import android.util.Log;


public class RuleFloorRightD2 {
	private static final String TAG = "RuleFloorRightD2";
	/*
	 * 记录当前阶段需要还原的位置
	 * 0 [L5,F3]
	 * 1 [F5,R3]
	 * 2 [R5,B3]
	 * 3 [B5,L3]
	 */
	private int mCurStep = -1;
	
	private Cube mCube = null;
	
	private boolean[] mIsStepRight = {false, false,false,false};
	
	private static ArrayList<RuleObjLengKuai> mRule = new ArrayList<RuleObjLengKuai>();
	
	/*
	 * 记录当前步骤需要哪个棱块
	 */
	HashMap<Integer, int[]> mStepMap = new HashMap<Integer, int[]>();
	
	/*
	 * 棱块的数组 包含位置和颜色信息
	 * 数组的位置信息
     * [F1,U7],[R1,U5],[B1,U1],[L1,U3]
	 * [L5,F3],[F5,R3],[R5,B3],[B5,L3]
	 * [F7,D1],[R7,D5],[B7,D7],[L7,D3]
	 */
	private LengKuai[] mLengKuaiArray = null;
	
	public RuleFloorRightD2(Cube cube){
		mCube = cube;
		/*
		 * 记录当前步骤需要哪个棱块
		 */
		int[] value0 = {3,0}; 
		mStepMap.put(Integer.valueOf(0), value0);
		int[] value1 = {0,1}; 
		mStepMap.put(Integer.valueOf(1), value1);
		int[] value2 = {1,2}; 
		mStepMap.put(Integer.valueOf(2), value2);
		int[] value3 = {2,3}; 
		mStepMap.put(Integer.valueOf(3), value3);
		
		initRule();
		updateInfo();
	}
	
	private void updateInfo(){
		mLengKuaiArray = CubeUtil.getCurrLengKuaiArray(mCube.getCurrentPic().getCurPic());
		calCurStep();
	}
	
	/*
	 * 先处理目标棱块在U上的步骤，再处理其他的
	 * 0 [L5,F3]
	 * 1 [F5,R3]
	 * 2 [R5,B3]
	 * 3 [B5,L3]
	 */
	private void calCurStep(){
		if(mLengKuaiArray == null){
			mLengKuaiArray = CubeUtil.getCurrLengKuaiArray(mCube.getCurrentPic().getCurPic());
		}
		if(!mIsStepRight[0] && isLengKuaiRight(4, mCube.getCurrentPic().getCurPic())){
		    mIsStepRight[0] = true;
        }
        
        if(!mIsStepRight[1] && isLengKuaiRight(5, mCube.getCurrentPic().getCurPic())){
            mIsStepRight[1] = true;
        }
        
        if(!mIsStepRight[2] && isLengKuaiRight(6, mCube.getCurrentPic().getCurPic())){
            mIsStepRight[2] = true;
        }
        
        if(!mIsStepRight[3] && isLengKuaiRight(7, mCube.getCurrentPic().getCurPic())){
            mIsStepRight[3] = true;
        }
		boolean ret = true;
		if(mCurStep > 0 && mCurStep < mIsStepRight.length && !mIsStepRight[mCurStep]){
			//keep this step
			return;
		}
		for (int i = 0; i < mIsStepRight.length; i++){
			ret = ret && mIsStepRight[i];
			if(mIsStepRight[i]){
				continue;
			}
			int index = findNeededLengKuaiIndex(i);
			if(index == -1){
				mCurStep = mIsStepRight.length; //do nothing
				return;
			}
			//目标棱块的位置在U上
			if(index >=0 && index <= 3){
				mCurStep = i;
				return;
			}
		}
		if(ret){
			mCurStep = mIsStepRight.length;//do nothing
		}else{
			for(int i = 0; i < mIsStepRight.length; i++){
				if(!mIsStepRight[i]){
					mCurStep = i;
					break;
				}
			}
		}
		return;
		/*
		if(isLengKuaiRight(4, mCube.getCurrentPic().getCurPic())){
			mCurStep = 1;
		}else{
			mCurStep = 0;
		}
		
		if(isLengKuaiRight(5, mCube.getCurrentPic().getCurPic())){
			mCurStep = 2;
		}
		
		if(isLengKuaiRight(6, mCube.getCurrentPic().getCurPic())){
			mCurStep = 3;
		}
		
		if(isLengKuaiRight(7, mCube.getCurrentPic().getCurPic())){
			mCurStep = 4;
		}*/
	}
	/*
	 * 当前阶段需要还原的位置
	 * 0 [L5,F3]
	 * 1 [F5,R3]
	 * 2 [R5,B3]
	 * 3 [B5,L3]
	 */
	private int findNeededLengKuaiIndex(int step){
		/*
		 * if(mCurStep < 0){
			calCurStep();
		}*/
		if(step < 0 || step >= mIsStepRight.length){
			Log.e(TAG,"findNeededLengKuaiIndex error step:"+step);
			return -1;
		}
		int[] color = new int[2];
		int[][] curPic = mCube.getCurrentPic().getCurPic();
		int[] curIntArray = mStepMap.get(Integer.valueOf(step));
		

		color[0] = curPic[curIntArray[0]][CubeUtil.CENTER_KUAI_INDEX];
		color[1] = curPic[curIntArray[1]][CubeUtil.CENTER_KUAI_INDEX];

		
		LengKuai needed = new LengKuai(color);
		for(int i = 0; i < mLengKuaiArray.length; i++){
			if(needed.isSame(mLengKuaiArray[i])){
				return i;
			}
		}
		Log.e(TAG, "can not found lengkuai step:"+mCurStep);
		Log.e(TAG, "cube:"+ mCube.getCurrentPic().toString());
		return -1;
	}
	
	private int getNeededLengKuaiPosStatus(int index){
		if(mCurStep < 0){
			calCurStep();
		}

		int[][] curPic = mCube.getCurrentPic().getCurPic();
		int[] curIntArray = mStepMap.get(Integer.valueOf(mCurStep));
		if(mLengKuaiArray[index].isColorExist(curPic[curIntArray[0]][CubeUtil.CENTER_KUAI_INDEX])
			&& mLengKuaiArray[index].isColorExist(curPic[curIntArray[1]][CubeUtil.CENTER_KUAI_INDEX])){
			if(!mLengKuaiArray[index].hasPos(mCurStep)){
				return -1;
			}
			if(mLengKuaiArray[index].isPosColor(mCurStep, curPic[mCurStep][CubeUtil.CENTER_KUAI_INDEX])){
				return 1;
			}
			return 0;
		}
		return -1;
	}
	
	
	public String getAction(){
		updateInfo();

		if(mCurStep < 0 || mCurStep >= mIsStepRight.length){
			Log.e(TAG, "nothing need to do step:"+mCurStep);
			return null;
		}
		
		if(!RecoverCube.isFloorRightD(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isFloorRightD failed step:"+mCurStep);
			return null;
		}
		if(RecoverCube.isFloorRightD2(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isFloorRightD2 ok.");
			return null;
		}
		Log.d(TAG, "[getAction]mCurStep:"+mCurStep);
		int index = findNeededLengKuaiIndex(mCurStep);
		for(int i = 0; i < mRule.size(); i++){
			RuleObjLengKuai temp = mRule.get(i);
			if(temp.isSameStepAndIndex(mCurStep, index)){
				if(temp.getmNeededLengKuaiPosStatus() == -1){
					return temp.getmAction();
				}
				if(temp.isSamePosStatus(getNeededLengKuaiPosStatus(index))){
					return temp.getmAction();
				}
			}
		}
		Log.e(TAG, "getAction fail step:"+mCurStep);
		Log.e(TAG, "cube:"+ mCube.getCurrentPic().toString());
		return null;
	}
	
	/*
	 * 棱块的数组 包含位置和颜色信息
	 * 数组的位置信息
     * [F1,U7],[R1,U5],[B1,U1],[L1,U3]
	 * [L5,F3],[F5,R3],[R5,B3],[B5,L3]
	 * [F7,D1],[R7,D5],[B7,D7],[L7,D3]
	 */
	
	private boolean isLengKuaiRight(int lengKuaiIndex, int[][] curPic){
    	if(mLengKuaiArray == null){
			mLengKuaiArray = CubeUtil.getCurrLengKuaiArray(curPic);
		}
    	LengKuai curLengKuai = mLengKuaiArray[lengKuaiIndex];
    	for(int i = 0; i < curLengKuai.getmPos().length; i++){
    		int pos = curLengKuai.getmPos()[i];
    		int color = curPic[pos][CubeUtil.CENTER_KUAI_INDEX];
    		if(!curLengKuai.isPosColor(pos,color)){
    			return false;
    		}
    	}
    	return true;
    }
	/*
	 * 0 [L5,F3]
	 * 1 [F5,R3]
	 * 2 [R5,B3]
	 * 3 [B5,L3]
	 */
	private static void initRule(){
    	RuleObjLengKuai temp = null;
    	//Step 0 [L5,F3]
    	temp = new RuleObjLengKuai(0, 0, 1, "F'U'F'U'F'UFUF");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 0, 0, "ULULULU'L'U'L'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 1, -1, "U");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 2, -1, "UU");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 3, -1, "U'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(0, 4, 0, "F'U'F'U'F'UFUF");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 5, -1, "FUFUFU'F'U'F'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 6, -1, "RURURU'R'U'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 7, -1, "L'U'L'U'L'ULUL");
    	mRule.add(temp);

    	//Step 1 [F5,R3]
    	temp = new RuleObjLengKuai(1, 1, 1, "R'U'R'U'R'URUR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 1, 0, "UFUFUFU'F'U'F'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 0, -1, "U'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 2, -1, "U'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 3, -1, "UU");
    	mRule.add(temp);

    	temp = new RuleObjLengKuai(1, 5, 0, "R'U'R'U'R'URUR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 6, -1, "RURURU'R'U'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 7, -1, "L'U'L'U'L'ULUL");
    	mRule.add(temp);
    	
    	//Step 2 [R5,B3]
    	temp = new RuleObjLengKuai(2, 2, 1, "B'U'B'U'B'UBUB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 2, 0, "URURURU'R'U'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 0, -1, "UU");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 1, -1, "U'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 3, -1, "U");
    	mRule.add(temp);

    	temp = new RuleObjLengKuai(2, 6, 0, "RURURU'R'U'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 7, -1, "L'U'L'U'L'ULUL");
    	mRule.add(temp);
    	
    	//Step 3 [B5,L3]
    	temp = new RuleObjLengKuai(3, 3, 1, "L'U'L'U'L'ULUL");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 3, 0, "UBUBUBU'B'U'B'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 0, -1, "U");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 1, -1, "UU");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 2, -1, "U'");
    	mRule.add(temp);

    	temp = new RuleObjLengKuai(3, 7, 0, "L'U'L'U'L'ULUL");
    	mRule.add(temp);
    }
}
