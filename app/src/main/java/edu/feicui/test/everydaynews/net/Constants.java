package edu.feicui.test.everydaynews.net;

import android.os.Environment;

import java.io.File;

/**
 * 静态常量
 * Created by Administrator on 16-9-22.
 */
public class Constants {
    /**
     * 封装get post 所需的常量
     */
    public static final int GET=10;
    public static final int POST=11;
    public static final int CONNECT_TIMEOUT=5000;
    public static final int READ_TIMEOUT=5000;



    /**
     * 请求相机权限的请求码，目的为了区分请求返回的结果的（多个请求时）
     */
    public static final int PERMISSION_CAMERA=200;
    /**
     * 回传照片数据时要指定存放照片的路径，需要写的权限
     */
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE=201;
    /**
     * 相机所拍照片的存储文件夹路径
     */
    public static final String PHOTO_FOLDER_PATH= Environment.getExternalStorageDirectory().getPath()+ File.separator+"EveryDayNews";
    /**
     * 相机所拍照片的文件路径
     */
    public static final String PHOTO_FILE_PATH= PHOTO_FOLDER_PATH+ File.separator+"photo.jpg";
    /**
     * 跳转相机的请求码
     */
    public static final int GOTO_CAMERA=300;
    /**
     * 跳转图库的请求码
     */
    public static final int GOTO_PICTURE=301;


    /**
     * Fragment的代码
     *
     */
    //新闻碎片
    public static final int NEWS_FRAGMENT=400;
    //注册碎片
    public static final int REGISTER_FRAGMENT=401;
    //忘记密码碎片
    public static final int FORGET_FRAGMENT=402;
    //登录碎片
    public static final int LOGIN_FRAGMENT=403;
    //收藏碎片
    public static final int FAVORITE_FRAGMENT=404;
    //本地碎片
    public static final int LOCAL_FRAGMENT=405;
    //跟帖碎片
    public static final int COMMENT_FRAGMENT=406;
    //图片碎片
    public static final int PHOTO_FRAGMENT=407;


}
