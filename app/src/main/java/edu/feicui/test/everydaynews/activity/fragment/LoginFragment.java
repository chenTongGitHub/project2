package edu.feicui.test.everydaynews.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.activity.HomeActivity;
import edu.feicui.test.everydaynews.activity.MyAccountActivity;
import edu.feicui.test.everydaynews.entity.LoginData;
import edu.feicui.test.everydaynews.entity.LoginInfo;
import edu.feicui.test.everydaynews.entity.SomeImportentData;
import edu.feicui.test.everydaynews.net.Constants;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;

/**
 * Created by Administrator on 16-10-13.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    /**
     * 拿到已经给Fragment加载好界面
     */
    View mView;
    /**
     * 拿到Activity,为了Fragment与Activity传数据
     */
    HomeActivity mActivity;
    /**
     * 找到输入的控件
     */
    EditText mEtName;
    EditText mEtPassword;
    /**
     * 拿到输入的信息
     */
    String mName;
    String mPassword;
    /**
     * 找到三个Button
     */
    Button mRegister;
    Button mforgetPassword;
    Button mEnter;

    /**
     * 给Fragment加载界面
     * @param inflater 布局填充器，主要加载xml  fragment
     * @param container 将碎片加入的view
     * @param savedInstanceState 与onCreat的参数一致
     * @return  Fragment的view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    /**
     * 已经给Fragment加载好界面
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //拿到已经给Fragment加载好界面
        mView=getView();
        mActivity= (HomeActivity) getActivity();
        //在 Fragment的view中id
        mEtName= (EditText) mView.findViewById(R.id.et_login_name);
        mEtPassword= (EditText) mView.findViewById(R.id.et_login_password);
        mRegister= (Button) mView.findViewById(R.id.bnt_login_register);
        mforgetPassword= (Button) mView.findViewById(R.id.bnt_login_forget_password);
        mEnter= (Button) mView.findViewById(R.id.bnt_login_enter);

        //绑定监听事件(将BaseActivity的setOnClickListener公开public)
        mActivity.setOnClickListener(this,mRegister,mforgetPassword,mEnter);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_login_register://点击注册
                mActivity.ChangeFragment(Constants.REGISTER_FRAGMENT);
            break;
            case R.id.bnt_login_forget_password://点击忘记密码
                mActivity.ChangeFragment(Constants.FORGET_FRAGMENT);

            break;
            case R.id.bnt_login_enter://点击登录
                getHttpData();
            break;
        }
    }
    /**
     * 请求登录的网络数据
     * ver=版本号&uid=用户名&pwd=密码&device=0
     */
    public void getHttpData(){
        mName=mEtName.getText().toString();
        mPassword=mEtPassword.getText().toString();
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("uid",mName);
        params.put("pwd",mPassword);
        params.put("device","0");//0为手机客户端
        if (mName.equals("")||mPassword.equals("")){//判断输入信息不能为空
            Toast.makeText(mActivity,"登录信息不能为空！",Toast.LENGTH_SHORT).show();

        }else{
            MyHttp.get(mActivity, ServeUrl.USER_LOGIN, params, new OnResultFinishListener() {
                @Override
                public void success(Response response) {
                    Gson gson=new Gson();
                    LoginInfo info=gson.fromJson(response.result.toString(), LoginInfo.class);
                    Log.e("aac","LoginInfo---"+info);
                    switch (info.status){
                        case 0:
                            Toast.makeText(mActivity,"登陆成功！",Toast.LENGTH_SHORT).show();
                            //拿到用户令牌，便于以后请求其他接口，如写评论
                            SomeImportentData.token=info.data.token;

                            Intent intent=new Intent(mActivity,MyAccountActivity.class);
                            //因为用户中心界面需要加载数据，将LoginData的对象传过去
                            LoginData mLoginData=info.data;
                            intent.putExtra("data",mLoginData);
                            startActivity(intent);
                            break;
                        case -1:
                            Toast.makeText(mActivity,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                            break;
                        case -2:
                            Toast.makeText(mActivity,"限制登陆(禁言,封IP)！",Toast.LENGTH_SHORT).show();
                            break;
                        case -3:
                            Toast.makeText(mActivity,"限制登陆(异地登陆等异常)！",Toast.LENGTH_SHORT).show();
                            break;
                    }

                    return;

                }

                @Override
                public void failed(Response response) {
                    Toast.makeText(mActivity,"失败!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}
