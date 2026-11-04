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
    }



public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
	for (int i = 0; i < years.length; i++) { 
		years[i]=in.nextInt();
		months[i]=in.nextInt();
		prices[i]=in.nextDouble();
	}
}

public static String monthName(int month) {
	String[] nm ={"Jan", "Feb", "Mar", "Apr",
				  "May", "Jun", "Jul", "Aug",
				  "Sep", "Oct", "Nov", "Dec"};
	return   nm [month-1];
}

public static void printData(int[] years, int[] months, double[] prices) {
	for(int i = 0; i < years.length; i++) {
		System.out.printf(monthName(months[i]) + " " + (years[i]) + " " + "%.2f" + "\n", prices[i]);
	}
}


public static int indexOfLowest(double[] prices) {
	int index = 0;
	for (int i = 0; i < prices.length; i++) {
	if (index > prices[i]){
		index = i;
	}
}
	return index;

}

public static int indexOfHighest(double[] prices) {
	int index = 0;
	for (int i = 0; i < prices.length; i++) {
	if (index < prices[i]){
		index = i;
	}
}
	return index;

}
}
