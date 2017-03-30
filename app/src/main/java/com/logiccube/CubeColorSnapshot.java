package com.logiccube;

import android.util.Log;

public class CubeColorSnapshot {
	private static final String TAG = "CubeColorSnapshot";
	private String[][] mCubeStrTwoArray = new String[CubeUtil.MIAN_NUM][CubeUtil.COLOR_NUM_IN_MIAN];
	private String[] mCubeStrOneArray = new String[CubeUtil.MIAN_NUM
			* CubeUtil.COLOR_NUM_IN_MIAN];

	public CubeColorSnapshot(String[][] cubeStrTwoArray) {
		if(!CubeUtil.copyCubeColor(cubeStrTwoArray, mCubeStrTwoArray)){
			Log.e(TAG,"CubeColorSnapshot(String[][] cubeStrTwoArray) failed!");
			return;
		}
		for(int i = 0; i < mCubeStrTwoArray.length; i++){
			for (int j = 0; j < mCubeStrTwoArray[i].length; j++){
				mCubeStrOneArray[(i * CubeUtil.COLOR_NUM_IN_MIAN)+j] = mCubeStrTwoArray[i][j];
			}
		}
	}

	public CubeColorSnapshot(String[] cubeStrOneArray) {
		if(!CubeUtil.copyCubeColorOneArray(cubeStrOneArray, mCubeStrOneArray)){
			Log.e(TAG,"CubeColorSnapshot(String[] cubeStrOneArray) failed!");
			return;
		}
		for(int i = 0; i < mCubeStrTwoArray.length; i++){
			for (int j = 0; j < mCubeStrTwoArray[i].length; j++){
				mCubeStrTwoArray[i][j] = mCubeStrOneArray[(i * CubeUtil.COLOR_NUM_IN_MIAN)+j];
			}
		}
	}

	public String[][] getmCubeStrTwoArray() {
		return mCubeStrTwoArray;
	}

	public String[] getmCubeStrOneArray() {
		return mCubeStrOneArray;
	}
	
	
}
