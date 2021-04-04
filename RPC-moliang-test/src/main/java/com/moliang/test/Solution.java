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

    public int numRabbits(int[] answers) {
        Map<Integer, Integer> map = new HashMap<>();
        int ans = 0;
        for (int e : answers) {
            if (e == 0) {
                ans ++;
            } else {
                int num = map.getOrDefault(e, 0);
                if (num == e + 1 || num == 0) {
                    ans += e + 1;
                    map.put(e, 1);
                } else {
                    map.put(e, num + 1);
                }
            }
        }
        return ans;
    }

    public String reverseVowels(String s) {
        int l = 0, r = s.length() - 1;
        Set<Character> set = new HashSet<>();
        char[] tab = new char[]{'a', 'o', 'e', 'i', 'u', 'v', 'A', 'O', 'I', 'U', 'V'};
        for (char e : tab) {
            set.add(e);
        }
        char[] chars = s.toCharArray();
        while(l < r) {
            while(l < r && !set.contains(s.charAt(l))) l++;
            while(l < r && !set.contains(s.charAt(r))) r--;
            char temp = chars[l];
            chars[l] = chars[r];
            chars[r] = temp;
            l++;
            r--;
        }
        return String.valueOf(chars);
    }

    private List<String> ans = new ArrayList<>();
    Map<String, PriorityQueue<String>> map;
    public List<String> findItinerary(List<List<String>> tickets) {
        map = new HashMap<>();
        for (List<String> l : tickets) {
            if (!map.containsKey(l.get(0))) {
                map.put(l.get(0), new PriorityQueue<>());
            }
            map.get(l.get(0)).add(l.get(1));
        }
        dfs("JFK");
        Collections.reverse(ans);
        return ans;
    }

    private void dfs(String s) {
        while (map.get(s) != null && map.get(s).size() > 0) {
            dfs(map.get(s).poll());
        }
        ans.add(s);
    }
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        Runtime.getRuntime().availableProcessors();
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            Object x = items[takeptr];
            System.out.println(x.toString());
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    static Solution s = new Solution();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (true) {
                try {
                    s.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(()->{
            int i = 0;
            while (true) {
                try {
                    s.put(String.valueOf(i++));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
        Thread.sleep(1000);

    }
}
