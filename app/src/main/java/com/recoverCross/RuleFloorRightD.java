package com.recoverCross;

import java.util.ArrayList;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;
import com.logiccube.JiaoKuai;

import android.util.Log;


public class RuleFloorRightD {
    private static final String TAG = "RuleFloorRightD";
	
	/*
	 * 记录当前阶段需要还原的位置
	 * 0 [L,D,F]
	 * 1 [F,D,R]
	 * 2 [R,D,B]
	 * 3 [B,D,L]
	 */
	private int mCurStep = 0;
	
	private Cube mCube = null;
	
	private static ArrayList<RuleObjJiaoKuai> mRule = new ArrayList<RuleObjJiaoKuai>();
	
	/*
     * 角块的数组 包含位置和颜色信息
     * [L2,U6,F0],[F2,U8,R0]，[R2,U2,B0],[B2,U0,L0]
     * [L8,D0,F6],[F8,D2,R6]，[R8,D6,B6],[B8,D8,L6]
	 */
	private JiaoKuai[] mJiaoKuaiArray = null;
	
	public ArrayList<RuleObjJiaoKuai> ruleObjList = new ArrayList<RuleObjJiaoKuai>();
	
	public RuleFloorRightD(Cube cube){
		mCube = cube;
		initRule();
		updateInfo();
	}
	
	private void updateInfo(){
		mJiaoKuaiArray = CubeUtil.getCurrJiaoKuaiArray(mCube.getCurrentPic().getCurPic());
		calCurStep();
	}
	
	private void calCurStep(){
		if(mJiaoKuaiArray == null){
			mJiaoKuaiArray = CubeUtil.getCurrJiaoKuaiArray(mCube.getCurrentPic().getCurPic());
		}
		if(isJiaoKuaiRight(4, mCube.getCurrentPic().getCurPic())){
			mCurStep = 1;
		}else{
			mCurStep = 0;
		}
		
		if(isJiaoKuaiRight(4, mCube.getCurrentPic().getCurPic()) 
				&& isJiaoKuaiRight(5, mCube.getCurrentPic().getCurPic())){
			mCurStep = 2;
		}

		if(isJiaoKuaiRight(4, mCube.getCurrentPic().getCurPic()) 
				&& isJiaoKuaiRight(5, mCube.getCurrentPic().getCurPic())
				&& isJiaoKuaiRight(6, mCube.getCurrentPic().getCurPic())){
			mCurStep = 3;
		}
		
		if(isJiaoKuaiRight(4, mCube.getCurrentPic().getCurPic()) 
				&& isJiaoKuaiRight(5, mCube.getCurrentPic().getCurPic())
				&& isJiaoKuaiRight(6, mCube.getCurrentPic().getCurPic())
				&& isJiaoKuaiRight(7, mCube.getCurrentPic().getCurPic())){
			mCurStep = 4;
		}
	}
	
