package edu.feicui.test.everydaynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.adapter.CommentListAdapter;
import edu.feicui.test.everydaynews.entity.CommentCommitInfo;
import edu.feicui.test.everydaynews.entity.CommentData;
import edu.feicui.test.everydaynews.entity.CommentInfo;
import edu.feicui.test.everydaynews.entity.News;
import edu.feicui.test.everydaynews.entity.SomeImportentData;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;
import edu.feicui.test.everydaynews.view.xlist.XListView;

/**
 * Created by Administrator on 16-10-11.
 */
public class CommentActivity extends BaseActivity implements XListView.IXListViewListener,View.OnClickListener {
    /**
     * 从详细新闻传过来的数据，拿到新闻id，用于请求展示评论
     */
    News news;
    /**
     * 上拉加载时，最后一条的id
     */
    int cid=1;
    /**
     * dir=1 刷新
     * dir=2 加载
     * 接口文档中
     * 1：用户下滑界面主动刷新，服务器返回较新的评论
     * 2：用户上拉界面主动刷新，服务器返回较早的评论；
     */
    int dir=1;
    /**
     * 找到XListView
     */
    XListView mXlvComment;
    /**
     * XListView的适配器
     */
    CommentListAdapter mAdapter;
    /**
     * 拿到编辑评论的view
     */
    EditText mCommentCommit;
    /**
     * 拿到点击发布评论的view
     */
    ImageView mIvSend;
    /**
     * 接收评论发布成功之后的消息，将新评论置顶
     */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                /**
                 * 评论成功后，接收消息，为了将评论显示在第一条
                 *
                 */
                case 0:

                    //清空之前的,数据改变必须刷新适配器
                    mAdapter.mList.clear();
                    mAdapter.notifyDataSetChanged();

                    getHttpCommentData();
                    break;

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    void initView() {
        //****************设置抬头*********************
        mIvLeft.setImageResource(R.mipmap.back);
        mTvTittle.setText("评论");
        mIvRight.setVisibility(View.GONE);
        mBntComments.setVisibility(View.GONE);

        mCommentCommit= (EditText) findViewById(R.id.et_comment_commit);
        mIvSend= (ImageView) findViewById(R.id.iv_comment_send);

        //****************上拉加载、下拉刷新*********************
        //拿到XListView,绑定适配器（因为要上拉加载、下拉刷新，先不给适配器传值）
        mXlvComment= (XListView) findViewById(R.id.xlv_comment_list);
        mAdapter=new CommentListAdapter(this,null,R.layout.item_comment_list);
        mXlvComment.setAdapter(mAdapter);
        //****从详细新闻处拿到news对象，进一步拿到新闻id用于请求展示评论
        Intent intent=getIntent();
        news= (News) intent.getSerializableExtra("data");
        //先请求一次数据，用于初始化，刚一进界面就展示出来
        getHttpCommentData();


        //设置上拉加载、下拉刷新的功能
        //可以进行上拉加载
        mXlvComment.setPullLoadEnable(true);
        //可以进行下拉刷新
        mXlvComment.setPullRefreshEnable(true);
        //绑定上拉 下拉滑动监听
        mXlvComment.setXListViewListener(this);



        //绑定监听
        setOnClickListener(this,mIvLeft,mIvSend);

    }
    /**
     * 请求展示评论的数据
     * ver=版本号&nid=新闻id&type=1&stamp=yyyyMMdd&cid=评论id&dir=0&cnt=20
     */
    public void getHttpCommentData(){
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("nid",news.nid+"");
        params.put("type","1");
        params.put("stamp","20140321000000");
        params.put("cid",""+cid);
        params.put("dir",""+dir);
        params.put("cnt","20");
        MyHttp.get(this, ServeUrl.CMT_LIST, params, new OnResultFinishListener() {
            @Override
            public void success(Response response) {
                Gson gson=new Gson();
                CommentInfo info=gson.fromJson(response.result.toString(), CommentInfo.class);
                Log.e("aaaa","COMMENT****"+info);
                /**
                 * 如果有数据，进行添加和刷新适配器
                 */
                if (info.data!=null&&info.data.size()>0){
                    mAdapter.mList.addAll(info.data);
                    mAdapter.notifyDataSetChanged();
                }
                //关闭上拉加载  下拉刷新
                mXlvComment.stopLoadMore();
                mXlvComment.stopRefresh();
            }

            @Override
            public void failed(Response response) {
                Toast.makeText(CommentActivity.this,"失败!", Toast.LENGTH_SHORT).show();
                //如果请求失败，关闭上拉加载  下拉刷新
                mXlvComment.stopLoadMore();
                mXlvComment.stopRefresh();
            }
        });

    }

    @Override
    public void onRefresh() {
        // dir=1 下拉刷新
        dir=1;
        //清空之前的
        mAdapter.mList.clear();
        getHttpCommentData();//请求展示评论
    }

    @Override
    public void onLoadMore() {
        // dir=2 上拉加载
        dir=2;
        //拿到最后一条评论id
        if (mAdapter.mList.size()>0){
            CommentData mComment=mAdapter.mList.get(mAdapter.mList.size()-1);
            cid=mComment.cid;
        }
        getHttpCommentData();//请求展示评论
    }

    /**
     * 请求发布评论的接口
     * ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
     */
    public void getHttpCommentCommit(){
        final String mComment=mCommentCommit.getText().toString();
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("nid",news.nid+"");
        params.put("token", SomeImportentData.token);
        params.put("imei",SomeImportentData.IMEI);
        params.put("ctx",mComment);
        if (mComment.equals("")){
            Toast.makeText(CommentActivity.this,"评论不能为空!", Toast.LENGTH_SHORT).show();

        }else{//请求接口
            MyHttp.get(this, ServeUrl.CMT_COMMIT, params, new OnResultFinishListener() {
                @Override
                public void success(Response response) {
                        //请求成功，解析
                        Gson gson=new Gson();
                        CommentCommitInfo info=gson.fromJson(response.result.toString(), CommentCommitInfo.class);
                        Log.e("aaaa","CommentCommitInfo---"+info);
                        switch (info.status){
                            case 0:
                                Toast.makeText(CommentActivity.this,"评论发布成功!", Toast.LENGTH_SHORT).show();
                                /**
                                 * 评论成功后，发一条消息，为了将评论显示在第一条
                                 *
                                 */
                                handler.sendEmptyMessage(0);

                                break;
                            case -3:
                                Toast.makeText(CommentActivity.this,"请先登录账号!", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        //发送完毕，编辑框清空
                        mCommentCommit.setText("");
                }

                @Override
                public void failed(Response response) {
                    Toast.makeText(CommentActivity.this,"失败!", Toast.LENGTH_SHORT).show();
                }
            });

        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_layout_base_top_left:
                finish();
            break;
            case R.id.iv_comment_send:
            getHttpCommentCommit();
            break;
        }


    }
}
