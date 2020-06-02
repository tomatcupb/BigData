package interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String, String> {
    int success;
    int error;
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }


    // 发送成功返回metadata， 失败返回exception
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if(exception==null){
            success++;
        }else {
            error++;
        }
    }

    // 批次数据发送完成后调用
    public void close() {
        System.out.println("success:"+success);
        System.out.println("error"+error);
    }

    public void configure(Map<String, ?> configs) {

    }
}
