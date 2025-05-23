package academy.pocu.comp3500.lab9;

import java.util.Arrays;

public class PyramidBuilder {
    public static int findMaxHeight(final int[] widths, int statue) {
        sortAsc(widths);

        int iLevelMinWidth = statue + 1;
        int iLevelMinCount = 1;

        int iLevelWidth = 0;
        int iLevelCount = 0;

        int levelHeight = 0;
        int index = 0;

        while (index < widths.length) {
            int n = widths[index];
            iLevelWidth += n;
            ++iLevelCount;

            if (iLevelWidth >= iLevelMinWidth && iLevelCount > iLevelMinCount) {
                ++levelHeight;
                iLevelMinCount = iLevelCount;
                iLevelCount = 0;
                ++index;
                break;
            }

            ++index;
        }


        while (index < widths.length) {
            ++iLevelCount;

            if (iLevelCount > iLevelMinCount) {
                ++levelHeight;
                iLevelMinCount = iLevelCount;
                iLevelCount = 0;
            }

            ++index;
        }

        return levelHeight;
    }

    public static void sortAsc(final int[] widths) {
        Arrays.sort(widths);
    }

}