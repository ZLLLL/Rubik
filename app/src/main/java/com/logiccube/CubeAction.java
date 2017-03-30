package com.logiccube;

import java.util.HashMap;

public class CubeAction {
	//private static String TAG = "cubeAction";
	private static HashMap<String, CubeActionItem> mAction = new HashMap<String, CubeActionItem>();
	
	/*
	 * 旋转动作 U
	 * X为不会发生变化的位置
	 * F->L->B->R->F,这几个面的三个颜色的变化
	 * U面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { 0, 0, 0, X, X, X, X, X, X }, // F
	 * { 0, 0, 0, X, X, X, X, X, X }, // R
	 * { 0, 0, 0, X, X, X, X, X, X }, // B
	 * { 0, 0, 0, X, X, X, X, X, X }, // L
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // U
	 * { X, X, X, X, X, X, X, X, X } // D
	 */
	
	/*
	 * 旋转动作 U'
	 * X为不会发生变化的位置
	 * F<-L<-B<-R<-F,这几个面的三个颜色的变化
	 * U面颜色位置变化 0<-2,1<-5,2<-8,3<-1,5<-7,6<-0,7<-3,8<-6
	 * { 0, 0, 0, X, X, X, X, X, X }, // F
	 * { 0, 0, 0, X, X, X, X, X, X }, // R
	 * { 0, 0, 0, X, X, X, X, X, X }, // B
	 * { 0, 0, 0, X, X, X, X, X, X }, // L
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // U
	 * { X, X, X, X, X, X, X, X, X } // D
	 */
	
	/*
	 * 旋转动作 D
	 * X为不会发生变化的位置
	 * F<-L<-B<-R<-F,这几个面的后三个颜色的变化
	 * D面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { X, X, X, X, X, X, 0, 0, 0 }, // F
	 * { X, X, X, X, X, X, 0, 0, 0 }, // R
	 * { X, X, X, X, X, X, 0, 0, 0 }, // B
	 * { X, X, X, X, X, X, 0, 0, 0 }, // L
	 * { X, X, X, X, X, X, X, X, X }, // U
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 } // D
	 */
	
	/*
	 * 旋转动作 D'
	 * X为不会发生变化的位置
	 * F->L->B->R->F,这几个面的三个颜色的变化
	 * D面颜色位置变化 0<-2,1<-5,2<-8,3<-1,5<-7,6<-0,7<-3,8<-6
	 * { X, X, X, X, X, X, 0, 0, 0 }, // F
	 * { X, X, X, X, X, X, 0, 0, 0 }, // R
	 * { X, X, X, X, X, X, 0, 0, 0 }, // B
	 * { X, X, X, X, X, X, 0, 0, 0 }, // L
	 * { X, X, X, X, X, X, X, X, X }, // U
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 } // D
	 */
	
	/*
	 * 旋转动作 F
	 * X为不会发生变化的位置
	 * U<-L<-D<-R<-U,这几个面的三个颜色的变化
	 * F面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // F
	 * { 0, X, X, 0, X, X, 0, X, X }, // R
	 * { X, X, X, X, X, X, X, X, X }, // B
	 * { X, X, 0, X, X, 0, X, X, 0 }, // L
	 * { X, X, X, X, X, X, 0, 0, 0 }, // U
	 * { 0, 0, 0, X, X, X, X, X, X } // D
	 */
	
	/*
	 * 旋转动作 F'
	 * X为不会发生变化的位置
	 * U->L->D->R->U,这几个面的三个颜色的变化
	 * F面颜色位置变化 0<-2,1<-5,2<-8,3<-1,5<-7,6<-0,7<-3,8<-6
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // F
	 * { 0, X, X, 0, X, X, 0, X, X }, // R
	 * { X, X, X, X, X, X, X, X, X }, // B
	 * { X, X, 0, X, X, 0, X, X, 0 }, // L
	 * { X, X, X, X, X, X, 0, 0, 0 }, // U
	 * { 0, 0, 0, X, X, X, X, X, X } // D
	 */
	
