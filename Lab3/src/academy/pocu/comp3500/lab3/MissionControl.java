package academy.pocu.comp3500.lab3;

import java.util.ArrayList;

public final class MissionControl {
    private MissionControl() {
    }

    public static int findMaxAltitudeHelperRecursive(final int[] altitudes, final int front, final int back) {
        final int mid = (front + back) / 2;

        if (mid - 1 < 0) {
            return (altitudes[mid] > altitudes[mid + 1]) ? mid : mid + 1;
        }

        if (mid + 1 >= altitudes.length) {
            return (altitudes[mid] > altitudes[mid - 1]) ? mid : mid - 1;
        }

        boolean leftIsBig = altitudes[mid] < altitudes[mid - 1];
        boolean rightIsBig = altitudes[mid] < altitudes[mid + 1];

        if (leftIsBig && rightIsBig) {
            return (altitudes[front] > altitudes[back] ? front : back);
        }

        if (!leftIsBig && !rightIsBig) {
            return mid;
        }

        if (leftIsBig) {
            return findMaxAltitudeHelperRecursive(altitudes, front, mid - 1);
        }

        return findMaxAltitudeHelperRecursive(altitudes, mid + 1, back);
    }

    public static int findMaxAltitudeTime(final int[] altitudes) {
        if (altitudes.length == 1) {
            return 0;
        }

        return findMaxAltitudeHelperRecursive(altitudes, 0, altitudes.length - 1);
    }

    public static void findAltitudeTimesHelperRecursive(final int[] altitudes, final int targetAltitude, final ArrayList<Integer> bounds, final int front, final int back) {

        final int mid = (front + back) / 2;

        if (front >= back) {
            if (altitudes[front] == targetAltitude) {
                bounds.add(front);
            }
            return;
        }

        if (mid == front) {
            if (altitudes[front] == targetAltitude) {
                bounds.add(front);
            }

            if (altitudes[back] == targetAltitude) {
                bounds.add(back);
            }
            return;
        }

        if (altitudes[mid] == targetAltitude) {
            bounds.add(mid);
        }

        int midMinusFront = altitudes[mid] - altitudes[front];
        int midMinusBack = altitudes[mid] - altitudes[back];

        boolean isLinear = (midMinusBack * midMinusFront < 0);

        if (!isLinear) {
            findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, front, mid - 1);
            findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, mid + 1, back);
            return;
        }

        if (midMinusFront >= 0) {
            if (altitudes[mid] > targetAltitude) {
                findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, front, mid - 1);
                return;
            }
            findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, mid + 1, back);
            return;
        }

        if (altitudes[mid] < targetAltitude) {
            findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, front, mid - 1);
            return;
        }
        findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, mid + 1, back);
    }

    public static ArrayList<Integer> findAltitudeTimes(final int[] altitudes, final int targetAltitude) {
        ArrayList<Integer> bounds = new ArrayList<>();

        findAltitudeTimesHelperRecursive(altitudes, targetAltitude, bounds, 0, altitudes.length - 1);
        return bounds;
    }
}