package com.yunmin.collections;

/**
 * Created by luoyunmin on 2017/5/27.
 */

public class Student {
    private String name;
    private int sex;//0 or 1
    private int age;

    public Student(String name, int sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "name:" + name + " , sex:" + (sex == 0 ? "male" : (sex == 1 ? "female" : "null")) + " , age:" + age;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
