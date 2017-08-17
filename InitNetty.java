import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sida.gsd on 2017/8/15.
 */
@Component
public class InitNetty implements ApplicationContextAware, InitializingBean{

    private Map<String,Object> handlerMap = new HashMap<String, Object>();

    private String serviceAddress="127.0.0.1:9000";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> beanMap = applicationContext.getBeansWithAnnotation(Rpc.class);
        for(Object bean: beanMap.values()){
            Rpc rpc = bean.getClass().getAnnotation(Rpc.class);
            String serviceName = rpc.value().getName();
            handlerMap.put(serviceName,bean);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new RpcEncoder(RpcRequestModel.class));
                pipeline.addLast(new RpcDecoder());
                pipeline.addLast(new RequestServer(handlerMap));
            }
        });
        bootstrap.option(ChannelOption.SO_BACKLOG,1024);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);

        String[] addressArray = this.serviceAddress.split(":");
        String ip = addressArray[0];
        int port = Integer.parseInt(addressArray[1]);
        ChannelFuture future = bootstrap.bind(ip,port).sync();
        future.channel().closeFuture().sync();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }
}
