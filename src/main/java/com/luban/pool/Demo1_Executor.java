package com.luban.pool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

/**
 * 认识Executor
 * JDK8在线文档地址:https://docs.oracle.com/javase/8/docs/api/index.html
 * 补充  CountDownLatch 使用场景 ：1 、等所有线程一起执行 2、某个线程需要等一些线程执行完后再执行
 */
public class Demo1_Executor implements Executor{

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(15);
		// 下面的效果 是让 3个线程一起执行
		Semaphore semaphore = new Semaphore(3);
		// 组合
		semaphore.acquire(); // 占坑
		semaphore.release();// 释放

	}

	@Override
	public void execute(Runnable command) {
		command.run();
	}
}
