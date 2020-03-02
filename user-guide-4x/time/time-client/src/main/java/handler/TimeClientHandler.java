package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 从服务端接收一个32位的integer 并显示为时间
 *
 * @author CloudS3n
 * @date 2020-03-02 22:55
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(TimeClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            long currentTimeMillis = ((((ByteBuf) msg).readUnsignedInt() - 2208988800L) * 1000L);
            LOG.info("客户端：收到时间<= " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTimeMillis)));
            ctx.close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("服务端：异常，详情：" + ExceptionUtils.getStackTrace(cause));
        ctx.close();
    }
}
