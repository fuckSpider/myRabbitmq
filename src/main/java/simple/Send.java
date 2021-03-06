package simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列生产者
 */
public class Send {
    private static final String QUEUE_NAME = "test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取一个链接
        Connection connection = MqConnectionUtils.getConnection();

        //获取一个通道
        Channel channel = connection.createChannel();

        //创建队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "hello world";

        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        System.out.println("send msg-->"+msg);

        channel.close();
        connection.close();
    }
}
