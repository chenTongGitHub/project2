package edu.feicui.test.everydaynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * ListView的适配器的基类
 * Created by Administrator on 16-9-27.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter{
    /**
     * 完善构造方法，便于传参数
     */
    Context mContext;//与activity产生关联
    public ArrayList<T> mList;//传数据
    int mlayoutId;//条目布局ID
    LayoutInflater mLayoutInflater;//渲染器

    public MyBaseAdapter(Context mContext, ArrayList<T> mList, int mlayoutId) {
        //判空
        if (mList==null){
            mList=new ArrayList<>();

        }
        this.mContext = mContext;
        this.mList = mList;
        this.mlayoutId = mlayoutId;
        mLayoutInflater= (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView=mLayoutInflater.inflate(mlayoutId,null);
            holder=new Holder();
            convertView.setTag(holder);

        }else{
            holder= (Holder) convertView.getTag();

        }
        /**
         * 将条目View的渲染具体过程交给子类去完成
         * 因为控件、条目View的具体信息在子类才能得到
         */
        renderingView(holder,convertView,position,mList.get(position));


        return convertView;
    }

    /**
     *
     * @param holder     对应条目的holder
     * @param convertView  对应条目的View
     * @param position   对应条目位置
     * @param t          对应条目数据
     */
    public abstract void renderingView(Holder holder,View convertView,int position,T t);
    class Holder{

    }
}
