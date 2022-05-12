/*
 * PROJECT III: GeneralMatrix.java
 *
 * This file contains a template for the class GeneralMatrix. Not all methods
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

import java.util.Arrays;

public class GeneralMatrix extends Matrix {
    /**
     * This instance variable stores the elements of the matrix.
     */
    private double[][] values;

    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the data array.
     *
     * @param firstDim   The first dimension of the array.
     * @param secondDim  The second dimension of the array.
     */
    public GeneralMatrix(int firstDim, int secondDim) {
        super(firstDim, secondDim);
        //create new empty array
        this.values = new double[firstDim][secondDim];
    }

    /**
     * Constructor function. This is a copy constructor; it should create a
     * copy of the second matrix.
     *
     * @param second  The matrix to create a copy of.
     */
    public GeneralMatrix(GeneralMatrix second) {
        super(second.iDim, second.jDim);
        //create new empty array
        this.values = new double[second.iDim][second.jDim];

        //fill array with values identical to those in the GeneralMatrix second
        for (int i = 0; i < second.iDim; i++) {
            for (int j = 0; j < second.jDim; j++) {
                this.setIJ(i, j, second.getIJ(i, j));
            }
        }
    }
    
    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        //if paramters are in bound return value
        if (i <= this.iDim && j <= this.jDim) {
            return values[i][j];
        }
        //else throw exception
        else{
            throw new MatrixException("There is no (" + i + ", " + j + ")'th entry to get in a matrix of dimension (" + this.iDim + ", " + this.jDim + ")");
        }
        
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the values array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        // if parameters are in bounds then set appropriate value
        if (i <= this.iDim && j <= this.jDim) {
            this.values[i][j] = value;
        }
        //else throw exception
        else{
            throw new MatrixException("Cannot set (" + i + ", " + j + ")'th entry in a matrix of dimension (" + this.iDim + ", " + this.jDim + ")");
        }
        
        
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {        
        double[] sign = new double[1];   //to account for sign change in LUDecomposition algorithm
        GeneralMatrix LUDecomp = this.LUdecomp(sign); // call function to perform LUDecomp (and find sign change)
        double determinant = sign[0]; // saves having to multiply by +/-1 later

        //If matrix A = matrix L * matrix U (lower and upper triangular respectively), then det(A) = det(L) * det(U)
        //Determinant of triangular matrix = product of diagonal. Diagonal of L is all 1's therefore det(A) = product of diagonal of U
        //Value calculated with for loop below, and returned
        for (int i = 0; i < LUDecomp.iDim; i++) {
            determinant *= LUDecomp.getIJ(i, i);
        }
       // if (Math.abs(determinant) < 1.0e-10) {
       //     determinant = 0.0;
       // }
        return determinant;
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second) {
        // if both matrices have same dimension, addition can be performed
        if (this.iDim == second.iDim && this.jDim == second.jDim) {
            //new matrix created, and each (i,j) entry set to the sum of the (i,j) entries of the 2 matrices to be added
            GeneralMatrix sum = new GeneralMatrix(this.iDim, this.jDim);
            for (int i = 0; i < this.iDim; i++) {
                for (int j = 0; j < this.jDim; j++) {
                    sum.setIJ(i, j, this.values[i][j] + second.getIJ(i, j)); 
                }
            }
            return sum;
        }
        //if dimensions different, throw exception
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
        //make new matrix and set it's values to those of the original matrix multiplied by the scalar value (ij'th entry => scalar * ij'th entry)
        GeneralMatrix product = new GeneralMatrix(this);
        for (int i = 0; i < this.iDim; i++) {
            for (int j = 0; j < this.jDim; j++) {
                product.setIJ(i, j, scalar * product.getIJ(i, j));
            }
        }
        return product;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        for (int i = 0; i < this.iDim; i++) {
            for (int j = 0; j < this.jDim; j++) {
                //Math.random creates random number between 0 and 1. Set every entry of matrix to one of these random numbers
                this.setIJ(i, j, Math.random());
            }
        }
    }

    /**
     * Returns the LU decomposition of this matrix; i.e. two matrices L and U
     * so that A = LU, where L is lower-diagonal and U is upper-diagonal.
     * 
     * On exit, decomp returns the two matrices in a single matrix by packing
     * both matrices as follows:
     *
     * [ u_11 u_12 u_13 u_14 ]
     * [ l_21 u_22 u_23 u_24 ]
     * [ l_31 l_32 u_33 u_34 ]
     * [ l_41 l_42 l_43 u_44 ]
     *
     * where u_ij are the elements of U and l_ij are the elements of l. When
     * calculating the determinant you will need to multiply by the value of
     * sign[0] calculated by the function.
     * 
     * If the matrix is singular, then the routine throws a MatrixException.
     * In this case the string from the exception's getMessage() will contain
     * "singular"
     *
     * This method is an adaptation of the one found in the book "Numerical
     * Recipies in C" (see online for more details).
     * 
     * @param sign  An array of length 1. On exit, the value contained in here
     *              will either be 1 or -1, which you can use to calculate the
     *              correct sign on the determinant.
     * @return      The LU decomposition of the matrix.
     */
    public GeneralMatrix LUdecomp(double[] sign) {
        // This method is complete. You should not even attempt to change it!!
        if (jDim != iDim)
            throw new MatrixException("Matrix is not square");
        if (sign.length != 1)
            throw new MatrixException("d should be of length 1");
        
        int           i, imax = -10, j, k; 
        double        big, dum, sum, temp;
        double[]      vv   = new double[jDim];
        GeneralMatrix a    = new GeneralMatrix(this);
        
        sign[0] = 1.0;
        
        for (i = 1; i <= jDim; i++) {
            big = 0.0;
            for (j = 1; j <= jDim; j++)
                if ((temp = Math.abs(a.values[i-1][j-1])) > big)
                    big = temp;
            if (big == 0.0)
                throw new MatrixException("Matrix is singular");
            vv[i-1] = 1.0/big;
        }
        
        for (j = 1; j <= jDim; j++) {
            for (i = 1; i < j; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < i; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
            }
            big = 0.0;
            for (i = j; i <= jDim; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < j; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
                if ((dum = vv[i-1]*Math.abs(sum)) >= big) {
                    big  = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (k = 1; k <= jDim; k++) {
                    dum = a.values[imax-1][k-1];
                    a.values[imax-1][k-1] = a.values[j-1][k-1];
                    a.values[j-1][k-1] = dum;
                }
                sign[0] = -sign[0];
                vv[imax-1] = vv[j-1];
            }
            if (a.values[j-1][j-1] == 0.0)
                a.values[j-1][j-1] = 1.0e-20;
            if (j != jDim) {
                dum = 1.0/a.values[j-1][j-1];
                for (i = j+1; i <= jDim; i++)
                    a.values[i-1][j-1] *= dum;
            }
        }
        
        return a;
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // Test your class implementation using this method.
        double[] sign = new double[1];
        Matrix test = new GeneralMatrix(2, 2);
        test.setIJ(0, 0, 1000);
        test.setIJ(0, 1, -2);
        test.setIJ(1, 0, -3);
        test.setIJ(1, 1, 40000000);
        Matrix test2 = new GeneralMatrix((GeneralMatrix)test);
        System.out.println(test.toString());
        System.out.println(test2.getIJ(0, 1));
        test2.setIJ(0, 1, 7);
        System.out.println(test2.getIJ(0, 1));
        System.out.println(test.toString());
        System.out.println(test2.toString());
        Matrix test3 = test.add(test2);
        Matrix test4 = test2.multiply(test3);
        Matrix test5 = test4.multiply(2);
        System.out.println(test3.toString());
        System.out.println(test4.toString());
        System.out.println(test5.toString());
        Matrix test6 = new GeneralMatrix(2, 3);
        Matrix test7 = new GeneralMatrix(3, 2);
        test6.random();
        test7.random();
        System.out.println(test6.toString());
        System.out.println(test7.toString());
        System.out.println(test2.toString());
        Matrix test8 = (GeneralMatrix) test7.multiply(test2);
        System.out.println(test8.toString());

        Matrix test9 = new GeneralMatrix(3, 4);
        test9.setIJ(0, 0, -10);
        test9.setIJ(0, 1, -2);
        test9.setIJ(0, 2, -3);
        test9.setIJ(0, 3, -400000000);
        test9.setIJ(1, 0, 1000);
        test9.setIJ(1, 1, 20);
        test9.setIJ(1, 2, 3.5);
        test9.setIJ(1, 3, 400000);
        test9.setIJ(2, 0, 1000);
        test9.setIJ(2, 1, -7);
        test9.setIJ(2, 2, 30.088);
        test9.setIJ(2, 3, -40000000);
        System.out.println(test9.toString());

        GeneralMatrix test10 = new GeneralMatrix(2, 2);
        test10.setIJ(0, 0, 3);
        test10.setIJ(0, 1, 15);
        test10.setIJ(1, 0, 2);
        test10.setIJ(1, 1, 10);
        System.out.println(test10.LUdecomp(sign));
        System.out.println(test10.determinant());
        System.out.println(test10.values);


        //will throw exceptions
        //System.out.println(test8.getIJ(4, 4));
        //test8.setIJ(4, 4, 60);
        //test2.multiply(test7);
        //test2.add(test7);

        GeneralMatrix test11 = new GeneralMatrix(100, 100);
        test11.random();
        //System.out.println(test11.toString()); prints 100x100 matrix
        System.out.println(test11.determinant());

        Matrix test12 = new TriMatrix(2);
        test12.setIJ(0, 0, 1000);
        System.out.println(test12.toString());
        System.out.println(test.add(test12).toString());

    }
}