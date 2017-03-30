package com.logiccube;

import java.util.HashMap;

import android.util.Log;

public class Cube {
	private static final String TAG = "cube";
	
	private static HashMap<String, Integer> mMianIndex = null;
	private static CubeAction mAction = null;
	

	/*
	 * 每个数字代表一个颜色，此数组记录的魔方当前的状态
	 * 魔方的六个面是有对应关系的，
	 * 已F面为基准，排列的位置为
	 *            1 2 3
	 *            4 5 6 -- U
	 *            7 8 9
	 *      1 2 3 1 2 3 1 2 3 1 2 3
	 * L -- 4 5 6 4 5 6 4 5 6 4 5 6 -- B
	 *      7 8 9 7 8 9 7 8 9 7 8 9
	 *            1 2 3
	 *            4 5 6 -- D
	 *            7 8 9
	 */
	private CubePic mCurrentPic = null;

	/*
	 * 每个数字代表一个颜色，此数组记录的魔方初始的状态
	 */
	private CubePic mInitPic = null;
	
	public boolean isValidCube(){
		if(mInitPic == null){
			return false;
		}
		return true;
	}

	private CubeHistory changeHistory = new CubeHistory();
	
	public String getCurCubeHistory() {
		if(changeHistory == null){
			return null;
		}
		return changeHistory.getStep();
	}
	
	public void formatHistory(){
		if(changeHistory == null){
			return;
		}
		changeHistory.formatHistory();
		return;
	}
	
	public int getCubeCurStepNum(){
		if(changeHistory == null){
			return 0;
		}
		return changeHistory.getCurStepNum();
	}
	
	public boolean hasNexStep(){
		if(changeHistory == null){
			return false;
		}
		return changeHistory.hasNextStep();
	}
	public boolean hasLastStep(){
		if(changeHistory == null){
			return false;
		}
		return changeHistory.hasLastStep();
	}
	public boolean redo(){
		if(changeHistory == null){
			return false;
		}
		return changeHistory.redo(this);
	}
	public boolean fallback(){
		if(changeHistory == null){
			return false;
		}
		return changeHistory.fallback(this);
	}

	/*
	 * 次变量用来对应@currentzPic中数字和颜色的关系
	 */
	private String[] mCurrentColor = {"blue","red","green","orange","yellow","white"};

	/*
	 * String[][] cube_color 
	 * F {9个颜色}
	 * R {9个颜色}
	 * B {9个颜色}
	 * L {9个颜色}
	 * U {9个颜色}
	 * D {9个颜色}
	 */
	public Cube(String[][] cubeColor){
		mMianIndex = new HashMap<String, Integer>();
		mMianIndex.put("F", 0);
		mMianIndex.put("R", 1);
		mMianIndex.put("B", 2);
		mMianIndex.put("L", 3);
		mMianIndex.put("U", 4);
		mMianIndex.put("D", 5);
		
		mAction = new CubeAction();
		
		initCube(cubeColor);
	}
	
	public boolean initCube(String[][] cubeColor) {
		Log.d(TAG,"initCube begin ...");
		/*
		 * 先判断颜色是否正确
		 */
		if(!CubeUtil.isCorrectCubeColor(cubeColor)){
			Log.e(TAG,"isCorrectCubeColor is false!");
			return false;
		}
		//记录魔方的颜色数组
		mCurrentColor = CubeUtil.getCubeColorArray(cubeColor);
		
		//记录魔方的当前状态
		CubePic temp = new CubePic(cubeColor, mCurrentColor);
		
		//判断魔方的数字数组是否正确
		if(!CubeUtil.isCorrectCubeInt(temp.getCurPic())){
			Log.e(TAG,"isCorrectCubeInt is false!");
			return false;
		}
		
		//记录魔方的当前状态
		mCurrentPic = new CubePic(cubeColor, mCurrentColor);
		mInitPic = new CubePic(cubeColor, mCurrentColor);
		
		Log.d(TAG,"initCube is successful.");
		return true;
	}
	

