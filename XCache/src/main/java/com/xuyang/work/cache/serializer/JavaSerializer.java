package com.xuyang.work.cache.serializer;

import com.google.common.base.Charsets;
import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.conf.XcacheConf;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 21:17
 * @Description:
 */
public class JavaSerializer implements CacheSerializer {

    @Override
    public <T> String serialize(XcacheInstance<T> value, XcacheConf xcacheConf) throws Exception {
        if (value == null) {
            throw new IllegalArgumentException("value can't be null");
        }

        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            oos.writeObject(value);
            oos.flush();
            oos.close();
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(oos);
        }
        return bos.toString(Charsets.ISO_8859_1.name());
    }

    @Override
    public <T> XcacheInstance<T> deserialize(String value, XcacheConf xcacheConf) throws Exception {
        if (xcacheConf == null) {
            throw new IllegalArgumentException("xcacheConf can't be null");
        }
        if (value == null) {
            return null;
        }

        XcacheInstance<T> res = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(value.getBytes(Charsets.ISO_8859_1));
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            res = (XcacheInstance<T>) ois.readObject();
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(ois);
        }

        return res;
    }
}