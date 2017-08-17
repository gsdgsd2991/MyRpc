import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by sida.gsd on 2017/8/15.
 */
public class RpcProxy {

    private String serviceAddress;

    public RpcProxy(String serviceAddress){
        this.serviceAddress = serviceAddress;
    }

    public <T> T create(Class<?> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequestModel request = new RpcRequestModel();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setInterfaceName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParams(args);
                        serviceAddress = "127.0.0.1:9000";
                        request.setParamTypes(method.getParameterTypes());
                        String[] array = serviceAddress.split(":");
                        String ip = array[0];
                        int port = Integer.parseInt(array[1]);
                        Client client = new Client(ip,port);

                        RpcResponseModel response = client.send(request);
                        return response.getReturnValue();
                    }
                });
    }
}
