package interceptor;

import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.Properties;

public class InterceptorProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "s1.hadoop:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        // 指定Interceptor链
        ArrayList<String> list = new ArrayList<String>();
        list.add("interceptor.TimeInterceptor");
        list.add("interceptor.CounterInterceptor");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, list);
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i<10; i++){
            producer.send(new ProducerRecord<String, String>("bigdata", "data"+i));
        }

        // 关闭资源
        // 一定要关闭 producer，这样才会调用 interceptor 的 close 方法
        producer.close();
    }
}
