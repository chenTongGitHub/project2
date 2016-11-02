package edu.feicui.test.everydaynews.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.adapter.MyAccountLoginLogAdapter;
import edu.feicui.test.everydaynews.entity.LoginData;
import edu.feicui.test.everydaynews.entity.LoginLogInfo;
import edu.feicui.test.everydaynews.entity.SomeImportentData;
import edu.feicui.test.everydaynews.entity.UserData;
import edu.feicui.test.everydaynews.entity.UserImageInfo;
import edu.feicui.test.everydaynews.entity.UserInfo;
import edu.feicui.test.everydaynews.net.Constants;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;

/**
 * 登陆成功，“我的账户”界面
 * Created by Administrator on 16-10-9.
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 头像
     */
    ImageView mPhoto;
    /**
     * 昵称
     */
    TextView mName;
    /**
      */
    TextView mIntegral;
    /**
     * 跟帖
     */
    TextView mComment;
    /**
     * 日志
     */
    RecyclerView mLogRecycler;
    /**
     * 退出按钮
     */
    Button mBntExit;
    /**
     * 创建PopupWindow对象
     */
    PopupWindow mPopupWindow;
    /**
     * PopupWindow的view
     */
    View mView;
    /**
     * PopupWindow的相机拍照
     */
    LinearLayout mLLCamera;
    /**
     * PopupWindow的从图库选
     */
    LinearLayout mLLPhoto;
    /**
     * 从登陆界面拿到的数据
     */
    LoginData data;

    /**
     *请求用户中心数据，解析返回来的数据
     *
     */
    UserInfo info;
    /**
     * 根据解析出来的数据，拿到用户具体的数据，此处用于上传头像
     */
    UserData mUserData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
    }

    @Override
    void initView() {
        mTvTittle.setText("我的账号");
        mIvLeft.setImageResource(R.mipmap.back);
        mIvRight.setVisibility(View.GONE);
        mBntComments.setVisibility(View.GONE);



        mPhoto= (ImageView) findViewById(R.id.iv_myaccount_photo);
        mName= (TextView) findViewById(R.id.tv_myaccount_name);
        mIntegral= (TextView) findViewById(R.id.tv_myaccount_integral);
        mComment= (TextView) findViewById(R.id.tv_myaccount_comment);
        mLogRecycler= (RecyclerView) findViewById(R.id.rv_myaccount_log_recycler);
        mBntExit= (Button) findViewById(R.id.bnt_myaccount_exit);


        //1.拿登录界面传过来的数据
        Intent intent=getIntent();
        data= (LoginData) intent.getSerializableExtra("data");

        getHttpData();


        /**
         * 将PopupWindow的view填充
         * PopupWindow(View contentView, int width, int height)
         * @param width the popup's width
         * @param height the popup's height
         */
        mView=getLayoutInflater().inflate(R.layout.item_myaccount_popup,null);
        mPopupWindow=new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        mLLCamera= (LinearLayout)mView.findViewById(R.id.ll_myaccount_camera);
        mLLPhoto= (LinearLayout)mView.findViewById(R.id.ll_myaccount_photo);

        setOnClickListener(this,mIvLeft,mPhoto,mLLCamera,mLLPhoto,mBntExit);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_layout_base_top_left://返回
                finish();
                break;
            case R.id.iv_myaccount_photo://弹出PopupWindow，用于选择是从相机还是图库中拿头像
                //设置外部可点击:setOutsideTouchable和setBackgroundDrawable一般配套用
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

                /**
                 * showAtLocation,基于整个窗口显示PopupWindow
                 * showAtLocation(View parent, int gravity, int x, int y)
                 *  @param parent  整个布局中任何一个控件或容器
                 *  @param gravity 窗口中的位置
                 *  @param x  x轴偏移量
                 *  @param y y轴偏移量
                 */
                mPopupWindow.showAtLocation(mPhoto, Gravity.BOTTOM,0,0);

                //showAsDropDown,基于某一个控件显示PopupWindow，默认从左下角开始
               // mPopupWindow.showAsDropDown(mPhoto,0,0);
            break;
            case R.id.ll_myaccount_camera://跳转到相机
//                /**
//                 * API 23   Android6.0以下手机直接加权限，直接跳转
//                 */
//                Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivity(intent1);


                /**
                 * API 23
                 * Android6.0 手机的权限申请
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//checkSelfPermission方法跳出来的，判断API大于等于23
                    //检查应用是否拥有此权限(相机权限；回传照片数据时要指定存放照片的路径，需要写的权限)

                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){//如果有权限
                         goToCamera();

                    }else{//如果没有权限，申请
                        /**
                         * requestPermissions(@NonNull String[] permissions, int requestCode)
                         * @param permissions 可以申请多个权限
                         * @param requestCode 建一个静态常量，当做请求码
                         */
                         requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_CAMERA);
                    }
                }else{//API 小于23，直接跳到相机
                    goToCamera();
                }


                break;
            case R.id.ll_myaccount_photo://跳转到图库
                /**
                 * 1.相册的意图
                 *   Intent.ACTION_PICK
                 * 2.同时需要指定Uri
                 *   MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                 */

                Intent intent2=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2,Constants.GOTO_PICTURE);

                break;
            case R.id.bnt_myaccount_exit://退出登录
                Intent intent3=new Intent(this,HomeActivity.class);
                //将用户令牌清空
                SomeImportentData.token=null;
                startActivity(intent3);
                break;


        }

    }

    /**
     * 请求权限的结果(请求相机权限、写权限)
     * @param requestCode   请求码
     * @param permissions   请求的权限
     * @param grantResults  请求的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Constants.PERMISSION_CAMERA://相机权限的请求
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){//同意给相机权限和写的权限
                    goToCamera();
                }else{//拒绝给相机权限
                    Toast.makeText(this,"打开相机需要权限  权限管理-->应用-->相应权限",Toast.LENGTH_SHORT).show();
                }
            break;

        }
    }

    /**
     *拿到回传数据的结果
     * @param requestCode  请求码  区分请求
     * @param resultCode   结果的返回码  区分请求是否成功
     * @param data  回传数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.GOTO_CAMERA://相机的图片回传的请求
                if (resultCode==RESULT_OK){//请求成功，读取图片路径
                    /**
                     * 注意：有些手机照片像素太高，用Bitmap展示不出来，建议用第三方jar包Glide
                     */
