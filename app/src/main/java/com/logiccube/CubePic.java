package com.logiccube;

import android.util.Log;

public class CubePic {
	private static final String TAG = "cubePic";
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
	public static final int[][] mInitalPic = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // R
			{ 2, 2, 2, 2, 2, 2, 2, 2, 2 }, // B
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3 }, // L
			{ 4, 4, 4, 4, 4, 4, 4, 4, 4 }, // U
			{ 5, 5, 5, 5, 5, 5, 5, 5, 5 } // D
	};
	
	private int[][] mCurPic = {
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1 }, // F
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1 }, // R
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1 }, // B
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1 }, // L
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1 }, // U
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1 } // D
	};
	
	public int[][] getCurPic() {
		return mCurPic;
	}
	
	/*
	 *执行这个函数之前需要校验魔方的颜色是否正确 
	 *需要先获取魔方的颜色数组信息
	 */
	public CubePic(String[][] cubeColor, String[] cubeColorArray){
		for (int i = 0; i < mCurPic.length; i++){
			for(int j = 0; j < mCurPic[i].length; j++){
				for (int k = 0; k < cubeColorArray.length; k++ ){
					if(cubeColor[i][j].equals(cubeColorArray[k])){
						mCurPic[i][j] = k;
					}
				}// k
				if(mCurPic[i][j] == -1){
					Log.e(TAG, "can not found the color["+i+"]["+j+"]:"+cubeColor[i][j]);
					return;
				}
			}// j
		} // i
	}

	public CubePic(int[][] temp){
		//Log.v(TAG, "cubePic(int[][] temp).");
		if(temp == null) {
			Log.e(TAG, "temp is null.");
			return;
		}
		if(temp.length != 6) {
			Log.e(TAG, "temp.length is not 6.");
			return;
		}
		for (int i = 0; i < temp.length; i++){
			if(temp[i] == null) {
				Log.e(TAG, "temp["+i+"] is null.");
				return;
			}
			if(temp[i].length != 9){
				Log.e(TAG, "temp["+i+"].length is not 9.");
				return;
			}
		}
		for (int i = 0; i < mCurPic.length; i++){
			for(int j = 0; j < mCurPic[i].length; j++){
				mCurPic[i][j] = temp[i][j];
			}
		}
	}
	
	public CubePic(){
		Log.e(TAG, "cubePic()");
		for (int i = 0; i < mCurPic.length; i++){
			for(int j = 0; j < mCurPic[i].length; j++){
				mCurPic[i][j] = mInitalPic[i][j];
			}
		}
	}

	@Override
	public String toString() {
		String output = "{";
		for (int i = 0; i < mCurPic.length; i++){
			output = output + "{";
			for(int j = 0; j < mCurPic[i].length; j++){
				output = output + mCurPic[i][j];
				if(j < mCurPic[i].length - 1){
					output = output + ",";
				}
			}
			output = output + "}";
			if(i < mCurPic.length - 1){
				output = output + ",\n";
			}
		}
		output = output + "}";
		return output;
	}
}
