package academy.pocu.comp3500.lab9;

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
                iLevelMinWidth = iLevelWidth;
                iLevelMinCount = iLevelCount;
                iLevelWidth = 0;
                iLevelCount = 0;
            }

            ++index;
        }

        return levelHeight;
    }
    public static void sortAsc(final int[] widths) {
        for (int i = 0; i < widths.length; ++i) {
            int min = widths[i];
            for (int k = i; k < widths.length - 1; ++k) {
                if (min > widths[k + 1]) {
                    int temp = min;
                    min = widths[k + 1];
                    widths[k + 1] = temp;
                }
            }
        }
    }

}