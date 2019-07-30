package workfair;

import com.rabbitmq.client.*;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer2 {
    private static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = MqConnectionUtils.getConnection();

        //channel
        final Channel channel = connection.createChannel();

        boolean durable = false;//消息是否持久化
        //声明队列
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);

        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        //消费消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String msg = new String (body);

                System.out.println("[0] consume msg :"+msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[0] done");
                    //手动回执给队列，消息处理完毕
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }

            }
        };

        //监听队列
        boolean autoAck = false; //自动应答，改成手动应答
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
