package edu.feicui.test.everydaynews.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 标准日期转换工具（将毫秒转化成标准时间格式）
 * Created by Administrator on 16-10-10.
 */
public class DateFormatUtil {
    public static String getDateToFormat(long  millisecond){
        Date date=new Date(millisecond);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);

    }

}
