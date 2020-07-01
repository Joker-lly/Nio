package com.luban.netty.sixthdome;


import com.google.protobuf.Parser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestServerHandler extends SimpleChannelInboundHandler<DataInfo.MyObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.MyObject myObject) throws Exception {
        DataInfo.MyObject.DataType dataType = myObject.getDataType();
        if(dataType== DataInfo.MyObject.DataType.StudentType){
            DataInfo.Student student = myObject.getStudent();
            System.out.println(student.getName());
            System.out.println(student.getAge());
            System.out.println(student.getAddress());
        }else if(dataType== DataInfo.MyObject.DataType.TeacherType){
            DataInfo.Teacher teacher = myObject.getTeacher();
            System.out.println(teacher.getAge());
            System.out.println(teacher.getName());
            System.out.println(teacher.getDuty());
        }else if(dataType== DataInfo.MyObject.DataType.PatriarchType){
            DataInfo.Patriarch patriarch = myObject.getPatriarch();
            System.out.println(patriarch.getId());
            System.out.println(patriarch.getName());
        }
    }
}
