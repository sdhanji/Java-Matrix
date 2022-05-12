/*
 * PROJECT III: Project3.java
 *
 * This file contains a template for the class Project3. None of methods are
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class, as 
 * well as GeneralMatrix and TriMatrix.
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

public class Project3 {
    /**
     * Calculates the variance of the distribution defined by the determinant
     * of a random matrix. See the formulation for a detailed description.
     *
     * @param matrix      The matrix object that will be filled with random
     *                    samples.
     * @param nSamp       The number of samples to generate when calculating 
     *                    the variance. 
     * @return            The variance of the distribution.
     */
    public static double matVariance(Matrix matrix, int nSamp) {
        // You need to fill in this method.
        double variance = 0;
        double detSquaredSum = 0;
        double detSum = 0;
        double[] determinants = new double[nSamp];
        for (int i = 0; i < determinants.length; i++) {
            matrix.random();
            determinants[i] = matrix.determinant();
            detSquaredSum += Math.pow(determinants[i], 2);
            detSum += determinants[i];
        }
        detSquaredSum /= nSamp;
        detSum = Math.pow(detSum / nSamp, 2);
        variance = detSquaredSum - detSum;
        return variance;
    }
    
    /**
     * This function should calculate the variances of matrices for matrices
     * of size 2 <= n <= 50 and print the results to the output. See the 
     * formulation for more detail.
     */
    public static void main(String[] args) {
        // You need to fill in this method.
        //long start = System.currentTimeMillis();
        for (int i = 2; i < 51; i++) {
            GeneralMatrix GM = new GeneralMatrix(i, i);
            TriMatrix TM = new TriMatrix(i);
            //System.out.println(String.valueOf(i) + "  " +  String.valueOf(String.format("%.15f",matVariance(GM, 20000))) + "  " + String.valueOf(String.format("%.15f",matVariance(TM, 200000))));
            System.out.println(String.valueOf(i) + "  " +  String.valueOf(matVariance(GM, 20000)) + "  " + String.valueOf(matVariance(TM, 200000)));

        }
        //long end = System.currentTimeMillis();
        //System.out.println("Task takes " + ((end - start) / 1000) + "s");
    }
}