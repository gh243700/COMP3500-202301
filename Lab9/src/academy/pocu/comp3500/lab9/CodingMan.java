package academy.pocu.comp3500.lab9;

import academy.pocu.comp3500.lab9.data.VideoClip;

public class CodingMan {
    public static int findMinClipsCount(final VideoClip[] clips, int time) {
        sort(clips);

        int startTime = 0;

        if (clips.length == 0) {
            return -1;
        }

        if (clips.length == 1) {
            VideoClip videoClip = clips[0];
            return (videoClip.getStartTime() == 0 && videoClip.getEndTime() >= time) ? 1 : -1;
        }

        VideoClip temp = clips[0];
        int index = 1;
        int count = 0;

        while (index < clips.length) {
            if (startTime >= time) {
                break;
            }
            VideoClip videoClip = clips[index];

            if (videoClip.getStartTime() > startTime || index == clips.length - 1) {
                if (temp.getStartTime() > startTime) {
                    return -1;
                }
                System.out.println(temp.getStartTime() + "  :  " + temp.getEndTime());
                startTime = temp.getEndTime();
                temp = videoClip;
                ++count;
                if (index == clips.length - 1) {
                    if (startTime >= time) {
                        break;
                    }
                    if (temp.getStartTime() <= startTime) {
                        startTime = temp.getEndTime();
                        ++count;
                    }
                }

            } else if (videoClip.getStartTime() <= startTime) {
                if (temp.getEndTime() < videoClip.getEndTime()) {
                    temp = videoClip;
                }
            }

            ++index;
        }


        return startTime >= time ? count : -1;
    }

    public static void sort(final VideoClip[] clips) {
        for (int i = 0; i < clips.length; ++i) {
            for (int k = 0; k < clips.length - i - 1; ++k) {
                if (clips[k].getStartTime() > clips[k + 1].getStartTime()) {
                    VideoClip temp = clips[k];
                    clips[k] = clips[k + 1];
                    clips[k + 1] = temp;
                } else if (clips[k].getStartTime() == clips[k + 1].getStartTime()) {
                    if (clips[k].getEndTime() < clips[k + 1].getEndTime()) {
                        VideoClip temp = clips[k];
                        clips[k] = clips[k + 1];
                        clips[k + 1] = temp;
                    }
                }
            }
        }
    }
}