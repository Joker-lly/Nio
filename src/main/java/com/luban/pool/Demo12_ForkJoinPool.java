package com.luban.pool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class Demo12_ForkJoinPool {
	static int[] nums = new int[1000000];
	static final int MAX_NUM = 50000;
	static Random r = new Random();

	static {
		for(int i=0; i<nums.length; i++) {
			nums[i] = r.nextInt(100);
		}
		System.out.println(Arrays.stream(nums).sum());
	}
	
	/*static class AddTask extends RecursiveAction {
		
		int start, end;
		
		AddTask(int s, int e) {
			start = s;
			end = e;
		}

		@Override
		protected void compute() {
			if(end-start <= MAX_NUM) {
				long sum = 0L;
				for(int i=start; i<end; i++){
					sum += nums[i];
				}
				System.out.println(sum);
			} else {
				int middle = start + (end-start)/2;
				AddTask subTask1 = new AddTask(start, middle);
				AddTask subTask2 = new AddTask(middle, end);
				subTask1.fork();
				subTask2.fork();
			}
		}
	}*/

	static class AddTask extends RecursiveTask<Long> {

		int start, end;
		
		AddTask(int s, int e) {
			start = s;
			end = e;
		}

		@Override
		protected Long compute() {
			if(end-start <= MAX_NUM) {
				long sum = 0L;
				for(int i=start; i<end; i++){
					sum += nums[i];
				}
				return sum;
			} 
			int middle = start + (end-start)/2;
			AddTask subTask1 = new AddTask(start, middle);
			AddTask subTask2 = new AddTask(middle, end);
			subTask1.fork();
			subTask2.fork();
			return subTask1.join() + subTask2.join();
		}
	}
	
	public static void main(String[] args) {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		AddTask task = new AddTask(0, nums.length);
		forkJoinPool.execute(task);
		long result = task.join();
		System.out.println(result);

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
