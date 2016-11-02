package edu.feicui.test.everydaynews.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 16-9-28.
 */
public class News implements Serializable{
    public String summary;//新闻摘要
    public String icon;//新闻图片
    public String stamp;//新闻时间
    public String title;//新闻标题
    public int nid;//新闻id
    public String link;//新闻链接
    public int type;//新闻类型

    @Override
    public String toString() {
        return "News{" +
                "summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", stamp='" + stamp + '\'' +
                ", title='" + title + '\'' +
                ", nid=" + nid +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }
}
