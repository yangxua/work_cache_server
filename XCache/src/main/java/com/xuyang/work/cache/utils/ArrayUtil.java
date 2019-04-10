package com.xuyang.work.cache.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Joeson Chan
 */
public final class ArrayUtil {

    public static <T> int length(T... a) {
        return null == a ? 0 : a.length;
    }

    /**
     * return a new List
     */
    public static <T> List<T> asList(T... a) {
        if (null == a) {
            return CollectionUtil.emptyList();
        }

        List<T> result = Arrays.asList(a);
        return CollectionUtil.select(result, new CollectionUtil.Selector<T>() {
            @Override
            public boolean select(T t) {
                return t != null;
            }
        });
    }

    public static <T> boolean contain(T[] array, T a){
        if(length(array) == 0){
            return false;
        }

        for(T item : array){
            if(item.equals(a)){
                return true;
            }
        }

        return false;
    }

    public static <T> boolean isEmpty(T... a) {
        return null == a || a.length == 0;
    }

    public static <T> boolean isNotEmpty(T... a) {
        return !isEmpty(a);
    }

    public static <T> T getFirst(T... a) {
        if (null == a || a.length == 0) {
            return null;
        }

        return a[0];
    }

    public static <T> void forEach(T[] array, Handler handler) {
        if (null == array || array.length == 0) {
            return;
        }
        if (null == handler) {
            throw new IllegalArgumentException("handler can not be null");
        }

        for (T a : array) {
            handler.handle(a);
        }
    }


    public interface Handler<T> {
        void handle(T t);
    }
}
