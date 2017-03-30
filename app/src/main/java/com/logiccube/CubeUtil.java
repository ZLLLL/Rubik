package com.logiccube;

import android.util.Log;

public class CubeUtil {
	private static final String TAG = "cubeUtil";
	
	public static final int MIAN_NUM = 6; // 面的数量
	public static final int COLOR_NUM_IN_MIAN = 9;//每个面中的颜色数量
	public static final int ZHONG_XIN_KUAI = 6; // 中心块的数量
	public static final int LENG_KUAI = 12; // 棱块的数量
	public static final int JIAO_KUAI = 8; // 角块的数量
	public static final String INVALID_STRING = "INVALID";
	public static final int CENTER_KUAI_INDEX = 4;
	
	public static final String COLOR_RED = "red";
	public static final String COLOR_GREEN = "green";
	public static final String COLOR_ORANGE = "orange";
	public static final String COLOR_BLUE = "blue";
	public static final String COLOR_YELLOW = "yellow";
	public static final String COLOR_WHITE = "white";
	public static final String COLOR_BLACK = "black";
	
	/*
	 * 魔方的基本动作
	 */
	public static final String[] ALLOW_ACTION = {"U","U'","D","D'","F","F'","B","B'","L","L'","R","R'"};
	public static final String[] MIAN_KEY = {"F","R","B","L","U","D"};
	
	public static final String[] FORMAT_STEP = {"FF'","F'F","BB'","B'B","RR'","R'R","LL'","L'L","UU'","U'U","DD'","D'D"};
	
	public static final String[] MIAN_COLOR = {
		COLOR_RED,  //F
		COLOR_GREEN, //R
		COLOR_ORANGE, //B
		COLOR_BLUE,  //L
		COLOR_YELLOW,  //U
		COLOR_WHITE  //D
	};
	
	/*
	 * 上黄，下白，左橙，右红，前蓝，后绿
	 */
	public static final String[][] DEFAULT_CUBE_COLOR_ARRAY = {
		{ COLOR_RED, COLOR_RED, COLOR_RED, COLOR_RED, COLOR_RED, COLOR_RED, COLOR_RED, COLOR_RED, COLOR_RED }, // F
		{ COLOR_GREEN, COLOR_GREEN, COLOR_GREEN, COLOR_GREEN, COLOR_GREEN, COLOR_GREEN, COLOR_GREEN, COLOR_GREEN, COLOR_GREEN }, // R
		{ COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE, COLOR_ORANGE }, // B
		{ COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE }, //L
		{ COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW, COLOR_YELLOW }, // U
		{ COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE } // D
	};
	
