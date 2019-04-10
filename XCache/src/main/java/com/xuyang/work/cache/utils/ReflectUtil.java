package com.xuyang.work.cache.utils;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.List;

/**
 * 反射工具类
 *
 * @author Joeson Chan<xueguichen@lexin.com>
 */
@Slf4j
public final class ReflectUtil {

    private static Method CLASSLOADER_DEFINE_CLASS_METHOD = null;

    static {
        try {
            CLASSLOADER_DEFINE_CLASS_METHOD = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            CLASSLOADER_DEFINE_CLASS_METHOD.setAccessible(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    ;





    /**
     * 方法本地变量索引和参数名封装
     */
    static class LocalVariable implements Comparable<LocalVariable> {
        public int index;
        public String name;

        public LocalVariable(int index, String name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public int compareTo(LocalVariable o) {
            return this.index - o.index;
        }
    }

    /**
     * 封装方法描述和参数个数
     */
    static class EnclosingMetadata {
        public String name;
        public String desc;
        public int size;

        public EnclosingMetadata(String name, String desc, int size) {
            this.name = name;
            this.desc = desc;
            this.size = size;
        }
    }

    /**
     * List<T> list 泛型属性将获取不到
     *
     * @param field
     * @return
     */
    public static List<Class> getFieldParameterizedClass(Field field) {
        if (null == field) {
            return CollectionUtil.emptyList();
        }

        List<Class> result = Lists.newArrayList();
        Type t = field.getGenericType();
        if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
            for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
                result.add((Class) t1);
            }
        }

        return result;
    }

    public static List<Field> getFields(Class clazz) {
        return getFields(clazz, false);
    }

    public static List<Field> getFields(Class clazz, boolean inherit) {
        return getFields(clazz, inherit, Object.class);
    }

    public static List<Field> getFields(Class clazz, boolean inherit, Class excludeSuperClass) {
        if (null == clazz) {
            return CollectionUtil.emptyList();
        }

        excludeSuperClass = null != excludeSuperClass ? excludeSuperClass : Object.class;
        if (clazz.equals(Object.class)) {
            return CollectionUtil.emptyList();
        } else {
            List<Field> fields = Lists.newArrayList(clazz.getDeclaredFields());
            if (inherit) {
                fields.addAll(getFields(clazz.getSuperclass(), inherit, excludeSuperClass));
            }
            return fields;
        }
    }

    /**
     * <pre>
     * 1、如果是局部变量
     * new Array<User>(){}.class 可以获取到，new Array<User>()获取不到泛型类型
     * new HashMap<Integer, User>(){}可以获取到，new HashMap<Integer, User>()获取不到泛型类型
     * 2、如果是对象属性
     * List<T> list 泛型属性将获取不到
     *
     * Java泛型有这么一种规律:
     * 位于声明一侧的，源码里写了什么到运行时就能看到什么；
     * 位于使用一侧的，源码里写什么到运行时都没了。
     * </pre>
     *
     * @param clazz
     * @return
     */
    public static VariableType getVariableType(Class<?> clazz) {
        if (null == clazz) {
            throw new IllegalArgumentException("clazz can not be null");
        }
        Type genType = clazz.getGenericSuperclass();

        return getVariableType(genType);
    }

    /**
     * 获取第 index 个参数的泛型类型
     * <pre>
     *     eg:
     *     void test(Map<Intege, String> map){
     *         ...
     *     }
     *     上面这个 方法 index=0 情况获取 map的泛型类型[Integer, String]
     * </pre>
     *
     * @return
     */
    public static List<Class<?>> getMethodParamterGenericType(Method method, int index) {
        if (null == method) {
            throw new IllegalArgumentException("method can not be null");
        }
        if (index < 0) {
            throw new IllegalArgumentException("index can not be null");
        }

        Type[] genericParameterTypes = method.getGenericParameterTypes();
        List<Class<?>> result = Lists.newArrayList();
        if (index >= genericParameterTypes.length) {
            return result;
        }


        Type genericParameterType = genericParameterTypes[index];
        if (genericParameterType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericParameterType;
            Type[] parameterArgTypes = aType.getActualTypeArguments();
            for (Type parameterArgType : parameterArgTypes) {
                result.add((Class<?>) parameterArgType);
            }
        }
        return result;
    }

    /**
     * 获取方法 参数类型
     *
     * @param method
     * @return
     */
    public static List<VariableType> getMethodParamterType(Method method) {
        if (null == method) {
            throw new IllegalArgumentException("method can not be null");
        }

        Type[] genericParameterTypes = method.getGenericParameterTypes();
        List<VariableType> result = Lists.newArrayListWithExpectedSize(genericParameterTypes.length);
        for (Type genericParameterType : genericParameterTypes) {
            result.add(getVariableType(genericParameterType));
        }
        return result;
    }

    /**
     * 获取字段类型，以及泛型类型
     *
     * @param field
     * @return
     */
    public static VariableType getFieldType(Field field) {
        if (null == field) {
            throw new IllegalArgumentException("field can not be null");
        }

        return getVariableType(field.getGenericType());
    }

    /**
     * 获取返回值类型及泛型类型
     *
     * @param method
     * @return
     */
    public static VariableType getMethodReturnType(Method method) {
        if (null == method) {
            throw new IllegalArgumentException("method can not be null");
        }

        return getVariableType(method.getGenericReturnType());
    }

    public static VariableType getVariableType(Type type) {
        if (null == type) {
            return null;
        }

        VariableType result = null;

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            result = new VariableType((Class) parameterizedType.getRawType());
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                result.addParameterType(getVariableType(typeArgument));
            }
        } else if (type instanceof GenericArrayType) {

            GenericArrayType genericArrayType = (GenericArrayType) type;
            result = new VariableType((Class) genericArrayType.getGenericComponentType());
//            Type[] typeArguments = genericArrayType.();
//            for (Type typeArgument : typeArguments) {
//                result.addParameterType(getVariableType(typeArgument));
//            }
            log.info("type{} is GenericArrayType", type);
        } else if (type instanceof GenericArrayType) {

        } else if (type instanceof Class) {
            result = new VariableType((Class) type);
        }
        return result;
    }

    /**
     * 参数类型
     */
    @Data
    @ToString
    public static class VariableType {
        public VariableType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public void addParameterType(VariableType parameterType) {
            if (null == parameterType) {
                return;
            }

            if (null == this.parameterTypeList) {
                this.parameterTypeList = Lists.newArrayList();
            }
            this.getParameterTypeList().add(parameterType);
        }

        /**
         * 参数{#link Class}
         */
        private Class<?> clazz;
        /**
         * 泛型参数类型，如Map<Integer, String> ,这两个参数类型为Integer，String
         */
        private List<VariableType> parameterTypeList;
    }



    /**
     * @param classLoader
     * @param clazzName
     * @param bytes
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Class<?> defineClass(ClassLoader classLoader, String clazzName, byte[] bytes) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (StringUtil.isEmpty(clazzName)) {
            throw new IllegalArgumentException("clazzName can not be empty");
        }

        return (Class<?>) CLASSLOADER_DEFINE_CLASS_METHOD.invoke(classLoader, clazzName, bytes, 0, bytes.length);
    }
}


