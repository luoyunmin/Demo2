package com.yunmin.collections;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by luoyunmin on 2017/5/28.
 * 使用递归实现一个二分查找
 */

public class BinarySearchMain {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 15, 18, 19, 23, 27, 28, 30};
        int position = 0;
        Arrays.binarySearch(array, 14, 14, position);
        while (position <= array[array.length - 1]) {
            System.out.println(binarySearch(position, array, 0, array.length));
            position++;
        }

        UUID uuid=UUID.randomUUID();
        System.out.println(uuid);
    }

    public static int binarySearch(int key, int[] array, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return array[startIndex] == key ? startIndex : -1;
        }
        int middleIndex = (startIndex + endIndex - 1) / 2;
        if (key == array[middleIndex]) {
            return middleIndex;
        } else if (key < array[middleIndex]) {
            return binarySearch(key, array, 0, middleIndex);
        } else {
            return binarySearch(key, array, middleIndex + 1, endIndex);
        }
    }
}
