/********************************
    通过button控制魔方的整体旋转
*********************************/
package com.rubik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.assist.AppConfig;
import com.logiccube.CubeColorSnapshot;
import com.logiccube.CubeService;


public class KubeActivity extends Activity implements KubeRenderer.AnimationCallback {
    
    private GLWorld mWorld;        //add by leizhong 2015-10-20
    
    private static String mRecoverStep = "";    //还原步骤的字符串变量，是逻辑层次返回的结果 
    private static String mRecoverStepFormated = "";    //还原步骤的字符串变量，是转换了逻辑层返回的结果，用来操作魔方转动
    private static int mRecoverStepNum = 0;    //魔方还原需要的总共的步骤数
    private static int mCurRecoverStep = 0;    //魔方还原的当前步骤
    private static boolean mRecoverResult = false;//魔方还原结果
    private myReceiver mReceiver = null;    //接收魔方还原算法的结果的Receiver
    private boolean isAnimate = false;     //标识魔方当前是否正在转动
    private boolean isAutoTurning = false;        //标识魔方是不是自动转动
    private final int START_RESTORE = 1;    //开始还原
    private final int START_RECOVER = 2;    //回退还原
    private int []index = null; //拍照传递过来的魔方的每个小立方体每个面的颜色数据
    private float []color = null;   //拍照传递过来的是小立方体的颜色数据
    private final int REQUEST_CODE = 0;  //启动Activity请求码
    public static Boolean isRotateByFinger = true;  //判断魔方是不是用手指在转动
    
    private GLWorld makeGLWorld()
    {
        GLWorld world = new GLWorld();
        GLShape.COUNT = 0;
        
        //立方体的坐标，用来绘制魔方的小立方体
        //通过下面的坐标可以看出 小立方体的边长是0.62 立方体之间间距是0.06
        // coordinates for our cubes
        float c0 = -1.0f;
        float c1 = -0.38f;
        float c2 = -0.32f;
        float c3 = 0.32f;
        float c4 = 0.38f;
        float c5 = 1.0f;
        
        //魔方的每个小立方体的绘制
        //从上到下，从后到前，从左到右
        /*
         *     0 1 2
         * 3 4 5
         *6 7 8 
         * */
        //最上层从左到右最后一排
        // top back, left to right
        mCubes[0]  = new Cube(world, c0, c4, c0, c1, c5, c1);
        mCubes[1]  = new Cube(world, c2, c4, c0, c3, c5, c1);
        mCubes[2]  = new Cube(world, c4, c4, c0, c5, c5, c1);
        //最上层从左到右中间一排
        // top middle, left to right
        mCubes[3]  = new Cube(world, c0, c4, c2, c1, c5, c3);
        mCubes[4]  = new Cube(world, c2, c4, c2, c3, c5, c3);
        mCubes[5]  = new Cube(world, c4, c4, c2, c5, c5, c3);
        // top front, left to right
        //-1,0.38,0.38,-0.38,1,1->-0.69,0.69,0.69
        mCubes[6]  = new Cube(world, c0, c4, c4, c1, c5, c5);
        mCubes[7]  = new Cube(world, c2, c4, c4, c3, c5, c5);
        mCubes[8]  = new Cube(world, c4, c4, c4, c5, c5, c5);
        // middle back, left to right
        mCubes[9]  = new Cube(world, c0, c2, c0, c1, c3, c1);
        mCubes[10] = new Cube(world, c2, c2, c0, c3, c3, c1);
        mCubes[11] = new Cube(world, c4, c2, c0, c5, c3, c1);
        // middle middle, left to right
        mCubes[12] = new Cube(world, c0, c2, c2, c1, c3, c3);
        //中间可以不用渲染,不会变点到
        //mCubes[13] = new Cube(world, c2, c2, c2, c3, c3, c3);
        mCubes[13] = null;
        mCubes[14] = new Cube(world, c4, c2, c2, c5, c3, c3);
        // middle front, left to right
        mCubes[15] = new Cube(world, c0, c2, c4, c1, c3, c5);
        mCubes[16] = new Cube(world, c2, c2, c4, c3, c3, c5);
        mCubes[17] = new Cube(world, c4, c2, c4, c5, c3, c5);
        // bottom back, left to right
        mCubes[18] = new Cube(world, c0, c0, c0, c1, c1, c1);
        mCubes[19] = new Cube(world, c2, c0, c0, c3, c1, c1);
        mCubes[20] = new Cube(world, c4, c0, c0, c5, c1, c1);
        // bottom middle, left to right
        mCubes[21] = new Cube(world, c0, c0, c2, c1, c1, c3);
        mCubes[22] = new Cube(world, c2, c0, c2, c3, c1, c3);
        mCubes[23] = new Cube(world, c4, c0, c2, c5, c1, c3);
        // bottom front, left to right
        mCubes[24] = new Cube(world, c0, c0, c4, c1, c1, c5);
        mCubes[25] = new Cube(world, c2, c0, c4, c3, c1, c5);
        mCubes[26] = new Cube(world, c4, c0, c4, c5, c1, c5);

        //初始化排列
        // initialize our permutation to solved position
        mPermutation = new int[27];
        for (int i = 0; i < mPermutation.length; i++)
            mPermutation[i] = i;
        paintCube(mPermutation);
        //添加魔方的所有可见的小立方体到集合中
        for (int i = 0; i < 27; i++){
            if (mCubes[i] != null){
                world.addShape(mCubes[i]);//把魔方除了中间的立方体之外全部的立方体添加到mShapeList
            }
        }
        createLayers();    //创建魔方的各层
        updateLayers();    //更新魔方的各层
        
        world.generate();    //生成魔方的小立方体的顶点和颜色资源
        return world;
    } // end of makeGLWorld

