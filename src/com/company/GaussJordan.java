package com.company;

/**
 * Created by alina on 12.12.15.
 */
public class GaussJordan {

    private String inputString;
    private double[][] matrix;


    public GaussJordan(String s) {
        this.inputString = s.trim();
    }

    public GaussJordan(double[][] inputMatrix) {

        int row = inputMatrix.length;
        int col = inputMatrix[0].length;

        this.matrix = new double[row][col];

        this.matrix = inputMatrix;
    }

    /**
     * Parses the input string to populate other attributes of class.
     */
    public void parseInput() {

        String lines[] = this.inputString.split(";");
        int width = lines.length;
        int height = lines[0].split(",").length;
        this.matrix = new double[width][height];

        for (int i=0; i<width; i++) {
            String cells[] = lines[i].split(",");
            for(int j=0; j<height; j++) {
                this.matrix[i][j] = Double.parseDouble(cells[j]);

            }
        }
    }


    public String produceSolutionString() {
        String solution = "";

        if (!this.validSolution()) {
            solution = "There is no solution.\n";
        }
        else if (this.hasFreeVariables()) {
            solution = "There are an infinite amount of solutions:\n";
            solution += produceInifinteSolution();
        }
        else {
            solution = "There is a unique solution:\n";
            solution += produceUniqueSolution();
        }

        return solution;
    }

    /**
     * The less-of-a-fuckery that is producing a unique solution string.
     * @return
     */
    private String produceUniqueSolution() {
        String solution = "";
        int numCol = matrix[0].length;

        for (int row = 0, col = 0; col < (this.matrix[0].length - 1) && row < (this.matrix.length); row++, col++) {
            solution += "x" + (col+1) + " = " + matrix[row][numCol-1] + "\n";
        }

        return solution;
    }


    /**
     * The fuckery that is producing a non-unique solution string.
     * Assumes that the solution is not invalid.
     * @return
     */
    private String produceInifinteSolution() {

        String solution = "";
        int numRow = matrix.length;
        int numCol = matrix[0].length;

        int row = 0, col = 0;

        // traverse down diagonal add solution for each variable (either free or as equation)
        while ( col < (numCol - 1) && row < numRow) {

            // System.out.println(solution);

            // if not a pivot, variable #(col+1) is free
            if (Math.abs(matrix[row][col]) == 0.0) {
                solution += "x" + (col+1) + " is free.\n";

                // this column can no longer contain an element, so move to the left in row
                col++;
            }
            // if a pivot, produce what it is in terms of constant and known variable
            else {

                // variable = constant
                solution += "x" + (col+1) + " = " + matrix[row][numCol-1] + " ";

                // and in terms of other variables
                for (int col2 = numCol-2; col2 > col; col2--) {

                    if (Math.abs(matrix[row][col2]) != 0.0) {
                        solution += doubleToOppositeString(matrix[row][col2]) + "x"+ (col2+1);
                    }
                }
                // move down to next potential pivot element
                row++;
                col++;
                solution += "\n";
            }
        }

        // add final free variables when bottom of matrix has been reached but there are
        // still columns that were not covered. They have to be free variables.
        while (col < numCol-1) {
            solution += "x" + (col+1) + " is free.\n";
            col++;
        }
        return solution;
    }


    /**
     * Produces the string that you get when moving a coefficient to other side of equal sign.
     * Ignores zero coefficients and adds *-sign to non-zeros.
     *
     * 1 + d = 0 -> 1 = -d
     *
     * @param d
     * @return
     */
    private String doubleToOppositeString(double d) {
        if (d == 0.0 || d == -0.0)
            return "";
        else if (d < 0)
            return "+ " + Math.abs(d) + "*";
        else
            return "- " + d + "*";
    }


    /**
     * Returns true if this matrix has free variables.
     *
     * @return
     */
    private boolean hasFreeVariables() {


        int numRow = this.matrix.length;
        int numCol = this.matrix[0].length;

        // easy first check
        if ((numCol - numRow) > 1)
            return true;

        // if a potential pivot element is zero, the corresponding variable is free
        for (int row = 0, col = 0; col < (numCol - 1); row++, col++) {
            if (this.matrix[row][col] == 0.0)
                return true;
        }

        return false;
    }


