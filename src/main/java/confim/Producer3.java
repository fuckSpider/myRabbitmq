package confim;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import util.MqConnectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * comfirm模式异步处理
 */
public class Producer3 {
    private static final String QUEUE_NAME = "test_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        //生产者调用confirmSelect将channel设置为confirm模式  注意!
        channel.confirmSelect();

        //存放的是未确认的消息标识
        final SortedSet confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        //channel监听
        channel.addConfirmListener(new ConfirmListener() {
            //消息发送成功
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                //这里的multiple表示是否多条消息 multiple=true 表示批量的消息  multiple=false 表示单条的消息
                if(multiple){
                    System.out.println("---handleAkc--multiple");
                    //这之前的所有的数据都进行删除
                    confirmSet.headSet(deliveryTag+1).clear();
                }else{
                    System.out.println("---handleAkc---multiple--false");
                    confirmSet.remove(deliveryTag);
                }
            }

            //消息发送失败
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if(multiple){
                    System.out.println("---handleNack--multiple");
                    //这之前的所有的数据都进行删除
                    confirmSet.headSet(deliveryTag+1).clear();
                }else{
                    System.out.println("---handleNack---multiple--false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msgString = "sssss";

        while(true){
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("",QUEUE_NAME,null,msgString.getBytes());
            confirmSet.add(seqNo);
        }
    }
}
