package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-9.
 */
public class RegisterInfo {
    public int status;
    public String message;
    public RegisterData data;

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
