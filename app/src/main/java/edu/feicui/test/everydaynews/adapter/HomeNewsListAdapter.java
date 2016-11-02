package edu.feicui.test.everydaynews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.entity.News;
import edu.feicui.test.everydaynews.entity.NewsArray;

/**
 * 新闻主界面适配器(因为要适配的数据是新闻数据，故MyBaseAdapter的泛型是News)
 * Created by Administrator on 16-9-28.
 */
public class HomeNewsListAdapter extends MyBaseAdapter<News> {


    public HomeNewsListAdapter(Context mContext, ArrayList mList, int mlayoutId) {
        super(mContext, mList, mlayoutId);
    }





    @Override
    public void renderingView(Holder holder, View convertView, int position, News news) {
        ImageView mIVIcon= (ImageView) convertView.findViewById(R.id.iv_home_news_icon);
        TextView mTVTitle= (TextView) convertView.findViewById(R.id.tv_home_news_title);
        TextView mTVSummary= (TextView) convertView.findViewById(R.id.tv_home_news_summary);
        TextView mTVStamp= (TextView) convertView.findViewById(R.id.tv_home_news_stamp);
        //第三方jar包glide,处理图片，格式:Glide.with(Context).load(图片String).into(mIVIcon);

        Glide.with(mContext).load(news.icon).into(mIVIcon);
        mTVTitle.setText(news.title);
        mTVSummary.setText(news.summary);
        mTVStamp.setText(news.stamp);


    }
}
