package com.recoverCross;


public class RuleObjLengKuai {
	//private static final String TAG = "ruleObjLengKuai";
	
	private int mStep = -1;
	/*
	 * 目标棱块在12个棱块中的位置
	 */
	private int mNeededLengKuaiIndex = -1;
	/**
	 * 目标棱块侧面的颜色已经与该侧面的中心块一致
	 * 0 不一致
	 * 1 一致
	 * -1 不在一个侧面
	 */
	private int mNeededLengKuaiPosStatus = -1;
	
	private String mAction = "";
	
	public RuleObjLengKuai(int step, int index, int pos, String action){
		mStep = step;
		mNeededLengKuaiIndex = index;
		mNeededLengKuaiPosStatus = pos;
		mAction = action;
	}

	public int getmStep() {
		return mStep;
	}

	public int getmNeededLengKuaiIndex() {
		return mNeededLengKuaiIndex;
	}
	/**
	 * 目标棱块侧面的颜色已经与该侧面的中心块一致
	 * 0 不一致
	 * 1 一致
	 * -1 不在一个侧面
	 */
	public int getmNeededLengKuaiPosStatus() {
		return mNeededLengKuaiPosStatus;
	}

	public String getmAction() {
		return mAction;
	}
	
	public boolean isSameStepAndIndex(int step, int index){
		if(mStep == step && mNeededLengKuaiIndex == index){
			return true;
		}
		return false;
	}
	
	public boolean isSamePosStatus(int pos){
		if(mNeededLengKuaiPosStatus == pos){
			return true;
		}
		return false;
	}
}
