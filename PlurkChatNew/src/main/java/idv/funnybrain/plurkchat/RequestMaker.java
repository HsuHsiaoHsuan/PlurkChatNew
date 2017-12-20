package idv.funnybrain.plurkchat;

import android.util.Log;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static idv.funnybrain.plurkchat.BuildConfig.DEBUG;

public class RequestMaker {
    private static final String TAG = "RequestMaker";

    private PlurkOAuth plurkOAuth;
    private String url;
    private Map<String, String> args;

    public RequestMaker(PlurkOAuth oauth, String api_url) {
        plurkOAuth = oauth;
        url = api_url;
    }

    public RequestMaker args(Map<String, String> pairs) {
        args = pairs;
        if (DEBUG) {
            if (pairs != null) {
                StringBuilder output = new StringBuilder("args, httpPost parameters: ");
                pairs.forEach((k, v) -> output.append(k + ", " + v + "\n"));
                Logger.d(output.append(output.toString()));
            } else {
                Logger.d("args, httpPost parameters: null");
            }
        }
        return this;
    }

    protected String requestResult() throws RequestException {
        String result;
        if(args == null) {
            result = plurkOAuth.sendRequest(url);
        } else {
            result = plurkOAuth.sendRequest(url, args);
        }
        if(DEBUG) { Log.d(TAG, "requestResult(): " + result); }
        return result;
    }

    public JSONObject getJSONObjectResult() throws RequestException {
        try {
            return new JSONObject(requestResult());
        } catch (Exception e) {
            throw new RequestException(e);
        }
    }

    public JSONArray getJSONArrayResult() throws RequestException {
        try {
            return new JSONArray(requestResult());
        } catch(Exception e) {
            throw new RequestException(e);
        }
    }

    public String getStringResult() throws RequestException {
        return requestResult();
    }
}
