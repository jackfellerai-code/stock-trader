import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("tiny.txt"));
        String symbol = in.next();
        int n = in.nextInt();
        int[] years = new int[n];
        int[] months = new int[n];
        double[] prices = new double[n];
        loadData(in, years, months, prices);
        printData(years, months, prices);

        int lowestIndex = indexOfLowest(prices);
        System.out.printf("Lowest:  $%.2f in %s %d\n",
            prices[lowestIndex], monthName(months[lowestIndex]), years[lowestIndex]);

        int highestIndex = indexOfHighest(prices);
        System.out.printf("Highest: $%.2f in %s %d\n",
            prices[highestIndex], monthName(months[highestIndex]), years[highestIndex]);
    }

    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {
        String[] nm = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return nm[month - 1];
    }

    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            System.out.printf(monthName(months[i]) + " " + (years[i]) + ":"
                    + " "
                    + "$"
                    + "%.2f"
                    + "\n",
                prices[i]);
        }
    }

    public static int indexOfLowest(double[] prices) {
        int index = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[index] > prices[i]) {
                index = i;
            }
        }
        return index;
        // TINY lowest:  $30.00 in Mar 2020
    }

    public static int indexOfHighest(double[] prices) {
        int index = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[index] < prices[i]) {
                index = i;
            }
        }
        return index;
        // TINY highest: $70.00 in Jul 2020
    }
}
