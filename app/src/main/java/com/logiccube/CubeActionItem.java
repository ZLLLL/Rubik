package com.logiccube;

import java.util.HashMap;

import android.util.Log;

public class CubeActionItem {
    private static final String TAG = "cubeActionItem";
	private String mAction = null;
    private String[] mSeq = null;
    private HashMap<String, int[]> mParamIndex = null;
    private HashMap<String, int[]> mValueIndex = null;
    
    public CubeActionItem(String action, String seq[],
    	HashMap<String, int[]> paramIndex,HashMap<String, int[]> valueIndex){
    	setmAction(action);
    	setmSeq(seq);
    	setmParamIndex(paramIndex);
    	setmValueIndex(valueIndex);
    }
    
	public String getmAction() {
		return mAction;
	}
	public void setmAction(String mAction) {
		if(!CubeUtil.isExistInStrArray(mAction, CubeUtil.ALLOW_ACTION)){
    		Log.e(TAG, "invalid action:"+mAction);
    		return;
    	}
		this.mAction = mAction;
	}
	public String[] getmSeq() {
		return mSeq;
	}
	public void setmSeq(String[] mSeq) {
		this.mSeq = mSeq;
	}
	public HashMap<String, int[]> getmParamIndex() {
		return mParamIndex;
	}
	public void setmParamIndex(HashMap<String, int[]> mParamIndex) {
		this.mParamIndex = mParamIndex;
	}
	public HashMap<String, int[]> getmValueIndex() {
		return mValueIndex;
	}
	public void setmValueIndex(HashMap<String, int[]> mValueIndex) {
		this.mValueIndex = mValueIndex;
	}
    
    
}
