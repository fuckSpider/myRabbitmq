package confim;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * confirm模式
 * 多条记录confirm多条
 */
public class Producer2 {
    private static final String  QUEUE_NAME = "test_queue_confirm1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = MqConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //生产者调用confirmSelect 将channel设置为confirm模式 注意
        channel.confirmSelect();

        String msgString = "hello confirm message";
        for(int i = 0;i < 10;i++){
            channel.basicPublish("",QUEUE_NAME,null,msgString.getBytes());
        }
        if(!channel.waitForConfirms()){
            System.out.println("send msg failed!");
        }else{
            System.out.println("msg send ok!");
        }


        channel.close();
        connection.close();
    }
}
