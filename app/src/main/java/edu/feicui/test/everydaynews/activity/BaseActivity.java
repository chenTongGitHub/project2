package edu.feicui.test.everydaynews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.feicui.test.everydaynews.R;

/**
 * Created by Administrator on 16-9-27.
 */
public abstract class BaseActivity extends AppCompatActivity{
    /**
     * 获取当前Activity的类名getSimpleName() getName()
     */
    public String Tag=this.getClass().getSimpleName();
    LinearLayout mLinLayoutTop;//base_layout上边的内容
    RelativeLayout mRelContent;//base_layout下边的内容
    ImageView mIvLeft;
    TextView mTvTittle;
    Button mBntComments;
    ImageView mIvRight;
    LayoutInflater mInflater;



    /**
     * 重写父类加载界面的方法，使其子类简化代码
     * 重载加载界面的方法
     * 两种参数：id    view
     */
    public void setContentView(int id){
        //将子activity的传入的id加载到base_layout的Content
        mInflater.inflate(id, mRelContent);
        initView();
    }
    public void setContentView(View view){
        //将子类的布局加到base_layout的Content
        mRelContent.addView(view);
        initView();
    }
    /**
     * 建抽象方法initView();  子类继承，用与初始化数据
     */
    abstract void  initView();
    /**
     * 提供去绑定事件的方法
     *
     * private 当前类 protected 当前包和子类 默认 当前包 public 公开的
     */
    /**
     *
     * @param listener
     * @param views  传入view
     */
    public void setOnClickListener(View.OnClickListener listener, View... views) {
        // 先判断是否监听到点击事件
        if (listener == null) {
            return;
        }
        // 给每一个View绑定点击事件
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }
    /**
     *
     * @param listener
     * @param ids  传入id
     */
    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        // 先判断是否监听到点击事件
        if (listener == null) {
            return;
        }
        // 先得到每一个点击事件id，用findViewById得到View，给每一个View绑定点击事件
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mLinLayoutTop=(LinearLayout) findViewById(R.id.ll_base_layout_top);
        mRelContent=(RelativeLayout) findViewById(R.id.rl_layout_base_content);
        mIvLeft=(ImageView) findViewById(R.id.iv_layout_base_top_left);
        mTvTittle=(TextView) findViewById(R.id.tv_layout_base_top_title);
        mBntComments= (Button) findViewById(R.id.bnt_layout_base_top_comment);
        mIvRight=(ImageView) findViewById(R.id.iv_layout_base_top_right);
        mInflater=getLayoutInflater();


    }


}