    /**
     * 初始化绘制立方体的各个面的颜色
     */
    private void paintCube(int []Permutation) {
     // paint the sides
        int i, j;
        // set all faces black by default
        //设置魔方的所有小立方体所有的面默认为黑色，除了最中间的一个小立方体
        for (i = 0; i < 27; i++) {
            Cube cube = mCubes[i];
            if (cube != null) {
                for (j = 0; j < 6; j++)
                    cube.setFaceColor(j, GLColor.BLACK);
            }
        }
        //绘制魔方的每个平面的颜色
        // paint top
        for (i = 0; i < 9; i++){
            mCubes[Permutation[i]].setFaceColor(Cube.kTop, GLColor.YELLOW);
        }
        // paint bottom
        for (i = 18; i < 27; i++){
            mCubes[Permutation[i]].setFaceColor(Cube.kBottom, GLColor.WHITE);
        }
        // paint left
        for (i = 0; i < 27; i += 3){
            mCubes[Permutation[i]].setFaceColor(Cube.kLeft, GLColor.BLUE);
        }
        // paint right
        for (i = 2; i < 27; i += 3){
            mCubes[Permutation[i]].setFaceColor(Cube.kRight, GLColor.GREEN);
        }
        // paint back
        for (i = 0; i < 27; i += 9){
            for (j = 0; j < 3; j++){
                mCubes[Permutation[i + j]].setFaceColor(Cube.kBack, GLColor.ORANGE);
            }
        }
        // paint front
        for (i = 6; i < 27; i += 9){
            for (j = 0; j < 3; j++){
                mCubes[Permutation[i + j]].setFaceColor(Cube.kFront, GLColor.RED);
            }
        }
    }
    /**
     * 创建魔方的各层，一共9层
     */
    private void createLayers() {
        mLayers[kUp] = new Layer(Layer.kAxisY,kUp);
        mLayers[kDown] = new Layer(Layer.kAxisY,kDown);
        mLayers[kLeft] = new Layer(Layer.kAxisX,kLeft);
        mLayers[kRight] = new Layer(Layer.kAxisX,kRight);
        mLayers[kFront] = new Layer(Layer.kAxisZ,kFront);
        mLayers[kBack] = new Layer(Layer.kAxisZ,kBack);
        //上层和下层的中间层
        mLayers[kMiddle] = new Layer(Layer.kAxisX,kMiddle);
        //左层和右层的中间层
        mLayers[kEquator] = new Layer(Layer.kAxisY,kEquator);
        //前层和后层的中间层
        mLayers[kSide] = new Layer(Layer.kAxisZ,kSide);
    }

