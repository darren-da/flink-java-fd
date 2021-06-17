package com.alibaba.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author :YuFada
 * @date： 2021/5/24 0024 下午 18:30
 * Description：
 * map
 * flatMap
 */
public class Operator {

    /**
     * 字符串拆分成字符后组成一个字符类型的流
     * @param str
     * @return
     */
    public static Stream<Character> filterCharacter(String str){
        List<Character> characterList = new ArrayList<>();
        for (Character ch: str.toCharArray()
                ) {
            characterList.add(ch);
        }
        return characterList.stream();
    }

    @Test
    public void TestMap() {
        //map映射
        List<String> stringList = Arrays.asList("aa", "bb", "cc", "dd");
        stringList.stream()
                .map(x -> (x.length())).forEach(System.out::println);

        stringList.stream()
                .map(x -> (x.toUpperCase())).limit(2).forEach(System.out::println);


    }

    @Test
    public void TestFlatMap(){
        List<String> stringList = Arrays.asList("aa", "bb", "cc", "dd");
        Stream<Stream<Character>> st1 = stringList.stream()
                .map(Operator::filterCharacter);
//此时流的内容为 {{"aa"},{"bb"},{"cc"},{"dd"}} 4个单独的字符流对象组成的流
//        st1.forEach(System.out::println);


        System.out.println("----------------");

//        Stream<Character> st2 = stringList.stream().flatMap(Operator::filterCharacter);
        Stream<Character> st2 = stringList.stream().flatMap(x -> (filterCharacter(x)));
//此时流的内容为{"a","a","b","b","c","c","d","d"}
        st2.forEach(System.out::println);

    }

    @Test
    public void sortedTest(){
        List<String> stringList = Arrays.asList("ee", "bb", "ff", "dd","哈哈","啊");
//根据String类中Comparable方式进行默认排序，即compare to()方法
        stringList.stream()
                .sorted().forEach(System.out::println);


        List<Stu> stuList = Arrays.asList(
                new Stu(1,"hh",22),
                new Stu(2,"aa",22),
                new Stu(3,"bb",32),
                new Stu(4,"cc",42),
                new Stu(5,"dd",52)
        );

        stuList.stream().sorted(
                (a,b) ->{
                    if (String.valueOf(a.getAge()).equals(String.valueOf(b.getAge()))){
                     return    a.getName().compareTo(b.getName());
                    }else {
                       return String.valueOf(a.getAge()).compareTo(String.valueOf(b.getAge()));
                    }
                }
        ).forEach(System.out::println);


    }


}
