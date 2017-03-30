package com.recoverCross;

import java.util.ArrayList;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;
import com.logiccube.JiaoKuai;

import android.util.Log;


public class RuleJiaoKuaiRightU {
	private static final String TAG = "RuleJiaoKuaiRightU";
	/**
	 * 3  2 -> 0  3
	 * 0  1    2  1
	 * 
	 */
	private static final String GONG_SHI_601 = "LLBBL'F'LBBL'FL'";
	/**
	 * 3  2 -> 2  1
	 * 0  1    0  3
	 * 
	 */
	private static final String GONG_SHI_602 = "RRBBRFR'BBRF'R";
	
	private ArrayList<Template> mTemplateList = new ArrayList<Template>();
	
	private static JiaoKuai[] mRightJiaoKuaiArray = new JiaoKuai[4];
	
	private JiaoKuai[] mJiaoKuaiArray = null;
	
	private Cube mTestCube = null;
	
	private int mCurStep = -1;
	
	
	public RuleJiaoKuaiRightU(){

		//mTestCube = new Cube(cube.getColorTwoArray());
		//Log.d(TAG,"Test Cube:"+mTestCube.toString());
		
		int[] color0 = {0,3,4};
		int[] pos0 = {0,3,4};
		JiaoKuai jiaoKuai0 = new JiaoKuai(color0,pos0);
		mRightJiaoKuaiArray[0] = jiaoKuai0;
		
		int[] color1 = {0,1,4};
		int[] pos1 = {0,1,4};
		JiaoKuai jiaoKuai1 = new JiaoKuai(color1,pos1);
		mRightJiaoKuaiArray[1] = jiaoKuai1;
		
		int[] color2 = {1,2,4};
		int[] pos2 = {1,2,4};
		JiaoKuai jiaoKuai2 = new JiaoKuai(color2,pos2);
		mRightJiaoKuaiArray[2] = jiaoKuai2;
		
		int[] color3 = {2,3,4};
		int[] pos3 = {2,3,4};
		JiaoKuai jiaoKuai3 = new JiaoKuai(color3, pos3);
		mRightJiaoKuaiArray[3] = jiaoKuai3;
		
		initRule();
	}
	
	private void updateInfo(){
    	mJiaoKuaiArray = CubeUtil.getCurrJiaoKuaiArray(mTestCube.getCurrentPic().getCurPic());
    	
		if(isJiaoKuaiRight(0, mTestCube.getCurrentPic().getCurPic())){
			mCurStep = 1;
		}else{
			mCurStep = 0;
		}
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
	
	public String getAction(Cube cube){
		
		mTestCube = new Cube(cube.getColorTwoArray());
		
		updateInfo();
		
		if(!RecoverCube.isRightU(mTestCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isRightU failed.");
			return null;
		}
		
		if(RecoverCube.isJiaoKuaiRightU(mTestCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isJiaoKuaiRightU OK.");
			return null;
		}
		
		String action = null;
		
		int step = 0;
		while(mCurStep == 0){
			if(step > 3){
				Log.e(TAG,"[getAction][mCurStep "+mCurStep+"]step over:"+step);
				return null;
			}
			int index = findNeededJiaoKuaiIndex(0);
			switch(index){
			case 1:
				action = "U";
				break;
			case 2:
				action = "UU";
				break;
			case 3:
				action = "U'";
				break;
			default:
				return null;
			}
			
			mTestCube.doAction(action);
			updateInfo();
			step++;
		}
		int[] curSeq = getCurSeq();
		for(int i = 0; i < mTemplateList.size(); i++){
			String temp = mTemplateList.get(i).getAction(curSeq);
			if(temp != null){
				Log.e(TAG, "test cube do action:"+temp);
				mTestCube.doAction(temp);
				break;
			}
		}
		
		if(RecoverCube.isJiaoKuaiRightU(mTestCube.getCurrentPic().getCurPic())){
			action = mTestCube.getCurCubeHistory();
			while(action.indexOf("UU'") != -1 || (action.indexOf("U'U") != -1 && action.indexOf("U'U") != action.indexOf("U'U'"))){
				action = action.replaceAll("UU'", "");
				if(action.indexOf("U'U") != action.indexOf("U'U'")){
				    action = action.replaceAll("U'U", "");
				}
			}
			return action;
		}else{
			Log.e(TAG,"can not find action:"+mTestCube.getCurCubeHistory());
			Log.e(TAG,"current seq:{"+curSeq[0]+","+curSeq[1]+","+curSeq[2]+","+curSeq[3]+"}");
			Log.e(TAG,"Test Cube:"+mTestCube.toString());
			return null;
		}
	}
	
	private int findNeededJiaoKuaiIndex(int index){
		for(int i = 0; i < mJiaoKuaiArray.length; i++){
			if(mRightJiaoKuaiArray[index].isSame(mJiaoKuaiArray[i])){
				return i;
			}
		}
		Log.e(TAG, "can not found jiaokuai step:"+mCurStep);
		Log.e(TAG, "cube:"+ mTestCube.getCurrentPic().toString());
		return -1;
	}
	
	private int[] getCurSeq(){
		int[] seq = {-1,-1,-1,-1};
		for(int i = 0; i < seq.length; i++){
			int temp = getJiaoKuaiSeq(mJiaoKuaiArray[i]);
			if(temp < 0 || temp > 3){
				Log.e(TAG, "[getCurSeq]Error:("+i+"):"+mJiaoKuaiArray[i].toString());
				return null;
			}
			seq[i] = temp;
		}
		
		return seq;
	}
	
	private int getJiaoKuaiSeq(JiaoKuai jiaoKuai){
		for (int j = 0; j < mRightJiaoKuaiArray.length; j++){
			if(jiaoKuai.isSame(mRightJiaoKuaiArray[j])){
				return j;
			}
		}
		return -1;
	}
	
	private void initRule(){
		
		Template temp = null;
		int[] seq = null;
		
		seq = new int[]{0,1,3,2};
		temp = new Template(seq,GONG_SHI_602+"UU" +GONG_SHI_602 + "U'");
		mTemplateList.add(temp);
		
		seq = new int[]{0,2,1,3};
		temp = new Template(seq,GONG_SHI_601 + "U'");
		mTemplateList.add(temp);
		
		seq = new int[]{0,2,3,1};
		temp = new Template(seq,GONG_SHI_602);
		mTemplateList.add(temp);
		
		seq = new int[]{0,3,1,2};
		temp = new Template(seq,"U'"+GONG_SHI_601+"U");
		mTemplateList.add(temp);
		
		seq = new int[]{0,3,2,1};
		temp = new Template(seq,"U"+GONG_SHI_601 + "U"+GONG_SHI_601 + "U");
		mTemplateList.add(temp);
	}
	

	
	private class Template{
		private int[] mTemplateJiaoKuaiSeq = {-1,-1,-1,-1};
		private String mAction = "";
		
		public Template(int[] seq, String action){
			for(int i = 0; i < mTemplateJiaoKuaiSeq.length; i++){
				mTemplateJiaoKuaiSeq[i] = seq[i];
			}
			mAction = action;
		}
		
		private boolean isMatch(int[] seq){
			for(int i = 0; i < mTemplateJiaoKuaiSeq.length; i++){
				if(mTemplateJiaoKuaiSeq[i] != seq[i]){
					return false;
				}
			}
			return true;
		}
		
		public String getAction(int[] seq){
			if(isMatch(seq)){
				return mAction;
			}
			return null;
		}
	}
}
