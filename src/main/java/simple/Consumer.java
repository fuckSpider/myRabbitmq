package simple;


import com.rabbitmq.client.*;
import util.MqConnectionUtils;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String QUEUE_NAME = "test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = MqConnectionUtils.getConnection();

        //获取一个通道
        Channel channel = connection.createChannel();

        //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //消费消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body);
                System.out.println("new ApiConsumer-->" + msgString);
            }
        };

        //监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);

    }

    public static void oldApi() throws IOException, TimeoutException, InterruptedException {
        Connection connection = MqConnectionUtils.getConnection();

        //获取一个通道
        Channel channel = connection.createChannel();

        //定义队列的消费者
        QueueingConsumer consumer  = new QueueingConsumer(channel);

        //监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String( delivery.getBody());
            System.out.println("[consumer] get msg ---> "+msg);
        }
    }
}
