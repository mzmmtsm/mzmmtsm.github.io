package com.example.magicmirror.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.magicmirror.R;
import com.example.magicmirror.utils.SetBrightness;
import com.example.magicmirror.view.DrawView;
import com.example.magicmirror.view.FunctionView;
import com.example.magicmirror.view.PictrueView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,SeekBar.OnSeekBarChangeListener,View.OnTouchListener,View.OnClickListener,FunctionView.onFunctionViewItemClickListener {
    private static final String TAG=MainActivity.class.getSimpleName();  //获得雷鸣
    private  SurfaceHolder holder;  //用于控制SurfaceView显示的内容
    private SurfaceView surfaceView;    //显示相机拍摄的内容
    private PictrueView pictrueView;    //效果自定义View
    private FunctionView functionView;  //标题栏类声明
    private SeekBar seekBar;    //控制焦距滑动条
    private ImageView add,minus;    //控制焦距按钮
    private LinearLayout bottom;    //调节焦距的按钮
    private ImageView save;     //保存图片的按钮
    private ProgressDialog dialog;      //弹窗
    private DrawView drawView;      //绘画类

//    摄像头操作将使用到的变量
    private boolean haveCamera;     //是否有相机设备
    private int mCurrentCamIndex;       //相机的指数
    private int ROTATE;     //旋转值
    private int minFocus;       //当前手机默认的焦距
    private int maxFocus;       //当前手机的最大焦距
    private int everyFocus;     //用于调整焦距
    private int nowFocus;       //当前的焦距值
    private Camera camera;      //声明相机变量

    private int frame_index;    //镜框类型
    private int[] frame_index_ID;   //图片资源ID的数组
    private static final  int PHOTO=1;  //镜框请求值

    private int brightnessValue;    //屏幕亮度值
    private boolean isAutoBrightness;   //屏幕是否为自动调节
    private int SegmentLengh;   //把亮度分为8段，每段为256的1/8






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setViews();
        frame_index=0;
        frame_index_ID=new int[] {R.mipmap.mag_0001,R.mipmap.mag_0003,R.mipmap.mag_0005,
                R.mipmap.mag_0006,R.mipmap.mag_0007,R.mipmap.mag_0008,R.mipmap.mag_0009,
                R.mipmap.mag_0011,R.mipmap.mag_0012,R.mipmap.mag_0014};
        getBrightnessFromWindow();  //获取屏幕亮度的相关属性
    }

    /**
     * 初始化方法，给变量赋值
     */
    private void initViews(){
        surfaceView=(SurfaceView) findViewById(R.id.surface);       //
        pictrueView = (PictrueView) findViewById(R.id.picture);     //
        functionView = (FunctionView) findViewById(R.id.function);      //
        seekBar = (SeekBar) findViewById(R.id.seekbar);     //
        add = (ImageView) findViewById(R.id.add);       //获取add焦距放大控件
        minus=(ImageView)findViewById(R.id.minus);      //
        bottom = (LinearLayout) findViewById(R.id.bottom_bar);      //获取布局文件中底部线性布局
        drawView = (DrawView) findViewById(R.id.draw_glasses);      //获取布局文件中擦屏控件
    }

    /**
     * 判断手机是否有摄像头
     */
    private boolean checkCameraHardware(){
        //判断是否有摄像头
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 打开前置摄像头
     */
    private Camera openFrontFacingCameraGingerbread(){
        int cameraCount;                                            //摄像头个数
        Camera mCamera=null;                                        //声明相机变量
        Camera.CameraInfo cameraInfo=new Camera.CameraInfo();       //创建相机对象
        cameraCount = Camera.getNumberOfCameras();                  //获取摄像头个数
        for (int camIdx=0; camIdx < cameraCount;camIdx++){
            Camera.getCameraInfo(camIdx,cameraInfo);                //获取相机对象
            //判断是否是前置摄像头
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                try {
                    mCamera=Camera.open(camIdx);                    //打开摄像头
                    mCurrentCamIndex=camIdx;                        //设置摄像头id
                }catch (RuntimeException e){
                    Log.e(TAG,"相机打开失败："+e.getLocalizedMessage());
                }
            }
        }
        return mCamera; //返回相机对象
    }

    /**
     * 设置摄像头方向
     *  因为摄像机默认为横向，需要将其竖向
     */
    private void setCameraDisplayOrientation(Activity activity,int cameraId,Camera camera){
        Camera.CameraInfo info=new Camera.CameraInfo();         //实例化相机对象
        Camera.getCameraInfo(cameraId,info);                    //获得相机对象
        //获得旋转角度
        int rotaton = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees =0;

        switch(rotaton){
            case 0:         //未旋转
                degrees=0;
                break;
            case 90:
                degrees=90;
                break;
            case 180:
                degrees=180;
                break;
            case 270:
                degrees=270;
                break;
        }

        int result=0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            //前置摄像头旋转算法
            result = (info.orientation - degrees +360)%360;
            result=(360 - result)%360;
        }else {
            //后置摄像头旋转算法
            result = (info.orientation - degrees+360)%360;
        }

        ROTATE=result + 180;
        camera.setDisplayOrientation(result);   //设置摄像机拍摄角度
    }

    /**
     * 设置摄像头
     */
    private void setCamera(){
        if (checkCameraHardware()){
            camera=openFrontFacingCameraGingerbread();          //打开前置摄像头
            //设置摄像头方向，调整摄像头旋转90度，成竖屏
            setCameraDisplayOrientation(this,mCurrentCamIndex,camera);      //设置相机的相关参数
            Camera.Parameters parameters=camera.getParameters();
            parameters.setPictureFormat(ImageFormat.JPEG);      //设置图片格式化JPEG
            List<String> list=parameters.getSupportedFocusModes();  // 获取支持的对焦模式
            for (String str:list){
                Log.e(TAG,"支持的对焦的模式"+str);
            }
            //手机支持的图片尺寸集合
            List<Camera.Size> pictureList=parameters.getSupportedPictureSizes();
            //手机支持的预览尺寸集合
            List<Camera.Size> previewList=parameters.getSupportedPreviewSizes();
            //设置为当前使用手机的最大尺寸
            parameters.setPictureSize(pictureList.get(0).width,pictureList.get(0).height);
            //设置为当前使用手机的最大预览尺寸
            parameters.setPreviewSize(previewList.get(0).width,previewList.get(0).height);
            minFocus = parameters.getZoom();        //获取最小焦距
            maxFocus = parameters.getMaxZoom();     //获取最大焦距
            everyFocus =1;         //每次增加及减少值
            nowFocus = minFocus;   //当前焦距为最小值
            seekBar.setMax(maxFocus);   //设置最大焦距
            Log.e(TAG,"当前镜头距离："+minFocus+"\t\t获取最大距离："+ maxFocus); //日志打印
            camera.setParameters(parameters);       //设置相机参数

        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.e("surfaceCreated","绘制开始");
        try {
            setCamera();;                       //设置摄像头
            camera.setPreviewDisplay(holder);   //设置预览显示的holder对象
            camera.startPreview();              //  开始预览
        } catch (IOException e) {
            camera.release();   //  相机释放
            camera=null;        //  清空对象
            e.printStackTrace();

        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.e("surfaceCreated","绘制改变");
        try {
            camera.stopPreview();   //相机停止预览
            camera.setPreviewDisplay(holder);       // 设置相机预览显示区域
            camera.startPreview();      //相机启动预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.e("surfaceDestroyed","绘制结束");
        toRelease();
    }

    /**
     * 释放相机资源
     */
    private void toRelease(){
        camera.setPreviewCallback(null);    //
        camera.stopPreview();
        camera.release();
        camera=null;
    }

    private void setViews(){
        holder=surfaceView.getHolder();
        holder.addCallback(this);
        add.setOnTouchListener(this);   //放大焦距
        minus.setOnTouchListener(this);     //缩小焦距
        seekBar.setOnSeekBarChangeListener(this);   //进度条监听
        functionView.setOnFunctionViewItemClickListener(this);  //调用按钮单击监听事件
    }

    /**
     * 通过改变控件seekBar值，设置相机焦距
     * @param want
     */
    private void setZoomValues(int want){
        Camera.Parameters parameters = camera.getParameters();
        seekBar.setProgress(want);
        camera.setParameters(parameters);
    }

    /**
     * 获取相机参数和当前焦距值
     */
    private int getZoomValue(){
        Camera.Parameters parameters=camera.getParameters();
        int values=parameters.getZoom();
        return values;
    }

    /**
     * 增加焦距方法
     */
    private void addZoomValues(){
        if (nowFocus > maxFocus){
            Log.e(TAG,"大于最大焦距值");
        }else if (nowFocus == maxFocus){

        }else {
            setZoomValues(getZoomValue() + everyFocus);
        }
    }

    /**
     * 减少焦距方法
     */
    private void minusZoomValues(){
        if (nowFocus < maxFocus){
            Log.e(TAG,"小于最大焦距值");
        }else if (nowFocus == maxFocus){

        }else {
            setZoomValues(getZoomValue() - everyFocus);
        }
    }

    /**
     * 拖动改变焦距
     */
    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
        //0~99
        Camera.Parameters parameters=camera.getParameters();
        nowFocus=progress;
        parameters.setZoom(progress);
        camera.setParameters(parameters);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.add){
            addZoomValues();
        }else if (v.getId() == R.id.minus){
            minusZoomValues();
        } else if (v.getId() == R.id.picture) {
            //待添加
        }
        return true;
    }


    @Override
    public void hint() {
        Intent intent = new Intent(this, HintActivity.class);
        startActivity(intent);      //界面跳转到系统帮助页面

    }

    @Override
    public void choose() {
        Intent intent = new Intent(this, PhotoFrameActivity.class);
        startActivityForResult(intent,PHOTO);   //跳转页面并获得选择镜框ID的返回值
        Toast.makeText(this,"选择！",Toast.LENGTH_SHORT).show();   //提示
    }

    @Override
    public void down() {
        downCurrentActivityBrightnessValue();   //调低亮度方法
    }

    @Override
    public void up() {
        upCurrentActivityBrightnessValue();//获取设置后的屏幕亮度
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"返回值："+requestCode+"请求值"+resultCode);
        if (resultCode == RESULT_OK && requestCode == PHOTO){
            int position = data.getIntExtra("POSITION",0);  //从返回数据获得POSITION值
            frame_index = position; //  图片位置赋值
            Log.e(TAG,"返回的镜框类别："+ position);
            pictrueView.setPhotoFrame(position);    //传递POSITION值，设置镜框
        }

    }
    /**
     * 设置屏幕亮度
     */
    private void setMyActivityBright(int brightnessValue){
        SetBrightness.setBrightness(this,brightnessValue);  //调用SetBrightness类方法设置亮度
        //保存亮度
        SetBrightness.saveBrightness(SetBrightness.getResolver(this),brightnessValue);
    }
    /**
     * 获取屏幕亮度
     */
    private void getAfterMySetBrightnessValues(){
        brightnessValue = SetBrightness.getScreenBrightness(this);  //获得亮度
        Log.e(TAG,"当前手机亮度："+ brightnessValue);
    }
    /**
     * 先获得是否自动调节亮度，如果是，则关闭自动调节，并设置一个缺省的亮度值
     */
    public void getBrightnessFromWindow(){
        //获得是否自动调节亮度
        isAutoBrightness =SetBrightness.isAutoBrightness(SetBrightness.getResolver(this));
        Log.e(TAG,"当前手机是否是自动调节屏幕亮度："+isAutoBrightness);
        if (isAutoBrightness){
            SetBrightness.stopAutoBrightness(this);
            Log.e(TAG,"关闭了自动调节");
            setMyActivityBright(255/2+1);
        }
        //亮度值0~256
        SegmentLengh = (255/2+1)/4; //每32为一个区间
        getAfterMySetBrightnessValues();    //获取设置后的亮度
    }
    /**
     * 如果亮度值大于0，则亮度值设置为原亮度值-被更改的亮度值
     */
    private void downCurrentActivityBrightnessValue(){
        if (brightnessValue > 0){
            setMyActivityBright(brightnessValue - SegmentLengh);    //减少亮度值
        }
        getAfterMySetBrightnessValues();
    }
    /**
     * 增加亮度
     */
    private void upCurrentActivityBrightnessValue(){
        if (brightnessValue <255){
            if (brightnessValue + SegmentLengh >= 256){
                return;
            }
            setMyActivityBright(brightnessValue + SegmentLengh);//调高亮度
        }
        getAfterMySetBrightnessValues();//获取设置后的屏幕亮度
    }











}