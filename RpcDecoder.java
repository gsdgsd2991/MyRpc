
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.util.SerializationUtils;

import java.util.List;

/**
 * Created by sida.gsd on 2017/8/15.
 */
public class RpcDecoder extends ByteToMessageDecoder {
   // private Class<?> genericClass;

  //  public RpcDecoder(Class<?> genericClass){
  //      this.genericClass = genericClass;
  //  }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        byte[] data = new byte[byteBuf.readInt()];
        byteBuf.readBytes(data);
        Object o = SerializationUtils.deserialize(data);
        list.add(o);
    }
}
