package edu.feicui.test.everydaynews.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.test.everydaynews.R;
import edu.feicui.test.everydaynews.ServeUrl;
import edu.feicui.test.everydaynews.activity.HomeActivity;
import edu.feicui.test.everydaynews.entity.ForgetPasswordInfo;
import edu.feicui.test.everydaynews.net.Constants;
import edu.feicui.test.everydaynews.net.MyHttp;
import edu.feicui.test.everydaynews.net.OnResultFinishListener;
import edu.feicui.test.everydaynews.net.Response;

/**
 * 忘记密码的碎片
 * Created by Administrator on 16-10-13.
 */
public class ForgetPasswordFragment extends Fragment implements View.OnClickListener {
    /**
     * 拿到已经给Fragment加载好界面
     */
    View mView;
    /**
     * 拿到Activity,为了Fragment与Activity传数据
     */
    HomeActivity mActivity;
    /**
     * TextInputLayout和TextInputEditText配套使用
     */
    TextInputLayout mTextInputLayout;
    //编辑框
    TextInputEditText mTextInputEditText;

    /**
     * 确认按钮
     */
    Button mBntEnsure;
    /**
     * 拿到输入的邮箱地址
     */
    String mEmail;
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
        mView=inflater.inflate(R.layout.fragment_forget_password,container,false);
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
        //拿到activity
        mActivity= (HomeActivity) getActivity();
        //在 Fragment的view中id
        mTextInputLayout= (TextInputLayout)mView.findViewById(R.id.til_email);
        mTextInputEditText= (TextInputEditText) mView.findViewById(R.id.textedit_email);
        mBntEnsure= (Button) mView.findViewById(R.id.bnt_forget_password_ensure);

        /**
         * TextInputEditText的输入监听addTextChangedListener，实现其三个方法
         * 这三个方法每输入一个字符，走一遍
         * （此处用处不大）
         */
        mTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTextInputEditText.length()>5){
                    mTextInputEditText.setError(null);
                }else{
                    mTextInputEditText.setError("输入正确的邮箱格式");
                }

            }
        });

        mBntEnsure.setOnClickListener(this);
    }

    /**
     * 点击请求网络数据，请求忘记密码接口
     * @param v
     */
    @Override
    public void onClick(View v) {
        getHttpData();
    }
    /**
     * 请求忘记密码的网络数据
     * user_forgetpass?ver=版本号&email=邮箱
     */
    public void getHttpData(){
        mEmail=mTextInputEditText.getText().toString();
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("email",mEmail);
        if (mEmail.equals("")){
            Toast.makeText(mActivity,"请输入注册时的邮箱号!", Toast.LENGTH_SHORT).show();

        }else{
            MyHttp.get(mActivity, ServeUrl.USER_FORGETPASS, params, new OnResultFinishListener() {
                @Override
                public void success(Response response) {
                    Gson gson=new Gson();
                    ForgetPasswordInfo info=gson.fromJson(response.result.toString(), ForgetPasswordInfo.class);
                    Log.e("aac","success：---"+info);
                    switch (info.data.result){
                        case 0:
                            Toast.makeText(mActivity,"密码已成功发送到邮箱!", Toast.LENGTH_SHORT).show();
                            //跳到登录界面，展示登录碎片
                            mActivity.ChangeFragment(Constants.LOGIN_FRAGMENT);
                            break;
                        case -1:
                            Toast.makeText(mActivity,"发送失败（该邮箱未注册）!", Toast.LENGTH_SHORT).show();
                            break;
                        case -2:
                            Toast.makeText(mActivity,"发送失败（邮箱不存在或被封号）!", Toast.LENGTH_SHORT).show();
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
