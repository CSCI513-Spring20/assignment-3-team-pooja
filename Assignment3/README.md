* Use the below methods to run the code:

1) >javac ArraySumByParallel.java
2) >java ArraySumByParallel 10000000
3) >java -jar assignment-3-team-pooja.jar 10000000

```$xslt
assignment-3-team-pooja\Assignment3> java -jar assignment-3-team-pooja.jar 10000000

Using array of length: 10000000 and using 10 threads

========Printing the results of parallel execution for ArraySumByParallel======
result: 50000005000000
parallel summation took 27 milliseconds
```
we are passing 1M as array length, if want to pass 100M then 2nd command should be like "java -Xmx1000m ArraySumByParallel 100000000"
* To run this code in any IDE just import "ArraySumByParallel.java" file into your project workspace and run by giving program argument.
* Also, run using jar file from console itself.
* Increase heap memory when faced with out of memory errors.

Framework used: "Java executor framework"

Note:
* Input array length should be at least greater than or equal to thread pool size which is 10, because
  to run on multiple thread parallel.
  We are dividing array into small small chunks of size (array.length/numOfThreads or numOfThreads/array.length which ever is greater) and assign tasks to threads
  e.g. input array size is 1000; num of threads =10 then no of tasks = 100
  Each thread works parallel and calculate sum and then finally we are merging each thread result 
  into final sum like divide and conquer.

* review ArraySumByParallel for scheduling method.
