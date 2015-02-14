/* 
 * The following code for the RMQ class was obtained from the University of Tuebingen 
 * website. The code is from the Algorithms in Bioinformatics course page, specifically 
 * from the Advanced Methods in Sequence Analysis lecture section.
 *
 * The main program is authored by Robin Cohen.
 *
 * The RMQ class includes functionality to perform a range minimum query, which will be 
 * used on the LCP array.
 */

public class RMQ {
	
	int[]A;    //array to be filled
    int n;     //length of A
    int[][] M; //previously computed RMQs

    //RMQ constructor
    RMQ(int[] InputArray) {
    	A = InputArray; n = A.length;
		M = new int[n][n];
		fill_M();
    }
    
    //fills the M matrix with range minimum values
    private void fill_M() {
    	for (int i = 0; i < n; i++)
	    	for (int j = i; j < n; j++) {
	    			if (i==j) M[i][j] = i;
	    			else M[i][j] = A[j] < A[M[i][j-1]] ? j : M[i][j-1];
	    			M[j][i] = M[i][j];
	    	}
    }

    //returns the range minimum query over the interval [i, j]
    public int query(int i, int j) { 
    	return M[i][j]; 
    }
    
    //Author: Robin Cohen Date: 11/18/2014
    //tests the RMQ data type
    public static void main(String[] args) {

        int [] arr = {10, 2, 5, 4, 6, 7, 2, 4, 2, 90};
    	RMQ array = new RMQ(arr);
    	
    	System.out.println(array.query(5, 1));
    }
}
