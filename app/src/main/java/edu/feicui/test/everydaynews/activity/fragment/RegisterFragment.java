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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.activity.HomeActivity;
import edu.feicui.test.everydaynews.entity.RegisterInfo;
import edu.feicui.test.everydaynews.net.Constants;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;

/**
 * Created by Administrator on 16-10-13.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    /**
     * 拿到已经给Fragment加载好界面
     */
    View mView;
    /**
     * 拿到Activity,为了Fragment与Activity传数据
     */
    HomeActivity mActivity;
    /**
     * 邮箱地址
     */
    EditText mEdEmailAddress;
    /**
     * 用户昵称
     */
    EditText mEdName;
    /**
     * 注册密码
     */
    EditText mEdPassword;
    /**
     * 注册按钮
     */
    Button mRegisterNow;
    /**
     * 是否同意用户协议
     */
    CheckBox mCheckBox;
    /**
     * 拿到输入的邮箱地址
     */
    String strEmail;
    /**
     * 拿到输入的用户昵称
     */
    String strName;
    /**
     * 拿到输入的登录密码
     */
    String strPassword;
    /**
     * 是否同意用户协议的开关，默认不同意
     */
    boolean flag=false;
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
        return inflater.inflate(R.layout.fragment_register,container,false);
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
        mEdEmailAddress= (EditText) mView.findViewById(R.id.et_register_email_address);
        mEdName= (EditText) mView.findViewById(R.id.et_register_name);
        mEdPassword= (EditText) mView.findViewById(R.id.et_register_password);
        mRegisterNow= (Button) mView.findViewById(R.id.bnt_register);
        mCheckBox= (CheckBox) mView.findViewById(R.id.checkbox_register);

        //mRegister点击监听
        mRegisterNow.setOnClickListener(this);
        //CheckBox的选择点击事件
        mCheckBox.setOnCheckedChangeListener(this);

    }

    /**
     * 注册按钮绑定点击监听
     *
     */
    @Override
    public void onClick(View v) {
        if (flag){//CheckBox已经点击，同意用户协议，申请注册
            getHttpData();
        }else{
            Toast.makeText(mActivity,"注册前请阅读用户协议并确认同意", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     *
     * CheckBox选择点击事件
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            //CheckBox已经点击，同意用户协议，开关置为true
            flag=true;
        }else{
            Toast.makeText(mActivity,"如果不同意用户协议将无法注册", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 正则表达式（匹配邮箱地址）
     * @param strEmail 输入的邮箱地址
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[0-9a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
    /**
     * 请求注册的网络信息
     *
     * ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
     * ver=0000000&uid=用户名&email=邮箱&pwd=登陆密码
     */
    public void getHttpData(){
        strEmail=mEdEmailAddress.getText().toString();
        strName=mEdName.getText().toString();
        strPassword=mEdPassword.getText().toString();
        //键值对，匹配参数
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("uid",strName);
        params.put("email",strEmail);
        params.put("pwd",strPassword);
        if(strEmail.equals("")||strName.equals("")||strPassword.equals("")){
            Toast.makeText(mActivity,"注册信息不能为空！",Toast.LENGTH_SHORT).show();
        }else{
            if (isEmail(strEmail)){//判断邮箱格式
                MyHttp.get(mActivity, ServeUrl.USER_REGISTER, params, new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {
                        Gson gson=new Gson();
                        //拿到结果
                        RegisterInfo info=gson.fromJson(response.result.toString(), RegisterInfo.class);
                        Log.e("aac","success：---"+info);
                        switch (info.data.result){
                            case 0:
                                Toast.makeText(mActivity,"注册成功，请登录！",Toast.LENGTH_SHORT).show();
                                mActivity.ChangeFragment(Constants.LOGIN_FRAGMENT);
                                break;
                            case -1:
                                Toast.makeText(mActivity,"服务器不允许注册！",Toast.LENGTH_SHORT).show();

                                break;
                            case -2:
                                Toast.makeText(mActivity,"用户名重复！",Toast.LENGTH_SHORT).show();

                                break;
                            case -3:
                                Toast.makeText(mActivity,"邮箱已注册！",Toast.LENGTH_SHORT).show();

                                break;
                        }
                        return;
                    }

                    @Override
                    public void failed(Response response) {
                        Toast.makeText(mActivity,"失败!", Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                Toast.makeText(mActivity,"邮箱格式不正确！",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