	/*
     * 此函数的目的是检验输入的魔方是否是一个正确的魔方
     * 1.校验输入的数组是否为6种颜色
     * 2.检查中心色块
     * 3.检查所以6种类的颜色个数是否为9
     */
	public static boolean isCorrectCubeColor(String[][] cubeColor) {
		String[] colorType = {null,null,null,null,null,null};
		int[] colorTypeNum = {0,0,0,0,0,0};
		
		/*
		 * 检查输入数组的合法性
		 */
		if(cubeColor == null){
			Log.e(TAG, "cubeColor is null.");
			return false;
		}
		if(cubeColor.length != 6){
			Log.e(TAG, "cubeColor.length is not 6.");
			return false;
		}
		for(int i = 0; i < cubeColor.length; i++){
			if(cubeColor[i] == null){
				Log.e(TAG, "cubeColor["+i+"] is null.");
				return false;
			}
			if(cubeColor[i].length != 9){
				Log.e(TAG, "cubeColor["+i+"].length is not 9.");
				return false;
			}
			for (int j = 0; j < cubeColor[i].length; j++){
				if( cubeColor[i][j] == null){
					Log.e(TAG, "cubeColor["+i+"]["+j+"] is null.");
					return false;
				}
				if("".equals(cubeColor[i][j])){
					Log.e(TAG, "cubeColor["+i+"]["+j+"] is empty.");
					return false;
				}
			}
		}
		/*
		 * 记录中心块颜色
		 */
		for(int i = 0; i < colorType.length; i++){
			colorType[i] = cubeColor[i][CENTER_KUAI_INDEX];
		}
		/*
		 * 检查中心块的颜色是否有相同的
		 */
		for(int i = 0; i < colorType.length - 1; i++){
			for(int j = i + 1; j< colorType.length; j++){
				if(colorType[i].equals(colorType[j])){
					Log.e(TAG, "The center color is not correct.");
					Log.e(TAG, "colorType["+i+"]:"+colorType[i]);
					Log.e(TAG, "colorType["+j+"]:"+colorType[j]);
					return false;
				}
			}
		}
		/*
		 * 计算颜色个数，同时检查所有颜色，有超过中心块颜色的，则报错
		 */
		for(int i = 0; i < cubeColor.length; i++){
			for (int j = 0; j < cubeColor[i].length; j++){
				boolean matchRet = false;
				for(int k = 0; k < colorType.length; k++){
					if(cubeColor[i][j].equals(colorType[k])){
						matchRet = true;
						colorTypeNum[k]++;
					}
				}
				if(!matchRet){
					Log.e(TAG, "cubeColor["+i+"]["+j+"]:"+cubeColor[i][j]+" is not match.");
					return false;
				}
			}
		}
		/*
		 * 检查所有颜色的个数是否均为9
		 */
		for(int i = 0; i < colorTypeNum.length; i++){
			if(colorTypeNum[i] != 9){
				Log.e(TAG, "colorTypeNum["+i+"]:"+colorTypeNum[i]+" is not 9.");
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * 这个函数的功能是获取魔方的颜色数组
	 */
	public static String[] getCubeColorArray(String[][] cubeColor){
		String[] Color = {"blue","red","green","orange","yellow","white"};
		
		for(int i = 0; i < Color.length; i++){
			Color[i] = cubeColor[i][CubeUtil.CENTER_KUAI_INDEX];
			Log.d(TAG, "Color["+i+"]:"+Color[i]);
		}
		
		return Color;
	}
	
	/*
	 * 这个函数的功能主要是检查是否有相同的棱块或者角块
	 * 
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
	 * cubeInt结构
	 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
	 * { 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // R
	 * { 2, 2, 2, 2, 2, 2, 2, 2, 2 }, // B
	 * { 3, 3, 3, 3, 3, 3, 3, 3, 3 }, // L
	 * { 4, 4, 4, 4, 4, 4, 4, 4, 4 }, // U
	 * { 5, 5, 5, 5, 5, 5, 5, 5, 5 } // D
	 */
    public static boolean isCorrectCubeInt(int [][] cubeInt){
    	/*
    	 * 魔方有8个角块
    	 */
    	JiaoKuai[] jiaoKuaiArray = null;
    	/*
    	 * 魔方有12个棱块
    	 */
    	LengKuai[] lengKuaiArray = null;
    	
    	/*
		 * 检查输入数组的合法性
		 */
		
		if(cubeInt == null){
			Log.e(TAG, "cubeInt is null.");
			return false;
		}
		if(cubeInt.length != 6){
			Log.e(TAG, "cubeInt.length is not 6.");
			return false;
		}
		for(int i = 0; i < cubeInt.length; i++){
			if(cubeInt[i] == null){
				Log.e(TAG, "cubeInt["+i+"] is null.");
				return false;
			}
			if(cubeInt[i].length != 9){
				Log.e(TAG, "cubeInt["+i+"].length is not 9.");
				return false;
			}
			for (int j = 0; j < cubeInt[i].length; j++){
				if( cubeInt[i][j] < 0 || cubeInt[i][j] > 5){
					Log.e(TAG, "cubeInt["+i+"]["+j+"] is invalid.");
					return false;
				}
			}
		}
		
		/*
		 * 根据数字数组生成棱块和角块，判断是否有一样的棱块或角块
		 * cubeInt第一个数组的顺序F,R,B,L,U,D
		 */
		//角块
		jiaoKuaiArray = getCurrJiaoKuaiArray(cubeInt);
		
		//棱块
		lengKuaiArray = getCurrLengKuaiArray(cubeInt);

		for (int i = 0; i < jiaoKuaiArray.length - 1; i++){
			for (int j = i + 1; j < jiaoKuaiArray.length; j++){
				if (jiaoKuaiArray[i].isSame(jiaoKuaiArray[j])){
					Log.e(TAG, "jiaoKuaiArray["+i+"] is same as jiaoKuaiArray["+j+"].");
					Log.e(TAG, "jiaoKuaiArray["+i+"]:"+jiaoKuaiArray[i].toString());
					Log.e(TAG, "jiaoKuaiArray["+j+"]:"+jiaoKuaiArray[j].toString());
					return false;
				}
			}
		}

		for (int i = 0; i < lengKuaiArray.length - 1; i++){
			for (int j = i + 1; j < lengKuaiArray.length; j++){
				if (lengKuaiArray[i].isSame(lengKuaiArray[j])){
					Log.e(TAG, "lengKuaiArray["+i+"] is same as lengKuaiArray["+j+"].");
					Log.e(TAG, "lengKuaiArray["+i+"]:"+lengKuaiArray[i].toString());
					Log.e(TAG, "lengKuaiArray["+j+"]:"+lengKuaiArray[j].toString());
					return false;
				}
			}
		}

		return true;
	}
    /*
	 * 魔方的六个面是有对应关系的，
	 * 已F面为基准，排列的位置为,
	 *            1 2 3
	 *            4 5 6 -- U
	 *            7 8 9
	 *      1 2 3 1 2 3 1 2 3 1 2 3
	 * L -- 4 5 6 4 5 6 4 5 6 4 5 6 -- B
	 *      7 8 9 7 8 9 7 8 9 7 8 9
	 *            1 2 3
	 *            4 5 6 -- D
	 *            7 8 9
	 *但是照相输入的数据排列位置如下，所以需要转换
	 *            9 8 7
	 *            6 5 4 -- U
	 *            3 2 1
	 *      1 2 3 1 2 3 1 2 3 1 2 3
	 * L -- 4 5 6 4 5 6 4 5 6 4 5 6 -- B
	 *      7 8 9 7 8 9 7 8 9 7 8 9
	 *            9 8 7
	 *            6 5 4 -- D
	 *            3 2 1
	 * 拍照的顺序是F，R，B，L，U，D
     */
    public static String[][] changeFormat(String[][] input){
    	String[][] output = {
    			{ "", "", "", "", "", "", "", "", "" }, // F
    			{ "", "", "", "", "", "", "", "", "" }, // R
    			{ "", "", "", "", "", "", "", "", "" }, // B
    			{ "", "", "", "", "", "", "", "", "" }, // L
    			{ "", "", "", "", "", "", "", "", "" }, // U
    			{ "", "", "", "", "", "", "", "", "" } // D
    		};
        Log.d(TAG, "changeFormat.");
		
		/*
		 * 检查输入数组的合法性
		 */
		if(input == null){
			Log.e(TAG, "input is null.");
			return output;
		}
		if(input.length != 6){
			Log.e(TAG, "input.length is not 6.");
			return output;
		}
		for(int i = 0; i < input.length; i++){
			if(input[i] == null){
				Log.e(TAG, "input["+i+"] is null.");
				return output;
			}
			if(input[i].length != 9){
				Log.e(TAG, "input["+i+"].length is not 9.");
				return output;
			}
			for (int j = 0; j < input[i].length; j++){
				if( input[i][j] == null){
					Log.e(TAG, "input["+i+"]["+j+"] is null.");
					return output;
				}
				if("".equals(input[i][j])){
					Log.e(TAG, "input["+i+"]["+j+"] is empty.");
					return output;
				}
			}
		}
		for(int i = 0; i < output.length; i++){
			/*
			 * U,D需要逆序
			 */
			if(i > 3){
				for (int j = 0; j < output[i].length; j++){
					output[i][j] = input[i][output[i].length - 1 - j];
				}
			}else{
				for (int j = 0; j < output[i].length; j++){
					output[i][j] = input[i][j];
				}	
			}
		}
		return output;
    }
    /**
     * @param from
     * @param to
     * @return
     */
    public static boolean copyCubeInt(int[][] from, int[][] to){
    	if(from.length != to.length){
    		Log.e(TAG, "[copyCubeInt]length is different.["+from.length+","+to.length+"]");
    		return false;
    	}
    	for(int i = 0; i < from.length; i++){
    		if(from[i].length != to[i].length){
        		Log.e(TAG, "[copyCubeInt]from["+i+"] length is different from to["+i+"]." +
        				"["+from[i].length+","+to[i].length+"]");
        		return false;
        	}
    		for(int j = 0; j< from[i].length; j++){
    			to[i][j] = from[i][j];
    		}
    	}
    	return true;
    }
    /**
     * @param from
     * @param to
     * @return
     */
    public static boolean copyCubeIntOneArray(int[] from, int[] to){
    	if(from.length != to.length){
    		Log.e(TAG, "[copyCubeIntOneArray]length is different.["+from.length+","+to.length+"]");
    		return false;
    	}
    	for(int i = 0; i < from.length; i++){
    		to[i] = from[i];
    	}
    	return true;
    }
    /**
     * @param from
     * @param to
     * @return
     */
    public static boolean copyCubeColor(String[][] from, String[][] to){
    	if(from.length != to.length){
    		Log.e(TAG, "[copyCubeColor]length is different.["+from.length+","+to.length+"]");
    		return false;
    	}
    	for(int i = 0; i < from.length; i++){
    		if(from[i].length != to[i].length){
        		Log.e(TAG, "[copyCubeColor]from["+i+"] length is different from to["+i+"]." +
        				"["+from[i].length+","+to[i].length+"]");
        		return false;
        	}
    		for(int j = 0; j< from[i].length; j++){
    			to[i][j] = from[i][j] + "";
    		}
    	}
    	return true;
    }
    /**
     * @param from
     * @param to
     * @return
     */
    public static boolean copyCubeColorOneArray(String[] from, String[] to){
    	if(from.length != to.length){
    		Log.e(TAG, "[copyCubeColorOneArray]length is different.["+from.length+","+to.length+"]");
    		return false;
    	}
    	for(int i = 0; i < from.length; i++){
    		to[i] = from[i] + "";
    	}
    	return true;
    }
    
    public static boolean isExistInStrArray(String temp, String[] strArray){
    	if(temp == null){
    		Log.e(TAG, "[isExistInStrArray]temp is null.");
    		return false;
    	}
    	if(strArray == null){
    		Log.e(TAG, "[isExistInStrArray]strArray is null.");
    		return false;
    	}
    	
    	for (int i = 0; i < strArray.length; i++){
    		if(temp.equals(strArray[i])) return true;
    	}
    	
    	return false;
    }
    /*
     * 棱块的数组 包含位置和颜色信息
     * [F1,U7],[R1,U5],[B1,U1],[L1,U3]
	 * [L5,F3],[F5,R3],[R5,B3],[B5,L3]
	 * [F7,D1],[R7,D5],[B7,D7],[L7,D3]
     */
    public static LengKuai[] getCurrLengKuaiArray(int[][] curPic){
		LengKuai[] lengKuaiArray = new LengKuai[12];
		int[] color = new int[2];
		int[] pos = new int[2];
		
		//[F1,U7]
		pos[0] = 0; color[0] = curPic[0][1];
		pos[1] = 4; color[1] = curPic[4][7];
		lengKuaiArray[0] = new LengKuai(color, pos);
		
		//[R1,U5]
		pos[0] = 1; color[0] = curPic[1][1];
		pos[1] = 4; color[1] = curPic[4][5];
		lengKuaiArray[1] = new LengKuai(color, pos);
		
		//[B1,U1]
		pos[0] = 2; color[0] = curPic[2][1];
		pos[1] = 4; color[1] = curPic[4][1];
		lengKuaiArray[2] = new LengKuai(color, pos);
		
		//[L1,U3]
		pos[0] = 3; color[0] = curPic[3][1];
		pos[1] = 4; color[1] = curPic[4][3];
		lengKuaiArray[3] = new LengKuai(color, pos);
		
		//[L5,F3]
		pos[0] = 3; color[0] = curPic[3][5];
		pos[1] = 0; color[1] = curPic[0][3];
		lengKuaiArray[4] = new LengKuai(color, pos);
		
		//[F5,R3]
		pos[0] = 0; color[0] = curPic[0][5];
		pos[1] = 1; color[1] = curPic[1][3];
		lengKuaiArray[5] = new LengKuai(color, pos);
		
		//[R5,B3] 
		pos[0] = 1; color[0] = curPic[1][5];
		pos[1] = 2; color[1] = curPic[2][3];
		lengKuaiArray[6] = new LengKuai(color, pos);
		
		//[B5,L3] 
		pos[0] = 2; color[0] = curPic[2][5];
		pos[1] = 3; color[1] = curPic[3][3];
		lengKuaiArray[7] = new LengKuai(color, pos);
		
		//[F7,D1]
		pos[0] = 0; color[0] = curPic[0][7];
		pos[1] = 5; color[1] = curPic[5][1];
		lengKuaiArray[8] = new LengKuai(color, pos);

		//[R7,D5]
		pos[0] = 1; color[0] = curPic[1][7];
		pos[1] = 5; color[1] = curPic[5][5];
		lengKuaiArray[9] = new LengKuai(color, pos);
		
		//[B7,D7]
		pos[0] = 2; color[0] = curPic[2][7];
		pos[1] = 5; color[1] = curPic[5][7];
		lengKuaiArray[10] = new LengKuai(color, pos);
		
		//[L7,D3]
		pos[0] = 3; color[0] = curPic[3][7];
		pos[1] = 5; color[1] = curPic[5][3];
		lengKuaiArray[11] = new LengKuai(color, pos);

		return lengKuaiArray;
	}
    /*
     * 角块的数组 包含位置和颜色信息
     * [L2,U6,F0],[F2,U8,R0]，[R2,U2,B0],[B2,U0,L0]
     * [L8,D0,F6],[F8,D2,R6]，[R8,D6,B6],[B8,D8,L6]
     */
    public static JiaoKuai[] getCurrJiaoKuaiArray(int[][] curPic){
    	JiaoKuai[] jiaoKuaiArray = new JiaoKuai[8];
    	
    	int[] color = new int[3];
		int[] pos = new int[3];
		
		//[L2,U6,F0]
		pos[0] = 3; color[0] = curPic[3][2];
		pos[1] = 4; color[1] = curPic[4][6];
		pos[2] = 0; color[2] = curPic[0][0];
		jiaoKuaiArray[0] = new JiaoKuai(color, pos);
		
		//[F2,U8,R0]
		pos[0] = 0; color[0] = curPic[0][2];
		pos[1] = 4; color[1] = curPic[4][8];
		pos[2] = 1; color[2] = curPic[1][0];
		jiaoKuaiArray[1] = new JiaoKuai(color, pos);
		
		//[R2,U2,B0]
		pos[0] = 1; color[0] = curPic[1][2];
		pos[1] = 4; color[1] = curPic[4][2];
		pos[2] = 2; color[2] = curPic[2][0];
		jiaoKuaiArray[2] = new JiaoKuai(color, pos);
		
		//[B2,U0,L0]
		pos[0] = 2; color[0] = curPic[2][2];
		pos[1] = 4; color[1] = curPic[4][0];
		pos[2] = 3; color[2] = curPic[3][0];
		jiaoKuaiArray[3] = new JiaoKuai(color, pos);
		
		//[L8,D0,F6]
		pos[0] = 3; color[0] = curPic[3][8];
		pos[1] = 5; color[1] = curPic[5][0];
		pos[2] = 0; color[2] = curPic[0][6];
		jiaoKuaiArray[4] = new JiaoKuai(color, pos);
		
		//[F8,D2,R6]，
		pos[0] = 0; color[0] = curPic[0][8];
		pos[1] = 5; color[1] = curPic[5][2];
		pos[2] = 1; color[2] = curPic[1][6];
		jiaoKuaiArray[5] = new JiaoKuai(color, pos);
		
		//[R8,D8,B6]
		pos[0] = 1; color[0] = curPic[1][8];
		pos[1] = 5; color[1] = curPic[5][8];
		pos[2] = 2; color[2] = curPic[2][6];
		jiaoKuaiArray[6] = new JiaoKuai(color, pos);
		
		//[B8,D6,L6]
		pos[0] = 2; color[0] = curPic[2][8];
		pos[1] = 5; color[1] = curPic[5][6];
		pos[2] = 3; color[2] = curPic[3][6];
		jiaoKuaiArray[7] = new JiaoKuai(color, pos);

		return jiaoKuaiArray;
    }
    
    public static boolean isValidOneStep(String step){
    	for (int i = 0; i < ALLOW_ACTION.length; i++){
    		if(ALLOW_ACTION[i].equals(step)){
    			return true;
    		}
    	}
    	Log.e(TAG,"invalid one step:" + step);
    	return false;
    }
    
    public static boolean isValidPatchStep(String patchStep){
    	if(patchStep == null){
    		return false;
    	}
    	String temp = patchStep + "";
    	
    	//先检查所有的逆时针操作
    	for (int i = 1; i < ALLOW_ACTION.length; i+=2){
    		if(temp.indexOf(ALLOW_ACTION[i]) != -1){
    			temp = temp.replaceAll(ALLOW_ACTION[i], "");
    		}
    	}
    	//再检查所有的顺时针操作
    	for (int i = 0; i < ALLOW_ACTION.length; i+=2){
    		if(temp.indexOf(ALLOW_ACTION[i]) != -1){
    			temp = temp.replaceAll(ALLOW_ACTION[i], "");
    		}
    	}
    	
    	if(!"".equals(temp)){
    		Log.e(TAG,"invalid patchStep:" + patchStep);
    		Log.e(TAG,"invalid part:"+temp);
    		return false;
    	}
    	return true;
    }
    
    public static String getPrintCubeColorInfo(int[][] input){
    	String output = "{";
		for (int i = 0; i < input.length; i++){
			output = output + "{";
			for(int j = 0; j < input[i].length; j++){
				output = output + MIAN_COLOR[input[i][j]];
				if(j < input[i].length - 1){
					output = output + ",";
				}
			}
			output = output + "}";
			if(i < input.length - 1){
				output = output + ",\n";
			}
		}
		output = output + "}";
		return output;
    }
    
    public static String getPrintCubeTwoArrayInfo(int[][] input){
    	String output = "{";
		for (int i = 0; i < input.length; i++){
			output = output + "{";
			for(int j = 0; j < input[i].length; j++){
				output = output + input[i][j];
				if(j < input[i].length - 1){
					output = output + ",";
				}
			}
			output = output + "}";
			if(i < input.length - 1){
				output = output + ",\n";
			}
		}
		output = output + "}";
		return output;
    }
    
    public static String getPrintCubeTwoArrayInfo(String[][] input){
    	String output = "{";
		for (int i = 0; i < input.length; i++){
			output = output + "{";
			for(int j = 0; j < input[i].length; j++){
				output = output + input[i][j];
				if(j < input[i].length - 1){
					output = output + ",";
				}
			}
			output = output + "}";
			if(i < input.length - 1){
				output = output + ",\n";
			}
		}
		output = output + "}";
		return output;
    }


}
