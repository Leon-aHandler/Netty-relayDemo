package com.netty.handler;

import com.netty.globalChannel.GlobalChannelGroup;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class SocketClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端通道-开启：" + ctx.channel().localAddress() + "channelActive");

        String sendInfo = "Hello 这里是客户端  你好啊！";
        System.out.println("客户端准备发送的数据包：" + sendInfo);

        ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush

    }
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("读取客户端通道信息..");
        ByteBuf buf = msg.readBytes(msg.readableBytes());
        //从全局Group中按端口号分别处理
        GlobalChannelGroup.channelGroup.forEach(o->{
            if (o.localAddress().toString().endsWith("5001")){
                o.writeAndFlush(buf.toString(Charset.forName("utf-8")));
            }else {
                TextWebSocketFrame text = new TextWebSocketFrame(o.remoteAddress() + "发送消息：" + buf.toString(Charset.forName("utf-8")) + "\n");
                o.writeAndFlush(text);
            }
        });
        System.out.println(
                "客户端接收到的服务端信息:" + buf.toString(Charset.forName("utf-8")));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常退出:" + cause.getMessage());
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //保存至全局Group
        GlobalChannelGroup.channelGroup.add(ctx.channel());
    }
}
