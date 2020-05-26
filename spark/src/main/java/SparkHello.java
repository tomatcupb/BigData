import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SparkHello {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("sparkhello").setMaster("local[6]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        JavaRDD<String> textFile = jsc.textFile("C:\\Users\\SirAlex\\Desktop\\input\\test.txt");
        JavaRDD<String> filter = textFile.filter(new Function<String, Boolean>() {
            public Boolean call(String v1) throws Exception {
                return v1.contains("o");
            }
        });

        JavaRDD<String> flatMap = filter.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(String s) throws Exception {
                ArrayList<String> strings = new ArrayList<String>();
                String[] s1 = s.split(" ");
                for (int i = 0; i < s1.length; i++) {
                    strings.add(s1[i]);
                }
                return strings.iterator();
            }
        });


        JavaRDD<Tuple2<String, Integer>> map = flatMap.map(new Function<String, Tuple2<String, Integer>>() {
            public Tuple2<String, Integer> call(String v1) throws Exception {
                return new Tuple2<String, Integer>(v1, 1);
            }
        });

        JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> groupBy = map.groupBy(new Function<Tuple2<String, Integer>, String>() {
            public String call(Tuple2<String, Integer> v1) throws Exception {
                return v1._1;
            }
        });

        List<Tuple2<String, Iterable<Tuple2<String, Integer>>>> collect = groupBy.collect();

        System.out.println(collect);

    }

}