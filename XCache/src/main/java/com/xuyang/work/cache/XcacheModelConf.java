package com.xuyang.work.cache;

import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 17:31
 * @Description:
 */
@Data
@ToString
public class XcacheModelConf {

    private Class clazz;

    private List<Field> fields;

    private String sign;
}