import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.lang.System;
import java.util.ArrayList;
import java.io.PrintWriter;

/*
 * @author Jacob Knight & Connor Chung
 * @version 11/8/22
 */

public class Algorithms {
    /*
     * Main function for the Algorithms class. Reads the array
     * from phw_input.txt, runs each algorithm and prints the
     * results to the console, creates the varying size arrays
     * of random integers and runs each algorithm on them, creates
     * the matrix to hold results, and finally outputs to file
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        /* HW Step 5 - Scanning the array from the input file */
        File file = new File("phw_input.txt");
        Scanner scan = new Scanner(file);
        int[] array = new int[10];
        String line = scan.nextLine();
        for (int i = 0; i < array.length; i++) {
            if (line.contains(",")) {
                String num = line.substring(0, line.indexOf(","));
                int value = Integer.parseInt(num);
                array[i] = value;
                line = line.substring(line.indexOf(",") + 1, line.length());
            }
            else {
                String num = line.substring(0, line.length());
                int value = Integer.parseInt(num);
                array[i] = value;
            }
        }

        /* Determine the MSCS of the array
         * using each algorithm implementation and print to console
         */
        Algorithms a = new Algorithms();
        System.out.println("algorithm-1: " + a.algorithm1(array));
        System.out.println("algorithm-2: " + a.algorithm2(array));
        System.out.println("algorithm-3: " + a.maxSum(array, 0, 9));
        System.out.println("algorithm-4: " + a.algorithm4(array));

        /* HW Step 6 - Creating 19 integer arrays*/
        int[][] twoDArray = new int[19][];
        int dblIndex = 0;

        /* HW Step 7 -Create 19 arrays of size 10, 15, 20, ... , 100 */
        for (int i = 10; i <= 100; i += 5) {
            int[] newArray = new int[i];
            /* Generation of random numbers between -500 and 500 */
            for (int j = 0; j < i; j++) {
                Random rand = new Random();
                int randomOne = rand.nextInt(501);
                int randomTwo = rand.nextInt(501);
                int superRandom = randomOne - randomTwo;
                newArray[j] = superRandom;
            }
            twoDArray[dblIndex] = newArray;
            dblIndex++;
        }
        /* Create a matrix which will hold the time complexity times
         * and the t(n) values
         */
        long[][] timeComplexity = new long[19][18];

        /* Algorithm 1 */
        for (int i = 0; i < 19; i++) {
            long t1 = System.nanoTime();
            long total = 0;
            for (int j = 0; j < 5000; j++) {
                a.algorithm1(twoDArray[i]);
                long t2 = System.nanoTime();
                long elapsed = t2 - t1;
                total += elapsed;
            }

            long average = total / 5000;
            timeComplexity[i][0] = average;
        }

        /* Algorithm 2 */
        for (int i = 0; i < 19; i++) {
            long t1 = System.nanoTime();
            long total = 0;
            for (int j = 0; j < 5000; j++) {
                a.algorithm2(twoDArray[i]);
                long t2 = System.nanoTime();
                long elapsed = t2 - t1;
                total += elapsed;
            }

            long average = total / 5000;
            timeComplexity[i][1] = average;
        }

        /* Algorithm 3 */
        for (int i = 0; i < 19; i++) {
            long t1 = System.nanoTime();
            long total = 0;
            for (int j = 0; j < 5000; j++) {
                a.maxSum(twoDArray[i], 0, twoDArray[i].length - 1);
                long t2 = System.nanoTime();
                long elapsed = t2 - t1;
                total += elapsed;
            }

            long average = total / 5000;
            timeComplexity[i][2] = average;
        }

        /* Algorithm 4 */
        for (int i = 0; i < 19; i++) {
            long t1 = System.nanoTime();
            long total = 0;
            for (int j = 0; j < 5000; j++) {
                a.algorithm4(twoDArray[i]);
                long t2 = System.nanoTime();
                long elapsed = t2 - t1;
                total += elapsed;
            }

            long average = total / 5000;
            timeComplexity[i][3] = average;
        }

