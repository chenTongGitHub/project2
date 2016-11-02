package edu.feicui.test.everydaynews.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.entity.CommentData;

/**
 * Created by Administrator on 16-10-12.
 */
public class CommentListAdapter extends MyBaseAdapter<CommentData>{
    public CommentListAdapter(Context mContext, ArrayList<CommentData> mList, int mlayoutId) {
        super(mContext, mList, mlayoutId);
    }

    /**
     * 渲染
     * @param holder     对应条目的holder
     * @param convertView  对应条目的View
     * @param position   对应条目位置
     * @param commentData
     */
    @Override
    public void renderingView(Holder holder, View convertView, int position, CommentData commentData) {
        ImageView mPortrait= (ImageView) convertView.findViewById(R.id.iv_item_comment_portrait);
        TextView mName= (TextView) convertView.findViewById(R.id.tv_item_comment_name);
        TextView mTime= (TextView) convertView.findViewById(R.id.tv_item_comment_time);
        TextView mCommentContent= (TextView) convertView.findViewById(R.id.tv_item_comment_content);

        //头像用第三方jar包
        Glide.with(mContext).load(commentData.portrait).into(mPortrait);
        mName.setText(commentData.uid);
        mTime.setText(commentData.stamp);
        mCommentContent.setText(commentData.content);

    }
}
