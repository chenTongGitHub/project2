package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-13.
 */
public class NewsCommentNumInfo {
    public int status;
    public String message;
    public int data;

    @Override
    public String toString() {
        return "NewsCommentNumInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