//                    Bitmap bit= BitmapFactory.decodeFile(Constants.PHOTO_FILE_PATH);
//                    mPhoto.setImageBitmap(bit);

                    Glide.with(this).load(new File(Constants.PHOTO_FILE_PATH)).into(mPhoto);
                    SomeImportentData.portrait=null;
                    SomeImportentData.portrait=Constants.PHOTO_FILE_PATH;
                    getHttpPortrait();
                }

            break;
            case Constants.GOTO_PICTURE://图库的图片回传请求
                if (resultCode==RESULT_OK){//请求成功

                    //数据资源定位  拿到图片选择结果  通过内容提供者
                    Uri uri=data.getData();
                    //需要进行查询（拿到系统中图片的路径）
                    //query方法的限制需要数组
                    String[] filePathColumn={MediaStore.Images.Media.DATA};
                    Cursor curs=getContentResolver().query(uri,filePathColumn,null,null,null);
                    curs.moveToFirst();
                    int columIndex=curs.getColumnIndex(filePathColumn[0]);
                    String path=curs.getString(columIndex);
                    Log.e("aa*", "onActivityResult: "+path);
//                    图片太大，用第三方jar包
//                    try {
//                        Bitmap bit=BitmapFactory.decodeStream(new FileInputStream(path));
//                        mPhoto.setImageBitmap(bit);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    Glide.with(this).load(new File(path)).into(mPhoto);
                    SomeImportentData.portrait=path;
                    getHttpPortrait();
                }
            break;

        }
    }

    /**
     * 跳转到系统相机
     */
      public void goToCamera(){
          Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          //在跳转的同时，将照片储存到指定路径，便于回传照片

          //1.指定一个手机SD卡路径
          File file=new File(Constants.PHOTO_FOLDER_PATH);

          if (!file.exists()){//如果不存在，先创建文件夹
              /**
               * mkdir（）创建此抽象路径名称指定的目录（及只能创建一级的目录，且需要存在父目录）
               *
               * mkdirs（）创建此抽象路径指定的目录，包括所有必须但不存在的父目录。（及可以创建多级目录，无论是否存在父目录）
               */
              file.mkdirs();
          }
          //向相机传递文件路径
          intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.PHOTO_FILE_PATH)));
          //跳转回传
          startActivityForResult(intent1,Constants.GOTO_CAMERA);
      }

    /**
     * 请求用户中心网络数据
     * user_home?ver=版本号&imei=手机标识符&token =用户令牌
     */
    public void getHttpData(){

        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("imei",SomeImportentData.IMEI);
        params.put("token",data.token);//最好是从登录成功之后拿到用户令牌

        MyHttp.get(this, ServeUrl.USER_HOME, params, new OnResultFinishListener() {
            @Override
            public void success(Response response) {
                Gson gson=new Gson();
                info=gson.fromJson(response.result.toString(), UserInfo.class);
                Log.e("aac","UserInfo***"+info);
                if (info.status==0){
                    mUserData=info.data;
                    mName.setText(mUserData.uid);
                    Glide.with(MyAccountActivity.this).load(mUserData.portrait).into(mPhoto);
                    mIntegral.setText("积分： "+mUserData.integration);
                    mComment.setText(mUserData.comnum+"");

                    //拿到用户姓名、头像
                    SomeImportentData.userName=mUserData.uid;



                    //1.拿到RecyclerView所展示的数据
                    ArrayList<LoginLogInfo> mLogList=mUserData.loginlog;
                    //2.设置管理器   线性布局管理器LinearLayoutManager
                    mLogRecycler.setLayoutManager(new LinearLayoutManager(MyAccountActivity.this));
                    //3.绑定适配器
                    MyAccountLoginLogAdapter adapter=new MyAccountLoginLogAdapter(MyAccountActivity.this,mLogList);
                    mLogRecycler.setAdapter(adapter);


                }

            }

            @Override
            public void failed(Response response) {
                Toast.makeText(MyAccountActivity.this,"失败!", Toast.LENGTH_SHORT).show();

            }
        });

    }
    /**
     * 请求将回传回来的图像上传
     * token=用户令牌& portrait =头像
     */
    public void getHttpPortrait(){
        Map<String,String> params=new HashMap<>();
        params.put("token",data.token);
        params.put("portrait",mUserData.portrait);
        Log.e("aaaa","portrait***"+mUserData.portrait);
        MyHttp.get(this, ServeUrl.USER_IMAGE, params, new OnResultFinishListener() {
            @Override
            public void success(Response response) {
                Gson gson=new Gson();
                UserImageInfo imageInfo=gson.fromJson(response.result.toString(), UserImageInfo.class);
                Log.e("aaaa","UserImageInfo***"+imageInfo);
                Toast.makeText(MyAccountActivity.this,"头像上传成功!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failed(Response response) {
                Toast.makeText(MyAccountActivity.this,"头像上传失败!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
