package idv.funnybrain.plurkchat;

import idv.funnybrain.plurkchat.data.Me;

/**
 * Created by freeman on 2014/7/16.
 */
public class DataCentral {
    public static final String IMAGE_CACHE_DIR = "thumbnails";

    private static DataCentral mData;

    private PlurkOAuth mPlurkOAuth;
    private Me mMe;

    public static DataCentral getInstance() {
        if (mData == null) {
            mData = new DataCentral();
        }

        return mData;
    }

    private void DataCentral() {

    }

    public void setPlurkOAuth(PlurkOAuth plurkOAuth) {
        mPlurkOAuth = plurkOAuth;
    }

    public PlurkOAuth getPlurkOAuth() {
        return mPlurkOAuth;
    }

    public void setMe(Me me) {
        mMe = me;
    }

    public Me getMe() {
        return mMe;
    }
}
