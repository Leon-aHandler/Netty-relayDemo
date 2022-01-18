# Netty-relayDemo
 websocket-socket server
 一个websocket客户端至socket服务器端的netty中转服务器
 
 业务需求场景：通过网页发送指令至硬件设备，并在连接过程中能够接收来自硬件设备的实时参数信息

 中转服务器逻辑：服务器内实现了一个websocket服务端和一个socket客户端，设置全局保存的ChannelGroup进行消息的转发
