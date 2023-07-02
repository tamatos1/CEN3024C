import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyAssignmentV2Test {
    final int[] arrayOfNumbers = {1,3,5,3,9,5,7,2,8,3};
    final long sumKnown = 46;

    @org.junit.jupiter.api.Test
    void getFinalSumUsingMultiThread() {
        long sum = 0;

        ConcurrencyAssignmentV2 c = new ConcurrencyAssignmentV2();
        sum = c.getFinalSumUsingExternalInfo(arrayOfNumbers, 2, true);

        assertTrue(sum == sumKnown);

        System.out.println("Total for multi-thread is " + sum);

    }

    @org.junit.jupiter.api.Test
    void getFinalSumUsingSingleThread() {
        long sum = 0;

        ConcurrencyAssignmentV2 c = new ConcurrencyAssignmentV2();
        sum = c.getFinalSumUsingExternalInfo(arrayOfNumbers, 2, false);

        assertTrue(sum == sumKnown);

        System.out.println("Total single-thread is " + sum);

    }

    /*
    @org.junit.jupiter.api.Test
    void getFinalSumUsingMultiThread() {
        long sum = 0;
        int[] arrayOfNumbers = {1,3,5,3,9,5,7,2,8,3};
        ConcurrencyAssignmentV2 c = new ConcurrencyAssignmentV2();

        sum = c.getFinalSumUsingExternalInfo(arrayOfNumbers, 2, true);

        System.out.println(sum);

    }

     */
}