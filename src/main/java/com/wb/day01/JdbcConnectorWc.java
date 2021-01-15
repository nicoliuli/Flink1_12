package com.wb.day01;

import com.wb.common.WC;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * flink sql
 * word count案例
 */
public class JdbcConnectorWc {
    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        SingleOutputStreamOperator<WC> dataStream = env.socketTextStream("localhost", 8888).map(new MapFunction<String, WC>() {
            @Override
            public WC map(String s) throws Exception {
                return new WC(s,1L);
            }
        });

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env,settings);

        tableEnv.createTemporaryView("wc",dataStream);

        String sqlQuery = "select word,count(*) as cnt from wc group by word";
        Table table = tableEnv.sqlQuery(sqlQuery);


        // 通过Connector注册MySQL表
        String sinkTable =
                "create table a_wc " +
                        "(word String primary key," +
                        "cnt BIGINT) " +
                "with (" +
                        "'connector' = 'jdbc'," +
                        "'url' = 'jdbc:mysql://devtest.wb.sql.wb-intra.com:13306/spy?useUnicode=true&characterEncoding=UTF-8', " +
                        "'driver' = 'com.mysql.jdbc.Driver'," +
                        "'table-name' = 'a_wc'," +
                        "'username' = 'test_liuli'," +
                        "'password' = 'p!rM+LXMR9*e='" +
                ")";

        tableEnv.executeSql(sinkTable);
        // 写入mysql表
        table.executeInsert("a_wc");


        env.execute("Flink Streaming Java API Skeleton");
    }
}
