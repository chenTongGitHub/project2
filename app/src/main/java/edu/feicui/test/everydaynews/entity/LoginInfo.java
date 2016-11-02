package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-9.
 */
public class LoginInfo {
    public int status;
    public String message;
    public LoginData data;

    @Override
    public String toString() {
        return "LoginInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
