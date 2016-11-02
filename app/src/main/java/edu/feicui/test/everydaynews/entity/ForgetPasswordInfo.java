package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-9.
 */
public class ForgetPasswordInfo {
    public int status;
    public String message;
    public ForgetPasswordData data;

    @Override
    public String toString() {
        return "ForgetPasswordInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
