package academy.pocu.comp3500.lab9;

import academy.pocu.comp3500.lab9.data.Task;

public class ProfitCalculator {
    public static int findMaxProfit(final Task[] tasks, final int[] skillLevels) {
        sortDscByProfit(tasks);

        int profit = 0;
        for (int i = 0; i < skillLevels.length; ++i) {
            int index = 0;
            while (index < tasks.length) {
                Task task = tasks[index];
                if (task.getDifficulty() <= skillLevels[i]) {
                    profit += task.getProfit();
                    break;
                }
                ++index;
            }
        }

        return profit;
    }


    public static void sortDscByProfit(final Task[] tasks) {
        for (int i = 0; i < tasks.length; ++i) {
            int maxTaskIndex = i;
            int maxProfit = tasks[i].getProfit();
            for (int k = i; k < tasks.length - 1; ++k) {
                int profit = tasks[k + 1].getProfit();
                if (maxProfit < profit) {
                    maxTaskIndex = k + 1;
                    maxProfit = profit;
                }
            }
            Task temp = tasks[i];
            tasks[i] = tasks[maxTaskIndex];
            tasks[maxTaskIndex] = temp;
        }
    }
}