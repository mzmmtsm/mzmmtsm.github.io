package com.example.magicmirror.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magicmirror.R;

public class HintActivity extends AppCompatActivity {
    private TextView konw;      //"我知道"控件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //不显示状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);//设置对应的布局文件
        konw = (TextView) findViewById(R.id.i_know);
        konw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//结束窗体
            }
        });
    }


















}
