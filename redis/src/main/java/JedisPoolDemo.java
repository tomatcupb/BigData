import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolDemo {
    public static void main(String[] args) {
        JedisPool jedisPool1 = MyJedisPool.getJdisPoolInstance();
        JedisPool jedisPool2 = MyJedisPool.getJdisPoolInstance();
        System.out.println(jedisPool1==jedisPool2);

        Jedis jedis = null;
        try {
            jedis = jedisPool1.getResource();
            jedis.set("k100", "1000");
            jedis.set("k101", "1001");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            MyJedisPool.release(jedis);
        }
    }
}

class MyJedisPool{
    private MyJedisPool(){}
    static volatile JedisPool pool;
    public static JedisPool getJdisPoolInstance(){
        if(pool==null){
            synchronized (MyJedisPool.class){
                if (pool==null){
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(32);
                    config.setMaxTotal(1000);
                    config.setMaxWaitMillis(10*1000);
                    config.setTestOnBorrow(true);
                    pool = new JedisPool(config,"s4.hadoop",6379);
                }
            }
        }
        return pool;
    }


    public static void release(Jedis jedis) {
        if(null != jedis){
            jedis.close();
        }
    }
}
