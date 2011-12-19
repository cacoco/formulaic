# formulaic

## Usage

Given an n-dimensional spreadsheet defined with width m, height n reduces the spreadsheet values.
Line 1 should be two integers that defines the width and height. n*m lines containing a cell expression
which is the value of the corresponding cell.

Input:

6 6  
A2  
B1++  
2  
F3    
4 5 3 *  
100 20 / 3 +  
1002  
0 1 1 3 +  
2  
A3 D3 E3 +  
D4 D1++ A6-- *  
C5 3 *  
63  
201 9 15 * /  
B1-- 7 -  
3 E4 5 *  
E1 F2 / 4 +  
55  
6 3 17 +  
D1++ A2 + E3 -  
22 7 /  
F1 67 3 * 3 +  
45 B6 D2 +  
E3++ 5 -  
9.85 E5 +  
1 4 7 -  
A5 8 *  
60 60 60 60 *  
40291 100 -  
62 34 23 +  
186000 3 9 *  
29301 D2 /  
E2 F2 +  
3  
A4 D2-- E2 C5 B2++ +  
5 10 15 20 25 +  

Yields:

6 6  
1003.00000  
1003.00000  
2.00000  
43.27454  
60.00000  
8.00000  
1002.00000  
5.00000  
2.00000  
485.14285  
949196544.00000  
2275.79346  
63.00000  
1.48889  
994.00000  
194400000.00000  
758.59778  
55.00000  
26.00000  
550.00000  
3.14286  
5022204.00000  
2870.79346  
476.00000  
40200.85156  
-10.00000  
480.00000  
12960000.00000  
40191.00000  
119.00000  
5022000.00000  
53.27454  
43.27454  
3.00000  
1346.87231  
75.00000  
time: [12ms]