	/*
	 * 旋转，面的赋值
	 * input 魔方的当前状态int数组
	 * mian 需要旋转的面{F,R,B,L,U,D}
	 * clockwise 为true, 则顺时针
	 *           为false,则逆时针
	 * output 魔方面旋转后的状态
	 */
	private boolean rotate(int[][] input, String action, int[][] output){

		/*
		 * 旋转当前面，对应四个侧面的旋转
		 */
		boolean clockwise = true;
		if(action.indexOf("'") != -1){
			clockwise = false;
		}
		String mian = action.replace("'", "");
		
		if(!CubeUtil.isExistInStrArray(mian,CubeUtil.MIAN_KEY)){
			Log.e(TAG,"[rotate]fail!invalid mian. main:"+mian);
			return false;
		}
		CubeActionItem actionItem = mAction.getAction(action);
		if(actionItem == null){
			Log.e(TAG,"[rotate]fail!actionItem is null. action:"+action);
			return false;
		}
		String[] seq = actionItem.getmSeq();
		if(seq.length != 5){
			Log.e(TAG,"[rotate]fail!invalid seq. action:"+action);
			return false;
		}
		for(int i = 1; i < seq.length; i ++){
			int[] paramIndex = actionItem.getmParamIndex().get(seq[i]);
			int[] valueIndex = actionItem.getmValueIndex().get(seq[i - 1]);
			
			int param = mMianIndex.get(seq[i]);
			int value = mMianIndex.get(seq[i - 1]);
			if(paramIndex.length != valueIndex.length){
				Log.e(TAG,"[rotate]fail! paramIndex.length != valueIndex.length. action:"+action);
				return false;
			}
			for (int j = 0; j < paramIndex.length; j++){
				//Log.v(TAG,"output["+param+"]["+paramIndex[j]+"]=input["+value+"]["+valueIndex[j]+"]");
				output[param][paramIndex[j]] = input[value][valueIndex[j]];
			}
		}
		
		/* 顺时针面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
		 * 逆时针面颜色位置变化 0<-2,1<-5,2<-8,3<-1,5<-7,6<-0,7<-3,8<-6
		 */
		
		int[] step = {2, 4, 6};
		int mianIndex = (Integer)mMianIndex.get(mian);
		if(mianIndex > output.length || mianIndex > input.length){
			Log.e(TAG,"[rotate]fail!mianIndex:"+mianIndex);
			return false;
		}
		for(int i = 0; i< 3; i++){
			int index = i + step[i];
			if(clockwise){
				output[mianIndex][index] = input[mianIndex][i];
			}else{
				output[mianIndex][i] = input[mianIndex][index];
			}
		}
		for(int i = 6; i< 9; i++){
			int index = i - step[step.length - (i - 6) - 1];
			if(clockwise){
				output[mianIndex][index] = input[mianIndex][i];
			}else{
				output[mianIndex][i] = input[mianIndex][index];
			}
			
		}
		if(clockwise){
			output[mianIndex][1] = input[mianIndex][3];
			output[mianIndex][7] = input[mianIndex][5];
		}else{
			output[mianIndex][3] = input[mianIndex][1];
			output[mianIndex][5] = input[mianIndex][7];
		}
		
		return true;
	}
	
