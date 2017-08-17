import java.io.Serializable;

/**
 * Created by sida.gsd on 2017/8/15.
 */
public class RpcResponseModel implements Serializable {
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    private String requestId;
   private Object returnValue;
   private Class<?> returnType;
}
