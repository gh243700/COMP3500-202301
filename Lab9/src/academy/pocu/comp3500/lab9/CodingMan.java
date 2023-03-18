package academy.pocu.comp3500.lab9;

import academy.pocu.comp3500.lab9.data.VideoClip;

public class CodingMan {
    public static int findMinClipsCount(final VideoClip[] clips, int time) {
        sort(clips);



        return 0;
    }

    public static void sort(final VideoClip[] clips) {
        for (int i = 0; i < clips.length; ++i) {
            for (int k = 0; k < clips.length - i - 1; ++k) {
                if (clips[k].getEndTime() > clips[k + 1].getEndTime()) {
                    VideoClip temp = clips[k];
                    clips[k] = clips[k + 1];
                    clips[k + 1] = temp;
                } else if (clips[k].getEndTime() == clips[k + 1].getEndTime()) {
                    if (clips[k].getStartTime() < clips[k + 1].getStartTime()) {
                        VideoClip temp = clips[k];
                        clips[k] = clips[k + 1];
                        clips[k + 1] = temp;
                    }
                }
            }
        }
    }
}