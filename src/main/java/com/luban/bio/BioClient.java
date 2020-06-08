package com.luban.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BioClient {
    public static void main(String[] args) {

        try {
            Socket socket=new Socket("127.0.0.1",6789);
            new Thread(){
                @Override
                public void run() {
                    while (true){
                        try {
                            byte[] b=new byte[1024];
                            int read = socket.getInputStream().read(b);
                            if(read>0){
                                System.out.println(new String(b));
                                break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            while (true){
                Scanner scanner=new Scanner(System.in);
                while(scanner.hasNextLine()){
                    String s = scanner.nextLine();
                    socket.getOutputStream().write(s .getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
