package workfair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列生产者
 */
public class producer {

    private static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = MqConnectionUtils.getConnection();

        //获取channel
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        /**
         * 每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
         *
         * 限制发送给同一个消费者不得超过一条数据
         */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        //发送消息
        for(int i = 0;i<10;i++){
            String msg = "work "+ i;
            channel.basicPublish("", QUEUE_NAME,null,msg.getBytes());
            System.out.println("workQueue send msg :"+ msg);
            Thread.sleep(i+20);
        }

        channel.close();

        connection.close();
    }
}
