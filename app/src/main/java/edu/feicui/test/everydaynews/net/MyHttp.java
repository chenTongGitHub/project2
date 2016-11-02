package edu.feicui.test.everydaynews.net;

import android.content.Context;

import java.util.Map;

/**
 * 网络请求类
 * Created by Administrator on 16-9-22.
 */
public class MyHttp {
    /**
     * get
     * @param context 为了与activity产生关联
     * @param url     ?前的链接：根链接+要链接的数据文件名
     * @param params  键值对，参数
     * @param mListener  创建的接口的对象，不管有没有请求成功，回应结果
     */
    public static void get(Context context,String url, Map<String,String> params,OnResultFinishListener mListener){
        //进行网络请求
        Request request=new Request();
        request.params=params;
        request.type=Constants.GET;
        request.url=url+Utils.getUrl(params,Constants.GET);//拼接完整的url
        //请求
        NetAsync async=new NetAsync(context,mListener);
        async.execute(request);

    }

    /**
     * post
     * @param context 为了与activity产生关联
     * @param url      ?前的链接：根链接+要链接的数据文件名
     * @param params   键值对，参数
     * @param mListener  创建的接口的对象，不管有没有请求成功，回应结果
     */
    public static void post(Context context,String url, Map<String,String> params,OnResultFinishListener mListener){
        //进行网络请求
        Request request=new Request();
        request.params=params;
        request.type=Constants.POST;
        request.url=url;

        //请求
        NetAsync async=new NetAsync(context,mListener);
        async.execute(request);

    }
}
