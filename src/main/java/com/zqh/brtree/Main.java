package com.zqh.brtree;

import com.alibaba.fastjson.JSON;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by OrangeKiller on 2017/3/19.
 */
public class Main {
    public static void main(String[] args) {
        BRTree tree = new BRTree();
        Random rd = new Random();
        Integer[] a = {21, 57, 45, 83, 33, 54, 49, 38, 89, 61};
//        Integer[] a = new Integer[100];
//        for (int i = 0; i< 100; i++) {
//            a[i] = rd.nextInt(1000);
//        }
        System.out.println(Arrays.toString(a));
        for (int i = 0; i< 10; i++) {
            tree.insert(a[i]);
        }
        System.out.println(tree);
        BRTree.printTree(tree.getRoot(),  BRTree.getDepth(tree.getRoot()), 0, 0, tree.getRoot(), null);
//        Integer[] b = {21, 57, 45, 83, 33, 54, 49, 38, 89, 61};
        for (int i = 0; i< 10; i++) {
            tree.delete(a[i]);
        }
        System.out.println(tree);

    }
}
