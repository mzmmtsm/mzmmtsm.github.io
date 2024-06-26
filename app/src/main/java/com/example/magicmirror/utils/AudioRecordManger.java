package com.example.magicmirror.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class AudioRecordManger {
    private static final String TAG = "AudioRecord";
    private static final int SAMPLE_RATE_IN_HZ = 8000;  //
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT,AudioFormat.ENCODING_PCM_16BIT);//用于写入声音的缓存
    private AudioRecord mAudioRecord;   //话筒类
    public boolean isGetVoiceRun;   //是否录音运行
    private Handler mHandler;   //消息句柄
    private int mWhat;  //动作
    public Object mLock;    //锁对象

    public AudioRecordManger(Handler handler,int what){
        mLock = new Object();
        this.mHandler = handler;
        this.mWhat = what;
    }

    public void getNoiseLevel(){
        if (isGetVoiceRun){
            Log.e(TAG,"在录着呢");
            return;
        }
        //创建录音对象，并初始化对象属性
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT,BUFFER_SIZE);

        //判断话筒对象是否为空
        if (mAudioRecord == null){
            Log.e("sound","mAudioRecord初始化失败");
        }
        isGetVoiceRun=true;//开启录音

        //使用新线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioRecord.startRecording();//录音启动
                short[] buffer = new short[BUFFER_SIZE];//设置缓存数组

                while (isGetVoiceRun){
                    int r=mAudioRecord.read(buffer,0,BUFFER_SIZE);
                    long v = 0;
                    //将bufer内容取出，进行平方和运算
                    for (int i = 0; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    double mean = v/(double)r; //平方和除以数据总长度得到音量大小
                    double volume = 10* Math.log10(mean);

                    //大概一秒十次,锁
                    synchronized (mLock){
                        try {
                            mLock.wait(500);

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //声明消息类，句柄发送消息到主窗体方法
                    Message message = Message.obtain();
                    message.what = mWhat;
                    message.obj = volume;
                    mHandler.sendMessage(message);
                }
                //话筒对象释放
                if (null !=mAudioRecord){
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord =null;
                }
            }
        }).start(); //启动线程









    }



}
