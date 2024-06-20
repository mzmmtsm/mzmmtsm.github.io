package com.example.magicmirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magicmirror.R;

public class StartPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        //3000毫秒后发送what为1给handler对象
        handler.sendEmptyMessageDelayed(1,3000);

    }

    //消息对象
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what==1){
                Intent intent = new Intent(StartPage.this, MainActivity.class);
                startActivity(intent);  //跳转界面
                finish();   //关闭界面

            }
            return false;
        }
    });

//    在此activity中按下键时执行此事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果按下返回键，取消返回操作
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return false;
    }
}
