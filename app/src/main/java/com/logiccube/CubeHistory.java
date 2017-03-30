package com.logiccube;

import java.util.ArrayList;

import android.util.Log;

public class CubeHistory {
	private static final String TAG = "cubeHistory";
	private ArrayList<cubeHistoryItem> mStepList = new ArrayList<cubeHistoryItem>();
	private int mCurPosition = -1;

	public CubeHistory(){
	      /*
	       * do nothing
	       */
	}
	
	/*
	 * 记录步骤
	 */
	public boolean addStep(String action,CubePic cubePicAfter){
		cubeHistoryItem item = new cubeHistoryItem(action, cubePicAfter.getCurPic());
		if(mCurPosition == -1){
			mStepList = new ArrayList<cubeHistoryItem>();
		}else if(mCurPosition>= 0 && mCurPosition < mStepList.size() - 1){
			Log.v(TAG,"mCurPosition:" + mCurPosition +"mStepList.size():"+mStepList.size());
			//mStepList = (ArrayList<cubeHistoryItem>)mStepList.subList(0, mCurPosition);
			ArrayList<cubeHistoryItem> temp = new ArrayList<cubeHistoryItem>();
			for (int i = 0; i <= mCurPosition; i ++){
				temp.add(mStepList.get(i));
			}
			mStepList = temp;
		}
		mStepList.add(item);
		mCurPosition = mStepList.size() - 1;
		Log.v(TAG, "[addStep]mStepList.size():" + mStepList.size());
		return true;
	}
	/*
	 * 回退一步
	 */
	public boolean fallback(Cube cube){
		if(mCurPosition < 0 || mStepList.size() == 0){
			Log.e(TAG, "no history.");
			return false;
		}else if(mCurPosition == 0){
			mCurPosition = -1;
	    	cube.setCurrentPic(cube.getInitPic());
		}else{
			mCurPosition--;
			cube.setCurrentPic(mStepList.get(mCurPosition).getPic());
		}
		Log.v(TAG, "[redo]mCurPosition:" + mCurPosition + ", mStepList.size():" + mStepList.size());
		return true;
	}
	/*
	 * 重做一步
	 */
	public boolean redo(Cube cube){
		if(mCurPosition >= mStepList.size() -1){
			Log.e(TAG, "the last step:" + mCurPosition);
			return false;
		}
		mCurPosition++;
		cube.setCurrentPic(mStepList.get(mCurPosition).getPic());
		Log.v(TAG, "[redo]mCurPosition:" + mCurPosition + ", mStepList.size():" + mStepList.size());
		return true;
	}
	
	public boolean hasLastStep(){
		if(mCurPosition >= 0){
			return true;
		}
		return false;
	}
	
	public boolean hasNextStep() {
		if(mCurPosition < mStepList.size() -1){
			return true;
		}
		return false;
	}
	
    public void clearHistory(Cube cube){
    	Log.d(TAG, "clearHistory...");
    	mStepList = new ArrayList<cubeHistoryItem>();
    	mCurPosition = -1;
    	cube.setCurrentPic(cube.getInitPic());
    }
    
    public String getStep(){
    	String temp = "";
    	for (int i = 0; i <= mCurPosition; i++){
    		temp = temp + mStepList.get(i).getStep();
    	}
    	return temp;
    }
    
    public String getAllHistory(){
    	String temp = "";
    	for (int i = 0; i < mStepList.size(); i++){
    		temp = temp + mStepList.get(i).getStep();
    	}
    	return temp;
    }

	public int getCurPosition() {
		return mCurPosition;
	}
	
	public int getCurStepNum(){
		return (mCurPosition + 1);
	}
	
	private class cubeHistoryItem{
		private String mStep = "";
		private CubePic mPic = null;
		
		public String getStep() {
			return mStep;
		}

		public CubePic getPic() {
			return mPic;
		}

		public cubeHistoryItem(String step, int[][] pic){
			mStep = step;
			mPic = new CubePic(pic);
		}
	}
	
	public String getLatestStep(){
		if(mStepList.size() > 0){
			return mStepList.get(mStepList.size() - 1).getStep();
		}
		return null;
	}
	
	public void formatHistory(){
	   int index = getformatIndex();
		while (index != -1){
			mStepList.remove(index+1);
			if(mCurPosition >= index +1){
				mCurPosition--;
			}
			mStepList.remove(index);
			if(mCurPosition >= index){
				mCurPosition--;
			}
			index = getformatIndex();
		}
		return;
	}
	
	private int getformatIndex(){
		String curStep = "";
	    String nextStep = "";
	    
		for(int i = 0; i < mStepList.size(); i++){
			curStep = mStepList.get(i).getStep();
			if(i+1 < mStepList.size()){
				nextStep = mStepList.get(i+1).getStep();
			}else{
				nextStep = "";
			}
			for(int j = 0; j < CubeUtil.FORMAT_STEP.length; j++){
				if(CubeUtil.FORMAT_STEP[j].equals(curStep + nextStep)){
					if(!"".equals(nextStep)){
						return i;
					}
				}
			}
	    }
		return -1;
	}
}
