package com.xuyang.work.cache.utils;


import com.google.common.collect.Maps;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joeson Chan
 */
public final class StringUtil {
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String LT = "<";
    public static final String GT = ">";
    public static final String EQAUL = "=";
    public static final String SPACING = " ";
    public static final String DOT = ".";
    public static final String UNDERLINE = "_";
    public static final String PLUS = "+";
    public static final String MULTIPLY = "×";
    public static final String LINKLINE = "-";
    public static final String AND = "&";
    public static final String QUESTION = "?";
    public static final String COLON = ":";
    public static final String DOLLAR = "$";
    public static final String SLASH = "/";
    public static final String OR = "|";
    public static final String POUND = "#";
    public static final String PERCENT = "%";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String EXCLAMATION = "!";
    public static final String BACKSLASH = "\\";

    public static final int INDEX_NOT_FOUND = -1;

    private static final Pattern UNDERLINE_PATTERN = Pattern.compile("_(\\w)");
    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    public static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static int length(String str) {
        if (isEmpty(str)) {
            return 0;
        }

        return str.length();
    }

    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    public static boolean isEmpty(String text) {
        return text == null || text.equals(EMPTY);
    }

    /**
     * 和 {@link #isEmpty(String)} 差别在于:
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isEmpty(" ")       = false
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean equals(String str1, String str2) {
        if (null == str1 && null == str2) {
            return true;
        }

        return null != str1 && str1.equals(str2);
    }

    /**
     * 只有一个空，返回true
     */
    public static boolean emptyInside(String... text) {
        if (text == null) {
            return true;
        }
        for (String t : text) {
            if (isEmpty(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只有一个空，返回true
     */
    public static boolean emptyInside(Collection<String> collection) {
        if (CollectionUtil.isEmpty(collection)) {
            return true;
        }

        for (String str : collection) {
            if (isEmpty(str)) {
                return true;
            }
        }

        return false;
    }

    public static int indexOf(final CharSequence seq, final CharSequence searchSeq) {
        return indexOf(seq, searchSeq, 0);
    }

    public static int indexOf(final CharSequence seq, final CharSequence searchSeq, int fromIndex) {
        if (fromIndex < 0) {
            throw new IllegalArgumentException("fromIndex can't < 0");
        }

        if (seq == null || searchSeq == null) {
            return INDEX_NOT_FOUND;
        }

        return seq.toString().indexOf(searchSeq.toString(), fromIndex);
    }

    public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq) {
        return lastIndexOf(seq, searchSeq, seq.length());
    }

    public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq, final int fromIndex) {
        if (fromIndex < 0) {
            throw new IllegalArgumentException("fromIndex can't < 0");
        }
        if (seq == null || searchSeq == null) {
            return INDEX_NOT_FOUND;
        }

        return seq.toString().lastIndexOf(searchSeq.toString(), fromIndex);
    }

    /**
     * 替换所有
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * 从头开始只替换一次
     */
    public static String replaceOnce(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * @param text         原来文本
     * @param searchString 搜索文本
     * @param replacement  替换文本
     * @param max          最大替换次数, -1 表示所有
     */
    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String remove(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return replace(str, remove, EMPTY, -1);
    }

    /**
     * 如果 str 开头不是remove，则不进行替换
     */
    public static String removeStart(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }

        if (startsWith(str, remove, true)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String removeStartIgnoreCase(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (startsWith(str, remove, true)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
        return startsWith(str, prefix, false);
    }

    public static boolean startsWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
    }

    public static String removeEnd(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String removeEndIgnoreCase(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (endsWith(str, remove, true)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        final int strOffset = str.length() - suffix.length();
        return regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart, final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The same check as in String.regionMatches():
            if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }

        return true;
    }

    public static String removePattern(final String source, final String regex) {
        return replacePattern(source, regex, EMPTY);
    }

    public static String replacePattern(final String source, final String regex, final String replacement) {
        if (StringUtil.isEmpty(regex) || null == source) {
            return source;
        }

        return Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement);
    }

    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return str;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }


    /**
     * 从指定字符串 {@code}separator 前开始(不包括separator) 截取
     * <p>
     * <pre>
     * StringUtil.substringBefore(null, *)      = null
     * StringUtil.substringBefore("", *)        = ""
     * StringUtil.substringBefore("abc", "a")   = ""
     * StringUtil.substringBefore("abcba", "b") = "a"
     * StringUtil.substringBefore("abc", "c")   = "ab"
     * StringUtil.substringBefore("abc", "d")   = "abc"
     * StringUtil.substringBefore("abc", "")    = ""
     * StringUtil.substringBefore("abc", null)  = "abc"
     * </pre>
     */
    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 从指定字符串{@code} separator后面开始截取，不包括{@code} separator
     * <p>
     * <pre>
     * StringUtil.substringAfter(null, *)      = null
     * StringUtil.substringAfter("", *)        = ""
     * StringUtil.substringAfter(*, null)      = ""
     * StringUtil.substringAfter("abc", "a")   = "bc"
     * StringUtil.substringAfter("abcba", "b") = "cba"
     * StringUtil.substringAfter("abc", "c")   = ""
     * StringUtil.substringAfter("abc", "d")   = ""
     * StringUtil.substringAfter("abc", "")    = "abc"
     * </pre>
     */
    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 从指定字符串 最后出现 之前开始截取
     * <p>
     * <pre>
     * StringUtil.substringBeforeLast(null, *)      = null
     * StringUtil.substringBeforeLast("", *)        = ""
     * StringUtil.substringBeforeLast("abcba", "b") = "abc"
     * StringUtil.substringBeforeLast("abc", "c")   = "ab"
     * StringUtil.substringBeforeLast("a", "a")     = ""
     * StringUtil.substringBeforeLast("a", "z")     = "a"
     * StringUtil.substringBeforeLast("a", null)    = "a"
     * StringUtil.substringBeforeLast("a", "")      = "a"
     * </pre>
     */
    public static String substringBeforeLast(final String str, final String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 从指定字符串 最后出现 之后开始截取
     * <p>
     * <pre>
     * StringUtil.substringAfterLast(null, *)      = null
     * StringUtil.substringAfterLast("", *)        = ""
     * StringUtil.substringAfterLast(*, "")        = ""
     * StringUtil.substringAfterLast(*, null)      = ""
     * StringUtil.substringAfterLast("abc", "a")   = "bc"
     * StringUtil.substringAfterLast("abcba", "b") = "a"
     * StringUtil.substringAfterLast("abc", "c")   = ""
     * StringUtil.substringAfterLast("a", "a")     = ""
     * StringUtil.substringAfterLast("a", "z")     = ""
     * </pre>
     */
    public static String substringAfterLast(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 从 open，close出现的中间(不包括 open，close)开始截取，如果其中一个没找到，则返回null
     * <p>
     * <pre>
     * StringUtil.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtil.substringBetween(null, *, *)          = null
     * StringUtil.substringBetween(*, null, *)          = null
     * StringUtil.substringBetween(*, *, null)          = null
     * StringUtil.substringBetween("", "", "")          = ""
     * StringUtil.substringBetween("", "", "]")         = null
     * StringUtil.substringBetween("", "[", "]")        = null
     * StringUtil.substringBetween("yabcz", "", "")     = ""
     * StringUtil.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtil.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     */
    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            final int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 查找所有 open,close 字符串之间的字符串
     * <p>
     * <pre>
     * StringUtil.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
     * StringUtil.substringsBetween(null, *, *)            = null
     * StringUtil.substringsBetween(*, null, *)            = null
     * StringUtil.substringsBetween(*, *, null)            = null
     * StringUtil.substringsBetween("", "[", "]")          = []
     * </pre>
     */
    public static String[] substringsBetween(final String str, final String open, final String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == 0) {
            return Emptys.EMPTY_STRING_ARRAY;
        }
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            final int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    public static byte[] getBytes(String text, Charset charset) {
        if (isEmpty(text)) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return getBytes(text, null);
    }

    /**
     * <pre>
     * 把数组组合成字符串
     * </pre>
     */
    public static <T> String join(T[] t, String separator) {
        return join(t, separator, StringUtil.EMPTY, StringUtil.EMPTY);
    }

    /**
     * <pre>
     * 把数组组合成字符串
     * </pre>
     */
    public static <T> String join(T[] t, String separator, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < t.length; i++) {
            if (i == 0) {
                sb.append(t[i]);
            } else {
                sb.append(separator).append(t[i]);
            }
        }
        sb.append(suffix);
        return sb.toString();
    }

    public static <T> String join(Collection<T> collection, String separator) {
        return join(collection, separator, StringUtil.EMPTY, StringUtil.EMPTY);
    }

    public static <T> String join(Collection<T> collection, String separator, Convert<T> convert) {
        return join(collection, separator, StringUtil.EMPTY, StringUtil.EMPTY, convert);
    }

    public static <T> String join(Collection<T> collection, String separator, String prefix, String suffix) {
        return join(collection, separator, prefix, suffix, null);
    }

    public static <T> String join(Collection<T> collection, String separator, String prefix, String suffix, Convert<T> convert) {
        StringBuilder sb = new StringBuilder(prefix);
        int i = 0;
        for (T t : collection) {
            if (convert != null) {
                t = convert.convert(t);
            }
            if (i == 0) {
                sb.append(t);
            } else {
                sb.append(separator).append(t);
            }
            i++;
        }
        sb.append(suffix);
        return sb.toString();
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }

        return new StringBuilder(str).reverse().toString();
    }

    public static interface Convert<T> {
        public T convert(T t);
    }

    public static List<String> split2List(String text, String split) {
        if (null == text) {
            return CollectionUtil.emptyList();
        }

        String[] arrays = text.split(split);
        return ArrayUtil.asList(arrays);
    }

    /**
     * eg : split2Map("a=1;b=2;c=3", "=", ";")
     */
    public static Map<String, String> split2Map(String text, String mapSplit, String split) {
        if (isEmpty(text)) {
            return CollectionUtil.emptyMap();
        }

        String[] arrays = text.split(split);
        Map<String, String> result = Maps.newHashMap();
        for (String str : arrays) {
            String[] tmp = str.split(mapSplit);
            if (tmp.length == 1) {
                result.put(tmp[0], EMPTY);
            } else if (tmp.length == 2) {
                result.put(tmp[0], tmp[1]);
            }
        }

        return result;
    }

    /**
     * 下划线转驼峰
     */
    public static String underLineToHump(String str) {
        if (isEmpty(str)) {
            return StringUtil.EMPTY;
        }

        str = str.toLowerCase();
        Matcher matcher = UNDERLINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     */
    public static String humpToUnderLine(String str) {
        if (isEmpty(str)) {
            return StringUtil.EMPTY;
        }

        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, UNDERLINE + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}

