package com.xuyang.work.cache.utils;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Joeson Chan
 */
@Slf4j
public final class CodecUtil {
    private static final String AES = "AES";
    private static final String DES = "DES";
    private static final String $3DES = "3DES";

    /**
     * md5 16进制
     *
     * @param bytes
     * @return
     */
    public static String md5HexString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return StringUtil.EMPTY;
        }

        return Hex.encodeHexString(md5(bytes));
    }

    public static byte[] md5(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return DigestUtils.md5(bytes);
    }

    public static String md5String(byte[] bytes) {
        return new String(md5(bytes));
    }

    public static String decodeBase642Str(String data) throws Exception {
        return new String(decodeBase64(data));
    }

    public static byte[] decodeBase64(String data) throws Exception {
        if (StringUtil.isEmpty(data)) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return Base64.decodeBase64(data);
    }


    public static byte[] decodeBase64(byte[] bytes) throws Exception {
        if (null == bytes || bytes.length == 0) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return Base64.decodeBase64(bytes);
    }

    public static String encodeBase642Str(String data) {
        return new String(encodeBase64(data));
    }

    public static byte[] encodeBase64(String data) {
        if (StringUtil.isEmpty(data)) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return Base64.encodeBase64(data.getBytes(Charsets.UTF_8));
    }

    public static byte[] encodeBase64(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return Base64.encodeBase64(bytes);
    }

    public static byte[] sha1(final byte[] data) {
        if (null == data || data.length == 0) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return DigestUtils.sha1(data);
    }

    public static String sha1String(final byte[] data) {
        if (null == data || data.length == 0) {
            return StringUtil.EMPTY;
        }

        return new String(sha1(data));
    }

    public static String sha1HexString(final byte[] data) {
        if (null == data || data.length == 0) {
            return StringUtil.EMPTY;
        }

        return Hex.encodeHexString(sha1(data));
    }


    public static byte[] sha256(final byte[] data) {
        if (null == data || data.length == 0) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return DigestUtils.sha256(data);
    }

    public static String sha256String(final byte[] data) {
        if (null == data || data.length == 0) {
            return StringUtil.EMPTY;
        }

        return new String(sha256(data));
    }

    public static String sha256HexString(final byte[] data) {
        if (null == data || data.length == 0) {
            return StringUtil.EMPTY;
        }

        return Hex.encodeHexString(sha256(data));
    }

    public static byte[] sha512(final byte[] data) {
        if (null == data || data.length == 0) {
            return Emptys.EMPTY_BYTE_ARRAY;
        }

        return DigestUtils.sha512(data);
    }

    public static String sha512String(final byte[] data) {
        if (null == data || data.length == 0) {
            return StringUtil.EMPTY;
        }

        return new String(sha512(data));
    }

    public static String sha512HexString(final byte[] data) {
        if (null == data || data.length == 0) {
            return StringUtil.EMPTY;
        }

        return Hex.encodeHexString(sha512(data));
    }

    /**
     * @param encodeType AES/DES
     * @param key        秘钥
     * @param data       加密数据
     */
    private static byte[] encode(String encodeType, byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (null == key || key.length == 0) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (null == data || data.length == 0) {
            throw new IllegalArgumentException("data can not be null");
        }

        SecretKey secretKey = new SecretKeySpec(key, encodeType);
        Cipher cipher = Cipher.getInstance(encodeType);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * @param encodeType AES/DES
     * @param key        秘钥
     * @param data       解密数据
     */
    private static byte[] decode(String encodeType, byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (null == key || key.length == 0) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (null == data || data.length == 0) {
            throw new IllegalArgumentException("data can not be null");
        }
        SecretKey secretKey = new SecretKeySpec(key, encodeType);
        Cipher cipher = Cipher.getInstance(encodeType);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    public static byte[] encodeAES(byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encode(AES, key, data);
    }


    /**
     * 优先考虑 AES，ADS能够有效抵御已知的针对DES算法的所有攻击
     *
     * @param key  秘钥
     * @param data 加密数据
     */
    public static byte[] encodeDES(byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encode(DES, key, data);
    }

    public static byte[] encode3DES(byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encode($3DES, key, data);
    }

    /**
     * @param key  秘钥
     * @param data 解密数据
     */
    public static byte[] decodeAES(byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return decode(AES, key, data);
    }

    /**
     * @param key  秘钥
     * @param data 解密数据
     */
    public static byte[] decodeDES(byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return decode(DES, key, data);
    }

    /**
     * @param key  秘钥
     * @param data 解密数据
     */
    public static byte[] decode3DES(byte[] key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return decode($3DES, key, data);
    }

}
