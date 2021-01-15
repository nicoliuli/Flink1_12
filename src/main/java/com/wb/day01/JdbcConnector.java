package com.wb.day01;

import com.wb.common.WordCnt;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 从socket读取，写入mysql
 */
public class JdbcConnector {
    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        SingleOutputStreamOperator<WordCnt> dataStream = env.socketTextStream("localhost", 8888).map(new MapFunction<String, WordCnt>() {
            @Override
            public WordCnt map(String s) throws Exception {
                return new WordCnt(Integer.parseInt(s), s, "aaa.txt", 1);
            }
        });

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env,settings);

        tableEnv.createTemporaryView("word_cnt",dataStream);

        String sqlQuery = "select id,word,filepath,cnt from word_cnt";
        Table table = tableEnv.sqlQuery(sqlQuery);


        // 通过Connector注册MySQL表
        String sinkTable =
                "create table a_word_cnt " +
                        "(id int," +
                        "word String," +
                        "filepath String," +
                        "cnt int) " +
                "with (" +
                        "'connector' = 'jdbc'," +
                        "'url' = 'jdbc:mysql://devtest.wb.sql.wb-intra.com:13306/spy?useUnicode=true&characterEncoding=UTF-8', " +
                        "'driver' = 'com.mysql.jdbc.Driver'," +
                        "'table-name' = 'a_word_cnt'," +
                        "'username' = 'test_liuli'," +
                        "'password' = 'p!rM+LXMR9*e='" +
                ")";

        tableEnv.executeSql(sinkTable);
        // 写入mysql表
        table.executeInsert("a_word_cnt");


        env.execute("Flink Streaming Java API Skeleton");
    }
}
