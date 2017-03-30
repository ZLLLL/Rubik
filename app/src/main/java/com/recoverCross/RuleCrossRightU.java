package com.recoverCross;

import java.util.ArrayList;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;

import android.util.Log;


public class RuleCrossRightU {
	private static final String TAG = "RuleCrossRightU";
	private static final String GONG_SHI_401 = "R'U'F'UFR";
	private static final String GONG_SHI_402 = "R'F'U'FUR";
	private static final int U_MIAN_INDEX = 4;
	private Cube mCube = null;
	
	private ArrayList<Template> mTemplateList = new ArrayList<Template>();
	
	public RuleCrossRightU(Cube cube){
		mCube = cube;
		initRule();
	}
	
	public String getAction(){
		if(!RecoverCube.isFloorRightD2(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isFloorRightD2 failed.");
			return null;
		}
		
		if(RecoverCube.isCrossRightU(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isCrossRightU OK.");
			return null;
		}
		
		for(int i = 0; i < mTemplateList.size(); i++){
			if(mTemplateList.get(i).getAction(mCube) != null){
				return mTemplateList.get(i).getAction(mCube);
			}
		}
		Log.d(TAG, "Can not found the rule then try 401.");
		return GONG_SHI_401;
	}
	
	private void initRule(){
		ArrayList<Position> posList = null;
		/*
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 1, 0, 1, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,1));
		posList.add(new Position(1,1));
		posList.add(new Position(4,1));
		posList.add(new Position(4,3));
		mTemplateList.add(new Template(posList, GONG_SHI_401));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 1, 1, 0, 0, 1, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,1));
		posList.add(new Position(2,1));
		posList.add(new Position(4,3));
		posList.add(new Position(4,7));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_401));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 1, 0, 1, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(2,1));
		posList.add(new Position(3,1));
		posList.add(new Position(4,5));
		posList.add(new Position(4,7));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_401));
		
		/*
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 1, 0, 0, 1, 1, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,1));
		posList.add(new Position(3,1));
		posList.add(new Position(4,1));
		posList.add(new Position(4,5));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_401));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 1, 0, 0, 1, 0, 0, 1, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,1));
		posList.add(new Position(4,1));
		posList.add(new Position(4,7));
		mTemplateList.add(new Template(posList, GONG_SHI_402));
		
		/*
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,1));
		posList.add(new Position(4,3));
		posList.add(new Position(4,5));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_402));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 1, 0, 0, 1, 0, 0, 1, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(3,1));
		posList.add(new Position(4,1));
		posList.add(new Position(4,7));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_402));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(2,1));
		posList.add(new Position(4,3));
		posList.add(new Position(4,5));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_402));
	}
	
	private class Position{
		private int[] mPosArray = {-1,-1};
		
		public Position(int mian, int index){
			mPosArray[0] = mian;
			mPosArray[1] = index;
		}
		
		public int getMian(){
			return mPosArray[0];
		}
		
		public int getIndex(){
			return mPosArray[1];
		}
	}
	
	private class Template{
		private ArrayList<Position> mPosList = new  ArrayList<Position>();
		private String mAction = "";
		
		public Template(ArrayList<Position> list, String action){
			mPosList = list;
			mAction = action;
		}
		
		private boolean isMatch(int[][] curPic){
			int matchPos = 0;
			for(int i = 0; i < mPosList.size(); i++){
				Position pos = mPosList.get(i);
				if(curPic[pos.getMian()][pos.getIndex()] 
						== curPic[U_MIAN_INDEX][CubeUtil.CENTER_KUAI_INDEX]){
					matchPos++;
				}
			}
			if(matchPos == mPosList.size()){
				return true;
			}else{
				return false;
			}
		}
		
		public String getAction(Cube cube){
			if(isMatch(cube.getCurrentPic().getCurPic())){
				
				return mAction;
			}
			return null;
		}
	}

}
