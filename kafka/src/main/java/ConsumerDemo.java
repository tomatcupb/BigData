import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class ConsumerDemo {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "s1.hadoop:9092");

        // 消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my_consumer");
        // 自动提交offset的时间间隔
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 关闭自动提交offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 初始化一个消费者或者目前的offset不存在于消费者的消费记录中，选择从earliest开始读，还是latest开始读
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer consumer = new KafkaConsumer(properties);

        // 订阅主题
        consumer.subscribe(Arrays.asList("bigdata2"));

        // 持续轮询
        while (true){
            // timeout: 若没有获得消息等等待时间,若为0，即无论获得数据与否，立刻返回
            ConsumerRecords<String,String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.partition()+":"+record.key()+":"+record.value()+":"+record.offset());
            }
            // 同步提交，当前线程会阻塞直到 offset 提交成功
//            consumer.commitSync();

            // 异步提交
//            consumer.commitAsync();
            // 异步提交并返回错误信息
            consumer.commitAsync(new OffsetCommitCallback() {
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if(exception!=null){
                        System.out.println(exception.getMessage());
                        System.out.println("Commit failed for"+offsets);
                    }
                }
            });
        }
    }
}
