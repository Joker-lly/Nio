package com.luban.netty.lbrpc.server.service.impl;

import com.luban.netty.lbrpc.server.service.TestService;

import java.util.ArrayList;
import java.util.List;

public class TestServiceImpl implements TestService {

    static ArrayList<String> list = new ArrayList<>();

    static {
        list.add("张三");
        list.add("李四");
    }

    @Override
    public List<String> listAll() {
        return list;
    }

    @Override
    public String listByid(Integer id) {
        return list.get(id);
    }
}
