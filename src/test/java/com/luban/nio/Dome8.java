package com.luban.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Dome8 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream=new FileOutputStream("dome8write.txt");
        FileInputStream fileInputStream=new FileInputStream("dome8read.txt");

        FileChannel channelRead = fileInputStream.getChannel();
        FileChannel channelWrite = fileOutputStream.getChannel();
        // DirectByteBuffer 堆外内存 性能要好点
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(100);
       while (true){
           byteBuffer.clear();
           int readNumber = channelRead.read(byteBuffer);
           System.out.println(readNumber);
           if(readNumber==-1){
               break;
           }
           byteBuffer.flip();
           channelWrite.write(byteBuffer);
       }
       fileOutputStream.close();
       fileInputStream.close();
    }
}