	/*
	 * 旋转动作 B
	 * X为不会发生变化的位置
	 * U->L->D->R->U,这几个面的三个颜色的变化
	 * B面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { X, X, X, X, X, X, X, X, X }, // F
	 * { X, X, 0, X, X, 0, X, X, 0 }, // R
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // B
	 * { 0, X, X, 0, X, X, 0, X, X }, // L
	 * { 0, 0, 0, X, X, X, X, X, X }, // U
	 * { X, X, X, X, X, X, 0, 0, 0 } // D
	 */
	
	/*
	 * 旋转动作 B'
	 * X为不会发生变化的位置
	 * U<-L<-D<-R<-U,这几个面的三个颜色的变化
	 * B面颜色位置变化 0<-2,1<-5,2<-8,3<-1,5<-7,6<-0,7<-3,8<-6
	 * { X, X, X, X, X, X, X, X, X }, // F
	 * { X, X, 0, X, X, 0, X, X, 0 }, // R
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // B
	 * { 0, X, X, 0, X, X, 0, X, X }, // L
	 * { 0, 0, 0, X, X, X, X, X, X }, // U
	 * { X, X, X, X, X, X, 0, 0, 0 } // D
	 */
	
	/*
	 * 旋转动作 R
	 * X为不会发生变化的位置
	 * U->B->D->F->U,这几个面的三个颜色的变化
	 * R面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { X, X, 0, X, X, 0, X, X, 0 }, // F
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // R
	 * { 0, X, X, 0, X, X, 0, X, X }, // B
	 * { X, X, X, X, X, X, X, X, X }, // L
	 * { X, X, 0, X, X, 0, X, X, 0 }, // U
	 * { X, X, 0, X, X, 0, X, X, 0 } // D
	 */
	
	/*
	 * 旋转动作 R'
	 * X为不会发生变化的位置
	 * U->F->D->B->U,这几个面的三个颜色的变化
	 * R面颜色位置变化 0<-2,1<-5,2<-8,3<-1,5<-7,6<-0,7<-3,8<-6
	 * { X, X, 0, X, X, 0, X, X, 0 }, // F
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // R
	 * { 0, X, X, 0, X, X, 0, X, X }, // B
	 * { X, X, X, X, X, X, X, X, X }, // L
	 * { X, X, 0, X, X, 0, X, X, 0 }, // U
	 * { X, X, 0, X, X, 0, X, X, 0 } // D
	 */
	
	/*
	 * 旋转动作 L
	 * X为不会发生变化的位置
	 * U->F->D->B->U,这几个面的三个颜色的变化
	 * R面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { 0, X, X, 0, X, X, 0, X, X }, // F
	 * { X, X, X, X, X, X, X, X, X }, // R
	 * { X, X, 0, X, X, 0, X, X, 0 }, // B
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // L
	 * { 0, X, X, 0, X, X, 0, X, X }, // U
	 * { 0, X, X, 0, X, X, 0, X, X } // D
	 */
	
