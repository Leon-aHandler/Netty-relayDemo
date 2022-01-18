package com.netty;


import com.netty.server.WebSocketServer;
import com.netty.server.SocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyDemoApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(NettyDemoApplication.class);
        try {
            System.out.println("准备启动客户端");
            new SocketClient(5001).start();
            System.out.println("客户端启动完成");
            System.out.println("准备启动服务端");
            new WebSocketServer(12345).start();
            System.out.println("服务端启动完成");
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }
}
