package com.moliang.test;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Use LeetCode
 * @Author Chui moliang
 * @Date 2021/1/22 13:26
 * @Version 1.0
 */
public class Solution {

    public boolean isPowerOfTwo(int n) {
        if (n == 1) {
            return true;
        }
        return (n / 2) * 2 == n && isPowerOfTwo(n / 2);
    }
















    public int strStr(String haystack, String needle) {
        if (needle == null || needle.equals("")) return 0;
        int[] next = new int[needle.length()];
        next[0] = 0;
        for (int i = 0, j = 1;j < needle.length();j++) {
            while (i > 0 && needle.charAt(i) != needle.charAt(j)) {
                i = next[i - 1];
            }
            if (needle.charAt(i) == needle.charAt(j)) {
                i++;
            }
            next[j] = i;
        }
        for (int i = 0, j = 0;i < haystack.length();i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = next[j];
            }
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            if (j == needle.length()) {
                return i - j + 1;
            }
        }
        return -1;
    }

    public int countRestrictedPaths(int n, int[][] edges) {
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        for (int[] t : edges) {
            int x = t[0], y = t[1], m = t[2];
            Map<Integer, Integer> ma = map.getOrDefault(x, new HashMap<>());
            ma.put(y, m);
            map.put(x, ma);
            Map<Integer, Integer> mb = map.getOrDefault(y, new HashMap<>());
            mb.put(x, m);
            map.put(y, mb);
        }
        int[] tab = new int[n + 1];
        Arrays.fill(tab, Integer.MAX_VALUE);
        tab[n] = 0;
        boolean[] visit = new boolean[n + 1];
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
        queue.add(new int[]{n, 0});
        while (!queue.isEmpty()) {
            int[] temp = queue.poll();
            if (visit[temp[0]]) continue;
            visit[temp[0]] = true;
            Map<Integer, Integer> m = map.get(temp[0]);
            for (Integer key : m.keySet()) {
                int val = m.get(key) + temp[1];
                if (val < tab[key]) {
                    tab[key] = val;
                    queue.add(new int[]{key, val});
                }
            }
        }
        int[][] arr = new int[n][2];
        for (int i = 0;i < n;i++) {
            arr[i] = new int[]{i + 1, tab[i + 1]};
        }
        Arrays.sort(arr, Comparator.comparingInt(o -> o[1]));
        int[] ans = new int[n + 1];
        ans[n] = 1;
        for (int i = 1;i < n;i++) {
            int index = arr[i][0], w = arr[i][1];
            Map<Integer, Integer> m = map.get(index);
            if (m == null) continue;
            for (Integer e : m.keySet()) {
                if (w > m.get(e)) {
                    ans[index] += ans[e];
                    ans[index] %= 1000000007;
                }
            }
            if (index == 1) break;
        }
        return ans[1];
    }

    public boolean canCross(int[] stones) {
        int n = stones.length;
        boolean[][] dp = new boolean[n][n];
        dp[0][0] = true;
        for (int i = 1; i < n; ++i) {
            if (stones[i] - stones[i - 1] > i) {
                return false;
            }
        }
        for (int i = 1; i < n; ++i) {
            for (int j = i - 1; j >= 0; --j) {
                int k = stones[i] - stones[j];
                if (k > j + 1) {
                    break;
                }
                dp[i][k] = dp[j][k - 1] || dp[j][k] || dp[j][k + 1];
                if (i == n - 1 && dp[i][k]) {
                    return true;
                }
            }
        }
        return false;
    }

    int[][] nums;
    public int getMoneyAmount(int n) {
        nums = new int[n + 1][n + 1];
        for (int[] t : nums) {
            Arrays.fill(t, -1);
        }
        for (int i = 1;i <= n;i++) {
            nums[i][i] = i;
        }
        return find(1, n);
    }

    private int find(int l, int r) {
        if (l > r) {
            return 0;
        }
        if (nums[l][r] != -1) {
            return nums[l][r];
        }
        int mid = (l + r) / 2;
        int ans = Integer.MAX_VALUE;
        for (int i = mid;i <= r;i++) {
            ans = Math.min(ans, i + Math.max(find(l, i - 1), find(i + 1, r)));
        }
        nums[l][r] = ans;
        return ans;
    }

    /**
     * 1 2 3 4 5 6 7 8 9 10
     *
     *
     * 6 + 12345 78910 15 34
     * 7 123456 8910 15
     * 8 1234567 910 28 19
     *
     */















    private static ReentrantLock lock = new ReentrantLock();
    private static Condition a = lock.newCondition();
    private static Condition b = lock.newCondition();
    private static Condition c = lock.newCondition();
    private static int anInt = 1;


}
