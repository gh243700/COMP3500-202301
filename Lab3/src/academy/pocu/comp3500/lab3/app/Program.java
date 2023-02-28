package academy.pocu.comp3500.lab3.app;

import academy.pocu.comp3500.lab3.MissionControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Program {

    public static void main(String[] args) {


        // write your code here
        {
            for (int k = 0; k < 100; ++k) {
                int[] altitude = new int[100];

                for (int i = 0; i < k; ++i) {
                    altitude[i] = i + 1;
                }

                for (int i = 0; i < 100 - k; ++i) {
                    altitude[k + i] = k - i - 1;
                }

                for (int i = 0; i < altitude.length; ++i) {
                    ArrayList<Integer> range = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                    ArrayList<Integer> expected = new ArrayList<>();

                    Collections.sort(range);

                    for (int j = 0; j < altitude.length; ++j) {
                        if (altitude[j] == altitude[i]) {
                            expected.add(j);
                        }
                    }

                    Collections.sort(expected);

                    assert (expected.size() <= 2);
                    assert (expected.size() == range.size());

                    for (int j = 0; j < range.size(); ++j) {
                        assert (range.get(j) == expected.get(j));
                    }

                }
            }
        }


        {
            int[] altitude = {1};

            ArrayList<Integer> result;
            for (int i = 0; i < altitude.length; ++i) {
                result = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                assert (result.size() == 1);
                assert (result.get(0) == i);
            }
        }

        {
            int[] altitude = {1, 2};

            ArrayList<Integer> result;
            for (int i = 0; i < altitude.length; ++i) {
                result = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                assert (result.size() == 1);
                assert (result.get(0) == i);
            }
        }

        {
            int[] altitude = {1, 2, 3};

            ArrayList<Integer> result;
            for (int i = 0; i < altitude.length; ++i) {
                result = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                assert (result.size() == 1);
                assert (result.get(0) == i);
            }
        }

        {
            int[] altitude = {17, 16, 15};

            ArrayList<Integer> result;
            for (int i = 0; i < altitude.length; ++i) {
                result = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                assert (result.size() == 1);
                assert (result.get(0) == i);
            }
        }



        {
            int[] altitude = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};

            ArrayList<Integer> result;
            for (int i = 0; i < altitude.length; ++i) {
                result = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                assert (result.size() == 1);
                assert (result.get(0) == i);
            }
        }

        {
            int[] altitude = {17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

            ArrayList<Integer> result;
            for (int i = 0; i < altitude.length; ++i) {
                result = MissionControl.findAltitudeTimes(altitude, altitude[i]);
                assert (result.size() == 1);
                assert (result.get(0) == i);
            }
        }


        {
            int[] altitude = {1, 2, 3};

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == 3);
        }

        {
            int[] altitude = {1, 2, 1};

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == 2);
        }

        {
            int[] altitude = {2, 1, 0};

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == 2);
        }

        {
            int[] altitude = {2, 1};

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == 2);
        }

        {
            int[] altitude = {1, 2};

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == 2);
        }

        {
            int[] altitude = {1};

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == 1);
        }

        {
            int[] altitude = new int[100];

            for (int i = 0; i < 90; ++i) {
                altitude[i] = i + 1;
            }

            for (int i = 0; i < 10; ++i) {
                altitude[90 + i] = 90 - i - 1;
            }

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == altitude[89]);
        }

        {
            int[] altitude = new int[100];

            for (int i = 0; i < 99; ++i) {
                altitude[i] = i + 1;
            }

            for (int i = 0; i < 1; ++i) {
                altitude[99 + i] = 99 - i - 1;
            }

            int index = MissionControl.findMaxAltitudeTime(altitude);
            assert (altitude[index] == altitude[98]);
        }

        {
            for (int k = 0; k < 100; ++k) {
                int[] altitude = new int[100];

                for (int i = 0; i < k; ++i) {
                    altitude[i] = i + 1;
                }

                for (int i = 0; i < 100 - k; ++i) {
                    altitude[k + i] = k - i - 1;
                }

                int index = MissionControl.findMaxAltitudeTime(altitude);
                int maxIndex = 0;
                for (int i = 0; i < altitude.length; ++i) {
                    if (altitude[i] > altitude[maxIndex]) {
                        maxIndex = i;
                    }
                }

                assert (index == maxIndex);
            }
        }

        {
            final int[] altitudes = new int[]{1, 2, 3, 4, 5, 6, 7, 4, 3, 2};

            ArrayList<Integer> bounds = MissionControl.findAltitudeTimes(altitudes, 2); // bounds: [ 1, 9 ]
            assert (bounds.size() == 2);
            assert (bounds.get(0) == 1);
            assert (bounds.get(1) == 9);

            bounds = MissionControl.findAltitudeTimes(altitudes, 5); // bounds: [ 4 ]
            assert (bounds.size() == 1);
            assert (bounds.get(0) == 4);
        }

    }
}
