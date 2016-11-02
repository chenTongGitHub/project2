package edu.feicui.test.everydaynews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import java.util.ArrayList;

/**
 * 引导界面左右滑动ViewPager和Fragment合用的适配器
 * Created by Administrator on 16-10-17.
 */
public class GuideViewPagerAdapter extends FragmentPagerAdapter{
    ArrayList<Fragment> mList;

    //继承2个方法和完善1个构造方法
    public GuideViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> mList) {
        super(fm);
        this.mList=mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }
}
