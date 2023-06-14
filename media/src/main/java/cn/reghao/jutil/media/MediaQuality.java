package cn.reghao.jutil.media;

/**
 * 视频质量
 *
 * @author reghao
 * @date 2022-08-05 10:06:08
 */
public class MediaQuality {
    /**
     * @param
     * @return
     * @date 2022-08-18 下午2:25
     */
    public static MediaResolution getQuality(int width, int height) {
        boolean horizontal = width > height;
        MediaResolution[] arr = MediaResolution.values();
        MediaResolution resolution = arr[0];
        int currentAbs;
        if (horizontal) {
            currentAbs = Math.abs(height-arr[0].getHeight());
            for (int i = 1; i < arr.length; i++) {
                int currentAbs1 = Math.abs(height-arr[i].getHeight());
                if (currentAbs1 < currentAbs) {
                    currentAbs = currentAbs1;
                    resolution = arr[i];
                }
            }
        } else {
            currentAbs = Math.abs(width-arr[0].getHeight());
            for (int i = 1; i < arr.length; i++) {
                int currentAbs1 = Math.abs(width-arr[i].getHeight());
                if (currentAbs1 < currentAbs) {
                    currentAbs = currentAbs1;
                    resolution = arr[i];
                }
            }
        }
        return resolution;
    }
}
