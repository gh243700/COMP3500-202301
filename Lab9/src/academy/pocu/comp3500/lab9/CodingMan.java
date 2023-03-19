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
            VideoClip vc = clips[index];

            if (vc.getStartTime() > startTime || index == clips.length - 1) {
                if (temp.getStartTime() > startTime) {
                    return -1;
                }

                if (!(vc.getEndTime() >= temp.getEndTime() && temp.getEndTime() >= vc.getStartTime() && vc.getStartTime() <= startTime)) {
                    System.out.println(temp.getStartTime() + "  :  " + temp.getEndTime());
                    startTime = temp.getEndTime();

                    ++count;
                }

                temp = vc;

                if (index == clips.length - 1) {
                    if (startTime >= time) {
                        break;
                    }
                    if (temp.getStartTime() <= startTime) {
                        startTime = temp.getEndTime();
                        ++count;
                    }
                }

            } else if (vc.getStartTime() <= startTime) {
                if (temp.getEndTime() < vc.getEndTime()) {
                    temp = vc;
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