package com.recoverCross;

import com.logiccube.CubeUtil;

import android.util.Log;


/**
 * 十字归棱法
 * 
 * @author user
 *
 */
public class RecoverCube {
	private static final String TAG = "recoverCube";

	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static boolean isMatchTemplate(int[][] template, int[][] curPic){
		for(int i = 0; i < template.length; i++){
			for (int j = 0; j < template[i].length; j++){
				if(template[i][j] == 1){
					if(curPic[i][j] != curPic[i][CubeUtil.CENTER_KUAI_INDEX]){
						return false;
					}
				}
			}// j
		}// i
		return true;
	}
	
	/*
	 * 这个是第一个目标
	 * 判断底层十字是否还原成功
	 */
	public static boolean isCrossRightD(int[][] curPic){
		if(isMatchTemplate(CROSS_RIGHT_D, curPic)){
			Log.d(TAG,"isCrossrightD is true!");
			return true;
		}
		Log.d(TAG,"isCrossrightD is false!");
		return false;
	}
	
	/*
	 * 第二个目标，
	 * 判断第一层是否还原成功
	 */
	public static boolean isFloorRightD(int[][] curPic){
		if(isMatchTemplate(FLOOR_RIGHT_D, curPic)){
			Log.d(TAG,"isFloorRightD is true!");
			return true;
		}
		Log.d(TAG,"isFloorRightD is false!");
		return false;
	}
	
	/*
	 * 第三个目标
	 * 判断第二层是否还原成功
	 */
	public static boolean isFloorRightD2(int[][] curPic){
		if(isMatchTemplate(FLOOR_RIGHT_D2, curPic)){
			Log.d(TAG,"isFloorRightD2 is true!");
			return true;
		}
		Log.d(TAG,"isFloorRightD2 is false!");
		return false;
	}
	
	/*
	 * 第四个目标
	 * 判断顶层十字是否还原成功
	 */
	public static boolean isCrossRightU(int[][] curPic){
		if(isMatchTemplate(CROSS_RIGHT_U, curPic)){
			Log.d(TAG,"isCrossRightU is true!");
			return true;
		}
		Log.d(TAG,"isCrossRightU is false!");
		return false;
	}
	
	/*
	 * 第五个目标
	 * 判断顶层平面似乎否还原成功
	 */
	public static boolean isRightU(int[][] curPic){
		if(isMatchTemplate(RIGHT_U, curPic)){
			Log.d(TAG,"isRightU is true!");
			return true;
		}
		Log.d(TAG,"isRightU is false!");
		return false;
	}
	
	/*
	 * 第六个目标
	 * 判断顶层角块是否还原成功
	 */
	public static boolean isJiaoKuaiRightU(int[][] curPic){
		if(isMatchTemplate(JIAO_KUAI_RIGHT_U, curPic)){
			Log.d(TAG,"isJiaoKuaiRightU is true!");
			return true;
		}
		Log.d(TAG,"isJiaoKuaiRightU is false!");
		return false;
	}
	
	/*
	 * 第七个目标
	 * 判断顶层棱块是否还原成功
	 */
	public static boolean isLengKuaiRightU(int[][] curPic){
		if(isMatchTemplate(LENG_KUAI_RIGHT_U, curPic)){
			Log.d(TAG,"isLengKuaiRightU is true!");
			return true;
		}
		Log.d(TAG,"isLengKuaiRightU is false!");
		return false;
	}
	
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] CROSS_RIGHT_D =  {
		{ 0, 0, 0, 0, 1, 0, 0, 1, 0 }, // F
		{ 0, 0, 0, 0, 1, 0, 0, 1, 0 }, // R
		{ 0, 0, 0, 0, 1, 0, 0, 1, 0 }, // B
		{ 0, 0, 0, 0, 1, 0, 0, 1, 0 }, // L
		{ 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		{ 0, 1, 0, 1, 1, 1, 0, 1, 0 } // D
	};
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] FLOOR_RIGHT_D =  {
		{ 0, 0, 0, 0, 1, 0, 1, 1, 1 }, // F
		{ 0, 0, 0, 0, 1, 0, 1, 1, 1 }, // R
		{ 0, 0, 0, 0, 1, 0, 1, 1, 1 }, // B
		{ 0, 0, 0, 0, 1, 0, 1, 1, 1 }, // L
		{ 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } // D
	};
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] FLOOR_RIGHT_D2 =  {
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // F
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // R
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // B
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // L
		{ 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } // D
	};
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] CROSS_RIGHT_U =  {
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // F
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // R
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // B
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // L
		{ 0, 1, 0, 1, 1, 1, 0, 1, 0 }, // U
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } // D
	};
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] RIGHT_U =  {
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // F
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // R
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // B
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // L
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // U
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } // D
	};
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] JIAO_KUAI_RIGHT_U =  {
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1 }, // F
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1 }, // R
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1 }, // B
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1 }, // L
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // U
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } // D
	};
	/*
	 * 模板信息中，0表示可以忽略，1表示需要在正确位置上
	 */
	private static final int[][] LENG_KUAI_RIGHT_U =  {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // F
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // R
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // B
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // L
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // U
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } // D
	};
}
