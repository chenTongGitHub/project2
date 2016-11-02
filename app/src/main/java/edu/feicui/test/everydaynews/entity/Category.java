package edu.feicui.test.everydaynews.entity;

import android.graphics.drawable.Drawable;

/**
 * 主界面左侧分类的实例
 * Created by Administrator on 16-10-8.
 */
public class Category {
    public int icon;//图标
    public String typeName;//分类名
    public String typeEnglishName;//分类英文名

    public Category(int icon, String typeName, String typeEnglishName) {
        this.icon = icon;
        this.typeName = typeName;
        this.typeEnglishName = typeEnglishName;
    }
}
