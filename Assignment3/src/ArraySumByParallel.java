import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *  calculate the sum of large dataset array element by parallely
 */
public class ArraySumByParallel {
    private long[] array;
    private long result = 0;

    /**
     * @param len just initialize the array
     *            the array contains the values
     *            1, 2, 3... len
     */
    ArraySumByParallel(int len) {
        array = new long[len];
        for (int index = 0; index < len; index++) {
            array[index] = (long) (index + 1);
        }
    }

    private class ParallelWorker implements Callable<Long> {
        int start;
        int end;
        long sum;
        // Note the start and end indexes for this worker
        ParallelWorker(int start, int end) {
            this.start = start;
            this.end = end;
            sum = 0;
        }


        /**
         *
         * @return each thread result
         * @throws Exception
         */
        @Override
        public Long call() throws Exception {
            for (int index=start; index<end; index++) {
                sum += array[index];
            }
            return  sum;
        }
    }


    /****
     * this will creates the callable list then returns it for executor service to execute it parallely and get the list of future objects
     */

    public List<Callable<Long>> scheduleTasksForParallelExecution(int numThreads){
        List<Callable<Long>> callables = new ArrayList<Callable<Long>>();
        int chunkSize; // chunks
        if (array.length > numThreads) {
            chunkSize = array.length / numThreads;
        }else {
            chunkSize = numThreads / array.length;
        }

        int rest = array.length % chunkSize;
        int chunks = array.length / chunkSize + (rest > 0 ? 1 : 0);

        for(int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++){
            Callable<Long> callable = new ParallelWorker(i * chunkSize, i * chunkSize + chunkSize);
            callables.add(callable);
        }

        if(rest > 0){
            // when we have a rest, less than chunk size, we copy the remaining elements into the last chunk
            Callable<Long> callable = new ParallelWorker((chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
            callables.add(callable);
        }

        return callables;
    }

    public List<Future<Long>> runParallel(ThreadPoolExecutor executor){
        List<Future<Long>> futures = new ArrayList<Future<Long>>(executor.getMaximumPoolSize());
        int chunkSize; // chunks
        if (array.length > executor.getMaximumPoolSize()) {
            chunkSize = array.length / executor.getMaximumPoolSize();
        }else {
            chunkSize = executor.getMaximumPoolSize() / array.length;
        }
        int rest = array.length % chunkSize;
        int chunks = array.length / chunkSize + (rest > 0 ? 1 : 0);
        int sum = 0;
        for(int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++){
            Callable<Long> callable = new ParallelWorker(i * chunkSize, i * chunkSize + chunkSize);
            futures.add(executor.submit(callable));
        }

        if(rest > 0){
            // when we have a rest, less than chunk size, we copy the remaining elements into the last chunk
            Callable<Long> callable = new ParallelWorker((chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
            futures.add(executor.submit(callable));
        }

        return futures;
    }

    public void calculateResult(List<Future<Long>> futures){
        for (int i = 0; i < futures.size(); i++) {
            Future<Long> future = futures.get(i);
            try {
                long result = future.get();
                this.result = this.result + result;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public long getResult() {
        return result;
    }

    public String Name(){
        return "ArraySumByParallel";
    }

    public long[] getArray() {
        return array;
    }
}