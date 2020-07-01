package com.luban.netty.sixthdome;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<DataInfo.MyObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.MyObject myObject) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int i = new Random().nextInt(3);
        DataInfo.MyObject myObject = null;
        switch (i){
            case 0:
                myObject= DataInfo.MyObject.newBuilder().setDataType(DataInfo.MyObject.DataType.StudentType)
                        .setStudent(DataInfo.Student.newBuilder().setAge(26).setAddress("长沙").setName("太白").build())
                        .build();
                break;
            case 1:
                myObject= DataInfo.MyObject.newBuilder().setDataType(DataInfo.MyObject.DataType.TeacherType)
                        .setTeacher(DataInfo.Teacher.newBuilder().setAge(26).setDuty("讲师").setName("太白").build())
                        .build();
                break;
            case 2:
                myObject= DataInfo.MyObject.newBuilder().setDataType(DataInfo.MyObject.DataType.PatriarchType)
                        .setPatriarch(DataInfo.Patriarch.newBuilder().setId(1).setName("太白").build())
                        .build();
                break;
        }
        ctx.writeAndFlush(myObject);
//        DataInfo.Student student = DataInfo.Student.newBuilder().setName("太白").setAge(25).setAddress("长沙").build();
//        byte[] bytes = student.toByteArray();
//        ByteBuf buf= Unpooled.copiedBuffer(bytes);
//        ctx.writeAndFlush(student);
    }
}
