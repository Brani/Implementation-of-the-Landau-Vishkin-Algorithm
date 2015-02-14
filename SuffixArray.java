/*
 * Much of the code for the SuffixArray class was obtained from the 
 * textbook, Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne.
 * Certain portions are marked as added or modified by another author,
 * Robin Cohen.
 *
 * The SuffixArray class represents a suffix array of a string. The SuffixArray
 * class also includes an LCP array.
 */

import java.util.Arrays;

public class SuffixArray {
	
    private Suffix[] suffixes;
    
    private static final int MAXCAPACITY = 5000; //Added by: Robin Cohen Date: 11/9/2014
    private int[] lcp = new int [MAXCAPACITY];  //Added by: Robin Cohen Date: 11/9/2014

    //SuffixArray constructor
    public SuffixArray(String str) {
        int N = str.length();
        this.suffixes = new Suffix[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = new Suffix(str, i);
        Arrays.sort(suffixes);
        
        buildLCP(str); //Added by: Robin Cohen Date:  11/9/2014
    }

    private static class Suffix implements Comparable<Suffix> {
        private final String text;
        private final int index;

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }
        private int length() {
            return text.length() - index;
        }
        private char charAt(int i) {
            return text.charAt(index + i);
        }

        public int compareTo(Suffix that) {
            if (this == that) return 0;  // optimization
            int N = Math.min(this.length(), that.length());
            for (int i = 0; i < N; i++) {
                if (this.charAt(i) < that.charAt(i)) 
                	return -1;
                if (this.charAt(i) > that.charAt(i)) 
                	return +1;
            }
            return this.length() - that.length();
        }
        
        public String toString() {
            return text.substring(index);
        }
    }

    //returns the length of the input string.
    public int length() {
        return suffixes.length;
    }


    //returns the index into the original string of the ith smallest suffix
    public int index(int i) {
        if (i < 0 || i >= suffixes.length) throw new IndexOutOfBoundsException();
        return suffixes[i].index;
    }


    //returns the length of the longest common prefix of the ith
    //smallest suffix and the i-1st smallest suffix.
    public int lcp(int i) {
        if (i < 1 || i >= suffixes.length) throw new IndexOutOfBoundsException();
     
        lcp [i] = lcp(suffixes[i], suffixes[i-1]);
        return lcp[i]; //Modified by: Robin Cohen Date: 11/9/2014
    }

    //longest common prefix of s and t
    private static int lcp (Suffix s, Suffix t) {
        int N = Math.min(s.length(), t.length());
        for (int i = 0; i < N; i++) {
            if (s.charAt(i) != t.charAt(i)) return i;
        }
        return N;
    }
    
    //returns the ith smallest suffix as a string
    public String select(int i) {
        if (i < 0 || i >= suffixes.length) 
        	throw new IndexOutOfBoundsException();
         return suffixes[i].toString();
    }

    //returns the number of suffixes strictly less than the query string.
    public int rank(String query) {
        int lo = 0, hi = suffixes.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, suffixes[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }

    //compares query string to suffix
    private static int compare(String query, Suffix suffix) {
        int N = Math.min(query.length(), suffix.length());
        for (int i = 0; i < N; i++) {
            if (query.charAt(i) < suffix.charAt(i)) return -1;
            if (query.charAt(i) > suffix.charAt(i)) return +1;
        }
        return query.length() - suffix.length();
    }

    
    //prints a table with information about the SuffixArray
    public void print (String s){
        
    	System.out.println("  i ind lcp rnk select");
    	System.out.println("---------------------------");

    	for (int i = 0; i < s.length(); i++) {
    		int index = index(i);
    		String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
    		assert s.substring(index).equals(select(i));
    		int rank = rank(s.substring(index));
    		if (i == 0) 
    			System.out.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
    		
    		else {
    			int lcp = lcp(i);
    			System.out.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
    		}
    	}
    }

    //Author: Robin Cohen Date: 11/9/2014
    //builds the LCP array, lcp
    public void buildLCP (String s){
    	for (int i = 1; i < s.length(); i++) {
    			int lcp = lcp(i);
    			//System.out.println("lcp [" + i + "]: " + lcp);
    	}
    }
    
    //Author: Robin Cohen Date: 11/9/2014    
	//concatenates two strings using StringBuilder, separating both strings with a delimiter
    //***Take out and keep only in MatrixL class **
    public String concat (String pattern, String text){
    	StringBuilder sb = new StringBuilder();
    	sb= sb.append(pattern).append('#').append(text);
    	System.out.println (sb);
    	return sb.toString();
    }
    
    //Author: Robin Cohen Date: 11/9/2014    
    //calculates the longest common prefix of substrings of the concatenated text and pattern
    public int calculateLCP (String str, int query1, int query2, int textLen, int patternLen){
    	
    	int rank1, rank2, rmqResult=-1;
    	
    	//if either query is beyond the bounds of its respective string within str, return a 0
    	if (query1 >= textLen) return 0;
    	else if (query2 > textLen + patternLen) return 0;
    	
    	//compute the rank in the lcp array of the substrings starting at query1 and query2
    	rank1 = rank(str.substring(query1));
    	rank2 = rank(str.substring(query2));
    	
    	System.out.println ("The rank of query 1 in the LCP array is: " + rank1);
    	System.out.println( "The rank of query 2 in the LCP array is: " + rank2);
        
    	//the lower rank is not the lower bound in the range minimum query of the LCP array; 
    	//rather the lower rank+1 is the lower bound
        if (rank1<rank2)
        	rank1++;
        else
        	rank2++;
        
        //perform a range minimum query on the LCP array  	                
        RMQ array = new RMQ(lcp);
        rmqResult = array.query(rank1, rank2);
    	System.out.println("The RMQ is: " + lcp (rmqResult));
    	
    	return lcp(rmqResult);
    }

    //Author: Robin Cohen Date: 11/9/2014
    //tests the SuffixArray data type
    public static void main(String[] args) {


    }
}
