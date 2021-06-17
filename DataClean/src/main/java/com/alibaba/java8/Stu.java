package com.alibaba.java8;

import java.util.Objects;

/**
 * @author :YuFada
 * @date： 2021/5/24 0024 下午 17:56
 * Description：
 */
public class Stu {
    private int id;
    private String name;
    private int age;

    @Override
    public String toString() {
        return "Stu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String  getName() {
       return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Stu() {
    }

    public Stu(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stu stu = (Stu) o;
        return Objects.equals(id, stu.id) &&
                Objects.equals(name, stu.name) &&
                Objects.equals(age, stu.age);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, age);
    }



}