	private int[] getNeedJiaoKuaiColor(){
		if(mCurStep < 0){
			calCurStep();
		}
		int[] color = new int[3];
		int[][] curPic = mCube.getCurrentPic().getCurPic();
		switch(mCurStep){
			case 0:
				//[L,D,F]
				color[0] = curPic[3][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[2] = curPic[0][CubeUtil.CENTER_KUAI_INDEX];
				break;
			case 1:
				//[F,D,R]
				color[0] = curPic[0][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[2] = curPic[1][CubeUtil.CENTER_KUAI_INDEX];
				break;
			case 2:
				//[R,D,B]
				color[0] = curPic[1][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[2] = curPic[2][CubeUtil.CENTER_KUAI_INDEX];
				break;
			case 3:
				//[B,D,L]
				color[0] = curPic[2][CubeUtil.CENTER_KUAI_INDEX];
				color[1] = curPic[5][CubeUtil.CENTER_KUAI_INDEX];
				color[2] = curPic[3][CubeUtil.CENTER_KUAI_INDEX];
				break;
			default:
				color[0] = -1;
				color[1] = -1;
				color[2] = -1;
				Log.e(TAG, "step is error:"+mCurStep);
				break;
		}
		return color;
	}
	
	private int[] getNeedJiaoKuaiPos(){
		if(mCurStep < 0){
			calCurStep();
		}
		int[] pos = new int[3];
		switch(mCurStep){
			case 0:
				//[L,D,F]
				pos[0] = 3;
				pos[1] = 5;
				pos[2] = 0;
				break;
			case 1:
				//[F,D,R]
				pos[0] = 0;
				pos[1] = 5;
				pos[2] = 1;
				break;
			case 2:
				//[R,D,B]
				pos[0] = 1;
				pos[1] = 5;
				pos[2] = 2;
				break;
			case 3:
				//[B,D,L]
				pos[0] = 2;
				pos[1] = 5;
				pos[2] = 3;
				break;
			default:
				pos[0] = -1;
				pos[1] = -1;
				pos[2] = -1;
				Log.e(TAG, "step is error:"+mCurStep);
				break;
		}
		return pos;
	}
	
	private int getRightIndex(){
		if(mCurStep < 0){
			calCurStep();
		}
		int index = -1;
		switch(mCurStep){
			case 0:
				//[L,D,F]
				index = 4;
				break;
			case 1:
				//[F,D,R]
				index = 5;
				break;
			case 2:
				//[R,D,B]
				index = 6;
				break;
			case 3:
				//[B,D,L]
				index = 7;
				break;
			default:
				index = -1;
				Log.e(TAG, "step is error:"+mCurStep);
		}
		return index;
	}
	
	private int findNeededJiaoKuaiIndex(){
		JiaoKuai needed = new JiaoKuai(getNeedJiaoKuaiColor());
		for(int i = 0; i < mJiaoKuaiArray.length; i++){
			if(needed.isSame(mJiaoKuaiArray[i])){
				return i;
			}
		}
		Log.e(TAG, "can not found lengkuai step:"+mCurStep);
		Log.e(TAG, "cube:"+ mCube.getCurrentPic().toString());
		return -1;
	}
	
	public String getAction(){
		updateInfo();
		if(mCurStep > 3){
			Log.e(TAG, "nothing need to do step:"+mCurStep);
			return null;
		}
		if(!RecoverCube.isCrossRightD(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isCrossRightD failed step:"+mCurStep);
			return null;
		}
		Log.d(TAG, "[getAction]mCurStep:"+mCurStep);
		int index = findNeededJiaoKuaiIndex();
		for(int i = 0; i < mRule.size(); i++){
			RuleObjJiaoKuai temp = mRule.get(i);
			if(temp.isSameStepAndIndex(mCurStep, index)){
				if(temp.getmNeededJiaoKuaiPosStatus() == -1){
					return temp.getmAction();
				}
				if(temp.isSamePosStatus(getNeededJiaoKuaiPosStatus(index))){
					return temp.getmAction();
				}
			}
		}
		Log.e(TAG, "getAction fail step:"+mCurStep);
		Log.e(TAG, "cube:"+ mCube.getCurrentPic().toString());
		return null;
	}
	/**
	 * 目标棱块侧面的颜色已经与该侧面的中心块一致
	 * 1 需要逆时针旋转
	 * 0 需要顺时针旋转
	 * -1 不在正确位置上
	 */
	private int getNeededJiaoKuaiPosStatus(int index){
		int[] color = getNeedJiaoKuaiColor();
		int[] pos = getNeedJiaoKuaiPos();
		JiaoKuai temp = new JiaoKuai(color, pos);
		int[][] curPic = mCube.getCurrentPic().getCurPic();
		if(temp.isSame(mJiaoKuaiArray[index])){
			//不在正确的位置上
			if(index != getRightIndex()){
				return -1;
			}
			//顺时针旋转
			if(mJiaoKuaiArray[index].isPosColor(mCurStep, curPic[5][CubeUtil.CENTER_KUAI_INDEX])){
				return 0;
			}
			//需要逆时针旋转
			return 1;
		}
		Log.e(TAG, "[getNeededJiaoKuaiPosStatus] index is error:"+index);
		Log.e(TAG, "cube:"+ mCube.getCurrentPic().toString());
		return -1;
	}
	
	private boolean isJiaoKuaiRight(int JiaoKuaiIndex, int[][] curPic){
    	if(mJiaoKuaiArray == null){
    		mJiaoKuaiArray = CubeUtil.getCurrJiaoKuaiArray(curPic);
		}
    	JiaoKuai curJiaoKuai = mJiaoKuaiArray[JiaoKuaiIndex];
    	for(int i = 0; i < curJiaoKuai.getmPos().length; i++){
    		int pos = curJiaoKuai.getmPos()[i];
    		int color = curPic[pos][CubeUtil.CENTER_KUAI_INDEX];
    		if(!curJiaoKuai.isPosColor(pos,color)){
    			return false;
    		}
    	}
    	return true;
    }
	
	private static void initRule(){
    	RuleObjJiaoKuai temp = null;
    	//在正确位置上做旋转
    	temp = new RuleObjJiaoKuai(0, 4, 0, "L'ULU'L'UL");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(0, 4, 1, "FU'F'UFU'F'");
    	mRule.add(temp);
    	
    	//不在正确位置上，需要转到正确位置上
    	temp = new RuleObjJiaoKuai(0, 0, -1, "UFU'F'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(0, 1, -1, "UUFU'F'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(0, 2, -1, "U'UFU'F'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(0, 3, -1, "FU'F'");
    	mRule.add(temp);
    	
    	temp = new RuleObjJiaoKuai(0, 5, -1, "RUUR'FU'F'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(0, 6, -1, "R'U'RFU'F'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(0, 7, -1, "LUL'U'FU'F'");
    	mRule.add(temp);
    	
    	//在正确位置上做旋转
    	temp = new RuleObjJiaoKuai(1, 5, 0, "F'UFU'F'UF");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(1, 5, 1, "RU'R'URU'R'");
    	mRule.add(temp);
    	
    	//不在正确位置上，需要转到正确位置上
    	temp = new RuleObjJiaoKuai(1, 0, -1, "RU'R'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(1, 1, -1, "URU'R'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(1, 2, -1, "UURU'R'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(1, 3, -1, "U'RU'R'");
    	mRule.add(temp);
    	
    	temp = new RuleObjJiaoKuai(1, 6, -1, "R'UURRU'R'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(1, 7, -1, "LUL'F'UF");
    	mRule.add(temp);
    	
    	//在正确位置上做旋转
    	temp = new RuleObjJiaoKuai(2, 6, 0, "R'URU'R'UR");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(2, 6, 1, "BU'B'UBU'B'");
    	mRule.add(temp);
    	
    	//不在正确位置上，需要转到正确位置上
    	temp = new RuleObjJiaoKuai(2, 0, -1, "U'BU'B'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(2, 1, -1, "BU'B'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(2, 2, -1, "UBU'B'");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(2, 3, -1, "UUBU'B'");
    	mRule.add(temp);
    	
    	temp = new RuleObjJiaoKuai(2, 7, -1, "B'UUBBU'B'");
    	mRule.add(temp);
    	
    	//在正确位置上做旋转
    	temp = new RuleObjJiaoKuai(3, 7, 0, "B'UBU'B'UB");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(3, 7, 1, "LU'L'ULU'L'");
    	mRule.add(temp);
    	
    	//不在正确位置上，需要转到正确位置上
    	temp = new RuleObjJiaoKuai(3, 0, -1, "B'UB");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(3, 1, -1, "UB'UB");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(3, 2, -1, "UUB'UB");
    	mRule.add(temp);
    	temp = new RuleObjJiaoKuai(3, 3, -1, "U'B'UB");
    	mRule.add(temp);
	}
}
