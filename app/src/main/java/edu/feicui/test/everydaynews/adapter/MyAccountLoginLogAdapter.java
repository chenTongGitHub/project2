package edu.feicui.test.everydaynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.entity.LoginLogInfo;

/**
 * Created by Administrator on 16-10-11.
 */
public class MyAccountLoginLogAdapter extends RecyclerView.Adapter{
    Context mContext;
    ArrayList<LoginLogInfo> mList;
    LayoutInflater mLayoutInflater;
    MyHolder holder;

    public MyAccountLoginLogAdapter(Context mContext,ArrayList<LoginLogInfo> mList){
        this.mContext=mContext;
        this.mList=mList;
        mLayoutInflater= (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     * @param parent  父容器
     * @param viewType  类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //初始化子条目的view
        View view=mLayoutInflater.inflate(R.layout.item_myaccount_log_recycler,parent,false);
        //将RecyclerView与Holder关联
        holder=new MyHolder(view);
        return holder;
    }

    /**
     *
     * @param holder  onCreateViewHolder返回的holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.holder= (MyHolder) holder;
        ((MyHolder) holder).mTvDate.setText(mList.get(position).time);
        ((MyHolder) holder).mTvLocation.setText(mList.get(position).address);
        ((MyHolder) holder).mTvDevice.setText(mList.get(position).device+"");

    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    /**
     * 继承ViewHolder，完善构造方法
     * 为了Holder与条目view 产生关联
     */
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mTvDate;
        TextView mTvLocation;
        TextView mTvDevice;
        View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            mTvDate= (TextView) itemView.findViewById(R.id.tv_myaccount_log_recycler_date);
            mTvLocation= (TextView) itemView.findViewById(R.id.tv_myaccount_log_recycler_location);
            mTvDevice= (TextView) itemView.findViewById(R.id.tv_myaccount_log_recycler_device);

        }
    }
}
