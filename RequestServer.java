import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by sida.gsd on 2017/8/15.
 */
public class RequestServer extends SimpleChannelInboundHandler<RpcRequestModel> {
    private Map<String,Object> handlerMap;

    public RequestServer(Map<String,Object> handlerMap){
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestModel rpcRequestModel) throws Exception {
        RpcResponseModel responseModel = new RpcResponseModel();
        responseModel.setRequestId(rpcRequestModel.getRequestId());
        responseModel.setReturnValue(handleRequest(rpcRequestModel));
        channelHandlerContext.writeAndFlush(responseModel).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handleRequest(RpcRequestModel request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object bean = handlerMap.get(request.getInterfaceName());
        Class<?> serviceClass = bean.getClass();

        Method method = serviceClass.getMethod(request.getMethodName(),request.getParamTypes());
        method.setAccessible(true);
        Object ans = method.invoke(bean,request.getParams());
        System.out.println(ans);
        return ans;
    }
}
