import java.util.Random;

public class ConcurrencyAssignment {

    private static int arraySize = 20000;
    private static long sum = 0;


    private static int[] numbersArray = new int[arraySize];
    private static int counterT1 = 0;
    private static int counterT2 = numbersArray.length-1;

    public static void main(String[] args){
        long startTime = 0;
        long stopTime = 0;
        System.out.println("hi");


        populateArrayWithRandomNumbers();

        startTime = System.nanoTime();
        System.out.println(getSumOfArraySingleThread());
        stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);

        startTime = 0;
        stopTime = 0;

        startTime = System.nanoTime();
        System.out.println(getSumOfArrayMultiThread());
        stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
    }

    private static long getSumOfArraySingleThread() {
        sum = 0;

        for(int i = 0; i < arraySize; i++) {
            sum = sum + numbersArray[i];
        }
        return sum;
    }

    private static long getSumOfArrayMultiThread() {
        sum = 0;

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (counterT1 < counterT2) {
                    sum = sum + numbersArray[counterT1];
                    counterT1 = counterT1 + 1;
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (counterT2 >= counterT1) {
                    sum = sum + numbersArray[counterT1];
                    counterT2 = counterT2 - 1;
                }
            }
        });

        thread1.start();
        thread2.start();




        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return sum;
    }

    private static void populateArrayWithRandomNumbers() {
        Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            numbersArray[i] = random.nextInt(10);
            //System.out.println(numbersArray[i]);
        }
    }
}
