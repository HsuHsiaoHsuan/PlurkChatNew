package idv.funnybrain.plurkchat;

import android.content.Context;
//import android.text.TextUtils;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.Volley;
import idv.funnybrain.plurkchat.data.Me;

/**
 * Created by freeman on 2014/7/16.
 */
public class DataCentral {
    private static final String TAG = "DataCentral";

    public static final String IMAGE_CACHE_DIR = "thumbnails";

    private Context mContext;

    private static DataCentral mData;

    private PlurkOAuth mPlurkOAuth;
    private Me mMe;

    // private RequestQueue mRequestQueue;

    public static synchronized DataCentral getInstance(Context context) {
        if (mData == null) {
            mData = new DataCentral(context);
        }

        return mData;
    }

    public DataCentral(Context context) {
        mContext = context;
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

//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(mContext);
//        }
//
//        return mRequestQueue;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//
//        VolleyLog.d("Adding request to queue: %s", req.getUrl());
//
//        getRequestQueue().add(req);
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//
//        getRequestQueue().add(req);
//    }
}
