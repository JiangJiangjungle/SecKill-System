package com.jsj.api.util;

import com.jsj.api.entity.SecKillRequest;

import java.util.Arrays;

public class SerializerTest {
    public static void main(String[] args) {
//        Serializer ser = new KryoSerializer(SecKillRequest.class);
//        for (long i = 0L; i < 10L; i++) {
//
//            SecKillRequest request = new SecKillRequest(System.currentTimeMillis(), Long.MAX_VALUE, Long.MAX_VALUE,Integer.MAX_VALUE);
//            long start = System.nanoTime();
//            byte[] buffer = new byte[100];
//            ser.serialize(request, buffer);
//            System.err.println("序列化耗时：" + (System.nanoTime() - start));
//            System.out.println(request.toString());
//            System.out.println(Arrays.toString(buffer));
//
//            SecKillRequest newRequest = null;
//            start = System.nanoTime();
//            newRequest = ser.deserialize(buffer);
//            System.err.println("反序列化耗时：" + (System.nanoTime() - start));
//            System.out.println(newRequest.toString());
//        }
    }
}
