package edu.feicui.test.everydaynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import edu.feicui.test.everydaynews.R;

/**
 * 登陆界面
 * Created by Administrator on 16-10-18.
 */
public class LoadingActivity extends AppCompatActivity{
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Intent intent=new Intent(LoadingActivity.this,HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slid_in_right,R.anim.slid_in_left);
                    finish();//不能返回
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ImageView mIvAnimation= (ImageView) findViewById(R.id.iv_loading);
        //添加渐变动画(透明0.0到不透明1.0)
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        //动画持续时间
        alphaAnimation.setDuration(3000);
        //启动动画
        mIvAnimation.startAnimation(alphaAnimation);
        //开启子线程(不要忘记.start())，延时3秒
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
