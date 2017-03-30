package com.recoverCross;

public class RuleObjJiaoKuai {
	private int mStep = -1;
	/*
	 * 目标棱块在8个角块中的位置
	 */
	private int mNeededJiaoKuaiIndex = -1;
	/**
	 * 目标棱块侧面的颜色已经与该侧面的中心块一致
	 * 1 需要逆时针旋转
	 * 0 需要顺时针旋转
	 * -1 不在正确位置上
	 */
	private int mNeededJiaoKuaiPosStatus = -1;

	private String mAction = "";
	
	public RuleObjJiaoKuai(int step, int index, int pos, String action){
		mStep = step;
		mNeededJiaoKuaiIndex = index;
		mNeededJiaoKuaiPosStatus = pos;
		mAction = action;
	}

	public int getmStep() {
		return mStep;
	}

	public String getmAction() {
		return mAction;
	}
	
	public boolean isSameStepAndIndex(int step, int index){
		if(mStep == step && mNeededJiaoKuaiIndex == index){
			return true;
		}
		return false;
	}
	/**
	 * 目标棱块侧面的颜色已经与该侧面的中心块一致
	 * 1 需要逆时针旋转
	 * 0 需要顺时针旋转
	 * -1 不在正确位置上
	 */
	public boolean isSamePosStatus(int pos){
		if(mNeededJiaoKuaiPosStatus == pos){
			return true;
		}
		return false;
	}

	public int getmNeededJiaoKuaiPosStatus() {
		return mNeededJiaoKuaiPosStatus;
	}
}
