package openrtb.bidrequest.model;

import java.util.List;

public final class NativeAsset implements Cloneable {
    public static int NATIVE_ADD_UNIT_PAID_SEARCH_UNITS = 1;
    public static int NATIVE_ADD_UNIT_RECOMMENDATION_WIDGETS = 2;
    public static int NATIVE_ADD_UNIT_PROMOTED_LISTINGS = 3;
    public static int NATIVE_ADD_UNIT_IN_AD_WITH_NATIVE_ELEMENT_UNITS = 4;
    public static int NATIVE_ADD_UNIT_CUSTOM_CAN_NOT_BE_CONTAINED = 5;
    public static int NATIVE_ADD_UNIT_IN_FEED = 501;
    public static int NATIVE_ADD_UNIT_END_OF_POST = 502;
    public static int NATIVE_ADD_UNIT_IN_ARTICLE = 503;
    public static int NATIVE_ADD_UNIT_IN_IMAGE = 504;

    public static int NATIVE_LAYOUTS_CONTENT_WALL = 1;
    public static int NATIVE_LAYOUTS_APP_WALL = 2;
    public static int NATIVE_LAYOUTS_NEWS_FEED = 3;
    public static int NATIVE_LAYOUTS_CHAT_LIST = 4;
    public static int NATIVE_LAYOUTS_CAROUSEL = 5;
    public static int NATIVE_LAYOUTS_CONTENT_STREAM = 6;
    public static int NATIVE_LAYOUTS_GRID_ADJOINING_THE_CONTENT = 7;

    private NativeRequestMarkup requestX;
    private List<Integer> battrX;

}
