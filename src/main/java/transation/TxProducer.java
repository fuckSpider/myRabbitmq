package transation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 事务模式下的生产者，
 * 解决生产者段数据丢失的问题
 *
 * 这是通过amqp协议的事务机制
 * 缺点 降低mq吞吐量
 * 由于大量的通信，效率低
 */
public class TxProducer {

    private static final String QUEUE_NAME = "test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "hello tx msg";

        try{
            channel.txSelect(); //开始事务
            channel.basicPublish("", QUEUE_NAME,null,msg.getBytes());
            System.out.println("send msg :"+msg);
            channel.txCommit();
        }catch (Exception e){
            channel.txRollback();
            System.out.println("send msg txRollback");
        }

        channel.close();
        connection.close();




    }
}
