package com.luban;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileNio {

    @Test   //写文件
    public void writeFile() throws Exception{
        FileOutputStream fileOutputStream=new FileOutputStream("test.txt");
        //得到管道
        FileChannel channel = fileOutputStream.getChannel();
        //创建缓冲区
        ByteBuffer buffer= ByteBuffer.allocate(1024);
        String test="hello,nio";
        buffer.put(test.getBytes());
        buffer.flip();
        channel.write(buffer);
        fileOutputStream.close();
    }


    @Test   //读文件
    public void readFile() throws Exception{
        File file=new File("test.txt");
        FileInputStream fileInputStream=new FileInputStream(file);
        //得到管道
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer buffer= ByteBuffer.allocate((int) file.length());
        channel.read(buffer);
        System.out.println(new String(buffer.array()));
        fileInputStream.close();
    }

}
