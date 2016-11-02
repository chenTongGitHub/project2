package edu.feicui.test.everydaynews.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 16-10-10.
 */
public class UserData {
    public String uid;
    public String portrait;
    public int integration;
    public int comnum;
    public ArrayList<LoginLogInfo> loginlog;

    @Override
    public String toString() {
        return "UserData{" +
                "uid='" + uid + '\'' +
                ", portrait='" + portrait + '\'' +
                ", integration=" + integration +
                ", comnum=" + comnum +
                ", loginlog=" + loginlog +
                '}';
    }
}
