package com.logiccube;

import android.util.Log;

public class JiaoKuai {
	private static final String TAG = "JiaoKuai";
    private int[] mColor = {-1, -1, -1};
    /*
     * 面的int数字
     * 0 - 5
     * F,R,B,L,U,D
     */
    private int[] mPos = {-1, -1, -1};
    
    public int[] getmPos() {
		return mPos;
	}

	public JiaoKuai(int[] color){
        if(color == null){
        	Log.e(TAG,"color is null.");
            return;
        }
        if(color.length != 3){
        	Log.e(TAG,"color.length is not 3.");
            return;
        }
        
        if(color[0] == color[1] || color[0] == color[2] || color[1] == color[2]){
        	Log.e(TAG,"color["+color[0]+","+color[1]+","+color[2]+"]");
        	Log.e(TAG,"1 There are same color.");
            return;
        }
        for (int i = 0; i < mColor.length; i++){
            mColor[i] = color[i];
        }
    }
    /**
     * @param int[] color, int[] pos
     * 
     */
    
    public JiaoKuai(int[] color, int[] pos){
        if(color == null){
        	Log.e(TAG,"color is null.");
            return;
        }
        if(color.length != 3){
        	Log.e(TAG,"color.length is not 3.");
            return;
        }
        if(pos == null){
        	Log.e(TAG,"pos is null.");
            return;
        }
        if(pos.length != 3){
        	Log.e(TAG,"pos.length is not 3.");
            return;
        }
        if(color[0] == color[1] || color[0] == color[2] || color[1] == color[2]){
        	Log.e(TAG,"color["+color[0]+","+color[1]+","+color[2]+"]");
        	Log.e(TAG,"pos["+pos[0]+","+pos[1]+","+pos[2]+"]");
        	Log.e(TAG,"2 There are same color.");
            return;
        }
        if(pos[0] == pos[1] || pos[0] == pos[2] || pos[1] == pos[2]){
        	Log.e(TAG,"color["+color[0]+","+color[1]+","+color[2]+"]");
        	Log.e(TAG,"pos["+pos[0]+","+pos[1]+","+pos[2]+"]");
        	Log.e(TAG,"There are same pos.");
            return;
        }
        for (int i = 0; i < mColor.length; i++){
            mColor[i] = color[i];
            mPos[i] = pos[i];
        }
    }
    
    public boolean isColorExist(int temp){
    	for (int i = 0; i < mColor.length; i++){
            if(mColor[i] == temp){
            	return true;
            }
        }
    	return false;
    }
    
    public boolean isSame(JiaoKuai other){
    	boolean ret = true;
    	if(other == null){
    		Log.e(TAG, "[isSame]other is null!");
    		return false;
    	}
    	for (int i = 0; i < mColor.length; i++){
    		ret = ret && other.isColorExist(mColor[i]);
        }
    	return ret;
    }

    /*
     * 判断棱块的颜色和位置是否与给定的一致
     */
    public boolean isPosColor(int pos, int color){
    	for(int i = 0 ; i < mPos.length; i++){
    		if(mPos[i] == pos && mColor[i] == color){
    			return true;
    		}
    	}
    	return false;
    }

    public boolean hasPos(int pos){
    	for(int i = 0 ; i < mPos.length; i++){
    		if(mPos[i] == pos){
    			return true;
    		}
    	}
    	return false;
	}

	@Override
	public String toString() {
		String temp = "color["+mColor[0] + "," + mColor[1] +"," + mColor[2] +"], pos["+mPos[0] + "," + mPos[1] +"," + mPos[2] +"]";
		return temp;
	}
}
