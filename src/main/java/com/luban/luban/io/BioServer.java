package com.luban.luban.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws IOException {
        int port=8080;
        if(args!=null&&args.length>0){
            try {
                port=Integer.valueOf(args[0]);
            }catch (Exception e){

            }
        }
        // 服务器端的socket

        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(port);
            TimeServerHandlerExecutorPool pool = new TimeServerHandlerExecutorPool(50, 1000);
            while (true){
                Socket socket = serverSocket.accept(); // 这里是阻塞的，接受客户端连接
                socket.getInputStream().read();// 这里也是阻塞的，读取数据阻塞

//                new Thread(new TimeServerHandler(socket)).start();
                pool.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                serverSocket.close();
            }
        }

    }
}
