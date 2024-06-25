package com.example.magicmirror.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.magicmirror.R;

public class FunctionView extends LinearLayout implements View.OnClickListener {
    private LayoutInflater mInflater;

    private ImageView hint,choose,down,up;
    //顶部栏四个控件id
    public static final int HINT_ID=R.id.hint;
    public static final int CHOOSE_ID=R.id.choose;
    public static final int DOWN_ID=R.id.light_down;
    public static final int UP_ID=R.id.light_up;
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

        hint= (ImageView) view.findViewById(HINT_ID);
        choose=(ImageView) view.findViewById(CHOOSE_ID);
        down=(ImageView) view.findViewById(DOWN_ID);
        up=(ImageView) view.findViewById(UP_ID);
        setView();
    }


    @Override
    public void onClick(View v) {
        if (listener!=null){
            if (v.getId() == HINT_ID)
                listener.hint();
            else if (v.getId() == CHOOSE_ID)
                listener.choose();
            else if (v.getId() == DOWN_ID)
                listener.down();
            else if (v.getId() == UP_ID)
                listener.up();
        }

    }

    private void setView(){
        //设置按钮监听事件
        hint.setOnClickListener(this);
        choose.setOnClickListener(this);
        down.setOnClickListener(this);
        up.setOnClickListener(this);
    }

    public void setOnFunctionViewItemClickListener(onFunctionViewItemClickListener monFunctionViewItemClickListener){
        this.listener = monFunctionViewItemClickListener;       //设置监听对象
    }
















}
