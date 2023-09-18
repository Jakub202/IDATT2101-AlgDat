public class Main {
    public static void main(String[] args) {
       /* System.out.println("Metode1: 13 * 2.5 = " + method1(13, 2.5));
        System.out.println("Metode2: 13 * 2.5 = " + method2(13, 2.5));
        System.out.println("Metode1: 14 * 10.1 = " + method1(14, 10.1));
        System.out.println("Metode2: 13 * 10.1 = " + method2(14, 10.1));*/

        int n = 1000;
        System.out.println("Time for n value of " + n);
        long startTime = System.nanoTime();
        method1(n, 2.52);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Method 1 took: " + duration + " nanoseconds");

        startTime = System.nanoTime();
        method2(n, 2.5);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("Method 2 took: " + duration + " nanoseconds");

        }

    public static double method1(int n, double x) {
        if (n == 1) {
            return x;
        } else {
            return x + method1(n - 1, x);
        }
    }

    public static double method2(int n, double x) {
        if (n == 1) {
            return x;
        } else if (n % 2 == 0) {
            return method2(n / 2, x + x);
        } else {
            return x + method2((n - 1) / 2, x + x);
        }
    }


}