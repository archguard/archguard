package net.aimeizi.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2015/8/5.
 */
public class Application {

    public static void main(String[] args) throws Exception{
    	ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("dubbo/zookeeper/applicationContext-dubbo-provider.xml");
    	applicationContext.start();
        System.in.read();
    }
}
