package edu.feicui.test.everydaynews.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.entity.News;
import edu.feicui.test.everydaynews.entity.NewsCommentNumInfo;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;

/**
 * 展示新闻的详细信息界面
 * Created by Administrator on 16-9-29.
 */
public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 新闻列表传过来的数据，用于拿到新闻id
     */
    News news;
    /**
     * 网页视图
     */
    WebView mWebView;
    /**
     * 加入收藏弹出的PopupWindow
     */
    PopupWindow mPopupWindow;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BaseActivity的方法setContentView
        setContentView(R.layout.activity_news_detail);
    }

    @Override
    void initView() {
        mIvLeft.setImageResource(R.mipmap.back);
        mTvTittle.setText("资讯");
        mIvRight.setImageResource(R.mipmap.news_menu);



        //展示详细新闻
        mWebView= (WebView) findViewById(R.id.web_news_detail);
        /**
         * 拿到新闻列表传过来的数据
         * 拿到传过来的news
         */
        Intent intent=getIntent();
        news= (News) intent.getSerializableExtra("data");
        getHttpCommentNum();

        //通过WebView拿到新闻详情加载进度
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Message message=Message.obtain();
                message.arg1=newProgress;
                handler.sendMessage(message);
            }
        });
        //加载链接
        mWebView.loadUrl(news.link);
        /**
         * 使用当前界面作为web界面的展示界面，返回true，并且重新加载
         */
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });

        //添加设置
        WebSettings settings=mWebView.getSettings();
        //页面中含有javaScript(java脚本)   则需要设置进行支持
        settings.setJavaScriptEnabled(true);
        //适应屏幕大小
        settings.setUseWideViewPort(true);
        //可以按照任意比例进行缩放
        settings.setLoadWithOverviewMode(true);
        //支持缩放功能
        settings.setSupportZoom(true);
        //显示缩放视图
        settings.setBuiltInZoomControls(true);

        /**
         * 点击抬头右侧加入收藏，弹出PopupWindow
         * 1.填充PopupWindow的view
         * 2.拿到PopupWindow的对象用于展示
         */
        View view=getLayoutInflater().inflate(R.layout.item_news_detail_popup,null);
        mPopupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        //绑定点击监听
        setOnClickListener(this,mIvLeft,mIvRight,mBntComments);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_layout_base_top_left://返回主新闻列表
                finish();
            break;
            case R.id.iv_layout_base_top_right://弹出PopupWindow
                //外部可点击 setOutsideTouchable和setBackgroundDrawable通常一起用
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                //基于某个控件弹出
                mPopupWindow.showAsDropDown(mIvRight,-20,0);

                break;
            case R.id.bnt_layout_base_top_comment://跳转到评论界面
                Intent intent1=new Intent(this,CommentActivity.class);
                intent1.putExtra("data",news);
                startActivity(intent1);
                break;

        }
    }
    /**
     * 请求跟帖数量的接口数据
     * ver=版本号& nid=新闻编号
     */
    public void getHttpCommentNum(){
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("nid",news.nid+"");
        MyHttp.get(this, ServeUrl.CMT_NUM, params, new OnResultFinishListener() {
            @Override
            public void success(Response response) {
                Gson gson=new Gson();
                NewsCommentNumInfo info=gson.fromJson(response.result.toString(), NewsCommentNumInfo.class);
                Log.e("AAAA","NewsCommentNumInfo***"+info);
                if (info.status==0){
                    //请求成功，将跟帖数展示在抬头
                    mBntComments.setText(info.data+"跟帖");
                }
            }

            @Override
            public void failed(Response response) {

            }
        });


    }

}
