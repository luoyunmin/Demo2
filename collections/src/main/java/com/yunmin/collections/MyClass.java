package com.yunmin.collections;

import java.util.HashSet;
import java.util.Set;

public class MyClass {
    public static void main(String[] args) {
        //HashSet
        System.out.println("----------HashSet----------");
        Set<Student> studentHashSet = new HashSet<>();
        addSet(studentHashSet);
        traversal(studentHashSet);



        //TreeSet
//        System.out.println("----------TreeSet----------");
//        Set<Student> studentTreeSet = new TreeSet<>();
//        addSet(studentTreeSet);
//        traversal(studentTreeSet);

        //LinkedHashSet
//        System.out.println("----------LinkedHashSet----------");
//        Set<Student> studentLinkedHashSet = new LinkedHashSet<>();
//        addSet(studentLinkedHashSet);
//        traversal(studentLinkedHashSet);
    }


    public static void traversal(Set<Student> set) {
        Student tempStudent = new Student("luoyunmin", 0, 23);
        for (Student student : set) {
            System.out.println(student.equals(tempStudent));
            System.out.println(student.hashCode());
            System.out.println(student);
        }
    }

    public static void addSet(Set<Student> set) {
        Student zhangsan = new Student("zhangsan", 0, 20);
        Student lisi = new Student("lisi", 0, 23);
        Student wangwu = new Student("wangwu", 1, 40);
        Student zhaoliu = new Student("zhaoliu", 3, 31);
        Student luoyunmin = new Student("luoyunmin", 0, 23);
        set.add(zhangsan);
        set.add(lisi);
        set.add(wangwu);
        set.add(zhaoliu);
        set.add(luoyunmin);
    }
}
