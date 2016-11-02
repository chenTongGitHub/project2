package edu.feicui.test.everydaynews.entity;

import java.util.ArrayList;

/**
 * 解析评论的最外层实体
 * Created by Administrator on 16-10-12.
 */
public class CommentInfo {
    public int status;
    public String message;
    public ArrayList<CommentData> data;

    @Override
    public String toString() {
        return "CommentInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