    /**
     * 更新魔方每层的小立方体
     */
    private void updateLayers() {
        Layer layer;    //声明一个魔方的层对象
        GLShape[] shapes;    //声明一个魔方的小立方体的数组
        int i, j, k;

        // up layer
        layer = mLayers[kUp];
        shapes = layer.mShapes;
        for (i = 0; i < 9; i++){    //最上层的小立方体index从0~8
            shapes[i] = mCubes[mPermutation[i]];
        }
        // equator layer
        layer = mLayers[kEquator];
        shapes = layer.mShapes;
        for (i = 9, k = 0; i < 18; i++){    //上册和下层的中间层index从9~17
            shapes[k++] = mCubes[mPermutation[i]];
        }
        // down layer
        layer = mLayers[kDown];
        shapes = layer.mShapes;
        for (i = 18, k = 0; i < 27; i++){    //下层的index从18~26
            shapes[k++] = mCubes[mPermutation[i]];
        }
        // left layer
        layer = mLayers[kLeft];
        shapes = layer.mShapes;
        for (i = 0, k = 0; i < 27; i += 9){    //左层的index是0、3、6、 9、12、15、 18、21、24
            for (j = 0; j < 9; j += 3){
                shapes[k++] = mCubes[mPermutation[i + j]];
            }
        }
        // middle layer
        layer = mLayers[kMiddle];
        shapes = layer.mShapes;
        for (i = 1, k = 0; i < 27; i += 9){    //左层和右层之间的中间层，index是1、4、7、 10、13、16、 19、 22、 25
            for (j = 0; j < 9; j += 3){
                shapes[k++] = mCubes[mPermutation[i + j]];
            }
        }
        // right layer
        layer = mLayers[kRight];
        shapes = layer.mShapes;
        for (i = 2, k = 0; i < 27; i += 9){    //右层，index是2、5、8、 11、14、17、 20、23、26
            for (j = 0; j < 9; j += 3){
                shapes[k++] = mCubes[mPermutation[i + j]];
            }
        }
        // front layer
        layer = mLayers[kFront];
        shapes = layer.mShapes;
        for (i = 6, k = 0; i < 27; i += 9){    //前层，index是6、7、8、 15、16、17、 24、25、26
            for (j = 0; j < 3; j++){
                shapes[k++] = mCubes[mPermutation[i + j]];
            }
        }
        // side layer
        layer = mLayers[kSide];
        shapes = layer.mShapes;
        for (i = 3, k = 0; i < 27; i += 9){ //前层和后层的中间层index是3、4、5、 12、13、14、 21、 22、 23
            for (j = 0; j < 3; j++){
                shapes[k++] = mCubes[mPermutation[i + j]];
            }
        }
        // back layer
        layer = mLayers[kBack];
        shapes = layer.mShapes;
        for (i = 0, k = 0; i < 27; i += 9){    //后层，index是0、1、2、 9、10、11、 18、19、20
            for (j = 0; j < 3; j++){
                shapes[k++] = mCubes[mPermutation[i + j]];
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (j = 0; j < mLayers[kFront].mShapes.length; j++) {    //打印出魔方前面一层的小立方体的编号
            if (mLayers[kFront].mShapes[j] != null) {
                sb.append(mLayers[kFront].mShapes[j].id + ";");
            }
        }
        
        Log.i("GLWorld", "front层包含：" + sb.toString());
    } //end of updateLayers
    //声明按钮控件
    private Button buttonNext = null;
    private Button buttonPrevious = null;
    private Button buttonStart = null;
    private Button buttonPause = null;
    private Button buttonPhotograph = null;
    private Button buttonRecover = null;
    private Button buttonReset = null;
    private View contentView;
    private RelativeLayout layout;
    private TextView textTotal = null;
    private TextView textStep = null;
    private TextView textError = null;
    //弹出显示的窗体
    private PopupWindow popupWindow = new PopupWindow();;

    /**
     * 创建3D视图的界面
     */
    private void create3DView() {
        //old: mRenderer = new KubeRenderer(makeGLWorld(), this);
        if(mView != null) {
            layout.removeView(mView);
        }    
        mWorld = makeGLWorld();
        mRenderer = new KubeRenderer(mWorld, KubeActivity.this);
        mView = new KubeSurfaceView(getApplication(), mRenderer);
        
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, R.id.linear_button);
        layout.addView(mView, params);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        create3DView();
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonPause = (Button) findViewById(R.id.buttonPause);
        
        //BEGIN add by leizhong 2015-10-24
        buttonNext.setOnClickListener(new ButtonClickListener());
        buttonPrevious.setOnClickListener(new ButtonClickListener());
        buttonStart.setOnClickListener(new ButtonClickListener());
        buttonPause.setOnClickListener(new ButtonClickListener());
        //END add by leizhong 2015-10-24
        
        IntentFilter filter = new IntentFilter(CubeService.CUBE_CHANGE_FILTER);
        mReceiver = new myReceiver();
        registerReceiver(mReceiver, filter);
        
        setInitButtonDisable();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mView.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        contentView = LayoutInflater.from(KubeActivity.this).inflate(R.layout.activity_setting, null);
        //获取控件
        buttonPhotograph = (Button) contentView.findViewById(R.id.buttonPhotograph);
        buttonRecover = (Button) contentView.findViewById(R.id.buttonRecover);
        buttonReset = (Button) contentView.findViewById(R.id.buttonReset);
        //按钮绑定点击事件监听器
        buttonPhotograph.setOnClickListener(new SettingsButtonClickListener());
        buttonRecover.setOnClickListener(new SettingsButtonClickListener());
        buttonReset.setOnClickListener(new SettingsButtonClickListener());
        
        buttonRecover.setEnabled(false);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch(item.getItemId()) {
        case R.id.action_settings:
            if(!popupWindow.isShowing()) {
                //显示popupwindow
                showPopupWindow();
            }
            else {
                //隐藏popupwindow
                popupWindow.dismiss();
            }
            break;
        case R.id.action_quit:
            //显示确认退出的对话框
            showQuitDialog();
            break;
        }
        return true;
    }

    /**
     * 获取Intent传递过来的数据，存储在手机上，并根据数据生成魔方
     * @param data
     */
    private void getIntentData(Intent data) {
        if(data == null) {
            return ;
        }
        int[] ind = data.getIntArrayExtra("index");        //获取拍照传递过来的魔方的各个面的颜色数据
        float [] col = data.getFloatArrayExtra("color");    //颜色数组
        
        if(ind != null && col != null) {    //获取到拍照传递过来的数据，再转换到数组中
            index = ind;
            color = col;
            
            String sIndex = "";
            String sColor = "";
            for(int i : index) {
                sIndex += i + ",";
            }
            for(float f : color) {
                sColor += f + ",";
            }
            
            Log.d("ZLinfo", "index:" + sIndex);
            Log.d("ZLinfo", "color:" + sColor);
            
            formatData(index, color);    //把ind和col转换成colors 和 cubeInts数组
            createCubeForPhoto(cubeInts, colors);    //传递拍照生成的数组
            createLogicCube(ind);    //在逻辑层创建魔方
        }
        else {
//            Toast.makeText(KubeActivity.this, R.string.get_photo_failed, Toast.LENGTH_SHORT).show();
            setInitButtonDisable();
        }
    }
    
    /**
     * 把ind和col转换成colors 和 cubeInts数组
     */
    private void formatData(int []ind, float col[]) {
        int i, j = 0;
        for(i = 0; i < col.length / 4; i++,j++) {
            colors[i] = new GLColor(col[i+3*j], col[1+i+3*j], col[2+i+3*j], col[3+i+3*j]);
        }
        
        int k = 0;
        for( i = 0; i < 6; i++) {
            for(j = 0; j < 9; j++,k++) {
                cubeInts[i][j] = ind[k];
            }
        }
    }
    /**
     * 在逻辑层创建魔方
     */
    private void createLogicCube(int[] ind){
        Log.d("createLogicCube","createLogicCube");
        String[][] cubeTwoArray = new String[6][9];
        for( int i = 0,k=0; i < 6; i++) {
            for(int j = 0; j < 9; j++,k++) {
                cubeTwoArray[i][j] = ind[k] + "";
            }
        }
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), CubeService.class);
        intent.putExtra(CubeService.ACTION_KEY, CubeService.ACTION_INITIAL);

