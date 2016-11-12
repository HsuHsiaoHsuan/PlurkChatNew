package idv.funnybrain.plurkchat;

import com.orhanobut.logger.Logger;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RequestMaker {
    private static final boolean D = BuildConfig.DEBUG;
    private static final String TAG = "RequestMaker";

    private PlurkOAuth plurkOAuth;
    private String url;
    private List<NameValuePair> args;

    public RequestMaker(PlurkOAuth oauth, String api_url) {
        plurkOAuth = oauth;
        url = api_url;
    }

    public RequestMaker args(List<NameValuePair> pairs) {
        args = pairs;
        return this;
    }

    protected String requestResult() throws RequestException {
        String result;
        if(args == null) {
            result = plurkOAuth.sendRequest(url);
        } else {
            result = plurkOAuth.sendRequest(url, args);
        }
        if(D) { Logger.json(result); }
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
