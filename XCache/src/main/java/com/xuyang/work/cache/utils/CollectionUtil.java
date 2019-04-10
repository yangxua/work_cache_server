package com.xuyang.work.cache.utils;

import com.google.common.collect.*;

import java.util.*;

/**
 * Collection 工具类
 *
 * @author Joeson Chan
 * @create 2017年12月26日
 */
public final class CollectionUtil {

    private static Integer INTEGER_ONE = new Integer(1);

    public static int size(Collection collection) {
        return null != collection ? collection.size() : 0;
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.size() == 0;
    }

    public static boolean isEmpty(Set set) {
        return null == set || set.size() == 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Set set) {
        return !isEmpty(set);
    }

    public static <T> boolean contain(Collection<T> collection, T t) {
        return isNotEmpty(collection) && collection.contains(t);
    }

    /**
     * 返回在t1，不在t2的元素
     */
    public static <T> List<T> subtract(Collection<T> t1, Collection<T> t2) {
        if (isEmpty(t1)) {
            return Collections.EMPTY_LIST;
        }
        if (isEmpty(t2)) {
            return new ArrayList<>(t1);
        }

        ArrayList list = new ArrayList(t1);
        for (Iterator it = t2.iterator(); it.hasNext(); ) {
            list.remove(it.next());
        }

        return list;
    }

