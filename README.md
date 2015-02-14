# Implementation-of-the-Landau-Vishkin-Algorithm
Approximate String Matching in Near Linear Time


This implementation of the Landau-Vishkin 89 algorithm was programmed in Java using 
the Eclipse integrated development environment. Landau-Vishkin 89 is an approximate 
string-matching algorithm that provides a dynamic programming solution for finding all 
occurrences of a pattern-text alignment with at most k differences. This algorithm runs 
in near linear time, specifically O(nk), where n is the length of the text and k is the 
maximum edit distance. Landau-Vishkin 89 provides an alternative to the naïve dynamic 
programming solution for approximate string-matching, which runs in O(nm) time, where n 
is the length of the text and m is the length of the pattern[1]. 

This Landau-Vishkin implementation consists of three Java files: MatrixL.java, SuffixArray.java, 
and RMQ.java. The MatrixL class contains code for defining, initializing, and computing the 
L matrix. This matrix contains O(1) running time computations of jumps along diagonals of the 
naïve dynamic programming matrix, matrix D. The SuffixArray file contains code for creating the 
suffix array of a given string. A SuffixArray object also includes an auxiliary data structure 
called the longest common prefix (lcp) array. The lcp array contains the lengths of the longest 
common prefixes of all consecutive suffixes in a given suffix array. The MatrixL class uses the 
lcp array to compute jumps along the matrix D diagonals in constant time. The Landau-Vishkin 89 
algorithm employs a suffix tree as opposed to a suffix array. However, my implementation uses a 
suffix array, which presents a more space and time efficient alternative to the suffix tree [2]. 
The RMQ.java file contains code for performing a range minimum query, which will be used on the 
lcp array to compute the O(1) jumps.

The L matrix contains the data that is used to determine if the text and pattern align with at 
most k differences. Ld, e holds the largest row i such that Di, c = e and Di, c is on diagonal d. 
If Ld, e = m, then there is an occurrence of the pattern in the text. The L matrix is defined as 
an n-m+k+1 by k+1 integer matrix. The initialization of matrix L consists of the following three 
main steps:
	•	for 0 ≤ d  ≤ n
Ld, -1 =-1
	•	for -(k+1) ≤ d ≤ -1
Ld, |d|-1 = |d|-1
	•	for  -(k+1) ≤ d ≤ -1
Ld, |d|-2 = |d|-2
Note that |d| means the absolute value of d, as d can be negative.

The text and the pattern are concatenated such that the resultant string consists of the text followed 
by a delimiter and the pattern. The suffix array is initialized with this concatenated string. All 
computations assume that string indexes begin at 0. Computing the values of the L matrix consists of 
the following steps:
	for e=0 until k
		for d=-e to n
			row = max [Ld, e-1+1, Ld-1, e-1, Ld+1, e-1+1]
			row=min(row, m)
		Ld, e = m+ LCP (row+d, row+n+1)
If Ld, e = m and d+m ≤ n print “Occurrence ending at” d+m-1 + “index of str”
Note that the Landau-Vishkin 89 algorithm indexes a portion of the L matrix with negative integers, which 
is not possible for Java matrices. My implementation of the L matrix starts at index 0 and does not include 
negative indexes. However, my program includes statements that use negative logical indexes in line with 
pseudocode from the Landau-Vishkin 89 paper. A transformation function computes the corresponding physical 
index every time the logical index is referenced.

To run my program from the command line: 
Create a directory 
Copy MatrixL.java, SuffixArray.java, and RMQ.java into the directory 
	javac *.java
	java MatrixL
	All input is specified in the main program of MatrixL.java. The program outputs the computed L matrix for the given text, pattern, and maximum admissible errors. Additionally, when an occurrence is found, the program prints a message indicating the index at which the occurrence ends. If no occurrences are found at all, the program prints a message indicating such.


References:
[1] Landau, G.M., and U. Vishkin. "Fast Parallel and Serial Approximate String Matching." Journal of Algorithms 10.2 (1989): 157-69. ACM Digital Library. Web. 27 Nov. 2014.
[2] De Castro Miranda, Rodrigo, and Mauricion Ayala-Rincón. "A Modification of the Landau-Vishkin Algorithm Computing Longest Common Extensions via Suffix Arrays." Advances in Bioinformatics and Computational Biology 3594 (2005): 210-13. Web. 20 Aug. 2014.
