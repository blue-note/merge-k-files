**************************************************** 

TO COMPILE: 

javac merge.java

TO RUN: 

java merge <full-path-to-input-directory> <name-of-output-file>

The output file will be created in the program's working directory. 

**************************************************** 

TIME COMPLEXITY: 

The solution uses a priority queue to process each line in all of the files. Insertion into the queue takes O(log(F)) time, where F is the number of files in the directory. Each line in the input must be inserted, so if N is the total number of lines across all files the overall runtime is O(N * log(F)).

SPACE COMPLEXITY: 

At any given time there are F lines residing in the priority queue in memory, so the space complexity is O(F).