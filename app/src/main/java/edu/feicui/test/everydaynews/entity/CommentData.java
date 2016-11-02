package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-12.
 */
public class CommentData {
    public int cid;//评论id
    public String uid;//用户名
    public String portrait;//用户头像链接
    public String stamp;//评论的发布时间
    public String content;//评论的发布内容

    @Override
    public String toString() {
        return "CommentData{" +
                "cid=" + cid +
                ", uid='" + uid + '\'' +
                ", portrait='" + portrait + '\'' +
                ", stamp='" + stamp + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
