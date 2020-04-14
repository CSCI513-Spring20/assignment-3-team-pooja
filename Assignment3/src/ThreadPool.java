import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool {

    public static void main(String[] args) {
        // command line argument: array_length
        Long len = (long) 10000000;
        if (args.length != 0){
            len = Long.parseLong(args[0]);
        }

        int numOfThreads = 10;

        System.out.println("\nUsing array of length: "+len+" and using "+numOfThreads+" threads");

        //Creates a thread pool that reuses a fixed number of threads (here it is 10) to execute any number of tasks.
        // If additional tasks are submitted/invoked when all threads are active, they will wait in the queue until a
        // thread is available.
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);

        if(len < 9) {
            System.out.println("Invalid array length, it should be greater than or equal to thread pool size which 10");
            System.exit(-1);
            return;
        }

        ArraySumByParallel asp = new ArraySumByParallel(len.intValue());
        long startTime = System.currentTimeMillis();

        // schedules tasks for parallel execution
        // example: 1000 elements in arr, and 10 threads, it creates 100 tasks are created and scheduled.
        List<Callable<Long>> callables = asp.scheduleTasksForParallelExecution(numOfThreads);

        //Execute all tasks and get reference to Future objects
        List<Future<Long>> resultList = null;
        try {
            // invokes all scheduled tasks in order. if no threads are available, tasks wait until a thread is available
            // and returns the Future objects which contains task results.
            resultList = executor.invokeAll(callables);
            asp.calculateResult(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();


        System.out.println("\n========Printing the results of parallel execution for "+asp.Name()+"======");
        System.out.println("result: "+asp.getResult());
        System.out.println("parallel summation took "+(endTime-startTime)+" milliseconds");
    }
}