package com.moliang.test;

import io.netty.buffer.CompositeByteBuf;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Use LeetCode
 * @Author Chui moliang
 * @Date 2021/1/22 13:26
 * @Version 1.0
 */
public class Solution {

    public int findMin(int[] nums) {
        return find(nums, 0, nums.length - 1);
    }

    private int find(int[] nums, int l, int r) {
        if (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] < nums[r]) {
                return find(nums, l, mid);
            }
            if (nums[mid] > nums[r]) {
                return find(nums, mid + 1, r);
            }
            return find(nums, l, r - 1);
        }
        return nums[l];
    }







    private static ReentrantLock lock = new ReentrantLock();
    private static Condition a = lock.newCondition();
    private static Condition b = lock.newCondition();
    private static Condition c = lock.newCondition();
    private static int num = 1;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (true) {
                lock.lock();
                try {
                    if (num != 1) {
                        a.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    num++;
                    b.signal();
                    lock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A");
        Thread t2 = new Thread(()->{
            while (true) {
                lock.lock();
                try {
                    if (num != 2) {
                        b.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    num++;
                    c.signal();
                    lock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B");
        Thread t3 = new Thread(()->{
            while (true) {
                lock.lock();
                try {
                    if (num != 3) {
                        c.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    num = 1;
                    a.signal();
                    lock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("程序执行完毕, 退出");
        }));
        String s = "3,5,5,6,9,1,4,5,0,5],[2,7,9,5,9,5,4,9,6,8],[6,0,7,8,1,0,1,6,8,1],[7,2,6,5,8,5,6,5,0,6],[2,3," +
                "3,1,0,4,6,5,3,5],[5,9,7,3,8,8,5,1,4,3],[2,4,7,9,9,8,4,7,3,7],[3,5,2,8,8,2,2,4,9,8";
        String[] t = s.split("],\\[");
        int[][] nums = new int[t.length][];
        for (int i = 0;i < t.length;i++) {
            String[] temp = t[i].split(",");
            int[] num = new int[temp.length];
            for (int j = 0;j < num.length;j++) {
                num[j] = Integer.parseInt(temp[j]);
            }
            nums[i] = num;
        }
        for (int[] te : nums) {
            System.out.println(Arrays.toString(te));
        }
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(5000);

    }
}
