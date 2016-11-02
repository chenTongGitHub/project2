package edu.feicui.test.everydaynews.activity.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.activity.HomeActivity;
import edu.feicui.test.everydaynews.activity.NewsDetailActivity;
import edu.feicui.test.everydaynews.adapter.HomeNewsListAdapter;
import edu.feicui.test.everydaynews.entity.News;
import edu.feicui.test.everydaynews.entity.NewsArray;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;
import edu.feicui.test.everydaynews.view.xlist.XListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 将有数据的新闻主页面当做碎片
 * Created by Administrator on 16-9-29.
 */
public class NewsFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener,View.OnClickListener {
    TextView mTvSocial;
    TextView mTvMilitary;
    ImageView mIvSort;
    /**
     * 拿到已经给Fragment加载好界面
     */
    View mView;
    /**
     * 拿到XListView
     */
    XListView mNewsList;
    /**
     * 适配器需要参数
     * Toast需要的参数
     * 封装get、post需要参数
     */
    HomeActivity activity;
    /**
     * 新闻列表的适配器
     */
    HomeNewsListAdapter mNewsListAdapter;
    /**
     * 新闻分类，默认subid=1，军事
     */
    int subid=1;
    /**
     * dir=1 刷新
     * dir=2 加载
     *
     * 接口文档：
     * 1:服务器需要返回最新的新闻列表
     * 2：表示用户上拉界面主动刷新，服务器需返回用户需要的之前的数据
     */
    int dir=1;//初始值为刷新
    /**
     * 上拉加载时，最后一条的id
     */
    int nid=1;//新闻id
    ProgressDialog mDialog;
    /**
     *
     */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    //清空之前数据，刷新适配器
                    mNewsListAdapter.mList.clear();
                    mNewsListAdapter.notifyDataSetChanged();
                    //重新请求一次
                    getHttpData();
                    break;
            }

        }
    };
    /**
     * 给Fragment加载界面
     * @param inflater  布局填充器，主要加载xml  fragment
     * @param container  将碎片加入的view
     * @param savedInstanceState  与onCreat的参数一致
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_news_list,container,false);
        return mView;
    }

    /**
     * 已经给Fragment加载好界面
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // mView=getView();拿到已经给Fragment加载好界面
        activity= (HomeActivity) getActivity();

      //加载dialog
        mDialog=new ProgressDialog(activity);
        mDialog.setCancelable(true);//是否可以被取消
        mDialog.setMessage("loading...");//加载显示的信息
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//圆环风格
        mDialog.show();
        //注意：setContentView()要在show()后使用
        mDialog.setContentView(R.layout.dialog_loading_data);


        mTvSocial= (TextView) mView.findViewById(R.id.tv_news_sort_social);
        mTvMilitary= (TextView) mView.findViewById(R.id.tv_news_sort_military);
        mIvSort= (ImageView) mView.findViewById(R.id.iv_news_sort);
        mNewsList= (XListView) mView.findViewById(R.id.xlv_news_fragement);
       //因为是用XListView，有上拉加载、下拉刷新，先不给适配器传数据，拿到请求数据后追加
        mNewsListAdapter=new HomeNewsListAdapter(activity,null,R.layout.item_home_news_list);
        mNewsList.setAdapter(mNewsListAdapter);//绑定适配器
        Log.e("aac","mNewsListAdapter---"+mNewsListAdapter.mList.size());

        //可以进行上拉加载
        mNewsList.setPullLoadEnable(true);

        //可以进行下拉刷新
        mNewsList.setPullRefreshEnable(true);
        //绑定上拉 下拉滑动监听
        mNewsList.setXListViewListener(this);

        //获取网络数据
        getHttpData();
        //绑定点击子条目监听
        mNewsList.setOnItemClickListener(this);
        //绑定各新闻分类点击监听事件
        activity.setOnClickListener(this,mIvSort,mTvMilitary,mTvSocial);
    }
    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        // dir=1 下拉刷新
        dir=1;
        //清空之前的
        mNewsListAdapter.mList.clear();
        //再拿数据
        getHttpData();
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMore() {
        // dir=2 上拉加载
        dir=2;
        //拿到最后一条的id,再getHttpData，请求网络数据
        if (mNewsListAdapter.mList.size()>0){
            News news=mNewsListAdapter.mList.get(mNewsListAdapter.mList.size()-1);
            nid=news.nid;
        }
        //再拿数据
        getHttpData();
    }

    /**
     * 绑定各新闻分类点击监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        getHttpNewsSort();
    }


//    /**
//     * 为了拿到XListView，所写的方法
//     * @param id  view的id
//     * @return
//     *      mView是已经给Fragment加载好界面
//     */
//    public View findViewById(int id) {
//        return mView.findViewById(id);
//
//    }
    /**
     * 请求网络数据
     * ver=版本号&subid=分类名&dir=1&nid=新闻&stamp=20140321&cnt=20
     *
     *  ver=0000000&subid=1&dir=1&nid=1&stamp=20140321000000&cnt=20
     */
    public void getHttpData(){
        //键值对，匹配参数
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("subid",subid+"");
        params.put("dir",""+dir);
        params.put("nid",""+nid);
        params.put("stamp","20140321000000");
        params.put("cnt","20");

        MyHttp.get(activity, ServeUrl.NEWS_LIST, params, new OnResultFinishListener() {
            @Override
            public void success(Response response) {

                //绑定适配器，添加返回的数据 ，刷新界面
                Gson gson=new Gson();
                //开启AsyncTask时，已经拿到结果：response.result。解析必须是NewsArray
                NewsArray array=gson.fromJson(response.result.toString(), NewsArray.class);
                /**
                 * 如果有数据，进行添加和刷新适配器
                 */
                if (array.data!=null&&array.data.size()>0){
                    //取消弹框
                    mDialog.dismiss();

                    //Log.e("aac", "success: "+ array.data.size());

                    //拿到返回的news数据array.data
                    // mList是MyBaseAdapter的。addAll追加新闻
                    mNewsListAdapter.mList.addAll(array.data);

                    /**
                     * 注意：
                     * mNewsListAdapter.mList=array.data;
                     * 如果每次加载20条，总共有21条数据，上拉刷新一次页面只展示一条数据
                     *
                     */

                    //刷新适配器
                    mNewsListAdapter.notifyDataSetChanged();
                }

                //关闭上拉加载  下拉刷新
                mNewsList.stopLoadMore();
                mNewsList.stopRefresh();

            }

            @Override
            public void failed(Response response) {
                Toast.makeText(activity,"失败!", Toast.LENGTH_SHORT).show();
                //关闭上拉加载  下拉刷新
                mNewsList.stopLoadMore();
                mNewsList.stopRefresh();
            }
        });

    }
    /**
     *请求新闻分类的接口（get）
     * 用OkHttp,下载okhttp
     */
    public void getHttpNewsSort(){
        //1.实例化OkHttpClient的对象
        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build();
        //2.新建一个请求
        Request request=new  Request.Builder().url(ServeUrl.NEWS_SORT+"?ver=0000000&imei=865982029080549").get().build();
        //3.加入请求
        Call call=client.newCall(request);
        //4.执行请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {//请求失败

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {//请求失败
                ResponseBody body=response.body();
                Log.e("azaz","onResponse***"+body.string());

                handler.sendEmptyMessage(0);

            }
        });
    }


    /**
     *点击子条目跳转到新闻的详细界面
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(activity, NewsDetailActivity.class);
        //因为评论模块需要新闻id，所以要传news到跳转界面
        //注意position是从1开始的
        News news=mNewsListAdapter.mList.get(position-1);
        //因为要传news，News必须实现可序列化接口Serializable
        intent.putExtra("data",news);
        startActivity(intent);
    }


}
