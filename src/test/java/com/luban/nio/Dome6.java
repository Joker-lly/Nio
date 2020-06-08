package com.luban.nio;

import java.nio.ByteBuffer;

public class Dome6 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer=ByteBuffer.allocate(10);
        for(int i=0;i<byteBuffer.capacity();++i){
            byteBuffer.put((byte)i);
        }
        // 起到标记的作用
//        byteBuffer.mark();
        byteBuffer.position(2);
        byteBuffer.limit(8);
        // 按照position 和limit 复制一个缓冲区， 读写位置不一样， 但数据共享，两个缓冲区
        // resetBuffer 是复制出来的的buffer  和ByteBuffer 共享数据
        ByteBuffer resetBuffer = byteBuffer.slice();
        for(int i=0;i<resetBuffer.capacity();i++){
            byte anInt = resetBuffer.get();
            resetBuffer.put(i, (byte) (anInt*2));
        }

        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());
        // 和 mark 一起使用
        byteBuffer.reset();
        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }

    }
}
