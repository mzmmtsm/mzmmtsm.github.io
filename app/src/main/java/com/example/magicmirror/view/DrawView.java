package com.example.magicmirror.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magicmirror.R;

public class DrawView extends View {
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float moveX,moveY;
    private Bitmap mBitmap;
    private Bitmap bitmap;
    private volatile boolean mComplete = false;
    public DrawView(Context context) {
        this(context,null);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(); //初始化
    }

    private void init(){
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.glasses).copy(Bitmap.Config.ARGB_8888,true);
        //新建画笔，设置画笔
        mPaint = new Paint();                           //新建画笔
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(100);
        mPath = new Path();                             //创建新路径
    }
    //新增画布重绘事件，完成擦除功能


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        //如果擦除还未完成
        if (!mComplete){
            //设定目标图模式
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(mBitmap,0,0,null);
            mCanvas.drawPath(mPath,mPaint);
            canvas.drawBitmap(mBitmap,0,0,null);
        }
        //如果擦除干净，则进行资源释放操作
        if (mComplete){
            if (mListener != null){
                mListener.complete();//监听结束
                setEndValues(); //变量重置
            }
        }
    }
    /**
     * 完成擦除图片的重新载入
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        //基于原bitmap创建一个新的宽高的bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
        mCanvas.drawBitmap(bitmap,0,0,null);
    }
    /**
     * 擦一擦监听接口
     */
    public interface OnCaYiCaCompleteListener{
        void complete();
    }
    private OnCaYiCaCompleteListener mListener;  //声明擦一擦接口
    public void setOnCaYiCaCompleteListener(OnCaYiCaCompleteListener mListener){
        this.mListener=mListener;   //接口回调
    }

    /**
     * 变量重置
     */
    public void setEndValues(){
        moveX=0;
        moveY=0;
        mPath.reset();
        mComplete = false;
    }

    /**
     * 完成手指移动进行重绘，并重绘画布
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //获取高、宽
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w*h;
            Bitmap bitmap = mBitmap;
            int[] mPixels = new int[w*h];   //像素数组

            //获得Bitmap上所有的像素信息
            bitmap.getPixels(mPixels,0,w,0,0,w,h);

            //计算擦除区域
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = i+j*w;
                    if (mPixels[index] == 0){
                        wipeArea++;
                    }
                }
            }
            //计算擦除区域所占百分比
            if (wipeArea > 0 && totalArea >0){
                int percent = (int) (wipeArea * 100 / totalArea);

                Log.e("TAG",percent + "");

                if (percent > 50){          //如果擦除区域大于50%，则清除图层
                    mComplete = true;       //清除图层区域
                    postInvalidate();
                }
            }
        }
    };
    /**
     * 实现重载手机触屏
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       float x = event.getX();
       float y = event.getY();

       //获得触屏事件
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                moveY = y;
                moveX = x;
                mPath.moveTo(moveX,moveY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) Math.abs(moveX - x);
                int dy = (int) Math.abs(moveY - y);
                if (dx > 1 || dy > 1){
                    mPath.quadTo(x,y,(moveX + x) /2,(moveY + y) / 2);
                }
                moveX =x;
                moveY =y;
                break;
            case MotionEvent.ACTION_UP:
                if (!mComplete){
                    new Thread(mRunnable).start();
                }
                break;
            default:
                break;
        }
        if (!mComplete){
            invalidate();
        }

        return true;

    }
}
