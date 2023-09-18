import java.util.Arrays;
import java.util.Random;

public class task1 {

    public static void main(String[] args) {

        int[] priceChange = {-1, 3, -9, 2, 2, -1, 2, -1, -5};

        int[] generatedPriceChange = fillRandomNumbers(new int[100000]);


        long startTime = System.nanoTime();

        String result = getMaxProfit(getPrices(generatedPriceChange));

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;


        System.out.println(result);
        System.out.println(timeElapsed / 1000000 + "ms");


    }

    public static int[] fillRandomNumbers(int[] array){
        Random random = new Random();

        for(int i = 0; i< array.length; i++){
            array[i] = random.nextInt(20)-10;
        }
        return array;
    }

    public static int[] getPrices(int[] priceChange){
        int price = 0;
        int[] prices = new int[priceChange.length];
        for(int i = 0; i < priceChange.length ; i++){
            price += priceChange[i];
            prices[i] = price;
        }
        return prices;
    }


    public static String getMaxProfit(int[] prices){

        int maxProfit = -1;
        int buyDay = 0;
        int sellDay = 0;
        for(int i = 0; i < prices.length; i++){

            for (int j = i; j < prices.length; j++){
                if((prices[j]-prices[i]) > maxProfit){
                    buyDay = i;
                    sellDay = j;
                    maxProfit = prices[j]-prices[i];
                }

            }


        }

        sellDay++;
        buyDay++;
        return "Buyday: " + buyDay + " Sellday: " + sellDay + " Profit: " + maxProfit;

    }


}
