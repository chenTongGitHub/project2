package edu.feicui.test.everydaynews.net;

import java.util.Map;
import java.util.Set;

/**
 * 拼接问号后内容：?name=zs&pwd=128946453
 * Created by Administrator on 16-9-22.
 */
public class Utils {

    /**
     *
     * @param params  @param params   {"name":"zs","pwd":"128946453"}
     * @param type  get   post
     * @return  ?name=zs&pwd=128946453
     */
   public static String getUrl(Map<String,String> params,int type){
       StringBuffer buffer=new StringBuffer();

       if (params != null&&params.size()!=0) {
           if (type==Constants.GET){
               //post中outputStream.write(没有问号)
               buffer.append("?");
           }
           //获得Kay的集合，对应也能拿到value值get(key)
           Set<String> keysSet=params.keySet();
           for (String key:keysSet) {
               buffer.append(key).append("=").append(params.get(key)).append("&");
           }
           //拼接到最后时，没有"&",所以将其删除
            buffer.deleteCharAt(buffer.length()-1);

       }



       return buffer.toString();
   }
}
