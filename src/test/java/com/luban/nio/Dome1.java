package com.luban.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class Dome1 {
    /**
     * buffer 有三个属性
     * capacity： 最大容量，它永远不可能为负数，并且是不会变化的
     * limit：  限制，它永远不可能为负数，并且不会大于capacity
     * position：下一个读或写的位置，它永远不可能为负数，并且不会大于limit
     * buffer.capacity() 容量
     * @param args
     */
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(8); // 堆内缓存
        for (int i=0;i<buffer.capacity();i++){
            int nextInt = new SecureRandom().nextInt(20);
            buffer.put(nextInt);
        }
        buffer.flip(); //  读之前先翻转
        System.out.println("==============");
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
