package openrtb.bidrequest.model;

public final class DataAsset implements Cloneable {
    public static int DATA_ASSET_TYPES_SPONSORED = 1;
    public static int DATA_ASSET_TYPES_DESC = 2;
    public static int DATA_ASSET_TYPES_RATING = 3;
    public static int DATA_ASSET_TYPES_LIKES = 4;
    public static int DATA_ASSET_TYPES_DOWNLOADS = 5;
    public static int DATA_ASSET_TYPES_PRICE = 6;
    public static int DATA_ASSET_TYPES_SALEPRICE = 7;

    public static int DATA_ASSET_TYPES_PHONE = 8;
    public static int DATA_ASSET_TYPES_ADDRESS = 9;
    public static int DATA_ASSET_TYPES_DESC2 = 10;
    public static int DATA_ASSET_TYPES_DISPLAY_URL = 11;
    public static int DATA_ASSET_TYPES_CTA_TEXT = 12;

    private int typeX;
    private int lenX;

}
