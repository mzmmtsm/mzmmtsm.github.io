package com.example.magicmirror.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.magicmirror.R;

public class FunctionView extends LinearLayout implements View.OnClickListener {
    private LayoutInflater mInflater;
    /**
     * 回调接口，调用四个按钮方法
     * */
    private onFunctionViewItemClickListener listener;
    public interface onFunctionViewItemClickListener{
        void hint();    //系统帮助按钮方法
        void choose();  //选择镜框方法
        void down();    //减少亮度按钮方法
        void up();      //增加亮度按钮方法
    }

    /**
     * 初始化构造方法
     */
    public  FunctionView(Context context){
        this(context,null);
    }
    public  FunctionView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public  FunctionView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        mInflater=LayoutInflater.from(context);
        init();     //初始化方法
    }

    private void init() {
        View view=mInflater.inflate(R.layout.view_function,this);   //导入view_function布局文件
    }


    @Override
    public void onClick(View v) {

    }
}
