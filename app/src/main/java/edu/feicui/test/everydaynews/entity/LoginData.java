package edu.feicui.test.everydaynews.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 16-10-9.
 */
public class LoginData implements Serializable {
    public int result;
    public String explain;
    public String token;

    @Override
    public String toString() {
        return "LoginData{" +
                "result=" + result +
                ", explain='" + explain + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
