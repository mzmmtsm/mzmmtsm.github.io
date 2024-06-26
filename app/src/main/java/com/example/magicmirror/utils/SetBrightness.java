package com.example.magicmirror.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

public class SetBrightness {
    /**
     * 获取亮度调节模式，返回是否自动调节的布尔值
     * @param aContentResolver
     * @return
     */
    public static boolean isAutoBrightness(ContentResolver aContentResolver){
        boolean automicBrightness = false;  //是否自动调节亮度
        try {
            //获得系统设置的亮度调节模式
            automicBrightness = Settings.System.getInt(aContentResolver,Settings.System.SCREEN_BRIGHTNESS_MODE)
                    == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }
        return automicBrightness;
    }

    /**
     * 获取屏幕亮度
     */
    public static int getScreenBrightness(Activity activity){
        int nowBrightnessValue = 0; //亮度值
        //获得实例
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver,Settings.System.SCREEN_BRIGHTNESS);    //获取亮度
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }
        return nowBrightnessValue;
    }
    /**
     * 设置屏幕亮度
     */
    public static void setBrightness(Activity activity,int brightness){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();   //获取窗体属性
        lp.screenBrightness = Float.valueOf(brightness) * (1f/255f);    //设置屏幕亮度
        activity.getWindow().setAttributes(lp);     //设置窗口属性
    }
    /**
     * 亮度自动调节
     */
    public static void stopAutoBrightness(Activity activity){
        //设置activity亮度手工模式
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }
    /**
     * 开启亮度自动调节
     */
    public static void startAutoBrightness(Activity activity){
        //设置activity亮度自动调节模式
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }
    /**
     * 保存亮度设置
     */
    public static void saveBrightness(ContentResolver resolver,int brightness){
        //获取屏幕亮度
        Uri uri = Settings.System.getUriFor("screen_brightness");
        //设置屏幕亮度
        Settings.System.putInt(resolver,"screen_brightness",brightness);
        resolver.notifyChange(uri,null);    //保存状态
    }
    /**
     * 获取内容解析
     */
    public static ContentResolver getResolver(Activity activity){
        ContentResolver cr = activity.getContentResolver();//获取activity解析内容
        return cr;
    }





















}
