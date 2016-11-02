package edu.feicui.test.everydaynews.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 16-9-28.
 */
public class NewsArray {
    String message;
    int status;
    public ArrayList<News> data;

    @Override
    public String toString() {
        return "NewsArray{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
