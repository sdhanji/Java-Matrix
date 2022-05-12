import javax.swing.text.html.HTMLDocument.RunElement;
import java.util.Random;

/*
 * PROJECT III: TriMatrix.java
 *
 * This file contains a template for the class TriMatrix. Not all methods are
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 * 
 * Tasks:
 *
 * 1) Complete this class with the indicated methods and instance variables.
 *
 * 2) Fill in the following fields:
 *
 * NAME: Shiv Dhanji
 * UNIVERSITY ID: 2109288
 * DEPARTMENT: Mathematics
 */

public class TriMatrix extends Matrix {
    /**
     * An array holding the diagonal elements of the matrix.
     */
    private double[] diagonal;

    /**
     * An array holding the upper-diagonal elements of the matrix.
     */
    private double[] upperDiagonal;

    /**
     * An array holding the lower-diagonal elements of the matrix.
     */
    private double[] lowerDiagonal;
    
    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the values array.
     *
     * @param dimension  The dimension of the array.
     */
    public TriMatrix(int dimension) {
        super(dimension, dimension);
        //create 3 new empty arrays of appropriate lengths (upper&lower diagonals 1 less than main diagonal)
        this.diagonal = new double[dimension];
        this.upperDiagonal = new double[dimension - 1];
        this.lowerDiagonal = new double[dimension - 1];
    }
    
    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        if (i == j) { // ij'th entry lies on diagonal
            return diagonal[i];
        }
        else if (i - j == 1) { //ij'th entry lies on the lower diagonal
            return lowerDiagonal[j];
        }
        else if (j - i == 1) { //ij'th entry lies on the upper diagonal
            return upperDiagonal[i];
        }
        else{ //ij'th entry not on any of the 3 diagonals (so entry = 0)
            return 0;
        }
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the data array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        // You need to fill in this method.
        if (i == j) { // ij'th entry lies on diagonal
            this.diagonal[i] = value;
        }
        else if (i - j == 1) { //ij'th entry lies on the lower diagonal
            this.lowerDiagonal[j] = value;
        }
        else if (j - i == 1) { //ij'th entry lies on the upper diagonal
            this.upperDiagonal[i] = value;
        }
        else{ //ij'th entry not on any of the 3 diagonals (so entry = 0)
            throw new MatrixException("If this value is altered the matrix will no longer be a trimatrix. Can only alter an entry on one of the 3 diagonals");
        }
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        TriMatrix LUDecomp = this.LUdecomp();
        double determinant = 1;
        for (int i = 0; i < LUDecomp.diagonal.length; i++) {
            determinant *= LUDecomp.diagonal[i];
        }
        return determinant;
    }
    
    /**
     * Returns the LU decomposition of this matrix. See the formulation for a
     * more detailed description.
     * 
     * @return The LU decomposition of this matrix.
     */
    public TriMatrix LUdecomp() {
        // You need to fill in this method.
        TriMatrix LU = new TriMatrix(this.iDim);
        double[] a = new double[this.iDim];
        double[] d = new double[this.iDim];
        double[] b = new double[this.iDim];
        double[] bb = new double[this.iDim];
        double[] dd = new double[this.iDim];
        for (int i = 0; i < b.length; i++) {
            d[i] = this.diagonal[i];
            if (i == 0) {
                b[i] = 0;
            }
            else{
                b[i] = this.lowerDiagonal[i - 1];
            }
            if (i == b.length - 1) {
                a[i] = 0;
            }
            else{
                a[i] = this.upperDiagonal[i];
            }
        }
        bb[0] = 0;
        dd[0] = d[0];
        for (int i = 1; i < dd.length; i++) {
            bb[i] = b[i] / dd[i - 1];
            dd[i] = d[i] - (bb[i] * a[i - 1]);
        }
        for (int i = 0; i < LU.iDim; i++) {
            if (i < LU.iDim - 1) {
                LU.setIJ(i, i + 1, a[i]);
                LU.setIJ(i + 1, i, bb[i + 1]);
            }
            LU.setIJ(i, i, dd[i]);
        }
        return LU;
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return        The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second){
        if (this.iDim == second.iDim && this.jDim == second.iDim) {
            if (second.getClass().getName() == "TriMatrix") {
                TriMatrix sum = new TriMatrix(this.iDim);
                for (int i = 0; i < diagonal.length; i++) {
                    sum.diagonal[i] = this.diagonal[i] + second.getIJ(i, i);
                    if (i < diagonal.length - 1) {
                        sum.upperDiagonal[i] = this.upperDiagonal[i] + second.getIJ(i, i + 1);
                        sum.lowerDiagonal[i] = this.lowerDiagonal[i] + second.getIJ(i + 1, i);
                    }
                }
                return sum;
            }
            else{
                GeneralMatrix sum = (GeneralMatrix)second.add(this);
                return sum;
            }
            
        }
        else{
            throw new MatrixException("Cannot add 2 matrices of different dimensions");
        }
    }
    
    /**
     * Multiply the matrix by another matrix A. This is a _left_ product,
     * i.e. if this matrix is called B then it calculates the product BA.
     *
     * @param A  The Matrix to multiply by.
     * @return   The product of this matrix with the matrix A.
     */
    public Matrix multiply(Matrix A) {
        // You need to fill in this method.
        // can only multiply if number of columns of left matrix = number of rows of right matrix 
        if (this.jDim == A.iDim) {
            //resultant matrix has same number of rows as left matrix and same number of columns of right matrix
            GeneralMatrix product = new GeneralMatrix(this.iDim, A.jDim);
            double sum = 0;
            //use 2 for loops to calculate every entry of product)
            for (int i = 0; i < product.iDim; i++) {
                for (int j = 0; j < product.jDim; j++) {
                    //use for loop to calculate dot product of i'th row of left matrix and j'th column of right matrix (= ij'th entry of product)
                    sum = 0;
                    for (int n = 0; n < this.jDim; n++) {
                        sum += this.getIJ(i, n) * A.getIJ(n, j);
                    }
                    product.setIJ(i, j, sum);
                }
            }
            return product;
        }
        //if dimensions not as specified above, can't multiply so throw exception
        else{
            throw new MatrixException("Cannot multiply 2 matrices of such dimensions. Require J dimension of left matrix = I dimension of right matrix");
        }
    }
    
    /**
     * Multiply the matrix by a scalar.
     *
     * @param scalar  The scalar to multiply the matrix by.
     * @return        The product of this matrix with the scalar.
     */
    public Matrix multiply(double scalar) {
        // You need to fill in this method.
        TriMatrix product = new TriMatrix(this.iDim);
        for (int i = 0; i < product.diagonal.length; i++) {
            product.diagonal[i] = this.getIJ(i, i) * scalar;
            if (i < product.diagonal.length - 1) {
                product.lowerDiagonal[i] = this.getIJ(i + 1, i) * scalar;
                product.upperDiagonal[i] = this.getIJ(i, i + 1) * scalar;
            }
        }
        return product;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     * @return 
     */
    public void random() {
        // You need to fill in this method.
        for (int i = 0; i < this.diagonal.length; i++) {
            this.diagonal[i] = Math.random();
            if (i < this.diagonal.length - 1) {
                this.lowerDiagonal[i] = Math.random();
                this.upperDiagonal[i] = Math.random();
            }
        }
    }
    
    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // Test your class implementation using this method.
        TriMatrix test = new TriMatrix(4);
        test.setIJ(0, 0, 1);
        test.setIJ(0, 1, 2);
        test.setIJ(1, 0, 3);
        test.setIJ(1, 1, 4);
        test.setIJ(1, 2, 5);
        test.setIJ(2, 1, 6);
        test.setIJ(2, 2, 7);
        test.setIJ(2, 3, 8);
        test.setIJ(3, 2, 9);
        test.setIJ(3, 3, 10);
        System.out.println(test.LUdecomp().toString());
        System.out.println(test.toString());
        System.out.println(test.getIJ(0, 1));
        test.setIJ(0, 1, 7);
        System.out.println(test.getIJ(0, 1));
        System.out.println(test.toString());
        Matrix test2 = (TriMatrix) test.add(test);
        System.out.println(test2.toString());
        Matrix test3 = new GeneralMatrix(4, 4);
        test3.setIJ(2, 2, 6);
        Matrix test4 = test.add(test3);
        System.out.println(test4.toString());
        TriMatrix test5 = new TriMatrix(2);
        test5.setIJ(0, 0, 3);
        test5.setIJ(0, 1, 15);
        test5.setIJ(1, 0, 2);
        test5.setIJ(1, 1, 10);
        System.out.println(test5.LUdecomp());
        System.out.println(test5.determinant());
    }
}