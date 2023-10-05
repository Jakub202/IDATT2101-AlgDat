import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HashArray2 {

    private Integer[] array;
    private long collisions = 0;

    public HashArray2(int length){
        array = new Integer[length];
    }
    //10000019  12500003;

    public int getLenght(){
        return array.length;
    }

    public int hash1(int num){
        return num % array.length;
    }

    public int hash2(int num){
        return (num % (array.length -1)) +1;
    }

    public int probe(int j, int h2, int m){
        return (j + h2) % m;
    }

    public int pushDoubleHash(int num){
        int h1 = hash1(num);
        if(array[h1] == null){
            array[h1] = num;
            return h1;
        }
        collisions++;
        int h2 = hash2(num);
        int j = h1;
        for(int i = 0; i < array.length; i++){
            j = probe(j,h2, array.length);
            if(array[j] == null){
                array[j] = num;
                return j;
            }
            collisions++;
        }
        return -1;
    }

    public int getElementsAmount(){
        int sum = 0;

        for(Integer i : array){
            if(i != null){
                sum += 1;
            }
        }
        return sum;
    }

    public int pushLinear(int num) {
        int h1 = hash1(num);
        if(array[h1] == null) {
            array[h1] = num;
            return h1;
        }
        collisions++;
        int j = h1;
        for(int i = 0; i < array.length; i++) {
            // Linear probing by simply adding 1 (i.e., j + 1)
            j = probe(j, 1, array.length);
            if(array[j] == null) {
                array[j] = num;
                return j;
            }
            collisions++;
        }
        return -1;
    }

    public static int nextPrime(int n){
        if(n % 2 == 0){
            n++;
        }
        while(!isPrime(n)){
            n += 2;
        }
        return n;
    }

    public static boolean isPrime(int n){
        for(int i = 3; i * i <= n; i += 2){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }

    public static int getOptimizedSize(int loadFactor, int elements) {

        // Convert the load factor to a decimal. For example, 50 becomes 0.5.
        double loadFactorDecimal = loadFactor / 100.0;

        // Calculate the size so that elements/size = loadFactorDecimal.
        // Therefore, size = elements / loadFactorDecimal.
        int size = (int) Math.ceil(elements / loadFactorDecimal);

        // Get the next prime number that is greater than or equal to this size.
        return nextPrime(size);
    }



    public static void main(String[] args){
        ArrayList<Integer> list = new ArrayList<>();
        Random random = new Random();

        long start;
        long end;
        int[] percentages = {50, 80, 90, 99, 100};

        for(int i = 0; i < 10000000; i++){
            list.add(random.nextInt(100000000));
        }

        for(int percent : percentages) {
            int elements = list.size();
            int optimalSize = getOptimizedSize(percent, 10000000);

            System.out.println("Optimal size for " + percent + "% load: " + optimalSize);
            HashArray2 hashArrayLinear = new HashArray2(optimalSize);
            HashArray2 hashArrayDoubleHash = new HashArray2(optimalSize);

            // Linear Probing
            start = System.nanoTime();
            for(int i = 0; i < elements; i++) {
                hashArrayLinear.pushLinear(list.get(i));
            }
            end = System.nanoTime();
            System.out.println("Linear Probing with " + percent + "% load:");
            System.out.println("Time: " + (end-start)/1000000 + " ms");
            System.out.println("Collisions per element: " + (double)hashArrayLinear.collisions/elements);

            // Double Hashing
            start = System.nanoTime();
            for(int i = 0; i < elements; i++) {
                hashArrayDoubleHash.pushDoubleHash(list.get(i));
            }
            end = System.nanoTime();
            System.out.println("Double Hashing with " + percent + "% load:");
            System.out.println("Time: " + (end-start)/1000000 + " ms");
            System.out.println("Collisions per element: " + (double)hashArrayDoubleHash.collisions/elements);

            System.out.println("---------------------");
        }

        HashMap<Integer,Integer> map = new HashMap<>();

        start = System.nanoTime();
        for(int i : list){
            map.put(i,i);
        }
        end = System.nanoTime();

        System.out.println("time java hashmap: " + (end-start)/1000000);


    }

}
