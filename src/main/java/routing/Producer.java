package routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式下的生产者
 */
public class Producer {
    private static final String EXCHANGE_NAME = "test_exchange_direct";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        String exchageType= "ect";//交换机类型

        channel.exchangeDeclare(EXCHANGE_NAME,exchageType);

        String msg = "hello routing";

        String routingKey = "info"; //路由键
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,msg.getBytes());
        System.out.println("send msg: "+ msg);

        channel.close();
        connection.close();
    }
}
