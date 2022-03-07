package com.alibaba.base;

import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

//*
// * @author fada.yu
// * @date 2020/10/9 15:56
// * @Description：


public class MySource implements SourceFunction<WaterSensor> {
    boolean flag = true;

    @Override
    public void run(SourceContext<WaterSensor> sourceContext) throws Exception {
        Random random = new Random();
        while (flag) {
            sourceContext.collect(
                    new WaterSensor(
                            "sensor_" + (random.nextInt(3) + 1),
                            System.currentTimeMillis(),
                            random.nextInt(35) + 50


                    )
            );
            Thread.sleep(1000L);

        }

    }

    @Override
    public void cancel() {
        flag = false;

    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<WaterSensor> inputDS = env.addSource(new MySource());
//
//        inputDs.print();
//        env.execute();

//lambda->
//        SingleOutputStreamOperator<WaterSensor> sensorDS = inputDS
//                .map((MapFunction<String, WaterSensor>) value -> {
//                    String[] datas = value.split(",");
//                    return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
//                });


//        SingleOutputStreamOperator<Integer> resultDS = inputDS.flatMap(
//                new FlatMapFunction<List<Integer>, Integer>() {
//                    @Override
//                    public void flatMap(List<Integer> value, Collector<Integer> out) throws Exception {
//                        for (Integer element : value) {
//                            out.collect(element);
//                        }
//                    }
//                }
//        );


//        SingleOutputStreamOperator<WaterSensor> filterDS = inputDS
//                .filter(new FilterFunction<Integer>() {
//            @Override
//            public boolean filter(Integer value) throws Exception {
//                return value > 5;
//            }
//        });


        //SplitStream<WaterSensor> splitSS = inputDS.split(
        //        new OutputSelector<WaterSensor>() {
        //            @Override
        //            public Iterable<String> select(WaterSensor value) {
        //                if (value.getVc() < 50) {
        //                    return Arrays.asList("normal");
        //                } else if (value.getVc() < 80) {
        //                    return Arrays.asList("warn");
        //
        //                } else {
        //                    return Arrays.asList("alarm");
        //                }
        //
        //
        //            }
        //        });
        //
        //splitSS.select("normal").print("normal");
        //splitSS.select("warn").print("warn");
        //splitSS.select("alarm").print("alarm");



        env.execute("job lambda");


    }


    public class MyMapFunction implements MapFunction<String, WaterSensor> {
        @Override
        public WaterSensor map(String value) throws Exception {
            String[] datas = value.split(",");


            return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
        }

    }


    public static class MyRichMapFunction extends RichMapFunction<String, WaterSensor> {


        @Override
        public WaterSensor map(String value) throws Exception {
            String[] datas = value.split(",");
            return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("open....");
        }

        @Override
        public void close() throws Exception {
            System.out.println("close....");
        }
    }


}


