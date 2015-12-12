package com.company;

public class Main {

    private static double[][] problem1 = {
            // x = 1, y = 2, z = 3
            { 1,  2, 3, 14 },  // 1x + 2y + 3z = 14
            { 1, -1, 1,  2 },  // 1x - 1y + 1z = 2
            { 4, -2, 1,  3 }   // 4x - 2y + 1z = 3
    };

    private static double[][] problem2 = {
            // x = 1, y = 2, z = 3
            { 2,  3, -4, -4 },  // 2x + 3y - 4z = -4
            { 1, -2,  1,  0 },  // 1x - 2y + 1z =  0
            {-1,  1,  2,  7 }   // -1x + 1y + 2z = 7
    };

    public static void main(String[] args) {
        GaussJordan gj = new GaussJordan(problem1);
        System.out.println(gj);
        gj.toReducedRowEchelonForm();
        System.out.println(gj);
        System.out.println(gj.produceSolutionString());

        gj = new GaussJordan(problem2);
        System.out.println(gj);
        gj.toReducedRowEchelonForm();
        System.out.println(gj);
        System.out.println(gj.produceSolutionString());

        System.out.println("Jacobi");
        Jacobi j = new Jacobi(problem1);
        j.solve();

        System.exit(0);
    }
}
