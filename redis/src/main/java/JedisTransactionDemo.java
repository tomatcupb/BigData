import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class JedisTransactionDemo {
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("s4.hadoop",6379);
        int payback = 20;
        jedis.watch("positive", "negative");
        System.out.println("============sleep===============");
        TimeUnit.SECONDS.sleep(5);
        Transaction transaction = jedis.multi();
        transaction.decrBy("positive", payback);
        transaction.incrBy("negative", payback);
        // 事务失败返回null，成功返回修改的结果列表
        List<Object> exec = transaction.exec();
        System.out.println(exec==null);
        for (Object o : exec) {
            System.out.println(o);
        }
//        TimeUnit.SECONDS.sleep(5);
//        int pos = Integer.parseInt(jedis.get("positive"));
//        int neg = Integer.parseInt(jedis.get("negative"));
//        if(pos+neg!=0){
//            System.out.println("value was changed, transaction failed");
//            // 回滚
//            jedis.unwatch();
//        } else {
//            Transaction transaction = jedis.multi();
//            transaction.decrBy("positive", payback);
//            transaction.incrBy("negative", payback);
//            transaction.exec();
//        }
    }
}
