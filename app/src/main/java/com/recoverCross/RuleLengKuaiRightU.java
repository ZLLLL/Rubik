package com.recoverCross;

import java.util.ArrayList;

import com.logiccube.Cube;
import com.logiccube.CubeUtil;
import com.logiccube.LengKuai;

import android.util.Log;



public class RuleLengKuaiRightU {
	private static final String TAG = "RuleLengKuaiRightU";
	/**
	 *   2        2
	 * 3   1 -> 1   0
	 *   0        3
	 */
	private static final String GONG_SHI_701 = "RU'RURURU'R'U'RR";

	private ArrayList<Template> mTemplateList = new ArrayList<Template>();

	private static LengKuai[] mRightLengKuaiArray = new LengKuai[4];

	private LengKuai[] mLengKuaiArray = null;

	private Cube mTestCube = null;

	public RuleLengKuaiRightU() {

		// mTestCube = new Cube(cube.getColorTwoArray());
		// Log.d(TAG,"Test Cube:"+mTestCube.toString());

		int[] color0 = { 0, 4 };
		int[] pos0 = { 0, 4 };
		LengKuai lengKuai0 = new LengKuai(color0, pos0);
		mRightLengKuaiArray[0] = lengKuai0;

		int[] color1 = { 1, 4 };
		int[] pos1 = { 1, 4 };
		LengKuai lengKuai1 = new LengKuai(color1, pos1);
		mRightLengKuaiArray[1] = lengKuai1;

		int[] color2 = { 2, 4 };
		int[] pos2 = { 2, 4 };
		LengKuai lengKuai2 = new LengKuai(color2, pos2);
		mRightLengKuaiArray[2] = lengKuai2;

		int[] color3 = { 3, 4 };
		int[] pos3 = { 3, 4 };
		LengKuai lengKuai3 = new LengKuai(color3, pos3);
		mRightLengKuaiArray[3] = lengKuai3;

		initRule();
	}

	private void updateInfo() {
		mLengKuaiArray = CubeUtil.getCurrLengKuaiArray(mTestCube
				.getCurrentPic().getCurPic());
	}

	public String getAction(Cube cube) {

		mTestCube = new Cube(cube.getColorTwoArray());

		updateInfo();

		if (!RecoverCube.isJiaoKuaiRightU(mTestCube.getCurrentPic().getCurPic())) {
			Log.e(TAG, "isJiaoKuaiRightU failed.");
			return null;
		}

		if (RecoverCube.isLengKuaiRightU(mTestCube.getCurrentPic().getCurPic())) {
			Log.e(TAG, "isLengKuaiRightU OK.");
			return null;
		}

		String action = null;

		int[] curSeq = getCurSeq();
		for (int i = 0; i < mTemplateList.size(); i++) {
			String temp = mTemplateList.get(i).getAction(curSeq);
			if(CubeUtil.INVALID_STRING.equals(temp)){
				Log.e(TAG, "can not find action:" + mTestCube.getCurCubeHistory());
				Log.e(TAG, "current seq:{" + curSeq[0] + "," + curSeq[1] + ","
						+ curSeq[2] + "," + curSeq[3] + "}");
				return null;
			}else if (temp != null) {
				Log.e(TAG, "test cube do action:" + temp);
				mTestCube.doAction(temp);
				break;
			} 
		}

		if (RecoverCube.isJiaoKuaiRightU(mTestCube.getCurrentPic().getCurPic())) {
			action = mTestCube.getCurCubeHistory();
			return action;
		} else {
			Log.e(TAG, "can not find action:" + mTestCube.getCurCubeHistory());
			Log.e(TAG, "current seq:{" + curSeq[0] + "," + curSeq[1] + ","
					+ curSeq[2] + "," + curSeq[3] + "}");
			Log.e(TAG, "Test Cube:" + mTestCube.toString());
			return null;
		}
	}

