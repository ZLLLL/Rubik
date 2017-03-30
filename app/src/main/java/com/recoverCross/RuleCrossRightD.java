package com.recoverCross;

import java.util.ArrayList;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;
import com.logiccube.LengKuai;

import android.util.Log;


public class RuleCrossRightD {
	private static final String TAG = "RuleCrossRightD";
	
	/*
	 * 记录当前阶段需要还原的位置
	 * 0 [F,D]
	 * 1 [R,D]
	 * 2 [B,D]
	 * 3 [L,D]
	 */
	private int mCurStep = 0;
	
	private Cube mCube = null;
	
	private static ArrayList<RuleObjLengKuai> mRule = new ArrayList<RuleObjLengKuai>();
	
	/*
	 * 棱块的数组 包含位置和颜色信息
	 * 数组的位置信息
     * [F1,U7],[R1,U5],[B1,U1],[L1,U3]
	 * [L5,F3],[F5,R3],[R5,B3],[B5,L3]
	 * [F7,D1],[R7,D5],[B7,D7],[L7,D3]
	 */
	private LengKuai[] mLengKuaiArray = null;
	
	public RuleCrossRightD(Cube cube){
		mCube = cube;
		initRule();
		updateInfo();
	}
	
	private void updateInfo(){
		mLengKuaiArray = CubeUtil.getCurrLengKuaiArray(mCube.getCurrentPic().getCurPic());
		calCurStep();
	}
	
	private void calCurStep(){
		if(mLengKuaiArray == null){
			mLengKuaiArray = CubeUtil.getCurrLengKuaiArray(mCube.getCurrentPic().getCurPic());
		}
		if(isLengKuaiRight(8, mCube.getCurrentPic().getCurPic())){
			mCurStep = 1;
		}else{
			mCurStep = 0;
		}
		
		if(isLengKuaiRight(8, mCube.getCurrentPic().getCurPic()) 
				&& isLengKuaiRight(9, mCube.getCurrentPic().getCurPic())){
			mCurStep = 2;
		}
		
		if(isLengKuaiRight(8, mCube.getCurrentPic().getCurPic()) 
				&& isLengKuaiRight(9, mCube.getCurrentPic().getCurPic()) 
				&& isLengKuaiRight(10, mCube.getCurrentPic().getCurPic())){
			mCurStep = 3;
		}
		
		if(isLengKuaiRight(8, mCube.getCurrentPic().getCurPic()) 
				&& isLengKuaiRight(9, mCube.getCurrentPic().getCurPic()) 
				&& isLengKuaiRight(10, mCube.getCurrentPic().getCurPic()) 
				&& isLengKuaiRight(11, mCube.getCurrentPic().getCurPic())){
			mCurStep = 4;
		}
	}
	
	private int findNeededLengKuaiIndex(){
		if(mCurStep < 0){
			calCurStep();
		}
		int[] color = new int[2];
		int[][] curPic = mCube.getCurrentPic().getCurPic();
		switch(mCurStep){
			case 0:
				color[0] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[0][CubeUtil.CENTER_KUAI_INDEX];
				break;
			case 1:
				color[0] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[1][CubeUtil.CENTER_KUAI_INDEX];
				break;
			case 2:
				color[0] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[2][CubeUtil.CENTER_KUAI_INDEX];
				break;
			case 3:
				color[0] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[3][CubeUtil.CENTER_KUAI_INDEX];
				break;
			default:
				Log.e(TAG, "step is error:"+mCurStep);
				return -1;
		}
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
		if(mLengKuaiArray[index].isColorExist(curPic[mCurStep][CubeUtil.CENTER_KUAI_INDEX])
			&& mLengKuaiArray[index].isColorExist(curPic[5][CubeUtil.CENTER_KUAI_INDEX])){
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
		if(mCurStep > 3){
			Log.e(TAG, "nothing need to do step:"+mCurStep);
			return null;
		}
		Log.d(TAG, "[getAction]mCurStep:"+mCurStep);
		int index = findNeededLengKuaiIndex();
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
    
    private static void initRule(){
    	RuleObjLengKuai temp = null;
    	//Step 0 [F,D]
    	temp = new RuleObjLengKuai(0, 0, 1, "FF");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 5, 1, "F");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 4, 1, "F'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(0, 0, 0, "U'R'FR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 4, 0, "FU'R'FR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 5, 0, "F'U'R'FR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 8, 0, "FFU'R'FR");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(0, 1, -1, "U");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 2, -1, "UU");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 3, -1, "U'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(0, 6, -1, "BUUB'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 7, -1, "B'UUB");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(0, 9, -1, "RRURR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 10, -1, "BBU'BB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(0, 11, -1, "LLU'LL");
    	mRule.add(temp);
    	
    	//Step 1 [R,D]
    	temp = new RuleObjLengKuai(1, 1, 1, "RR");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 5, 1, "R");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 6, 1, "R'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(1, 1, 0, "U'B'RB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 5, 0, "RU'B'RB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 6, 0, "R'U'B'RB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 9, 0, "RRU'B'RB");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(1, 0, -1, "U'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 2, -1, "U");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 3, -1, "UU");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(1, 7, -1, "LUUL'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 4, -1, "L'UUL");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 10, -1, "BBUBB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(1, 11, -1, "LLUULL");
    	mRule.add(temp);
    	
    	//Step 2 [B,D]
    	temp = new RuleObjLengKuai(2, 2, 1, "BB");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 6, 1, "B'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 7, 1, "B");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(2, 2, 0, "URB'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 6, 0, "BURB'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 7, 0, "B'URB'R'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 10, 0, "BBURB'R'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(2, 0, -1, "UU");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 1, -1, "U'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 3, -1, "U");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(2, 4, -1, "L'UL");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(2, 5, -1, "RU'R'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(2, 11, -1, "LLULL");
    	mRule.add(temp);
    	
    	//Step 3 [L,D]
    	temp = new RuleObjLengKuai(3, 3, 1, "LL");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 4, 1, "L");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 7, 1, "L'");
    	mRule.add(temp);
    	
    	
    	temp = new RuleObjLengKuai(3, 3, 0, "UBL'B'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 4, 0, "L'UBL'B'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 7, 0, "LUBL'B'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(3, 11, 0, "LLUBL'B'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(3, 0, -1, "U");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 1, -1, "UU");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 2, -1, "U'");
    	mRule.add(temp);
    	
    	temp = new RuleObjLengKuai(3, 5, -1, "RUUR'");
    	mRule.add(temp);
    	temp = new RuleObjLengKuai(3, 6, -1, "BU'B'");
    	mRule.add(temp);
    }
}
