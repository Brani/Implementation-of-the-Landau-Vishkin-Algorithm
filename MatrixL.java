/*
 * The MatrixL class represents the L matrix described in the 1989 paper,
 * "Fast Parallel and Serial Approximate String Matching" by G.M. Landau
 * and U. Vishkin.
 *
 * Author: Robin Cohen Date: 11/18/2014
 */

import java.util.Arrays;
import java.lang.Math;

public class MatrixL {

	//member variables
	private int n, m, k, matrix[][];
	private SuffixArray suffixArray;
	String str;
    
	//MatrixL constructor
    public MatrixL (int maxErrors, String text, String pattern){

    	k=maxErrors;
    	n=text.length();
    	m=pattern.length();
    	    	
		str = concat(text, pattern);
		suffixArray = new SuffixArray(str); //the LCP array is built in this
											//SuffixArray constructor
    	matrix = new int [n-m+k+1][k+1]; 

    }
    
    //computes the L matrix by initializing matrix and filling in its values
    //according to the Landau-Vishkin 1989 algorithm
    public void computeMatrixL(){
    	
    	initStepTwo();
    	initStepThree();
    	fillMatrix();
    	printMatrix();
    
    }
    
    //concatenates two strings, separating both strings with a delimiter
    public String concat (String string1, String string2){
  
    	return string1 + '#' + string2;

    }
    
    //initializes matrix according to the second initialization step
    //specified in the 1989 Landau-Vishkin paper
    public void initStepTwo(){
    	
    	int d, i, j=k;
    	
    	for (d=-(j), i=1; d < -1 &&  i>=1; d++, i++)
        	matrix[transform(d, j)][j-i]=j-i;
    }
    
    //initializes matrix according to the third initialization step
    //specified in the 1989 Landau-Vishkin paper
    public void initStepThree (){ 
    	
    	int d, i, j=k;
    	
    	for (d=-(j), i=1; d < 0 &&  i>=1; d++, i++)
        	matrix[transform(d, j)][j-i+1]=j-i+1;
        
    }
    
    //performs a transformation on a logical index and returns the computed 
    //physical index
    public  int transform (int x, int k){
    	
    	return x+k;
    
    }

    //fills in matrix according to the algorithm described in the 1989 Landau-Vishkin
    //paper
    public void fillMatrix (){
    	
        int e, d, row, upperLeft, immediateLeft, lowerLeft;
        boolean isBottomRow=false;
		upperLeft=immediateLeft=lowerLeft=-1;
        
        for (e=0; e<=k; e++){ //iterate over each column

        	for (d= (e*-1)+1; d <= n-m; d++){ //iterate over select rows
     		   	
     		   System.out.println("\nlogical d: " + d + " physical d: " + transform(d,k) + " e: " + e);
      			
     		   //all elements in the first physical column have their left values initialized to -1,
     		   //as specified in the first initialization step of the Landau-Vishkin paper
     		   	if (e==0){ 
     				upperLeft=immediateLeft=lowerLeft=-1;
     				System.out.println("Immediate left (initialized): " + immediateLeft);
     				System.out.println("Lower left (initialized): " + lowerLeft);
     				System.out.println("Upper left (initialized): " + upperLeft);
     				
     			}
     			
     		   	//all elements not in the first physical column must actually look for their left values
     			else{ 

     				immediateLeft=matrix[transform(d, k)][e-1];
     				System.out.println("Immediate left: " + matrix[transform(d, k)][e-1]);
     				
 					upperLeft=matrix[transform(d-1, k)][e-1];
     				System.out.println("Upper left: " + matrix[transform(d-1, k)][e-1]);
     				
     				
     				if(transform(d, k)<transform(n-m, k)){//if the element is NOT in the bottom row

     					lowerLeft=matrix[transform(d+1, k)][e-1]; //its lower left value can be read
         				System.out.println("Lower left: " + matrix[transform(d+1, k)][e-1]);
     				}
     				
     				//if the element is in the bottom row, its lower left element obviously cannot be read     				
     				else{
     					
     					System.out.print ("Cannot read from spot to bottom left at: " + transform(d+1, k) + " ");
     					System.out.println (e-1);
     					isBottomRow=true;
     				}
     				
     			}//end else
     			
     			
     		   	//if the element is on the bottom row, then row is computed as the max of only two left values
     			if (isBottomRow=true){
     				row = Math.max(upperLeft+1, immediateLeft+1);
     				System.out.println("max: " + row);
     				isBottomRow=false;
     			}
     			
     			//otherwise, row is computed by looking at three left values
     			else{
    				 row =  Math.max(Math.max(immediateLeft+1,lowerLeft),upperLeft+1);
    				 System.out.println("max: " + row);
     			}
     			
     			row = Math.min(row, m);
     			
     			System.out.println("m: " + m);
     			System.out.println("row: "+row);
     			
     			int lcp= suffixArray.calculateLCP (str, row+d, row+n+1, n, m);
     					//row+n+1 is the index in str that corresponds to the index of row in pattern 
     			System.out.println("lcp: " + lcp);
     			
     			matrix[transform(d, k)][e]= row + lcp;
     			System.out.println("matrix element: " + matrix[transform(d, k)][e]);
     			
         	}//end for
         }//end for
    }//end fillMatrixL

    //prints matrix
    public void printMatrix() {
    	
    	for (int[] row : matrix)
	        System.out.println(Arrays.toString(row));       
	
    }
   
    //tests the MatrixL data type
    public static void main(String[] args)  {
    	
    	int maxErrors=2;
    	String text = "baananaaan";
    	String pattern = "aaa";
    	    	
    	MatrixL matrixL = new MatrixL (maxErrors, text, pattern);
    	matrixL.computeMatrixL(); //should this be called from the constructor?

    }
}
