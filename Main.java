import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sida.gsd on 2017/8/15.
 */
@Configuration
@ComponentScan
public class Main {

    public static void main(String args[]) throws InterruptedException {

        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(SayImpl.class);

        ExecutorService threadPool = Executors.newCachedThreadPool();
       threadPool.execute(()->{

           RpcProxy proxy = new RpcProxy("127.0.0.1:9000");//context.getBean(RpcProxy.class);
           Say s = proxy.create(Say.class);
           String result = s.saySomething("abc");
           System.out.println(result);
       });
        context.register(InitNetty.class);
        context.refresh();
        //threadPool.execute(()->{InitNetty initNetty = (InitNetty)context.getBean("Server");});

    }
}