    /**
     * Reduces the matrix to Reduced Row Echelon Form.
     */
    public void toReducedRowEchelonForm() {
        int lead = 0;
        int rowCount = this.matrix.length;
        int colCount = this.matrix[0].length;

        for (int r = 0; r < rowCount; r++) {

            // if past last column, stop
            if (lead >= colCount)
                break;

            // set index that will go through column looking non-zero, starting with row below previous iteration
            int i = r;

            // if element in row i and and column lead is not zero, look further down column
            while (matrix[i][lead] == 0.0 ) {
                i = i + 1;

                // if at last row, move one column to right, and start looking down from row r again
                if (i == rowCount) {
                    i = r;
                    lead = lead + 1;

                    // if bottom right element of array has been reach, stop
                    if (lead == colCount)
                        return;
                }
            }

            // swap row r (where started) and i (where first non-zero element is found) if necessary
            if (r != i) {
                double[] temp = this.matrix[r];
                this.matrix[r] = this.matrix[i];
                this.matrix[i] = temp;
            }


            matrix[r] = scaleRowWithPivot(lead, matrix[r]);

            // eliminate other entries in same column by subtracting matrix[r]*number from its respective row
            for (int j = 0; j < rowCount; j++) {

                // do not eliminate row r itself
                if (j != r && matrix[j][lead] != 0.0) {
                    matrix[j] = eliminateEntry(matrix[j][lead], matrix[r], matrix[j]);
                }

                System.out.println(this);
            }

            // increase column
            lead = lead + 1;
        }

    }

    /**
     * Scales the row by dividing each element by the value of element at pivotIndex.
     *
     * @param pivotIndex
     * @param row
     * @return
     */
    private double[] scaleRowWithPivot(int pivotIndex, double[] row) {

        double pivotValue = row[pivotIndex];

        for (int i = pivotIndex; i < row.length; i++) {
            row[i] = row[i]/pivotValue;
            System.out.println("[scaleRowWithPivot] Scaled value = " + row[i]);
        }

        return row;
    }


    /**
     * Takes two columns and a coefficient and subtracts one column multipled by coefficient from the other.
     *
     * @param coefficient
     * @param pivotColumn
     * @param elimColumn
     * @return new column
     */
    private double[] eliminateEntry(double coefficient, double[] pivotColumn, double[] elimColumn) {
        System.out.println("[eliminateEntry]: coefficient = " + coefficient);
        System.out.println("[eliminateEntry]: pivotColumn = " + pivotColumn);
        System.out.println("[eliminateEntry]: elimColumn = " + elimColumn);

        double[] result = new double[pivotColumn.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = elimColumn[i] - coefficient*pivotColumn[i];
            System.out.println("[eliminateEntry]: Entry = " + result[i]);
        }

        return result;
    }


    /**
     * Produces true if the given matrix (assumed to be in RRE form) has a valid solution.
     * @return
     */
    public boolean validSolution() {
        int i=0;

        while(i<this.matrix.length) {
            if (!validRow(matrix[i]))
                return false;
            i++;
        }
        return true;
    }


    /**
     * Returns true if the given row has a valid solution.
     *
     * @return
     */
    private boolean validRow(double[] row) {
        if (row[row.length-1] == 0.0) {
            System.out.println("Row ok!");
            return true;
        }

        for (int i=0; i<row.length-1; i++) {
            if (row[i] != 0.0 || row[i] != -0.0) {
                System.out.println("Row ok!");
                return true;
            }
        }

        return false;
    }


    @Override
    public String toString() {
        String s = "";
        for (int i=0; i<this.matrix.length; i++) {
            for (int j=0; j<this.matrix[i].length; j++) {
                s += this.matrix[i][j] + ", ";
            }
            s += "\n";
        }

        return s;
    }

    /**
     * Returns matrix.
     * @return
     */
    public double[][] getMatrix() {
        return this.matrix;
    }

    /**
     * Sets the matrix using given matrix.
     * @param givenMatrix
     */
    public void setMatrix(double[][] givenMatrix) {

        int row = givenMatrix.length;
        int col = givenMatrix[0].length;

        this.matrix = new double[row][col];

        this.matrix = givenMatrix;
    }
}
