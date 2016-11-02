package edu.feicui.test.everydaynews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.activity.fragment.GuideFourFragment;
import edu.feicui.test.everydaynews.activity.fragment.GuideOneFragment;
import edu.feicui.test.everydaynews.activity.fragment.GuideThreeFragment;
import edu.feicui.test.everydaynews.activity.fragment.GuideTwoFragment;
import edu.feicui.test.everydaynews.adapter.GuideViewPagerAdapter;

/**
 * Created by Administrator on 16-9-28.
 */
public class GuideActivity extends FragmentActivity implements  RadioGroup.OnCheckedChangeListener, View.OnTouchListener {
    /**
     * 数据源（左右滑动4个碎片）
     */
    ArrayList<Fragment> mList;
    /**
     * 找到ViewPager（左右滑动的View）
     */
    ViewPager mViewPager;
    /**
     * 找到RadioGroup（底下滑动点击变色的点的容器）
     */
    RadioGroup mRadioGroup;
    /**
     * 找到第一个RadioButton
     */
    RadioButton mRadioButtonOne;

    /**
     *拿到手指按下点的坐标
     * Y轴没用到
     */
    float startX;
    float startY;

    /**
     * 拿到手指抬起点的坐标
     * Y轴没用到
     */
    float endX;
    float endY;
    /**
     * getSharedPreferences()的第一个参数
     *
     */
    String PREFERENCES_NAME="preferences_name";

    /**
     * 跳转要记录,用SharedPreferences存储
     *
     */
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        /**
         * getData()返回值为true，则说明已经跳转到加载界面，有数据存储记录，以后直接跳转到登入界面
         */
        if(getData()){
            Intent intent=new Intent(this,LoadingActivity.class);
            startActivity(intent);
            finish();//跳转完后不能再返回
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mViewPager= (ViewPager) findViewById(R.id.vp_guide);
        mRadioGroup= (RadioGroup) findViewById(R.id.rg_guide);
        mRadioButtonOne= (RadioButton) findViewById(R.id.rb_guide_one);
        //首次进入，默认第一个RadioButton是点中状态
        mRadioButtonOne.setChecked(true);


        //1.数据源(4个碎片，展示4张图)
        mList=new ArrayList<>();
        mList.add(new GuideOneFragment());
        mList.add(new GuideTwoFragment());
        mList.add(new GuideThreeFragment());
        mList.add(new GuideFourFragment());
        //2.新建适配器，传数据，绑定适配器
        FragmentManager manager=getSupportFragmentManager();//注意：v4包
        GuideViewPagerAdapter adapter=new GuideViewPagerAdapter(manager,mList);
        mViewPager.setAdapter(adapter);

        //左右滑动绑定监听,获取position(滑动到了第几页)
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             *
             * @param position 滑动到第几个页面
             */
            @Override
            public void onPageSelected(int position) {
                //拿到滑动界面对应的点RadioButton
                RadioButton radioButton= (RadioButton) mRadioGroup.getChildAt(position);
                //该RadioButton是点中状态
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //判断在最后一页中是否向左滑动了，然后进入加载界面
        mViewPager.setOnTouchListener(this);
        //点击RadioButton，跳转到相应的View
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 点击哪个RadioButton，跳转到哪个界面
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i <mList.size(); i++) {
            //遍历每个RadioButton
            RadioButton radioButton= (RadioButton) mRadioGroup.getChildAt(i);
            if (radioButton.getId()==checkedId){//如果当前RadioButton是被选中的，则展示对应view
                mViewPager.setCurrentItem(i);
            }
        }
    }

