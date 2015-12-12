package com.company;

import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

/**
 * Created by alina on 12.12.15.
 */
public class Jacobi {
    private double[][] matrix;
    
    public Jacobi(double[][] matrix){
        this.matrix = matrix;
    }

    public static double[][] multiply(double[][] mat1, double[][] mat2) {
        double[][] mat3 = new double[4][4];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2.length; j++) {
                for (int k = 0; k < mat3.length; k++) {
                    mat3[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return mat3;
    }

    public double[][] zeroing(double[][] a) {

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                if (i != j)
                    if (a[i][j] < 0.0000000001)
                        a[i][j] = 0;
            }
        }
        return a;
    }

    public  double[][] yakobi(double[][] a) {
        double[][] t = new double[4][4];
        for (int i = 0; i < t.length; i++) {
            t[i][i] = 1;
        }
        int maxi = 0;
        int maxj = 1;
        double max = Math.abs(a[0][1]);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                if (i != j)
                    if (Math.abs(max) < Math.abs(a[i][j])) {
                        max = a[i][j];
                        maxi = i;
                        maxj = j;
                    }
            }
        }
        double miu = (2 * max) / (a[maxi][maxi] - a[maxj][maxj]);
        double c = Math.sqrt((1 + 1 / Math.sqrt(1 + miu * miu)) / 2);
        double s = Math.signum(miu) * Math.sqrt((1 - 1 / Math.sqrt(1 + miu * miu)) / 2);
        t[maxi][maxi] = c;
        t[maxi][maxj] = -s;
        t[maxj][maxi] = s;
        t[maxj][maxj] = c;

        double[][] a1 = multiply(a, t);
        double[][] tt = transpose(t);
        return multiply(tt, a1);
    }

    public double[][] transpose(double[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                if (i < j) {
                    double x = t[i][j];
                    t[i][j] = t[j][i];
                    t[j][i] = x;
                }
            }
        }
        return t;
    }

}