	/*
	 * 旋转动作 L'
	 * X为不会发生变化的位置
	 * U->B->D->F->U,这几个面的三个颜色的变化
	 * R面颜色位置变化 0->2,1->5,2->8,3->1,5->7,6->0,7->3,8->6
	 * { 0, X, X, 0, X, X, 0, X, X }, // F
	 * { X, X, X, X, X, X, X, X, X }, // R
	 * { X, X, 0, X, X, 0, X, X, 0 }, // B
	 * { 0, 0, 0, 0, X, 0, 0, 0, 0 }, // L
	 * { 0, X, X, 0, X, X, 0, X, X }, // U
	 * { 0, X, X, 0, X, X, 0, X, X } // D
	 */
	public CubeAction(){
		
		//U 顺时针方向 F->L->B->R->F
		String action_U = "U";
		String[] seq_U = {"F","L","B","R","F"};
		HashMap<String,int[]> actionParam_U = new HashMap<String,int[]>();
		actionParam_U.put("F", new int[] {0,1,2});
		actionParam_U.put("L", new int[] {0,1,2});
		actionParam_U.put("B", new int[] {0,1,2});
		actionParam_U.put("R", new int[] {0,1,2});
		
		HashMap<String,int[]> actionValue_U = new HashMap<String,int[]>();
		actionValue_U.put("F", new int[] {0,1,2});
		actionValue_U.put("L", new int[] {0,1,2});
		actionValue_U.put("B", new int[] {0,1,2});
		actionValue_U.put("R", new int[] {0,1,2});
		CubeActionItem cubeAction_U = new CubeActionItem(action_U,seq_U,actionParam_U,actionValue_U);
		mAction.put(action_U, cubeAction_U);
		
		//U' 逆时针方向 F->R->B->L->F
		String action_U1 = "U'";
		String[] seq_U1 = {"F","R","B","L","F"};
		HashMap<String,int[]> actionParam_U1 = new HashMap<String,int[]>();
		actionParam_U1.put("F", new int[] {0,1,2});
		actionParam_U1.put("R", new int[] {0,1,2});
		actionParam_U1.put("B", new int[] {0,1,2});
		actionParam_U1.put("L", new int[] {0,1,2});
		
		HashMap<String,int[]> actionValue_U1 = new HashMap<String,int[]>();
		actionValue_U1.put("F", new int[] {0,1,2});
		actionValue_U1.put("R", new int[] {0,1,2});
		actionValue_U1.put("B", new int[] {0,1,2});
		actionValue_U1.put("L", new int[] {0,1,2});
		CubeActionItem cubeAction_U1 = new CubeActionItem(action_U1,seq_U1,actionParam_U1,actionValue_U1);
		mAction.put(action_U1, cubeAction_U1);
		
		//D 顺时针方向 F->R->B->L->F
		String action_D = "D";
		String[] seq_D = {"F","R","B","L","F"};
		HashMap<String,int[]> actionParam_D = new HashMap<String,int[]>();
		actionParam_D.put("F", new int[] {6,7,8});
		actionParam_D.put("R", new int[] {6,7,8});
		actionParam_D.put("B", new int[] {6,7,8});
		actionParam_D.put("L", new int[] {6,7,8});
		
		HashMap<String,int[]> actionValue_D = new HashMap<String,int[]>();
		actionValue_D.put("F", new int[] {6,7,8});
		actionValue_D.put("R", new int[] {6,7,8});
		actionValue_D.put("B", new int[] {6,7,8});
		actionValue_D.put("L", new int[] {6,7,8});
		CubeActionItem cubeAction_D = new CubeActionItem(action_D,seq_D,actionParam_D,actionValue_D);
		mAction.put(action_D, cubeAction_D);
		
		//D' 逆时针方向 F->L->B->R->F
		String action_D1 = "D'";
		String[] seq_D1 = {"F","L","B","R","F"};
		HashMap<String,int[]> actionParam_D1 = new HashMap<String,int[]>();
		actionParam_D1.put("F", new int[] {6,7,8});
		actionParam_D1.put("L", new int[] {6,7,8});
		actionParam_D1.put("B", new int[] {6,7,8});
		actionParam_D1.put("R", new int[] {6,7,8});
		
		HashMap<String,int[]> actionValue_D1 = new HashMap<String,int[]>();
		actionValue_D1.put("F", new int[] {6,7,8});
		actionValue_D1.put("L", new int[] {6,7,8});
		actionValue_D1.put("B", new int[] {6,7,8});
		actionValue_D1.put("R", new int[] {6,7,8});
		CubeActionItem cubeAction_D1 = new CubeActionItem(action_D1,seq_D1,actionParam_D1,actionValue_D1);
		mAction.put(action_D1, cubeAction_D1);
		
		//F 顺时针方向 U->R->D->L->U
		String action_F = "F";
		String[] seq_F = {"U","R","D","L","U"};
		HashMap<String,int[]> actionParam_F = new HashMap<String,int[]>();
		actionParam_F.put("U", new int[] {6,7,8});
		actionParam_F.put("R", new int[] {0,3,6});
		actionParam_F.put("D", new int[] {2,1,0});
		actionParam_F.put("L", new int[] {8,5,2});
		
		HashMap<String,int[]> actionValue_F = new HashMap<String,int[]>();
		actionValue_F.put("U", new int[] {6,7,8});
		actionValue_F.put("R", new int[] {0,3,6});
		actionValue_F.put("D", new int[] {2,1,0});
		actionValue_F.put("L", new int[] {8,5,2});
		CubeActionItem cubeAction_F = new CubeActionItem(action_F,seq_F,actionParam_F,actionValue_F);
		mAction.put(action_F, cubeAction_F);
		
		//F' 逆时针方向 U->L->D->R->U
		String action_F1 = "F'";
		String[] seq_F1 = {"U","L","D","R","U"};
		HashMap<String,int[]> actionParam_F1 = new HashMap<String,int[]>();
		actionParam_F1.put("U", new int[] {6,7,8});
		actionParam_F1.put("L", new int[] {8,5,2});
		actionParam_F1.put("D", new int[] {2,1,0});
		actionParam_F1.put("R", new int[] {0,3,6});
		
		HashMap<String,int[]> actionValue_F1 = new HashMap<String,int[]>();
		actionValue_F1.put("U", new int[] {6,7,8});
		actionValue_F1.put("L", new int[] {8,5,2});
		actionValue_F1.put("D", new int[] {2,1,0});
		actionValue_F1.put("R", new int[] {0,3,6});
		
		CubeActionItem cubeAction_F1 = new CubeActionItem(action_F1,seq_F1,actionParam_F1,actionValue_F1);
		mAction.put(action_F1, cubeAction_F1);
		
		//B 顺时针方向 U->L->D->R->U
		String action_B = "B";
		String[] seq_B = {"U","L","D","R","U"};
		HashMap<String,int[]> actionParam_B = new HashMap<String,int[]>();
		actionParam_B.put("U", new int[] {0,1,2});
		actionParam_B.put("L", new int[] {6,3,0});
		actionParam_B.put("D", new int[] {8,7,6});
		actionParam_B.put("R", new int[] {2,5,8});
		
		HashMap<String,int[]> actionValue_B = new HashMap<String,int[]>();
		actionValue_B.put("U", new int[] {0,1,2});
		actionValue_B.put("L", new int[] {6,3,0});
		actionValue_B.put("D", new int[] {8,7,6});
		actionValue_B.put("R", new int[] {2,5,8});
		
		CubeActionItem cubeAction_B = new CubeActionItem(action_B,seq_B,actionParam_B,actionValue_B);
		mAction.put(action_B, cubeAction_B);
		
		//B' 逆时针方向 U->L->D->R->U
		String action_B1 = "B'";
		String[] seq_B1 = {"U","R","D","L","U"};
		HashMap<String,int[]> actionParam_B1 = new HashMap<String,int[]>();
		actionParam_B1.put("U", new int[] {0,1,2});
		actionParam_B1.put("R", new int[] {2,5,8});
		actionParam_B1.put("D", new int[] {8,7,6});
		actionParam_B1.put("L", new int[] {6,3,0});
		
		HashMap<String,int[]> actionValue_B1 = new HashMap<String,int[]>();
		actionValue_B1.put("U", new int[] {0,1,2});
		actionValue_B1.put("R", new int[] {2,5,8});
		actionValue_B1.put("D", new int[] {8,7,6});
		actionValue_B1.put("L", new int[] {6,3,0});
		CubeActionItem cubeAction_B1 = new CubeActionItem(action_B1,seq_B1,actionParam_B1,actionValue_B1);
		mAction.put(action_B1, cubeAction_B1);
		
		//R 顺时针方向 U->B->D->F->U
		String action_R = "R";
		String[] seq_R = {"U","B","D","F","U"};
		HashMap<String,int[]> actionParam_R = new HashMap<String,int[]>();
		actionParam_R.put("U", new int[] {2,5,8});
		actionParam_R.put("B", new int[] {6,3,0});
		actionParam_R.put("D", new int[] {2,5,8});
		actionParam_R.put("F", new int[] {2,5,8});
		
		HashMap<String,int[]> actionValue_R = new HashMap<String,int[]>();
		actionValue_R.put("U", new int[] {2,5,8});
		actionValue_R.put("B", new int[] {6,3,0});
		actionValue_R.put("D", new int[] {2,5,8});
		actionValue_R.put("F", new int[] {2,5,8});
		CubeActionItem cubeAction_R = new CubeActionItem(action_R,seq_R,actionParam_R,actionValue_R);
		mAction.put(action_R, cubeAction_R);
		
		//R' 逆时针方向 U->F->D->B->U
		String action_R1 = "R'";
		String[] seq_R1 = {"U","F","D","B","U"};
		HashMap<String,int[]> actionParam_R1 = new HashMap<String,int[]>();
		actionParam_R1.put("U", new int[] {2,5,8});
		actionParam_R1.put("F", new int[] {2,5,8});
		actionParam_R1.put("D", new int[] {2,5,8});
		actionParam_R1.put("B", new int[] {6,3,0});
		
		HashMap<String,int[]> actionValue_R1 = new HashMap<String,int[]>();
		actionValue_R1.put("U", new int[] {2,5,8});
		actionValue_R1.put("F", new int[] {2,5,8});
		actionValue_R1.put("D", new int[] {2,5,8});
		actionValue_R1.put("B", new int[] {6,3,0});
		CubeActionItem cubeAction_R1 = new CubeActionItem(action_R1,seq_R1,actionParam_R1,actionValue_R1);
		mAction.put(action_R1, cubeAction_R1);
		
		//L 顺时针方向 U->F->D->B->U
		String action_L = "L";
		String[] seq_L = {"U","F","D","B","U"};
		HashMap<String,int[]> actionParam_L = new HashMap<String,int[]>();
		actionParam_L.put("U", new int[] {0,3,6});
		actionParam_L.put("F", new int[] {0,3,6});
		actionParam_L.put("D", new int[] {0,3,6});
		actionParam_L.put("B", new int[] {8,5,2});
		
		HashMap<String,int[]> actionValue_L = new HashMap<String,int[]>();
		actionValue_L.put("U", new int[] {0,3,6});
		actionValue_L.put("F", new int[] {0,3,6});
		actionValue_L.put("D", new int[] {0,3,6});
		actionValue_L.put("B", new int[] {8,5,2});
		CubeActionItem cubeAction_L = new CubeActionItem(action_L,seq_L,actionParam_L,actionValue_L);
		mAction.put(action_L, cubeAction_L);
		
		//L' 逆时针方向 U->B->D->F->U
		String action_L1 = "L'";
		String[] seq_L1 = {"U","B","D","F","U"};
		HashMap<String,int[]> actionParam_L1 = new HashMap<String,int[]>();
		actionParam_L1.put("U", new int[] {0,3,6});
		actionParam_L1.put("B", new int[] {8,5,2});
		actionParam_L1.put("D", new int[] {0,3,6});
		actionParam_L1.put("F", new int[] {0,3,6});
		
		HashMap<String,int[]> actionValue_L1 = new HashMap<String,int[]>();
		actionValue_L1.put("U", new int[] {0,3,6});
		actionValue_L1.put("B", new int[] {8,5,2});
		actionValue_L1.put("D", new int[] {0,3,6});
		actionValue_L1.put("F", new int[] {0,3,6});
		CubeActionItem cubeAction_L1 = new CubeActionItem(action_L1,seq_L1,actionParam_L1,actionValue_L1);
		mAction.put(action_L1, cubeAction_L1);

	}
	
	public CubeActionItem getAction(String action){
		return mAction.get(action);
	}
}
