package com.netty.handler;

import com.netty.globalChannel.GlobalChannelGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端channel通道激活-"+ctx.channel());
        System.out.println(ctx.channel().remoteAddress()+"：连接");
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //保存至全局Group
        GlobalChannelGroup.channelGroup.add(ctx.channel());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端channel通道注销-"+ctx.channel());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)  throws Exception{
        System.out.println("接收到客户端-{"+ctx.channel()+"}的消息为-{"+msg.text()+"}");
        int size = GlobalChannelGroup.channelGroup.size();
        System.out.println("当前连接数:"+(size==0?0:size-1));
        System.out.println("收到消息："+msg.text());
        //从全局Group中按端口号分别处理
        GlobalChannelGroup.channelGroup.forEach(o->{
            if (o.localAddress().toString().endsWith("12345")){
                TextWebSocketFrame text = new TextWebSocketFrame(o.remoteAddress() + "发送消息：" + msg.text() + "\n");
                o.writeAndFlush(text);
            }else {
                o.writeAndFlush(msg.text());
            }
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
