package com.luban.pool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * 认识Executor
 * JDK8在线文档地址:https://docs.oracle.com/javase/8/docs/api/index.html
 * 补充  CountDownLatch 使用场景 ：1 、等所有线程一起执行 2、某个线程需要等一些线程执行完后再执行
 */
public class Demo1_Executor implements Executor{

	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(15);
	}

	@Override
	public void execute(Runnable command) {
		command.run();
	}
}
