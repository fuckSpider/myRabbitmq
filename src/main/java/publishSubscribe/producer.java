package publishSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发部订阅模式生产者1
 */
public class producer {
    private static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取mq连接
        Connection connection = MqConnectionUtils.getConnection();

        //获取channel
        Channel channel = connection.createChannel();

        //声明交换机
        String type = "fanout";//交换机类型 - 分发
        channel.exchangeDeclare(EXCHANGE_NAME,type);

        //发送消息
        String msg = "hello publish/subscribe";

        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());

        System.out.println("send msg :" +msg);

        channel.close();
        connection.close();

    }
}
