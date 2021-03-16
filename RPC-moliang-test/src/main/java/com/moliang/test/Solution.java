package com.moliang.test;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

/**
 * @Use LeetCode
 * @Author Chui moliang
 * @Date 2021/1/22 13:26
 * @Version 1.0
 */
public class Solution {

    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return intervals;
        Arrays.sort(intervals, (Comparator.comparingInt(o -> o[0])));
        int[][] ans = new int[10][];
        int index = 0;
        int pre = intervals[0][0], big = intervals[0][1];
        for(int i = 1;i < intervals.length;i++) {
            int[] t = intervals[i];
            if (t[0] > big) {
                if (index == ans.length) {
                    ans = Arrays.copyOf(ans, index + index);
                }
                ans[index] = new int[]{pre, big};
                index++;
                pre = t[0];
            }
            big = Math.max(t[1], big);
        }
        ans[index] = new int[]{pre, big};
        return Arrays.copyOf(ans, index + 1);
    }

    int[][] matrix;
    int[][] result;
    int[][] visit;
    int r;
    int ct;
    public int longestIncreasingPath(int[][] matrix) {
        this.matrix = matrix;
        r = matrix.length; ct = matrix[0].length;
        result = new int[r][ct];
        visit = new int[r][ct];
        for(int[] e: result) Arrays.fill(e, -1);
        int ans = 0;
        for(int i = 0;i < r;i++) {
            for(int j = 0;j < ct;j++) {
                ans = Math.max(ans, once(i, j, visit, 0));
            }
        }
        return ans;
    }

    int[][] step = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    public int once(int i, int j, int[][] visit, int sum) {
        visit[i][j] = 1;
        int now = matrix[i][j];
        sum++;
        int ans = sum;
        for(int c = 0;c < 4;c++) {
            int ix = i + step[c][0], iy = j + step[c][1];
            if(ix >= 0 && iy >= 0 && ix < r && iy < ct && visit[ix][iy] == 0 && matrix[ix][iy] > now) {
                if(result[ix][iy] != -1) ans = Math.max(ans, sum + result[ix][iy]);
                else ans = Math.max(ans, once(ix, iy, visit, sum));
            }
        }
        visit[i][j] = 0;
        result[i][j] = ans - sum + 1;
        return ans;
    }

    int[][] points;
    public int maxPoints(int[][] points) {
        if(points.length < 2) return points.length;
        this.points = points;
        Map<Edge, Set<Integer>> map = new HashMap<>();
        for(int i = 0;i < points.length;i++) {
            for(int j = i + 1;j < points.length;j++) {
                Edge edge = calculate(i, j);
                Set<Integer> set = map.getOrDefault(edge, new HashSet<Integer>());
                set.add(i); set.add(j);
                map.put(edge, set);
            }
        }
        int ans = 0;
        for(Set<Integer> e : map.values()) {
            System.out.println(e);
            ans = Math.max(ans, e.size());
        }
        return ans;
    }

    public Edge calculate(int i, int j) {
        if(points[i][0] == points[j][0]) {
            return new Edge(points[j][0]);
        }
        double k = (double) (points[i][1] - points[j][1]) / (double) (points[i][0] - points[j][0]);
        float b = (float) ((double) points[i][1] - k * (double) points[i][0]);
        return new Edge(k, b);
    }

    class Edge {
        double k;
        double b;
        int x;
        public Edge(double k, float b) {
            this.k = k;
            this.b = b;
            x = 0;
        }

        public Edge(int x) {
            this.x = x;
        }

        @Override
        public int hashCode() {
            if(x == 0)
            return (int)k << 2 + (int)b << 2 ;
            else return x << 2;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Edge) {
                return this.k == ((Edge) obj).k && this.b == ((Edge) obj).b && this.x == ((Edge) obj).x;
            }
            return false;
        }

    }

    public String fractionToDecimal(int numerator, int denominator) {
        long x = (long) Math.abs(numerator), y = (long) Math.abs(denominator);
        long value = x / y;
        long remain = x % y;
        if(x < 0 ^ y < 0) value *= -1;
        if(remain == 0) return String.valueOf(value);
        StringBuilder str = new StringBuilder();
        str.append(String.valueOf(value) + ".");
        Map<Long, Integer> map = new HashMap<>();
        while(remain != 0) {
            if(map.containsKey(remain)) {
                str.insert(map.get(remain), "(");
                str.append(")");
                return new String(str);
            }
            map.put(remain, str.length());
            str.append(String.valueOf(remain * 10 / y));
            remain = (remain * 10) % y;
        }
        return null;
    }

    public int reverseBits(int n) {
        return Integer.reverse(n);
    }

    public void futureTest() throws ExecutionException, InterruptedException {
        ExecutorService executors = Executors.newSingleThreadExecutor();
        Future<String> future = executors.submit(()-> {
            for(int i = 0;i < 100;i++) {
                System.out.println(Thread.currentThread().getName() + i);
            }
            return Thread.currentThread().getName();
        });
        System.out.println(Thread.currentThread().getName() + " +++ " + future.get());
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public int[] levelOrder(TreeNode root) {
        if(root == null) return new int[0];
        int[] nums = new int[10]; int index = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()) {
            int size = q.size();
            for(int i = 0;i < size;i++) {
                TreeNode node = q.poll();
                if(nums.length <= index) {
                    int length = index + index >> 1;
                    nums = Arrays.copyOf(nums, length);
                }
                nums[index] = node.val;
                index++;
                if(node.left != null) q.add(node.left);
                if(node.right != null) q.add(node.right);
            }
        }
        return Arrays.copyOf(nums, index);
    }

    public int countBalls(int lowLimit, int highLimit) {
        int[] nums = new int[10];
        int ans = 0;
        for(int i = lowLimit;i <= highLimit;i++) {
            int index = calculate(i);
            while(nums.length <= index) {
                nums = Arrays.copyOf(nums, nums.length << 1);
            }
            nums[index]++;
            ans = Math.max(nums[index], ans);
        }
        return ans;
    }

    public int calculate(int x) {
        int ans = 0;
        while(x > 0) {
            ans += x % 10;
            x /= 10;
        }
        return ans;
    }

    public int[] restoreArray(int[][] adjacentPairs) {
        int n = adjacentPairs.length + 1;
        int[] ans = new int[n];
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for(int[] t : adjacentPairs) {
            List<Integer> l1 = map.getOrDefault(t[0], new ArrayList<>());
            l1.add(t[1]);
            map.put(t[0], l1);
            List<Integer> l2 = map.getOrDefault(t[1], new ArrayList<>());
            l2.add(t[0]);
            map.put(t[1], l2);
        }
        int index = 0;
        for(Map.Entry<Integer, List<Integer>> t : map.entrySet()) {
            if(t.getValue().size() == 1) index =t.getKey();
        }
        int i = 0;
        while(i < n) {
            ans[i] = index;
            int next = map.get(index).get(0);
            List<Integer> temp = map.get(next);
            for(int z = 0;z < 2;z++) {
                if (temp.get(z) == index) {
                    temp.remove(z);
                    break;
                }
            }
            i++;
        }
        return ans;
    }

    public boolean[] canEat(int[] candiesCount, int[][] queries) {
        boolean[] ans = new boolean[queries.length];
        double[] tab1 = new double[candiesCount.length];
        double[] tab2 = new double[candiesCount.length];
        double temp = 0; int c = 0;
        for(int e : candiesCount) {
            temp += e;
            tab2[c] = temp;
            if(c < tab1.length - 1) tab1[c + 1] = temp;
            c++;
        }
        c = 0;
        for(int[] t : queries) {
            ans[c] = tab1[t[0]] / (double) t[2] < (double) (t[1] + 1) && tab2[t[0]] > t[1];
            c++;
        }
        return ans;
    }
    int[][] res;
    char[] chars;
    public boolean checkPartitioning(String s) {
        int len = s.length();
        res = new int[len][len];
        chars = s.toCharArray();
        List<Integer> ll = new ArrayList<>();
        for(int i = 0;i < len;i++) {
            res[i][i] = 2;
        }
        for(int i = 1;i < len - 2;i++) {
            for(int l = 1;l + i < len;l++) {
                if(isPartition(0, l -1) && isPartition(l, l + i - 1) && isPartition(l + i, len - 1))
                    return true;
            }
        }
        return false;
    }
    public boolean isPartition(int l, int r) {
        if(res[l][r] > 0) return res[l][r] > 1;
        boolean ans = chars[l] == chars[r] &&(l > r || isPartition(l + 1, r - 1));
        if(ans)
        res[l][r] = 2;
        else res[l][r] = 1;
        return ans;
    }

    static {
        System.out.println(System.currentTimeMillis() + "--------------Solution 静态代码块 ---------------");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        Solution s = new Solution();
        Thread.sleep(1000);
        Test t = new Test();
        Thread.sleep(1000);
        Test t2 = new Test();
    }
}