	public boolean doAction(String actionPatch){
		Log.d(TAG,"[doAction] actionPatch:"+actionPatch);
		
		if(!CubeUtil.isValidPatchStep(actionPatch)){
			Log.e(TAG,"[doAction] InValid actionPatch."+actionPatch);
			return false;
		}
		
		int patchStep = 0;
		char[] ch = actionPatch.toCharArray();
		boolean error = false;
		String curStr = "";
		String nextStr = "";
		for (int i = 0; i < ch.length; i++){
			curStr = ch[i] + "";
			if(i+1 < ch.length){
				nextStr = ch[i+1] + "";
			}else{
				nextStr = "";
			}
			if(!"'".equals(curStr) && !CubeUtil.isExistInStrArray(curStr, CubeUtil.MIAN_KEY)){
				Log.e(TAG,"[doAction]fail1! ch["+i+"]:" + curStr);
				error = true;
				break;
			}

			if("'".equals(nextStr)){
				
				if("'".equals(curStr)){
					Log.e(TAG,"[doAction]fail2! invalid format ch["+i+"]:"
							+ curStr +", ch["+(i+1)+"]:"+nextStr);
					error = true;
					break;
				}
				
				if(!doSingleAction(curStr + nextStr)){
					Log.e(TAG,"[doAction]doSingleAction fail3!action:" + curStr + nextStr);
					error = true;
					break;
				}
				patchStep++;
				i++;
			}else{
				if(!doSingleAction(curStr)){
					Log.e(TAG,"[doAction]doSingleAction fail4!action:" + curStr);
					error = true;
					break;
				}
				patchStep++;
			}
		}
		
		if(error){
			while(patchStep > 0){
				Log.e(TAG,"[doAction]fail! forwardback:" + changeHistory.getLatestStep());
				changeHistory.fallback(this);
				patchStep--;
			}
			return false;
		}
		return true;
	}
	
	//=======================魔方基本旋转动作=========================
	/*
	 * 以下为魔方的12个基本旋转动作
	 */
	
	private boolean doSingleAction(String action){
		int[][] curPic = mCurrentPic.getCurPic();
		int[][] temp = new int[CubeUtil.MIAN_NUM][CubeUtil.COLOR_NUM_IN_MIAN];

		CubeUtil.copyCubeInt(curPic, temp);
		if(!CubeUtil.isExistInStrArray(action, CubeUtil.ALLOW_ACTION)){
			Log.e(TAG,"[doSingleAction]invalid action:"+action);
			return false;
		}

		if(!rotate(curPic,action,temp)){
			Log.e(TAG,"[doSingleAction]fail!");
			return false;
		}

		/*
		 * 记录当前状态和历史
		 */
		mCurrentPic = new CubePic(temp);
		changeHistory.addStep(action, mCurrentPic);
		return true;
	}

	//=======================魔方基本旋转动作=========================

	@Override
	public String toString() {
		String output = "{";
		int[][] pic = mCurrentPic.getCurPic();
		for (int i = 0; i < pic.length; i++){
			output = output + "{";
			for(int j = 0; j < pic[i].length; j++){
				output = output + getColor(pic[i][j]);
				if(j < pic[i].length - 1){
					output = output + ",";
				}
			}
			output = output + "}";
			if(i < pic.length - 1){
				output = output + ",\n";
			}
		}
		output = output + "}";
		return output;
	}
	
	public String getColor(int index){
		if(index < 0 || index >= mCurrentColor.length){
			Log.e(TAG, "[getColor]invalid index:" + index);
			return CubeUtil.INVALID_STRING;
		}
		return mCurrentColor[index];
	}
	
	public CubePic getCurrentPic() {
		return mCurrentPic;
	}

	public void setCurrentPic(CubePic mCurrentPic) {
		this.mCurrentPic = mCurrentPic;
	}
	
	public CubePic getInitPic() {
		return mInitPic;
	}

	public void setInitPic(CubePic mInitPic) {
		this.mInitPic = mInitPic;
	}
	
	public String[][] getColorTwoArray(){
		if(mCurrentPic == null){
			Log.e(TAG,"[getColorTwoArray]mCurrentPic is null.");
			return null;
		}
		String[][] result = new String[6][9];
		try{
			for(int i = 0; i < mCurrentPic.getCurPic().length; i++){
				for(int j = 0; j < mCurrentPic.getCurPic()[i].length; j++){
					result[i][j] = mCurrentColor[mCurrentPic.getCurPic()[i][j]] + "";
				}
			}
		}catch(Exception e){
			Log.e(TAG,"[getColorTwoArray]Error");
			e.printStackTrace();
			return null;
		}
		return result;
	}
}
