package academy.pocu.comp3500.assignment2.app;

import academy.pocu.comp3500.assignment2.Indent;
import academy.pocu.comp3500.assignment2.Logger;
import academy.pocu.comp3500.assignment2.datastructure.Sort;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static academy.pocu.comp3500.assignment2.Logger.log;

public class Program {
    public static void main(String[] args) throws IOException {
        {
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter("coderTest.txt"));
                Indent level1 = Logger.indent();
                {
                    log("Richard the Third");
                    log("Best bib and tucker");
                    Indent level2 = Logger.indent();
                    {
                        log("Which witch is which?");
                    }
                    Logger.unindent();
                    log("You can lead a horse to water but you can't make it drink");
                    log("Chinless wonder");
                    log("(In the) nick of time");
                    log("Double entendre");
                }
                Logger.unindent();
                log("Cut off without a penny");
                log("Let not poor Nelly starve");
                level1.discard();
                Logger.printTo(writer);
                writer.close();
            }

            Logger.clear();
        }


        {

            BufferedWriter writer = new BufferedWriter(new FileWriter("mylog1.log"));

            int[] nums = new int[]{1, 2, 3, 4};

            log("call sum()");
            int sum = sum(nums);

            log("call average()");
            double average = calculateAverage(nums);

            Logger.printTo(writer);

            Logger.clear();
        }
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("mylog2.log"));

            log("first level 1");

            Logger.indent();
            {
                log("second level 1");
                log("second level 2");

                doMagic();

                log("second level 3");
            }
            Logger.unindent();
            Logger.unindent();
            Logger.unindent();


            log("first level 2");
            Logger.printTo(writer);


            Logger.clear();
        }

        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("mylog3.log"));

            int x = 10;

            log("first level 1");

            Indent indent = Logger.indent();
            {
                log("second level 1");
                log("second level 2");

                if (x % 2 == 0) {
                    indent.discard();
                }
            }
            Logger.unindent();

            log("first level 2");
            Logger.printTo(writer);


            Logger.clear();

            System.out.println();
        }

        {
            try {
                final BufferedWriter writer1 = new BufferedWriter(new FileWriter("quicksort1.log"));

                int[] nums = new int[]{30, 10, 80, 90, 50, 70, 40};

                Sort.quickSort(nums);

                Logger.printTo(writer1);
            } catch (Exception e) {
                System.exit(-1);
            }


            Logger.clear();
        }


        {
            final BufferedWriter writer2 = new BufferedWriter(new FileWriter("quicksort2.log"));

            int[] nums = new int[]{30, 10, 80, 90, 50, 70, 40};

            Sort.quickSort(nums);

            Logger.printTo(writer2, "90");

            Logger.clear();
//
/*
30 10 80 90 50 70 40
  R: 90 50 70 80
    R: 90
    X: 50 70 80 90
  X: 10 30 40 50 70 80 90
*/
        }


        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("mylog1.log"));

            log("hello");
            log("world");
            log("this is logging at the top level");

            Logger.indent();
            {
                log("using indent, you can indent to organize your logs");
                log("call unindent() to decrease the indentation level");
            }
            Logger.unindent();

            Indent indent = Logger.indent();
            {
                log("whatever I say here");
                log("is discarded!");
                log("too bad!");

                indent.discard();
            }
            Logger.unindent();

            Logger.indent();
            {
                log("this won't be discarded");
                log("it's true!");

                doMagic();
            }
            Logger.unindent();

            log("back to the top level!");
            log("and let's print the logs");

            Logger.printTo(writer);

            Logger.clear();

            log("log was just cleared");
            log("so you start logging from the top level again");

            Logger.printTo(writer);

            writer.close();
        }

        Logger.clear();

        {
            final BufferedWriter writer1 = new BufferedWriter(new FileWriter("quicksort1.log"));
            final BufferedWriter writer2 = new BufferedWriter(new FileWriter("quicksort2.log"));

            int[] nums = new int[]{30, 10, 80, 90, 50, 70, 40};

            Sort.quickSort(nums);

            Logger.printTo(writer1);

            Logger.printTo(writer2, "90");

            writer1.close();
            writer2.close();
        }

    }

    public static void doMagic() {
        Logger.indent();
        {
            log("third level 1");
            log("third level 2");
        }
        Logger.unindent();
    }

    private static int sum(int[] nums) {
        int sum = 0;
        for (int i = 0; i < nums.length; ++i) {
            log(String.format("sum + %d", nums[i]));
            sum += nums[i];
            log(String.format("sum: %d", sum));
        }

        log(String.format("return sum: %d", sum));
        return sum;
    }

    private static double calculateAverage(int[] nums) {
        log("call sum()");
        int sum = sum(nums);

        log(String.format("sum / nums.length: %d / %d", sum, nums.length));
        double average = sum / (double) nums.length;

        log(String.format("return average: %f", average));
        return average;
    }


}
