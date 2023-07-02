import java.util.Arrays;
import java.util.Random;

public class ConcurrencyAssignmentV2 {
    private static final int arraySize = 2000000;
    private static final int threadCount = 2;

    private long sumOfArray = 0;

    public long getFinalSumUsingExternalInfo(int[] arrayOfNumbers, int countOfThreads, boolean useMultiThreadVersion) {
        if (useMultiThreadVersion) {
            return  getSumOfArrayMultiThread(arrayOfNumbers, countOfThreads);
        } else {
            return getSumOfArraySingleThread(arrayOfNumbers);
        }
    }

    private static int[] arrayOfRandomNumbers = getArrayWithRandomNumbers(arraySize);


    public static void main(String[] args) {
        long startTime = 0;
        long stopTime = 0;
        //int[] arrayOfRandomNumbers = getArrayWithRandomNumbers(arraySize);



        startTime = System.nanoTime();
        System.out.println("Single thread sum: " + getSumOfArraySingleThread(arrayOfRandomNumbers));
        stopTime = System.nanoTime();
        System.out.println("It took " + (stopTime - startTime) + " nanoseconds");

        startTime = System.nanoTime();
        System.out.println("Multi thread sum: " + getSumOfArrayMultiThread(arrayOfRandomNumbers, threadCount));
        stopTime = System.nanoTime();
        System.out.println("It took " + (stopTime - startTime) + " nanoseconds");
    }





    private static int[] getArrayWithRandomNumbers(int arraySize) {
        int[] arrayOfRandomNumbers = new int[arraySize];
        Random random = new Random();

        for(int i = 0; i < arraySize; i++) {
            arrayOfRandomNumbers[i] = random.nextInt(10);
        }

        return arrayOfRandomNumbers;
    }

    private static long getSumOfArraySingleThread(int[] arrayOfNumbers) {
        long sum = 0;

        for(int i = 0; i < arrayOfNumbers.length; i++) {
            sum = sum + arrayOfNumbers[i];
        }
        return sum;
    }

    private static long getSumOfArrayMultiThread(int[] arrayofNumbers, int countOfThreads) {
        ThreadForAdding[] threads = new ThreadForAdding[countOfThreads];
        long sum = 0;
        int blockSize = arrayofNumbers.length / countOfThreads;
        int startingPosition = 0;
        int stopingPosition = 0;



        for(int i = 0; i < countOfThreads; i++) {
            startingPosition = i * blockSize;
            stopingPosition = startingPosition + blockSize -1;

            threads[i] = new ThreadForAdding(arrayofNumbers, startingPosition, stopingPosition);
            threads[i].start();
        }

        try {
            for(ThreadForAdding t : threads) {
                t.join();
                sum += t.getSum();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return sum;
    }

    private static class ThreadForAdding extends Thread {
        private int[] arrayOfRandomNumbers;
        private int startPosition;
        private int stopPosition;
        private long sum = 0;


        ThreadForAdding(int[] arrayOfRandomNumbers, int startPosition, int stopPosition) {
            this.arrayOfRandomNumbers = arrayOfRandomNumbers;
            this.startPosition = startPosition;
            this.stopPosition = stopPosition;
        }



        public void run() {

            if(arrayOfRandomNumbers != null) {
                for(int i = startPosition; i <= stopPosition; i++) {
                    this.sum += arrayOfRandomNumbers[i];
                }
            }
        }

        public long getSum() {
            return sum;
        }
    }
}


