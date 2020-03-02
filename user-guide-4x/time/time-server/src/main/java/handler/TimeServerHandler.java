package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>4个字节共32位的integer传输协议</p>
 * <p>建立连接时，主动发送32位的int，不需要请求。一旦发送完毕，将关闭连接</p>
 *
 * @author CloudS3n
 * @date 2020-03-02 22:21
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private static final Integer BYTE = 4;
    private static final Logger LOG = LoggerFactory.getLogger(TimeServerHandler.class);

    /**
     * 连接建立成功时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final ByteBuf buffer = ctx.alloc().buffer(BYTE);
        int now = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
        buffer.writeInt(now);
        LOG.info("服务端：发送时间到客户端 => " + now);
        final ChannelFuture f = ctx.writeAndFlush(buffer);
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("服务端：异常，详情：" + ExceptionUtils.getStackTrace(cause));
        ctx.close();
    }
}
