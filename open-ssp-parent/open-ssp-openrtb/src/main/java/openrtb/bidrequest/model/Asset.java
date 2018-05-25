package openrtb.bidrequest.model;

import java.util.List;

public final class Asset implements Cloneable {
    public static final int REQUIRED_NO = 0;
    public static final int REQUIRED_YES = 1;

    private int idX;
    private int requiredX=REQUIRED_NO;
    private TitleAsset titleX;
    private DataAsset dataX;
    private ImageAsset imgX;
    private VideoAsset videoX;

}
