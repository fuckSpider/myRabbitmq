package topic;

import com.rabbitmq.client.*;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final  String QUEUE_NAME = "test_queue_topic_1";
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"goods.add");
        channel.basicQos(1);//每次处理一条

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("get msg :" + msg);
                channel.basicAck(envelope.getDeliveryTag(), false); //消息回执
            }
        };

        channel.basicConsume(QUEUE_NAME,false,consumer);


    }
}