        intent.putExtra(CubeService.ACTION_INITIAL_VALUE_KEY, new CubeColorSnapshot(cubeTwoArray).getmCubeStrOneArray());
        getApplicationContext().startService(intent);
        Log.d("createLogicCube","startService");
        mRecoverStep = "";
        mRecoverStepNum = 0;
        
        Intent intentStart = new Intent();
        intentStart.setClass(getApplicationContext(), CubeService.class);
        intentStart.putExtra(CubeService.ACTION_KEY, CubeService.ACTION_START);
        getApplicationContext().startService(intentStart);
    }
    
    private class myReceiver extends BroadcastReceiver{
        private Intent mIntent = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            mIntent = intent;
            String action = mIntent.getStringExtra(CubeService.ACTION_KEY);
            Log.d("myReceiver", "receive action:"+action);
            if(CubeService.ACTION_INVALID_CUBE_VALUE_KEY.equals(action)){ //拍照获取的魔方是错误的
                mCurRecoverStep = 0;
                mRecoverStep = "";
                mRecoverStepNum = 0;
                mRecoverStepFormated = "";
                index = null;
                color = null;
                setTextShowCubeError();
                setInitButtonDisable();
                Log.d("myReceiver", "mRecoverResult:"+mRecoverResult);
                Log.d("myReceiver", "mRecoverStep: " + mRecoverStep);
                Log.d("myReceiver", "mRecoverStepNum: " + mRecoverStepNum);
                Log.d("myReceiver", "mRecoverStepFormated: " + mRecoverStepFormated);
            } 
            else if(CubeService.ACTIVITY_ACTION_UPDATE_CUBE.equals(action)){
                //现在测试会接收到两次CubeService_ACTIVITY_ACTION_UPDATE_CUBE，第一次的mRecoverStepNum是0
                mRecoverResult = mIntent.getBooleanExtra(CubeService.CUBE_RECOVER_VALUE_KEY, false);
                if(mRecoverResult) {
                    mCurRecoverStep = 0;
                    mRecoverStep = mIntent.getStringExtra(CubeService.CUBE_HISTORY_VALUE_KEY);
                    mRecoverStepNum = mIntent.getIntExtra(CubeService.CUBE_CUR_STEP_NUM_VALUE_KEY, 0);
                    getMyStepStr();
                    setTextShowStep();
                    setButtonEnable(true);
                    Log.d("myReceiver", "mRecoverResult:"+mRecoverResult);
                    Log.d("myReceiver", "mRecoverStep: " + mRecoverStep);
                    Log.d("myReceiver", "mRecoverStepNum: " + mRecoverStepNum);
                    Log.d("myReceiver", "mRecoverStepFormated: " + mRecoverStepFormated);
                    return;
                } else {
                    Toast.makeText(KubeActivity.this, R.string.restore_rubik_failed, Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
    }
    
    /**
     * 转换获取到的魔方还原步骤成我自定义的字符串
     */
    private void getMyStepStr(){
        mRecoverStepFormated = mRecoverStep + "";
        //逆时针
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("F'", "0");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("B'", "2");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("L'", "4");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("R'", "6");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("U'", "8");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("D'", "B");
        //顺时针
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("F", "1");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("B", "3");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("L", "5");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("R", "7");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("U", "9");
        mRecoverStepFormated = mRecoverStepFormated.replaceAll("D", "A");

    }
    
    /**
     * 添加显示布数的控件
     */
    private void setTextShowStep() {
        if(textTotal == null || textStep == null) {
            textTotal = new TextView(KubeActivity.this);
            textStep = new TextView(KubeActivity.this);
            
            textTotal.setTextSize(20);
            textStep.setTextSize(20);
            LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.ABOVE, R.id.linear_button);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            
            params2.addRule(RelativeLayout.ABOVE, R.id.linear_button);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layout.addView(textTotal, params1);
            layout.addView(textStep, params2);
        }
        String totalStepNum = getApplicationContext().getResources().getString(R.string.total_step_count);
        String currentStepNum = getApplicationContext().getResources().getString(R.string.current_step_count);
        textTotal.setText(totalStepNum + mRecoverStepNum);
        textStep.setText(currentStepNum + mCurRecoverStep);
    }
    /**
     * 当魔方拍照生成还原步骤的时候出现错误来显示提示信息
     */
    private void setTextShowCubeError() {
        if(textError == null) {
            textError = new TextView(KubeActivity.this);
            
            textError.setTextSize(20);
            textError.setTextColor(Color.RED);
            LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.ABOVE, R.id.linear_button);
            params1.addRule(RelativeLayout.CENTER_IN_PARENT);
            
            layout.addView(textError, params1);
        }
        textError.setText(R.string.string_take_photo_error);
        setInitButtonDisable();
    }
    
    /**
     * 设置初始化的时候button的disable状态
     * 此时的button的状态也是错误魔方的状态
     */
    private void setInitButtonDisable() {
        buttonNext.setEnabled(false);
        buttonPrevious.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonPause.setEnabled(false);
    }
    /**
     * 显示退出应用对话框
     */
    private void showQuitDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(KubeActivity.this);
        ab.setTitle("退出")
        .setMessage("确定退出应用么？")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
        .setNegativeButton("取消", null)
        .create()
        .show();
    }

    /**
     * 显示PopupWindow
     */
    private void showPopupWindow() {
        //新建popupwindow窗口
        popupWindow.setContentView(contentView);
        popupWindow.setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);        //设置popupwindow可点击
        popupWindow.showAsDropDown(findViewById(R.id.action_settings));    //在action_setting下面显示显示popupwindow
    }
    
    /**
     * 用于线程之间传递信息
     */
    private Handler myHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == START_RESTORE) {
                restoreKube();
            }
            else if(msg.what == START_RECOVER) {
                recoverKube();
            }
        };
    };
    
    /**
     * 设置按钮是否可点击
     */
    private void setButtonEnable(Boolean isEnable) {
        if(buttonPhotograph != null) {
            buttonPhotograph.setEnabled(isEnable);   
        }
        if(buttonRecover != null) {
            buttonRecover.setEnabled(isEnable);   
        }
        if(buttonReset != null) {
            buttonReset.setEnabled(isEnable);   
        }
        buttonNext.setEnabled(isEnable);
        buttonPrevious.setEnabled(isEnable);
        buttonStart.setEnabled(isEnable);
        buttonPause.setEnabled(!isEnable);
    }
    
    /**
     * 魔方的各个面的颜色的数据 魔方的面的排序是 F R B L U D
     * 各个小立方体的编号是从左到右从上到 0～8
     */
    int [][] cubeInts = new int[6][9];
    /**
     * 存储魔方的六面的颜色值
     */
    GLColor [] colors = new GLColor[6];
    
    /**
     * 通过照片来生成魔方 add by leizhong 2015-10-20
     */
    public void createCubeForPhoto(int [][]cubeInts, GLColor []colors) {
        
        //绘制魔方各个表面的颜色
        int i,j,k;
        Cube []cubeArray = new Cube[27];
        for(i = 0; i < mCubes.length; i++) {
            cubeArray[i] = mCubes[i];
        }
        int index[] = new int[9];    //需要对拍照传递过来的数组进行转换才能在魔方上面使用
        // paint top
        for (i = 0; i < 9; i++){
            cubeArray[mPermutation[i]].setFaceColor(Cube.kTop, colors[cubeInts[4][i]]);
        }
        index[0] = cubeInts[5][6];
        index[1] = cubeInts[5][7];
        index[2] = cubeInts[5][8];
        index[3] = cubeInts[5][3];
        index[4] = cubeInts[5][4];
        index[5] = cubeInts[5][5];
        index[6] = cubeInts[5][0];
        index[7] = cubeInts[5][1];
        index[8] = cubeInts[5][2];
        // paint bottom
        for (i = 18,k =0; i < 27; i++,k++){
            cubeArray[mPermutation[i]].setFaceColor(Cube.kBottom, colors[index[k]]);
        }
        // paint left
        for (i = 0,k = 0; i < 27; i += 3, k++){
            cubeArray[mPermutation[i]].setFaceColor(Cube.kLeft, colors[cubeInts[3][k]]);
        }
        index[0] = cubeInts[1][2];
        index[1] = cubeInts[1][1];
        index[2] = cubeInts[1][0];
        index[3] = cubeInts[1][5];
        index[4] = cubeInts[1][4];
        index[5] = cubeInts[1][3];
        index[6] = cubeInts[1][8];
        index[7] = cubeInts[1][7];
        index[8] = cubeInts[1][6];
        // paint right
        for (i = 2, k = 0; i < 27; i += 3, k++){
            cubeArray[mPermutation[i]].setFaceColor(Cube.kRight, colors[index[k]]);
        }
        index[0] = cubeInts[2][2];
        index[1] = cubeInts[2][1];
        index[2] = cubeInts[2][0];
        index[3] = cubeInts[2][5];
        index[4] = cubeInts[2][4];
        index[5] = cubeInts[2][3];
        index[6] = cubeInts[2][8];
        index[7] = cubeInts[2][7];
        index[8] = cubeInts[2][6];
        // paint back
        for (i = 0, k = 0; i < 27; i += 9){
            for (j = 0; j < 3; j++, k++){
                cubeArray[mPermutation[i + j]].setFaceColor(Cube.kBack, colors[index[k]]);
            }
        }
        // paint front
        for (i = 6, k = 0; i < 27; i += 9){
            for (j = 0; j < 3; j++, k++){
                cubeArray[mPermutation[i + j]].setFaceColor(Cube.kFront, colors[cubeInts[0][k]]);
            }
        }
//        mWorld.createCubeImage();
    }
    PowerManager mPm;
    PowerManager.WakeLock mWakeLock;
    /**
     * 设置屏幕的亮灭
     */
    private void setScreenWakeUp() {
        if(mPm == null) {
            mPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        }
        if(mWakeLock == null) {
            mWakeLock = mPm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag"); //屏幕常亮
        }
        new Thread(){
            public void run() {
                while(isAutoTurning) {
                    if(!mWakeLock.isHeld()) {
                        mWakeLock.acquire();    //获得屏幕常亮
                    }
                }
                SystemClock.sleep(500);
                mWakeLock.release();    //释放屏幕常亮
            };
        }.start();
    }
    
    /**
     * 按照识别算法返回的步骤还原魔方
     */
    private void restoreKube() {
        if(!mRecoverStepFormated.equals("")) {
            if(mCurRecoverStep < mRecoverStepFormated.length()) {
                char ch = mRecoverStepFormated.charAt(mCurRecoverStep++);
                changeCharToTurn(ch);
                setTextShowStep();
            }
            else {
                Toast.makeText(KubeActivity.this, R.string.restore_rubik_completed, Toast.LENGTH_SHORT).show();
                isAutoTurning = false;
                setButtonEnable(true);
            }
        }
    }
    
    /**
     * 魔方还原步骤的回退
     */
    private void recoverKube() {
        if(!mRecoverStepFormated.equals("")) {
            if(mCurRecoverStep > 0) {
                char ch = mRecoverStepFormated.charAt(--mCurRecoverStep);
                switch(ch) {
                case '0':
                    ch = '1'; break;
                case '1':
                    ch = '0'; break;
                case '2':
                    ch = '3'; break;
                case '3':
                    ch = '2'; break;
                case '4':
                    ch = '5'; break;
                case '5':
                    ch = '4'; break;
                case '6':
                    ch = '7'; break;
                case '7':
                    ch = '6'; break;
                case '8':
                    ch = '9'; break;
                case '9':
                    ch = '8'; break;
                case 'A':
                    ch = 'B'; break;
                case 'B':
                    ch = 'A'; break;
                }
                changeCharToTurn(ch);
                setTextShowStep();
            }
            else {
                Toast.makeText(KubeActivity.this, R.string.rubik_is_in_initialization, Toast.LENGTH_SHORT).show();
                isAutoTurning = false;
                setButtonEnable(true);
            }
        }
    }
    
    /**
     * 重置魔方到拍照后的初始状态，即时转换
     */
    private void resetKube() {
        if(index != null && color != null) {
            int[] ind = index;
            float []col = color;
            layout.removeView(textTotal);
            layout.removeView(textStep);
            layout.removeView(textError);
            textTotal = null;
            textStep = null;
            textError = null;
            create3DView();
            formatData(ind,col);    //把ind和col转换成colors 和 cubeInts数组
            createCubeForPhoto(cubeInts, colors);    //传递拍照生成的数组
            createLogicCube(ind);    //在逻辑层创建魔方
        } else {
            Toast.makeText(KubeActivity.this, R.string.get_sharedpreference_failed, Toast.LENGTH_SHORT).show();
            setInitButtonDisable();
        }
    }
    
    /**
     * 识别魔方还原步骤中的字符，并转换成相应的旋转操作
     * @param c
     */
    private void changeCharToTurn(char c) {
//        逆时针
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("F'", "0");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("B'", "2");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("L'", "4");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("R'", "6");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("U'", "8");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("D'", "B");
//        顺时针
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("F", "1");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("B", "3");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("L", "5");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("R", "7");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("U", "9");
//        mRecoverStepFormated = mRecoverStepFormated.replaceAll("D", "A");
        
        switch(c) {
        case '0' :
            layerID = 4;
            turningDirection = true;
            break;
        case '1' :    //front层顺时针
            layerID = 4;
            turningDirection = false;
            break;
        case '2' :
            layerID = 5;
            turningDirection = false;
            break;
        case '3' :
            layerID = 5;
            turningDirection = true;
            break;
        case '4' :
            layerID = 2;
            turningDirection = false;
            break;
        case '5' :
            layerID = 2;
            turningDirection = true;
            break;
        case '6' :
            layerID = 3;
            turningDirection = true;
            break;
        case '7' :
            layerID = 3;
            turningDirection = false;
            break;
        case '8' :
            layerID = 0;
            turningDirection = false;
            break;
        case '9' :
            layerID = 0;
            turningDirection = true;
            break;
        case 'A' :
            layerID = 1;
            turningDirection = true;
            break;
        case 'B' :
            layerID = 1;
            turningDirection = false;
            break;
        }
    }
    
    /**
     * 重写KubeRenderer中的AnimationCallback接口的方法
     * 执行魔方的某一层的旋转
     * */
    public void animate() {
        if (layerID == -1) {
            return;
        }
        if (mCurrentLayer == null) {
            mCurrentLayer = mLayers[layerID];
            
            //mLayerPermutations表示旋转位置，具体位置的方块在每次旋转后改变
            if (turningDirection) {    //当触摸行为在x或y方向上增加
                mCurrentLayerPermutation = mLayerCCWPermutations[layerID];
            }
            else{    //当触摸行为在x或y方向上减少
                mCurrentLayerPermutation = mLayerCWPermutations[layerID];
            }
            //这一层开始执行动画
            mCurrentLayer.startAnimation();
            
            int count = 1;
            
            mCurrentAngle = 0;
            
             if (turningDirection) {    //当触摸行为在x或y方向上增加
                mAngleIncrement = (float)Math.PI / 50;    //每次要旋转的角度值
                   mEndAngle = mCurrentAngle + ((float)Math.PI * count) / 2f;    //pi / 2 = 90度
               } else {        //当触摸行为在x或y方向上减少
                mAngleIncrement = -(float)Math.PI / 50;
                   mEndAngle = mCurrentAngle - ((float)Math.PI * count) / 2f;
            }
        } //end of if (mCurrentLayer == null)

         mCurrentAngle += mAngleIncrement;    //角度值一点一点的增加
         if ((mAngleIncrement > 0f && mCurrentAngle >= mEndAngle)
                 || (mAngleIncrement < 0f && mCurrentAngle <= mEndAngle)) {    //旋转到位
             mCurrentLayer.setAngle(mEndAngle);
             mCurrentLayer.endAnimation();
             mCurrentLayer = null;
             layerID=-1;
             
             isAnimate = false;        //add by leizhong 2015-10-25
             
             //基于已经完成旋转指定角度的层调整mPermutation的排列
             // adjust mPermutation based on the completed layer rotation
             int[] newPermutation = new int[27];
             for (int i = 0; i < 27; i++) {
                newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];
             }
             mPermutation = newPermutation;
             String s = "";
             for(int i = 0; i < mPermutation.length; i++) {
                 s += mPermutation[i] + ",";
             }
             Log.d("GLWorld", "mPermutation: " + s);
             
             updateLayers();    //更新各层中小立方体
             AppConfig.Turning = false;
            
             Log.d("GLWorld","当前上层现有方块：" +  mLayers[kUp].toString());
             Log.d("GLWorld","当前中层现有方块：" +  mLayers[kEquator].toString());
             Log.d("GLWorld","当前下层现有方块：" +  mLayers[kDown].toString());
             
             mRenderer.clearPickedCubes();
         } else {    //旋转中
             isAnimate = true;    //add by leizhong 2015-10-25
             mCurrentLayer.setAngle(mCurrentAngle);    //旋转
         }
    }    //end of animate()

    GLSurfaceView mView = null;
    KubeRenderer mRenderer = null;
    Cube[] mCubes = new Cube[27];    //魔方中一共有9 * 3个小立方体
    /**
     * 魔方总共有9层
     */
    Layer[] mLayers = new Layer[9];
    // permutations corresponding to a  pi/2 clockwise rotation of each layer about its axis
    
    /**
     * 从初始状态经过一次旋转之后小立方体的当前排列情况
     * 跟每一层方块位置进行编号(由上至下),旋转后坐标一定要写对，否则坐标保存的与看到的不同
     * 0 1 2                     2 5 8
     * 3 4 5 ->顺时针旋转90度->    1 4 7
     * 6 7 8                    0 3 6
     * */
    static int[][] mLayerCWPermutations = {
        // permutation for UP layer 最上层顺时针旋转90度后布局
        { 2, 5, 8, 1, 4, 7, 0, 3, 6, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 },
        // permutation for DOWN layer 最下层顺时针旋转90度后布局
        { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 23, 26, 19, 22, 25, 18, 21, 24 },
        // permutation for LEFT layer 左侧旋转90度
        { 6, 1, 2, 15, 4, 5, 24, 7, 8, 3, 10, 11, 12, 13, 14, 21, 16, 17, 0, 19, 20, 9, 22, 23, 18, 25, 26 },
        // permutation for RIGHT layer 右侧旋转90度
        { 0, 1, 8, 3, 4, 17, 6, 7, 26, 9, 10, 5, 12, 13, 14, 15, 16, 23, 18, 19, 2, 21, 22, 11, 24, 25, 20 },
        // permutation for FRONT layer 前面旋转90度
        { 0, 1, 2, 3, 4, 5, 24, 15, 6, 9, 10, 11, 12, 13, 14, 25, 16, 7, 18, 19, 20, 21, 22, 23, 26, 17, 8 },
        // permutation for BACK layer 后面旋转90度
        { 18, 9, 0, 3, 4, 5, 6, 7, 8, 19, 10, 1, 12, 13, 14, 15, 16, 17, 20, 11, 2, 21, 22, 23, 24, 25, 26 },
        // permutation for MIDDLE layer （中间面绕X轴旋转90度）
        { 0, 7, 2, 3, 16, 5, 6, 25, 8, 9, 4, 11, 12, 13, 14, 15, 22, 17, 18, 1, 20, 21, 10, 23, 24, 19, 26 },
        // permutation for EQUATOR layer (中间绕Y轴旋转90度)
        { 0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 10, 13, 16, 9, 12, 15, 18, 19, 20, 21, 22, 23, 24, 25, 26 },
        // permutation for SIDE layer (中间绕Z轴旋转90度)
        { 0, 1, 2, 21, 12, 3, 6, 7, 8, 9, 10, 11, 22, 13, 4, 15, 16, 17, 18, 19, 20, 23, 14, 5, 24, 25, 26 }
};
    
    /**
     * 跟每一层方块位置进行编号(由上至下)旋转后坐标一定要写对，
     * 否则坐标保存的与看到的不同(则触摸后方块旋转就有问题了，不该旋转的会旋转)
     * 0 1 2                     6 3 0
     * 3 4 5 ->逆时针旋转90度->    7 4 1
     * 6 7 8                    8 5 2
     * */
    static int[][] mLayerCCWPermutations = {     
            // permutation for UP layer 最上层逆时针旋转90度后布局
            { 6, 3, 0, 7, 4, 1, 8, 5, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 },
            // permutation for DOWN layer 最下层逆时针旋转90度后布局
            { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 24, 21, 18, 25, 22, 19, 26, 23, 20},
            // permutation for LEFT layer 左侧
            { 18, 1, 2, 9, 4, 5, 0, 7, 8, 21, 10, 11, 12, 13, 14, 3, 16, 17, 24, 19, 20, 15, 22, 23, 6, 25, 26 },
            // permutation for RIGHT layer 右侧
            { 0, 1, 20, 3, 4, 11, 6, 7, 2, 9, 10, 23, 12, 13, 14, 15, 16, 5, 18, 19, 26, 21, 22, 17, 24, 25, 8 },
            // permutation for FRONT layer 前面
            { 0, 1, 2, 3, 4, 5, 8, 17, 26, 9, 10, 11, 12, 13, 14, 7, 16, 25, 18, 19, 20, 21, 22, 23, 6, 15, 24 },
            // permutation for BACK layer 后面
            { 2, 11, 20, 3, 4, 5, 6, 7, 8, 1, 10, 19, 12, 13, 14, 15, 16, 17, 0, 9, 18, 21, 22, 23, 24, 25, 26 },
            // permutation for MIDDLE layer （中间面绕X轴逆时针旋转）
            { 0, 19, 2, 3, 10, 5, 6, 1, 8, 9, 22, 11, 12, 13, 14, 15, 4, 17, 18, 25, 20, 21, 16, 23, 24, 7, 26 },
            // permutation for EQUATOR layer (中间绕Y轴逆时针旋转)
            { 0, 1, 2, 3, 4, 5, 6, 7, 8, 15, 12, 9, 16, 13, 10, 17, 14, 11, 18, 19, 20, 21, 22, 23, 24, 25, 26 },
            // permutation for SIDE layer    (中间绕Z轴逆时针旋转)
            { 0, 1, 2, 5, 14, 23, 6, 7, 8, 9, 10, 11, 4, 13, 22, 15, 16, 17, 18, 19, 20, 3, 12, 21, 24, 25, 26 }
    };
    /**
     * current permutation of starting position
     * 目前起始位置的排列
     * mPermutation在makeGLWorld方法中初始化为存储自己角标的数组
     */
    public int[] mPermutation;
    /**
     * 正在旋转的层号
     */
    int layerID = -1;
    /**
     * 需要旋转的方向
     */
    boolean turningDirection = false;
    /**
     * 正在旋转的层
     */
    Layer mCurrentLayer = null;
    /**
     * 当前旋转层的现在的角度和最后的角度
     */
    float mCurrentAngle, mEndAngle;
    /**
     * 增加的角度值
     */
    float mAngleIncrement;
    /**
     * 当前层的排列
     */
    int[] mCurrentLayerPermutation;
    /**
     * 魔方的9层的层号
     */
    static final int kUp = 0;
    static final int kDown = 1;
    static final int kLeft = 2;
    static final int kRight = 3;
    static final int kFront = 4;
    static final int kBack = 5;
    static final int kMiddle = 6;
    static final int kEquator = 7;
    static final int kSide = 8;
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQUEST_CODE) {
            layout.removeView(textTotal);
            layout.removeView(textStep);
            layout.removeView(textError);
            textTotal = null;
            textStep = null;
            textError = null;
            create3DView();
            getIntentData(data);
        }
    }

    /**
     * 按键点击的监听类
     */
    class ButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            switch(arg0.getId()) {
            case R.id.buttonNext:
                if(!isAnimate) {    //当魔方没有在旋转的时候，点击按钮才触发方法
                    restoreKube();
                }
                break;
            case R.id.buttonPrevious:
                if(!isAnimate) {    //当魔方没有在旋转的时候，点击按钮才触发方法
                    recoverKube();
                }
                break;
            case R.id.buttonStart:  //开始还原
                if(!isAutoTurning) {    //魔方当前不是自动还原状态
                    isAutoTurning = true;
                    setButtonEnable(false);
                    setScreenWakeUp();
                    new Thread(){
                        public void run() {
                            while(isAutoTurning) {
                                if(!isAnimate) {
                                    myHandler.sendEmptyMessage(START_RESTORE);
                                    SystemClock.sleep(500);
                                }
                            }
                        };
                    }.start();
                }
                break;
            case R.id.buttonPause:  //暂停还原
                if(isAutoTurning) {
                    isAutoTurning = false;
                    setButtonEnable(true);
                }
                break;
            }
            if(popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }
    
    /**
     *  菜单项按键的点击监听事件
     */
    class SettingsButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch(arg0.getId()) {
            case R.id.buttonPhotograph:
                Intent intent = new Intent();
                intent.setClassName("com.hucheng.ui", "com.hucheng.ui.CaptureActivity");
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.buttonRecover:  //重置魔方到初始状态
                if(!isAutoTurning) {
                    isAutoTurning = true;
                    setButtonEnable(false);
                    setScreenWakeUp();
                    new Thread(){
                        public void run() {
                            while(isAutoTurning) {
                                if(!isAnimate) {
                                    myHandler.sendEmptyMessage(START_RECOVER);
                                    SystemClock.sleep(500);
                                }
                            }
                        };
                    }.start();
                }
                break;
            case R.id.buttonReset:
                resetKube();
                break;
            }
            if(popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        super.onBackPressed();
        showQuitDialog();
    }
    
}