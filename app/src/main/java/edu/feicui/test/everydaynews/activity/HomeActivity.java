package edu.feicui.test.everydaynews.activity;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.activity.fragment.FavoriteFragment;
import edu.feicui.test.everydaynews.activity.fragment.ForgetPasswordFragment;
import edu.feicui.test.everydaynews.activity.fragment.LoginFragment;
import edu.feicui.test.everydaynews.activity.fragment.NewsFragment;
import edu.feicui.test.everydaynews.activity.fragment.RegisterFragment;
import edu.feicui.test.everydaynews.adapter.HomeLeftRecyclerAdapter;
import edu.feicui.test.everydaynews.entity.Category;
import edu.feicui.test.everydaynews.entity.SomeImportentData;
import edu.feicui.test.everydaynews.entity.UpdateInfo;
import edu.feicui.test.everydaynews.entity.UserImageInfo;
import edu.feicui.test.everydaynews.net.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 主界面
 * Created by Administrator on 16-9-27.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 新建一个新闻列表的碎片，加到主界面
     */
    NewsFragment mNewsFragment=new NewsFragment();
    /**
     * 新建一个注册的碎片，加到主界面
     */
    RegisterFragment mRegisterFragment=new RegisterFragment();
    /**
     * 新建一个忘记密码的碎片，加到主界面
     */
    ForgetPasswordFragment mForgetFragment=new ForgetPasswordFragment();
    /**
     * 新建一个登录的碎片，加到主界面
     */
    LoginFragment mLoginFragment=new LoginFragment();
    /**
     * 新建一个收藏的碎片，加到主界面
     */
    FavoriteFragment mFavoriteFragment=new FavoriteFragment();
    /**
     * 左右侧滑界面
     */
    public DrawerLayout mDrawerLayout;
    /**
     * 左侧滑界面
     */
    public LinearLayout mHomeLeft;
    /**
     * 左侧recyclerView
     */
    RecyclerView mRecyclerView;

    /**
     * 右侧滑界面
     */
    public LinearLayout mHomeRight;
    /**
     * 右侧滑中的登录界面
     */
    ImageView mIvLogin;
    /**
     * 右侧滑中的用户名
     */
    TextView mTvName;
    /**
     * 右侧滑中的版本更新
     */
    TextView mTvUpdateVersion;
    /**
     * 分享按键
     */
    LinearLayout mLlShare;
    /**
     * FragmentManager:管理Activity中的fragments
     */
    FragmentManager fragmentManager;
    /**
     * 主界面左侧分类的数据源
     */
    ArrayList<Category> mList=new ArrayList<>();
    /**
     * 主界面左侧分类
     */
    int[] array={Constants.NEWS_FRAGMENT,Constants.FAVORITE_FRAGMENT,Constants.LOCAL_FRAGMENT,Constants.COMMENT_FRAGMENT,Constants.PHOTO_FRAGMENT};

    /**
     *展开主界面左侧的计数器
     */
    int a=0;
    /**
     *展开主界面右侧的计数器
     */
    int b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //第三方分享shareSDK初始化
        ShareSDK.initSDK(this,"18206cdc58ae4");

        /**
         * 左右滑动，新布局DrawerLayout
         * 里面需要有一个控件宽度充满
         */
    }

    /**
     * BaseActivity中优化的抽象方法
     */
    @Override
    void initView() {
        mTvTittle.setText("资讯");
        mBntComments.setVisibility(View.GONE);



        //添加一个碎片
        /**
         * 1.拿到FragmentManager对象
         * FragmentManager:管理Activity中的fragments
         *app-->Activity-->this.getFragmentManager();
         *v4-->FragmentActivity-->getSuportFragmnetManager()
         */
        fragmentManager=getSupportFragmentManager();
        /**
         * 2.需要一个事务对象 (每一次操作都需要是一个新事务)
         */
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        /**
         * 3.操作   添加 替换  移除
         * add(@IdRes int containerViewId, Fragment fragment);
         * containerViewId  装碎片的容器的id
         * fragment 碎片
         */
        transaction.add(R.id.ll_home_content,mNewsFragment);

        /**
         * 4.事务需要提交
         */
        transaction.commit();



        mDrawerLayout= (DrawerLayout) findViewById(R.id.dralayout_home);
        mHomeLeft= (LinearLayout) findViewById(R.id.ll_home_left);
        mHomeRight= (LinearLayout) findViewById(R.id.ll_home_right);
        //主界面左侧recyclerView
        mRecyclerView= (RecyclerView) findViewById(R.id.rv_home_left);

        mIvLogin= (ImageView) findViewById(R.id.iv_home_right_login);
        mTvName= (TextView) findViewById(R.id.tv_home_right_name);
        mTvUpdateVersion= (TextView) findViewById(R.id.tv_home_right_update_version);
        mLlShare= (LinearLayout) findViewById(R.id.ll_home_right_share);

//        if (SomeImportentData.userName!=null){
//            Glide.with(this).load(new File(SomeImportentData.portrait)).into(mIvLogin);
//            mTvName.setText(SomeImportentData.userName);
//        }

        /**
         * 1.左边的收藏等的数据源
         * 2.布局管理器
         * 3.RecyclerView的适配器，绑定适配器
         */

        Category mNews=new Category(R.mipmap.biz_navigation_tab_news,"新闻","NEWS");
        Category mFavorite=new Category(R.mipmap.biz_navigation_tab_read,"收藏","FAVORITE");
        Category mLocal=new Category(R.mipmap.biz_navigation_tab_local_news,"本地","LOCAL");
        Category mComment=new Category(R.mipmap.biz_navigation_tab_ties,"跟帖","COMMENT");
        Category mPhoto=new Category(R.mipmap.biz_navigation_tab_pics,"图片","PHOTO");
        mList.add(mNews);
        mList.add(mFavorite);
        mList.add(mLocal);
        mList.add(mComment);
        mList.add(mPhoto);
        //布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        HomeLeftRecyclerAdapter adapter=new HomeLeftRecyclerAdapter(this,mList);
        mRecyclerView.setAdapter(adapter);
        /**
         * 适配器里写了回调接口传数据，用于在activity中绑定监听子条目
         */
        adapter.setOnItemClickListener(new HomeLeftRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position, RecyclerView.ViewHolder holder) {

                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        ChangeFragment(array[position]);
            }
        });


        setOnClickListener(this,mIvLeft,mIvRight,mIvLogin,mTvUpdateVersion,mLlShare);
        //绑定监听左右侧滑展开、关闭情况
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * 当抽屉被滑动的时候调用此方法
             * slideOffset 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
               Log.e("ASAS","onDrawerSlide---"+slideOffset);
            }
            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View drawerView) {
                Log.e("ASAS","onDrawerOpened---"+drawerView);
            }
            /**
             * 当一个抽屉完全关闭的时候调用此方法
             */
            @Override
            public void onDrawerClosed(View drawerView) {
                Log.e("ASAS","onDrawerClosed---"+drawerView);
            }
            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
             */
            @Override
            public void onDrawerStateChanged(int newState) {
                Log.e("ASAS","onDrawerStateChanged---"+newState);
            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
       switch (v.getId()){
           //跳转到登陆界面
           case R.id.iv_home_right_login:
               /**
                * 1.如果已经登录(有用户令牌)，跳转到用户中心界面
                * 2.如果没有登录，跳转到登录界面
                */
               if (SomeImportentData.token!=null){
                   intent.setClass(this,MyAccountActivity.class);
                   startActivity(intent);
               }else{
                   ChangeFragment(Constants.LOGIN_FRAGMENT);
                   mDrawerLayout.closeDrawer(Gravity.RIGHT);
               }

               break;
           case R.id.iv_layout_base_top_left:
               a++;
               switch (a%2){
                   case 0:
                       mDrawerLayout.closeDrawer(Gravity.LEFT);
                       break;
                   case 1:
                       mDrawerLayout.openDrawer(Gravity.LEFT);
                       break;
               }

               break;
           case R.id.iv_layout_base_top_right:
               b++;
               switch (b%2){
                   case 0:
                       mDrawerLayout.closeDrawer(Gravity.RIGHT);
                       break;
                   case 1:
                       mDrawerLayout.openDrawer(Gravity.RIGHT);
                       break;
               }


               break;
           case R.id.tv_home_right_update_version://版本更新
               getHttpUpdateVersion();
               break;
           case R.id.ll_home_right_share://第三方分享
               showShare();
               break;
       }

    }
    /**
     * 版本更新
     *    1.检查当前应用是否是最新版本
     *           PackageInfo   ----->拿到当前应用的版本号
     *           服务端最新版本 ----->服务端
     *    2.服务端下载   服务端需要返回一个连接
     *          http// ........a.mp4       IO流
     *
     *    3.下载这个连接   并且 提示安装 apk
     *        1.下载连接
     *
     *           A:通过流 自己读写
     *
     *           B:调用系统的下载管理器   直接使用
     *
     *              DownloadManager
     *    4.监听下载完成   并且打开此文件  隐式意图
     *       广播接收器  接收系统下载完成的一个广播
     *
     * 请求版本更新接口
     *update?imei=唯一识别号&pkg=包名&ver=版本
     * okhttp
     */
    public void getHttpUpdateVersion(){
        //拿到当前应用的版本号
        PackageManager manager;
        PackageInfo packageInfo=null;
        manager=this.getPackageManager();

        try {
            packageInfo=manager.getPackageInfo(this.getPackageName(),0); //所有信息
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //拿到版本号、版本名
        final int code=packageInfo.versionCode;
        final String versionName=packageInfo.versionName;



        //1.实例化OkHttpClient的对象
        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build();
        //2.新建一个请求
        Request request=new  Request.Builder().url(ServeUrl.UPDATE+"?imei=865982029080549&pkg=edu.feicui.test.everydaynews&ver=0000000").get().build();
        //3.加入请求
        Call call=client.newCall(request);
        //4.执行请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body=response.body();
                Log.e("home","onResponse***"+Thread.currentThread().getName().toString());
                Gson gson=new Gson();
                UpdateInfo info=gson.fromJson(body.string(), UpdateInfo.class);
                if(!info.version.equals(code+"")){//当前版本与请求到的版本作对比，不同则下载新版本
                    //调用下载方法
                    downloadAPK(info.link,"down.apk");
                }
            }
        });

    }
    /**
     * 调用系统的下载管理器下载
     * @param downloadUri 下载的地址(请求到的网络数据)
     * @param downloadPath 下载的存放的路径
     */
    public void downloadAPK(String downloadUri,String downloadPath){
        //1.拿到DownloadManager对象
        DownloadManager manager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //2.需要创建一个 下载请求
        DownloadManager.Request request=new
                DownloadManager.Request(Uri.parse(downloadUri));
        //3.做一些额外的设置
        //网络要求
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
        //设置通知栏是否显示
        request.setShowRunningNotification(true);
        request.setDestinationInExternalFilesDir(this,null,downloadPath);
        //4.下载 将请求加入请求
        Toast.makeText(this,"加入下载队列...", Toast.LENGTH_SHORT).show();
        manager.enqueue(request);
    }
    /**
     * 广播接收器  接收系统下载完成的一个广播
     */
    @Override
    protected void onResume() {
        super.onResume();
        //动态注册广播接收器
        reciver=new DownLoadReciver();
        // Action
        IntentFilter filter=new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(reciver,filter);//注册广播
    }
    DownLoadReciver reciver;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciver);//反注册广播
    }

    /**
     * 下载完成监听广播
     */
    public class DownLoadReciver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();

            //打开此apk    隐式意图
        }
    }



    /**
     * 创建一个方法，用于切换展示碎片
     * @param fragment  展示的碎片
     */
    public void ChangeFragment(int fragment){
        switch (fragment){
            case Constants.NEWS_FRAGMENT://新闻碎片
                mTvTittle.setText("资讯");
                mBntComments.setVisibility(View.GONE);
                mIvRight.setVisibility(View.VISIBLE);
                mIvLeft.setImageResource(R.mipmap.ic_title_home_default);

                FragmentTransaction transaction1=fragmentManager.beginTransaction();
                transaction1.replace(R.id.ll_home_content,mNewsFragment);
                transaction1.commit();
            break;

            case Constants.REGISTER_FRAGMENT://注册碎片
                mIvLeft.setImageResource(R.mipmap.back);
                mTvTittle.setText("用户注册");
                mBntComments.setVisibility(View.GONE);
                mIvRight.setVisibility(View.GONE);
                //绑定监听返回按钮,返回到登陆界面
                mIvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeFragment(Constants.LOGIN_FRAGMENT);
                    }
                });


                FragmentTransaction transaction2=fragmentManager.beginTransaction();
                transaction2.replace(R.id.ll_home_content,mRegisterFragment);
                transaction2.commit();
                break;

            case Constants.FORGET_FRAGMENT://忘记密码碎片
                mIvLeft.setImageResource(R.mipmap.back);
                mTvTittle.setText("忘记密码");
                mBntComments.setVisibility(View.GONE);
                mIvRight.setVisibility(View.GONE);
                //绑定监听返回按钮,返回到登陆界面
                mIvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeFragment(Constants.LOGIN_FRAGMENT);
                    }
                });


                FragmentTransaction transaction3=fragmentManager.beginTransaction();
                transaction3.replace(R.id.ll_home_content,mForgetFragment);
                transaction3.commit();
                break;

            case Constants.LOGIN_FRAGMENT://登录碎片
                mIvLeft.setImageResource(R.mipmap.back);
                mTvTittle.setText("用户登录");
                mBntComments.setVisibility(View.GONE);
                mIvRight.setVisibility(View.GONE);
                //绑定监听返回按钮
                mIvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeFragment(Constants.NEWS_FRAGMENT);
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                    }
                });


                FragmentTransaction transaction4=fragmentManager.beginTransaction();
                transaction4.replace(R.id.ll_home_content,mLoginFragment);
                transaction4.commit();
                break;

            case Constants.FAVORITE_FRAGMENT://收藏碎片
                mIvLeft.setImageResource(R.mipmap.back);
                mTvTittle.setText("收藏");
                mBntComments.setVisibility(View.GONE);
                mIvRight.setVisibility(View.GONE);
                //绑定监听返回按钮
                mIvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                FragmentTransaction transaction5=fragmentManager.beginTransaction();
                transaction5.replace(R.id.ll_home_content,mFavoriteFragment);
                transaction5.commit();
                break;

        }

    }

    /**
     * 第三方分享shareSDK
     * 调用onekeyshare的界面分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
