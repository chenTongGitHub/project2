package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-10.
 */
public class LoginLogInfo {
    public String time;
    public String address;
    public int device;

    @Override
    public String toString() {
        return "LoginLogInfo{" +
                "time=" + time +
                ", address='" + address + '\'' +
                ", device=" + device +
                '}';
    }
}
