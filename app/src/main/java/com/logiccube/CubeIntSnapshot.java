package com.logiccube;

import android.util.Log;

public class CubeIntSnapshot {
	private static final String TAG = "CubeIntSnapshot";
	private int[][] mCubeIntTwoArray = new int[CubeUtil.MIAN_NUM][CubeUtil.COLOR_NUM_IN_MIAN];
	private int[] mCubeIntOneArray = new int[CubeUtil.MIAN_NUM * CubeUtil.COLOR_NUM_IN_MIAN];
	
	public int[][] getCubeIntTwoArray() {
		return mCubeIntTwoArray;
	}

	public int[] getCubeIntOneArray() {
		return mCubeIntOneArray;
	}

	public CubeIntSnapshot(int[][] cubeIntTwoArray){
		if(!CubeUtil.copyCubeInt(cubeIntTwoArray, mCubeIntTwoArray)){
			Log.e(TAG,"CubeIntSnapshot(int[][] cubeIntTwoArray) failed!");
			return;
		}
		for(int i = 0; i < mCubeIntTwoArray.length; i++){
			for (int j = 0; j < mCubeIntTwoArray[i].length; j++){
				mCubeIntOneArray[(i * CubeUtil.COLOR_NUM_IN_MIAN)+j] = mCubeIntTwoArray[i][j];
			}
		}
	}
	
	public CubeIntSnapshot(int[] cubeIntOneArray){
		if(!CubeUtil.copyCubeIntOneArray(cubeIntOneArray, mCubeIntOneArray)){
			Log.e(TAG,"CubeIntSnapshot(int[] cubeIntOneArray) failed!");
			return;
		}
		
		for(int i = 0; i < mCubeIntTwoArray.length; i++){
			for (int j = 0; j < mCubeIntTwoArray[i].length; j++){
				mCubeIntTwoArray[i][j] = mCubeIntOneArray[(i * CubeUtil.COLOR_NUM_IN_MIAN)+j];
			}
		}
	}
}
