import java.util.Random;
import java.util.function.Consumer;

public class Main {
    public static void quickSortWithHelp(int[] arr, int low, int high, int threshold) {
        if (high - low > threshold) {
            int pi = partition(arr, low, high);

            quickSortWithHelp(arr, low, pi - 1, threshold);
            quickSortWithHelp(arr, pi, high, threshold);
        } else {
            insertionSort(arr, low, high);
        }
    }

    public static void quickSortWithoutHelp(int[] arr, int low, int high, int threshold) {
        if (high - low > 2) {
            int pi = partition(arr, low, high);

            quickSortWithoutHelp(arr, low, pi - 1, threshold);
            quickSortWithoutHelp(arr, pi, high, threshold);
        } else {
            median3sort(arr, low, high);
        }
    }

    private static int median3sort(int[] t, int left, int right){
        int median = (left + right) / 2;
        if(t[left] > t[median]) swap(t,left,median);
        if(t[median] > t[right]){
            swap(t,median,right);
            if(t[left] > t[median]) swap(t,left,median);
        }
        return median;
    }

    public static int swap(int[] t, int i, int j){
        int temp = t[i];
        t[i] = t[j];
        t[j] = temp;
        return temp;
    }


    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[(low + high) / 2];
        int i = low, j = high;
        while (i <= j) {
            while (arr[i] < pivot) i++;
            while (arr[j] > pivot) j--;
            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void insertionSort(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public static boolean orderTest(int[] t) {
        for (int i = 0; i < t.length - 1; i++) {
            if (t[i] > t[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static int getArraySum(int[] t) {
        int sum = 0;
        for (int i = 0; i < t.length; i++) {
            sum += t[i];
        }
        return sum;
    }

    public static void testSort(Consumer<int[]> sortingFunction, int[] originalArray, int[] originalArrayWithDuplicates) {
        long start, end;
        int checkSumRandomArray = getArraySum(originalArray);
        int checkSumDuplicateArray = getArraySum(originalArrayWithDuplicates);

        // Test 1: Duplicates with random values
        int[] duplicateRandomArray = originalArrayWithDuplicates.clone();
        start = System.nanoTime();
        sortingFunction.accept(duplicateRandomArray);
        end = System.nanoTime();
        printResult("Random and 42 duplicates", checkSumDuplicateArray, duplicateRandomArray, start, end);

        // Test 2: Random values
        int[] randomArray = originalArray.clone();
        start = System.nanoTime();
        sortingFunction.accept(randomArray);
        end = System.nanoTime();
        printResult("Random", checkSumRandomArray, randomArray, start, end);

        // Test 3: Sorted values
        start = System.nanoTime();
        //random array is sorted from before
        sortingFunction.accept(randomArray);
        end = System.nanoTime();
        printResult("Sorted", checkSumRandomArray, randomArray, start, end);
    }


    public static void printResult(String testType, int checkSum, int[] arr, long start, long end) {
        if (checkSum != getArraySum(arr) || !orderTest(arr)) {
            System.out.println("The sorting went wrong!");
        }
        System.out.println(testType + ": " + (end - start) / 1000000);
    }

    public static int[] makeRandomArray(int n, int max) {
        int[] arr = new int[n];
        Random random = new Random();
        for(int i = 0; i < n; i++){
            arr[i] = random.nextInt(max);
        }
        return arr;
    }

    public static int[] makeRandomArrayWithDuplicates(int n, int max){
        int[] arr = new int[n];
        Random random = new Random();
        for(int i = 0; i < n; i++){
            if(i % 2 ==0){
                arr[i] = 42;
            }else{
                arr[i] = random.nextInt(max);
            }
        }
        return arr;
    }



    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int[] t1 = makeRandomArray(10000000, 10000000);
        int[] t2 = makeRandomArrayWithDuplicates(10000000, 10000000);
        int threshold = 100;

        System.out.println("Testing with array size 10 000 000 and max value 10 000 000");

        System.out.println("Testing quickSortWithHelp:");
        testSort(arr -> quickSortWithHelp(arr, 0, arr.length - 1, threshold), t1, t2);
        System.out.println("------------------------------------");

        System.out.println("Testing quickSortWithoutHelp:");
        testSort(arr -> quickSortWithoutHelp(arr, 0, arr.length - 1, threshold), t1, t2);

        t1 = makeRandomArray(20000000, 20000000);
        t2 = makeRandomArrayWithDuplicates(20000000, 20000000);

        System.out.println();
        System.out.println("Testing with array size 20 000 000 and max value 20 000 000");
        System.out.println("Testing quickSortWithHelp:");
        testSort(arr -> quickSortWithHelp(arr, 0, arr.length - 1, threshold), t1, t2);
        System.out.println("------------------------------------");

        System.out.println("Testing quickSortWithoutHelp:");
        testSort(arr -> quickSortWithoutHelp(arr, 0, arr.length - 1, threshold), t1, t2);

        t1 = makeRandomArray(100000000, 100000000);
        t2 = makeRandomArrayWithDuplicates(100000000, 100000000);

        System.out.println();
        System.out.println("Testing with array size 100 000 000 and max value 100 000 000");
        System.out.println("Testing quickSortWithHelp:");
        testSort(arr -> quickSortWithHelp(arr, 0, arr.length - 1, threshold), t1, t2);
        System.out.println("------------------------------------");

        System.out.println("Testing quickSortWithoutHelp:");
        testSort(arr -> quickSortWithoutHelp(arr, 0, arr.length - 1, threshold), t1, t2);




        //setup for the threshold test
        t1 = makeRandomArray(1000000, 1000000);
        long start;
        long end;
        int checkSum;


        int bestThreshold = 0;
        int bestThresholdSum = 0;
        long minTime = Long.MAX_VALUE;
        int[] originalArray = new int[t1.length];


        // Generate the original random array
        for (int j = 0; j < t1.length; j++) {
            originalArray[j] = random.nextInt(1000000);
        }



       /* //loop for finding approximate best threshold
        for(int i = 1; i < 10000; i+=10){
            bestThreshold = 0;
            minTime = Long.MAX_VALUE;

            System.arraycopy(originalArray, 0, t1, 0, originalArray.length);
            checkSum = getArraySum(t1);

            start = System.nanoTime();
            quickSortWithHelp(t1, 0, t1.length - 1, i);
            end = System.nanoTime();

            if (checkSum != getArraySum(t1) || !orderTest(t1)) {
                System.out.println("The sorting went wrong!");
            }

            long time = (end - start);
            if (time < minTime) {
                minTime = time;
                bestThreshold = i;
            }
            System.out.println("Threshold: " + i + " Time: " + time/1000000);
        }
*/


        //loop for finding the best threshold
        for(int i = 0; i < 10; i++) {
            bestThreshold = 0;
            minTime = Long.MAX_VALUE;

            // Generate the original random array
            for (int j = 0; j < t1.length; j++) {
                originalArray[j] = random.nextInt(1000000);
            }

            for (int j = 20; j < 260; j++) {
                // Copy the original random array to 't' before sorting
                System.arraycopy(originalArray, 0, t1, 0, originalArray.length);

                checkSum = getArraySum(t1);

                start = System.nanoTime();
                quickSortWithHelp(t1, 0, t1.length - 1, j + 1);  // Assuming quickSort accepts threshold as a parameter
                end = System.nanoTime();

                if (checkSum != getArraySum(t1) || !orderTest(t1)) {
                    System.out.println("The sorting went wrong!");
                }

                long time = (end - start);
                if (time < minTime) {
                    minTime = time;
                    bestThreshold = j + 1;
                }

            }
            System.out.println(bestThreshold);
            bestThresholdSum += bestThreshold;
        }
        System.out.println("Best threshold: " + bestThresholdSum/10);
    }
}