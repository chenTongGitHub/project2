package edu.feicui.test.everydaynews.net;

/**
 * 结果
 * Created by Administrator on 16-9-23.
 */
public class Response {
    public int code;
    public Object result;

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", result=" + result +
                '}';
    }
}
