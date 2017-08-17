/**
 * Created by sida.gsd on 2017/8/15.
 */
@Rpc(value = Say.class)
public class SayImpl implements Say{
    @Override
    public String saySomething(String name) {
        return "Hello"+name;
    }
}
