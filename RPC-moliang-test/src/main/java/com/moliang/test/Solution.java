package com.moliang.test;

import java.util.*;

/**
 * @Use leetcode刷题所用临时文件
 * @Author Chui moliang
 * @Date 2021/1/22 13:26
 * @Version 1.0
 */
public class Solution {
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

    public static void main(String[] args) {
        int[][] test = new int[][]{{1,1},{2,1},{2,2},{1,4},{3,3}};
        Solution s = new Solution();
        System.out.println(s.maxPoints(test));
    }
}
