//package com.jsj.api.util;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Input;
//import com.esotericsoftware.kryo.io.Output;
//import com.esotericsoftware.kryo.serializers.BeanSerializer;
//
///**
// * 基于Kryo的序列化工具
// */
//public class KryoSerializer implements Serializer {
//    /**
//     * 由于kryo不是线程安全的，所以每个线程都使用独立的kryo
//     */
//    final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
//        @Override
//        protected Kryo initialValue() {
//            Kryo kryo = new Kryo();
//            kryo.register(ct, new BeanSerializer<>(kryo, ct));
//            return kryo;
//        }
//    };
//    final ThreadLocal<Output> outputLocal = new ThreadLocal<>();
//    final ThreadLocal<Input> inputLocal = new ThreadLocal<>();
//    private Class<?> ct = null;
//
//    public KryoSerializer(Class<?> ct) {
//        this.ct = ct;
//    }
//
//    public Class<?> getCt() {
//        return ct;
//    }
//
//    public void setCt(Class<?> ct) {
//        this.ct = ct;
//    }
//
//    @Override
//    public void serialize(Object obj, byte[] bytes) {
//        Kryo kryo = getKryo();
//        Output output = getOutput(bytes);
//        kryo.writeObjectOrNull(output, obj, obj.getClass());
//        output.flush();
//    }
//
//    @Override
//    public void serialize(Object obj, byte[] bytes, int offset, int count) {
//        Kryo kryo = getKryo();
//        Output output = getOutput(bytes, offset, count);
//        kryo.writeObjectOrNull(output, obj, obj.getClass());
//        output.flush();
//    }
//
//    /**
//     * 获取kryo
//     *
//     * @return
//     */
//    private Kryo getKryo() {
//        return kryoLocal.get();
//    }
//
//    /**
//     * 获取Output并设置初始数组
//     *
//     * @param bytes
//     * @return
//     */
//    private Output getOutput(byte[] bytes) {
//        Output output = null;
//        if ((output = outputLocal.get()) == null) {
//            output = new Output();
//            outputLocal.set(output);
//        }
//        if (bytes != null) {
//            output.setBuffer(bytes);
//        }
//        return output;
//    }
//
//    /**
//     * 获取Output
//     *
//     * @param bytes
//     * @return
//     */
//    private Output getOutput(byte[] bytes, int offset, int count) {
//        Output output = null;
//        if ((output = outputLocal.get()) == null) {
//            output = new Output();
//            outputLocal.set(output);
//        }
//        if (bytes != null) {
//            output.writeBytes(bytes, offset, count);
//        }
//        return output;
//    }
//
//    /**
//     * 获取Input
//     *
//     * @param bytes
//     * @param offset
//     * @param count
//     * @return
//     */
//    private Input getInput(byte[] bytes, int offset, int count) {
//        Input input = null;
//        if ((input = inputLocal.get()) == null) {
//            input = new Input();
//            inputLocal.set(input);
//        }
//        if (bytes != null) {
//            input.setBuffer(bytes, offset, count);
//        }
//        return input;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T deserialize(byte[] bytes, int offset, int count) {
//        Kryo kryo = getKryo();
//        Input input = getInput(bytes, offset, count);
//        return (T) kryo.readObjectOrNull(input, ct);
//    }
//
//    @Override
//    public <T> T deserialize(byte[] bytes) {
//        return deserialize(bytes, 0, bytes.length);
//    }
//}
