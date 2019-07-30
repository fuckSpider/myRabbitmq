package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * topic模式下的生产者
 */
public class Producer {
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String msg = "商品消息事件";

        channel.basicPublish(EXCHANGE_NAME,"goods.add",null,msg.getBytes());

        System.out.println("send msg : "+msg);

        channel.close();

        connection.close();

    }
}