    /**
     * 返回交集,即在 a 也在 b 的元素
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        if (isEmpty(a) || isEmpty(b)) {
            return Collections.EMPTY_LIST;
        }

        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            for (int i = 0, m = Math.min(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
                list.add(obj);
            }
        }

        return list;
    }

    /**
     * 返回聚集
     */
    public static <T> List<T> union(Collection<T> a, Collection<T> b) {
        if (isEmpty(a)) {
            return new ArrayList<>(b);
        }
        if (isEmpty(b)) {
            return new ArrayList<>(a);
        }

        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            for (int i = 0, m = Math.max(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 交集的补集
     * <pre>
     *      String[] arrayA = new String[] { "A", "B", "C", "D", "E", "F" };
     *      String[] arrayB = new String[] { "B", "D", "F", "G", "H", "K" };
     *      List<String> listA = Arrays.asList(arrayA);
     *      List<String> listB = Arrays.asList(arrayB);
     *      //2个数组取交集 的补集
     *      System.out.println(ArrayUtils.toString(CollectionUtils.disjunction(listA, listB)));  //[A, C, E, G, H, K]
     * </pre>
     *
     * @param a
     * @param b
     * @return
     */
    public static Collection disjunction(final Collection a, final Collection b) {

        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            for (int i = 0, m = ((Math.max(getFreq(obj, mapa), getFreq(obj, mapb))) - (Math.min(getFreq(obj, mapa), getFreq(obj, mapb)))); i < m; i++) {
                list.add(obj);
            }
        }
        return list;
    }

    private static final int getFreq(final Object obj, final Map freqMap) {
        Integer count = (Integer) freqMap.get(obj);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    private static Map getCardinalityMap(final Collection coll) {
        Map count = new HashMap();
        for (Iterator it = coll.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            Integer c = (Integer) (count.get(obj));
            if (c == null) {
                count.put(obj, INTEGER_ONE);
            } else {
                count.put(obj, new Integer(c.intValue() + 1));
            }
        }
        return count;
    }


    public static <T> List<T> sub(List<T> list, int fromIndex, int toIndex) {
        if (null == list) {
            return CollectionUtil.emptyList();
        }

        return list.subList(fromIndex, toIndex);
    }

    public static <T> boolean notContain(Collection<T> collection, T t) {
        return isEmpty(collection) || !collection.contains(t);
    }

    public static <T> boolean containAll(Collection<T> collection, Collection<T> subCollection) {
        return isNotEmpty(collection) && collection.containsAll(subCollection);
    }

    public static <T> T get(List<T> list, int index) {
        if (isEmpty(list) || index < 0 || index >= list.size()) {
            return null;
        }

        return list.get(index);
    }

    public static <T> T getFirst(List<T> list) {
        return isNotEmpty(list) ? list.get(0) : null;
    }

    public static <T, F> List<F> collect(Collection<T> collection, Converter<T, F> converter) {
        return collect(collection, true, false, converter);
    }

    public static <T> List<T> select(Collection<T> collection, Selector<T> selector) {
        return select(collection, true, selector);
    }

    public static <T> List<T> select(Collection<T> collection, boolean ignoreNull, Selector<T> selector) {
        if (isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<T> result = Lists.newArrayListWithExpectedSize(collection.size());
        for (T t : collection) {
            if (null == t && ignoreNull) {
                continue;
            }

            if (selector.select(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static <T, F> List<F> collect(Collection<T> collection, boolean ignoreNull, boolean distinct, Converter<T, F> converter) {
        if (isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<F> result = Lists.newArrayListWithExpectedSize(collection.size());
        for (T t : collection) {
            F f = converter.convert(t);
            if (ignoreNull && null == f) {
                continue;
            }

            if (!distinct || !result.contains(t)) {
                result.add(f);
            }
        }

        return result;
    }

    public static <T, F> Set<F> toSet(Collection<T> collection, Converter<T, F> converter) {
        return toSet(collection, true, converter);
    }

    public static <T, F> Set<F> toSet(Collection<T> collection, boolean ignoreNull, Converter<T, F> converter) {
        if (isEmpty(collection)) {
            return Collections.emptySet();
        }

        Set<F> set = Sets.newHashSetWithExpectedSize(collection.size());
        for (T t : collection) {
            F f = converter.convert(t);
            if (ignoreNull && null == f) {
                continue;
            }

            set.add(f);
        }

        return set;
    }

    public static <T> void forEach(Collection<T> collection, Handler1<T> handler1) {
        forEach(collection, true, handler1);
    }

    public static <T> void forEach(Collection<T> collection, boolean ignoreNull, Handler1<T> handler1) {
        if (isEmpty(collection)) {
            return;
        }

        for (T t : collection) {
            if (null == t && ignoreNull) {
                continue;
            }

            handler1.handle(t);
        }
    }

    /**
     * 分批处理（数据库批次处理，缓存等）
     */
    public static <T> void batchHandle(List<T> list, int offset, int step, StepHandler<T> handler) {
        if (isEmpty(list)) {
            return;
        }

        if (offset >= list.size()) {
            return;
        }

        while (offset < list.size()) {
            int end = offset + step <= list.size() ? offset + step : list.size();
            handler.handle(list.subList(offset, end));
            offset += end;
        }

        return;
    }

    public static <T> Set<T> toSet(Collection<T> collection) {
        if (isEmpty(collection)) {
            return Collections.emptySet();
        }

        Set<T> set = Sets.newHashSetWithExpectedSize(collection.size());
        for (T t : collection) {
            set.add(t);
        }

        return set;
    }

    public static <T> List<T> toList(Set<T> set) {
        if (isEmpty(set)) {
            return Collections.emptyList();
        }

        List<T> list = Lists.newArrayListWithExpectedSize(set.size());
        for (T t : set) {
            list.add(t);
        }

        return list;
    }

    public static <T, V, F> Map<V, F> toMap(Collection<T> collection, MapConverter<T, V, F> converter) {
        if (isEmpty(collection)) {
            return Collections.emptyMap();
        }

        Map<V, F> result = Maps.newHashMapWithExpectedSize(collection.size());
        for (T t : collection) {
            if (null == t) {
                continue;
            }

            result.put(converter.key(t), converter.value(t));
        }

        return result;
    }

    public static <T, V, F> Multimap<V, F> toMultiMap(Collection<T> collection, MapConverter<T, V, F> converter) {
        if (isEmpty(collection)) {
            return LinkedListMultimap.create(0);
        }

        Multimap<V, F> result = LinkedListMultimap.create();
        for (T t : collection) {
            if (null == t) {
                continue;
            }

            result.put(converter.key(t), converter.value(t));
        }

        return result;
    }

    public static <T> List<T> emptyList() {
        return Emptys.EMPTY_LIST;
    }

    public static <K, V> Map<K, V> emptyMap() {
        return Emptys.EMPTY_MAP;
    }

    public static <T> Set<T> emptySet() {
        return Emptys.EMPTY_SET;
    }

    public interface Selector<T> {
        boolean select(T t);
    }

    public interface MapConverter<T, V, F> {

        V key(T t);

        F value(T t);
    }

    public interface Handler1<T> {
        void handle(T t);
    }

    public interface StepHandler<T> {
        void handle(List<T> list);
    }

    public interface Converter<T, F> {
        F convert(T t);
    }
}
