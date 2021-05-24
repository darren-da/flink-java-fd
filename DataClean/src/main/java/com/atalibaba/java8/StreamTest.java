package com.atalibaba.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author :YuFada
 * @date： 2021/5/24 0024 下午 16:59
 * Description：
 */
public class StreamTest {
    @Test
    public void TestStream(){
        ArrayList<String> list = new ArrayList<>();
        Stream<String> stream1 = list.stream();

        Stream<String> stream3 = Stream.of("hxh", "aj", "hhh");

        Stream<Integer> stream4 = Stream.iterate(0, x -> (x + 2));
        Stream<Integer> limit = stream4.limit(6);

        //generate
        Stream<Double> generate = Stream.generate(() -> Math.random());
//        generate.limit(10).forEach(System.out::println);


        //filter

        List<Stu> stuList = Arrays.asList(
                new Stu(1,"hh",22),
                new Stu(2,"aa",22),
                new Stu(3,"bb",32),
                new Stu(4,"cc",42),
                new Stu(5,"dd",52)
        );

        Stream<Stu> stuStream = stuList.stream().filter(x ->
                (x.getAge() > 20));

//       stuStream.forEach(System.out::println);


//        stuList.stream()
////                .filter((s) ->{
////                    System.out.println("测试迭代几次");
////                    return s.getAge()>40;
////                }).limit(2).forEach(System.out::println);

        //todo for skip()
//        stuStream.skip(2).forEach(System.out::println);


        //todo for distinct

        List<Stu> stuList2 = Arrays.asList(
                new Stu(1,"hh",22),
                new Stu(2,"aa",22),
                new Stu(3,"bb",32),
                new Stu(4,"cc",42),
                new Stu(4,"cc",42),
                new Stu(4,"cc",42),
                new Stu(4,"cc",42),
                new Stu(5,"dd",52)
        );
//        此时的Stu类中没有生成重写hashCode()和equals()方法
        stuList2.stream().distinct().forEach(System.out::println);

    }
}
