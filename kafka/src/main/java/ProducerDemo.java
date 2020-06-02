import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "nn2.hadoop:9092");
//        properties.put(ProducerConfig.ACKS_CONFIG, "all");
//        properties.put(ProducerConfig.RETRIES_CONFIG, 1);
        // 指定发送消息时的批次大小，只有数据积累到 batch.size 之后， sender 才会发送数据
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 指定发送消息的的等待时间，如果数据迟迟未达到 batch.size， sender 等待 linger.time 之后就会发送数据
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // RecordAccumulator 缓冲区大小
//        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // key和value的序列化方式
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i<10; i++){
            // 每条数据都要封装成一个 ProducerRecord 对象
            // 不带回调函数的发送消息
//            producer.send(new ProducerRecord<String, String>("bigdata", "data"+i));

            // 带回调函数的发送消息
            producer.send(new ProducerRecord<String, String>("bigdata", "data"), new Callback(){
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(exception==null){
                        System.out.println(metadata.topic()+"-"+metadata.partition() + "-" + metadata.offset());
                    }
                }
            });
        }

        // 关闭资源
        producer.close();
    }
}