	private int[] getCurSeq() {
		int[] seq = { -1, -1, -1, -1 };
		for (int i = 0; i < seq.length; i++) {
			int temp = getLengKuaiSeq(mLengKuaiArray[i]);
			if (temp < 0 || temp > 3) {
				Log.e(TAG,
						"[getCurSeq]Error:(" + i + "):"
								+ mLengKuaiArray[i].toString());
				return null;
			}
			seq[i] = temp;
		}

		return seq;
	}

	private int getLengKuaiSeq(LengKuai jiaoKuai) {
		for (int j = 0; j < mRightLengKuaiArray.length; j++) {
			if (jiaoKuai.isSame(mRightLengKuaiArray[j])) {
				return j;
			}
		}
		return -1;
	}

	private void initRule() {

		Template temp = null;
		int[] seq = null;
		//共24种排列，一种是正确的不需要处理，需要处理的有23种
		seq = new int[]{0,1,2,3};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{0,1,3,2};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{0,2,1,3};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{0,2,3,1};
		temp = new Template(seq,"UU"+GONG_SHI_701 +"UU");
		mTemplateList.add(temp);
		
		seq = new int[]{0,3,1,2};
		temp = new Template(seq,"UU"+GONG_SHI_701 +GONG_SHI_701+ "UU");
		mTemplateList.add(temp);
		
		seq = new int[]{0,3,2,1};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		/*============================================================*/
		seq = new int[]{3,0,1,2};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{3,0,2,1};
		temp = new Template(seq,GONG_SHI_701+GONG_SHI_701);
		mTemplateList.add(temp);
		
		seq = new int[]{2,0,1,3};
		temp = new Template(seq,"U"+GONG_SHI_701+GONG_SHI_701+"U'");
		mTemplateList.add(temp);
		
		seq = new int[]{2,0,3,1};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{1,0,2,3};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{1,0,3,2};
		temp = new Template(seq,GONG_SHI_701+GONG_SHI_701 + "UU"+GONG_SHI_701+"UU");
		mTemplateList.add(temp);
		
		/*============================================================*/
		seq = new int[]{3,1,0,2};
		temp = new Template(seq,"U'" + GONG_SHI_701+GONG_SHI_701 + "U");
		mTemplateList.add(temp);
		
		seq = new int[]{3,2,0,1};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{2,1,0,3};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{2,3,0,1};
		temp = new Template(seq,"U'"+GONG_SHI_701+"U"+GONG_SHI_701);
		mTemplateList.add(temp);
		
		seq = new int[]{1,2,0,3};
		temp = new Template(seq,"U"+GONG_SHI_701 + "U'");
		mTemplateList.add(temp);
		
		seq = new int[]{1,3,0,2};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		/*============================================================*/
		
		seq = new int[]{3,1,2,0};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{3,2,1,0};
		temp = new Template(seq,GONG_SHI_701 + "UU"+GONG_SHI_701+GONG_SHI_701+"UU");
		mTemplateList.add(temp);
		
		seq = new int[]{2,1,3,0};
		temp = new Template(seq,GONG_SHI_701 + "UU"+GONG_SHI_701+"UU");
		mTemplateList.add(temp);
		
		seq = new int[]{2,3,1,0};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{1,2,3,0};
		temp = new Template(seq,CubeUtil.INVALID_STRING);
		mTemplateList.add(temp);
		
		seq = new int[]{1,3,2,0};
		temp = new Template(seq,GONG_SHI_701);
		mTemplateList.add(temp);
		
		/*============================================================*/

	}

	private class Template {
		private int[] mTemplateLengKuaiSeq = { -1, -1, -1, -1 };
		private String mAction = "";

		public Template(int[] seq, String action) {
			for (int i = 0; i < mTemplateLengKuaiSeq.length; i++) {
				mTemplateLengKuaiSeq[i] = seq[i];
			}
			mAction = action;
		}

		private boolean isMatch(int[] seq) {
			for (int i = 0; i < mTemplateLengKuaiSeq.length; i++) {
				if (mTemplateLengKuaiSeq[i] != seq[i]) {
					return false;
				}
			}
			return true;
		}

		public String getAction(int[] seq) {
			if (isMatch(seq)) {
				return mAction;
			}
			return null;
		}
	}
}
