package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-10.
 */
public class UserInfo {
    public int status;
    public String message;
    public UserData data;

    @Override
    public String toString() {
        return "UserInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
