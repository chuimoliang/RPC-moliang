package com.moliang.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Use
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

    public static void main(String[] args) {
        int[][] test = new int[][]{{3,4,5},{3,2,6},{2,2,1}};
        Solution s = new Solution();
        System.out.println(s.longestIncreasingPath(test));
    }
}
