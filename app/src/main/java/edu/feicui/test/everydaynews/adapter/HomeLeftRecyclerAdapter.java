package edu.feicui.test.everydaynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.entity.Category;

/**
 * 主界面左侧recyclerView的适配器，以及点击子条目的回调接口
 * Created by Administrator on 16-10-20.
 */
public class HomeLeftRecyclerAdapter extends RecyclerView.Adapter{
    Context mContext;
    ArrayList<Category> mList;
    LayoutInflater mLayoutInflater;
    MyHolder holder;
    public HomeLeftRecyclerAdapter(Context mContext,ArrayList<Category> mList){
        if (mList==null){
            mList=new ArrayList<>();
        }
        this.mContext=mContext;
        this.mList=mList;
        mLayoutInflater= (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //初始化子条目的view
        View mView=mLayoutInflater.inflate(R.layout.item_home_news_left_recycler,parent,false);
        //将RecyclerView与Holder关联
        holder=new MyHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.holder= (MyHolder) holder;
        ((MyHolder) holder).mIcon.setImageResource(mList.get(position).icon);
        ((MyHolder) holder).mTVName.setText(mList.get(position).typeName);
        ((MyHolder) holder).mTVEnglishName.setText(mList.get(position).typeEnglishName);
        //子条目绑定监听，用到内部类或者外部类的方法.此处内部类好传数据
        ((MyHolder) holder).itemView.setOnClickListener(new MyOnClickListener(position,holder));

    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder{
        //为了能拿到子条目，将其变成成员变量
        View itemView;
        ImageView mIcon;//左侧分类的图标
        TextView mTVName;//左侧分类的名字
        TextView mTVEnglishName;//左侧分类的英文名字
        public MyHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            mIcon= (ImageView) itemView.findViewById(R.id.iv_home_left_icon);
            mTVName= (TextView) itemView.findViewById(R.id.tv_home_left_name);
            mTVEnglishName= (TextView) itemView.findViewById(R.id.tv_home_left_english_name);

        }
    }

    /**
     *（在适配器中）子条目绑定监听,内部类
     */
    class MyOnClickListener implements View.OnClickListener{
        int position;
        RecyclerView.ViewHolder holder;
        public MyOnClickListener(int position,RecyclerView.ViewHolder holder){
            this.position=position;
            this.holder=holder;
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,mList.get(position).typeName , Toast.LENGTH_SHORT).show();
            mOnItemClickListener.OnItemClick(v,position,holder);
        }
    }

    /**
     * (适配器给activity传东西)写一个回调接口，用于在activity中绑定点击子条目监听
     * 注意：回调只是用于传数据，跟子线程刷新UI没有关系
     */
    public interface OnItemClickListener{
        void OnItemClick(View view, int position, RecyclerView.ViewHolder holder);

    }
    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }

}

