package sample.netty.server.myTestServer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class PrintHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        System.out.println("$$$$$$$$$$$$$ \n" + request.toString());

        final ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener(ChannelFutureListener.CLOSE);
        ReferenceCountUtil.release(msg);
    }

//    /**
//     * <strong>Please keep in mind that this method will be renamed to
//     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
//     * <p>
//     * Is called for each message of type {@link I}.
//     *
//     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
//     *            belongs to
//     * @param msg the message to handle
//     * @throws Exception is thrown if an error occurred
//     */
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        FullHttpRequest request = (FullHttpRequest) msg;
//        System.out.println("$$$$$$$$$$$$$ \n" + request.toString());
//
//        final ChannelFuture f = ctx.writeAndFlush(msg);
//        f.addListener(ChannelFutureListener.CLOSE);
//    }

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p>
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        System.out.println("$$$$$$$$$$$$$ \n" + msg.toString());

        final ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener(ChannelFutureListener.CLOSE);
    }
}
