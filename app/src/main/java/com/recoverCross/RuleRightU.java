package com.recoverCross;

import java.util.ArrayList;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;

import android.util.Log;


public class RuleRightU {
	private static final String TAG = "RuleRightU";
	private static final String GONG_SHI_501 = "L'U'LU'L'UUL";
	private static final String GONG_SHI_502 = "RUR'URUUR'";
	private static final int U_MIAN_INDEX = 4;
	private Cube mCube = null;
	
	private ArrayList<Template> mTemplateList = new ArrayList<Template>();
	
	public RuleRightU(Cube cube){
		mCube = cube;
		initRule();
	}
	
	public String getAction(){
		if(!RecoverCube.isCrossRightU(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isCrossRightU failed.");
			return null;
		}
		
		if(RecoverCube.isRightU(mCube.getCurrentPic().getCurPic())){
			Log.e(TAG, "isRightU OK.");
			return null;
		}
		
		for(int i = 0; i < mTemplateList.size(); i++){
			if(mTemplateList.get(i).getAction(mCube) != null){
				return mTemplateList.get(i).getAction(mCube);
			}
		}
		
		return null;
	}
	
	private void initRule(){
		ArrayList<Position> posList = null;
		/*
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(2,0));
		posList.add(new Position(3,0));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, GONG_SHI_501));
		
		/*
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 1, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(1,0));
		posList.add(new Position(3,0));
		posList.add(new Position(4,2));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_501));
		
		/*
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(2,0));
		posList.add(new Position(0,0));
		posList.add(new Position(4,0));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_501));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 1, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(2,0));
		posList.add(new Position(3,0));
		posList.add(new Position(4,6));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_501));
		
		/*===================================================================*/
		
		/*
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 1, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,2));
		posList.add(new Position(1,2));
		posList.add(new Position(2,2));
		posList.add(new Position(4,6));
		mTemplateList.add(new Template(posList, GONG_SHI_502));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,2));
		posList.add(new Position(2,2));
		posList.add(new Position(3,2));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 1, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(2,2));
		posList.add(new Position(3,2));
		posList.add(new Position(0,2));
		posList.add(new Position(4,2));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,2));
		posList.add(new Position(1,2));
		posList.add(new Position(3,2));
		posList.add(new Position(4,0));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_502));
		
		/*===================================================================*/
		
		/*
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(0,2));
		posList.add(new Position(2,0));
		posList.add(new Position(2,2));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_501+GONG_SHI_501));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(1,2));
		posList.add(new Position(3,0));
		posList.add(new Position(3,2));
		mTemplateList.add(new Template(posList, GONG_SHI_501+GONG_SHI_501));
		
		/*===================================================================*/
		
		/*
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,2));
		posList.add(new Position(2,0));
		posList.add(new Position(3,0));
		posList.add(new Position(3,2));
		mTemplateList.add(new Template(posList, GONG_SHI_502+"U'"+GONG_SHI_502));
		
		/*
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(0,2));
		posList.add(new Position(1,2));
		posList.add(new Position(3,0));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_502+"U'"+GONG_SHI_502));
		
		/*
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(1,0));
		posList.add(new Position(1,2));
		posList.add(new Position(2,2));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_502+"U'"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(2,0));
		posList.add(new Position(2,2));
		posList.add(new Position(3,2));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_502+"U'"+GONG_SHI_502));
		
		/*===================================================================*/
		
		/*
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 1, 0, 1, 0, 1, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(3,0));
		posList.add(new Position(0,2));
		posList.add(new Position(4,2));
		posList.add(new Position(4,6));
		mTemplateList.add(new Template(posList, GONG_SHI_501+"U"+GONG_SHI_502));
		
		/*
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 0, 0, 1, 0, 0, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(1,2));
		posList.add(new Position(4,0));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_501+"U"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 1, 0, 1, 0, 1, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(2,2));
		posList.add(new Position(4,2));
		posList.add(new Position(4,6));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_501+"U"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 0, 0, 1, 0, 0, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(2,0));
		posList.add(new Position(3,2));
		posList.add(new Position(4,0));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_501+"U"+GONG_SHI_502));
		
		/*===================================================================*/
		
		/*
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 1, 0, 1, 0, 0, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(2,2));
		posList.add(new Position(4,2));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, GONG_SHI_502+GONG_SHI_501));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 1, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(3,2));
		posList.add(new Position(4,2));
		posList.add(new Position(4,0));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_502+GONG_SHI_501));
		
		/*
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 0, 0, 1, 0, 1, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(2,0));
		posList.add(new Position(0,2));
		posList.add(new Position(4,6));
		posList.add(new Position(4,0));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_502+GONG_SHI_501));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 1, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,2));
		posList.add(new Position(3,0));
		posList.add(new Position(4,6));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_502+GONG_SHI_501));
		
		/*===================================================================*/
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 1, 0, 1, 0, 0, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(3,0));
		posList.add(new Position(3,2));
		posList.add(new Position(4,2));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, GONG_SHI_502+"U"+GONG_SHI_502 + "U'"+GONG_SHI_502));
		
		/*
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 1, 0, 1, 0, 0, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(0,0));
		posList.add(new Position(0,2));
		posList.add(new Position(4,2));
		posList.add(new Position(4,0));
		mTemplateList.add(new Template(posList, "U"+GONG_SHI_502+"U"+GONG_SHI_502 + "U'"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // R
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 1, 0, 0, 0, 1, 0, 1, 0, 0 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(1,0));
		posList.add(new Position(1,2));
		posList.add(new Position(4,0));
		posList.add(new Position(4,6));
		mTemplateList.add(new Template(posList, "UU"+GONG_SHI_502+"U"+GONG_SHI_502 + "U'"+GONG_SHI_502));
		
		/*
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // R
		 * { 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // B
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // L
		 * { 0, 0, 0, 0, 1, 0, 1, 0, 1 }, // U
		 * { 0, 0, 0, 0, 0, 0, 0, 0, 0 } // D
		 */
		posList = new ArrayList<Position>();
		posList.add(new Position(2,0));
		posList.add(new Position(2,2));
		posList.add(new Position(4,6));
		posList.add(new Position(4,8));
		mTemplateList.add(new Template(posList, "U'"+GONG_SHI_502+"U"+GONG_SHI_502 + "U'"+GONG_SHI_502));
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
