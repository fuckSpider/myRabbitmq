package routing;


import com.rabbitmq.client.*;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式消费者2
 */
public class Consumer2 {
    private static  final  String QUEUE_NAME = "test_queue_direct_2"; //队列名称
    private static final String EXCHANGE_NAME = "test_exchange_direct"; //交换机名称
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        Connection connection = MqConnectionUtils.getConnection();

        //通道
        final Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"info");
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"warn");

        //每次推送一条
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        //消费消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String msg = new String (body);

                System.out.println("[2] consume msg :"+msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[2] done");
                    //回执给队列，消息处理完毕
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }

            }
        };

        //监听队列
        boolean autoAck = false; //自动应答关闭，改成手动应答
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }
}