    /**
     *监听手势，向左滑动到最后一页，是否继续左滑
     * @param v
     * @param event  手势的移动
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://手指按下
                //拿到手指按下点的坐标
                startX=event.getX();
                startY=event.getY();
            break;

            case MotionEvent.ACTION_UP://手指抬起
               //拿到手指抬起点的坐标
                endX=event.getX();
                endY=event.getY();

                /**
                 * 拿到手机屏幕的宽度，与X轴的偏移量做对比
                 * X轴的偏移量超过手机屏幕宽度的三分之一，则视为继续向左活动，跳转到登录界面
                 */
                //X轴的偏移量(不能用绝对值，因为绝对值分辨不出来是向左滑还是向右滑)
                float offsetX=startX-endX;
                /**
                 * 拿到手机屏幕的宽度
                 *方法一：
                 *WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                 *int width = wm.getDefaultDisplay().getWidth();
                 *int height = wm.getDefaultDisplay().getHeight();</div>
                 *
                 *方法二：
                 *WindowManager wm1 = this.getWindowManager();
                 *int width1 = wm1.getDefaultDisplay().getWidth();
                 *int height1 = wm1.getDefaultDisplay().getHeight();
                 *方法一与方法二获取屏幕宽度的方法类似，只是获取WindowManager 对象时的途径不同。
                 *方法一与方法二已过时getWidth()、getHeight()
                 * WindowManager是Android中一个重要的服务（Service ）
                 *
                 *方法三：
                 *WindowManager manager = this.getWindowManager();
                 *DisplayMetrics outMetrics = new DisplayMetrics();
                 *manager.getDefaultDisplay().getMetrics(outMetrics);
                 *int width2 = outMetrics.widthPixels;
                 *int height2 = outMetrics.heightPixels;
                 *
                 *方法四：
                 *Resources resources = this.getResources();
                 *DisplayMetrics dm = resources.getDisplayMetrics();
                 *float density1 = dm.density;
                 *int width3 = dm.widthPixels;
                 *int height3 = dm.heightPixels;
                 *方法三和方法四是拿到坐标的像素
                 */
                Point outSize=new Point();
                WindowManager windowManager= (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getSize(outSize);
                int width=outSize.x;
                //当滑动到最后一页并且继续向左滑超过屏幕三分之一，跳转
                if ((mViewPager.getCurrentItem()==mList.size()-1)&&(offsetX>=(width/3.0))){
                    /**
                     * 跳转要记录（引导界面只是在首次安装展现），写数据
                     *
                     * 1.拿到编辑器
                     * 2.写数据
                     * 3.提交，否则不生效
                     */
                    SharedPreferences.Editor editor=preferences.edit();//拿到编辑器
                    /**
                     * 写入Boolean类型（为了方便判断数据是否存储）
                     * putBoolean(String key, boolean value)
                     * @key  自己定义 此处：key值定义为is_first
                     * @value 对应Boolean类型的value值定义为true
                     */
                    editor.putBoolean("is_first",true);//写数据
                    editor.commit();//提交生效
                    //符合滑动到最后一页继续左滑，跳转到加载界面
                    Intent intent=new Intent(this,LoadingActivity.class);
                    startActivity(intent);
                    finish();//跳转完后不能再返回
                }
                break;
        }
        return false;
    }

    /**
     * 操作SharedPreference中的数据
     * 用于判断是否跳到加载界面了
     * @return 读SharedPreferences存储的数据是不是存在
     */
    public boolean getData(){
        /**
         * 要拿到SharedPreferences对象，用getSharedPreferences
         * getSharedPreferences(String name, int mode)
         * @name name文件名
         * @mode 访问以及读写操作权限
         * Context.MODE_PRIVATE：表示该文件只能被自身应用调用，
         * MODE_WORLD_READABLE: 表示该文件可以被其他应用程序读取
         * MODE_WORLD_WRITEABLE:表示该文件可以被其他应用程序写入
         */

        preferences=getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE);
        /**
         * 根据SharedPreferences的对象 读数据
         * 因为写入的是Boolean类型， 读取也得Boolean类型
         * getBoolean(String key, boolean defValue)
         * @key   匹配写入的数据
         * @defValue  当key对应的值不存在时，返回的值(该参数默认值为false)
         *
         * 如果key值is_first存在，对应的value值true存在，用boolean接收
         * 如果key值is_first不存在，用boolean接收当其不存在时返回的值（即第二个参数）
         */

         boolean isFirst=preferences.getBoolean("is_first",false);
         return  isFirst;
    }
}