        /* HW Step 8 - Calculation of complexity using the theoretical
         * complexities determined using the tables
         * from the assignment instructions
         */
        int matIndex = 0;
        for (int i = 10; i<= 100; i += 5) {
            timeComplexity[matIndex][4] = (long) Math.ceil(Math.pow(i, 3) * 1000000);
            timeComplexity[matIndex][5] = (long) Math.ceil(Math.pow(i, 2) * 1000000);
            timeComplexity[matIndex][6] = (long) Math.ceil(i * (Math.log(i) / Math.log(2)) * 1000000);
            timeComplexity[matIndex][7] = (long) Math.ceil(i * 1000000);
            matIndex++;
        }

        /* Output data from matrix to ArrayList */
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < 19; i++) {
            String outputLine = "";
            for (int j = 0; j <= 6; j++ ) {
                outputLine += timeComplexity[i][j] + ",";
            }
            outputLine += timeComplexity[i][7];
            output.add(outputLine);
        }

        /* How to create and write file taken from StackOverflow:
         * https://stackoverflow.com/questions/2885173/how-do-i-
         * create-a-file-and-write-to-it-in-java
         *
         * HW Step 9 - Writing string from ArrayList to file taken from StackOverflow:
         * https://stackoverflow.com/questions/6548157/how-to-write-an-arraylist
         * -of-strings-into-a-text-file
         */
        try {
            PrintWriter writer = new PrintWriter("Jacob_Connor_phw_output.txt", "UTF-8");
            writer.println("algorithm-1,algorithm-2,algorithm-3,algorithm-4,T1(n),T2(n),T3(n),T4(n)");
            for (String s : output) {
                writer.println(s);
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    /* Algorithm Implementation */

    /* Algorithm 1 */
    public int algorithm1(int[] X) {
        int maxSoFar = 0;
        int P = 0;
        int Q = X.length;
        for (int L = P; L <= Q; L++) {
            for (int U = L; U <= Q; U++) {
                int sum = 0;
                for (int I = L; I < U; I++) {
                    sum = sum + X[I]; /* sum now contains the sum of X[L…U] */
                }
                maxSoFar = max(maxSoFar, sum);
            }
        }
        return maxSoFar;
    }

    /* Algorithm 2 */
    public int algorithm2(int[] X) {
        int maxSoFar = 0;
        int P = 0;
        int Q = X.length;
        for (int L = P; L <= Q; L++) {
            int sum = 0;
            for (int U = L; U < Q; U++) {
                sum = sum + X[U]; /* sum now contains the sum of X[L…U] */
                maxSoFar = max(maxSoFar, sum);
            }
        }
        return maxSoFar;
    }

    /* Algorithm 3 */
    public int maxSum(int[] X, int L, int U) {
        if (L > U) {
            return 0; /* zero-element vector */
        }
        if (L == U) {
            return max(0, X[L]); /* one-element vector */
        }

        int M = (L + U) / 2; /* A is X[L…M], B is X[M+1…U] */

        /* Find max crossing to left */
        int sum = 0;
        int maxToLeft = 0;
        for (int I = M; I >= L; I--) {
            sum = sum + X[I];
            maxToLeft = max(maxToLeft, sum);
        }

        /* Find max crossing to right */
        sum = 0;
        int maxToRight = 0;
        for (int I = M + 1; I <= U; I++) {
            sum = sum + X[I];
            maxToRight = max(maxToRight, sum);
        }
        int maxCrossing = maxToLeft + maxToRight;

        int maxInA = maxSum(X, L, M);
        int maxInB = maxSum(X, M + 1, U);
        return max(maxCrossing, maxInA, maxInB);
    }

    /* Algorithm 4 */
    public int algorithm4(int[] X) {
        int P = 0;
        int Q = X.length;
        int maxSoFar = 0;
        int maxEndingHere = 0;
        for (int I = P; I < Q; I++) {
            maxEndingHere = max(0, maxEndingHere + X[I]);
            maxSoFar = max(maxSoFar, maxEndingHere);
        }
        return maxSoFar;
    }

    /* Added Functions */

    /* Return the max of two integers.
     * Worst case cost of 4.
     */
    public int max(int x, int y) {
        if (x >= y) {
            return x;
        }
        else {
            return y;
        }
    }

    /* Return the max of three integers
     * Worst case cost of 13.
     */
    public int max(int x, int y, int z) {
        if (x >= y && x >= z) {
            return x;
        }
        else if (y >= x && y >= z) {
            return y;
        }
        else {
            return z;
        }
    }

}
