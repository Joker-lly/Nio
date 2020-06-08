package com.luban.testdome;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestUnmodifiableSet {
    public static void main(String[] args) {
        Set<String> selectedKeys = new HashSet();
        Set<String> publicKeys = Collections.unmodifiableSet(selectedKeys);
        selectedKeys.add("太白");
        selectedKeys.add("商鞅");
        System.out.println(selectedKeys);
        selectedKeys.remove("太白");
        System.out.println(publicKeys);
    }
}
