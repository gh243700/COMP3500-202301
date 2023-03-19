package academy.pocu.comp3500.lab9.app;

import academy.pocu.comp3500.lab9.CodingMan;
import academy.pocu.comp3500.lab9.ProfitCalculator;
import academy.pocu.comp3500.lab9.PyramidBuilder;
import academy.pocu.comp3500.lab9.data.Task;
import academy.pocu.comp3500.lab9.data.VideoClip;

public class Program {

    public static void main(String[] args) {
        // write your code here

        {
            VideoClip[] clips = new VideoClip[]{
                    new VideoClip(0, 7),
                    new VideoClip(0, 15),
                    new VideoClip(10, 20),
                    new VideoClip(15, 25),
                    new VideoClip(20, 26),
                    new VideoClip(23, 30),
                    new VideoClip(24, 31),
                    new VideoClip(25, 32),
                    new VideoClip(27, 32),
                    new VideoClip(28, 32),
                    new VideoClip(25, 33),
                    new VideoClip(29, 33),
                    new VideoClip(30, 35),
                    new VideoClip(33, 35),
                    new VideoClip(31, 40),
                    new VideoClip(40, 50),
            };
            int airTime = 35;

            int count = CodingMan.findMinClipsCount(clips, airTime);

            assert (count == 4);
        }

        {
            int pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{25}, 2);
            assert (pyramidHeight == 0);
            pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{1, 1, 1, 1, 1, 1}, 2);
            assert (pyramidHeight == 1);
            pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{3, 3}, 2);
            assert (pyramidHeight == 1);
            pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{3, 3, 3, 3}, 2);
            assert (pyramidHeight == 1);
            pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{3, 3, 3, 3, 3}, 2);
            assert (pyramidHeight == 2);

        }

        {
            int pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{2, 2, 2, 2, 3, 3}, 1);
            assert (pyramidHeight == 2);

        }

        {
            int[] widths = {5};
            int result = PyramidBuilder.findMaxHeight(widths, 10);
            assert (result == 0);
        }
        {
            int[] widths = {6, 8};
            int result = PyramidBuilder.findMaxHeight(widths, 10);
            assert (result == 1);
        }
        {
            int[] widths = {3, 3, 4, 4, 30, 12, 10, 10, 6};
            int result = PyramidBuilder.findMaxHeight(widths, 5);
            assert (result == 3);
        }

        {
            int[] widths = {3, 3, 3, 3, 3, 3, 3, 3};
            int result = PyramidBuilder.findMaxHeight(widths, 5);
            assert (result == 2);
        }

        {
            int[] widths = {3, 3, 3, 3, 3, 3, 3, 3, 3};
            int result = PyramidBuilder.findMaxHeight(widths, 5);
            assert (result == 3);
        }

        {
            int[] widths = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
            int result = PyramidBuilder.findMaxHeight(widths, 5);
            assert (result == 3);
        }

        {
            Task[] tasks = new Task[]{
                    new Task(20, 30),
                    new Task(30, 40),
                    new Task(10, 35)
            };

            int profit = ProfitCalculator.findMaxProfit(tasks, new int[]{10}); // 35
            assert (profit == 35);
            profit = ProfitCalculator.findMaxProfit(tasks, new int[]{20, 25}); // 70
            assert (profit == 70);
            profit = ProfitCalculator.findMaxProfit(tasks, new int[]{40, 15, 5}); // 75
            assert (profit == 75);
        }
        {
            {

                // PyramidBuilder
                int pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{3}, 2);

                assert (pyramidHeight == 0);

                pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{5, 5}, 10);

                assert (pyramidHeight == 0);

                pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{5, 5}, 9);

                assert (pyramidHeight == 1);

                pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{5, 4, 6}, 8);

                assert (pyramidHeight == 1);

                pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{5, 6, 8, 10, 12, 16, 16}, 17);

                assert (pyramidHeight == 2);

                pyramidHeight = PyramidBuilder.findMaxHeight(new int[]{60, 40, 20, 16, 16, 12, 10, 8, 6, 5}, 10);

                assert (pyramidHeight == 3);

                // ProfitCalculator
                Task[] tasks = new Task[]{
                        new Task(20, 30),
                };
                int[] skillLevels = new int[]{20};

                int profit = ProfitCalculator.findMaxProfit(tasks, skillLevels);

                assert (profit == 30);

                tasks = new Task[]{
                        new Task(20, 30),
                };
                skillLevels = new int[]{10};

                profit = ProfitCalculator.findMaxProfit(tasks, skillLevels);

                assert (profit == 0);

                tasks = new Task[]{
                        new Task(20, 50),
                        new Task(20, 40)
                };
                skillLevels = new int[]{25};

                profit = ProfitCalculator.findMaxProfit(tasks, skillLevels);

                assert (profit == 50);

                tasks = new Task[]{
                        new Task(20, 40),
                        new Task(30, 40),
                        new Task(50, 25),
                        new Task(60, 45)
                };
                skillLevels = new int[]{10, 20, 35, 70, 45};

                profit = ProfitCalculator.findMaxProfit(tasks, skillLevels);

                assert (profit == 165);

                // CodingMan
                VideoClip[] clips = new VideoClip[]{
                        new VideoClip(0, 10),
                };
                int airTime = 10;

                int count = CodingMan.findMinClipsCount(clips, airTime);

                assert (count == 1);

                clips = new VideoClip[]{
                        new VideoClip(30, 60),
                        new VideoClip(0, 20)
                };
                airTime = 60;

                count = CodingMan.findMinClipsCount(clips, airTime);

                assert (count == -1);

                clips = new VideoClip[]{
                        new VideoClip(0, 5),
                        new VideoClip(0, 20),
                        new VideoClip(5, 30),
                        new VideoClip(25, 35),
                        new VideoClip(35, 70),
                        new VideoClip(50, 75)
                };
                airTime = 60;

                count = CodingMan.findMinClipsCount(clips, airTime);

                assert (count == 4);
            }
        }

        {
            {
                VideoClip[] clips = new VideoClip[]{
                        new VideoClip(0, 15),
                        new VideoClip(10, 20),
                        new VideoClip(30, 35)
                };

                int count = CodingMan.findMinClipsCount(clips, 10); // 1
                assert (count == 1);
                count = CodingMan.findMinClipsCount(clips, 20); // 2
                assert (count == 2);
                count = CodingMan.findMinClipsCount(clips, 25); // -1
                assert (count == -1);
                count = CodingMan.findMinClipsCount(clips, 35); // -1
                assert (count == -1);
            }
        }


        {
            {
                VideoClip[] clips = new VideoClip[]{
                        new VideoClip(0, 15),
                        new VideoClip(15, 20),
                        new VideoClip(20, 35)
                };

                int count = CodingMan.findMinClipsCount(clips, 35); // 1
                assert (count == 3);
            }
        }


        {
            {
                VideoClip[] clips = new VideoClip[]{
                        new VideoClip(0, 15),
                        new VideoClip(15, 35),
                        new VideoClip(0, 13),
                        new VideoClip(15, 35)
                };

                int count = CodingMan.findMinClipsCount(clips, 35); // 1
                assert (count == 2);
            }
        }


        System.out.println("--------");
    }
}
