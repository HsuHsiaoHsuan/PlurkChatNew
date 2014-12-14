package idv.funnybrain.plurkchat;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Freeman on 2014/4/3.
 */
public class RequestMaker {
    private static final boolean D = true;
    private static final String TAG = "RequestMaker";

    private PlurkOAuth plurkOAuth;
    private String url;
    private List<NameValuePair> args;

    public RequestMaker(PlurkOAuth oauth, String api_url) {
        plurkOAuth = oauth;
        url = api_url;
    }

    public RequestMaker args(List<NameValuePair> pairs) {
        try {
            args = pairs;
            if(D && (pairs!=null) ) { Log.d(TAG, "args, httpPost parameters: " + EntityUtils.toString(new UrlEncodedFormEntity(pairs, HTTP.UTF_8))); }
            if(D && (pairs==null) ) { Log.d(TAG, "args, httpPost parameters: null"); }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        if(D) { Log.d(TAG, "requestResult(): " + result); }
        return result;
    }

    public JSONObject getJSONObjectResult() throws RequestException {
        try {
            String tmp = requestResult();
            return new JSONObject(tmp);
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
