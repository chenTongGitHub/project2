package edu.feicui.test.everydaynews;

/**
 * Created by Administrator on 16-9-28.
 */
public class ServeUrl {
    /**
     * 根链接
     */
    public static final String BASE_URL="http://118.244.212.82:9092/newsClient";
    /**
     * 新闻列表数据链接
     */
    public static final String NEWS_LIST=BASE_URL+"/news_list";
    /**
     * 新闻分类数据连接
     */
    public static final String NEWS_SORT=BASE_URL+"/news_sort";

    /**
     * 用户注册数据链接
     */
    public static final String USER_REGISTER=BASE_URL+"/user_register";
    /**
     * 用户登录数据链接
     */

    public static final String USER_LOGIN=BASE_URL+"/user_login";
    /**
     * 用户忘记密码，找回密码链接
     */
    public static final String USER_FORGETPASS=BASE_URL+"/user_forgetpass";
    /**
     * 用户中心数据链接
     */
    public static final String USER_HOME=BASE_URL+"/user_home";
    /**
     * 用户中心图像上传数据链接
     */
    public static final String USER_IMAGE=BASE_URL+"/user_image";
    /**
     * 用户评论展示数据链接
     */
    public static final String CMT_LIST=BASE_URL+"/cmt_list";
    /**
     * 用户发布评论数据链接
     */
    public static final String CMT_COMMIT=BASE_URL+"/cmt_commit";
    /**
     * 跟帖数量数据链接
     */
    public static final String CMT_NUM=BASE_URL+"/cmt_num";
    /**
     * 版本更新数据链接
     */
    public static final String UPDATE=BASE_URL+"/update";



}
