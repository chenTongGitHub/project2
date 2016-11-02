package edu.feicui.test.everydaynews;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 16-10-9.
 */
public class ExampleApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
