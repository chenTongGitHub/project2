package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-26.
 */
public class UpdateInfo {
    public String pkgName;
    public String version;
    public String link;
    public String md5;

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", version='" + version + '\'' +
                ", link='" + link + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
