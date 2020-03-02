package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CloudS3n
 * @date 2020-03-02 22:02
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(EchoServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("服务端：将收到的消息=> " + ((ByteBuf) msg).toString(CharsetUtil.UTF_8) + " 返回给客户端");
        // 写入时自动 release 了 不要再手动 release, 否则抛出 IllegalReferenceCountException 异常
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("服务端：异常，详情：" + ExceptionUtils.getStackTrace(cause));
        ctx.close();
    }
}
