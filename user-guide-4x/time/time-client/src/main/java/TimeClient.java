import handler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CloudS3n
 * @date 2020-03-02 22:46
 */
public class TimeClient {

    private static final Logger LOG = LoggerFactory.getLogger(TimeClient.class);
    private String host;
    private int port;

    public TimeClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 8080;
        if (args.length > 1) {
            host = args[0];
        }
        if (args.length > 2) {
            port = Integer.parseInt(args[1]);
        }
        new TimeClient(host, port).run();
    }

    public void run() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimeClientHandler());
                    }
                });
            // 启动客户端
            ChannelFuture f = b.connect(host, port).sync();
            // 等待，直到连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
