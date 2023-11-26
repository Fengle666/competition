package com.yjq.programmer.util;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-26 19:09
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 验证字符串是否为空
     * @return
     */
    public static boolean isEmpty(String str) {
        if(str == null || "".equals(str)) {
            return true; //为空
        }else {
            return false; //不为空
        }
    }

    /**
     * 判断后缀是否是图片文件的后缀
     * @param suffix
     * @return
     */
    public static boolean isPhoto(String suffix){
        if("jpg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("png".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("gif".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("jpeg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 返回指定格式的日期字符串
     * @param date
     * @param formatter
     * @return
     */
    public static String getFormatterDate(Date date, String formatter){
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }

    /**
     * 字符串转时间类型
     * @param str_date
     * @param formatter
     * @return
     */
    public static Date getFormatterDate(String str_date, String formatter){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
        try{
            Date date = simpleDateFormat.parse(str_date);
            return date;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
