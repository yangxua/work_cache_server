package com.xuyang.work.cache.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Joeson Chan
 */
@Slf4j
public class NumberUtil {

    public static boolean nullOrZero(Integer value) {
        return null == value || 0 == value;
    }

    public static boolean nullOrZero(Long value) {
        return null == value || 0L == value;
    }

    public static boolean equals(Integer val1, Integer val2) {
        if (null == val1 && null == val2) {
            return true;
        }

        return null != val1 && val1.equals(val2);
    }

    public static boolean equals(Long val1, Long val2) {
        if (null == val1 && null == val2) {
            return true;
        }

        return null != val1 && val1.equals(val2);
    }

    public static int intValue(Integer value){
        if(null == value){
            return 0;
        }

        return value.intValue();
    }

    public static int toInt(Long val) {
        if (null == val) {
            return 0;
        }

        return val.intValue();
    }

    /**
     * 小数取整，非法返回0
     *
     * @param str
     * @return
     */
    public static int toInt(String str) {
        if (StringUtil.isEmpty(str)) {
            return 0;
        }

        str = str.trim();
        int dotIndex = str.indexOf(".");
        if (dotIndex == -1) {
            str = str.substring(0, str.length());
        } else {
            str = str.substring(0, dotIndex);
        }

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    public static long longValue(Long val){
        if(null == val){
            return 0L;
        }

        return val.longValue();
    }


    public static long toLong(Integer val) {
        if (null == val) {
            return 0L;
        }

        return new Long(val);
    }

    public static long toLong(Double val){
        if(null == val){
            return 0L;
        }

        return Math.round(val);
    }

    public static long toLong(String val){
        if(StringUtil.isEmpty(val)){
            return 0L;
        }

        try{
            return Long.parseLong(val);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    public static double toDouble(Long val){
        if(null == val){
            return 0D;
        }

        return (double)val;
    }

    public static double toDouble(String val){
        if(StringUtil.isEmpty(val)){
            return 0D;
        }

        try{
            return Double.parseDouble(val);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return 0D;
        }
    }


}
