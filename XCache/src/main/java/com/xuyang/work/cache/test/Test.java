package com.xuyang.work.cache.test;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 17:18
 * @Description:
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        TestMgr testMgr = new TestMgr();
        String s = testMgr.get();
        System.out.println(s);

        String s1 = testMgr.get();
        System.out.println(s1);
    }
}