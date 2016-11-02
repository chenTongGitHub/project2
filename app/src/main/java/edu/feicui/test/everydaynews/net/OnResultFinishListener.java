package edu.feicui.test.everydaynews.net;

/**
 * 创建一个接口，表示不管有没有请求成功，回应结果
 * Created by Administrator on 16-9-23.
 */
public interface OnResultFinishListener {
     void success(Response response);
     void failed(Response response);




}
