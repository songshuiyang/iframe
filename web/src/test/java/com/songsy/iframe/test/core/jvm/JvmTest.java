package com.songsy.iframe.test.core.jvm;

/**
 * VM参数：
 * -verbose:gc -Xms200M  -Xmx200M -Xmn100M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
 *  -Xms最小堆空间 -Xmx最大堆空间 -Xmn年轻代空间 SurvivorRatio:eden与Survivor比例
 *
 * @author songsy
 * @date 2019/3/11 15:33
 */
public class JvmTest {

    private static final int _1KB = 1024;
    private static final int _1MB = 1024 * _1KB;
    private static final int _10MB = 1024 * _1MB;
    private static final int _HALF_1MB = 512 * _1KB;

    public static void testAllocation() {
        byte [] allocation1,allocation2, allocation3,allocation4,allocation5;
        allocation1 = new byte[2 * _10MB];
        allocation2 = new byte[2 * _10MB];
        allocation3 = new byte[2 * _10MB];
        allocation4 = new byte[1 * _10MB];
        allocation5 = new byte[3 * _10MB];
    }
    public static void main(String[] args) {
        testAllocation();
    }
}
