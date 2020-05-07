package com.cheng.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseJava {
    /**
     * 带有hbase配置的Configuration
     */
    static Configuration conf = HBaseConfiguration.create();

    static TableName tableName = TableName.valueOf("table1");

    public static void main(String[] args) throws IOException {
        getTable();
    }

    private static void getTable() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(tableName);

        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        for(Result res: scanner){
            printRowData(res);
        }
    }

    /**
     * 打印一行数据
     */
    public static void printRowData(Result result) {
        Cell[] rawCells = result.rawCells();
        StringBuilder sb = new StringBuilder();

        for(Cell cell : rawCells){
            //id01	column=cf1:age, timestamp=1554967509956, value=10
            String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
            String cf = Bytes.toString(CellUtil.cloneFamily(cell));
            String column = Bytes.toString(CellUtil.cloneQualifier(cell));
            long timestamp = cell.getTimestamp();
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            sb.append(rowkey).append("\tcolumn=").append(cf).append(":")
                    .append(column).append(",timestmap=").append(timestamp)
                    .append(", value=").append(value).append("\n");
        }

        System.out.println(sb.toString());
    }
}
