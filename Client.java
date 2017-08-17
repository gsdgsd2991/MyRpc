import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;



/**
 * Created by sida.gsd on 2017/8/15.
 */
public class Client extends SimpleChannelInboundHandler<RpcResponseModel> {

    private String ip;
    private int port;

    private RpcResponseModel responseModel;

    public Client(String ip,int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponseModel rpcResponseModel) throws Exception {
        this.responseModel = rpcResponseModel;
        System.out.println(responseModel);
    }

    public RpcResponseModel send(RpcRequestModel request) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new RpcEncoder(RpcRequestModel.class));
                pipeline.addLast(new RpcDecoder());
                pipeline.addLast(Client.this);
            }
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        ChannelFuture future = bootstrap.connect(ip,port).sync();
        Channel channel = future.channel();
        channel.writeAndFlush(request).sync();
        channel.closeFuture().sync();
        group.shutdownGracefully();
        System.out.println(responseModel);
        return responseModel;
    }
}
