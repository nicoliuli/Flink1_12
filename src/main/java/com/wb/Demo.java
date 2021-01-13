package com.wb;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class Demo {
    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env,settings);

        String s =
"create table order_item (id String,name String) with ('connector' = 'jdbc','url' = 'jdbc:mysql://devtest.wb.sql.wb-intra.com:13306/spy?useUnicode=true&characterEncoding=UTF-8', 'driver' = 'com.mysql.jdbc.Driver','table-name' = 't_order_item','username' = 'test_liuli','password' = 'p!rM+LXMR9*e=')";
        tableEnv.executeSql(s);


        env.execute("Flink Streaming Java API Skeleton");
    }
}
