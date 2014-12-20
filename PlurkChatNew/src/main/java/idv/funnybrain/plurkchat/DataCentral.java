package idv.funnybrain.plurkchat;

import android.content.Context;
//import android.text.TextUtils;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.Volley;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.support.v4.util.LruCache;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;
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
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

    public static synchronized DataCentral getInstance(Context context) {
        if (mData == null) {
            mData = new DataCentral(context);
        }

        return mData;
    }

    public DataCentral(Context context) {
        mContext = context;

        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                }
        );

//        HttpStack stack;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            stack = new HurlStack();
//        } else {
//            stack = new HttpClientStack(AndroidHttpClient.newInstance(System.getProperty("http.agent")));
//        }
//        Network network = new BasicNetwork(stack);
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
//        mRequestQueue = new RequestQueue(cache, network);
//        mRequestQueue.start();
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

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            HttpStack stack;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                stack = new HurlStack();
            } else {
                stack = new HttpClientStack(AndroidHttpClient.newInstance(System.getProperty("http.agent")));
            }
            Network network = new BasicNetwork(stack);
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024);
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void clearLruCache() {
        cache.evictAll();
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
