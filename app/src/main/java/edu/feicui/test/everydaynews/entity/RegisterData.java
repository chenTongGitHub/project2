package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-9.
 */
public class RegisterData {
    public int result;
    public String explain;
    public String token;

    @Override
    public String toString() {
        return "RegisterData{" +
                "result=" + result +
                ", explain='" + explain + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
