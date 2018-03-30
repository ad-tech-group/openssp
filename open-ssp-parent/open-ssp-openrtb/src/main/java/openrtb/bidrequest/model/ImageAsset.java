package openrtb.bidrequest.model;

import java.util.List;

public final class ImageAsset implements Cloneable {
    public static int IMAGE_ASSET_TYPES_ICON = 1;
    public static int IMAGE_ASSET_TYPES_LOGO = 2;
    public static int IMAGE_ASSET_TYPES_MAIN = 3;

    private int typeX;
    private int wX;
    private int wminX;
    private int hX;
    private int hminX;
    private List<String> mimesX;

}
