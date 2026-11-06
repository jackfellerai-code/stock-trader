import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("aapl_2000_2010.txt"));
        String symbol = in.next();
        int n = in.nextInt();
        int[] years = new int[n];
        int[] months = new int[n];
        double[] prices = new double[n];
        loadData(in, years, months, prices);
        printData(years, months, prices);

        int low = indexOfLowest(prices);
        int high = indexOfHighest(prices);
        System.out.printf("%s lowest: $%.2f in %s %d%n", symbol, prices[low], monthName(months[low]), years[low]);
        System.out.printf("%s highest: $%.2f in %s %d%n", symbol, prices[high], monthName(months[high]), years[high]);

        double result = simulate(years, months, prices);
        System.out.printf("Final value: $%,.2f%n", result);
    }

    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {
        String[] nm = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return nm[month - 1];
    }

    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            System.out.printf("%s %d: $%.2f%n", monthName(months[i]), years[i], prices[i]);
        }
    }

    public static int indexOfLowest(double[] prices) {
        int index = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < prices[index]) {
                index = i;
            }
        }
        return index;
    }

    public static int indexOfHighest(double[] prices) {
        int index = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] > prices[index]) {
                index = i;
            }
        }
        return index;
    }

    public static double simulate(int[] years, int[] months, double[] prices) {
        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;

        for (int i = 0; i < prices.length; i++) {
            double[] history = Arrays.copyOf(prices, i + 1);
            String action = decide(history, shares > 0, boughtAt);

            if (action.equals("BUY") && shares == 0) {
                int canBuy = (int) ((cash - FEE) / prices[i]);
                if (canBuy > 0) {
                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    boughtAt = prices[i];
                    System.out.printf("%s %d: BUY %d shares at $%.2f%n",
                             monthName(months[i]), years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SELL %d shares at $%.2f%n",
                         monthName(months[i]), years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }

    public static String decide(double[] history, boolean holding, double boughtAt) {
        return myStrategy(history, holding, boughtAt);
    }

    public static String buyAndHold(double[] history, boolean holding, double boughtAt) {
        return "BUY";
    }

    public static String dipBuyer(double[] history, boolean holding, double boughtAt) {
        String action = "HOLD";
        int t = history.length - 1;
        if (holding) {
            if (history[t] >= boughtAt * 1.10) {
                action = "SELL";
            }
        } else if (t >= 2 && history[t] < history[t - 1] && history[t - 1] < history[t - 2]) {
            action = "BUY";
        }
        return action;
    }

    public static String movingAverage(double[] history, boolean holding, double boughtAt) {
        return trend(history, holding, 12);
    }

    // My strategy during the first 9 months there isn't enough history, so i'll use the dip buyer.
    // After that, buy when today's price is higher than 9 months ago and sell when it is
    // more than 2% lower. Stays in during uptrends, gets out of long slides, and the 2%
    // cushion stops it selling on tiny dips and paying extra fees.
    public static String myStrategy(double[] history, boolean holding, double boughtAt) {
        String action = "HOLD";
        int t = history.length - 1;
        if (t < 9) {
            action = dipBuyer(history, holding, boughtAt);
        } else {
            double change = history[t] / history[t - 9] - 1;
            if (!holding && change > 0) {
                action = "BUY";
            } else if (holding && change < -0.02) {
                action = "SELL";
            }
        }
        return action;
    }

    public static String trend(double[] history, boolean holding, int months) {
        String action = "HOLD";
        int t = history.length - 1;
        if (history.length >= months) {
            double sum = 0.0;
            for (int i = history.length - months; i < history.length; i++) {
                sum = sum + history[i];
            }
            double avg = sum / months;
            if (!holding && history[t] > avg) {
                action = "BUY";
            } else if (holding && history[t] < avg) {
                action = "SELL";
            }
        }
        return action;
    }
}
