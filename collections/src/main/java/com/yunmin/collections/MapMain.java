package com.yunmin.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luoyunmin on 2017/5/28.
 */

public class MapMain {
    public static void main(String[] args) {
        Map<String, Student> studentHashMap = new HashMap<>();
        Student luoyunmin = new Student("luoyunmin", 0, 23);
        studentHashMap.put("luoyunmin", luoyunmin);
    }
}
